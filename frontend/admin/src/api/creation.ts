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
    return http.get('/v1/novels', { params })
  },

  getById(id: string | number): Promise<Novel> {
    return http.get(`/v1/novels/${id}`)
  },

  create(data: NovelRequest): Promise<string | number> {
    return http.post('/v1/novels', data)
  },

  update(id: string | number, data: NovelRequest): Promise<void> {
    return http.put(`/v1/novels/${id}`, data)
  },

  delete(id: string | number): Promise<void> {
    return http.delete(`/v1/novels/${id}`)
  },

  publish(id: string | number): Promise<void> {
    return http.post(`/v1/novels/${id}/publish`)
  },

  complete(id: string | number): Promise<void> {
    return http.post(`/v1/novels/${id}/complete`)
  },

  batch(action: string, ids: (string | number)[]): Promise<void> {
    return http.post('/v1/novels/batch', { action, ids })
  },

  getCategories(): Promise<NovelCategory[]> {
    return http.get('/v1/novels/categories')
  },

  getChapters(novelId: string | number): Promise<Chapter[]> {
    return http.get(`/v1/novels/${novelId}/chapters`)
  },

  getChapter(chapterId: string | number): Promise<Chapter> {
    return http.get(`/v1/novels/chapters/${chapterId}`)
  },

  createChapter(novelId: string | number, data: ChapterRequest): Promise<string | number> {
    return http.post(`/v1/novels/${novelId}/chapters`, data)
  },

  updateChapter(chapterId: string | number, data: ChapterRequest): Promise<void> {
    return http.put(`/v1/novels/chapters/${chapterId}`, data)
  },

  deleteChapter(chapterId: string | number): Promise<void> {
    return http.delete(`/v1/novels/chapters/${chapterId}`)
  },

  publishChapter(chapterId: string | number): Promise<void> {
    return http.post(`/v1/novels/chapters/${chapterId}/publish`)
  },

  batchChapters(action: string, ids: (string | number)[]): Promise<void> {
    return http.post('/v1/novels/chapters/batch', { action, ids })
  }
}

// ==================== 诗歌 API ====================
export const poetryApi = {
  getList(params: PoetryQuery): Promise<PageResult<Poetry>> {
    return http.get('/v1/poetry', { params })
  },

  getById(id: string | number): Promise<Poetry> {
    return http.get(`/v1/poetry/${id}`)
  },

  create(data: PoetryRequest): Promise<string | number> {
    return http.post('/v1/poetry', data)
  },

  update(id: string | number, data: PoetryRequest): Promise<void> {
    return http.put(`/v1/poetry/${id}`, data)
  },

  delete(id: string | number): Promise<void> {
    return http.delete(`/v1/poetry/${id}`)
  },

  publish(id: string | number): Promise<void> {
    return http.post(`/v1/poetry/${id}/publish`)
  },

  batch(action: string, ids: (string | number)[]): Promise<void> {
    return http.post('/v1/poetry/batch', { action, ids })
  },

  getCategories(): Promise<PoetryCategory[]> {
    return http.get('/v1/poetry/categories')
  }
}

// ==================== 散文 API ====================
export const essayApi = {
  getList(params: EssayQuery): Promise<PageResult<Essay>> {
    return http.get('/v1/essays', { params })
  },

  getById(id: string | number): Promise<Essay> {
    return http.get(`/v1/essays/${id}`)
  },

  create(data: EssayRequest): Promise<string | number> {
    return http.post('/v1/essays', data)
  },

  update(id: string | number, data: EssayRequest): Promise<void> {
    return http.put(`/v1/essays/${id}`, data)
  },

  delete(id: string | number): Promise<void> {
    return http.delete(`/v1/essays/${id}`)
  },

  publish(id: string | number): Promise<void> {
    return http.post(`/v1/essays/${id}/publish`)
  },

  batch(action: string, ids: (string | number)[]): Promise<void> {
    return http.post('/v1/essays/batch', { action, ids })
  },

  getCategories(): Promise<EssayCategory[]> {
    return http.get('/v1/essays/categories')
  }
}
