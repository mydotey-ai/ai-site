# 数据库设计

## 数据库概述

项目支持 MySQL 和 SQLite 两种数据库：

| 数据库 | 用途 | 特点 |
|--------|------|------|
| MySQL 8.0+ | 生产环境 | 高性能、高可用 |
| SQLite 3.x | 开发/轻量部署 | 零配置、单文件 |

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

### 博客相关表

#### article (文章表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(200) | 标题 |
| slug | VARCHAR(200) | URL 别名 (唯一) |
| summary | VARCHAR(500) | 摘要 |
| content | LONGTEXT | 内容 (Markdown) |
| cover_image | VARCHAR(255) | 封面图 |
| category_id | BIGINT | 分类 ID |
| status | TINYINT | 状态: 0-草稿, 1-已发布, 2-归档 |
| view_count | INT | 浏览量 |
| like_count | INT | 点赞数 |
| is_top | TINYINT | 是否置顶 |
| published_at | DATETIME | 发布时间 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |
| deleted | TINYINT | 逻辑删除 |

#### category (分类表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 分类名称 |
| slug | VARCHAR(50) | URL 别名 |
| parent_id | BIGINT | 父分类 ID |
| sort | INT | 排序 |
| created_at | DATETIME | 创建时间 |

#### tag (标签表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 标签名称 |
| slug | VARCHAR(50) | URL 别名 |
| color | VARCHAR(20) | 颜色 |
| created_at | DATETIME | 创建时间 |

#### article_tag (文章标签关联表)

| 字段 | 类型 | 说明 |
|------|------|------|
| article_id | BIGINT | 文章 ID |
| tag_id | BIGINT | 标签 ID |

### 作品集相关表

#### project (项目表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(100) | 项目名称 |
| slug | VARCHAR(100) | URL 别名 |
| description | VARCHAR(500) | 描述 |
| content | TEXT | 详情 |
| cover_image | VARCHAR(255) | 封面图 |
| images | TEXT | 图片列表 (JSON) |
| tech_stack | TEXT | 技术栈 (JSON) |
| demo_url | VARCHAR(255) | 演示地址 |
| source_url | VARCHAR(255) | 源码地址 |
| status | TINYINT | 状态 |
| sort | INT | 排序 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### skill (技能表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 技能名称 |
| category | VARCHAR(50) | 分类 |
| level | TINYINT | 等级: 1-5 |
| icon | VARCHAR(255) | 图标 |
| sort | INT | 排序 |

### 创作相关表

#### novel (小说表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| author | VARCHAR(50) | 作者 |
| summary | VARCHAR(500) | 简介 |
| cover_image | VARCHAR(255) | 封面 |
| category | VARCHAR(50) | 分类 |
| status | TINYINT | 状态: 0-连载, 1-完结 |
| word_count | INT | 总字数 |
| chapter_count | INT | 章节数 |
| view_count | INT | 浏览量 |
| is_finished | TINYINT | 是否完结 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### chapter (章节表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| novel_id | BIGINT | 小说 ID |
| title | VARCHAR(100) | 章节标题 |
| content | LONGTEXT | 内容 |
| word_count | INT | 字数 |
| chapter_no | INT | 章节序号 |
| status | TINYINT | 状态 |
| created_at | DATETIME | 创建时间 |

#### poetry (诗歌表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| author | VARCHAR(50) | 作者 |
| content | TEXT | 内容 |
| category | VARCHAR(50) | 分类 |
| cover_image | VARCHAR(255) | 封面 |
| tags | VARCHAR(255) | 标签 |
| created_at | DATETIME | 创建时间 |

#### essay (散文表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| author | VARCHAR(50) | 作者 |
| content | LONGTEXT | 内容 |
| summary | VARCHAR(500) | 摘要 |
| cover_image | VARCHAR(255) | 封面 |
| category | VARCHAR(50) | 分类 |
| tags | VARCHAR(255) | 标签 |
| created_at | DATETIME | 创建时间 |

### 多媒体相关表

#### image (图片表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| description | VARCHAR(500) | 描述 |
| url | VARCHAR(255) | 图片 URL |
| thumbnail_url | VARCHAR(255) | 缩略图 URL |
| width | INT | 宽度 |
| height | INT | 高度 |
| size | BIGINT | 文件大小 |
| album_id | BIGINT | 相册 ID |
| tags | VARCHAR(255) | 标签 |
| is_public | TINYINT | 是否公开 |
| created_at | DATETIME | 创建时间 |

#### video (视频表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| description | VARCHAR(500) | 描述 |
| cover_image | VARCHAR(255) | 封面 |
| url | VARCHAR(255) | 视频 URL |
| duration | INT | 时长 (秒) |
| size | BIGINT | 文件大小 |
| category | VARCHAR(50) | 分类 |
| tags | VARCHAR(255) | 标签 |
| view_count | INT | 浏览量 |
| is_public | TINYINT | 是否公开 |
| created_at | DATETIME | 创建时间 |

#### album (相册表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(100) | 名称 |
| description | VARCHAR(500) | 描述 |
| cover_image | VARCHAR(255) | 封面 |
| image_count | INT | 图片数量 |
| is_public | TINYINT | 是否公开 |
| sort | INT | 排序 |
| created_at | DATETIME | 创建时间 |

## 索引设计

### 主要索引

```sql
-- 用户表
CREATE UNIQUE INDEX uk_username ON user(username);
CREATE UNIQUE INDEX uk_email ON user(email);

-- 文章表
CREATE UNIQUE INDEX uk_slug ON article(slug);
CREATE INDEX idx_category_id ON article(category_id);
CREATE INDEX idx_status ON article(status);
CREATE INDEX idx_created_at ON article(created_at);

-- 小说表
CREATE INDEX idx_novel_id ON chapter(novel_id);

-- 图片表
CREATE INDEX idx_album_id ON image(album_id);
```

## 数据库迁移

使用 Flyway 管理数据库版本：

```
src/main/resources/db/migration/
├── V1.0.0__init_schema.sql        # 初始化表结构
├── V1.0.1__add_article_tag.sql    # 添加文章标签关联表
├── V1.1.0__add_novel_tables.sql   # 添加小说相关表
└── V1.2.0__add_media_tables.sql   # 添加多媒体相关表
```