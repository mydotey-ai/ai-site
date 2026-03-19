import { http } from '@/utils/request'
import type { Category, CategoryRequest } from '@/types'

export const categoryApi = {
  /**
   * 获取所有分类
   */
  getList(): Promise<Category[]> {
    return http.get('/v1/categories')
  },

  /**
   * 获取分类树
   */
  getTree(): Promise<Category[]> {
    return http.get('/v1/categories/tree')
  },

  /**
   * 获取分类详情
   */
  getById(id: string | number): Promise<Category> {
    return http.get(`/v1/categories/${id}`)
  },

  /**
   * 创建分类
   */
  create(data: CategoryRequest): Promise<string | number> {
    return http.post('/v1/categories', data)
  },

  /**
   * 更新分类
   */
  update(id: string | number, data: CategoryRequest): Promise<void> {
    return http.put(`/v1/categories/${id}`, data)
  },

  /**
   * 删除分类
   */
  delete(id: string | number): Promise<void> {
    return http.delete(`/v1/categories/${id}`)
  }
}
