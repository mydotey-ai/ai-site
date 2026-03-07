# Admin 项目设计系统

> 面向管理的后台前端设计系统

---

## 设计理念

**关键词：大方、时尚、科技感**

| 关键词 | 设计体现 |
|--------|---------|
| **大方** | 清晰层级、高效布局、信息密度合理 |
| **时尚** | 现代配色、简洁线条、圆润边角 |
| **科技感** | 深色主题、数据可视化、代码高亮 |

---

## 项目定位

| 属性 | 描述 |
|------|------|
| **类型** | 管理后台 (Content Management System) |
| **受众** | 网站管理员（网站主人） |
| **模式** | Dashboard + Sidebar Navigation |
| **核心** | 高效操作、数据可视化、快速响应 |

---

## 主题系统

### 双主题支持

支持 **深色主题** 和 **浅色主题** 切换，默认深色主题。

```scss
// 主题切换
data-theme="dark"   // 深色主题（默认）
data-theme="light"  // 浅色主题
```

### 配色方案

#### 蓝色主题色

| 名称 | 色值 | 用途 |
|------|------|------|
| Primary | `#3b82f6` | 主色调 |
| Primary Hover | `#60a5fa` | 悬停态 |
| Primary Active | `#2563eb` | 激活态 |
| Success | `#22c55e` | 成功状态 |
| Warning | `#f59e0b` | 警告状态 |
| Error | `#ef4444` | 错误状态 |

#### 深色主题变量

```scss
:root[data-theme="dark"] {
  // 背景
  --bg-primary: #0a0a0f;      // 主背景
  --bg-secondary: #12121a;    // 侧边栏背景
  --bg-tertiary: #1a1a24;     // 卡片/表格背景
  --bg-hover: #22222e;        // 悬停背景
  --bg-active: #2a2a36;       // 激活背景

  // 文字
  --text-primary: #f8fafc;    // 主文字
  --text-secondary: #94a3b8;  // 次级文字
  --text-muted: #64748b;      // 弱化文字

  // 边框
  --border-primary: #2d2d3a;
  --border-secondary: #1e1e28;

  // 主题色
  --color-primary: #3b82f6;
  --color-primary-hover: #60a5fa;
  --color-primary-active: #2563eb;

  // 状态色
  --color-success: #22c55e;
  --color-warning: #f59e0b;
  --color-error: #ef4444;
  --color-info: #06b6d4;

  // 科技感效果
  --glow-primary: 0 0 20px rgba(59, 130, 246, 0.3);
}
```

#### 浅色主题变量

```scss
:root[data-theme="light"] {
  // 背景
  --bg-primary: #ffffff;
  --bg-secondary: #f8fafc;
  --bg-tertiary: #f1f5f9;
  --bg-hover: #e2e8f0;
  --bg-active: #cbd5e1;

  // 文字
  --text-primary: #0f172a;
  --text-secondary: #475569;
  --text-muted: #94a3b8;

  // 边框
  --border-primary: #e2e8f0;
  --border-secondary: #f1f5f9;

  // 主题色
  --color-primary: #2563eb;
  --color-primary-hover: #3b82f6;
  --color-primary-active: #1d4ed8;
}
```

---

## 字体系统

### 字体家族

```scss
// 主字体：思源黑体
$font-family: "Noto Sans SC", -apple-system, BlinkMacSystemFont, "Segoe UI",
              Roboto, "Helvetica Neue", Arial, sans-serif;

// 等宽字体：代码
$font-family-mono: "JetBrains Mono", "Fira Code", "Source Code Pro",
                   Menlo, Monaco, Consolas, monospace;
```

### Google Fonts 引入

```css
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+SC:wght@400;500;600;700&display=swap');
```

### 字体大小

| 名称 | 大小 | 用途 |
|------|------|------|
| xs | 12px | 辅助文字、标签、时间 |
| sm | 14px | 次要内容、表格内容 |
| base | 16px | 正文 |
| lg | 18px | 重要正文 |
| xl | 20px | 小标题 |
| 2xl | 24px | 页面标题 |
| 3xl | 30px | 大标题 |

