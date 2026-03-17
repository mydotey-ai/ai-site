# 后端项目重构计划

## 概述

根据后端开发规范，对后端项目进行完整重构，包括模块合并、DDD 分层调整和代码分类整理。

## 目标结构

```
backend/
├── common/                          # 公共模块 (业务无关的工具类)
│   └── src/main/java/org/mydotey/ai/site/common/
│       ├── annotation/              # 注解
│       ├── constant/                # 常量
│       ├── enums/                   # 通用枚举
│       └── util/                    # 工具类
├── service/                         # 服务模块
│   └── src/main/java/org/mydotey/ai/site/
│       ├── ServiceApplication.java  # 启动类
│       ├── common/                  # 公共代码
│       │   ├── advice/              # 全局异常处理
│       │   ├── config/              # 配置类
│       │   ├── exception/           # 异常类
│       │   ├── security/            # 安全组件
│       │   └── domain/              # DDD 四层结构
│       │       ├── interfaces/      # Result, PageResult
│       │       ├── application/
│       │       ├── domain/
│       │       │   ├── entity/      # BaseEntity, PageQuery
│       │       │   └── repository/
│       │       └── infrastructure/
│       │           ├── config/
│       │           ├── persistence/
│       │           └── security/
│       └── auth/                    # 领域包示例
│           ├── interfaces/          # 接入层
│           ├── application/         # 应用服务层
│           ├── domain/              # 领域层
│           └── infrastructure/      # 基础设施层
└── pom.xml
```

## 实施步骤

### Phase 1: 模块合并 (admin + api → service) ✅

- [x] 创建 `backend/service/` 模块
- [x] 创建 `ServiceApplication.java` 启动类
- [x] 迁移 `api/` 模块代码到 `service/`
- [x] 删除 `admin/` 和 `api/` 目录
- [x] 更新父 `pom.xml` 模块列表

---

### Phase 2: DDD 四层结构调整 ✅

- [x] auth 领域重构
- [x] blog 领域重构
- [x] creation 领域重构
- [x] media 领域重构
- [x] portfolio 领域重构
- [x] 删除根目录下的 `infrastructure/` 目录

---

### Phase 3: 代码分类整理 ✅

#### 3.1 common 模块 - 业务无关的工具类
保留：
- [x] `annotation/` - 注解
- [x] `constant/` - 常量
- [x] `enums/` - 通用枚举
- [x] `util/` - 工具类

#### 3.2 service/common 包 - 业务相关的公共代码
- [x] `advice/` - GlobalExceptionHandler
- [x] `config/` - MybatisPlusConfig, SecurityConfig, etc.
- [x] `exception/` - BusinessException, ErrorCode
- [x] `security/` - JWT, UserDetailsServiceImpl
- [x] `domain/interfaces/` - Result, PageResult
- [x] `domain/domain/entity/` - BaseEntity, PageQuery

#### 3.3 更新 import 语句
- [x] 更新所有 Java 文件的 package 声明
- [x] 更新所有引用的 import 语句

---

### Phase 4: 验证与测试 ✅

- [x] 运行 Maven 编译：`mvn clean compile`
- [x] 运行单元测试：`mvn test`
- [x] 启动应用验证：`mvn spring-boot:run`
- [x] 更新项目文档

---

## 最终结构

### common 模块（业务无关）
```
common/
├── annotation/      # 自定义注解
├── constant/        # 常量定义
├── enums/           # 通用枚举
└── util/            # 工具类
```

### service 模块
```
service/
├── common/
│   ├── advice/                      # GlobalExceptionHandler
│   ├── config/                      # MybatisPlusConfig, SecurityConfig, etc.
│   ├── exception/                   # BusinessException, ErrorCode
│   ├── security/                    # JWT, UserDetailsServiceImpl
│   └── domain/
│       ├── interfaces/              # Result, PageResult
│       ├── application/
│       ├── domain/
│       │   ├── entity/              # BaseEntity, PageQuery
│       │   └── repository/
│       └── infrastructure/
│           ├── config/
│           ├── persistence/
│           └── security/
├── auth/                            # 认证领域
├── blog/                            # 博客领域
├── creation/                        # 创作领域
├── media/                           # 媒体领域
└── portfolio/                       # 作品集领域
```

---

## 执行状态

| Phase | 状态 |
|-------|------|
| Phase 1 | ✅ 完成 |
| Phase 2 | ✅ 完成 |
| Phase 3 | ✅ 完成 |
| Phase 4 | ✅ 完成 |

**重构完成！** 后端项目已成功迁移到 DDD 四层结构。