# Phase 3: 博客模块

## 阶段目标

实现完整的博客系统，包括：
- 文章管理（双编辑器、多状态、置顶、搜索）
- 分类与标签管理
- 评论系统
- 归档与统计
- SEO 优化

---

## 1. 需求分析

### 1.1 目标用户

| 用户类型 | 需求 |
|---------|------|
| 网站主人 | 发布技术文章、管理内容、查看统计数据 |
| 访客 | 浏览文章、搜索内容、发表评论 |
| HR/面试官 | 了解技术能力、查看项目经验 |

### 1.2 核心功能

| 功能 | 优先级 | 说明 |
|------|--------|------|
| 文章 CRUD | P0 | 创建、编辑、删除文章 |
| 双编辑器 | P0 | Markdown + 富文本，可切换 |
| 分类管理 | P0 | 文章分类，支持层级 |
| 标签管理 | P0 | 文章标签，多对多关系 |
| 文章状态 | P0 | 草稿/已发布/隐藏 |
| 文章置顶 | P1 | 重要文章置顶显示 |
| 全文搜索 | P1 | 标题+内容搜索 |
| 归档功能 | P1 | 按年月归档文章 |
| 阅读统计 | P1 | 记录文章浏览量 |
| 相关文章 | P2 | 基于标签推荐相关文章 |
| 基础评论 | P2 | 访客评论，无需登录 |
| 基础 SEO | P1 | Title、Description、Keywords |

### 1.3 用户场景

#### 场景 1：发布技术文章

```
用户：网站主人
目标：发布一篇技术文章

流程：
1. 登录管理后台
2. 进入文章管理，点击"新建文章"
3. 选择编辑器类型（Markdown/富文本）
4. 填写标题、内容
5. 选择分类、添加标签
6. 设置 SEO 信息（可选）
7. 保存为草稿 或 直接发布
```

#### 场景 2：浏览博客

```
用户：访客
目标：浏览并阅读文章

流程：
1. 访问博客首页
2. 浏览文章列表（置顶文章在前）
3. 点击文章进入详情页
4. 阅读文章内容
5. 查看相关文章推荐
6. 发表评论（可选）
```

#### 场景 3：搜索文章

```
用户：访客
目标：搜索特定主题的文章

流程：
1. 在搜索框输入关键词
2. 系统搜索标题和内容
3. 显示匹配的文章列表
4. 点击文章阅读
```

#### 场景 4：归档浏览

```
用户：访客
目标：按时间浏览历史文章

流程：
1. 访问归档页面
2. 选择年份/月份
3. 查看该时间段的文章列表
```

---

## 2. 数据建模

### 2.1 实体关系图

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Article   │────▶│  Category   │     │     Tag     │
└─────────────┘     └─────────────┘     └─────────────┘
       │                                        │
       │            ┌─────────────┐            │
       └───────────▶│ ArticleTag  │◀───────────┘
                    └─────────────┘
       │
       │            ┌─────────────┐
       └───────────▶│   Comment   │
                    └─────────────┘
