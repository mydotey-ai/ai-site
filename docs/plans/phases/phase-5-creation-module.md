# Phase 5: 创作模块

## 阶段目标

实现文学创作展示功能，包括：
- 小说连载（章节管理）
- 诗歌展示
- 散文展示
- 随笔/杂文展示
- 前端创作页面

---

## 1. 需求分析

### 1.1 目标用户

| 用户类型 | 需求 |
|---------|------|
| 网站主人 | 发布文学作品、管理章节、查看阅读统计 |
| 访客 | 阅读小说、浏览诗歌/散文、发表评论 |

### 1.2 核心功能

| 功能 | 优先级 | 说明 |
|------|--------|------|
| 小说管理 | P0 | 创建、编辑、删除小说 |
| 章节管理 | P0 | 小说章节的增删改查 |
| 诗歌管理 | P0 | 诗歌的增删改查 |
| 散文管理 | P1 | 散文的增删改查 |
| 随笔管理 | P1 | 随笔/杂文的增删改查 |
| 基础阅读器 | P0 | 上下章节导航 |
| 创作状态 | P0 | 已发布/草稿 |

### 1.3 用户场景

#### 场景 1：发布小说

```
用户：网站主人
目标：发布一部连载小说

流程：
1. 登录管理后台
2. 进入创作管理，点击"新建小说"
3. 填写小说标题、简介、封面
4. 设置小说分类（玄幻/都市/科幻等）
5. 保存小说信息
6. 添加章节：填写章节标题、内容
7. 发布章节
8. 重复步骤 6-7 添加更多章节
```

#### 场景 2：阅读小说

```
用户：访客
目标：阅读一部小说

流程：
1. 访问创作页面
2. 浏览小说列表
3. 点击小说进入详情页
4. 查看小说简介、章节列表
5. 点击章节开始阅读
6. 使用"上一章/下一章"导航
7. 可选：发表评论
```

#### 场景 3：浏览诗歌

```
用户：访客
目标：浏览诗歌作品

流程：
1. 访问创作页面
2. 切换到"诗歌"标签
3. 浏览诗歌列表
4. 点击诗歌查看详情
5. 阅读诗歌内容
```

---

## 2. 数据建模

### 2.1 实体关系图

```
┌─────────────┐     ┌─────────────┐
│    Novel    │────▶│   Chapter   │
└─────────────┘     └─────────────┘
       │
       │            ┌─────────────┐
       └───────────▶│ NovelCategory│
                    └─────────────┘

┌─────────────┐
│   Poetry    │
└─────────────┘

┌─────────────┐
│    Essay    │
└─────────────┘

┌─────────────┐
│   Article   │ (随笔/杂文，复用博客文章表)
└─────────────┘
```

### 2.2 小说表 (novel)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 小说标题 |
| slug | VARCHAR(100) | URL 别名（唯一） |
| author | VARCHAR(50) | 作者 |
| summary | VARCHAR(1000) | 简介 |
| cover_image | VARCHAR(255) | 封面图 URL |
| category_id | BIGINT | 分类 ID |
| status | VARCHAR(20) | 状态: DRAFT / PUBLISHED / COMPLETED |
| word_count | INT | 总字数（冗余字段） |
| chapter_count | INT | 章节数（冗余字段） |
| view_count | INT | 浏览量 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

**状态说明：**
- `DRAFT` - 草稿
- `PUBLISHED` - 已发布（连载中）
- `COMPLETED` - 已完结

**索引设计：**
- `uk_slug` - slug 唯一索引
- `idx_category_id` - 分类索引
- `idx_status` - 状态索引

### 2.3 章节表 (chapter)

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

### 2.4 小说分类表 (novel_category)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 分类名称 |
| slug | VARCHAR(50) | URL 别名 |
| sort | INT | 排序 |

**预设分类：**
- 玄幻奇幻
- 武侠仙侠
- 都市言情
- 历史军事
- 科幻灵异
- 其他

