# Git 规则

## 分支命名

| 类型 | 格式 | 示例 |
|------|------|------|
| 功能 | `feature/<name>` | `feature/user-auth` |
| 修复 | `bugfix/<name>` | `bugfix/login-error` |
| 紧急修复 | `hotfix/<name>` | `hotfix/security-patch` |
| 发布 | `release/v<version>` | `release/v1.0.0` |
| 文档 | `docs/<name>` | `docs/api-guide` |

## Commit 规范

格式：`<type>(<scope>): <subject>`

**Type**: `feat` | `fix` | `docs` | `style` | `refactor` | `test` | `chore` | `perf`

## 提交控制

- **禁止自动提交**：未经用户明确确认，不得执行 `git commit` 或 `git push`
- 只有用户明确要求时（如"提交"、"commit"、"帮我提交"）才可执行 commit
- 执行 commit 前必须展示变更摘要，等待用户确认
- 禁止在完成代码修改后自动触发 commit
