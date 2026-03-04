# 后端开发规则

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| JDK | 25 | Java 运行环境 |
| Spring Boot | 3.5.x | 核心框架 |
| MyBatis Plus | 最新稳定版 | ORM 框架 |
| Maven | 3.9+ | 构建工具 |
| Flyway | 最新 | 数据库迁移 |
| Spring Security | 6.x | 安全框架 |
| SpringDoc | 最新 | API 文档 |
| JUnit 5 | 最新 | 测试框架 |

---

## 项目结构

### 模块结构
```
backend/
├── common/          # 公共模块
├── api/             # API 模块 (对外服务)
├── admin/           # Admin 模块 (管理后台)
├── pom.xml          # 父 POM
└── mvnw             # Maven Wrapper
```

### DDD 分层结构
```
org.mydotey.ai.site/
├── common/                # 公共模块
└── {domain}/              # 领域模块
    ├── controller/        # 接入层
    ├── dto/               # 接入层 - DTO
    ├── command/           # 应用服务层 - 写操作
    ├── query/             # 应用服务层 - 读操作
    ├── job/               # 应用服务层 - 定时任务
    ├── entity/            # 领域层 - 实体
    ├── repository/        # 领域层 - 仓储接口
    ├── enums/             # 领域层 - 枚举
    └── service/           # 领域层 - 领域服务 (可选)

infrastructure/            # 基础设施层
├── persistence/
│   ├── mapper/            # MyBatis Mapper
│   ├── repository/        # Repository 实现
│   └── converter/         # 对象转换器
├── security/
└── storage/
```

---

## 编码规范

### 包命名

| 类型 | 包路径 | 说明 |
|------|--------|------|
| 公共配置 | `org.mydotey.ai.site.common.config` | 配置类 |
| 公共异常 | `org.mydotey.ai.site.common.exception` | 异常处理 |
| 公共响应 | `org.mydotey.ai.site.common.response` | 统一响应 |
| 领域模块 | `org.mydotey.ai.site.{domain}` | 领域代码 |
| 基础设施 | `org.mydotey.ai.site.infrastructure` | 基础设施 |

### 类命名

| 类型 | 后缀 | 示例 |
|------|------|------|
| Controller | Controller | `BlogController` |
| DTO (Request) | Request | `CreateArticleRequest` |
| DTO (Response) | Response | `ArticleResponse` |
| DTO (View Object) | VO | `ArticleVO` |
| Command | Command | `CreateArticleCommand` |
| Command Service | CommandService | `BlogCommandService` |
| Query | Query | `ArticleListQuery` |
| Query Service | QueryService | `BlogQueryService` |
| Entity | 无后缀 | `Article` |
| Repository (接口) | Repository | `ArticleRepository` |
| Repository (实现) | RepositoryImpl | `ArticleRepositoryImpl` |
| Mapper | Mapper | `ArticleMapper` |

### Controller 规范

```java
@RestController
@RequestMapping("/api/v1/articles")
@Tag(name = "Article", description = "文章管理接口")
@Validated
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleQueryService queryService;
    private final ArticleCommandService commandService;

    @GetMapping
    @Operation(summary = "获取文章列表")
    public Result<PageResult<ArticleVO>> list(@Valid ArticleListQuery query) {
        return Result.success(queryService.list(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文章详情")
    public Result<ArticleVO> get(@PathVariable Long id) {
        return Result.success(queryService.getById(id));
    }

    @PostMapping
    @Operation(summary = "创建文章")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Long> create(@Valid @RequestBody CreateArticleRequest request) {
        return Result.success(commandService.create(request.toCommand()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文章")
    public Result<Void> update(@PathVariable Long id,
                               @Valid @RequestBody UpdateArticleRequest request) {
        commandService.update(request.toCommand(id));
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文章")
    public Result<Void> delete(@PathVariable Long id) {
        commandService.delete(id);
        return Result.success();
    }
}
```

### Entity 规范

```java
@Data
@TableName("article")
public class Article extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String title;

    private String slug;

    private String summary;

    private String content;

    private String coverImage;

    private Long categoryId;

    private ArticleStatus status;

    private Integer viewCount;

    private Integer likeCount;

    private Boolean isTop;

    private LocalDateTime publishedAt;

    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private List<Tag> tags;
}
```

### Repository 规范

```java
// 领域层 - 接口
public interface ArticleRepository {
    Article findById(Long id);
    Article findBySlug(String slug);
    Page<Article> findAll(PageQuery query);
    void save(Article article);
    void deleteById(Long id);
}

// 基础设施层 - 实现
@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final ArticleMapper mapper;

    @Override
    public Article findById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public void save(Article article) {
        if (article.getId() == null) {
            mapper.insert(article);
        } else {
            mapper.updateById(article);
        }
    }

    // ... 其他方法实现
}
```

### Command Service 规范

```java
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleCommandService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    public Long create(CreateArticleCommand command) {
        // 验证分类存在
        Category category = categoryRepository.findById(command.getCategoryId());
        if (category == null) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        // 创建文章
        Article article = new Article();
        article.setTitle(command.getTitle());
        article.setSlug(generateSlug(command.getTitle()));
        article.setContent(command.getContent());
        article.setCategoryId(command.getCategoryId());
        article.setStatus(ArticleStatus.DRAFT);

        articleRepository.save(article);
        return article.getId();
    }

    public void update(UpdateArticleCommand command) {
        Article article = articleRepository.findById(command.getId());
        if (article == null) {
            throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        // 更新字段
        article.setTitle(command.getTitle());
        article.setContent(command.getContent());

        articleRepository.save(article);
    }

    public void delete(Long id) {
        articleRepository.deleteById(id);
    }

    private String generateSlug(String title) {
        return SlugUtils.generate(title);
    }
}
```

