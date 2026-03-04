# 全局开发规则

## 核心原则

### 1. 代码质量
- 遵循 SOLID 原则
- 保持代码简洁、可读
- 编写可测试的代码
- 避免过度设计

### 2. 安全性
- 所有用户输入必须验证
- 敏感信息不得提交到代码库
- API 接口需要权限控制
- 防止 SQL 注入、XSS 等攻击

### 3. 性能
- 避免不必要的数据库查询
- 合理使用缓存
- 注意内存使用
- 优化关键路径

---

## Git 规范

### 分支策略

```
main ──────●──────●──────●──────→ (生产稳定版本)
           │      │
           │      └── release/vX.Y.Z ──→ 发布分支
           │
           └── develop ──────────────→ (开发主分支)
                  │
                  ├── feature/xxx ──→ 功能分支
                  ├── bugfix/xxx ───→ 修复分支
                  └── hotfix/xxx ───→ 紧急修复
```

### 分支命名

| 类型 | 格式 | 示例 |
|------|------|------|
| 功能 | `feature/<name>` | `feature/user-auth` |
| 修复 | `bugfix/<name>` | `bugfix/login-error` |
| 紧急修复 | `hotfix/<name>` | `hotfix/security-patch` |
| 发布 | `release/v<version>` | `release/v1.0.0` |
| 文档 | `docs/<name>` | `docs/api-guide` |
| 重构 | `refactor/<name>` | `refactor/user-service` |

### Commit 规范

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Type**:
- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档
- `style`: 格式
- `refactor`: 重构
- `test`: 测试
- `chore`: 构建/工具
- `perf`: 性能

**示例**:
```
feat(blog): 添加文章评论功能

- 支持嵌套评论
- 添加评论点赞功能

Closes #123
```

---

## Code Review 规范

### 自动化 Review
- 使用 Code Review Agent 自动审查代码
- PR 创建/更新时自动触发
- 审查内容：代码质量、安全性、性能、规范遵循

### Review 清单
- [ ] 代码是否符合规范
- [ ] 是否有安全风险
- [ ] 是否有性能问题
- [ ] 测试是否充分
- [ ] 文档是否更新

---

## 文档规范

### 文档位置
- 项目文档：`/docs/`
- API 文档：`/docs/api/`
- 架构文档：`/docs/architecture/`
- 开发计划：`/docs/plans/`

### 文档格式
- 使用 Markdown 格式
- 标题层级清晰
- 包含必要的代码示例
- 及时更新

---

## 测试规范

### 测试原则
- 单元测试覆盖核心逻辑
- 集成测试覆盖关键流程
- E2E 测试覆盖用户场景

### 测试命名
```
// 前端
describe('ComponentName', () => {
  it('should render correctly', () => {})
  it('should handle click event', () => {})
})

// 后端
class UserServiceTest {
  @Test
  void shouldCreateUser() {}
  @Test
  void shouldThrowExceptionWhenUserExists() {}
}
```

---

## 环境管理

### 环境变量
- 开发环境：`.env.development`
- 生产环境：`.env.production`
- 敏感配置：使用环境变量注入，不提交到代码库

### 配置文件
- 后端：`application-{profile}.yml`
- 前端：`.env.{mode}`
- 示例文件：提供 `.example` 模板