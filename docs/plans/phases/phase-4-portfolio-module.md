# Phase 4: 作品集模块

> **状态**: ✅ 已完成 (2026-03-19)

## 阶段目标

实现项目作品展示功能，包括：
- 项目管理（CRUD、多状态）
- 项目标签分类
- 多链接支持
- 前端作品集页面

---

## 1. 需求分析

### 1.1 目标用户

| 用户类型 | 需求 |
|---------|------|
| 网站主人 | 展示项目作品、管理项目信息 |
| 访客 | 浏览项目、了解技术栈、访问项目链接 |
| HR/面试官 | 评估技术能力、查看项目经验 |

### 1.2 核心功能

| 功能 | 优先级 | 说明 |
|------|--------|------|
| 项目 CRUD | P0 | 创建、编辑、删除项目 |
| 项目状态 | P0 | 开发中/已发布/已归档 |
| 项目标签 | P1 | 通过标签区分项目类型 |
| 多链接支持 | P1 | 演示链接、源码链接等 |
| 封面图 | P1 | 项目封面展示 |
| 时间排序 | P1 | 按创建/更新时间排序 |

### 1.3 不需要的功能

- 技能展示
- 工作经历
- 教育背景
- 访问统计

### 1.4 用户场景

#### 场景 1：添加新项目

```
用户：网站主人
目标：添加一个新项目到作品集

流程：
1. 登录管理后台
2. 进入作品集管理，点击"新建项目"
3. 填写项目名称、描述
4. 添加技术栈（如 Vue、Spring Boot）
5. 添加项目链接（演示地址、源码地址）
6. 上传封面图
7. 选择项目标签（Web/Mobile/CLI 等）
8. 设置状态（开发中/已发布）
9. 保存发布
```

#### 场景 2：浏览作品集

```
用户：访客
目标：浏览项目作品

流程：
1. 访问作品集页面
2. 浏览项目列表（卡片布局）
3. 可按标签筛选项目
4. 点击项目查看详情
5. 点击链接访问项目演示或源码
```

#### 场景 3：筛选项目

```
用户：访客
目标：查看特定类型的项目

流程：
1. 在作品集页面点击标签筛选
2. 系统显示该标签下的项目
3. 浏览筛选后的项目列表
```

---

## 2. 数据建模

### 2.1 实体关系图

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Project   │────▶│ ProjectTag  │◀────│     Tag     │
└─────────────┘     └─────────────┘     └─────────────┘
       │
       │            ┌─────────────┐
       └───────────▶│ ProjectLink │
                    └─────────────┘
