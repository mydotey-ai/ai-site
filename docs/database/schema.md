# 数据库设计

## 数据库概述

项目支持 MySQL 和 SQLite 两种数据库：

| 数据库 | 用途 | 特点 |
|--------|------|------|
| MySQL 8.0+ | 生产环境 | 高性能、高可用 |
| SQLite 3.x | 开发/轻量部署 | 零配置、单文件 |

---

## 表结构设计

### 用户相关表

#### user (用户表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名 (唯一) |
| password | VARCHAR(255) | 密码 (加密) |
| email | VARCHAR(100) | 邮箱 (唯一) |
| nickname | VARCHAR(50) | 昵称 |
| avatar | VARCHAR(255) | 头像 URL |
| status | TINYINT | 状态: 0-禁用, 1-启用 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### role (角色表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 角色名称 |
| code | VARCHAR(50) | 角色编码 (唯一) |
| created_at | DATETIME | 创建时间 |

#### user_role (用户角色关联表)

| 字段 | 类型 | 说明 |
|------|------|------|
| user_id | BIGINT | 用户 ID |
| role_id | BIGINT | 角色 ID |

---

### 博客相关表

#### article (文章表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(200) | 标题 |
| slug | VARCHAR(200) | URL 别名 (唯一) |
| summary | VARCHAR(500) | 摘要 |
| content | LONGTEXT | 内容 (Markdown/富文本) |
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

#### category (分类表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 分类名称 |
| slug | VARCHAR(50) | URL 别名 |
| description | VARCHAR(200) | 分类描述 |
| parent_id | BIGINT | 父分类 ID (支持层级) |
| sort | INT | 排序 |
| article_count | INT | 文章数量 (冗余字段) |
| created_at | DATETIME | 创建时间 |

#### tag (标签表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 标签名称 |
| slug | VARCHAR(50) | URL 别名 |
| color | VARCHAR(20) | 标签颜色 (HEX) |
| article_count | INT | 文章数量 (冗余字段) |
| created_at | DATETIME | 创建时间 |

#### article_tag (文章标签关联表)

| 字段 | 类型 | 说明 |
|------|------|------|
| article_id | BIGINT | 文章 ID |
| tag_id | BIGINT | 标签 ID |

**联合主键：** `(article_id, tag_id)`

#### comment (评论表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| article_id | BIGINT | 文章 ID |
| nickname | VARCHAR(50) | 昵称 |
| email | VARCHAR(100) | 邮箱 |
| website | VARCHAR(200) | 网站 (可选) |
| content | TEXT | 评论内容 |
| ip | VARCHAR(50) | IP 地址 |
| user_agent | VARCHAR(500) | 浏览器信息 |
| status | VARCHAR(20) | 状态: PENDING / APPROVED / SPAM |
| created_at | DATETIME | 创建时间 |

**索引设计：**
- `idx_article_id` - 文章索引
- `idx_status` - 状态索引

---

### 作品集相关表

#### project (项目表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(100) | 项目名称 |
| slug | VARCHAR(100) | URL 别名 |
| description | VARCHAR(500) | 项目描述 |
| content | TEXT | 项目详情 (Markdown, 可选) |
| cover_image | VARCHAR(255) | 封面图 URL |
| tech_stack | JSON | 技术栈列表 |
| status | VARCHAR(20) | 状态: DEVELOPING / RELEASED / ARCHIVED |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

**技术栈 JSON 格式：**
```json
["Vue.js", "Spring Boot", "MySQL", "Redis"]
```

#### project_link (项目链接表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| project_id | BIGINT | 项目 ID |
| type | VARCHAR(20) | 链接类型: DEMO / SOURCE / DOCS / OTHER |
| label | VARCHAR(50) | 链接标签 (显示名称) |
| url | VARCHAR(500) | 链接地址 |
| sort | INT | 排序 |

**链接类型说明：**
- `DEMO` - 演示地址
- `SOURCE` - 源码地址
- `DOCS` - 文档地址
- `OTHER` - 其他链接

#### project_tag (项目标签表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 标签名称 |
| slug | VARCHAR(50) | URL 别名 |
| color | VARCHAR(20) | 标签颜色 (HEX) |
| sort | INT | 排序 |

#### project_tag_relation (项目标签关联表)

| 字段 | 类型 | 说明 |
|------|------|------|
| project_id | BIGINT | 项目 ID |
| tag_id | BIGINT | 标签 ID |

**联合主键：** `(project_id, tag_id)`

---

### 创作相关表

#### novel (小说表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| slug | VARCHAR(100) | URL 别名 (唯一) |
| author | VARCHAR(50) | 作者 |
| summary | VARCHAR(1000) | 简介 |
| cover_image | VARCHAR(255) | 封面图 URL |
| category_id | BIGINT | 分类 ID |
| status | VARCHAR(20) | 状态: DRAFT / PUBLISHED / COMPLETED |
| word_count | INT | 总字数 (冗余字段) |
| chapter_count | INT | 章节数 (冗余字段) |
| view_count | INT | 浏览量 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

**索引设计：**
- `uk_slug` - slug 唯一索引
- `idx_category_id` - 分类索引
- `idx_status` - 状态索引

#### chapter (章节表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| novel_id | BIGINT | 小说 ID |
| title | VARCHAR(100) | 章节标题 |
| content | LONGTEXT | 章节内容 |
| word_count | INT | 字数 |
| chapter_no | INT | 章节序号 |
| status | VARCHAR(20) | 状态: DRAFT / PUBLISHED |
| view_count | INT | 浏览量 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

