# Site 项目设计系统

> 面向用户的个人网站前端设计系统

---

## 设计理念

**关键词：大方、时尚、科技感**

| 关键词 | 设计体现 |
|--------|---------|
| **大方** | 宽松留白、清晰层级、宽敞舒适的阅读空间 |
| **时尚** | 现代配色、渐变点缀、圆润边角、简洁线条 |
| **科技感** | 深色主题、霓虹点缀、微妙光效、代码高亮 |

---

## 项目定位

| 属性 | 描述 |
|------|------|
| **类型** | 个人网站 (Portfolio + Blog + Creation + Media) |
| **受众** | 访客、HR、技术面试官 |
| **模式** | Portfolio Grid Pattern |
| **核心** | 内容优先，视觉吸引，快速加载 |

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
| CTA | `#f97316` | 行动召唤按钮 |

#### 深色主题变量

```scss
:root[data-theme="dark"] {
  // 背景
  --bg-primary: #0a0a0f;      // 主背景
  --bg-secondary: #12121a;    // 次级背景 (导航栏)
  --bg-tertiary: #1a1a24;     // 卡片背景
  --bg-hover: #22222e;        // 悬停背景

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
  --glow-accent: 0 0 30px rgba(6, 182, 212, 0.2);
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
| xs | 12px | 辅助文字、标签 |
| sm | 14px | 次要内容、描述 |
| base | 16px | 正文 |
| lg | 18px | 重要正文 |
| xl | 20px | 小标题 |
| 2xl | 24px | 标题 |
| 3xl | 30px | 大标题 |
| 4xl | 36px | 页面标题 |
| 5xl | 48px | Hero 标题 |

### 行高

```scss
$line-height-tight: 1.25;   // 标题
$line-height-normal: 1.5;   // 正文
$line-height-relaxed: 1.75; // 长文阅读
```

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
| spacing-12 | 48px | 章节间距 |
| spacing-16 | 64px | 大章节间距 |

---

## 布局系统

### 全宽布局

```scss
.container {
  width: 100%;
  max-width: 1440px;
  margin: 0 auto;
  padding: 0 24px;

  @media (max-width: 768px) {
    padding: 0 16px;
  }
}
```

### 页面结构

```
┌─────────────────────────────────────────────────────────┐
│  Header: 顶部导航 (64px 高度)                           │
├─────────────────────────────────────────────────────────┤
│  Hero: 页面标题区域                                     │
├─────────────────────────────────────────────────────────┤
│  Content: 内容区域                                      │
├─────────────────────────────────────────────────────────┤
│  Footer: 页脚                                           │
└─────────────────────────────────────────────────────────┘
```

### 响应式断点

| 名称 | 宽度 | 设备 |
|------|------|------|
| sm | 640px | 小屏手机 |
| md | 768px | 平板竖屏 |
| lg | 1024px | 平板横屏 |
| xl | 1280px | 桌面 |
| 2xl | 1536px | 大屏桌面 |

### 网格系统

```scss
.grid {
  display: grid;
  gap: 24px;

  // 响应式网格
  &--responsive {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }
}
```

---

## 导航设计

### 顶部导航

```
┌─────────────────────────────────────────────────────────┐
│  [Logo]   首页  博客  作品集  创作  关于    🔍  🌙  登录 │
└─────────────────────────────────────────────────────────┘
```

```scss
.navbar {
  position: sticky;
  top: 0;
  z-index: 100;
  height: 64px;
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border-primary);
  backdrop-filter: blur(10px);

  &__container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    max-width: 1440px;
    margin: 0 auto;
    padding: 0 24px;
  }

  &__logo {
    font-size: 20px;
    font-weight: 700;
    color: var(--text-primary);
    text-decoration: none;

    &:hover {
      text-shadow: var(--glow-primary);
    }
  }

  &__menu {
    display: flex;
    gap: 32px;
  }

  &__link {
    color: var(--text-secondary);
    text-decoration: none;
    font-weight: 500;
    transition: color 0.2s;

    &:hover,
    &--active {
      color: var(--color-primary);
    }
  }
}
```

---

## 卡片设计

### 扁平无框风格

```scss
.card {
  background: var(--bg-tertiary);
  border-radius: 12px;
  padding: 24px;
  transition: transform 0.2s, background 0.2s;
  cursor: pointer;

  &:hover {
    background: var(--bg-hover);
    transform: translateY(-2px);
  }
}
```

### 文章卡片

```scss
.article-card {
  @extend .card;
  display: flex;
  flex-direction: column;
  gap: 16px;

  &__title {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);

    a:hover {
      color: var(--color-primary);
    }
  }

  &__summary {
    color: var(--text-secondary);
    line-height: 1.75;
  }

  &__meta {
    display: flex;
    gap: 16px;
    font-size: 14px;
    color: var(--text-muted);
  }

  &__tag {
    display: inline-flex;
    padding: 4px 8px;
    background: var(--bg-hover);
    border-radius: 4px;
    font-size: 12px;
    color: var(--text-secondary);
  }
}
```

### 项目卡片

```scss
.project-card {
  @extend .card;
  padding: 0;
  overflow: hidden;

  &__cover {
    aspect-ratio: 16 / 9;
    width: 100%;
    object-fit: cover;
  }

  &__content {
    padding: 20px;
  }

  &__title {
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 8px;
  }

  &__description {
    color: var(--text-secondary);
    font-size: 14px;
    margin-bottom: 16px;
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
  padding: 12px 20px;
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
      box-shadow: var(--glow-primary);
    }
  }

  // 次要按钮
  &--secondary {
    background: var(--bg-tertiary);
    color: var(--text-primary);
    border: 1px solid var(--border-primary);

    &:hover {
      background: var(--bg-hover);
      border-color: var(--color-primary);
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

### 常用动画

```scss
// 淡入
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

// 上滑进入
@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 科技感脉冲
@keyframes pulse-glow {
  0%, 100% {
    box-shadow: 0 0 0 0 rgba(59, 130, 246, 0.4);
  }
  50% {
    box-shadow: 0 0 20px 5px rgba(59, 130, 246, 0.2);
  }
}
```

### 悬停效果

```scss
.hover-lift {
  transition: transform 0.2s;

  &:hover {
    transform: translateY(-4px);
  }
}

.hover-underline {
  position: relative;

  &::after {
    content: '';
    position: absolute;
    bottom: -2px;
    left: 0;
    width: 0;
    height: 2px;
    background: var(--color-primary);
    transition: width 0.2s;
  }

  &:hover::after {
    width: 100%;
  }
}
```

---

## 页面模板

### 首页结构

```
┌─────────────────────────────────────────────────────────┐
│  导航栏                                                  │
├─────────────────────────────────────────────────────────┤
│  Hero: 欢迎语 + 简短介绍                                │
├─────────────────────────────────────────────────────────┤
│  最新博客 (3篇文章卡片)                                 │
├─────────────────────────────────────────────────────────┤
│  精选作品 (3个项目卡片)                                 │
├─────────────────────────────────────────────────────────┤
│  最新创作 (3个创作卡片)                                 │
├─────────────────────────────────────────────────────────┤
│  Footer                                                  │
└─────────────────────────────────────────────────────────┘
```

### 博客列表页

```
┌─────────────────────────────────────────────────────────┐
│  导航栏                                                  │
├─────────────────────────────────────────────────────────┤
│  页面标题: 博客                                          │
├─────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌─────────────────────────────────┐ │
│  │ 分类导航     │  │ 搜索框                          │ │
│  │ 标签云       │  │ 文章列表                        │ │
│  │ 归档         │  │ ...                             │ │
│  └──────────────┘  └─────────────────────────────────┘ │
├─────────────────────────────────────────────────────────┤
│  Footer                                                  │
└─────────────────────────────────────────────────────────┘
```

---

## 响应式设计

### 布局调整

| 断点 | 布局 |
|------|------|
| < 640px | 单列，导航折叠，卡片全宽 |
| 640-768px | 两列网格 |
| 768-1024px | 三列网格 |
| 1024-1280px | 四列网格 |
| > 1280px | 全宽，最大 1440px |

### 移动端导航

```scss
@media (max-width: 768px) {
  .navbar {
    &__menu {
      display: none;
    }

    &__menu--open {
      display: flex;
      flex-direction: column;
      position: absolute;
      top: 64px;
      left: 0;
      right: 0;
      background: var(--bg-secondary);
      padding: 16px;
    }

    &__hamburger {
      display: flex;
      flex-direction: column;
      gap: 4px;

      span {
        width: 24px;
        height: 2px;
        background: var(--text-primary);
      }
    }
  }
}
```

---

## 代码高亮

```scss
pre {
  background: var(--bg-tertiary);
  border: 1px solid var(--border-primary);
  border-radius: 8px;
  padding: 16px;
  overflow-x: auto;
  font-family: $font-family-mono;
  font-size: 14px;
  line-height: 1.5;
}

code {
  font-family: $font-family-mono;
}
```

---

## 无障碍设计

### 焦点样式

```scss
:focus-visible {
  outline: 2px solid var(--color-primary);
  outline-offset: 2px;
}
```

### 跳过导航

```html
<a href="#main-content" class="skip-link">跳到主要内容</a>
```

```scss
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

---

## 主题切换

```typescript
// composables/useTheme.ts
export function useTheme() {
  const theme = ref<'light' | 'dark'>('dark')

  onMounted(() => {
    const saved = localStorage.getItem('theme')
    if (saved) {
      theme.value = saved as 'light' | 'dark'
    } else {
      theme.value = window.matchMedia('(prefers-color-scheme: dark)').matches
        ? 'dark'
        : 'light'
    }
    applyTheme(theme.value)
  })

  function applyTheme(value: 'light' | 'dark') {
    document.documentElement.setAttribute('data-theme', value)
    localStorage.setItem('theme', value)
  }

  function toggleTheme() {
    theme.value = theme.value === 'dark' ? 'light' : 'dark'
    applyTheme(theme.value)
  }

  return { theme, toggleTheme }
}
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
    fontFamily: '"Noto Sans SC", sans-serif'
  },
  dark: {
    cardColor: '#1a1a24',
    modalColor: '#12121a',
    popoverColor: '#1a1a24'
  }
}
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
- [ ] 不使用 emoji 作为图标（使用 SVG）
- [ ] 所有图标来自一致的图标集
- [ ] 悬停状态不导致布局偏移
- [ ] 使用主题色变量

### 交互
- [ ] 所有可点击元素有 cursor-pointer
- [ ] 悬停状态提供清晰的视觉反馈
- [ ] 过渡平滑 (150-300ms)
- [ ] 键盘导航焦点状态可见

### 深色/浅色模式
- [ ] 浅色模式文字对比度足够 (4.5:1 最小)
- [ ] 玻璃/透明元素在浅色模式可见
- [ ] 边框在两种模式都可见
- [ ] 交付前测试两种模式

### 布局
- [ ] 浮动元素与边缘有适当间距
- [ ] 内容不被固定导航栏遮挡
- [ ] 响应式测试: 375px, 768px, 1024px, 1440px
- [ ] 移动端无水平滚动

### 无障碍
- [ ] 所有图片有 alt 文本
- [ ] 表单输入有标签
- [ ] 颜色不是唯一的指示器
- [ ] 尊重 prefers-reduced-motion