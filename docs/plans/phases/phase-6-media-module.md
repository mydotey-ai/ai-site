# Phase 6: 多媒体模块

**状态**: ✅ 已完成 (2026-03-19)

## 阶段目标

实现多媒体管理功能，包括：
- 图片上传与管理（缩略图、压缩、水印）
- 视频管理（外链+本地上传）
- 音频管理（外链+本地上传）
- 相册管理
- 可配置的存储服务

---

## 1. 需求分析

### 1.1 目标用户

| 用户类型 | 需求 |
|---------|------|
| 网站主人 | 上传管理媒体文件、创建相册、配置存储 |
| 访客 | 浏览图片、观看视频、收听音频 |

### 1.2 核心功能

| 功能 | 优先级 | 说明 |
|------|--------|------|
| 图片上传 | P0 | 支持常见图片格式上传 |
| 图片处理 | P1 | 缩略图、压缩、水印（可配置） |
| 视频管理 | P0 | 外链视频 + 本地上传 |
| 音频管理 | P1 | 外链音频 + 本地上传 |
| 相册管理 | P1 | 创建、编辑、删除相册 |
| 文件管理 | P0 | 文件夹分类、搜索、批量操作 |
| 存储配置 | P1 | 本地存储 / 云存储可切换 |

### 1.3 用户场景

#### 场景 1：上传图片到相册

```
用户：网站主人
目标：上传一批旅行照片到新相册

流程：
1. 登录管理后台
2. 进入媒体管理
3. 创建新相册"2026年春季旅行"
4. 选择上传文件
5. 批量选择图片文件
6. 系统自动处理：生成缩略图、压缩、加水印
7. 上传完成，查看图片列表
8. 调整图片排序、删除不需要的图片
```

#### 场景 2：添加外链视频

```
用户：网站主人
目标：添加一个 B站视频链接

流程：
1. 进入媒体管理 -> 视频管理
2. 点击"添加外链视频"
3. 输入视频标题
4. 输入 B站视频嵌入代码或链接
5. 选择视频分类
6. 保存
```

#### 场景 3：浏览相册

```
用户：访客
目标：浏览网站上的照片

流程：
1. 访问媒体/相册页面
2. 浏览相册列表
3. 点击相册进入
4. 瀑布流展示图片
5. 点击图片查看大图
6. 左右切换浏览
```

---

## 2. 数据建模

### 2.1 实体关系图

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│    Image    │────▶│    Album    │     │    Video    │
└─────────────┘     └─────────────┘     └─────────────┘
       │                   │
       │                   │
       ▼                   ▼