```

### 2.2 文章表 (article)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(200) | 标题 |
| slug | VARCHAR(200) | URL 别名（唯一） |
| summary | VARCHAR(500) | 摘要（自动生成或手动填写） |
| content | LONGTEXT | 内容 |
| content_type | VARCHAR(20) | 内容类型: MARKDOWN / RICHTEXT |
| category_id | BIGINT | 分类 ID |
| status | VARCHAR(20) | 状态: DRAFT / PUBLISHED / HIDDEN |
| is_top | TINYINT(1) | 是否置顶: 0-否, 1-是 |
| view_count | INT | 浏览量 |
| seo_title | VARCHAR(100) | SEO 标题 |
| seo_description | VARCHAR(200) | SEO 描述 |
| seo_keywords | VARCHAR(200) | SEO 关键词 |
| published_at | DATETIME | 发布时间 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

**索引设计：**
- `uk_slug` - slug 唯一索引
- `idx_category_id` - 分类索引
- `idx_status` - 状态索引
- `idx_published_at` - 发布时间索引
- `idx_is_top` - 置顶索引

**全文搜索：**
- MySQL: `FULLTEXT INDEX ft_title_content ON article(title, content)`
- SQLite: 使用 FTS5 虚拟表

### 2.3 分类表 (category)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 分类名称 |
| slug | VARCHAR(50) | URL 别名 |
| description | VARCHAR(200) | 分类描述 |
| parent_id | BIGINT | 父分类 ID（支持层级） |
| sort | INT | 排序（越小越前） |
| article_count | INT | 文章数量（冗余字段，定时更新） |
| created_at | DATETIME | 创建时间 |

### 2.4 标签表 (tag)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 标签名称 |
| slug | VARCHAR(50) | URL 别名 |
| color | VARCHAR(20) | 标签颜色（HEX） |
| article_count | INT | 文章数量（冗余字段） |
| created_at | DATETIME | 创建时间 |

### 2.5 文章标签关联表 (article_tag)

| 字段 | 类型 | 说明 |
|------|------|------|
| article_id | BIGINT | 文章 ID |
| tag_id | BIGINT | 标签 ID |

**联合主键：** `(article_id, tag_id)`

### 2.6 评论表 (comment)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| article_id | BIGINT | 文章 ID |
| nickname | VARCHAR(50) | 昵称 |
| email | VARCHAR(100) | 邮箱（不公开显示） |
| website | VARCHAR(200) | 网站（可选） |
| content | TEXT | 评论内容 |
| ip | VARCHAR(50) | IP 地址 |
| user_agent | VARCHAR(500) | 浏览器信息 |
| status | VARCHAR(20) | 状态: PENDING / APPROVED / SPAM |
| created_at | DATETIME | 创建时间 |

**索引设计：**
- `idx_article_id` - 文章索引
- `idx_status` - 状态索引

---

## 3. API 设计

### 3.1 接口概览

| 模块 | 接口 | 方法 | 说明 |
|------|------|------|------|
| **文章** | /api/v1/articles | GET | 文章列表（公开） |
| | /api/v1/articles/{id} | GET | 文章详情 |
| | /api/v1/articles/slug/{slug} | GET | 按 slug 获取文章 |
| | /admin/v1/articles | POST | 创建文章 |
| | /admin/v1/articles/{id} | PUT | 更新文章 |
| | /admin/v1/articles/{id} | DELETE | 删除文章 |
| | /api/v1/articles/search | GET | 搜索文章 |
| | /api/v1/articles/archive | GET | 归档统计 |
| **分类** | /api/v1/categories | GET | 分类列表 |
| | /admin/v1/categories | POST | 创建分类 |
| | /admin/v1/categories/{id} | PUT | 更新分类 |
| | /admin/v1/categories/{id} | DELETE | 删除分类 |
| **标签** | /api/v1/tags | GET | 标签列表 |
| | /admin/v1/tags | POST | 创建标签 |
| | /admin/v1/tags/{id} | PUT | 更新标签 |
| | /admin/v1/tags/{id} | DELETE | 删除标签 |
| **评论** | /api/v1/articles/{id}/comments | GET | 文章评论列表 |
| | /api/v1/articles/{id}/comments | POST | 发表评论 |
| | /admin/v1/comments | GET | 评论管理列表 |
| | /admin/v1/comments/{id}/approve | PUT | 审核通过 |
| | /admin/v1/comments/{id} | DELETE | 删除评论 |

### 3.2 文章接口详细设计

#### 获取文章列表

```
GET /api/v1/articles?page=1&size=10&categoryId=1&tagId=1&status=PUBLISHED

Query Parameters:
- page: 页码（默认 1）
- size: 每页数量（默认 10，最大 50）
- categoryId: 分类 ID（可选）
- tagId: 标签 ID（可选）
- status: 状态（可选，公开接口仅支持 PUBLISHED）

