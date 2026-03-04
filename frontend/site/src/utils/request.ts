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

    if (data.code === 200) {
      return response
    }

    const error = new Error(data.message || '请求失败')
    ;(error as unknown as Record<string, unknown>).code = data.code
    return Promise.reject(error)
  },
  (error) => {
    if (error.response) {
      const { status } = error.response
      switch (status) {
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
  }
}

export default request