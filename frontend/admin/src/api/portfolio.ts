import { http } from '@/utils/request'
import type { Project, ProjectRequest, ProjectQuery, ProjectTag, ProjectTagRequest, PageResult, ID, IDs } from '@/types'

export const projectApi = {
  /**
   * 分页查询项目
   */
  getList(params: ProjectQuery): Promise<PageResult<Project>> {
    return http.get('/v1/projects', { params })
  },

  /**
   * 获取项目详情
   */
  getById(id: ID): Promise<Project> {
    return http.get(`/v1/projects/${id}`)
  },

  /**
   * 创建项目
   */
  create(data: ProjectRequest): Promise<ID> {
    return http.post('/v1/projects', data)
  },

  /**
   * 更新项目
   */
  update(id: ID, data: ProjectRequest): Promise<void> {
    return http.put(`/v1/projects/${id}`, data)
  },

  /**
   * 删除项目
   */
  delete(id: ID): Promise<void> {
    return http.delete(`/v1/projects/${id}`)
  },

  /**
   * 发布项目
   */
  release(id: ID): Promise<void> {
    return http.post(`/v1/projects/${id}/release`)
  },

  /**
   * 归档项目
   */
  archive(id: ID): Promise<void> {
    return http.post(`/v1/projects/${id}/archive`)
  },

  /**
   * 批量操作
   */
  batch(action: string, ids: IDs): Promise<void> {
    return http.post('/v1/projects/batch', { action, ids })
  }
}

export const projectTagApi = {
  /**
   * 获取标签列表
   */
  getList(): Promise<ProjectTag[]> {
    return http.get('/v1/project-tags')
  },

  /**
   * 创建标签
   */
  create(data: ProjectTagRequest): Promise<ID> {
    return http.post('/v1/project-tags', data)
  },

  /**
   * 更新标签
   */
  update(id: ID, data: ProjectTagRequest): Promise<void> {
    return http.put(`/v1/project-tags/${id}`, data)
  },

  /**
   * 删除标签
   */
  delete(id: ID): Promise<void> {
    return http.delete(`/v1/project-tags/${id}`)
  }
}
