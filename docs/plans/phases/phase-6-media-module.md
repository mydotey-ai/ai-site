# Phase 6: 多媒体模块

## 阶段目标

实现多媒体功能，包括：
- 图片管理
- 视频管理
- 相册管理
- 文件上传
- 前端多媒体展示页面

## 任务清单

### 1. 后端 - Media 领域

#### 接入层
- [ ] MediaController
- [ ] ImageRequest, ImageResponse
- [ ] VideoRequest, VideoResponse
- [ ] AlbumRequest, AlbumResponse

#### 应用服务层
- [ ] MediaCommandService
  - [ ] uploadImage
  - [ ] uploadVideo
  - [ ] deleteMedia
- [ ] MediaQueryService
  - [ ] getImages
  - [ ] getVideos
  - [ ] getAlbums

#### 领域层
- [ ] Image Entity
- [ ] Video Entity
- [ ] Album Entity
- [ ] ImageRepository
- [ ] VideoRepository

#### 基础设施层
- [ ] ImageMapper
- [ ] VideoMapper
- [ ] AlbumMapper
- [ ] LocalStorageService
- [ ] StorageService (接口)

### 2. 数据库

- [ ] image 表
- [ ] video 表
- [ ] album 表

### 3. 文件存储

- [ ] 本地文件存储实现
- [ ] 文件上传接口
- [ ] 图片压缩/缩略图 (可选)

### 4. 前端 - 多媒体页面

#### Admin
- [ ] ImageManage.vue
- [ ] VideoManage.vue
- [ ] AlbumManage.vue
- [ ] MediaUploader.vue

#### Site
- [ ] Gallery.vue
- [ ] VideoShow.vue
- [ ] AlbumView.vue

## API 设计

### 上传图片

```
POST /admin/v1/media/images
Content-Type: multipart/form-data
Request:
  file: binary
  albumId: number (可选)
Response:
{
  "code": 200,
  "data": {
    "id": 1,
    "url": "/uploads/images/xxx.jpg",
    "thumbnailUrl": "/uploads/images/thumb/xxx.jpg"
  }
}
```

### 图片列表

```
GET /api/v1/images?albumId=1&page=1&size=20
```

### 相册列表

```
GET /api/v1/albums
```

## 验收标准

- [ ] 图片可以上传和管理
- [ ] 视频可以管理
- [ ] 相册可以管理
- [ ] 文件正确存储
- [ ] 前端图库可正常访问

## 项目完成

完成 Phase 6 后，项目基础功能开发完成。后续可根据需求继续迭代优化。