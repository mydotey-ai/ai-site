import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse } from 'axios'
import type { Result } from '@/types'

// 创建 axios 实例
const request: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 从 localStorage 获取 token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse<Result<unknown>>) => {
    const { data } = response

    // 检查业务状态码
    if (data.code === 200) {
      return response
    }

    // 处理业务错误
    const error = new Error(data.message || '请求失败')
    ;(error as unknown as Record<string, unknown>).code = data.code
    return Promise.reject(error)
  },
  (error) => {
    // 处理 HTTP 错误
    if (error.response) {
      const { status } = error.response

      switch (status) {
        case 401:
          // 未授权，清除 token 并跳转登录
          localStorage.removeItem('token')
          window.location.href = '/login'
          break
        case 403:
          error.message = '没有权限访问'
          break
        case 404:
          error.message = '请求的资源不存在'
          break
        case 500:
          error.message = '服务器内部错误'
          break
        default:
          error.message = `请求失败: ${status}`
      }
    } else if (error.request) {
      error.message = '网络错误，请检查网络连接'
    }

    console.error('Response error:', error)
    return Promise.reject(error)
  }
)

// 封装请求方法
export const http = {
  get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return request.get(url, config).then((res) => res.data.data)
  },

  post<T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    return request.post(url, data, config).then((res) => res.data.data)
  },

  put<T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    return request.put(url, data, config).then((res) => res.data.data)
  },

  delete<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return request.delete(url, config).then((res) => res.data.data)
  }
}

export default request