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
├── common/              # 公共模块
├── service/             # 服务模块 (对外服务 + 管理后台)
├── pom.xml              # 父 POM
└── mvnw                 # Maven Wrapper
```

### DDD 分层结构
```
org.mydotey.ai.site/
├── common/                    # 公共包
│   └── domain/                # 公共领域组件
│       ├── interfaces/        # 接入层基类/组件
│       ├── application/       # 应用服务层基类/组件
│       ├── domain/            # 领域层基类/组件
│       └── infrastructure/    # 基础设施层基类/组件
└── {domain}/                  # 领域包
    ├── interfaces/            # 接入层
    │   ├── controller/        # HTTP 接口
    │   ├── dto/               # 数据传输对象
    │   └── assembler/         # DTO 转换器
    ├── application/           # 应用服务层
    │   ├── command/           # 写操作
    │   ├── query/             # 读操作
    │   └── job/               # 定时任务
    ├── domain/                # 领域层
    │   ├── entity/            # 领域实体
    │   ├── repository/        # 仓储接口
    │   ├── enums/             # 枚举
    │   └── service/           # 领域服务 (可选)
    └── infrastructure/        # 基础设施层
        ├── persistence/
        │   ├── mapper/        # MyBatis Mapper
        │   ├── repository/    # Repository 实现
        │   └── converter/     # 对象转换器
        ├── security/
        └── storage/
```

**分包原则**：领域简单时，各层直接放类文件，无需再分包；领域复杂时，再按功能分子包。

---

## 编码规范

### 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 类名 | PascalCase | `UserServiceImpl`, `ArticleController` |
| 接口名 | PascalCase | `ArticleRepository`, `UserService` |
| 方法名 | camelCase | `getUserById()`, `createArticle()` |
| 变量名 | camelCase | `userName`, `articleList` |
| 常量名 | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT`, `DEFAULT_PAGE_SIZE` |
| 包名 | 全小写 | `org.mydotey.ai.site.blog` |
| 包文件夹 | 全小写 | `controller/`, `service/`, `repository/` |

### 类命名约定

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

### 代码风格

- 单个文件不超过 500 行
- 单个方法不超过 50 行
- 单个类职责单一
- 避免深层嵌套（最多 3 层）
- 类注释：说明类的职责和用途
- 方法注释：说明方法的功能、参数、返回值
- 复杂逻辑：添加必要的行内注释
- 避免无意义的注释

### Controller 规范

- 使用 `@RestController` + `@RequestMapping`
- 注入 QueryService 和 CommandService
- GET 请求返回查询结果，POST/PUT/DELETE 调用 CommandService
- 使用 `@Valid` 验证请求参数
- 使用 `@Tag` 和 `@Operation` 注解生成 API 文档

### Entity 规范

- 使用 `@Data` + `@TableName`
- 主键使用 `@TableId(type = IdType.ASSIGN_ID)`
- 逻辑删除使用 `@TableLogic`
- 非数据库字段使用 `@TableField(exist = false)`

### Repository 规范

- 领域层定义接口，基础设施层实现
- 实现类使用 `@Repository` + `@RequiredArgsConstructor`
- save 方法根据 id 是否为空决定 insert 或 update

### Service 规范

- CommandService 使用 `@Transactional`（写操作）
- QueryService 使用 `@Transactional(readOnly = true)`（读操作）
- 使用 `@Service` + `@RequiredArgsConstructor`

---

## 安全规范

- API 接口需要权限控制
- 防止 SQL 注入：使用参数化查询，避免拼接 SQL
- 密码使用 BCrypt 加密存储
- 敏感配置使用环境变量注入
- 接口限流，防止暴力攻击

---

## 异常处理

- 业务异常使用 `BusinessException`，包含 `ErrorCode`
- 错误码使用枚举定义，包含 code 和 message
- 全局异常处理器使用 `@RestControllerAdvice`
- 处理 `BusinessException`、`MethodArgumentNotValidException`、`Exception`

---

## 统一响应

- 使用 `Result<T>` 包装响应，包含 code、message、data、timestamp
- 分页使用 `PageResult<T>`，包含 list 和 total

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
- 迁移文件放在 `resources/db/migration/`
- 命名格式：`V{version}__{description}.sql`

---

## API 文档

- 使用 SpringDoc 生成 OpenAPI 文档
- Controller 使用 `@Tag` 注解
- 方法使用 `@Operation`、`@ApiResponses` 注解
- 参数使用 `@Parameter` 注解

---

## 测试规范

### 测试命名
- 测试类命名为 `{ClassName}Test`
- 测试方法使用 `should{ExpectedBehavior}When{Condition}` 格式

### 测试类型
- 单元测试：使用 JUnit 5 + Mockito
- 集成测试：使用 `@SpringBootTest` + `@AutoConfigureMockMvc`

---

## 配置规范

### 环境变量文件
- 开发环境：`application-dev.yml`
- 生产环境：`application-prod.yml`
- 敏感配置使用 `${ENV_VAR}` 语法注入，不提交到代码库

### 关键配置
- 数据源：URL、用户名、密码、驱动
- Flyway：启用迁移，指定迁移文件位置
- MyBatis Plus：Mapper 位置、驼峰转换、日志