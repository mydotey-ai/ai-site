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

  getById(id: bigint): Promise<Novel> {
    return http.get(`/v1/novels/${id}`)
  },

  create(data: NovelRequest): Promise<bigint> {
    return http.post('/v1/novels', data)
  },

  update(id: bigint, data: NovelRequest): Promise<void> {
    return http.put(`/v1/novels/${id}`, data)
  },

  delete(id: bigint): Promise<void> {
    return http.delete(`/v1/novels/${id}`)
  },

  publish(id: bigint): Promise<void> {
    return http.post(`/v1/novels/${id}/publish`)
  },

  complete(id: bigint): Promise<void> {
    return http.post(`/v1/novels/${id}/complete`)
  },

  batch(action: string, ids: bigint[]): Promise<void> {
    return http.post('/v1/novels/batch', { action, ids })
  },

  getCategories(): Promise<NovelCategory[]> {
    return http.get('/v1/novels/categories')
  },

  getChapters(novelId: bigint): Promise<Chapter[]> {
    return http.get(`/v1/novels/${novelId}/chapters`)
  },

  getChapter(chapterId: bigint): Promise<Chapter> {
    return http.get(`/v1/novels/chapters/${chapterId}`)
  },

  createChapter(novelId: bigint, data: ChapterRequest): Promise<bigint> {
    return http.post(`/v1/novels/${novelId}/chapters`, data)
  },

  updateChapter(chapterId: bigint, data: ChapterRequest): Promise<void> {
    return http.put(`/v1/novels/chapters/${chapterId}`, data)
  },

  deleteChapter(chapterId: bigint): Promise<void> {
    return http.delete(`/v1/novels/chapters/${chapterId}`)
  },

  publishChapter(chapterId: bigint): Promise<void> {
    return http.post(`/v1/novels/chapters/${chapterId}/publish`)
  },

  batchChapters(action: string, ids: bigint[]): Promise<void> {
    return http.post('/v1/novels/chapters/batch', { action, ids })
  }
}

// ==================== 诗歌 API ====================
export const poetryApi = {
  getList(params: PoetryQuery): Promise<PageResult<Poetry>> {
    return http.get('/v1/poetry', { params })
  },

  getById(id: bigint): Promise<Poetry> {
    return http.get(`/v1/poetry/${id}`)
  },

  create(data: PoetryRequest): Promise<bigint> {
    return http.post('/v1/poetry', data)
  },

  update(id: bigint, data: PoetryRequest): Promise<void> {
    return http.put(`/v1/poetry/${id}`, data)
  },

  delete(id: bigint): Promise<void> {
    return http.delete(`/v1/poetry/${id}`)
  },

  publish(id: bigint): Promise<void> {
    return http.post(`/v1/poetry/${id}/publish`)
  },

  batch(action: string, ids: bigint[]): Promise<void> {
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

  getById(id: bigint): Promise<Essay> {
    return http.get(`/v1/essays/${id}`)
  },

  create(data: EssayRequest): Promise<bigint> {
    return http.post('/v1/essays', data)
  },

  update(id: bigint, data: EssayRequest): Promise<void> {
    return http.put(`/v1/essays/${id}`, data)
  },

  delete(id: bigint): Promise<void> {
    return http.delete(`/v1/essays/${id}`)
  },

  publish(id: bigint): Promise<void> {
    return http.post(`/v1/essays/${id}/publish`)
  },

  batch(action: string, ids: bigint[]): Promise<void> {
    return http.post('/v1/essays/batch', { action, ids })
  },

  getCategories(): Promise<EssayCategory[]> {
    return http.get('/v1/essays/categories')
  }
}