---

## 间距系统

使用 8px 基准网格：

| 名称 | 值 | 用途 |
|------|-----|------|
| spacing-1 | 4px | 极小间距 |
| spacing-2 | 8px | 紧凑间距 |
| spacing-3 | 12px | 小间距 |
| spacing-4 | 16px | 标准间距 |
| spacing-5 | 20px | 中等间距 |
| spacing-6 | 24px | 常用间距 |
| spacing-8 | 32px | 大间距 |
| spacing-10 | 40px | 区块间距 |

---

## 布局系统

### 后台布局

```
┌────────────┬────────────────────────────────────────────────┐
│            │  顶部栏 (64px)                                 │
│  侧边栏    ├────────────────────────────────────────────────┤
│  (240px)   │                                                │
│            │                                                │
│            │              内容区域                          │
│            │                                                │
│            │                                                │
│            │                                                │
└────────────┴────────────────────────────────────────────────┘
```

### 侧边栏

```scss
.sidebar {
  width: 240px;
  height: 100vh;
  background: var(--bg-secondary);
  border-right: 1px solid var(--border-primary);
  position: fixed;
  left: 0;
  top: 0;
  overflow-y: auto;

  &__logo {
    height: 64px;
    display: flex;
    align-items: center;
    padding: 0 24px;
    border-bottom: 1px solid var(--border-primary);
  }

  &__menu {
    padding: 16px 0;
  }

  &__group {
    padding: 8px 16px;
    font-size: 12px;
    color: var(--text-muted);
    text-transform: uppercase;
    letter-spacing: 0.5px;
  }

  &__item {
    padding: 0 12px;
  }

  &__link {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 16px;
    color: var(--text-secondary);
    text-decoration: none;
    border-radius: 8px;
    transition: all 0.2s;

    &:hover {
      background: var(--bg-hover);
      color: var(--text-primary);
    }

    &--active {
      background: var(--color-primary);
      color: white;

      &:hover {
        background: var(--color-primary-hover);
      }
    }
  }

  &__icon {
    width: 20px;
    height: 20px;
    flex-shrink: 0;
  }
}

// 移动端收起
@media (max-width: 1024px) {
  .sidebar {
    width: 64px;

    &__link-text {
      display: none;
    }

    &__group {
      display: none;
    }
  }
}
```

### 顶部栏

```scss
.header {
  height: 64px;
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border-primary);
  position: sticky;
  top: 0;
  z-index: 50;
  margin-left: 240px;

  &__container {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 24px;
  }

  &__title {
    font-size: 18px;
    font-weight: 600;
  }

  &__actions {
    display: flex;
    align-items: center;
    gap: 16px;
  }
}

@media (max-width: 1024px) {
  .header {
    margin-left: 64px;
  }
}
```

### 内容区域

```scss
.content {
  margin-left: 240px;
  padding: 24px;
  min-height: calc(100vh - 64px);
  background: var(--bg-primary);
}

@media (max-width: 1024px) {
  .content {
    margin-left: 64px;
  }
}
```

---

## 响应式断点

| 名称 | 宽度 | 设备 |
|------|------|------|
| sm | 640px | 小屏手机 |
| md | 768px | 平板竖屏 |
| lg | 1024px | 平板横屏 |
| xl | 1280px | 桌面 |
| 2xl | 1536px | 大屏桌面 |

---

## 导航设计

### 侧边栏菜单结构

```
┌────────────────────┐
│  Logo              │
├────────────────────┤
│  仪表盘            │
├────────────────────┤
│  内容管理          │
│  ├─ 博客           │
│  ├─ 作品集         │
│  └─ 创作           │
├────────────────────┤
│  媒体管理          │
│  ├─ 图片           │
│  ├─ 视频           │
│  └─ 音频           │
├────────────────────┤
│  系统设置          │
│  ├─ 用户           │
│  └─ 配置           │
└────────────────────┘
```

---

