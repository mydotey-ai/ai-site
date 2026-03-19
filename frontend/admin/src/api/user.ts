import { http } from '@/utils/request'
import type { User, UserRequest, UserQuery, PageResult, ID } from '@/types'

export const userApi = {
  /**
   * 分页查询用户
   */
  getList(params: UserQuery): Promise<PageResult<User>> {
    return http.get('/v1/users', { params })
  },

  /**
   * 获取用户详情
   */
  getById(id: ID): Promise<User> {
    return http.get(`/v1/users/${id}`)
  },

  /**
   * 获取所有用户
   */
  getAll(): Promise<User[]> {
    return http.get('/v1/users/all')
  },

  /**
   * 创建用户
   */
  create(data: UserRequest): Promise<ID> {
    return http.post('/v1/users', data)
  },

  /**
   * 更新用户
   */
  update(id: ID, data: UserRequest): Promise<void> {
    return http.put(`/v1/users/${id}`, data)
  },

  /**
   * 删除用户
   */
  delete(id: ID): Promise<void> {
    return http.delete(`/v1/users/${id}`)
  },

  /**
   * 更新用户状态
   */
  updateStatus(id: ID, status: number): Promise<void> {
    return http.put(`/v1/users/${id}/status`, null, { params: { status } })
  }
}
