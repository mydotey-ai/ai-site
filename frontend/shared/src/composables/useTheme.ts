import { ref, onMounted, watch } from 'vue'

export type Theme = 'light' | 'dark'

const THEME_KEY = 'theme'

// 全局主题状态
const theme = ref<Theme>('dark')

/**
 * 主题切换 Composable
 *
 * 支持深色/浅色主题切换，默认深色主题
 * - 自动检测系统偏好
 * - 本地存储持久化
 * - 响应式更新
 *
 * @example
 * ```ts
 * const { theme, toggleTheme, setTheme } = useTheme()
 * ```
 */
export function useTheme() {
  let initialized = false

  /**
   * 应用主题到 DOM
   */
  function applyTheme(value: Theme) {
    document.documentElement.setAttribute('data-theme', value)
    localStorage.setItem(THEME_KEY, value)

    // 更新 meta theme-color
    const metaThemeColor = document.querySelector('meta[name="theme-color"]')
    if (metaThemeColor) {
      metaThemeColor.setAttribute('content', value === 'dark' ? '#0a0a0f' : '#ffffff')
    }
  }

  /**
   * 设置主题
   */
  function setTheme(value: Theme) {
    theme.value = value
    applyTheme(value)
  }

  /**
   * 切换主题
   */
  function toggleTheme() {
    setTheme(theme.value === 'dark' ? 'light' : 'dark')
  }

  /**
   * 初始化主题
   */
  function initTheme() {
    if (initialized) return
    initialized = true

    // 优先级：本地存储 > 系统偏好 > 默认深色
    const saved = localStorage.getItem(THEME_KEY) as Theme | null

    if (saved && (saved === 'light' || saved === 'dark')) {
      theme.value = saved
    } else {
      // 检测系统偏好
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
      theme.value = prefersDark ? 'dark' : 'light'
    }

    applyTheme(theme.value)

    // 监听系统主题变化
    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
    const handleChange = (e: MediaQueryListEvent) => {
      // 只有在用户没有手动设置时才跟随系统
      if (!localStorage.getItem(THEME_KEY)) {
        setTheme(e.matches ? 'dark' : 'light')
      }
    }

    mediaQuery.addEventListener('change', handleChange)
  }

  // 组件挂载时初始化
  onMounted(() => {
    initTheme()
  })

  // 监听主题变化
  watch(theme, (value: Theme) => {
    applyTheme(value)
  })

  return {
    /** 当前主题 */
    theme,
    /** 设置主题 */
    setTheme,
    /** 切换主题 */
    toggleTheme,
    /** 是否为深色主题 */
    isDark: () => theme.value === 'dark',
    /** 是否为浅色主题 */
    isLight: () => theme.value === 'light'
  }
}

// 导出单例获取函数（用于非组件上下文）
export function getTheme(): Theme {
  return theme.value
}