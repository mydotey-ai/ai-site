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
- [ ] AuthController
- [ ] LoginRequest, RegisterRequest
- [ ] AuthResponse

#### 应用服务层
- [ ] AuthCommandService
  - [ ] login
  - [ ] register
  - [ ] logout
- [ ] AuthQueryService
  - [ ] getCurrentUser
  - [ ] checkPermission

#### 领域层
- [ ] User Entity
- [ ] Role Entity
- [ ] Permission Entity
- [ ] UserRepository
- [ ] RoleRepository

#### 基础设施层
- [ ] UserMapper
- [ ] RoleMapper
- [ ] UserRepositoryImpl
- [ ] PasswordEncoder

### 2. 数据库

- [ ] user 表
- [ ] role 表
- [ ] permission 表
- [ ] user_role 表
- [ ] role_permission 表

### 3. 前端 - 登录页面

#### Admin
- [ ] LoginPage.vue
- [ ] useAuth composable
- [ ] auth store

#### Site
- [ ] 登录/注册弹窗组件
- [ ] 用户状态展示

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
  "email": "string"
}
Response:
{
  "code": 200,
  "data": {
    "userId": 1
  }
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

- [ ] 用户可以注册
- [ ] 用户可以登录
- [ ] Token 正确生成和验证
- [ ] 权限控制生效
- [ ] 前端登录页面可正常使用

## 下一阶段

完成 Phase 2 后，进入 [Phase 3: 博客模块](./phase-3-blog-module.md)。