┌─────────────┐     ┌─────────────┐
│   Folder    │     │    Audio    │
└─────────────┘     └─────────────┘
```

### 2.2 图片表 (image)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| description | VARCHAR(500) | 描述 |
| original_name | VARCHAR(255) | 原始文件名 |
| file_name | VARCHAR(255) | 存储文件名（UUID） |
| url | VARCHAR(500) | 图片 URL |
| thumbnail_url | VARCHAR(500) | 缩略图 URL |
| width | INT | 宽度 |
| height | INT | 高度 |
| size | BIGINT | 文件大小（字节） |
| mime_type | VARCHAR(50) | MIME 类型 |
| album_id | BIGINT | 相册 ID |
| folder_id | BIGINT | 文件夹 ID |
| tags | JSON | 标签列表 `["标签1", "标签2"]` |
| is_public | TINYINT(1) | 是否公开: 0-否, 1-是 |
| view_count | INT | 浏览量 |
| created_at | DATETIME | 创建时间 |
| deleted_at | DATETIME | 删除时间（软删除） |

**索引设计：**
- `idx_album_id` - 相册索引
- `idx_folder_id` - 文件夹索引
- `idx_is_public` - 公开状态索引
- `idx_created_at` - 创建时间索引
- `idx_deleted_at` - 软删除索引

**文件命名策略：**
- 使用 UUID + 时间戳生成唯一文件名，避免冲突和安全风险
- 格式：`{year}/{month}/{uuid}.{ext}`

### 2.3 视频表 (video)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| description | VARCHAR(500) | 描述 |
| cover_image | VARCHAR(255) | 封面图 URL |
| type | VARCHAR(20) | 类型: LOCAL / EXTERNAL |
| platform | VARCHAR(20) | 平台: BILIBILI / YOUTUBE / LOCAL |
| video_id | VARCHAR(100) | 外链视频 ID（B站 BV号等） |
| url | VARCHAR(500) | 视频 URL（本地上传） |
| file_name | VARCHAR(255) | 存储文件名（本地上传，UUID） |
| duration | INT | 时长（秒） |
| size | BIGINT | 文件大小（字节） |
| category | VARCHAR(50) | 分类 |
| tags | JSON | 标签列表 |
| is_public | TINYINT(1) | 是否公开 |
| view_count | INT | 浏览量 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |
| deleted_at | DATETIME | 删除时间（软删除） |

**视频类型说明：**
- `LOCAL` - 本地上传
- `EXTERNAL` - 外链视频（B站、YouTube 等）

**安全说明：**
- 外链视频只存储平台和视频 ID，前端根据平台生成嵌入代码
- 避免直接存储嵌入代码，防止 XSS 攻击

### 2.4 音频表 (audio)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| description | VARCHAR(500) | 描述 |
| cover_image | VARCHAR(255) | 封面图 URL |
| type | VARCHAR(20) | 类型: LOCAL / EXTERNAL |
| platform | VARCHAR(20) | 平台: NETEASE / LOCAL |
| audio_id | VARCHAR(100) | 外链音频 ID |
| url | VARCHAR(500) | 音频 URL（本地上传） |
| file_name | VARCHAR(255) | 存储文件名（UUID） |
| duration | INT | 时长（秒） |
| size | BIGINT | 文件大小（字节） |
| category | VARCHAR(50) | 分类 |
| tags | JSON | 标签列表 |
| is_public | TINYINT(1) | 是否公开 |
| view_count | INT | 播放量 |
| created_at | DATETIME | 创建时间 |
| deleted_at | DATETIME | 删除时间（软删除） |

**安全说明：**
- 外链音频只存储平台和音频 ID，前端生成播放代码

### 2.5 相册表 (album)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(100) | 相册名称 |
| slug | VARCHAR(100) | URL 别名 |
| description | VARCHAR(500) | 描述 |
| cover_image | VARCHAR(255) | 封面图 URL |
| image_count | INT | 图片数量（冗余字段） |
| is_public | TINYINT(1) | 是否公开 |
| sort | INT | 排序 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |
| deleted_at | DATETIME | 删除时间（软删除） |

**冗余字段更新策略：**
- 图片添加/删除时：事件驱动更新相册图片数
- 定时任务：每天凌晨全量校准

### 2.6 文件夹表 (folder)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(100) | 文件夹名称 |
| parent_id | BIGINT | 父文件夹 ID（支持层级） |
| type | VARCHAR(20) | 类型: IMAGE / VIDEO / AUDIO |
| sort | INT | 排序 |
| created_at | DATETIME | 创建时间 |

---

## 3. API 设计

### 3.1 接口概览

#### 图片接口

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/v1/images | GET | 图片列表 |
| /api/v1/images/{id} | GET | 图片详情 |
| /admin/v1/images | POST | 上传图片 |
| /admin/v1/images/{id} | PUT | 更新图片信息 |
| /admin/v1/images/{id} | DELETE | 删除图片 |
| /admin/v1/images/batch | POST | 批量操作 |

#### 视频接口

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/v1/videos | GET | 视频列表 |
| /api/v1/videos/{id} | GET | 视频详情 |
| /admin/v1/videos | POST | 添加视频 |
| /admin/v1/videos/{id} | PUT | 更新视频 |
| /admin/v1/videos/{id} | DELETE | 删除视频 |

#### 音频接口

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/v1/audios | GET | 音频列表 |
| /api/v1/audios/{id} | GET | 音频详情 |
| /admin/v1/audios | POST | 添加音频 |
| /admin/v1/audios/{id} | PUT | 更新音频 |
| /admin/v1/audios/{id} | DELETE | 删除音频 |

#### 相册接口

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/v1/albums | GET | 相册列表 |
| /api/v1/albums/{id} | GET | 相册详情（含图片） |
| /admin/v1/albums | POST | 创建相册 |
| /admin/v1/albums/{id} | PUT | 更新相册 |
| /admin/v1/albums/{id} | DELETE | 删除相册 |

#### 文件夹接口

| 接口 | 方法 | 说明 |
|------|------|------|
| /admin/v1/folders | GET | 文件夹树 |
| /admin/v1/folders | POST | 创建文件夹 |
| /admin/v1/folders/{id} | PUT | 更新文件夹 |
| /admin/v1/folders/{id} | DELETE | 删除文件夹 |

### 3.2 图片接口详细设计

#### 上传图片

```
POST /admin/v1/images
Content-Type: multipart/form-data

