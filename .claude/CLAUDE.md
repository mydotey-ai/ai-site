# AI-Site 项目记忆

## 项目概览

- **类型**: 个人网站 (Portfolio + Blog + 创作秀 + 多媒体)
- **架构**: 前后端分离 + Monorepo
- **前端**: Vue 3.5 + Vite 6 + TypeScript (admin + site 双项目)
- **后端**: Spring Boot 3.5 + MyBatis Plus + JDK 25 + DDD
- **数据库**: MySQL (生产) / SQLite (开发/轻量部署)

## 关键路径

| 路径 | 说明 |
|------|------|
| `/frontend/admin/` | 管理后台前端项目 |
| `/frontend/site/` | 用户网站前端项目 |
| `/frontend/shared/` | 前端共享代码 |
| `/backend/common/` | 后端公共模块 |
| `/backend/api/` | 后端 API 模块 (对外服务) |
| `/backend/admin/` | 后端管理模块 |
| `/docs/` | 项目文档 |
| `/docs/plans/` | 开发计划 |
| `/docs/plans/phases/` | 实施阶段文档 |

## 模块依赖

```
┌─────────────┐    ┌─────────────┐
│    admin    │    │     api     │   <- 后端应用模块
└──────┬──────┘    └──────┬──────┘
       │                  │
       └────────┬─────────┘
                │
                ▼
         ┌─────────────┐
         │   common    │   <- 后端公共模块
         └─────────────┘
```

## DDD 分层架构

```
领域包结构 (以 blog 为例):

blog/
├── controller/          # 接入层 - HTTP接口
├── dto/                 # 接入层 - 数据传输对象
├── command/             # 应用服务层 - 写操作
├── query/               # 应用服务层 - 读操作
├── job/                 # 应用服务层 - 定时任务
├── entity/              # 领域层 - 领域实体
├── repository/          # 领域层 - 仓储接口
├── enums/               # 领域层 - 枚举
└── service/             # 领域层 - 领域服务 (可选)

infrastructure/          # 基础设施层 (跨领域共享)
├── persistence/
│   ├── mapper/          # MyBatis Mapper
│   ├── repository/      # Repository 实现
│   └── converter/       # 对象转换器
├── security/
└── storage/
```

## 常用命令

### 前端

```bash
# 安装依赖
cd frontend && pnpm install

# 启动管理后台
cd frontend/admin && pnpm dev

# 启动用户网站
cd frontend/site && pnpm dev

# 构建生产版本
pnpm build

# 运行测试
pnpm test
```

### 后端

```bash
# 编译项目
cd backend && ./mvnw clean compile

# 运行 API 服务
cd backend && ./mvnw spring-boot:run -pl api

# 运行 Admin 服务
cd backend && ./mvnw spring-boot:run -pl admin

# 运行测试
./mvnw test

# 打包
./mvnw package -DskipTests
```

## 详细记忆

- [架构决策记录](./memory/architecture.md)
- [编码约定](./memory/conventions.md)
- [领域知识](./memory/domain-knowledge.md)
- [问题排查记录](./memory/troubleshooting.md)

## 开发规则

- [全局规则](./rules/global.md)
- [前端规则](./rules/frontend.md)
- [后端规则](./rules/backend.md)