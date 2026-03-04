# 后端架构

## 项目结构

```
backend/
├── common/              # 公共模块
├── api/                 # API 模块 (对外服务)
├── admin/               # Admin 模块 (管理后台)
├── pom.xml              # 父 POM
└── mvnw                 # Maven Wrapper
```

## 模块依赖

```
┌─────────────┐    ┌─────────────┐
│    admin    │    │     api     │
│  (管理服务)  │    │  (API服务)  │
└──────┬──────┘    └──────┬──────┘
       │                  │
       └────────┬─────────┘
                │
                ▼
         ┌─────────────┐
         │   common    │
         │  (公共模块)  │
         └─────────────┘
```

## 模块说明

### Common 模块

**职责**: 提供公共功能和基础设施支持

```
common/
└── src/main/java/org/mydotey/ai/site/common/
    ├── config/          # 配置类
    │   ├── MybatisPlusConfig.java
    │   ├── WebMvcConfig.java
    │   ├── SwaggerConfig.java
    │   └── SecurityConfig.java
    ├── exception/       # 异常处理
    │   ├── GlobalExceptionHandler.java
    │   ├── BusinessException.java
    │   └── ErrorCode.java
    ├── response/        # 统一响应
    │   └── Result.java
    ├── entity/          # 基础实体
    │   ├── BaseEntity.java
    │   └── PageQuery.java
    ├── enums/           # 公共枚举
    ├── util/            # 工具类
    ├── annotation/      # 自定义注解
    └── constant/        # 常量
```

### API 模块

**职责**: 提供对外 API 服务，处理用户网站请求

```
api/
└── src/main/java/org/mydotey/ai/site/
    ├── ApiApplication.java
    ├── auth/            # 认证领域
    ├── blog/            # 博客领域
    ├── portfolio/       # 作品集领域
    ├── creation/        # 创作领域
    ├── media/           # 多媒体领域
    └── infrastructure/  # 基础设施层
```

### Admin 模块

**职责**: 提供管理后台服务，处理管理操作

```
admin/
└── src/main/java/org/mydotey/ai/site/
    ├── AdminApplication.java
    ├── auth/            # 认证领域 (管理端)
    ├── blog/            # 博客领域 (管理端)
    ├── portfolio/       # 作品集领域 (管理端)
    ├── creation/        # 创作领域 (管理端)
    ├── media/           # 多媒体领域 (管理端)
    └── infrastructure/  # 基础设施层
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

每个领域采用相同的包结构：

```
{domain}/
├── controller/          # 接入层 - HTTP 接口
│   └── XxxController.java
├── dto/                 # 接入层 - 数据传输对象
│   ├── XxxRequest.java
│   ├── XxxResponse.java
│   └── XxxVO.java
├── command/             # 应用服务层 - 写操作
│   ├── CreateXxxCommand.java
│   ├── UpdateXxxCommand.java
│   └── XxxCommandService.java
├── query/               # 应用服务层 - 读操作
│   ├── XxxListQuery.java
│   └── XxxQueryService.java
├── job/                 # 应用服务层 - 定时任务
│   └── XxxCleanupJob.java
├── entity/              # 领域层 - 实体
│   └── Xxx.java
├── repository/          # 领域层 - 仓储接口
│   └── XxxRepository.java
├── enums/               # 领域层 - 枚举
│   └── XxxStatus.java
└── service/             # 领域层 - 领域服务 (可选)
    └── XxxDomainService.java
```

## 基础设施层

```
infrastructure/
├── persistence/         # 持久化
│   ├── mapper/          # MyBatis Mapper
│   │   └── XxxMapper.java
│   ├── repository/      # Repository 实现
│   │   └── XxxRepositoryImpl.java
│   └── converter/       # 对象转换器
│       └── XxxConverter.java
├── security/            # 安全
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── UserDetailsServiceImpl.java
└── storage/             # 存储
    ├── StorageService.java
    └── LocalStorageService.java
```

## 技术组件

### Spring Boot 配置

```yaml
# application.yml
spring:
  application:
    name: ai-site-api

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
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
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