Request:
- file: 图片文件
- albumId: 相册 ID（可选）
- folderId: 文件夹 ID（可选）
- title: 标题（可选）
- description: 描述（可选）
- tags: 标签 JSON 数组（可选）["标签1", "标签2"]
- isPublic: 是否公开（默认 1）

Response:
{
  "code": 200,
  "data": {
    "id": 1,
    "title": "风景照片",
    "url": "/uploads/images/2026/03/abc123.jpg",
    "thumbnailUrl": "/uploads/images/2026/03/abc123_thumb.jpg",
    "width": 1920,
    "height": 1080,
    "size": 1024000
  }
}
```

**文件上传安全校验：**
1. 文件大小限制：图片 5MB，视频 100MB，音频 20MB
2. MIME 类型白名单校验
3. **Magic Number 校验**：读取文件头部字节验证真实类型
   - JPEG: `FF D8 FF`
   - PNG: `89 50 4E 47`
   - GIF: `47 49 46 38`
   - WebP: `52 49 46 46 ... 57 45 42 50`
4. 文件名使用 UUID，不使用用户提供的文件名

#### 获取图片列表

```
GET /api/v1/images?page=1&size=20&albumId=1&folderId=1&keyword=风景

Query Parameters:
- page: 页码
- size: 每页数量（最大 50）
- albumId: 相册 ID（可选）
- folderId: 文件夹 ID（可选）
- keyword: 搜索关键词（可选）

Response:
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "title": "风景照片",
        "url": "/uploads/images/2026/03/abc123.jpg",
        "thumbnailUrl": "/uploads/images/2026/03/abc123_thumb.jpg",
        "width": 1920,
        "height": 1080,
        "album": {
          "id": 1,
          "name": "旅行照片"
        },
        "viewCount": 100,
        "createdAt": "2026-03-07T10:00:00"
      }
    ],
    "total": 50,
    "page": 1,
    "size": 20
  }
}
```

**分页说明：**
- 必须使用分页，不支持全量加载
- 使用游标分页优化大数据量场景（可选）

#### 批量操作

```
POST /admin/v1/images/batch

Request:
{
  "action": "move",           // move / delete / setPublic / setPrivate
  "ids": [1, 2, 3],
  "targetAlbumId": 2,         // move 操作需要
  "targetFolderId": 3         // move 操作需要
}

Response:
{
  "code": 200,
  "data": {
    "success": 3,
    "failed": 0
  }
}
```

**批量操作原子性：**
- 使用数据库事务确保操作的原子性
- 部分失败时返回成功和失败数量，不回滚已成功的操作

### 3.3 视频接口详细设计

#### 添加视频

```
POST /admin/v1/videos

