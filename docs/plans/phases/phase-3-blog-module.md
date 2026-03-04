# Phase 3: 博客模块

## 阶段目标

实现博客功能，包括：
- 文章管理 (CRUD)
- 分类管理
- 标签管理
- 前端博客页面

## 任务清单

### 1. 后端 - Blog 领域

#### 接入层
- [ ] BlogController
- [ ] ArticleRequest, ArticleResponse
- [ ] CategoryRequest, CategoryResponse
- [ ] TagRequest, TagResponse

#### 应用服务层
- [ ] BlogCommandService
  - [ ] createArticle
  - [ ] updateArticle
  - [ ] deleteArticle
  - [ ] publishArticle
- [ ] BlogQueryService
  - [ ] getArticleById
  - [ ] getArticleList
  - [ ] getArticleBySlug

#### 领域层
- [ ] Article Entity
- [ ] Category Entity
- [ ] Tag Entity
- [ ] ArticleRepository
- [ ] CategoryRepository
- [ ] TagRepository

#### 基础设施层
- [ ] ArticleMapper
- [ ] CategoryMapper
- [ ] TagMapper
- [ ] ArticleRepositoryImpl

### 2. 数据库

- [ ] article 表
- [ ] category 表
- [ ] tag 表
- [ ] article_tag 表

### 3. 前端 - 博客页面

#### Admin
- [ ] ArticleList.vue
- [ ] ArticleEdit.vue
- [ ] ArticleEditor.vue (Markdown 编辑器)
- [ ] CategoryManage.vue
- [ ] TagManage.vue

#### Site
- [ ] BlogList.vue
- [ ] BlogDetail.vue
- [ ] BlogCategory.vue
- [ ] ArticleCard.vue

## API 设计

### 文章列表

```
GET /api/v1/articles?page=1&size=20&status=PUBLISHED
Response:
{
  "code": 200,
  "data": {
    "list": [ ... ],
    "total": 100
  }
}
```

### 文章详情

```
GET /api/v1/articles/{id}
或
GET /api/v1/articles/slug/{slug}
```

### 创建文章

```
POST /admin/v1/articles
Request:
{
  "title": "string",
  "content": "string (Markdown)",
  "categoryId": 1,
  "tagIds": [1, 2, 3]
}
```

## 验收标准

- [ ] 文章可以创建、编辑、删除
- [ ] 分类可以管理
- [ ] 标签可以管理
- [ ] 文章可以按分类/标签筛选
- [ ] 前端博客页面可正常访问

## 下一阶段

完成 Phase 3 后，进入 [Phase 4: 作品集模块](./phase-4-portfolio-module.md)。