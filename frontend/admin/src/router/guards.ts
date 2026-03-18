import type { Router } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

export function setupRouterGuards(router: Router) {
  router.beforeEach(async (to, _from, next) => {
    const authStore = useAuthStore()

    // 设置页面标题
    const title = to.meta.title as string
    if (title) {
      document.title = `${title} - AI-Site Admin`
    }

    // 不需要认证的页面
    if (to.meta.requiresAuth === false) {
      next()
      return
    }

    // 需要认证但未登录
    if (!authStore.isLoggedIn) {
      next({
        name: 'Login',
        query: { redirect: to.fullPath }
      })
      return
    }

    // 已登录但没有用户信息，尝试获取
    if (!authStore.user) {
      try {
        await authStore.fetchUser()
      } catch {
        authStore.logout()
        next({
          name: 'Login',
          query: { redirect: to.fullPath }
        })
        return
      }
    }

    next()
  })

  router.afterEach(() => {
    // 页面切换后滚动到顶部
    window.scrollTo(0, 0)
  })
}