Response:
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "title": "文章标题",
        "slug": "article-slug",
        "summary": "文章摘要...",
        "contentType": "MARKDOWN",
        "category": {
          "id": 1,
          "name": "后端开发",
          "slug": "backend"
        },
        "tags": [
          { "id": 1, "name": "Java", "slug": "java", "color": "#f89820" },
          { "id": 2, "name": "Spring", "slug": "spring", "color": "#6db33f" }
        ],
        "isTop": true,
        "viewCount": 1234,
        "publishedAt": "2026-03-07T10:00:00",
        "createdAt": "2026-03-07T09:00:00"
      }
    ],
    "total": 100,
    "page": 1,
    "size": 10
  }
}
```

#### 获取文章详情

```
GET /api/v1/articles/{id}
或
GET /api/v1/articles/slug/{slug}

Response:
{
  "code": 200,
  "data": {
    "id": 1,
    "title": "文章标题",
    "slug": "article-slug",
    "summary": "文章摘要",
    "content": "# 文章内容\n\n...",
    "contentType": "MARKDOWN",
    "category": {
      "id": 1,
      "name": "后端开发",
      "slug": "backend"
    },
    "tags": [
      { "id": 1, "name": "Java", "slug": "java", "color": "#f89820" }
    ],
    "isTop": false,
    "viewCount": 1234,
    "seoTitle": "文章标题 - 我的博客",
    "seoDescription": "文章摘要...",
    "seoKeywords": "Java, Spring, 后端",
    "publishedAt": "2026-03-07T10:00:00",
    "createdAt": "2026-03-07T09:00:00",
    "updatedAt": "2026-03-07T11:00:00",
    "relatedArticles": [
      {
        "id": 2,
        "title": "相关文章1",
        "slug": "related-1",
        "summary": "摘要..."
      }
    ]
  }
}
```

#### 创建文章

```
POST /admin/v1/articles

Request:
{
  "title": "文章标题",
  "slug": "article-slug",           // 可选，自动生成
  "summary": "文章摘要",            // 可选，从内容提取
  "content": "# 标题\n\n内容...",
  "contentType": "MARKDOWN",        // MARKDOWN / RICHTEXT
  "categoryId": 1,
  "tagIds": [1, 2, 3],
  "status": "DRAFT",                // DRAFT / PUBLISHED / HIDDEN
  "isTop": false,
  "seoTitle": "SEO标题",            // 可选
  "seoDescription": "SEO描述",      // 可选
  "seoKeywords": "关键词1,关键词2"  // 可选
}

Response:
{
  "code": 200,
  "data": {
    "id": 1,
    "title": "文章标题",
    "slug": "article-slug"
  }
}
```

#### 搜索文章

```
GET /api/v1/articles/search?keyword=Spring&page=1&size=10

Query Parameters:
- keyword: 搜索关键词（必填，2-50字符）
- page: 页码
- size: 每页数量

Response:
{
  "code": 200,
  "data": {
    "list": [...],
    "total": 50,
    "keyword": "Spring"
  }
}
```

#### 获取归档统计

```
GET /api/v1/articles/archive

Response:
{
  "code": 200,
  "data": [
    {
      "year": 2026,
      "months": [
        {
          "month": 3,
          "count": 15
        },
        {
          "month": 2,
          "count": 10
        }
      ]
    },
    {
      "year": 2025,
      "months": [...]
    }
  ]
}
```

### 3.3 评论接口详细设计

#### 获取文章评论

```
GET /api/v1/articles/{articleId}/comments?page=1&size=20

Response:
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "nickname": "访客",
        "website": "https://example.com",
        "content": "很好的文章！",
        "createdAt": "2026-03-07T12:00:00"
      }
    ],
    "total": 10
  }
}
```

#### 发表评论

```
POST /api/v1/articles/{articleId}/comments

Request:
{
  "nickname": "访客昵称",
  "email": "visitor@example.com",
  "website": "https://example.com",  // 可选
  "content": "评论内容..."
}