## 卡片设计

### 数据卡片

```scss
.stat-card {
  background: var(--bg-tertiary);
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;

  &__icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: var(--color-primary);
    color: white;
  }

  &__content {
    flex: 1;
  }

  &__label {
    font-size: 14px;
    color: var(--text-secondary);
    margin-bottom: 4px;
  }

  &__value {
    font-size: 24px;
    font-weight: 700;
    color: var(--text-primary);
  }

  &__trend {
    font-size: 12px;

    &--up {
      color: var(--color-success);
    }

    &--down {
      color: var(--color-error);
    }
  }
}
```

### 表格卡片

```scss
.table-card {
  background: var(--bg-tertiary);
  border-radius: 12px;
  overflow: hidden;

  &__header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 20px;
    border-bottom: 1px solid var(--border-primary);
  }

  &__title {
    font-size: 16px;
    font-weight: 600;
  }

  &__actions {
    display: flex;
    gap: 8px;
  }

  &__table {
    width: 100%;
    border-collapse: collapse;

    th, td {
      padding: 12px 20px;
      text-align: left;
      border-bottom: 1px solid var(--border-primary);
    }

    th {
      font-size: 12px;
      font-weight: 600;
      color: var(--text-muted);
      text-transform: uppercase;
      letter-spacing: 0.5px;
      background: var(--bg-secondary);
    }

    td {
      font-size: 14px;
      color: var(--text-primary);
    }

    tr:hover td {
      background: var(--bg-hover);
    }
  }
}
```

---

## 按钮设计

```scss
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 10px 16px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: all 0.2s;

  // 主要按钮
  &--primary {
    background: var(--color-primary);
    color: white;

    &:hover {
      background: var(--color-primary-hover);
    }

    &:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }
  }

  // 次要按钮
  &--secondary {
    background: var(--bg-tertiary);
    color: var(--text-primary);
    border: 1px solid var(--border-primary);

    &:hover {
      background: var(--bg-hover);
    }
  }

  // 危险按钮
  &--danger {
    background: var(--color-error);
    color: white;

    &:hover {
      background: #dc2626;
    }
  }

  // 文字按钮
  &--text {
    background: transparent;
    color: var(--text-secondary);

    &:hover {
      color: var(--color-primary);
      background: var(--bg-hover);
    }
  }

  // 尺寸
  &--sm {
    padding: 6px 12px;
    font-size: 12px;
  }

  &--lg {
    padding: 12px 24px;
    font-size: 16px;
  }

  // 图标按钮
  &--icon {
    padding: 10px;

    &.btn--sm {
      padding: 6px;
    }
  }
}
```

---

## 表单设计

### 输入框

```scss
.input {
  width: 100%;
  padding: 10px 14px;
  font-size: 14px;
  color: var(--text-primary);
  background: var(--bg-primary);
  border: 1px solid var(--border-primary);
  border-radius: 8px;
  outline: none;
  transition: all 0.2s;

  &::placeholder {
    color: var(--text-muted);
  }

  &:focus {
    border-color: var(--color-primary);
    box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
  }

  &--error {
    border-color: var(--color-error);

    &:focus {
      box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);
    }
  }
}
```

### 表单项

```scss
.form-item {
  margin-bottom: 20px;

  &__label {
    display: block;
    font-size: 14px;
    font-weight: 500;
    color: var(--text-primary);
    margin-bottom: 8px;

    &--required::after {
      content: '*';
      color: var(--color-error);
      margin-left: 4px;
    }
  }

  &__error {
    font-size: 12px;
    color: var(--color-error);
    margin-top: 4px;
  }

  &__hint {
    font-size: 12px;
    color: var(--text-muted);
    margin-top: 4px;
  }
}
```

---

## 标签与徽章

