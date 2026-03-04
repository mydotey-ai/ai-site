# Phase 0: 项目设置

## 阶段目标

建立项目的基础设施，包括：
- 项目目录结构
- Claude 记忆文件和规则
- 文档框架
- 配置文件
- 前后端项目骨架

## 任务清单

### 1. 项目目录结构

- [x] 创建根目录结构
- [x] 创建前端目录结构 (admin/site/shared)
- [x] 创建后端目录结构 (common/api/admin)
- [x] 创建文档目录结构

### 2. Claude 记忆文件

- [x] 创建 CLAUDE.md 主记忆文件
- [x] 创建 architecture.md 架构决策记录
- [x] 创建 conventions.md 编码约定
- [x] 创建 domain-knowledge.md 领域知识
- [x] 创建 troubleshooting.md 问题排查记录

### 3. Claude 规则文件

- [x] 创建 global.md 全局规则
- [x] 创建 frontend.md 前端规则
- [x] 创建 backend.md 后端规则

### 4. 文档框架

- [x] 创建 docs/README.md
- [x] 创建架构文档
- [x] 创建 API 文档索引
- [x] 创建数据库文档
- [x] 创建开发文档
- [x] 创建部署文档
- [x] 创建开发计划文档
- [x] 创建各阶段文档框架

### 5. 配置文件

- [ ] 创建根目录 .gitignore
- [ ] 创建 .editorconfig
- [ ] 创建 pnpm-workspace.yaml
- [ ] 创建前端 .gitignore
- [ ] 创建后端 .gitignore
- [ ] 创建 GitHub workflow
- [ ] 创建 PR 模板

### 6. 前端项目骨架

- [ ] 创建 admin 项目基础文件
- [ ] 创建 site 项目基础文件
- [ ] 创建 shared 项目基础文件
- [ ] 创建 package.json
- [ ] 创建 vite.config.ts
- [ ] 创建 tsconfig.json

### 7. 后端项目骨架

- [ ] 创建父 pom.xml
- [ ] 创建 common 模块 pom.xml
- [ ] 创建 api 模块 pom.xml
- [ ] 创建 admin 模块 pom.xml
- [ ] 创建启动类
- [ ] 创建 application.yml

## 产出物

### 目录结构

```
ai-site/
├── .claude/
│   ├── CLAUDE.md
│   ├── memory/
│   └── rules/
├── .github/
├── docs/
│   ├── architecture/
│   ├── api/
│   ├── database/
│   ├── development/
│   ├── deployment/
│   └── plans/
│       └── phases/
├── frontend/
│   ├── admin/
│   ├── site/
│   └── shared/
├── backend/
│   ├── common/
│   ├── api/
│   └── admin/
└── scripts/
```

### 文档清单

- 项目概览文档
- 架构设计文档
- 编码规范文档
- 开发计划文档
- 各阶段实施文档

## 验收标准

- [ ] 所有目录结构创建完成
- [ ] Claude 记忆文件完整可用
- [ ] 规则文件内容完整
- [ ] 文档框架搭建完成
- [ ] 配置文件创建完成
- [ ] 前端项目可启动
- [ ] 后端项目可启动

## 下一阶段

完成 Phase 0 后，进入 [Phase 1: 基础设施](./phase-1-infrastructure.md)。