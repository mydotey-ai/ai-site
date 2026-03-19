import { http } from '@/utils/request'
import type {
  Novel, NovelRequest, NovelQuery, NovelCategory,
  Chapter, ChapterRequest,
  Poetry, PoetryRequest, PoetryQuery, PoetryCategory,
  Essay, EssayRequest, EssayQuery, EssayCategory,
  PageResult
} from '@/types'

// ==================== 小说 API ====================
export const novelApi = {
  getList(params: NovelQuery): Promise<PageResult<Novel>> {
    return http.get('/admin/v1/novels', { params })
  },

  getById(id: number): Promise<Novel> {
    return http.get(`/admin/v1/novels/${id}`)
  },

  create(data: NovelRequest): Promise<number> {
    return http.post('/admin/v1/novels', data)
  },

  update(id: number, data: NovelRequest): Promise<void> {
    return http.put(`/admin/v1/novels/${id}`, data)
  },

  delete(id: number): Promise<void> {
    return http.delete(`/admin/v1/novels/${id}`)
  },

  publish(id: number): Promise<void> {
    return http.post(`/admin/v1/novels/${id}/publish`)
  },

  complete(id: number): Promise<void> {
    return http.post(`/admin/v1/novels/${id}/complete`)
  },

  batch(action: string, ids: number[]): Promise<void> {
    return http.post('/admin/v1/novels/batch', { action, ids })
  },

  getCategories(): Promise<NovelCategory[]> {
    return http.get('/admin/v1/novels/categories')
  },

  getChapters(novelId: number): Promise<Chapter[]> {
    return http.get(`/admin/v1/novels/${novelId}/chapters`)
  },

  getChapter(chapterId: number): Promise<Chapter> {
    return http.get(`/admin/v1/novels/chapters/${chapterId}`)
  },

  createChapter(novelId: number, data: ChapterRequest): Promise<number> {
    return http.post(`/admin/v1/novels/${novelId}/chapters`, data)
  },

  updateChapter(chapterId: number, data: ChapterRequest): Promise<void> {
    return http.put(`/admin/v1/novels/chapters/${chapterId}`, data)
  },

  deleteChapter(chapterId: number): Promise<void> {
    return http.delete(`/admin/v1/novels/chapters/${chapterId}`)
  },

  publishChapter(chapterId: number): Promise<void> {
    return http.post(`/admin/v1/novels/chapters/${chapterId}/publish`)
  },

  batchChapters(action: string, ids: number[]): Promise<void> {
    return http.post('/admin/v1/novels/chapters/batch', { action, ids })
  }
}

// ==================== 诗歌 API ====================
export const poetryApi = {
  getList(params: PoetryQuery): Promise<PageResult<Poetry>> {
    return http.get('/admin/v1/poetry', { params })
  },

  getById(id: number): Promise<Poetry> {
    return http.get(`/admin/v1/poetry/${id}`)
  },

  create(data: PoetryRequest): Promise<number> {
    return http.post('/admin/v1/poetry', data)
  },

  update(id: number, data: PoetryRequest): Promise<void> {
    return http.put(`/admin/v1/poetry/${id}`, data)
  },

  delete(id: number): Promise<void> {
    return http.delete(`/admin/v1/poetry/${id}`)
  },

  publish(id: number): Promise<void> {
    return http.post(`/admin/v1/poetry/${id}/publish`)
  },

  batch(action: string, ids: number[]): Promise<void> {
    return http.post('/admin/v1/poetry/batch', { action, ids })
  },

  getCategories(): Promise<PoetryCategory[]> {
    return http.get('/admin/v1/poetry/categories')
  }
}

// ==================== 散文 API ====================
export const essayApi = {
  getList(params: EssayQuery): Promise<PageResult<Essay>> {
    return http.get('/admin/v1/essays', { params })
  },

  getById(id: number): Promise<Essay> {
    return http.get(`/admin/v1/essays/${id}`)
  },

  create(data: EssayRequest): Promise<number> {
    return http.post('/admin/v1/essays', data)
  },

  update(id: number, data: EssayRequest): Promise<void> {
    return http.put(`/admin/v1/essays/${id}`, data)
  },

  delete(id: number): Promise<void> {
    return http.delete(`/admin/v1/essays/${id}`)
  },

  publish(id: number): Promise<void> {
    return http.post(`/admin/v1/essays/${id}/publish`)
  },

  batch(action: string, ids: number[]): Promise<void> {
    return http.post('/admin/v1/essays/batch', { action, ids })
  },

  getCategories(): Promise<EssayCategory[]> {
    return http.get('/admin/v1/essays/categories')
  }
}