```scss
.badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 10px;
  font-size: 12px;
  font-weight: 500;
  border-radius: 9999px;

  &--default {
    background: var(--bg-tertiary);
    color: var(--text-secondary);
  }

  &--primary {
    background: rgba(59, 130, 246, 0.15);
    color: var(--color-primary);
  }

  &--success {
    background: rgba(34, 197, 94, 0.15);
    color: var(--color-success);
  }

  &--warning {
    background: rgba(245, 158, 11, 0.15);
    color: var(--color-warning);
  }

  &--error {
    background: rgba(239, 68, 68, 0.15);
    color: var(--color-error);
  }
}
```

---

## 状态标签

```scss
.status {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  font-size: 12px;
  font-weight: 500;
  border-radius: 6px;

  &__dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
  }

  &--published {
    background: rgba(34, 197, 94, 0.15);
    color: var(--color-success);

    .status__dot {
      background: var(--color-success);
    }
  }

  &--draft {
    background: rgba(148, 163, 184, 0.15);
    color: var(--text-secondary);

    .status__dot {
      background: var(--text-secondary);
    }
  }

  &--hidden {
    background: rgba(245, 158, 11, 0.15);
    color: var(--color-warning);

    .status__dot {
      background: var(--color-warning);
    }
  }
}
```

---

## 动画系统

### 过渡时间

| 名称 | 时长 | 用途 |
|------|------|------|
| fast | 150ms | 快速反馈 |
| normal | 200ms | 标准过渡 |
| slow | 300ms | 慢速过渡 |

### 加载状态

```scss
// 骨架屏
.skeleton {
  background: linear-gradient(
    90deg,
    var(--bg-tertiary) 25%,
    var(--bg-hover) 50%,
    var(--bg-tertiary) 75%
  );
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
  border-radius: 4px;
}

@keyframes skeleton-loading {
  0% { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

// 旋转加载
.spinner {
  width: 20px;
  height: 20px;
  border: 2px solid var(--border-primary);
  border-top-color: var(--color-primary);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
```

---

## 页面模板

### 仪表盘

```
┌────────────┬────────────────────────────────────────────────┐
│            │  仪表盘                                        │
│  侧边栏    ├────────────────────────────────────────────────┤
│            │  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐
│            │  │ 文章数  │ │ 浏览量  │ │ 评论数  │ │ 项目数  │
│            │  └─────────┘ └─────────┘ └─────────┘ └─────────┘
│            │  ┌─────────────────────┐ ┌─────────────────────┐
│            │  │ 最近文章            │ │ 最近评论            │
│            │  │ ...                 │ │ ...                 │
│            │  └─────────────────────┘ └─────────────────────┘
└────────────┴────────────────────────────────────────────────┘
```

### 列表页

```
┌────────────┬────────────────────────────────────────────────┐
│            │  博客管理                    [+ 新建文章]      │
│  侧边栏    ├────────────────────────────────────────────────┤
│            │  ┌─────────────────────────────────────────────┐
│            │  │ 搜索: [________] 状态: [全部 ▼] 分类: [全部] │
│            │  ├─────────────────────────────────────────────┤
│            │  │ [x] │ 标题     │ 分类   │ 状态   │ 时间   │
│            │  │ [ ] │ 文章1    │ 后端   │ 已发布 │ 03-07  │
│            │  │ [ ] │ 文章2    │ 前端   │ 草稿   │ 03-06  │
│            │  └─────────────────────────────────────────────┘
│            │  [批量删除]                    1/10  [<] [>]   │
└────────────┴────────────────────────────────────────────────┘
```

### 编辑页

```
┌────────────┬────────────────────────────────────────────────┐
│            │  新建文章                    [保存] [发布]     │
│  侧边栏    ├────────────────────────────────────────────────┤
│            │  ┌─────────────────────────────────────────────┐
│            │  │ 标题: [________________________]            │
│            │  │ Slug: [________________________] (自动生成) │
│            │  ├─────────────────────────────────────────────┤
│            │  │ 编辑器区域                                  │
│            │  │ ...                                         │
│            │  ├─────────────────────────────────────────────┤
│            │  │ 分类: [后端开发 ▼]  标签: [Java] [Spring]  │
│            │  │ 状态: [草稿 ▼]      置顶: [ ]              │
│            │  └─────────────────────────────────────────────┘
└────────────┴────────────────────────────────────────────────┘
```

