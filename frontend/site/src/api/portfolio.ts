import { http } from '@/utils/request'
import type { Project, ProjectTag, ProjectQuery, PageResult } from '@/types'

export const portfolioApi = {
  /**
   * 分页查询项目
   */
  getProjects(params: ProjectQuery): Promise<PageResult<Project>> {
    return http.get('/v1/projects', { params })
  },

  /**
   * 根据ID获取项目
   */
  getProjectById(id: number): Promise<Project> {
    return http.get(`/v1/projects/${id}`)
  },

  /**
   * 根据Slug获取项目
   */
  getProjectBySlug(slug: string): Promise<Project> {
    return http.get(`/v1/projects/slug/${slug}`)
  },

  /**
   * 获取项目标签列表
   */
  getProjectTags(): Promise<ProjectTag[]> {
    return http.get('/v1/project-tags')
  }
}
