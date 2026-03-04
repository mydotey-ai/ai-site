# AI-Site 项目文档

## 项目简介

AI-Site 是一个个人网站项目，包含作品展示、技术博客、创作内容和多媒体展示等功能。

## 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue 3.5 + Vite 6 + TypeScript |
| 后端 | Spring Boot 3.5 + MyBatis Plus + JDK 25 |
| 数据库 | MySQL / SQLite |
| 部署 | 传统部署 (JAR + Nginx) |

## 文档目录

### 架构文档
- [架构总览](./architecture/overview.md)
- [DDD 领域设计](./architecture/ddd-design.md)
- [前端架构](./architecture/frontend-architecture.md)
- [后端架构](./architecture/backend-architecture.md)
- [部署架构](./architecture/deployment.md)

### API 文档
- [API 文档索引](./api/README.md)

### 数据库文档
- [数据库设计](./database/schema.md)
- [MySQL/SQLite 兼容性说明](./database/mysql-sqlite-compat.md)

### 开发文档
- [开发环境搭建](./development/setup.md)
- [编码规范](./development/conventions.md)
- [测试指南](./development/testing.md)

### 部署文档
- [部署指南](./deployment/README.md)

### 开发计划
- [计划总览](./plans/README.md)
- [实施阶段](./plans/phases/)

### 架构决策记录
- [ADR 索引](./decisions/)

## 快速开始

### 环境要求

- JDK 25+
- Node.js 20+
- pnpm 9+
- MySQL 8.0+ (可选)
- SQLite 3.x (开发环境)

### 启动项目

```bash
# 克隆项目
git clone <repository-url>
cd ai-site

# 安装前端依赖
cd frontend && pnpm install

# 启动前端开发服务
cd admin && pnpm dev    # 管理后台
cd site && pnpm dev     # 用户网站

# 启动后端服务
cd backend
./mvnw spring-boot:run -pl api
```

## 项目结构

```
ai-site/
├── .claude/           # Claude AI 记忆和规则
├── .github/           # GitHub 配置
├── docs/              # 项目文档
├── frontend/          # 前端项目
│   ├── admin/         # 管理后台
│   ├── site/          # 用户网站
│   └── shared/        # 共享代码
├── backend/           # 后端项目
│   ├── common/        # 公共模块
│   ├── api/           # API 模块
│   └── admin/         # Admin 模块
├── scripts/           # 脚本工具
└── README.md
```

## 联系方式

- 作者: [Your Name]
- 邮箱: [your-email]
- GitHub: [your-github]