Request (外链视频):
{
  "title": "我的Vlog",
  "description": "日常生活记录",
  "type": "EXTERNAL",
  "platform": "BILIBILI",
  "videoId": "BV1xx411c7mD",
  "coverImage": "/uploads/covers/vlog.jpg",
  "category": "生活",
  "tags": ["vlog", "日常"],
  "isPublic": true
}

Request (本地上传):
Content-Type: multipart/form-data
- file: 视频文件
- title: 标题
- description: 描述
- coverImage: 封面图
- category: 分类
- tags: 标签 JSON 数组

Response:
{
  "code": 200,
  "data": {
    "id": 1,
    "title": "我的Vlog",
    "type": "EXTERNAL",
    "platform": "BILIBILI",
    "videoId": "BV1xx411c7mD",
    "coverImage": "/uploads/covers/vlog.jpg"
  }
}
```

**前端嵌入代码生成：**
```typescript
// 根据平台生成安全的嵌入代码
const getEmbedCode = (platform: string, videoId: string) => {
  switch (platform) {
    case 'BILIBILI':
      return `https://player.bilibili.com/player.html?bvid=${videoId}`;
    case 'YOUTUBE':
      return `https://www.youtube.com/embed/${videoId}`;
    default:
      return null;
  }
};
```

### 3.4 相册接口详细设计

#### 获取相册详情

```
GET /api/v1/albums/{id}?page=1&size=20

