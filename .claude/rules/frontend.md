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

### admin 项目
```
frontend/admin/
├── src/
│   ├── api/           # API 请求
│   ├── assets/        # 静态资源
│   ├── components/    # 组件
│   ├── composables/   # 组合式函数
│   ├── directives/    # 自定义指令
│   ├── layouts/       # 布局组件
│   ├── router/        # 路由配置
│   ├── stores/        # 状态管理
│   ├── types/         # 类型定义
│   ├── utils/         # 工具函数
│   └── views/         # 页面视图
└── ...
```

### site 项目
```
frontend/site/
├── src/
│   ├── api/           # API 请求
│   ├── assets/        # 静态资源
│   ├── components/    # 组件
│   ├── composables/   # 组合式函数
│   ├── directives/    # 自定义指令
│   ├── layouts/       # 布局组件
│   ├── router/        # 路由配置
│   ├── stores/        # 状态管理
│   ├── types/         # 类型定义
│   ├── utils/         # 工具函数
│   └── views/         # 页面视图
└── ...
```

### shared 共享代码
```
frontend/shared/
├── src/
│   ├── components/    # 共享组件
│   ├── composables/   # 共享组合式函数
│   ├── types/         # 共享类型定义
│   └── utils/         # 共享工具函数
└── ...
```

---

## 编码规范

### 组件规范

#### 文件命名
- 组件文件：PascalCase (`UserCard.vue`)
- 通用组件：`Base` 前缀 (`BaseButton.vue`)
- 业务组件：语义化命名 (`ArticleCard.vue`)

#### 组件结构
```vue
<script setup lang="ts">
// 1. imports
import { ref, computed, onMounted } from 'vue'

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
const displayTitle = computed(() => props.title)

// 6. methods
function handleSubmit() {}

// 7. lifecycle hooks
onMounted(() => {})
</script>

<template>
  <div class="component">
    <!-- 内容 -->
  </div>
</template>

<style scoped lang="scss">
.component {
  /* 样式 */
}
</style>
```

### TypeScript 规范

#### 类型定义
```typescript
// 优先使用 interface 定义对象类型
interface User {
  id: number
  name: string
  email: string
}

// 使用 type 定义联合类型、工具类型
type Status = 'active' | 'inactive'
type UserResponse = ApiResponse<User>
```

#### 避免 any
```typescript
// ❌ 避免
function process(data: any) {
  return data.value
}

// ✅ 使用泛型或 unknown
function process<T extends { value: unknown }>(data: T) {
  return data.value
}
```

### API 请求规范

#### Axios 封装
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
    // 统一错误处理
    return Promise.reject(error)
  }
)

export default request
```

#### API 模块
```typescript
// api/blog.ts
import request from '@/utils/request'
import type { Article, PageResult } from '@/types'

export const blogApi = {
  getArticleList(params: PageParams): Promise<PageResult<Article>> {
    return request.get('/api/v1/articles', { params })
  },

  getArticle(id: number): Promise<Article> {
    return request.get(`/api/v1/articles/${id}`)
  },

  createArticle(data: CreateArticleParams): Promise<Article> {
    return request.post('/api/v1/articles', data)
  }
}
```

### 状态管理规范

#### Pinia Store
```typescript
// stores/auth.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User } from '@/types'

export const useAuthStore = defineStore('auth', () => {
  // state
  const user = ref<User | null>(null)
  const token = ref<string | null>(localStorage.getItem('token'))

  // getters
  const isLoggedIn = computed(() => !!token.value)

  // actions
  async function login(credentials: LoginParams) {
    const response = await authApi.login(credentials)
    user.value = response.user
    token.value = response.token
    localStorage.setItem('token', response.token)
  }

  function logout() {
    user.value = null
    token.value = null
    localStorage.removeItem('token')
  }

  return {
    user,
    token,
    isLoggedIn,
    login,
    logout
  }
})
```

### 路由规范

#### 路由配置
```typescript
// router/routes/blog.ts
import type { RouteRecordRaw } from 'vue-router'

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
        path: ':id',
        name: 'BlogDetail',
        component: () => import('@/views/blog/BlogDetail.vue'),
        meta: { title: '文章详情' }
      }
    ]
  }
]
```

#### 路由守卫
```typescript
// router/guards.ts
import type { Router } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

export function setupRouterGuards(router: Router) {
  router.beforeEach((to, from, next) => {
    const authStore = useAuthStore()

    // 需要登录的页面
    if (to.meta.requiresAuth && !authStore.isLoggedIn) {
      next({ name: 'Login', query: { redirect: to.fullPath } })
      return
    }

    next()
  })
}
```

---

## 样式规范

### CSS 变量
```scss
// assets/styles/variables.scss
:root {
  // 颜色
  --color-primary: #1890ff;
  --color-success: #52c41a;
  --color-warning: #faad14;
  --color-error: #f5222d;

  // 间距
  --spacing-xs: 4px;
  --spacing-sm: 8px;
  --spacing-md: 16px;
  --spacing-lg: 24px;
  --spacing-xl: 32px;

  // 圆角
  --radius-sm: 4px;
  --radius-md: 8px;
  --radius-lg: 12px;
}
```

### 组件样式
```vue
<style scoped lang="scss">
.component {
  padding: var(--spacing-md);

  &__title {
    font-size: 18px;
    font-weight: 600;
  }

  &__content {
    margin-top: var(--spacing-sm);
  }
}
</style>
```

---

## 测试规范

### 单元测试
```typescript
// tests/unit/components/BaseButton.test.ts
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import BaseButton from '@/components/common/BaseButton.vue'

describe('BaseButton', () => {
  it('should render correctly', () => {
    const wrapper = mount(BaseButton, {
      slots: { default: 'Click me' }
    })
    expect(wrapper.text()).toBe('Click me')
  })

  it('should emit click event', async () => {
    const wrapper = mount(BaseButton)
    await wrapper.trigger('click')
    expect(wrapper.emitted('click')).toBeTruthy()
  })
})
```

---

## 性能优化

### 懒加载
```typescript
// 路由懒加载
const BlogList = () => import('@/views/blog/BlogList.vue')

// 组件懒加载
const HeavyComponent = defineAsyncComponent(() =>
  import('@/components/HeavyComponent.vue')
)
```

### 响应式优化
```typescript
// 大列表使用 shallowRef
const largeList = shallowRef<LargeItem[]>([])

// 使用 computed 缓存
const filteredList = computed(() =>
  largeList.value.filter(item => item.active)
)
```

---

## 工具配置

### ESLint
```javascript
// eslint.config.js
import js from '@eslint/js'
import vue from 'eslint-plugin-vue'
import typescript from '@typescript-eslint/eslint-plugin'
import parser from 'vue-eslint-parser'

export default [
  js.configs.recommended,
  ...vue.configs['flat/recommended'],
  {
    files: ['**/*.vue', '**/*.ts'],
    languageOptions: {
      parser,
      parserOptions: {
        ecmaVersion: 'latest',
        sourceType: 'module'
      }
    },
    plugins: {
      '@typescript-eslint': typescript
    },
    rules: {
      'vue/multi-word-component-names': 'off',
      '@typescript-eslint/no-unused-vars': 'error'
    }
  }
]
```

### Vite 配置
```typescript
// vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```