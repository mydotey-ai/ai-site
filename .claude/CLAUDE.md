# AI-Site 项目

## 概览

- **类型**: 个人网站 (Portfolio + Blog + 创作秀 + 多媒体)
- **架构**: 前后端分离 + Monorepo
- **前端**: Vue 3.5 + Vite 6 + TypeScript (admin + site)
- **后端**: Spring Boot 3.5 + MyBatis Plus + JDK 25 + DDD
- **数据库**: MySQL (生产) / SQLite (开发)

## 关键路径

| 路径 | 说明 |
|------|------|
| `/frontend/admin/` | 管理后台前端 |
| `/frontend/site/` | 用户网站前端 |
| `/backend/common/` | 后端公共模块 |
| `/backend/service/` | 后端服务模块 |
| `/docs/` | 项目文档 |
| `/docs/plans/` | 开发计划 |
| `/scripts/dev.sh` | 一键启动脚本 |

## 快速启动

```bash
# 启动所有服务（后端 + Admin + Site）
./scripts/dev.sh start

# 查看服务状态
./scripts/dev.sh status

# 查看日志
./scripts/dev.sh log [backend|admin|site|all]

# 停止服务
./scripts/dev.sh stop

# 重启服务
./scripts/dev.sh restart
```

服务地址：
- 后端 API: http://localhost:8080
- Admin: http://localhost:3000
- Site: http://localhost:3001

默认登录凭据：`admin` / `admin123`

## 开发规则

- [全局规则](./rules/global.md)
- [前端规则](./rules/frontend.md)
- [后端规则](./rules/backend.md)

## 项目文档

详细文档索引见 [docs/README.md](../docs/README.md)。新文档须按该文件规范创建、命名和放置。