import { http } from '@/utils/request'
import type { Project, ProjectRequest, ProjectQuery, ProjectTag, ProjectTagRequest, PageResult } from '@/types'

export const projectApi = {
  /**
   * 分页查询项目
   */
  getList(params: ProjectQuery): Promise<PageResult<Project>> {
    return http.get('/admin/v1/projects', { params })
  },

  /**
   * 获取项目详情
   */
  getById(id: number): Promise<Project> {
    return http.get(`/admin/v1/projects/${id}`)
  },

  /**
   * 创建项目
   */
  create(data: ProjectRequest): Promise<number> {
    return http.post('/admin/v1/projects', data)
  },

  /**
   * 更新项目
   */
  update(id: number, data: ProjectRequest): Promise<void> {
    return http.put(`/admin/v1/projects/${id}`, data)
  },

  /**
   * 删除项目
   */
  delete(id: number): Promise<void> {
    return http.delete(`/admin/v1/projects/${id}`)
  },

  /**
   * 发布项目
   */
  release(id: number): Promise<void> {
    return http.post(`/admin/v1/projects/${id}/release`)
  },

  /**
   * 归档项目
   */
  archive(id: number): Promise<void> {
    return http.post(`/admin/v1/projects/${id}/archive`)
  },

  /**
   * 批量操作
   */
  batch(action: string, ids: number[]): Promise<void> {
    return http.post('/admin/v1/projects/batch', { action, ids })
  }
}

export const projectTagApi = {
  /**
   * 获取标签列表
   */
  getList(): Promise<ProjectTag[]> {
    return http.get('/api/v1/project-tags')
  },

  /**
   * 创建标签
   */
  create(data: ProjectTagRequest): Promise<number> {
    return http.post('/admin/v1/project-tags', data)
  },

  /**
   * 更新标签
   */
  update(id: number, data: ProjectTagRequest): Promise<void> {
    return http.put(`/admin/v1/project-tags/${id}`, data)
  },

  /**
   * 删除标签
   */
  delete(id: number): Promise<void> {
    return http.delete(`/admin/v1/project-tags/${id}`)
  }
}