**索引设计：**
- `idx_novel_id` - 小说索引
- `uk_novel_chapter_no` - 小说+章节序号唯一索引

#### novel_category (小说分类表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 分类名称 |
| slug | VARCHAR(50) | URL 别名 |
| sort | INT | 排序 |

**预设分类：** 玄幻奇幻、武侠仙侠、都市言情、历史军事、科幻灵异、其他

#### poetry (诗歌表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| slug | VARCHAR(100) | URL 别名 |
| author | VARCHAR(50) | 作者 |
| content | TEXT | 内容 |
| category | VARCHAR(50) | 分类 |
| status | VARCHAR(20) | 状态: DRAFT / PUBLISHED |
| view_count | INT | 浏览量 |
| created_at | DATETIME | 创建时间 |

**分类：** 古体诗、近体诗、词、现代诗、其他

#### essay (散文表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| slug | VARCHAR(100) | URL 别名 |
| author | VARCHAR(50) | 作者 |
| summary | VARCHAR(500) | 摘要 |
| content | LONGTEXT | 内容 |
| category | VARCHAR(50) | 分类 |
| status | VARCHAR(20) | 状态: DRAFT / PUBLISHED |
| view_count | INT | 浏览量 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

---

### 多媒体相关表

#### image (图片表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| description | VARCHAR(500) | 描述 |
| original_name | VARCHAR(255) | 原始文件名 |
| file_name | VARCHAR(255) | 存储文件名 |
| url | VARCHAR(500) | 图片 URL |
| thumbnail_url | VARCHAR(500) | 缩略图 URL |
| width | INT | 宽度 |
| height | INT | 高度 |
| size | BIGINT | 文件大小 (字节) |
| mime_type | VARCHAR(50) | MIME 类型 |
| album_id | BIGINT | 相册 ID |
| folder_id | BIGINT | 文件夹 ID |
| tags | VARCHAR(255) | 标签 (逗号分隔) |
| is_public | TINYINT(1) | 是否公开 |
| view_count | INT | 浏览量 |
| created_at | DATETIME | 创建时间 |

**索引设计：**
- `idx_album_id` - 相册索引
- `idx_folder_id` - 文件夹索引
- `idx_is_public` - 公开状态索引
- `idx_created_at` - 创建时间索引

#### video (视频表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| description | VARCHAR(500) | 描述 |
| cover_image | VARCHAR(255) | 封面图 URL |
| type | VARCHAR(20) | 类型: LOCAL / EXTERNAL |
| url | VARCHAR(500) | 视频 URL 或嵌入代码 |
| file_name | VARCHAR(255) | 存储文件名 (本地上传) |
| duration | INT | 时长 (秒) |
| size | BIGINT | 文件大小 (字节) |
| category | VARCHAR(50) | 分类 |
| tags | VARCHAR(255) | 标签 |
| is_public | TINYINT(1) | 是否公开 |
| view_count | INT | 浏览量 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### audio (音频表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| description | VARCHAR(500) | 描述 |
| cover_image | VARCHAR(255) | 封面图 URL |
| type | VARCHAR(20) | 类型: LOCAL / EXTERNAL |
| url | VARCHAR(500) | 音频 URL 或嵌入代码 |
| file_name | VARCHAR(255) | 存储文件名 |
| duration | INT | 时长 (秒) |
| size | BIGINT | 文件大小 (字节) |
| category | VARCHAR(50) | 分类 |
| tags | VARCHAR(255) | 标签 |
| is_public | TINYINT(1) | 是否公开 |
| view_count | INT | 播放量 |
| created_at | DATETIME | 创建时间 |

#### album (相册表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(100) | 相册名称 |
| slug | VARCHAR(100) | URL 别名 |
| description | VARCHAR(500) | 描述 |
| cover_image | VARCHAR(255) | 封面图 URL |
| image_count | INT | 图片数量 (冗余字段) |
| is_public | TINYINT(1) | 是否公开 |
| sort | INT | 排序 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### folder (文件夹表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(100) | 文件夹名称 |
| parent_id | BIGINT | 父文件夹 ID (支持层级) |
| type | VARCHAR(20) | 类型: IMAGE / VIDEO / AUDIO |
| sort | INT | 排序 |
| created_at | DATETIME | 创建时间 |

---

## 全文搜索配置

### MySQL 全文索引

```sql
-- 文章表全文索引
ALTER TABLE article ADD FULLTEXT INDEX ft_title_content(title, content);

-- 搜索查询（自然语言模式）
SELECT * FROM article
WHERE MATCH(title, content) AGAINST('关键词' IN NATURAL LANGUAGE MODE)
AND status = 'PUBLISHED';
```

### SQLite FTS5

```sql
-- 创建 FTS5 虚拟表
CREATE VIRTUAL TABLE article_fts USING fts5(
    title,
    content,
    content='article',
    content_rowid='id'
);

-- 搜索查询
SELECT a.* FROM article a
JOIN article_fts fts ON a.id = fts.rowid
WHERE article_fts MATCH '关键词'
AND a.status = 'PUBLISHED';
```

---

## 数据库迁移

使用 Flyway 管理数据库版本：

```
src/main/resources/db/migration/
├── V1.0.0__init_schema.sql        # 初始化用户相关表
├── V1.1.0__add_blog_tables.sql    # 添加博客相关表
├── V1.2.0__add_portfolio_tables.sql # 添加作品集相关表
├── V1.3.0__add_creation_tables.sql # 添加创作相关表
└── V1.4.0__add_media_tables.sql   # 添加多媒体相关表
```