### 2.5 诗歌表 (poetry)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| slug | VARCHAR(100) | URL 别名 |
| author | VARCHAR(50) | 作者 |
| content | TEXT | 内容 |
| category | VARCHAR(50) | 分类（古体诗/现代诗/词等） |
| status | VARCHAR(20) | 状态: DRAFT / PUBLISHED |
| view_count | INT | 浏览量 |
| created_at | DATETIME | 创建时间 |

**分类说明：**
- 古体诗
- 近体诗
- 词
- 现代诗
- 其他

### 2.6 散文表 (essay)

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

### 2.7 随笔/杂文

随笔/杂文复用博客文章表 (`article`)，通过分类区分。

---

## 3. API 设计

### 3.1 接口概览

#### 小说接口

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/v1/novels | GET | 小说列表 |
| /api/v1/novels/{id} | GET | 小说详情 |
| /api/v1/novels/{id}/chapters | GET | 章节列表 |
| /api/v1/chapters/{id} | GET | 章节内容 |
| /admin/v1/novels | POST | 创建小说 |
| /admin/v1/novels/{id} | PUT | 更新小说 |
| /admin/v1/novels/{id} | DELETE | 删除小说 |
| /admin/v1/novels/{id}/chapters | POST | 添加章节 |
| /admin/v1/chapters/{id} | PUT | 更新章节 |
| /admin/v1/chapters/{id} | DELETE | 删除章节 |

#### 诗歌接口

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/v1/poetry | GET | 诗歌列表 |
| /api/v1/poetry/{id} | GET | 诗歌详情 |
| /admin/v1/poetry | POST | 创建诗歌 |
| /admin/v1/poetry/{id} | PUT | 更新诗歌 |
| /admin/v1/poetry/{id} | DELETE | 删除诗歌 |

#### 散文接口

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/v1/essays | GET | 散文列表 |
| /api/v1/essays/{id} | GET | 散文详情 |
| /admin/v1/essays | POST | 创建散文 |
| /admin/v1/essays/{id} | PUT | 更新散文 |
| /admin/v1/essays/{id} | DELETE | 删除散文 |

### 3.2 小说接口详细设计

#### 获取小说列表

```
GET /api/v1/novels?page=1&size=10&categoryId=1&status=PUBLISHED

Response:
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "title": "星际迷途",
        "slug": "star-journey",
        "author": "作者名",
        "summary": "一个关于星际探险的故事...",
        "coverImage": "/uploads/novels/star-journey.jpg",
        "category": {
          "id": 5,
          "name": "科幻灵异"
        },
        "status": "PUBLISHED",
        "wordCount": 500000,
        "chapterCount": 120,
        "viewCount": 10000,
        "createdAt": "2026-01-01T00:00:00"
      }
    ],
    "total": 50
  }
}
```

#### 获取小说详情

```
GET /api/v1/novels/{id}

Response:
{
  "code": 200,
  "data": {
    "id": 1,
    "title": "星际迷途",
    "slug": "star-journey",
    "author": "作者名",
    "summary": "一个关于星际探险的故事...",
    "coverImage": "/uploads/novels/star-journey.jpg",
    "category": {
      "id": 5,
      "name": "科幻灵异"
    },
    "status": "PUBLISHED",
    "wordCount": 500000,
    "chapterCount": 120,
    "viewCount": 10000,
    "createdAt": "2026-01-01T00:00:00",
    "updatedAt": "2026-03-07T00:00:00"
  }
}
```

#### 获取章节列表

```
GET /api/v1/novels/{novelId}/chapters

Response:
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "chapterNo": 1,
      "title": "第一章 启程",
      "wordCount": 3000,
      "createdAt": "2026-01-01T00:00:00"
    },
    {
      "id": 2,
      "chapterNo": 2,
      "title": "第二章 星海",
      "wordCount": 3500,
      "createdAt": "2026-01-02T00:00:00"
    }
  ]
}
```

#### 获取章节内容

```
GET /api/v1/chapters/{id}

Response:
{
  "code": 200,
  "data": {
    "id": 1,
    "novelId": 1,
    "novelTitle": "星际迷途",
    "chapterNo": 1,
    "title": "第一章 启程",
    "content": "章节内容...",
    "wordCount": 3000,
    "prevChapterId": null,
    "nextChapterId": 2
  }
}
```

