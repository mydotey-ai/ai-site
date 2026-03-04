# 开发计划

## 项目概览

AI-Site 是一个个人网站项目，包含作品展示、技术博客、创作内容和多媒体展示等功能。

## 开发阶段

| 阶段 | 名称 | 目标 | 状态 |
|------|------|------|------|
| Phase 0 | 项目设置 | 建立项目结构、规范、文档框架 | ✅ 已完成 |
| Phase 1 | 基础设施 | 配置、安全、数据库基础设施 | 待开始 |
| Phase 2 | 认证模块 | 用户注册、登录、权限管理 | 待开始 |
| Phase 3 | 博客模块 | 文章、分类、标签管理 | 待开始 |
| Phase 4 | 作品集模块 | 项目、技能、经历展示 | 待开始 |
| Phase 5 | 创作模块 | 小说、诗歌、散文展示 | 待开始 |
| Phase 6 | 多媒体模块 | 图片、视频、相册管理 | 待开始 |

## 阶段详情

详细阶段文档位于 [phases/](./phases/) 目录：

- [Phase 0: 项目设置](./phases/phase-0-project-setup.md) - ✅ 已完成 (2026-03-04)
- [Phase 1: 基础设施](./phases/phase-1-infrastructure.md)
- [Phase 2: 认证模块](./phases/phase-2-auth-module.md)
- [Phase 3: 博客模块](./phases/phase-3-blog-module.md)
- [Phase 4: 作品集模块](./phases/phase-4-portfolio-module.md)
- [Phase 5: 创作模块](./phases/phase-5-creation-module.md)
- [Phase 6: 多媒体模块](./phases/phase-6-media-module.md)

## 技术栈

### 前端

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.5+ | 核心框架 |
| Vite | 6.x | 构建工具 |
| TypeScript | 5.x | 类型系统 |
| Pinia | 最新 | 状态管理 |
| Naive UI | 最新 | UI 组件库 |

### 后端

| 技术 | 版本 | 用途 |
|------|------|------|
| JDK | 25 | 运行环境 |
| Spring Boot | 3.5.x | 核心框架 |
| MyBatis Plus | 最新 | ORM 框架 |
| Spring Security | 6.x | 安全框架 |

### 数据库

| 数据库 | 用途 |
|--------|------|
| MySQL 8.0+ | 生产环境 |
| SQLite 3.x | 开发/轻量部署 |

## 开发规范

详见 [Claude 规则文件](/.claude/rules/)。

## 文档索引

- [架构文档](../architecture/)
- [API 文档](../api/)
- [数据库文档](../database/)
- [开发文档](../development/)
- [部署文档](../deployment/)