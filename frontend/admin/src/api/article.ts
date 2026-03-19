import { http } from '@/utils/request'
import type { Article, ArticleRequest, ArticleQuery, PageResult, ID } from '@/types'

export const articleApi = {
  /**
   * 分页查询文章
   */
  getList(params: ArticleQuery): Promise<PageResult<Article>> {
    return http.get('/v1/articles', { params })
  },

  /**
   * 获取文章详情
   */
  getById(id: ID): Promise<Article> {
    return http.get(`/v1/articles/${id}`)
  },

  /**
   * 创建文章
   */
  create(data: ArticleRequest): Promise<ID> {
    return http.post('/v1/articles', data)
  },

  /**
   * 更新文章
   */
  update(id: ID, data: ArticleRequest): Promise<void> {
    return http.put(`/v1/articles/${id}`, data)
  },

  /**
   * 删除文章
   */
  delete(id: ID): Promise<void> {
    return http.delete(`/v1/articles/${id}`)
  },

  /**
   * 发布文章
   */
  publish(id: ID): Promise<void> {
    return http.post(`/v1/articles/${id}/publish`)
  },

  /**
   * 取消发布文章
   */
  unpublish(id: ID): Promise<void> {
    return http.post(`/v1/articles/${id}/unpublish`)
  },

  /**
   * 置顶文章
   */
  top(id: ID): Promise<void> {
    return http.post(`/v1/articles/${id}/top`)
  },

  /**
   * 取消置顶
   */
  untop(id: ID): Promise<void> {
    return http.post(`/v1/articles/${id}/untop`)
  }
}
