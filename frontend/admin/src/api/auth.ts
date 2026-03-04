import { http } from '@/utils/request'
import type { LoginRequest, LoginResponse, User } from '@/types'

export const authApi = {
  /**
   * 用户登录
   */
  login(data: LoginRequest): Promise<LoginResponse> {
    return http.post('/v1/auth/login', data)
  },

  /**
   * 用户注册
   */
  register(data: RegisterRequest): Promise<{ userId: number }> {
    return http.post('/v1/auth/register', data)
  },

  /**
   * 获取当前用户信息
   */
  getCurrentUser(): Promise<User> {
    return http.get('/v1/auth/me')
  },

  /**
   * 用户登出
   */
  logout(): Promise<void> {
    return http.post('/v1/auth/logout')
  }
}

interface RegisterRequest {
  username: string
  password: string
  email: string
  nickname?: string
}