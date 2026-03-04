# 编码约定

## 通用约定

### 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 文件名 | PascalCase | `UserController.java`, `LoginPage.vue` |
| 类名 | PascalCase | `UserServiceImpl`, `ArticleController` |
| 方法名 | camelCase | `getUserById()`, `createArticle()` |
| 变量名 | camelCase | `userName`, `articleList` |
| 常量名 | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT`, `DEFAULT_PAGE_SIZE` |
| 包名 | 全小写 | `org.mydotey.ai.site.blog` |

### Git 提交规范

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Type 类型**:
- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式（不影响逻辑）
- `refactor`: 重构
- `test`: 测试相关
- `chore`: 构建/工具/依赖
- `perf`: 性能优化

**示例**:
```
feat(blog): 添加文章评论功能

- 支持嵌套评论
- 添加评论点赞
- 评论审核功能

Closes #123
```

---

## 前端约定

### 目录结构约定

```
src/
├── api/           # API 请求模块
├── assets/        # 静态资源
├── components/    # 组件
│   ├── common/    # 通用组件 (BaseButton, BaseInput)
│   └── business/  # 业务组件 (ArticleCard, MediaUploader)
├── composables/   # 组合式函数 (useXxx)
├── directives/    # 自定义指令
├── layouts/       # 布局组件
├── router/        # 路由配置
├── stores/        # Pinia 状态管理
├── types/         # TypeScript 类型定义
├── utils/         # 工具函数
└── views/         # 页面视图
```

### 组件命名

- 组件文件：PascalCase (`UserCard.vue`)
- 通用组件：`Base` 前缀 (`BaseButton.vue`)
- 业务组件：语义化命名 (`ArticleCard.vue`)
- 组合式函数：`use` 前缀 (`useAuth.ts`)

### Vue 组件结构

```vue
<script setup lang="ts">
// 1. imports
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'

// 2. props/emits
const props = defineProps<{
  title: string
}>()

const emit = defineEmits<{
  (e: 'update', value: string): void
}>()

// 3. composables
const route = useRoute()

// 4. reactive state
const loading = ref(false)

// 5. computed
const displayTitle = computed(() => props.title.toUpperCase())

// 6. methods
function handleSubmit() {
  emit('update', 'value')
}

// 7. lifecycle hooks
onMounted(() => {
  // 初始化逻辑
})
</script>

<template>
  <div class="component">
    <!-- 模板内容 -->
  </div>
</template>

<style scoped lang="scss">
.component {
  /* 样式 */
}
</style>
```

### TypeScript 约定

- 使用 strict mode
- 优先使用 interface 定义对象类型
- 使用 type 定义联合类型、工具类型
- 避免使用 any，使用 unknown + 类型守卫

---

## 后端约定

### 包结构约定

```
org.mydotey.ai.site/
├── common/                # 公共模块
│   ├── config/
│   ├── exception/
│   ├── response/
│   ├── entity/
│   ├── enums/
│   ├── util/
│   ├── annotation/
│   └── constant/
├── {domain}/              # 领域模块 (auth, blog, portfolio, creation, media)
│   ├── controller/        # 接入层
│   ├── dto/               # 接入层
│   ├── command/           # 应用服务层
│   ├── query/             # 应用服务层
│   ├── job/               # 应用服务层
│   ├── entity/            # 领域层
│   ├── repository/        # 领域层
│   └── enums/             # 领域层
└── infrastructure/        # 基础设施层
    ├── persistence/
    ├── security/
    └── storage/
```

### 类命名约定

| 类型 | 后缀 | 示例 |
|------|------|------|
| Controller | Controller | `BlogController` |
| DTO (Request) | Request | `CreateArticleRequest` |
| DTO (Response) | Response | `ArticleResponse` |
| DTO (View Object) | VO | `ArticleVO` |
| Command | Command | `CreateArticleCommand` |
| Command Service | CommandService | `BlogCommandService` |
| Query | Query | `ArticleListQuery` |
| Query Service | QueryService | `BlogQueryService` |
| Entity | 无后缀 | `Article`, `User` |
| Repository (接口) | Repository | `ArticleRepository` |
| Repository (实现) | RepositoryImpl | `ArticleRepositoryImpl` |
| Mapper | Mapper | `ArticleMapper` |
| Service (领域) | Service | `PasswordService` |

### API 响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { ... },
  "timestamp": "2024-01-15T10:30:00"
}
```

### RESTful API 设计

```
# 查询操作 (GET)
GET  /api/v1/articles           # 列表
GET  /api/v1/articles/{id}      # 详情

# 创建操作 (POST)
POST /api/v1/articles           # 创建

# 更新操作 (PUT/PATCH)
PUT  /api/v1/articles/{id}      # 全量更新

# 删除操作 (DELETE)
DELETE /api/v1/articles/{id}    # 删除

# 分页查询
GET /api/v1/articles?page=1&size=20&sort=createdAt,desc
```

### 异常处理

```java
// 业务异常
throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);

// 错误码定义
public enum ErrorCode {
    ARTICLE_NOT_FOUND(40401, "文章不存在"),
    UNAUTHORIZED(40100, "未授权访问"),
    // ...
}
```

---

## 代码风格

### 注释规范

- 类注释：说明类的职责和用途
- 方法注释：说明方法的功能、参数、返回值
- 复杂逻辑：添加必要的行内注释
- 避免无意义的注释

### 代码组织

- 单个文件不超过 500 行
- 单个方法不超过 50 行
- 单个类职责单一
- 避免深层嵌套（最多 3 层）