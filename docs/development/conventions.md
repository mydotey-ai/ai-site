# 编码规范

详细的编码规范请参考 [Claude 规则文件](/.claude/rules/)。

## 规范文档

- [全局规则](/.claude/rules/global.md) - Git、Code Review、文档规范
- [前端规则](/.claude/rules/frontend.md) - Vue、TypeScript、样式规范
- [后端规则](/.claude/rules/backend.md) - Java、Spring Boot、MyBatis Plus 规范

## 快速参考

### Git Commit 规范

```
<type>(<scope>): <subject>

# 示例
feat(blog): 添加文章评论功能
fix(auth): 修复 token 过期判断
docs(api): 更新登录接口文档
```

### 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 类名 | PascalCase | `ArticleController` |
| 方法名 | camelCase | `getArticleById` |
| 变量名 | camelCase | `articleList` |
| 常量名 | UPPER_SNAKE_CASE | `MAX_PAGE_SIZE` |
| 文件名 | PascalCase | `ArticleCard.vue` |

### API 响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { ... },
  "timestamp": "2024-01-15T10:30:00"
}
```