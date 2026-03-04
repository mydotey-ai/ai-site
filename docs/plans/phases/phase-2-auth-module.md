# Phase 2: 认证模块

## 阶段目标

实现用户认证和授权功能，包括：
- 用户注册、登录
- JWT Token 管理
- 角色权限控制
- 前端登录页面

## 任务清单

### 1. 后端 - Auth 领域

#### 接入层
- [x] AuthController
- [x] LoginRequest, RegisterRequest
- [x] AuthResponse

#### 应用服务层
- [x] AuthCommandService
  - [x] login
  - [x] register
- [x] AuthQueryService
  - [x] getCurrentUser

#### 领域层
- [x] User Entity
- [x] Role Entity
- [x] UserRole Entity
- [x] UserRepository
- [x] RoleRepository

#### 基础设施层
- [x] UserMapper
- [x] RoleMapper
- [x] UserRoleMapper
- [x] UserRepositoryImpl
- [x] RoleRepositoryImpl

### 2. 数据库

- [x] user 表 (Phase 1 已创建)
- [x] role 表 (Phase 1 已创建)
- [x] user_role 表 (Phase 1 已创建)

### 3. 前端 - 登录页面

#### Admin
- [x] LoginPage.vue
- [x] auth API
- [x] auth store

## API 设计

### 登录

```
POST /api/v1/auth/login
Request:
{
  "username": "string",
  "password": "string"
}
Response:
{
  "code": 200,
  "data": {
    "token": "jwt-token",
    "refreshToken": "refresh-token",
    "user": { ... }
  }
}
```

### 注册

```
POST /api/v1/auth/register
Request:
{
  "username": "string",
  "password": "string",
  "email": "string",
  "nickname": "string (optional)"
}
Response:
{
  "code": 200,
  "data": 1  // userId
}
```

### 获取当前用户

```
GET /api/v1/auth/me
Headers:
  Authorization: Bearer <token>
Response:
{
  "code": 200,
  "data": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "roles": ["ADMIN"]
  }
}
```

## 验收标准

- [x] 用户可以注册
- [x] 用户可以登录
- [x] Token 正确生成和验证
- [x] 前端登录页面可正常使用

## 完成情况

**完成日期**: 2026-03-04

**产出统计**:
- 新增文件: 21 个
- 代码行数: 987+ 行

**主要产出**:
1. 后端 Auth 领域完整实现 (DDD分层)
2. 用户注册、登录、获取当前用户API
3. 前端登录页面和认证状态管理

## 下一阶段

完成 Phase 2 后，进入 [Phase 3: 博客模块](./phase-3-blog-module.md)。