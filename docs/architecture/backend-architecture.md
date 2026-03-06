# 后端架构

## 项目结构

```
backend/
├── common/              # 公共模块 (业务无关的工具类)
├── service/             # 服务模块 (对外服务 + 管理后台)
├── pom.xml              # 父 POM
└── mvnw                 # Maven Wrapper
```

## 模块依赖

```
         ┌─────────────┐
         │   service   │
         │  (服务模块)  │
         └──────┬──────┘
                │
                ▼
         ┌─────────────┐
         │   common    │
         │  (公共模块)  │
         └─────────────┘
```

## 模块说明

### Common 模块

**职责**: 提供业务无关的公共功能和工具支持

```
common/
└── src/main/java/org/mydotey/ai/site/common/
    ├── annotation/      # 自定义注解
    ├── constant/        # 常量定义
    ├── enums/           # 通用枚举
    └── util/            # 工具类
```

### Service 模块

**职责**: 提供所有业务服务，包括对外 API 和管理后台

```
service/
└── src/main/java/org/mydotey/ai/site/
    ├── ServiceApplication.java
    ├── common/                  # 公共代码 (业务相关)
    │   ├── advice/              # 全局异常处理
    │   ├── config/              # 配置类
    │   ├── exception/           # 异常类
    │   ├── security/            # 安全组件
    │   └── module/              # 公共模块组件 (DDD 四层)
    │       ├── interfaces/      # Result, PageResult
    │       ├── application/
    │       ├── domain/
    │       │   ├── entity/      # BaseEntity, PageQuery
    │       │   └── repository/
    │       └── infrastructure/
    │           └── persistence/
    ├── auth/                    # 认证领域
    ├── blog/                    # 博客领域
    ├── portfolio/               # 作品集领域
    ├── creation/                # 创作领域
    └── media/                   # 多媒体领域
```

## DDD 分层架构

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

## 领域包结构

每个领域采用相同的 DDD 四层包结构：

```
{domain}/
├── interfaces/              # 接入层
│   ├── controller/          # HTTP 接口
│   ├── dto/                 # 数据传输对象
│   └── assembler/           # DTO 转换器
├── application/             # 应用服务层
│   ├── command/             # 写操作
│   ├── query/               # 读操作
│   └── job/                 # 定时任务
├── domain/                  # 领域层
│   ├── entity/              # 领域实体
│   ├── repository/          # 仓储接口
│   ├── enums/               # 枚举
│   └── service/             # 领域服务 (可选)
└── infrastructure/          # 基础设施层
    ├── persistence/
    │   ├── mapper/          # MyBatis Mapper
    │   ├── repository/      # Repository 实现
    │   └── converter/       # 对象转换器
    ├── security/
    └── storage/
```

**分包原则**：领域简单时，各层直接放类文件，无需再分包；领域复杂时，再按功能分子包。

## 公共包结构

```
common/
├── advice/                  # GlobalExceptionHandler
├── config/                  # 配置类
│   ├── MybatisPlusConfig.java
│   ├── SecurityConfig.java
│   ├── WebMvcConfig.java
│   └── SwaggerConfig.java
├── exception/               # 异常类
│   ├── BusinessException.java
│   └── ErrorCode.java
├── security/                # 安全组件
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── UserDetailsServiceImpl.java
└── module/                  # 公共模块组件 (DDD 四层)
    ├── interfaces/          # Result, PageResult
    ├── application/
    ├── domain/
    │   ├── entity/          # BaseEntity, PageQuery
    │   └── repository/
    └── infrastructure/
        └── persistence/
```

## 技术组件

### Spring Boot 配置

```yaml
# application.yml
spring:
  application:
    name: ai-site-service

  datasource:
    url: jdbc:mysql://localhost:3306/ai_site
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
```

### MyBatis Plus 配置

```java
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

### 安全配置

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth ->
                auth.requestMatchers("/api/v1/auth/**").permitAll()
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
```

## API 设计

### RESTful 规范

```
# 查询操作 (GET)
GET  /api/v1/articles           # 列表
GET  /api/v1/articles/{id}      # 详情

# 创建操作 (POST)
POST /api/v1/articles           # 创建

# 更新操作 (PUT)
PUT  /api/v1/articles/{id}      # 更新

# 删除操作 (DELETE)
DELETE /api/v1/articles/{id}    # 删除
```

### 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { ... },
  "timestamp": "2024-01-15T10:30:00"
}
```

### 分页响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [ ... ],
    "total": 100
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

## 数据库支持

### MySQL 配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_site?useUnicode=true&characterEncoding=utf-8
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### SQLite 配置

```yaml
spring:
  datasource:
    url: jdbc:sqlite:./data/ai_site.db
    driver-class-name: org.sqlite.JDBC
```

### Flyway 迁移

```
src/main/resources/db/migration/
├── V1.0.0__init_schema.sql      # 初始化表结构
├── V1.0.1__add_article_tag.sql  # 添加文章标签关联表
└── V1.1.0__add_novel_tables.sql # 添加小说相关表
```