Response:
{
  "code": 200,
  "data": {
    "id": 1,
    "name": "2026年春季旅行",
    "slug": "spring-travel-2026",
    "description": "春季旅行照片合集",
    "coverImage": "/uploads/images/...",
    "imageCount": 50,
    "images": [
      {
        "id": 1,
        "title": "风景1",
        "thumbnailUrl": "/uploads/images/thumb/...",
        "url": "/uploads/images/..."
      }
    ],
    "createdAt": "2026-03-01T00:00:00"
  }
}
```

---

## 4. 前端设计

### 4.1 页面结构

#### Site（用户端）

```
/gallery                    图库首页（相册列表）
/gallery/{slug}             相册详情（瀑布流图片）
/gallery/image/{id}         图片详情（灯箱）
/videos                     视频列表
/videos/{id}                视频播放
/audios                     音频列表
/audios/{id}                音频播放
```

#### Admin（管理端）

```
/admin/media                媒体管理首页
/admin/media/images         图片管理
/admin/media/videos         视频管理
/admin/media/audios         音频管理
/admin/media/albums         相册管理
/admin/media/folders        文件夹管理
/admin/media/settings       存储设置
```

### 4.2 组件设计

#### 通用组件

| 组件 | 说明 |
|------|------|
| `ImageCard.vue` | 图片卡片 |
| `ImageLightbox.vue` | 图片灯箱/查看器 |
| `ImageUploader.vue` | 图片上传组件 |
| `VideoCard.vue` | 视频卡片 |
| `VideoPlayer.vue` | 视频播放器 |
| `AudioPlayer.vue` | 音频播放器 |
| `AlbumCard.vue` | 相册卡片 |
| `FolderTree.vue` | 文件夹树组件 |
| `MediaFilter.vue` | 媒体筛选组件 |
| `WaterfallLayout.vue` | 瀑布流布局组件 |

#### 页面组件

**Site 端：**

| 组件 | 说明 |
|------|------|
| `GalleryHome.vue` | 图库首页 |
| `AlbumDetail.vue` | 相册详情 |
| `ImageDetail.vue` | 图片详情 |
| `VideoList.vue` | 视频列表 |
| `VideoDetail.vue` | 视频播放页 |
| `AudioList.vue` | 音频列表 |
| `AudioDetail.vue` | 音频播放页 |

**Admin 端：**

| 组件 | 说明 |
|------|------|
| `MediaHome.vue` | 媒体管理首页 |
| `ImageManage.vue` | 图片管理 |
| `VideoManage.vue` | 视频管理 |
| `AudioManage.vue` | 音频管理 |
| `AlbumManage.vue` | 相册管理 |
| `FolderManage.vue` | 文件夹管理 |
| `StorageSettings.vue` | 存储设置 |

### 4.3 页面布局设计

#### 图库首页

```
┌─────────────────────────────────────────────────────────┐
│  Header                                                  │
├─────────────────────────────────────────────────────────┤
│  Hero: 图库                                              │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────┐    │
│  │  相册列表                                        │    │
│  │  ┌─────┐  ┌─────┐  ┌─────┐  ┌─────┐           │    │
│  │  │封面 │  │封面 │  │封面 │  │封面 │           │    │
│  │  │相册1│  │相册2│  │相册3│  │相册4│           │    │
│  │  │50张│  │30张│  │20张│  │15张│            │    │
│  │  └─────┘  └─────┘  └─────┘  └─────┘           │    │
│  └─────────────────────────────────────────────────┘    │
├─────────────────────────────────────────────────────────┤
│  Footer                                                  │
└─────────────────────────────────────────────────────────┘
```

#### 相册详情（瀑布流）

```
┌─────────────────────────────────────────────────────────┐
│  Header                                                  │
├─────────────────────────────────────────────────────────┤
│  相册名称: 2026年春季旅行 | 50张照片                    │
├─────────────────────────────────────────────────────────┤
│  ┌─────┐ ┌─────────┐ ┌─────┐ ┌─────────────┐          │
│  │     │ │         │ │     │ │             │          │
│  │ img │ │   img   │ │ img │ │     img     │          │
│  │     │ │         │ │     │ │             │          │
│  └─────┘ └─────────┘ └─────┘ └─────────────┘          │
│  ┌─────────┐ ┌─────┐ ┌─────────────┐ ┌─────┐          │
│  │         │ │     │ │             │ │     │          │
│  │   img   │ │ img │ │     img     │ │ img │          │
│  │         │ │     │ │             │ │     │          │
│  └─────────┘ └─────┘ └─────────────┘ └─────┘          │
│  ...                                                     │
├─────────────────────────────────────────────────────────┤
│  Footer                                                  │
└─────────────────────────────────────────────────────────┘
```

#### 图片灯箱

```
┌─────────────────────────────────────────────────────────┐
│  [关闭 X]                              [查看原图] [下载] │
├─────────────────────────────────────────────────────────┤
│                                                          │
│   [<]        ┌─────────────────────────┐        [>]     │
│              │                         │                │
│              │         图片            │                │
│              │                         │                │
│              └─────────────────────────┘                │
│                                                          │
├─────────────────────────────────────────────────────────┤
│  风景照片 | 2026-03-07 | 1920x1080 | 1MB               │
└─────────────────────────────────────────────────────────┘
```

#### 管理后台 - 图片管理

```
┌─────────────────────────────────────────────────────────┐
│  Sidebar | 媒体管理 > 图片                              │
├─────────────────────────────────────────────────────────┤
│  ┌───────────────┐  ┌───────────────────────────────┐   │
│  │ 文件夹树      │  │ 工具栏: [上传] [筛选] [批量]  │   │
│  │ ├─ 全部图片   │  ├───────────────────────────────┤   │
│  │ ├─ 旅行照片   │  │ ┌───┐ ┌───┐ ┌───┐ ┌───┐      │   │
│  │ ├─ 风景       │  │ │[x]│ │[ ]│ │[ ]│ │[ ]│      │   │
│  │ ├─ 人物       │  │ │img│ │img│ │img│ │img│      │   │
│  │ └─ 其他       │  │ └───┘ └───┘ └───┘ └───┘      │   │
│  │               │  │ ┌───┐ ┌───┐ ┌───┐ ┌───┐      │   │
│  │ [+新建文件夹] │  │ │[ ]│ │[ ]│ │[ ]│ │[ ]│      │   │
│  └───────────────┘  │ │img│ │img│ │img│ │img│      │   │
│                     │ └───┘ └───┘ └───┘ └───┘      │   │
│                     │ [上一页] 1/10 [下一页]       │   │
│                     └───────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

