# API 文档索引

## API 服务

### 认证模块 (Auth)
- [登录接口](./auth-api.md#登录)
- [注册接口](./auth-api.md#注册)
- [Token 刷新](./auth-api.md#token刷新)

### 博客模块 (Blog)
- [文章接口](./blog-api.md#文章)
- [分类接口](./blog-api.md#分类)
- [标签接口](./blog-api.md#标签)

### 作品集模块 (Portfolio)
- [项目接口](./portfolio-api.md#项目)
- [技能接口](./portfolio-api.md#技能)
- [经历接口](./portfolio-api.md#经历)

### 创作模块 (Creation)
- [小说接口](./creation-api.md#小说)
- [章节接口](./creation-api.md#章节)
- [诗歌接口](./creation-api.md#诗歌)
- [散文接口](./creation-api.md#散文)

### 多媒体模块 (Media)
- [图片接口](./media-api.md#图片)
- [视频接口](./media-api.md#视频)
- [相册接口](./media-api.md#相册)

## API 基础信息

### Base URL

| 环境 | URL |
|------|-----|
| 开发 | `http://localhost:8080` |
| 生产 | `https://api.example.com` |

### 认证方式

使用 JWT Bearer Token 认证：

```
Authorization: Bearer <token>
```

### 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { ... },
  "timestamp": "2024-01-15T10:30:00"
}
```

### 错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器错误 |

## OpenAPI 文档

- Swagger UI: `/swagger-ui.html`
- OpenAPI JSON: `/v3/api-docs`