### 3.3 诗歌接口详细设计

#### 获取诗歌列表

```
GET /api/v1/poetry?page=1&size=20&category=现代诗

Response:
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "title": "春日",
        "slug": "spring-day",
        "author": "作者名",
        "category": "现代诗",
        "excerpt": "春风拂面...",
        "viewCount": 500,
        "createdAt": "2026-03-01T00:00:00"
      }
    ],
    "total": 30
  }
}
```

#### 获取诗歌详情

```
GET /api/v1/poetry/{id}

Response:
{
  "code": 200,
  "data": {
    "id": 1,
    "title": "春日",
    "slug": "spring-day",
    "author": "作者名",
    "content": "春风拂面\n花开满园\n...",
    "category": "现代诗",
    "viewCount": 500,
    "createdAt": "2026-03-01T00:00:00"
  }
}
```

---

## 4. 前端设计

### 4.1 页面结构

#### Site（用户端）

```
/creation                    创作首页（作品分类导航）
/creation/novels             小说列表
/creation/novels/{slug}      小说详情（章节列表）
/creation/chapters/{id}      章节阅读
/creation/poetry             诗歌列表
/creation/poetry/{slug}      诗歌详情
/creation/essays             散文列表
/creation/essays/{slug}      散文详情
```

#### Admin（管理端）

```
/admin/creation/novels       小说管理
/admin/creation/novels/{id}/chapters  章节管理
/admin/creation/poetry       诗歌管理
/admin/creation/essays       散文管理
```

### 4.2 组件设计

#### 通用组件

| 组件 | 说明 |
|------|------|
| `NovelCard.vue` | 小说卡片 |
| `ChapterList.vue` | 章节列表 |
| `ChapterReader.vue` | 章节阅读器 |
| `PoetryCard.vue` | 诗歌卡片 |
| `PoetryContent.vue` | 诗歌内容展示 |
| `EssayCard.vue` | 散文卡片 |

#### 页面组件

**Site 端：**

| 组件 | 说明 |
|------|------|
| `CreationHome.vue` | 创作首页 |
| `NovelList.vue` | 小说列表 |
| `NovelDetail.vue` | 小说详情 |
| `ChapterReader.vue` | 章节阅读页 |
| `PoetryList.vue` | 诗歌列表 |
| `PoetryDetail.vue` | 诗歌详情 |
| `EssayList.vue` | 散文列表 |
| `EssayDetail.vue` | 散文详情 |

**Admin 端：**

| 组件 | 说明 |
|------|------|
| `NovelManage.vue` | 小说管理 |
| `NovelEdit.vue` | 小说编辑 |
| `ChapterManage.vue` | 章节管理 |
| `ChapterEdit.vue` | 章节编辑 |
| `PoetryManage.vue` | 诗歌管理 |
| `PoetryEdit.vue` | 诗歌编辑 |
| `EssayManage.vue` | 散文管理 |
| `EssayEdit.vue` | 散文编辑 |

### 4.3 页面布局设计

#### 创作首页

```
┌─────────────────────────────────────────────────────────┐
│  Header                                                  │
├─────────────────────────────────────────────────────────┤
│  Hero: 创作天地                                          │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────┐    │
│  │  [小说] [诗歌] [散文] [随笔]                     │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  最新小说                                        │    │
│  │  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐              │    │
│  │  │小说1│ │小说2│ │小说3│ │小说4│              │    │
│  │  └─────┘ └─────┘ └─────┘ └─────┘              │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  最新诗歌                                        │    │
│  │  [诗歌1] [诗歌2] [诗歌3] ...                    │    │
│  └─────────────────────────────────────────────────┘    │
├─────────────────────────────────────────────────────────┤
│  Footer                                                  │
└─────────────────────────────────────────────────────────┘
```

#### 小说详情页