```

### 2.2 项目表 (project)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(100) | 项目名称 |
| slug | VARCHAR(100) | URL 别名（唯一） |
| description | VARCHAR(500) | 项目描述 |
| content | TEXT | 项目详情（可选，Markdown） |
| cover_image | VARCHAR(255) | 封面图 URL |
| tech_stack | JSON | 技术栈列表 |
| status | VARCHAR(20) | 状态: DEVELOPING / RELEASED / ARCHIVED |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |
| deleted_at | DATETIME | 删除时间（软删除） |

**技术栈 JSON 格式：**
```json
["Vue.js", "Spring Boot", "MySQL", "Redis"]
```

**索引设计：**
- `uk_slug` - slug 唯一索引
- `idx_status` - 状态索引
- `idx_created_at` - 创建时间索引
- `idx_deleted_at` - 软删除索引

**封面图说明：**
- Phase 3-5 的封面图上传先使用简单实现（直接保存到本地）
- Phase 6 完成后可集成完整的媒体管理功能

### 2.3 项目链接表 (project_link)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| project_id | BIGINT | 项目 ID |
| type | VARCHAR(20) | 链接类型: DEMO / SOURCE / DOCS / OTHER |
| label | VARCHAR(50) | 链接标签（显示名称） |
| url | VARCHAR(500) | 链接地址 |
| sort | INT | 排序 |

**链接类型说明：**
- `DEMO` - 演示地址
- `SOURCE` - 源码地址（如 GitHub）
- `DOCS` - 文档地址
- `OTHER` - 其他链接

**级联删除策略：**
- 删除项目时，级联删除该项目的所有链接
- 使用数据库外键 `ON DELETE CASCADE` 或应用层事务删除

### 2.4 标签表 (project_tag)

> 使用独立表，与博客标签解耦，便于独立管理和扩展

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR(50) | 标签名称 |
| slug | VARCHAR(50) | URL 别名 |
| color | VARCHAR(20) | 标签颜色（HEX） |
| sort | INT | 排序 |
| deleted_at | DATETIME | 删除时间（软删除） |

**冗余字段更新策略：**
- 项目发布/取消发布时：事件驱动更新相关标签的项目数
- 定时任务：每天凌晨全量校准

### 2.5 项目标签关联表 (project_tag_relation)

| 字段 | 类型 | 说明 |
|------|------|------|
| project_id | BIGINT | 项目 ID |
| tag_id | BIGINT | 标签 ID |

**联合主键：** `(project_id, tag_id)`

---

## 3. API 设计

### 3.1 接口概览

| 模块 | 接口 | 方法 | 说明 |
|------|------|------|------|
| **项目** | /api/v1/projects | GET | 项目列表（公开） |
| | /api/v1/projects/{id} | GET | 项目详情 |
| | /api/v1/projects/slug/{slug} | GET | 按 slug 获取项目 |
| | /admin/v1/projects | POST | 创建项目 |
| | /admin/v1/projects/{id} | PUT | 更新项目 |
| | /admin/v1/projects/{id} | DELETE | 删除项目 |
| | /admin/v1/projects/batch | POST | 批量操作（删除/修改状态） |
| **标签** | /api/v1/project-tags | GET | 项目标签列表 |
| | /admin/v1/project-tags | POST | 创建标签 |
| | /admin/v1/project-tags/{id} | PUT | 更新标签 |
| | /admin/v1/project-tags/{id} | DELETE | 删除标签 |

### 3.2 项目接口详细设计

#### 获取项目列表

```
GET /api/v1/projects?page=1&size=12&tagId=1&status=RELEASED

Query Parameters:
- page: 页码（默认 1）
- size: 每页数量（默认 12，最大 50）
- tagId: 标签 ID（可选）
- status: 状态（可选，公开接口仅支持 RELEASED）

Response:
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "name": "AI-Site",
        "slug": "ai-site",
        "description": "个人网站项目，包含博客、作品集、创作等功能",
        "coverImage": "/uploads/projects/ai-site.png",
        "techStack": ["Vue.js", "Spring Boot", "MySQL"],
        "tags": [
          { "id": 1, "name": "Web", "slug": "web", "color": "#42b883" }
        ],
        "links": [
          { "type": "DEMO", "label": "在线演示", "url": "https://example.com" },
          { "type": "SOURCE", "label": "源码", "url": "https://github.com/..." }
        ],
        "status": "RELEASED",
        "createdAt": "2026-03-07T10:00:00"
      }
    ],
    "total": 20,
    "page": 1,
    "size": 12
  }
}
```

#### 获取项目详情

```
GET /api/v1/projects/{id}
或
GET /api/v1/projects/slug/{slug}

Response:
{
  "code": 200,
  "data": {
    "id": 1,
    "name": "AI-Site",
    "slug": "ai-site",
    "description": "个人网站项目",
    "content": "## 项目背景\n\n这是一个...",
    "coverImage": "/uploads/projects/ai-site.png",
    "techStack": ["Vue.js", "Spring Boot", "MySQL", "Redis"],
    "tags": [
      { "id": 1, "name": "Web", "slug": "web", "color": "#42b883" }
    ],
    "links": [
      { "type": "DEMO", "label": "在线演示", "url": "https://example.com" },
      { "type": "SOURCE", "label": "GitHub", "url": "https://github.com/..." }
    ],
    "status": "RELEASED",
    "createdAt": "2026-03-07T10:00:00",
    "updatedAt": "2026-03-07T12:00:00"
  }
}
```

#### 创建项目

```
POST /admin/v1/projects

