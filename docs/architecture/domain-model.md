# 领域知识

## 业务领域划分

本项目包含以下核心领域：

```
┌─────────────────────────────────────────────────────────────┐
│                        AI-Site 领域模型                       │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐        │
│  │   Auth  │  │  Blog   │  │Portfolio│  │Creation │        │
│  │  认证   │  │  博客   │  │ 作品集  │  │  创作   │        │
│  └────┬────┘  └────┬────┘  └────┬────┘  └────┬────┘        │
│       │            │            │            │              │
│       └────────────┴────────────┴────────────┘              │
│                          │                                  │
│                    ┌─────┴─────┐                            │
│                    │   Media   │                            │
│                    │  多媒体   │                            │
│                    └───────────┘                            │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 1. Auth 认证领域

### 职责
- 用户注册、登录、登出
- 用户身份认证和授权
- 角色和权限管理
- Token 管理

### 核心实体

```
User (用户)
├── id: Long
├── username: String
├── password: String (加密)
├── email: String
├── nickname: String
├── avatar: String
├── status: UserStatus
├── roles: List<Role>
└── createdAt, updatedAt

Role (角色)
├── id: Long
├── name: String
├── code: String
├── permissions: List<Permission>
└── createdAt, updatedAt

Permission (权限)
├── id: Long
├── name: String
├── code: String
└── resource: String
```

### 角色设计

| 角色 | Code | 说明 |
|------|------|------|
| 超级管理员 | `SUPER_ADMIN` | 系统最高权限 |
| 管理员 | `ADMIN` | 内容管理权限 |
| 编辑 | `EDITOR` | 内容编辑权限 |
| 用户 | `USER` | 普通用户权限 |

---

## 2. Blog 博客领域

### 职责
- 文章的发布、编辑、删除
- 分类和标签管理
- 文章搜索和筛选
- 评论管理
- SEO 优化

### 核心实体

```
Article (文章)
├── id: Long
├── title: String
├── slug: String (URL友好)
├── summary: String
├── content: String (Markdown/RichText)
├── contentType: ContentType (MARKDOWN/RICHTEXT)
├── categoryId: Long
├── tags: List<Tag>
├── status: ArticleStatus (DRAFT/PUBLISHED/HIDDEN)
├── isTop: Boolean
├── viewCount: Integer
├── seoTitle: String
├── seoDescription: String
├── seoKeywords: String
├── publishedAt: LocalDateTime
└── createdAt, updatedAt

Category (分类)
├── id: Long
├── name: String
├── slug: String
├── description: String
├── parentId: Long (支持层级)
├── sort: Integer
├── articleCount: Integer
└── createdAt, updatedAt

Tag (标签)
├── id: Long
├── name: String
├── slug: String
├── color: String
├── articleCount: Integer
└── createdAt, updatedAt

Comment (评论)
├── id: Long
├── articleId: Long
├── nickname: String
├── email: String
├── website: String
├── content: String
├── ip: String
├── userAgent: String
├── status: CommentStatus (PENDING/APPROVED/SPAM)
└── createdAt, updatedAt
```

### 文章状态

| 状态 | 说明 |
|------|------|
| DRAFT | 草稿 |
| PUBLISHED | 已发布 |
| HIDDEN | 隐藏 |

### 评论状态

| 状态 | 说明 |
|------|------|
| PENDING | 待审核 |
| APPROVED | 已批准 |
| SPAM | 垃圾评论 |

---

## 3. Portfolio 作品集领域

### 职责
- 项目作品展示
- 项目标签分类
- 多链接支持

### 核心实体

```
Project (项目)
├── id: Long
├── name: String
├── slug: String
├── description: String
├── content: String (详情，Markdown)
├── coverImage: String
├── techStack: List<String>
├── links: List<ProjectLink>
├── tags: List<ProjectTag>
├── status: ProjectStatus (DEVELOPING/RELEASED/ARCHIVED)
├── sort: Integer
└── createdAt, updatedAt

ProjectLink (项目链接)
├── id: Long
├── projectId: Long
├── type: LinkType (DEMO/SOURCE/DOCS/OTHER)
├── label: String
├── url: String
├── sort: Integer
└── createdAt, updatedAt