```
┌─────────────────────────────────────────────────────────┐
│  Header                                                  │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────┐    │
│  │  ┌─────────┐  小说标题                          │    │
│  │  │         │  作者: xxx | 分类: 科幻            │    │
│  │  │  封面图  │  状态: 连载中 | 120章 | 50万字    │    │
│  │  │         │  浏览: 10000 次                    │    │
│  │  └─────────┘                                    │    │
│  │  简介: 一个关于星际探险的故事...                │    │
│  │  [开始阅读]                                     │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  章节列表                                        │    │
│  │  ├─ 第一章 启程                                  │    │
│  │  ├─ 第二章 星海                                  │    │
│  │  ├─ 第三章 迷途                                  │    │
│  │  └─ ...                                         │    │
│  └─────────────────────────────────────────────────┘    │
├─────────────────────────────────────────────────────────┤
│  Footer                                                  │
└─────────────────────────────────────────────────────────┘
```

#### 章节阅读页

```
┌─────────────────────────────────────────────────────────┐
│  Header: 小说标题 < 章节标题                             │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────┐    │
│  │  第一章 启程                                      │    │
│  │  ────────────────────────────────────────────    │    │
│  │  章节内容...                                     │    │
│  │                                                  │    │
│  │  段落1                                           │    │
│  │  段落2                                           │    │
│  │  ...                                            │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  [上一章]              [目录]              [下一章]│    │
│  └─────────────────────────────────────────────────┘    │
├─────────────────────────────────────────────────────────┤
│  Footer                                                  │
└─────────────────────────────────────────────────────────┘
```

#### 诗歌详情页

```
┌─────────────────────────────────────────────────────────┐
│  Header                                                  │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────┐    │
│  │  春日                                            │    │
│  │  作者: xxx | 分类: 现代诗                        │    │
│  │  ────────────────────────────────────────────    │    │
│  │                                                  │    │
│  │        春风拂面                                  │    │
│  │        花开满园                                  │    │
│  │        阳光正好                                  │    │
│  │        岁月安然                                  │    │
│  │                                                  │    │
│  │  ────────────────────────────────────────────    │    │
│  │  浏览: 500 次 | 发布于 2026-03-01               │    │
│  └─────────────────────────────────────────────────┘    │
├─────────────────────────────────────────────────────────┤
│  Footer                                                  │
└─────────────────────────────────────────────────────────┘
```

### 4.4 设计规范

#### 小说卡片样式

```scss
.novel-card {
  display: flex;
  gap: 16px;
  padding: 16px;
  border-radius: 8px;
  background: var(--card-bg);

  &__cover {
    width: 120px;
    height: 160px;
    object-fit: cover;
    border-radius: 4px;
  }

  &__info {
    flex: 1;
  }

  &__title {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
  }

  &__meta {
    font-size: 12px;
    color: var(--text-muted);
    margin: 8px 0;
  }

  &__summary {
    font-size: 14px;
    color: var(--text-secondary);
    line-height: 1.6;
  }
}
```

#### 诗歌内容样式

```scss
.poetry-content {
  text-align: center;
  padding: 40px 20px;
  line-height: 2;
  font-size: 18px;
  color: var(--text-primary);

  // 古体诗/词使用特殊排版
  &--classic {
    writing-mode: vertical-rl;
    text-orientation: upright;
  }
}
```

#### 阅读器样式

```scss
.chapter-reader {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;

  &__title {
    font-size: 24px;
    font-weight: 600;
    text-align: center;
    margin-bottom: 40px;
  }

  &__content {
    font-size: 18px;
    line-height: 2;
    text-indent: 2em;
    color: var(--text-primary);
  }

  &__navigation {
    display: flex;
    justify-content: space-between;
    margin-top: 40px;
    padding-top: 20px;
    border-top: 1px solid var(--border-color);
  }
}
```

---

## 5. 技术实现要点

### 5.1 章节导航

```java
public ChapterResponse getChapterWithNavigation(Long chapterId) {
    Chapter chapter = chapterMapper.selectById(chapterId);

    // 获取上一章和下一章
    Chapter prevChapter = chapterMapper.selectPrevChapter(chapter.getNovelId(), chapter.getChapterNo());
    Chapter nextChapter = chapterMapper.selectNextChapter(chapter.getNovelId(), chapter.getChapterNo());

    return ChapterResponse.builder()
        .id(chapter.getId())
        .title(chapter.getTitle())
        .content(chapter.getContent())
        .prevChapterId(prevChapter != null ? prevChapter.getId() : null)
        .nextChapterId(nextChapter != null ? nextChapter.getId() : null)
        .build();
}
```

