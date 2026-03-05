# AI-Site 项目文档

## 目录结构

```
docs/
├── architecture/       # 架构文档
├── api/                # API 文档
├── database/           # 数据库文档
├── development/        # 开发文档
├── deployment/         # 部署文档
├── decisions/          # 架构决策记录
└── plans/              # 开发计划
```

---

## 架构文档 (architecture/)

系统架构设计相关文档。

| 文档 | 说明 |
|------|------|
| [overview.md](./architecture/overview.md) | 架构总览，系统整体设计和技术选型 |
| [domain-model.md](./architecture/domain-model.md) | 领域模型，业务领域划分和核心实体设计 |
| [ddd-design.md](./architecture/ddd-design.md) | DDD 分层架构设计 |
| [frontend-architecture.md](./architecture/frontend-architecture.md) | 前端架构设计 |
| [backend-architecture.md](./architecture/backend-architecture.md) | 后端架构设计 |
| [deployment.md](./architecture/deployment.md) | 部署架构设计 |

---

## API 文档

API 接口设计文档。

| 文档 | 说明 |
|------|------|
| [README.md](./api/README.md) | API 文档索引 |

---

## 数据库文档

数据库设计相关文档。

| 文档 | 说明 |
|------|------|
| [schema.md](./database/schema.md) | 数据库表结构设计 |
| [mysql-sqlite-compat.md](./database/mysql-sqlite-compat.md) | MySQL/SQLite 兼容性说明 |

---

## 开发文档

开发相关指南和规范。

| 文档 | 说明 |
|------|------|
| [setup.md](./development/setup.md) | 开发环境搭建指南 |
| [conventions.md](./development/conventions.md) | 编码规范 |
| [testing.md](./development/testing.md) | 测试指南 |
| [troubleshooting.md](./development/troubleshooting.md) | 问题排查记录 |

---

## 部署文档

部署相关文档。

| 文档 | 说明 |
|------|------|
| [README.md](./deployment/README.md) | 部署指南 |

---

## 架构决策记录

记录项目中的重要架构决策。

| 文档 | 说明 |
|------|------|
| [adr.md](./decisions/adr.md) | 架构决策记录 (ADR) |

---

## 开发计划

项目开发计划和进度跟踪。

| 路径 | 说明 |
|------|------|
| [README.md](./plans/README.md) | 计划总览 |
| [phases/](./plans/phases/) | 各阶段实施文档 |