ProjectTag (项目标签)
├── id: Long
├── name: String
├── slug: String
├── color: String
├── sort: Integer
└── createdAt, updatedAt
```

### 项目状态

| 状态 | 说明 |
|------|------|
| DEVELOPING | 开发中 |
| RELEASED | 已发布 |
| ARCHIVED | 已归档 |

### 链接类型

| 类型 | 说明 |
|------|------|
| DEMO | 演示地址 |
| SOURCE | 源码地址 |
| DOCS | 文档地址 |
| OTHER | 其他链接 |

---

## 4. Creation 创作领域

### 职责
- 小说连载管理
- 诗歌、散文、随笔发布
- 章节内容管理
- 阅读体验优化

### 核心实体

```
Novel (小说)
├── id: Long
├── title: String
├── slug: String
├── author: String
├── summary: String
├── coverImage: String
├── categoryId: Long
├── status: NovelStatus (DRAFT/PUBLISHED/COMPLETED)
├── wordCount: Integer
├── chapterCount: Integer
├── viewCount: Integer
└── createdAt, updatedAt

Chapter (章节)
├── id: Long
├── novelId: Long
├── title: String
├── content: String
├── wordCount: Integer
├── chapterNo: Integer
├── status: ChapterStatus (DRAFT/PUBLISHED)
├── viewCount: Integer
└── createdAt, updatedAt

NovelCategory (小说分类)
├── id: Long
├── name: String
├── slug: String
├── sort: Integer
└── createdAt, updatedAt

Poetry (诗歌)
├── id: Long
├── title: String
├── slug: String
├── author: String
├── content: String
├── category: String
├── status: PublishStatus (DRAFT/PUBLISHED)
├── viewCount: Integer
└── createdAt, updatedAt

Essay (散文)
├── id: Long
├── title: String
├── slug: String
├── author: String
├── content: String
├── summary: String
├── category: String
├── status: PublishStatus (DRAFT/PUBLISHED)
├── viewCount: Integer
└── createdAt, updatedAt
```

### 小说状态

| 状态 | 说明 |
|------|------|
| DRAFT | 草稿 |
| PUBLISHED | 已发布（连载中） |
| COMPLETED | 已完结 |

---

## 5. Media 多媒体领域

### 职责
- 图片上传和管理
- 视频展示
- 音频管理
- 相册管理
- 文件存储

### 核心实体

```
Image (图片)
├── id: Long
├── title: String
├── description: String
├── originalName: String
├── fileName: String
├── url: String
├── thumbnailUrl: String
├── width: Integer
├── height: Integer
├── size: Long
├── mimeType: String
├── albumId: Long
├── folderId: Long
├── tags: List<String>
├── isPublic: Boolean
├── viewCount: Integer
└── createdAt, updatedAt

Video (视频)
├── id: Long
├── title: String
├── description: String
├── coverImage: String
├── type: VideoType (LOCAL/EXTERNAL)
├── url: String
├── fileName: String
├── duration: Integer (秒)
├── size: Long
├── category: String
├── tags: List<String>
├── isPublic: Boolean
├── viewCount: Integer
└── createdAt, updatedAt

Audio (音频)
├── id: Long
├── title: String
├── description: String
├── coverImage: String
├── type: AudioType (LOCAL/EXTERNAL)
├── url: String
├── fileName: String
├── duration: Integer (秒)
├── size: Long
├── category: String
├── tags: List<String>
├── isPublic: Boolean
├── viewCount: Integer
└── createdAt, updatedAt

Album (相册)
├── id: Long
├── name: String
├── slug: String
├── description: String
├── coverImage: String
├── imageCount: Integer
├── isPublic: Boolean
├── sort: Integer
└── createdAt, updatedAt

Folder (文件夹)
├── id: Long
├── name: String
├── parentId: Long
├── type: FolderType (IMAGE/VIDEO/AUDIO)
├── sort: Integer
└── createdAt, updatedAt
```

### 视频类型

| 类型 | 说明 |
|------|------|
| LOCAL | 本地上传 |
| EXTERNAL | 外链视频 |

---

## 领域交互关系

```
                    ┌─────────────┐
                    │    Auth     │
                    │   (认证)    │
                    └──────┬──────┘
                           │
         ┌─────────────────┼─────────────────┐
         │                 │                 │
         ▼                 ▼                 ▼
   ┌───────────┐    ┌───────────┐    ┌───────────┐
   │   Blog    │    │ Portfolio │    │ Creation  │
   │  (博客)   │    │ (作品集)  │    │  (创作)   │
   └─────┬─────┘    └─────┬─────┘    └─────┬─────┘
         │                │                │
         └────────────────┼────────────────┘
                          │
                          ▼
                    ┌───────────┐
                    │   Media   │
                    │ (多媒体)  │
                    └───────────┘

说明：
- Auth 为所有领域提供认证支持
- Blog、Portfolio、Creation 都可能引用 Media 的图片/视频
- 各领域相对独立，通过 ID 关联
```