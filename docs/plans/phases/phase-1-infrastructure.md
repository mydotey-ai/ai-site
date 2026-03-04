# Phase 1: 基础设施

## 阶段目标

搭建项目的基础设施，包括：
- 后端配置框架
- 安全框架配置
- 数据库配置 (MySQL + SQLite)
- 前端基础组件和工具

## 任务清单

### 1. 后端配置框架

- [ ] 创建通用配置类
  - [ ] MyBatisPlusConfig
  - [ ] WebMvcConfig
  - [ ] SwaggerConfig
  - [ ] CorsConfig
- [ ] 创建统一响应类
  - [ ] Result
  - [ ] PageResult
- [ ] 创建异常处理
  - [ ] GlobalExceptionHandler
  - [ ] BusinessException
  - [ ] ErrorCode

### 2. 安全框架

- [ ] Spring Security 配置
- [ ] JWT Token Provider
- [ ] 认证过滤器
- [ ] 用户详情服务

### 3. 数据库配置

- [ ] 数据源配置 (MySQL)
- [ ] 数据源配置 (SQLite)
- [ ] Flyway 迁移脚本
- [ ] 基础表结构创建

### 4. 前端基础设施

- [ ] Axios 请求封装
- [ ] 路由配置
- [ ] 状态管理配置
- [ ] 通用组件库搭建
- [ ] 样式变量定义

## 产出物

### 后端

```
common/
├── config/
│   ├── MybatisPlusConfig.java
│   ├── WebMvcConfig.java
│   ├── SwaggerConfig.java
│   └── SecurityConfig.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── BusinessException.java
│   └── ErrorCode.java
├── response/
│   ├── Result.java
│   └── PageResult.java
└── entity/
    ├── BaseEntity.java
    └── PageQuery.java
```

### 前端

```
admin/src/
├── utils/
│   └── request.ts
├── router/
│   ├── index.ts
│   └── guards.ts
├── stores/
│   └── index.ts
├── assets/styles/
│   └── variables.scss
└── components/common/
    ├── BaseButton.vue
    ├── BaseInput.vue
    └── BaseModal.vue
```

## 验收标准

- [ ] 后端服务可正常启动
- [ ] Swagger UI 可访问
- [ ] 数据库表创建成功
- [ ] 前端开发服务可启动
- [ ] 基础组件可正常使用

## 下一阶段

完成 Phase 1 后，进入 [Phase 2: 认证模块](./phase-2-auth-module.md)。