Request:
{
  "name": "AI-Site",
  "slug": "ai-site",                    // 可选，自动生成
  "description": "个人网站项目",
  "content": "## 项目背景\n\n...",      // 可选
  "coverImage": "/uploads/projects/ai-site.png",
  "techStack": ["Vue.js", "Spring Boot"],
  "tagIds": [1, 2],
  "links": [
    { "type": "DEMO", "label": "在线演示", "url": "https://example.com" },
    { "type": "SOURCE", "label": "GitHub", "url": "https://github.com/..." }
  ],
  "status": "DEVELOPING"                // DEVELOPING / RELEASED / ARCHIVED
}

Response:
{
  "code": 200,
  "data": {
    "id": 1,
    "name": "AI-Site",
    "slug": "ai-site"
  }
}
```

### 3.3 标签接口详细设计

#### 获取项目标签列表

```
GET /api/v1/project-tags

Response:
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "Web",
      "slug": "web",
      "color": "#42b883",
      "projectCount": 10
    },
    {
      "id": 2,
      "name": "Mobile",
      "slug": "mobile",
      "color": "#38bdf8",
      "projectCount": 5
    }
  ]
}
```

### 3.4 批量操作接口

#### 批量操作项目

```
POST /admin/v1/projects/batch

Request:
{
  "action": "delete",           // delete / archive / release
  "ids": [1, 2, 3]
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

---

## 4. 前端设计

### 4.1 页面结构

#### Site（用户端）

```
/portfolio                 作品集首页（项目列表）
/portfolio/{slug}          项目详情
```

#### Admin（管理端）

```
/admin/portfolio/projects     项目管理
/admin/portfolio/projects/new  新建项目
/admin/portfolio/projects/{id}/edit  编辑项目
/admin/portfolio/tags         标签管理
```

### 4.2 组件设计

#### 通用组件

| 组件 | 说明 |
|------|------|
| `ProjectCard.vue` | 项目卡片（卡片布局） |
| `ProjectList.vue` | 项目列表容器 |
| `ProjectFilter.vue` | 标签筛选组件 |
| `TechStackBadge.vue` | 技术栈标签 |
| `ProjectLinks.vue` | 项目链接列表 |

#### 页面组件

**Site 端：**

| 组件 | 说明 |
|------|------|
| `PortfolioList.vue` | 作品集首页 - 项目列表 |
| `PortfolioDetail.vue` | 项目详情页 |

**Admin 端：**

| 组件 | 说明 |
|------|------|
| `ProjectList.vue` | 项目管理列表 |
| `ProjectEdit.vue` | 项目编辑页 |
| `ProjectForm.vue` | 项目表单组件 |
| `ProjectLinkEditor.vue` | 链接编辑器组件 |
| `ProjectTagManage.vue` | 标签管理 |

### 4.3 页面布局设计

#### 作品集首页

```
┌─────────────────────────────────────────────────────────┐
│  Header: Logo | 首页 | 博客 | 作品集 | 创作 | 关于     │
├─────────────────────────────────────────────────────────┤
│  Hero: 作品集标题 + 简介                                │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────┐    │
│  │  标签筛选: [全部] [Web] [Mobile] [CLI] [其他]    │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌───────────┐  ┌───────────┐  ┌───────────┐           │
│  │ ┌───────┐ │  │ ┌───────┐ │  │ ┌───────┐ │           │
│  │ │ 封面图 │ │  │ │ 封面图 │ │  │ │ 封面图 │ │           │
│  │ └───────┘ │  │ └───────┘ │  │ └───────┘ │           │
│  │ 项目名称   │  │ 项目名称   │  │ 项目名称   │           │
│  │ 项目描述   │  │ 项目描述   │  │ 项目描述   │           │
│  │ Vue | Java│  │ React | Go│  │ Python    │           │
│  │ [演示][源码]│  │ [演示]    │  │ [源码]    │           │
│  └───────────┘  └───────────┘  └───────────┘           │
│  ┌───────────┐  ┌───────────┐  ┌───────────┐           │
│  │   ...     │  │   ...     │  │   ...     │           │
│  └───────────┘  └───────────┘  └───────────┘           │
├─────────────────────────────────────────────────────────┤
│  Footer: 版权信息                                       │
└─────────────────────────────────────────────────────────┘
```

#### 项目详情页

```
┌─────────────────────────────────────────────────────────┐
│  Header                                                  │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────┐    │
│  │  ┌─────────────┐  项目名称                       │    │
│  │  │             │  Web | 已发布                    │    │
│  │  │   封面图     │  创建于 2026-03-07             │    │
│  │  │             │                                  │    │
│  │  └─────────────┘  项目描述...                     │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  链接: [在线演示] [GitHub源码]                   │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  技术栈: Vue.js | Spring Boot | MySQL | Redis   │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  项目详情 (Markdown 渲染)                        │    │
│  │  ## 项目背景                                     │    │
│  │  ...                                            │    │
│  └─────────────────────────────────────────────────┘    │
├─────────────────────────────────────────────────────────┤
│  Footer                                                  │
└─────────────────────────────────────────────────────────┘
```

#### 管理后台 - 项目编辑页

```
┌─────────────────────────────────────────────────────────┐
│  Sidebar | 作品集管理 > 新建项目                        │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────┐    │
│  │  项目名称: [________________________]            │    │
│  │  Slug: [________________________] (自动生成)    │    │
│  │  描述: [________________________]               │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  封面图: [上传图片] 或 [输入URL]                 │    │
│  │  ┌─────────────────────────────────────────┐    │    │
│  │  │           预览区域                        │    │    │
│  │  └─────────────────────────────────────────┘    │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  技术栈: [Vue] [Spring Boot] [+]               │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  项目链接                                        │    │
│  │  ┌────────────────────────────────────────┐     │    │
│  │  │ 类型: [演示 ▼] 标签: [在线演示]         │     │    │
│  │  │ 链接: [https://...]                     │     │    │
│  │  └────────────────────────────────────────┘     │    │
│  │  ┌────────────────────────────────────────┐     │    │
│  │  │ 类型: [源码 ▼] 标签: [GitHub]           │     │    │
│  │  │ 链接: [https://github.com/...]          │     │    │
│  │  └────────────────────────────────────────┘     │    │
│  │  [+ 添加链接]                                   │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  标签: [Web] [Frontend] [+]                     │    │
│  │  状态: [已发布 ▼]                               │    │
│  └─────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────┐    │
│  │  详细内容 (Markdown 编辑器，可选)                │    │
│  │  [编辑器区域]                                   │    │
│  └─────────────────────────────────────────────────┘    │
│  [保存] [发布]                                           │
└─────────────────────────────────────────────────────────┘
```

### 4.4 设计规范

#### 项目卡片样式

```scss
.project-card {
  border-radius: 12px;
  background: var(--card-bg);
  overflow: hidden;
  transition: transform 0.2s, box-shadow 0.2s;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
  }

  &__cover {
    width: 100%;
    aspect-ratio: 16 / 9;
    object-fit: cover;
  }

  &__content {
    padding: 16px;
  }

  &__name {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 8px;
  }

  &__description {
    font-size: 14px;
    color: var(--text-secondary);
    line-height: 1.5;
    margin-bottom: 12px;
  }

  &__tech-stack {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-bottom: 12px;
  }

  &__links {
    display: flex;
    gap: 12px;
  }
}
```

#### 技术栈标签样式

```scss
.tech-badge {
  display: inline-block;
  padding: 4px 12px;
  background: var(--badge-bg);
  color: var(--badge-text);
  border-radius: 16px;
  font-size: 12px;
  font-weight: 500;
}
```

---

## 5. 技术实现要点

### 5.1 项目链接管理

项目链接支持多个，使用单独的 `project_link` 表存储：

```java
// 创建/更新项目时同步处理链接
@Transactional
public void saveProject(ProjectRequest request) {
    // 1. 保存项目基本信息
    Project project = saveProjectBasic(request);

    // 2. 删除旧的链接
    projectLinkMapper.deleteByProjectId(project.getId());

    // 3. 保存新的链接
    if (request.getLinks() != null) {
        request.getLinks().forEach(link -> {
            link.setProjectId(project.getId());
            projectLinkMapper.insert(link);
        });
    }
}
```

### 5.2 技术栈存储

技术栈使用 JSON 数组存储，便于前端直接使用：

```java
// Entity
@TableField(typeHandler = JacksonTypeHandler.class)
private List<String> techStack;

// MyBatis Plus 配置
@TableName(autoResultMap = true)
public class Project {
    // ...
}
```

### 5.3 标签筛选

```sql
-- 按标签筛选项目
SELECT DISTINCT p.*
FROM project p
JOIN project_tag_relation ptr ON p.id = ptr.project_id
WHERE ptr.tag_id = #{tagId}
AND p.status = 'RELEASED'
ORDER BY p.created_at DESC;
```

---

## 6. 任务清单

### 6.1 后端开发

#### 接入层
- [x] ProjectController（公开接口）
- [x] ProjectAdminController（管理接口）
- [x] ProjectTagController
- [x] ProjectRequest / ProjectResponse
- [x] ProjectLinkRequest / ProjectLinkResponse
- [x] ProjectTagRequest / ProjectTagResponse

#### 应用服务层
- [x] ProjectCommandService
- [x] ProjectQueryService
- [x] ProjectTagService

#### 领域层
- [x] Project Entity
- [x] ProjectLink Entity
- [x] ProjectTag Entity
- [x] ProjectRepository
- [x] ProjectLinkRepository
- [x] ProjectTagRepository

#### 基础设施层
- [x] ProjectMapper
- [x] ProjectLinkMapper
- [x] ProjectTagMapper
- [x] ProjectTagRelationMapper

### 6.2 数据库
- [x] project 表
- [x] project_link 表
- [x] project_tag 表（独立表）
- [x] project_tag_relation 表

### 6.3 前端开发

#### Site 端
- [x] PortfolioList.vue（作品集首页）
- [x] PortfolioDetail.vue（项目详情）
- [x] ProjectCard.vue（项目卡片）- 集成在列表页
- [x] ProjectFilter.vue（标签筛选）- 集成在列表页
- [x] TechStackBadge.vue（技术栈标签）- 集成在组件中
- [x] ProjectLinks.vue（项目链接）- 集成在详情页

#### Admin 端
- [x] ProjectList.vue（项目管理列表）
- [x] ProjectEdit.vue（项目编辑）
- [x] ProjectForm.vue（项目表单）- 集成在编辑页
- [x] ProjectLinkEditor.vue（链接编辑器）- 集成在编辑页
- [x] ProjectTagManage.vue（标签管理）

---

## 7. 验收标准

### 功能验收
- [x] 项目可以创建、编辑、删除
- [x] 项目状态流转正常（开发中/已发布/已归档）
- [x] 项目可以添加多个链接
- [x] 项目可以设置封面图
- [x] 项目可以设置标签
- [x] 项目可以按标签筛选
- [x] 技术栈正确显示
- [x] 前端作品集页面正常展示

### 性能验收
- [x] 项目列表加载 < 500ms
- [x] 项目详情加载 < 300ms

### 兼容性验收
- [x] MySQL 环境功能正常
- [x] SQLite 环境功能正常

### 测试验收
- [ ] 后端单元测试覆盖率 > 60%
- [ ] 核心 API 集成测试通过
- [ ] 前端组件测试覆盖关键组件

---

## 下一阶段

完成 Phase 4 后，进入 [Phase 5: 创作模块](./phase-5-creation-module.md)。