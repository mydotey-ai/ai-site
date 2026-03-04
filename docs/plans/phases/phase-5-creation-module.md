# Phase 5: 创作模块

## 阶段目标

实现创作功能，包括：
- 小说连载
- 诗歌展示
- 散文展示
- 前端创作页面

## 任务清单

### 1. 后端 - Creation 领域

#### 接入层
- [ ] CreationController
- [ ] NovelRequest, NovelResponse
- [ ] ChapterRequest, ChapterResponse
- [ ] PoetryRequest, PoetryResponse
- [ ] EssayRequest, EssayResponse

#### 应用服务层
- [ ] CreationCommandService
- [ ] CreationQueryService

#### 领域层
- [ ] Novel Entity
- [ ] Chapter Entity
- [ ] Poetry Entity
- [ ] Essay Entity
- [ ] NovelRepository
- [ ] ChapterRepository

#### 基础设施层
- [ ] NovelMapper
- [ ] ChapterMapper
- [ ] PoetryMapper
- [ ] EssayMapper

### 2. 数据库

- [ ] novel 表
- [ ] chapter 表
- [ ] poetry 表
- [ ] essay 表

### 3. 前端 - 创作页面

#### Admin
- [ ] NovelManage.vue
- [ ] ChapterEdit.vue
- [ ] PoetryManage.vue
- [ ] EssayManage.vue

#### Site
- [ ] NovelList.vue
- [ ] NovelReader.vue
- [ ] PoetryList.vue
- [ ] PoetryDetail.vue
- [ ] EssayList.vue
- [ ] EssayDetail.vue

## API 设计

### 小说列表

```
GET /api/v1/novels
Response:
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "title": "小说标题",
        "author": "作者",
        "summary": "简介",
        "chapterCount": 50,
        "wordCount": 100000,
        "isFinished": false
      }
    ]
  }
}
```

### 章节列表

```
GET /api/v1/novels/{novelId}/chapters
```

### 章节内容

```
GET /api/v1/chapters/{id}
或
GET /api/v1/novels/{novelId}/chapters/{chapterNo}
```

## 验收标准

- [ ] 小说可以创建和管理
- [ ] 章节可以创建和管理
- [ ] 诗歌可以管理
- [ ] 散文可以管理
- [ ] 前端阅读器可正常使用

## 下一阶段

完成 Phase 5 后，进入 [Phase 6: 多媒体模块](./phase-6-media-module.md)。