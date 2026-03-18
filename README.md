# AI-Site

个人网站项目 - 包含作品展示、技术博客、创作内容和多媒体展示等功能。

## 技术栈

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5+ | 核心框架 |
| Vite | 6.x | 构建工具 |
| TypeScript | 5.6+ | 类型系统 |
| Pinia | 2.2+ | 状态管理 |
| Vue Router | 4.4+ | 路由管理 |
| Naive UI | 2.40+ | UI 组件库 |
| Vitest | 2.0+ | 测试框架 |

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| JDK | 25 | 运行环境 |
| Spring Boot | 3.5.0 | 核心框架 |
| MyBatis Plus | 3.5.9 | ORM 框架 |
| Spring Security | 6.x | 安全框架 |
| Flyway | 10.10 | 数据库迁移 |
| SpringDoc | 2.3.0 | API 文档 |

### 数据库

| 数据库 | 用途 |
|--------|------|
| MySQL 8.0+ | 生产环境 |
| SQLite 3.x | 开发/轻量部署 |

## 项目结构

```
ai-site/
├── .claude/           # Claude AI 记忆和规则
│   ├── CLAUDE.md      # 主记忆文件
│   ├── memory/        # 详细记忆
│   └── rules/         # 开发规则
├── .github/           # GitHub 配置
│   ├── workflows/     # CI/CD 工作流
│   └── ISSUE_TEMPLATE/
├── docs/              # 项目文档
│   ├── architecture/  # 架构文档
│   ├── api/           # API 文档
│   ├── database/      # 数据库文档
│   ├── design/        # 设计文档
│   ├── development/   # 开发文档
│   ├── deployment/    # 部署文档
│   ├── decisions/     # 架构决策记录
│   └── plans/         # 开发计划
├── frontend/          # 前端项目
│   ├── admin/         # 管理后台
│   ├── site/          # 用户网站
│   └── shared/        # 共享代码
├── backend/           # 后端项目
│   ├── common/        # 公共模块
│   └── service/       # 服务模块 (DDD 分层)
└── scripts/           # 脚本工具
```

## 快速开始

### 环境要求

- JDK 25+
- Node.js 20+
- npm 或 pnpm
- MySQL 8.0+ (可选，开发环境使用 SQLite)

### 一键启动

```bash
# 启动所有服务
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

### 单独启动

#### 前端

```bash
# 安装依赖
cd frontend/admin && npm install

# 启动管理后台
cd frontend/admin && npm run dev

# 启动用户网站
cd frontend/site && npm run dev
```

#### 后端

```bash
# 编译并启动
cd backend/service && mvn spring-boot:run
```

## 文档

详细文档请参阅 [docs/](./docs/) 目录。

- [架构文档](./docs/architecture/)
- [API 文档](./docs/api/)
- [数据库文档](./docs/database/)
- [设计文档](./docs/design/)
- [开发文档](./docs/development/)
- [部署文档](./docs/deployment/)
- [架构决策记录](./docs/decisions/)
- [开发计划](./docs/plans/)

## 开发规范

详见 [.claude/rules/](./.claude/rules/) 目录。

## 许可证

MIT License