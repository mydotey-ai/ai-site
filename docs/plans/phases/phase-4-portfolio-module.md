# Phase 4: 作品集模块

## 阶段目标

实现作品集功能，包括：
- 项目展示
- 技能展示
- 工作经历
- 前端作品集页面

## 任务清单

### 1. 后端 - Portfolio 领域

#### 接入层
- [ ] PortfolioController
- [ ] ProjectRequest, ProjectResponse
- [ ] SkillRequest, SkillResponse
- [ ] ExperienceRequest, ExperienceResponse

#### 应用服务层
- [ ] PortfolioCommandService
- [ ] PortfolioQueryService

#### 领域层
- [ ] Project Entity
- [ ] Skill Entity
- [ ] Experience Entity
- [ ] ProjectRepository
- [ ] SkillRepository

#### 基础设施层
- [ ] ProjectMapper
- [ ] SkillMapper
- [ ] ExperienceMapper

### 2. 数据库

- [ ] project 表
- [ ] skill 表
- [ ] experience 表

### 3. 前端 - 作品集页面

#### Admin
- [ ] ProjectList.vue
- [ ] ProjectEdit.vue
- [ ] SkillManage.vue
- [ ] ExperienceManage.vue

#### Site
- [ ] PortfolioList.vue
- [ ] PortfolioDetail.vue
- [ ] ProjectCard.vue
- [ ] SkillSection.vue

## API 设计

### 项目列表

```
GET /api/v1/projects
Response:
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "name": "项目名称",
        "description": "项目描述",
        "techStack": ["Vue", "Spring Boot"],
        "demoUrl": "https://...",
        "coverImage": "..."
      }
    ]
  }
}
```

### 技能列表

```
GET /api/v1/skills
Response:
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "name": "Vue.js",
        "category": "前端",
        "level": 5
      }
    ]
  }
}
```

## 验收标准

- [ ] 项目可以管理
- [ ] 技能可以管理
- [ ] 工作经历可以管理
- [ ] 前端作品集页面可正常访问

## 下一阶段

完成 Phase 4 后，进入 [Phase 5: 创作模块](./phase-5-creation-module.md)。