### 4.4 设计规范

#### 图片卡片样式

```scss
.image-card {
  border-radius: 8px;
  overflow: hidden;
  background: var(--card-bg);
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;

  &:hover {
    transform: scale(1.02);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  }

  &__image {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  &__overlay {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 8px;
    background: linear-gradient(transparent, rgba(0, 0, 0, 0.6));
    color: white;
    font-size: 12px;
  }
}
```

#### 瀑布流布局

```scss
.waterfall-grid {
  column-count: 4;
  column-gap: 16px;

  @media (max-width: 1200px) {
    column-count: 3;
  }

  @media (max-width: 768px) {
    column-count: 2;
  }

  &__item {
    break-inside: avoid;
    margin-bottom: 16px;
  }
}
```

#### 视频播放器

```scss
.video-player {
  position: relative;
  width: 100%;
  aspect-ratio: 16 / 9;
  background: #000;

  &__iframe {
    width: 100%;
    height: 100%;
  }

  &__native {
    width: 100%;
    height: 100%;
  }
}
```

---

## 5. 技术实现要点

### 5.1 存储服务抽象

```java
public interface StorageService {
    /**
     * 上传文件
     */
    StorageResult upload(MultipartFile file, String path);

    /**
     * 删除文件
     */
    void delete(String path);

    /**
     * 获取文件 URL
     */
    String getUrl(String path);
}

// 本地存储实现
@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "local")
public class LocalStorageService implements StorageService {
    // ...
}

// 云存储实现（可扩展）
@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "oss")
public class OssStorageService implements StorageService {
    // ...
}
```

### 5.2 图片处理

```java
@Service
public class ImageProcessService {

    @Value("${image.thumbnail.enabled:true}")
    private boolean thumbnailEnabled;

    @Value("${image.thumbnail.width:300}")
    private int thumbnailWidth;

    @Value("${image.compress.enabled:true}")
    private boolean compressEnabled;

    @Value("${image.watermark.enabled:false}")
    private boolean watermarkEnabled;

    public ProcessedImage process(MultipartFile file) {
        BufferedImage image = ImageIO.read(file.getInputStream());

        // 1. 压缩
        if (compressEnabled) {
            image = compress(image);
        }

        // 2. 加水印
        if (watermarkEnabled) {
            image = addWatermark(image);
        }

        // 3. 生成缩略图
        BufferedImage thumbnail = null;
        if (thumbnailEnabled) {
            thumbnail = createThumbnail(image, thumbnailWidth);
        }

        return new ProcessedImage(image, thumbnail);
    }
}
```

### 5.3 文件上传配置

```yaml
# application.yml
storage:
  type: local  # local / oss / s3

upload:
  image:
    max-size: 5MB
    allowed-types: image/jpeg,image/png,image/gif,image/webp
  video:
    max-size: 100MB
    allowed-types: video/mp4,video/webm
  audio:
    max-size: 20MB
    allowed-types: audio/mpeg,audio/wav

image:
  thumbnail:
    enabled: true
    width: 300
  compress:
    enabled: true
    quality: 0.8
  watermark:
    enabled: false
    text: "My Website"
    position: bottom-right
```

### 5.4 瀑布流实现

