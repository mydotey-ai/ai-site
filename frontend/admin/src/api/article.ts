import { http } from '@/utils/request'
import type { Article, ArticleRequest, ArticleQuery, PageResult } from '@/types'

export const articleApi = {
  /**
   * 分页查询文章
   */
  getList(params: ArticleQuery): Promise<PageResult<Article>> {
    return http.get('/admin/v1/articles', { params })
  },

  /**
   * 获取文章详情
   */
  getById(id: number): Promise<Article> {
    return http.get(`/admin/v1/articles/${id}`)
  },

  /**
   * 创建文章
   */
  create(data: ArticleRequest): Promise<number> {
    return http.post('/admin/v1/articles', data)
  },

  /**
   * 更新文章
   */
  update(id: number, data: ArticleRequest): Promise<void> {
    return http.put(`/admin/v1/articles/${id}`, data)
  },

  /**
   * 删除文章
   */
  delete(id: number): Promise<void> {
    return http.delete(`/admin/v1/articles/${id}`)
  },

  /**
   * 发布文章
   */
  publish(id: number): Promise<void> {
    return http.post(`/admin/v1/articles/${id}/publish`)
  },

  /**
   * 取消发布文章
   */
  unpublish(id: number): Promise<void> {
    return http.post(`/admin/v1/articles/${id}/unpublish`)
  },

  /**
   * 置顶文章
   */
  top(id: number): Promise<void> {
    return http.post(`/admin/v1/articles/${id}/top`)
  },

  /**
   * 取消置顶
   */
  untop(id: number): Promise<void> {
    return http.post(`/admin/v1/articles/${id}/untop`)
  }
}
