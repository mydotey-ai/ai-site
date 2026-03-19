import { http } from '@/utils/request'
import type { Tag, TagRequest } from '@/types'

export const tagApi = {
  /**
   * 获取所有标签
   */
  getList(): Promise<Tag[]> {
    return http.get('/v1/tags')
  },

  /**
   * 获取标签详情
   */
  getById(id: string | number): Promise<Tag> {
    return http.get(`/v1/tags/${id}`)
  },

  /**
   * 创建标签
   */
  create(data: TagRequest): Promise<string | number> {
    return http.post('/v1/tags', data)
  },

  /**
   * 更新标签
   */
  update(id: string | number, data: TagRequest): Promise<void> {
    return http.put(`/v1/tags/${id}`, data)
  },

  /**
   * 删除标签
   */
  delete(id: string | number): Promise<void> {
    return http.delete(`/v1/tags/${id}`)
  }
}