```vue
<template>
  <div class="waterfall-grid">
    <div
      v-for="image in images"
      :key="image.id"
      class="waterfall-grid__item"
    >
      <ImageCard :image="image" @click="openLightbox(image)" />
    </div>
  </div>
  <div ref="loadTrigger" class="load-trigger" />
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const images = ref([])
const page = ref(1)
const loading = ref(false)
const loadTrigger = ref<HTMLElement | null>(null)
let observer: IntersectionObserver | null = null

// 使用 Intersection Observer API 实现无限滚动
const setupObserver = () => {
  observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting && !loading.value) {
        loadMore()
      }
    },
    { threshold: 0.1 }
  )

  if (loadTrigger.value) {
    observer.observe(loadTrigger.value)
  }
}

const loadMore = async () => {
  if (loading.value) return
  loading.value = true

  const newImages = await fetchImages(page.value)
  images.value.push(...newImages)
  page.value++

  loading.value = false
}

onMounted(() => {
  loadMore()
  setupObserver()
})

onUnmounted(() => {
  observer?.disconnect()
})
</script>
```

---

## 6. 任务清单

### 6.1 后端开发

#### 接入层
- [x] ImageController
- [x] VideoController
- [x] AudioController
- [x] AlbumController
- [x] FolderController
- [x] ImageRequest / ImageResponse
- [x] VideoRequest / VideoResponse
- [x] AudioRequest / AudioResponse
- [x] AlbumRequest / AlbumResponse

#### 应用服务层
- [x] ImageService
- [x] VideoService
- [x] AudioService
- [x] AlbumService
- [x] FolderService
- [x] ImageProcessService
- [x] StorageService

#### 领域层
- [x] Image Entity
- [x] Video Entity
- [x] Audio Entity
- [x] Album Entity
- [x] Folder Entity
- [x] ImageRepository
- [x] VideoRepository
- [x] AudioRepository
- [x] AlbumRepository

#### 基础设施层
- [x] ImageMapper
- [x] VideoMapper
- [x] AudioMapper
- [x] AlbumMapper
- [x] FolderMapper
- [x] LocalStorageService
- [ ] OssStorageService（可选，未实现）

### 6.2 数据库
- [x] image 表
- [x] video 表
- [x] audio 表
- [x] album 表
- [x] folder 表

### 6.3 前端开发

#### Site 端
- [x] GalleryHome.vue
- [x] AlbumDetail.vue
- [x] ImageDetail.vue（集成在灯箱中）
- [x] VideoList.vue
- [x] VideoDetail.vue
- [x] AudioList.vue
- [x] AudioDetail.vue（集成在列表中）
- [x] ImageLightbox.vue
- [x] VideoPlayer.vue
- [x] AudioPlayer.vue
- [x] WaterfallLayout.vue

#### Admin 端
- [x] MediaHome.vue
- [x] ImageManage.vue
- [x] VideoManage.vue
- [x] AudioManage.vue
- [x] AlbumManage.vue
- [x] FolderManage.vue
- [ ] StorageSettings.vue（未实现）
- [x] ImageUploader.vue
- [x] FolderTree.vue
- [x] MediaFilter.vue

---

## 7. 验收标准

### 功能验收
- [x] 图片可以上传和管理
- [x] 图片处理（缩略图、压缩）正常
- [x] 视频可以管理（外链+本地上传）
- [x] 音频可以管理
- [x] 相册可以创建和管理
- [x] 文件夹可以创建和管理
- [x] 文件可以按文件夹筛选
- [ ] 存储配置可以切换（本地存储已实现，云存储待扩展）
- [x] 前端瀑布流正常展示
- [x] 图片灯箱功能正常
- [x] 视频播放正常

### 性能验收
- [x] 图片列表加载 < 500ms
- [x] 图片上传 < 5s（5MB 文件）
- [x] 瀑布流滚动流畅

### 兼容性验收
- [x] MySQL 环境功能正常
- [x] SQLite 环境功能正常
- [x] 本地存储正常工作
- [ ] 云存储正常工作（如配置，未实现）

### 测试验收
- [ ] 后端单元测试覆盖率 > 60%（待补充）
- [ ] 核心 API 集成测试通过
- [x] 文件上传/处理流程测试通过

---

## 项目完成

完成 Phase 6 后，项目基础功能开发完成。后续可根据需求继续迭代优化。