Response:
{
  "code": 200,
  "message": "评论提交成功，等待审核"
}
```

---

## 4. 前端设计

### 4.1 页面结构

#### Site（用户端）

```
/blog                    博客首页（文章列表）
/blog/{slug}             文章详情
/blog/category/{slug}    分类文章列表
/blog/tag/{slug}         标签文章列表
/blog/archive            归档页面
/blog/search             搜索页面
```

#### Admin（管理端）

```
/admin/blog/articles     文章管理
/admin/blog/articles/new 新建文章
/admin/blog/articles/{id}/edit  编辑文章
/admin/blog/categories   分类管理
/admin/blog/tags         标签管理
/admin/blog/comments     评论管理
```

### 4.2 组件设计

#### 通用组件

| 组件 | 说明 |
|------|------|
| `ArticleCard.vue` | 文章卡片（列表项） |
| `CategorySelect.vue` | 分类选择器 |
| `TagSelect.vue` | 标签选择器（多选） |
| `ArticleList.vue` | 文章列表容器 |
| `Pagination.vue` | 分页组件 |
| `SearchBox.vue` | 搜索框 |

#### 编辑器组件

| 组件 | 说明 |
|------|------|
| `MarkdownEditor.vue` | Markdown 编辑器（基于 v-md-editor） |
| `RichTextEditor.vue` | 富文本编辑器（基于 @wangeditor） |
| `EditorSwitch.vue` | 编辑器切换组件 |

#### 页面组件

**Site 端：**

| 组件 | 说明 |
|------|------|
| `BlogList.vue` | 博客首页 - 文章列表 |
| `BlogDetail.vue` | 文章详情页 |
| `BlogCategory.vue` | 分类文章页 |
| `BlogTag.vue` | 标签文章页 |
| `BlogArchive.vue` | 归档页面 |
| `BlogSearch.vue` | 搜索结果页 |
| `CommentSection.vue` | 评论区组件 |
| `RelatedArticles.vue` | 相关文章推荐 |

**Admin 端：**

| 组件 | 说明 |
|------|------|
| `ArticleList.vue` | 文章管理列表 |
| `ArticleEdit.vue` | 文章编辑页 |
| `ArticleForm.vue` | 文章表单组件 |
| `CategoryManage.vue` | 分类管理 |
| `TagManage.vue` | 标签管理 |
| `CommentManage.vue` | 评论管理 |

### 4.3 页面布局设计

#### 博客首页

```
┌─────────────────────────────────────────────────────────┐
│  Header: Logo | 首页 | 博客 | 作品集 | 创作 | 关于     │
├─────────────────────────────────────────────────────────┤
│  Hero: 博客标题 + 搜索框                                │
├─────────────────────────────────────────────────────────┤
│  ┌──────────────────┐  ┌─────────────────────────────┐  │
│  │  侧边栏          │  │  文章列表                   │  │
│  │  ├─ 分类         │  │  ├─ [置顶] 文章1            │  │
│  │  ├─ 标签云       │  │  ├─ [置顶] 文章2            │  │
│  │  └─ 归档         │  │  ├─ 文章3                   │  │
│  │                  │  │  ├─ 文章4                   │  │
│  │                  │  │  └─ ...                     │  │
│  └──────────────────┘  └─────────────────────────────┘  │
├─────────────────────────────────────────────────────────┤
│  Footer: 版权信息                                       │
└─────────────────────────────────────────────────────────┘
```

#### 文章详情页

```
┌─────────────────────────────────────────────────────────┐
│  Header                                                  │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────┐    │
│  │  文章标题                                        │    │
│  │  分类: 后端开发 | 标签: Java, Spring            │    │
│  │  发布于 2026-03-07 | 阅读 1234 次               │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  文章内容                                        │    │
│  │  (Markdown 渲染 / 富文本渲染)                   │    │
│  │                                                  │    │
│  │  代码高亮、表格、图片等                          │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  相关文章推荐                                    │    │
│  │  [文章1] [文章2] [文章3]                        │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  评论区                                          │    │
│  │  ├─ 昵称、邮箱、网站（可选）                     │    │
│  │  ├─ 评论内容                                    │    │
│  │  └─ 评论列表                                    │    │
│  └─────────────────────────────────────────────────┘    │
├─────────────────────────────────────────────────────────┤
│  Footer                                                  │
└─────────────────────────────────────────────────────────┘
```

#### 管理后台 - 文章编辑页

```
┌─────────────────────────────────────────────────────────┐
│  Sidebar | 文章管理 > 新建文章                          │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────┐    │
│  │  标题: [________________________]               │    │
│  │  Slug: [________________________] (自动生成)    │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  编辑器类型: [Markdown ▼]                       │    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  编辑器区域                                      │    │
│  │  ┌─────────────┬─────────────────────────────┐  │    │
│  │  │ 工具栏      │                             │  │    │
│  │  ├─────────────┤  编辑/预览区域              │  │    │
│  │  │             │                             │  │    │
│  │  │             │                             │  │    │
│  │  └─────────────┴─────────────────────────────┘  │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  分类: [后端开发 ▼]  标签: [Java] [Spring] [+x]│    │
│  │  状态: [草稿 ▼]  置顶: [ ]                      │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  SEO 设置（可折叠）                              │    │
│  │  标题: [________________________]               │    │
│  │  描述: [________________________]               │    │
│  │  关键词: [________________________]             │    │
│  └─────────────────────────────────────────────────┘    │
│  [保存草稿] [发布]                                       │
└─────────────────────────────────────────────────────────┘
```

### 4.4 设计规范

#### 文章卡片样式

```scss
.article-card {
  padding: 16px;
  border-radius: 8px;
  background: var(--card-bg);
  transition: box-shadow 0.2s;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  &__top-badge {
    display: inline-block;
    padding: 2px 8px;
    background: var(--primary-color);
    color: white;
    border-radius: 4px;
    font-size: 12px;
  }

  &__title {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
    margin: 8px 0;
  }

  &__summary {
    font-size: 14px;
    color: var(--text-secondary);
    line-height: 1.6;
  }

  &__meta {
    display: flex;
    gap: 16px;
    margin-top: 12px;
    font-size: 12px;
    color: var(--text-muted);
  }
}
```

#### Markdown 编辑器集成

推荐使用 `@kangc/v-md-editor`（Vue 3 版本）：

```typescript
// 主要功能
- 实时预览
- 代码高亮（支持多种语言）
- 快捷键支持
- 图片上传
- 表格、流程图支持（可选）
```

#### 富文本编辑器集成

推荐使用 `@wangeditor/editor-for-vue`：

```typescript
// 主要功能
- 所见即所得
- 工具栏自定义
- 图片上传
- 代码块插入
- 表格支持
```

---

## 5. 技术实现要点

### 5.1 全文搜索实现

#### MySQL 方案

```sql
-- 创建全文索引
ALTER TABLE article ADD FULLTEXT INDEX ft_title_content(title, content);

