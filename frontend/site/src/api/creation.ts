import { http } from '@/utils/request'
import type {
  Novel, NovelQuery, NovelCategory, Chapter,
  Poetry, PoetryQuery, PoetryCategory,
  Essay, EssayQuery, EssayCategory,
  PageResult
} from '@/types'

export const creationApi = {
  // ==================== 小说 ====================
  getNovels(params: NovelQuery): Promise<PageResult<Novel>> {
    return http.get('/v1/novels', { params })
  },

  getNovelById(id: bigint): Promise<Novel> {
    return http.get(`/v1/novels/${id}`)
  },

  getNovelBySlug(slug: string): Promise<Novel> {
    return http.get(`/v1/novels/slug/${slug}`)
  },

  getNovelCategories(): Promise<NovelCategory[]> {
    return http.get('/v1/novels/categories')
  },

  getChapters(novelId: bigint): Promise<Chapter[]> {
    return http.get(`/v1/novels/${novelId}/chapters`)
  },

  getChapter(chapterId: bigint): Promise<Chapter> {
    return http.get(`/v1/novels/chapters/${chapterId}`)
  },

  // ==================== 诗歌 ====================
  getPoetryList(params: PoetryQuery): Promise<PageResult<Poetry>> {
    return http.get('/v1/poetry', { params })
  },

  getPoetryById(id: bigint): Promise<Poetry> {
    return http.get(`/v1/poetry/${id}`)
  },

  getPoetryBySlug(slug: string): Promise<Poetry> {
    return http.get(`/v1/poetry/slug/${slug}`)
  },

  getPoetryCategories(): Promise<PoetryCategory[]> {
    return http.get('/v1/poetry/categories')
  },

  // ==================== 散文 ====================
  getEssayList(params: EssayQuery): Promise<PageResult<Essay>> {
    return http.get('/v1/essays', { params })
  },

  getEssayById(id: bigint): Promise<Essay> {
    return http.get(`/v1/essays/${id}`)
  },

  getEssayBySlug(slug: string): Promise<Essay> {
    return http.get(`/v1/essays/slug/${slug}`)
  },

  getEssayCategories(): Promise<EssayCategory[]> {
    return http.get('/v1/essays/categories')
  }
}