---

## 无障碍设计

### 键盘导航

```scss
// 焦点样式
:focus-visible {
  outline: 2px solid var(--color-primary);
  outline-offset: 2px;
}

// 跳过链接
.skip-link {
  position: absolute;
  top: -40px;
  left: 0;
  padding: 8px 16px;
  background: var(--color-primary);
  color: white;
  z-index: 1000;

  &:focus {
    top: 0;
  }
}
```

### 表格无障碍

```html
<table aria-label="文章列表">
  <thead>
    <tr>
      <th scope="col">标题</th>
      <th scope="col">状态</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>文章标题</td>
      <td><span class="badge badge--success">已发布</span></td>
    </tr>
  </tbody>
</table>
```

---

## Naive UI 集成

```typescript
const themeOverrides = {
  common: {
    primaryColor: '#3b82f6',
    primaryColorHover: '#60a5fa',
    primaryColorPressed: '#2563eb',
    borderRadius: '8px',
    fontFamily: '"Noto Sans SC", sans-serif',
    fontSize: '14px',
    height: '36px'
  },
  dark: {
    cardColor: '#1a1a24',
    modalColor: '#12121a',
    popoverColor: '#1a1a24',
    tableColor: '#1a1a24',
    inputColor: '#0a0a0f'
  },
  Button: {
    heightMedium: '36px',
    paddingMedium: '0 16px'
  },
  Input: {
    heightMedium: '36px'
  },
  DataTable: {
    thColor: '#12121a',
    tdColor: '#1a1a24',
    thTextColor: '#64748b',
    tdTextColor: '#f8fafc'
  }
}
```

---

## Vue 最佳实践

### 组合式 API

```typescript
// 使用 Composition API
<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useArticleStore } from '@/stores/article'

const router = useRouter()
const route = useRoute()
const articleStore = useArticleStore()

const loading = ref(false)
const articles = computed(() => articleStore.articles)

onMounted(async () => {
  loading.value = true
  await articleStore.fetchArticles()
  loading.value = false
})
</script>
```

### Pinia Store

```typescript
// stores/article.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { Article } from '@/types'

export const useArticleStore = defineStore('article', () => {
  const articles = ref<Article[]>([])
  const loading = ref(false)
  const total = computed(() => articles.value.length)

  async function fetchArticles() {
    loading.value = true
    try {
      const res = await articleApi.getList()
      articles.value = res.data.list
    } finally {
      loading.value = false
    }
  }

  return { articles, loading, total, fetchArticles }
})
```

---

## 设计资源

| 类型 | 资源 |
|------|------|
| 字体 | [Google Fonts - Noto Sans SC](https://fonts.google.com/noto/specimen/Noto+Sans+SC) |
| 图标 | [Lucide Icons](https://lucide.dev/) / [Heroicons](https://heroicons.com/) |
| 配色 | [Tailwind Colors](https://tailwindcss.com/docs/customizing-colors) |

---

## 交付检查清单

### 视觉质量
- [ ] 不使用 emoji 作为图标
- [ ] 所有图标来自一致的图标集
- [ ] 表格行高一致
- [ ] 状态标签颜色正确

### 交互
- [ ] 所有按钮有 loading 状态
- [ ] 表单验证即时反馈
- [ ] 删除操作有确认弹窗
- [ ] 批量操作有确认

### 数据展示
- [ ] 空状态有友好提示
- [ ] 加载中显示骨架屏
- [ ] 错误状态有重试选项
- [ ] 分页组件正确工作

### 无障碍
- [ ] 表格有 aria-label
- [ ] 表单标签关联
- [ ] 键盘可完全操作
- [ ] 焦点顺序正确

### 响应式
- [ ] 侧边栏在移动端收起
- [ ] 表格在移动端水平滚动
- [ ] 表单在移动端全宽
- [ ] 按钮在移动端可点击区域足够大