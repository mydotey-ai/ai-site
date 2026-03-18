import { http } from '@/utils/request'
import type { Tag, TagRequest } from '@/types'

export const tagApi = {
  /**
   * 获取所有标签
   */
  getList(): Promise<Tag[]> {
    return http.get('/admin/v1/tags')
  },

  /**
   * 获取标签详情
   */
  getById(id: number): Promise<Tag> {
    return http.get(`/admin/v1/tags/${id}`)
  },

  /**
   * 创建标签
   */
  create(data: TagRequest): Promise<number> {
    return http.post('/admin/v1/tags', data)
  },

  /**
   * 更新标签
   */
  update(id: number, data: TagRequest): Promise<void> {
    return http.put(`/admin/v1/tags/${id}`, data)
  },

  /**
   * 删除标签
   */
  delete(id: number): Promise<void> {
    return http.delete(`/admin/v1/tags/${id}`)
  }
}
