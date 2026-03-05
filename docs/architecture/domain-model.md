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
- 评论管理（可选）

### 核心实体

```
Article (文章)
├── id: Long
├── title: String
├── slug: String (URL友好)
├── summary: String
├── content: String (Markdown)
├── coverImage: String
├── categoryId: Long
├── tags: List<Tag>
├── status: ArticleStatus
├── viewCount: Integer
├── likeCount: Integer
├── isTop: Boolean
├── publishedAt: LocalDateTime
└── createdAt, updatedAt

Category (分类)
├── id: Long
├── name: String
├── slug: String
├── parentId: Long (支持层级)
├── sort: Integer
└── createdAt, updatedAt

Tag (标签)
├── id: Long
├── name: String
├── slug: String
├── color: String
└── createdAt, updatedAt
```

### 文章状态

| 状态 | 说明 |
|------|------|
| DRAFT | 草稿 |
| PUBLISHED | 已发布 |
| ARCHIVED | 已归档 |

---

## 3. Portfolio 作品集领域

### 职责
- 项目作品展示
- 技能展示
- 工作经历
- 教育背景

### 核心实体

```
Project (项目)
├── id: Long
├── name: String
├── slug: String
├── description: String
├── content: String (详情)
├── coverImage: String
├── images: List<String>
├── techStack: List<String>
├── demoUrl: String
├── sourceUrl: String
├── status: ProjectStatus
├── sort: Integer
└── createdAt, updatedAt

Skill (技能)
├── id: Long
├── name: String
├── category: String
├── level: SkillLevel
├── icon: String
├── sort: Integer
└── createdAt, updatedAt

Experience (工作经历)
├── id: Long
├── company: String
├── position: String
├── description: String
├── startDate: LocalDate
├── endDate: LocalDate (nullable)
├── isCurrent: Boolean
└── createdAt, updatedAt
```

---

## 4. Creation 创作领域

### 职责
- 小说连载管理
- 诗歌、散文发布
- 章节内容管理
- 阅读体验优化

### 核心实体

```
Novel (小说)
├── id: Long
├── title: String
├── author: String
├── summary: String
├── coverImage: String
├── category: NovelCategory
├── status: NovelStatus
├── wordCount: Integer
├── chapterCount: Integer
├── viewCount: Integer
├── isFinished: Boolean
└── createdAt, updatedAt

Chapter (章节)
├── id: Long
├── novelId: Long
├── title: String
├── content: String
├── wordCount: Integer
├── chapterNo: Integer
├── status: ChapterStatus
└── createdAt, updatedAt

Poetry (诗歌)
├── id: Long
├── title: String
├── author: String
├── content: String
├── category: PoetryCategory
├── coverImage: String
├── tags: List<String>
└── createdAt, updatedAt

Essay (散文)
├── id: Long
├── title: String
├── author: String
├── content: String
├── summary: String
├── coverImage: String
├── category: EssayCategory
├── tags: List<String>
└── createdAt, updatedAt
```

---

## 5. Media 多媒体领域

### 职责
- 图片上传和管理
- 视频展示
- 相册管理
- 文件存储

### 核心实体

```
Image (图片)
├── id: Long
├── title: String
├── description: String
├── url: String
├── thumbnailUrl: String
├── width: Integer
├── height: Integer
├── size: Long
├── albumId: Long
├── tags: List<String>
├── isPublic: Boolean
└── createdAt, updatedAt

Video (视频)
├── id: Long
├── title: String
├── description: String
├── coverImage: String
├── url: String
├── duration: Integer (秒)
├── size: Long
├── category: VideoCategory
├── tags: List<String>
├── viewCount: Integer
├── isPublic: Boolean
└── createdAt, updatedAt

Album (相册)
├── id: Long
├── name: String
├── description: String
├── coverImage: String
├── imageCount: Integer
├── isPublic: Boolean
├── sort: Integer
└── createdAt, updatedAt
```

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