# 前端开发规则

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5+ | 核心框架 |
| Vite | 6.x | 构建工具 |
| TypeScript | 5.x | 类型系统 (strict mode) |
| Pinia | 最新 | 状态管理 |
| Vue Router | 4.x | 路由管理 |
| Axios | 最新 | HTTP 客户端 |
| Naive UI | 最新 | UI 组件库 |
| SCSS | 最新 | CSS 预处理器 |

---

## 项目结构

### 项目目录
```
frontend/
├── admin/           # 管理后台
│   └── src/
│       ├── api/         # API 请求
│       ├── assets/      # 静态资源
│       ├── components/  # 组件
│       ├── composables/ # 组合式函数
│       ├── directives/  # 自定义指令
│       ├── layouts/     # 布局组件
│       ├── router/      # 路由配置
│       ├── stores/      # 状态管理
│       ├── types/       # 类型定义
│       ├── utils/       # 工具函数
│       └── views/       # 页面视图
├── site/            # 用户网站 (结构同 admin)
└── shared/          # 共享代码
    └── src/
        ├── components/  # 共享组件
        ├── composables/ # 共享组合式函数
        ├── types/       # 共享类型定义
        └── utils/       # 共享工具函数
```

---

## 编码规范

### 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 组件文件 | PascalCase | `UserCard.vue`, `LoginPage.vue` |
| 组件文件夹 | PascalCase | `components/UserCard/` |
| 通用组件 | `Base` 前缀 | `BaseButton.vue`, `BaseInput.vue` |
| 业务组件 | 语义化命名 | `ArticleCard.vue`, `MediaUploader.vue` |
| 组合式函数 | `use` 前缀 | `useAuth.ts`, `useBlog.ts` |
| 工具函数 | camelCase | `formatDate.ts`, `validateEmail.ts` |
| 类型文件 | PascalCase | `User.ts`, `Article.ts` |
| 常量 | UPPER_SNAKE_CASE | `API_BASE_URL`, `MAX_RETRY_COUNT` |
| 变量 | camelCase | `userName`, `articleList` |
| 方法/函数 | camelCase | `getUserById()`, `createArticle()` |

### 代码风格

- 单个组件文件不超过 300 行
- 单个函数不超过 30 行
- 单个组件职责单一
- 避免深层嵌套（最多 3 层）
- 组件注释：说明组件用途和 props
- 复杂逻辑：添加必要的行内注释
- 避免无意义的注释

### 组件结构

使用 `<script setup>` 语法，按以下顺序组织：
1. imports
2. props/emits
3. composables
4. reactive state
5. computed
6. methods
7. lifecycle hooks

### TypeScript 规范

- 优先使用 `interface` 定义对象类型
- 使用 `type` 定义联合类型、工具类型
- 避免使用 `any`，使用泛型或 `unknown`
- 开启 strict mode
- **后端 `Long` 类型字段使用 `bigint`**：通过 `json-bigint` 处理大数字精度问题

### API 请求规范

- 统一封装 Axios 实例（`utils/request.ts`）
- 请求拦截器：添加 token
- 响应拦截器：统一错误处理
- API 按模块组织（`api/xxx.ts`）
- 返回类型明确标注

### 状态管理规范

- 使用 Pinia + Composition API
- Store 使用 `defineStore` + setup 语法
- state 使用 `ref`/`reactive`
- getters 使用 `computed`
- actions 定义为函数并返回

### 路由规范

- 路由按模块拆分（`router/routes/xxx.ts`）
- 使用路由懒加载
- meta 字段记录页面信息（title、requiresAuth 等）
- 路由守卫统一处理权限验证

---

## 安全规范

- 防止 XSS 攻击：对用户输入进行转义
- 使用 Content-Security-Policy 头
- 避免使用 `v-html`，必要时使用 DOMPurify 清理
- 敏感数据不在客户端存储，如需存储需加密

---

## 样式规范

- 使用 CSS 变量定义主题色、间距、圆角等
- 组件样式使用 `scoped` + SCSS
- 遵循 BEM 命名规范

### 设计系统

遵循 [设计系统规则](./design-system.md)，核心要点：

- **设计理念**: 大方、时尚、科技感
- **主题**: 双主题切换（深色默认）
- **配色**: 蓝色系 (#3b82f6)
- **布局**: 全宽布局，最大 1440px
- **字体**: 思源黑体
- **动画**: 适度动画，平滑过渡

---

## 测试规范

- 测试框架：Vitest + @vue/test-utils
- 测试类命名为 `{ComponentName}.test.ts`
- 测试方法使用 `should {ExpectedBehavior}` 格式

---

## 性能优化

- 路由懒加载：`() => import('@/views/xxx.vue')`
- 组件懒加载：`defineAsyncComponent()`
- 大列表使用 `shallowRef`
- 使用 `computed` 缓存计算结果

---

## 环境配置

- 开发环境：`.env.development`
- 生产环境：`.env.production`
- 示例文件：`.env.example`
- 环境变量以 `VITE_` 前缀开头
- 敏感配置使用环境变量注入，不提交到代码库

---

## 工具配置

- ESLint：使用 flat config，集成 Vue 和 TypeScript 规则
- Vite：配置路径别名 `@`、代理、端口等