# 前端设计系统规则

## 项目设计系统

前端包含两个子项目，开发时请参考对应的设计系统文档：

| 项目 | 文档 | 说明 |
|------|------|------|
| **Site** | [site-design-system.md](../../docs/design/site-design-system.md) | 面向用户的个人网站 |
| **Admin** | [admin-design-system.md](../../docs/design/admin-design-system.md) | 面向管理的后台系统 |

---

## 核心设计决策

| 维度 | 选择 |
|------|------|
| **风格** | 大方、时尚、科技感 |
| **主题** | 双主题切换（深色默认） |
| **配色** | 蓝色系 (#3b82f6) |
| **字体** | 思源黑体 |
| **卡片** | 扁平无框，悬停浮起 |
| **动画** | 适度动画 |
| **图标** | Lucide Icons / Heroicons |

---

## 必须遵守的规则

### 视觉规范
- **不使用 emoji 作为图标** - 使用 SVG 图标
- **悬停状态不导致布局偏移** - 使用 transform/opacity
- **文字对比度 4.5:1 最小** - 符合 WCAG AA

### 交互规范
- **所有可点击元素添加 cursor-pointer**
- **过渡时间 150-300ms** - 平滑但不拖沓
- **焦点状态可见** - 键盘导航友好

### 响应式规范
- **移动端完整适配** - 375px 测试
- **侧边栏移动端收起** - Admin 项目
- **导航移动端折叠** - Site 项目

---

## 设计资源

| 类型 | 资源 |
|------|------|
| 字体 | [Noto Sans SC](https://fonts.google.com/noto/specimen/Noto+Sans+SC) |
| 图标 | [Lucide Icons](https://lucide.dev/) / [Heroicons](https://heroicons.com/) |
| 配色 | [Tailwind Colors](https://tailwindcss.com/docs/customizing-colors) |