-- 搜索查询（自然语言模式）
SELECT * FROM article
WHERE MATCH(title, content) AGAINST('关键词' IN NATURAL LANGUAGE MODE)
AND status = 'PUBLISHED';
```

#### SQLite 方案

```sql
-- 创建 FTS5 虚拟表
CREATE VIRTUAL TABLE article_fts USING fts5(title, content, content='article', content_rowid='id');

-- 搜索查询
SELECT a.* FROM article a
JOIN article_fts fts ON a.id = fts.rowid
WHERE article_fts MATCH '关键词'
AND a.status = 'PUBLISHED';
```

### 5.2 相关文章推荐算法

基于标签匹配的简单推荐：

```java
public List<Article> findRelatedArticles(Long articleId, int limit) {
    // 1. 获取当前文章的标签 ID 列表
    List<Long> tagIds = articleTagMapper.selectTagIdsByArticleId(articleId);

    if (tagIds.isEmpty()) {
        return Collections.emptyList();
    }

    // 2. 查找有相同标签的文章，按匹配标签数量排序
    return articleMapper.selectRelatedArticles(articleId, tagIds, limit);
}
```

```sql
-- 查询相关文章
SELECT a.*, COUNT(at.tag_id) as match_count
FROM article a
JOIN article_tag at ON a.id = at.article_id
WHERE at.tag_id IN (#{tagIds})
AND a.id != #{articleId}
AND a.status = 'PUBLISHED'
GROUP BY a.id
ORDER BY match_count DESC, a.published_at DESC
LIMIT #{limit};
```

### 5.3 文章统计

#### 浏览量统计

- 每次访问文章详情页 +1
- 可考虑防刷机制（同一 IP 短时间内只计一次）

#### 归档统计

```sql
SELECT
    YEAR(published_at) as year,
    MONTH(published_at) as month,
    COUNT(*) as count
FROM article
WHERE status = 'PUBLISHED'
GROUP BY YEAR(published_at), MONTH(published_at)
ORDER BY year DESC, month DESC;
```

### 5.4 评论审核流程

```
┌─────────┐    提交    ┌──────────┐   审核    ┌───────────┐
│  访客   │ ────────▶ │ PENDING  │ ────────▶ │ APPROVED  │
└─────────┘           └──────────┘           └───────────┘
                           │
                           │ 标记垃圾
                           ▼
                      ┌──────────┐
                      │   SPAM   │
                      └──────────┘
```

- 默认状态：PENDING（待审核）
- 管理员审核通过后：APPROVED（已批准）
- 垃圾评论：SPAM（垃圾）

---

## 6. 任务清单

### 6.1 后端开发

#### 接入层
- [ ] ArticleController（公开接口）
- [ ] ArticleAdminController（管理接口）
- [ ] CategoryController
- [ ] TagController
- [ ] CommentController
- [ ] ArticleRequest / ArticleResponse
- [ ] CategoryRequest / CategoryResponse
- [ ] TagRequest / TagResponse
- [ ] CommentRequest / CommentResponse

#### 应用服务层
- [ ] ArticleCommandService
- [ ] ArticleQueryService
- [ ] CategoryService
- [ ] TagService
- [ ] CommentService

#### 领域层
- [ ] Article Entity
- [ ] Category Entity
- [ ] Tag Entity
- [ ] Comment Entity
- [ ] ArticleRepository
- [ ] CategoryRepository
- [ ] TagRepository
- [ ] CommentRepository

#### 基础设施层
- [ ] ArticleMapper
- [ ] CategoryMapper
- [ ] TagMapper
- [ ] CommentMapper
- [ ] ArticleTagMapper
- [ ] 全文搜索实现

### 6.2 数据库
- [ ] article 表（新增字段）
- [ ] category 表
- [ ] tag 表
- [ ] article_tag 表
- [ ] comment 表
- [ ] 全文索引

### 6.3 前端开发

#### Site 端
- [ ] BlogList.vue（博客首页）
- [ ] BlogDetail.vue（文章详情）
- [ ] BlogCategory.vue（分类页）
- [ ] BlogTag.vue（标签页）
- [ ] BlogArchive.vue（归档页）
- [ ] BlogSearch.vue（搜索页）
- [ ] ArticleCard.vue（文章卡片）
- [ ] CommentSection.vue（评论区）
- [ ] RelatedArticles.vue（相关文章）

#### Admin 端
- [ ] ArticleList.vue（文章管理）
- [ ] ArticleEdit.vue（文章编辑）
- [ ] ArticleForm.vue（文章表单）
- [ ] MarkdownEditor.vue
- [ ] RichTextEditor.vue
- [ ] CategoryManage.vue
- [ ] TagManage.vue
- [ ] CommentManage.vue

---

## 7. 验收标准

### 功能验收
- [ ] 文章可以创建、编辑、删除、发布
- [ ] 支持 Markdown 和富文本两种编辑器
- [ ] 文章状态流转正常（草稿/已发布/隐藏）
- [ ] 文章可以置顶
- [ ] 分类可以管理（增删改查）
- [ ] 标签可以管理
- [ ] 文章可以按分类、标签筛选
- [ ] 全文搜索功能正常
- [ ] 归档功能正常
- [ ] 阅读统计准确
- [ ] 相关文章推荐正确
- [ ] 评论可以发表和管理
- [ ] SEO 信息正确渲染

### 性能验收
- [ ] 文章列表加载 < 500ms
- [ ] 文章详情加载 < 300ms
- [ ] 搜索响应 < 1s

### 兼容性验收
- [ ] MySQL 环境功能正常
- [ ] SQLite 环境功能正常

### 测试验收
- [ ] 后端单元测试覆盖率 > 60%
- [ ] 核心 API 集成测试通过
- [ ] 前端组件测试覆盖关键组件

---

## 下一阶段

完成 Phase 3 后，进入 [Phase 4: 作品集模块](./phase-4-portfolio-module.md)。