### Query Service 规范

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleQueryService {

    private final ArticleMapper articleMapper;

    public ArticleVO getById(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);
        }
        return ArticleConverter.toVO(article);
    }

    public PageResult<ArticleVO> list(ArticleListQuery query) {
        Page<Article> page = articleMapper.selectPage(
            new Page<>(query.getPage(), query.getSize()),
            new LambdaQueryWrapper<Article>()
                .eq(query.getStatus() != null, Article::getStatus, query.getStatus())
                .eq(query.getCategoryId() != null, Article::getCategoryId, query.getCategoryId())
                .like(StringUtils.hasText(query.getKeyword()), Article::getTitle, query.getKeyword())
                .orderByDesc(Article::getIsTop)
                .orderByDesc(Article::getCreatedAt)
        );

        return PageResult.of(
            page.getRecords().stream().map(ArticleConverter::toVO).toList(),
            page.getTotal()
        );
    }
}
```

---

## 异常处理

### 异常定义

```java
// 业务异常
@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

// 错误码
@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 通用错误
    BAD_REQUEST(40000, "请求参数错误"),
    UNAUTHORIZED(40100, "未授权访问"),
    FORBIDDEN(40300, "禁止访问"),
    NOT_FOUND(40400, "资源不存在"),

    // 业务错误
    ARTICLE_NOT_FOUND(40401, "文章不存在"),
    CATEGORY_NOT_FOUND(40402, "分类不存在"),
    DUPLICATE_TITLE(40001, "标题已存在");

    private final int code;
    private final String message;
}
```

### 全局异常处理

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("Business exception: {}", e.getMessage());
        return Result.error(e.getErrorCode().getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining(", "));
        return Result.error(ErrorCode.BAD_REQUEST.getCode(), message);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("Unexpected exception", e);
        return Result.error(50000, "系统错误");
    }
}
```

---

## 统一响应

### Result 类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data, LocalDateTime.now());
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null, LocalDateTime.now());
    }
}
```

### PageResult 类

```java
@Data
@AllArgsConstructor
public class PageResult<T> {
    private List<T> list;
    private long total;

    public static <T> PageResult<T> of(List<T> list, long total) {
        return new PageResult<>(list, total);
    }
}
```

---

## 数据库规范

### 表命名
- 使用小写字母和下划线
- 表名使用单数形式：`article`、`user`、`category`
- 关联表使用双表名：`article_tag`

### 字段命名
- 使用小写字母和下划线
- 主键：`id`
- 外键：`{table}_id`，如 `category_id`
- 时间字段：`created_at`、`updated_at`、`deleted_at`

### 索引规范
- 主键自动创建索引
- 外键创建索引
- 常用查询字段创建索引
- 唯一约束字段创建唯一索引

### Flyway 迁移

```sql
-- V1.0.0__init_schema.sql

-- 用户表
CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(100),
    `nickname` VARCHAR(50),
    `avatar` VARCHAR(255),
    `status` TINYINT DEFAULT 1,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`)
);

-- 文章表
CREATE TABLE `article` (
    `id` BIGINT PRIMARY KEY,
    `title` VARCHAR(200) NOT NULL,
    `slug` VARCHAR(200) NOT NULL,
    `summary` VARCHAR(500),
    `content` LONGTEXT,
    `cover_image` VARCHAR(255),
    `category_id` BIGINT,
    `status` TINYINT DEFAULT 0,
    `view_count` INT DEFAULT 0,
    `like_count` INT DEFAULT 0,
    `is_top` TINYINT DEFAULT 0,
    `published_at` DATETIME,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    UNIQUE KEY `uk_slug` (`slug`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`)
);
```

---

## API 文档

### OpenAPI 注解

```java
@Tag(name = "Article", description = "文章管理接口")
public class ArticleController {

    @Operation(summary = "获取文章列表", description = "支持分页、筛选、排序")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功"),
        @ApiResponse(responseCode = "400", description = "参数错误")
    })
    public Result<PageResult<ArticleVO>> list(
        @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
        @Parameter(description = "每页数量") @RequestParam(defaultValue = "20") int size
    ) {
        // ...
    }
}
```

---

## 测试规范

### 单元测试

```java
@ExtendWith(MockitoExtension.class)
class ArticleCommandServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleCommandService commandService;

    @Test
    void shouldCreateArticle() {
        // given
        CreateArticleCommand command = new CreateArticleCommand();
        command.setTitle("Test Article");
        command.setContent("Test Content");

        // when
        Long id = commandService.create(command);

        // then
        verify(articleRepository).save(any(Article.class));
    }

    @Test
    void shouldThrowExceptionWhenArticleNotFound() {
        // given
        when(articleRepository.findById(anyLong())).thenReturn(null);

        // when & then
        assertThrows(BusinessException.class, () ->
            commandService.delete(1L)
        );
    }
}
```

### 集成测试

```java
@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnArticleList() throws Exception {
        mockMvc.perform(get("/api/v1/articles")
                .param("page", "1")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.list").isArray());
    }
}
```

---

## 配置规范

### application.yml

```yaml
spring:
  application:
    name: ai-site-api

  profiles:
    active: dev

  datasource:
    url: jdbc:mysql://localhost:3306/ai_site?useUnicode=true&characterEncoding=utf-8
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:}
    driver-class-name: com.mysql.cj.jdbc.Driver

  flyway:
    enabled: true
    locations: classpath:db/migration

mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

server:
  port: 8080
```

### 环境配置

```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:sqlite:./data/ai_site.db
    driver-class-name: org.sqlite.JDBC

logging:
  level:
    org.mydotey.ai.site: DEBUG

# application-prod.yml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST:localhost}:3306/ai_site
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

logging:
  level:
    org.mydotey.ai.site: INFO
```