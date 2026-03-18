import { http } from '@/utils/request'
import type { Article, ArticleQuery, PageResult, Comment, CommentRequest } from '@/types'

export const blogApi = {
  /**
   * 分页查询文章
   */
  getArticles(params: ArticleQuery): Promise<PageResult<Article>> {
    return http.get('/v1/articles', { params })
  },

  /**
   * 根据ID获取文章
   */
  getArticleById(id: number): Promise<Article> {
    return http.get(`/v1/articles/${id}`)
  },

  /**
   * 根据Slug获取文章
   */
  getArticleBySlug(slug: string): Promise<Article> {
    return http.get(`/v1/articles/slug/${slug}`)
  },

  /**
   * 获取相关文章
   */
  getRelatedArticles(id: number, limit = 5): Promise<Article[]> {
    return http.get(`/v1/articles/${id}/related`, { params: { limit } })
  },

  /**
   * 搜索文章
   */
  searchArticles(keyword: string, page = 1, size = 10): Promise<PageResult<Article>> {
    return http.get('/v1/articles/search', { params: { keyword, page, size } })
  },

  /**
   * 获取文章评论
   */
  getComments(articleId: number): Promise<Comment[]> {
    return http.get(`/v1/comments/article/${articleId}`)
  },

  /**
   * 提交评论
   */
  submitComment(data: CommentRequest): Promise<number> {
    return http.post('/v1/comments', data)
  },

  /**
   * 点赞评论
   */
  likeComment(id: number): Promise<void> {
    return http.post(`/v1/comments/${id}/like`)
  }
}
