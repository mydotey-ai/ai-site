# DDD 领域设计

## 分层架构

项目采用 DDD 四层架构：

```
┌─────────────────────────────────────────────────────────────┐
│                      接入层 (Interface)                      │
│         Controller, DTO, 参数校验, HTTP 处理                  │
├─────────────────────────────────────────────────────────────┤
│                    应用服务层 (Application)                   │
│         Command, Query, Job, 用例编排, 事务管理                │
├─────────────────────────────────────────────────────────────┤
│                      领域层 (Domain)                         │
│         Entity, Repository, 领域服务, 业务规则                 │
├─────────────────────────────────────────────────────────────┤
│                   基础设施层 (Infrastructure)                 │
│         Mapper, Repository 实现, 外部服务, 存储服务            │
└─────────────────────────────────────────────────────────────┘
```

## 各层职责

### 接入层 (Interface Layer)

**职责**: 处理 HTTP 请求，参数校验，DTO 转换

```
controller/     - HTTP 接口定义
dto/            - 数据传输对象 (Request/Response/VO)
```

**代码示例**:
```java
@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {
    private final ArticleQueryService queryService;
    private final ArticleCommandService commandService;

    @GetMapping("/{id}")
    public Result<ArticleVO> get(@PathVariable Long id) {
        return Result.success(queryService.getById(id));
    }

    @PostMapping
    public Result<Long> create(@Valid @RequestBody CreateArticleRequest request) {
        return Result.success(commandService.create(request.toCommand()));
    }
}
```

### 应用服务层 (Application Layer)

**职责**: 用例编排，事务管理，调用领域服务

```
command/        - 写操作 (创建、更新、删除)
query/          - 读操作 (查询、列表)
job/            - 定时任务
```

**Command Service 示例**:
```java
@Service
@Transactional
public class ArticleCommandService {
    private final ArticleRepository articleRepository;

    public Long create(CreateArticleCommand command) {
        Article article = new Article();
        article.setTitle(command.getTitle());
        article.setContent(command.getContent());
        articleRepository.save(article);
        return article.getId();
    }
}
```

**Query Service 示例**:
```java
@Service
@Transactional(readOnly = true)
public class ArticleQueryService {
    private final ArticleMapper articleMapper;

    public ArticleVO getById(Long id) {
        Article article = articleMapper.selectById(id);
        return ArticleConverter.toVO(article);
    }
}
```

### 领域层 (Domain Layer)

**职责**: 业务规则，领域对象，仓储接口

```
entity/         - 领域实体
repository/     - 仓储接口 (只有接口定义)
enums/          - 领域枚举
service/        - 领域服务 (可选，复杂业务逻辑)
```

**Entity 示例**:
```java
@Data
@TableName("article")
public class Article extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String title;
    private String content;
    private ArticleStatus status;

    // 领域行为
    public void publish() {
        if (this.status != ArticleStatus.DRAFT) {
            throw new BusinessException("只有草稿状态可以发布");
        }
        this.status = ArticleStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }
}
```

**Repository 接口示例**:
```java
public interface ArticleRepository {
    Article findById(Long id);
    void save(Article article);
    void deleteById(Long id);
}
```

### 基础设施层 (Infrastructure Layer)

**职责**: 技术实现，数据库访问，外部服务

```
persistence/
  ├── mapper/           - MyBatis Mapper
  ├── repository/       - Repository 接口实现
  └── converter/        - 对象转换器
security/               - 安全相关实现
storage/                - 存储服务实现
```

**Repository 实现示例**:
```java
@Repository
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
}
```

## 领域划分

### Auth 认证领域

```
auth/
├── controller/         - AuthController
├── dto/                - LoginRequest, RegisterRequest
├── command/            - LoginCommand, RegisterCommand
├── query/              - UserInfoQuery
├── entity/             - User, Role, Permission
├── repository/         - UserRepository, RoleRepository
└── enums/              - UserStatus
```

### Blog 博客领域

```
blog/
├── controller/         - BlogController
├── dto/                - ArticleVO, ArticleQuery
├── command/            - CreateArticleCommand, UpdateArticleCommand
├── query/              - ArticleListQuery
├── entity/             - Article, Category, Tag
├── repository/         - ArticleRepository, CategoryRepository
└── enums/              - ArticleStatus
```

### Portfolio 作品集领域

```
portfolio/
├── controller/
├── dto/
├── command/
├── query/
├── entity/             - Project, Skill, Experience
├── repository/
└── enums/
```

### Creation 创作领域

```
creation/
├── controller/
├── dto/
├── command/
├── query/
├── entity/             - Novel, Chapter, Poetry, Essay
├── repository/
└── enums/
```

### Media 多媒体领域

```
media/
├── controller/
├── dto/
├── command/
├── query/
├── entity/             - Image, Video, Album
├── repository/
└── enums/
```

## CQRS 模式

项目采用简化的 CQRS 模式：

- **Command**: 处理写操作 (创建、更新、删除)
- **Query**: 处理读操作 (查询、列表)

**优势**:
- 读写分离，职责清晰
- 便于优化读取性能
- 便于扩展 (如读写分离数据库)

```
┌──────────────┐     ┌──────────────┐
│   Command    │     │    Query     │
│   Service    │     │   Service    │
└──────┬───────┘     └──────┬───────┘
       │                    │
       ▼                    ▼
┌──────────────┐     ┌──────────────┐
│  Repository  │     │    Mapper    │
│   (写入)     │     │   (读取)     │
└──────────────┘     └──────────────┘
```