```sql
-- 获取上一章
SELECT * FROM chapter
WHERE novel_id = #{novelId} AND chapter_no < #{chapterNo} AND status = 'PUBLISHED'
ORDER BY chapter_no DESC LIMIT 1;

-- 获取下一章
SELECT * FROM chapter
WHERE novel_id = #{novelId} AND chapter_no > #{chapterNo} AND status = 'PUBLISHED'
ORDER BY chapter_no ASC LIMIT 1;
```

### 5.2 字数统计

```java
// 章节保存时自动计算字数
public void saveChapter(Chapter chapter) {
    chapter.setWordCount(chapter.getContent().length());
    chapterMapper.insert(chapter);

    // 更新小说总字数
    novelMapper.updateWordCount(chapter.getNovelId());
}
```

### 5.3 浏览量统计

```java
// 使用 Redis 计数，定时同步到数据库
public void incrementViewCount(Long novelId, Long chapterId) {
    String key = "novel:view:" + novelId;
    redisTemplate.opsForValue().increment(key);

    if (chapterId != null) {
        String chapterKey = "chapter:view:" + chapterId;
        redisTemplate.opsForValue().increment(chapterKey);
    }
}
```

---

## 6. 任务清单

### 6.1 后端开发

#### 接入层
- [ ] NovelController
- [ ] ChapterController
- [ ] PoetryController
- [ ] EssayController
- [ ] NovelRequest / NovelResponse
- [ ] ChapterRequest / ChapterResponse
- [ ] PoetryRequest / PoetryResponse
- [ ] EssayRequest / EssayResponse

#### 应用服务层
- [ ] NovelCommandService
- [ ] NovelQueryService
- [ ] ChapterService
- [ ] PoetryService
- [ ] EssayService

#### 领域层
- [ ] Novel Entity
- [ ] Chapter Entity
- [ ] Poetry Entity
- [ ] Essay Entity
- [ ] NovelCategory Entity
- [ ] NovelRepository
- [ ] ChapterRepository
- [ ] PoetryRepository
- [ ] EssayRepository

#### 基础设施层
- [ ] NovelMapper
- [ ] ChapterMapper
- [ ] PoetryMapper
- [ ] EssayMapper
- [ ] NovelCategoryMapper

### 6.2 数据库
- [ ] novel 表
- [ ] chapter 表
- [ ] poetry 表
- [ ] essay 表
- [ ] novel_category 表

### 6.3 前端开发

#### Site 端
- [ ] CreationHome.vue
- [ ] NovelList.vue
- [ ] NovelDetail.vue
- [ ] ChapterReader.vue
- [ ] PoetryList.vue
- [ ] PoetryDetail.vue
- [ ] EssayList.vue
- [ ] EssayDetail.vue
- [ ] NovelCard.vue
- [ ] PoetryCard.vue
- [ ] EssayCard.vue

#### Admin 端
- [ ] NovelManage.vue
- [ ] NovelEdit.vue
- [ ] ChapterManage.vue
- [ ] ChapterEdit.vue
- [ ] PoetryManage.vue
- [ ] PoetryEdit.vue
- [ ] EssayManage.vue
- [ ] EssayEdit.vue

---

## 7. 验收标准

### 功能验收
- [ ] 小说可以创建、编辑、删除
- [ ] 章节可以创建、编辑、删除
- [ ] 章节导航功能正常
- [ ] 诗歌可以管理
- [ ] 散文可以管理
- [ ] 创作状态流转正常
- [ ] 前端阅读器可正常使用
- [ ] 字数统计准确

### 性能验收
- [ ] 小说列表加载 < 500ms
- [ ] 章节内容加载 < 300ms

### 兼容性验收
- [ ] MySQL 环境功能正常
- [ ] SQLite 环境功能正常

---

## 下一阶段

完成 Phase 5 后，进入 [Phase 6: 多媒体模块](./phase-6-media-module.md)。