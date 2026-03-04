import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User, LoginRequest, LoginResponse } from '@/types'
import { http } from '@/utils/request'

export const useAuthStore = defineStore('auth', () => {
  // 状态
  const token = ref<string | null>(localStorage.getItem('token'))
  const refreshToken = ref<string | null>(localStorage.getItem('refreshToken'))
  const user = ref<User | null>(null)

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const username = computed(() => user.value?.username || '')
  const nickname = computed(() => user.value?.nickname || user.value?.username || '')

  // 登录
  async function login(credentials: LoginRequest): Promise<void> {
    const response: LoginResponse = await http.post('/v1/auth/login', credentials)

    token.value = response.token
    refreshToken.value = response.refreshToken
    user.value = response.user

    // 持久化存储
    localStorage.setItem('token', response.token)
    localStorage.setItem('refreshToken', response.refreshToken)
  }

  // 登出
  function logout(): void {
    token.value = null
    refreshToken.value = null
    user.value = null

    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken')
  }

  // 刷新 token
  async function refresh(): Promise<void> {
    if (!refreshToken.value) {
      throw new Error('No refresh token')
    }

    const response: LoginResponse = await http.post('/v1/auth/refresh', {
      refreshToken: refreshToken.value
    })

    token.value = response.token
    refreshToken.value = response.refreshToken

    localStorage.setItem('token', response.token)
    localStorage.setItem('refreshToken', response.refreshToken)
  }

  // 获取当前用户信息
  async function fetchUser(): Promise<void> {
    const response: User = await http.get('/v1/auth/me')
    user.value = response
  }

  // 初始化 - 如果有 token 则获取用户信息
  async function init(): Promise<void> {
    if (token.value) {
      try {
        await fetchUser()
      } catch {
        // token 无效，清除
        logout()
      }
    }
  }

  return {
    // 状态
    token,
    refreshToken,
    user,
    // 计算属性
    isLoggedIn,
    username,
    nickname,
    // 方法
    login,
    logout,
    refresh,
    fetchUser,
    init
  }
})