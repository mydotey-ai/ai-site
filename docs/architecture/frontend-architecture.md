# 前端架构

## 项目结构

```
frontend/
├── admin/              # 管理后台项目
├── site/               # 用户网站项目
├── shared/             # 共享代码
└── pnpm-workspace.yaml # 工作区配置
```

## Admin 项目 (管理后台)

### 技术栈

| 技术 | 用途 |
|------|------|
| Vue 3.5+ | 核心框架 |
| Vite 6.x | 构建工具 |
| TypeScript | 类型系统 |
| Naive UI | UI 组件库 |
| Pinia | 状态管理 |
| Vue Router | 路由管理 |
| Axios | HTTP 客户端 |

### 目录结构

```
admin/
├── public/             # 静态资源
├── src/
│   ├── api/            # API 请求模块
│   ├── assets/         # 静态资源
│   │   ├── images/     # 图片
│   │   └── styles/     # 样式
│   ├── components/     # 组件
│   │   ├── common/     # 通用组件
│   │   └── business/   # 业务组件
│   ├── composables/    # 组合式函数
│   ├── directives/     # 自定义指令
│   ├── layouts/        # 布局组件
│   ├── router/         # 路由配置
│   │   ├── index.ts    # 路由入口
│   │   ├── guards.ts   # 路由守卫
│   │   └── routes/     # 路由模块
│   ├── stores/         # Pinia 状态管理
│   ├── types/          # TypeScript 类型
│   ├── utils/          # 工具函数
│   ├── views/          # 页面视图
│   ├── App.vue         # 根组件
│   └── main.ts         # 入口文件
├── tests/              # 测试文件
├── .env                # 环境变量
├── index.html          # HTML 模板
├── package.json
├── tsconfig.json
└── vite.config.ts
```

### 页面模块

```
views/
├── dashboard/          # 仪表盘
├── blog/               # 博客管理
│   ├── ArticleList.vue
│   ├── ArticleEdit.vue
│   └── CategoryManage.vue
├── portfolio/          # 作品集管理
├── creation/           # 创作管理
├── media/              # 多媒体管理
├── user/               # 用户管理
└── login/              # 登录页
```

## Site 项目 (用户网站)

### 技术栈

| 技术 | 用途 |
|------|------|
| Vue 3.5+ | 核心框架 |
| Vite 6.x | 构建工具 |
| TypeScript | 类型系统 |
| 自定义 UI | 组件库 (基于 Naive UI 或自定义) |
| Pinia | 状态管理 |
| Vue Router | 路由管理 |
| Axios | HTTP 客户端 |

### 目录结构

```
site/
├── public/
├── src/
│   ├── api/
│   ├── assets/
│   ├── components/
│   ├── composables/
│   ├── directives/
│   ├── layouts/
│   ├── router/
│   ├── stores/
│   ├── types/
│   ├── utils/
│   ├── views/
│   │   ├── home/       # 首页
│   │   ├── blog/       # 博客
│   │   ├── portfolio/  # 作品集
│   │   ├── creation/   # 创作展示
│   │   ├── media/      # 多媒体展示
│   │   ├── about/      # 关于页面
│   │   └── error/      # 错误页面
│   ├── App.vue
│   └── main.ts
├── tests/
├── .env
├── index.html
├── package.json
├── tsconfig.json
└── vite.config.ts
```

## Shared 共享代码

### 目录结构

```
shared/
├── src/
│   ├── components/     # 共享组件
│   ├── composables/    # 共享组合式函数
│   ├── types/          # 共享类型定义
│   └── utils/          # 共享工具函数
├── package.json
└── tsconfig.json
```

### 使用方式

```typescript
// 在 admin 或 site 项目中引用
import { BaseButton } from '@ai-site/shared/components'
import { useRequest } from '@ai-site/shared/composables'
import type { Article } from '@ai-site/shared/types'
import { formatDate } from '@ai-site/shared/utils'
```

## 状态管理

### Pinia Store 结构

```typescript
// stores/auth.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  // State
  const user = ref<User | null>(null)
  const token = ref<string | null>(null)

  // Getters
  const isLoggedIn = computed(() => !!token.value)
  const username = computed(() => user.value?.username ?? '')

  // Actions
  async function login(credentials: LoginParams) {
    const response = await authApi.login(credentials)
    user.value = response.user
    token.value = response.token
  }

  function logout() {
    user.value = null
    token.value = null
  }

  return {
    user,
    token,
    isLoggedIn,
    username,
    login,
    logout
  }
})
```

## 路由设计

### 路由配置

```typescript
// router/routes/blog.ts
export const blogRoutes: RouteRecordRaw[] = [
  {
    path: '/blog',
    component: () => import('@/layouts/BlogLayout.vue'),
    children: [
      {
        path: '',
        name: 'BlogList',
        component: () => import('@/views/blog/BlogList.vue'),
        meta: { title: '博客列表' }
      },
      {
        path: ':slug',
        name: 'BlogDetail',
        component: () => import('@/views/blog/BlogDetail.vue'),
        meta: { title: '文章详情' }
      }
    ]
  }
]
```

### 路由守卫

```typescript
// router/guards.ts
export function setupRouterGuards(router: Router) {
  router.beforeEach((to, from, next) => {
    // 设置页面标题
    document.title = to.meta.title ?? 'AI-Site'

    // 登录检查
    const authStore = useAuthStore()
    if (to.meta.requiresAuth && !authStore.isLoggedIn) {
      next({ name: 'Login', query: { redirect: to.fullPath } })
      return
    }

    next()
  })
}
```

## API 请求

### Axios 封装

```typescript
// utils/request.ts
import axios from 'axios'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器
request.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const message = error.response?.data?.message ?? '请求失败'
    // 显示错误提示
    return Promise.reject(error)
  }
)

export default request
```

### API 模块化

```typescript
// api/blog.ts
import request from '@/utils/request'
import type { Article, PageResult } from '@/types'

export const blogApi = {
  list(params: PageParams): Promise<PageResult<Article>> {
    return request.get('/api/v1/articles', { params })
  },

  get(id: number): Promise<Article> {
    return request.get(`/api/v1/articles/${id}`)
  },

  create(data: CreateArticleParams): Promise<Article> {
    return request.post('/api/v1/articles', data)
  },

  update(id: number, data: UpdateArticleParams): Promise<void> {
    return request.put(`/api/v1/articles/${id}`, data)
  },

  delete(id: number): Promise<void> {
    return request.delete(`/api/v1/articles/${id}`)
  }
}
```

## 构建优化

### Vite 配置

```typescript
// vite.config.ts
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          'vue-vendor': ['vue', 'vue-router', 'pinia'],
          'ui-vendor': ['naive-ui']
        }
      }
    }
  }
})
```

### 性能优化

- **路由懒加载**: 页面组件按需加载
- **组件懒加载**: 大型组件使用 `defineAsyncComponent`
- **代码分割**: 第三方库分离打包
- **资源压缩**: 图片压缩、代码压缩
- **缓存策略**: 合理使用浏览器缓存