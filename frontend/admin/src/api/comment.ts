import { http } from '@/utils/request'
import type { Comment, CommentQuery, PageResult } from '@/types'

export const commentApi = {
  /**
   * 分页查询评论
   */
  getList(params: CommentQuery): Promise<PageResult<Comment>> {
    return http.get('/v1/comments', { params })
  },

  /**
   * 获取待审核评论
   */
  getPending(page = 1, size = 10): Promise<Comment[]> {
    return http.get('/v1/comments/pending', { params: { page, size } })
  },

  /**
   * 获取待审核评论数
   */
  getPendingCount(): Promise<number> {
    return http.get('/v1/comments/pending/count')
  },

  /**
   * 获取评论详情
   */
  getById(id: string | number): Promise<Comment> {
    return http.get(`/v1/comments/${id}`)
  },

  /**
   * 审核通过
   */
  approve(id: string | number): Promise<void> {
    return http.post(`/v1/comments/${id}/approve`)
  },

  /**
   * 拒绝评论
   */
  reject(id: string | number): Promise<void> {
    return http.post(`/v1/comments/${id}/reject`)
  },

  /**
   * 批量审核通过
   */
  batchApprove(ids: (string | number)[]): Promise<void> {
    return http.post('/v1/comments/batch-approve', ids)
  },

  /**
   * 批量拒绝
   */
  batchReject(ids: (string | number)[]): Promise<void> {
    return http.post('/v1/comments/batch-reject', ids)
  },

  /**
   * 删除评论
   */
  delete(id: string | number): Promise<void> {
    return http.delete(`/v1/comments/${id}`)
  }
}
