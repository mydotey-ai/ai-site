import { http } from '@/utils/request'
import type { PageResult } from '@/types'

// 公开 API 基础路径
const API_PREFIX = '/api/v1'

// 相册
export interface Album {
  id: number
  name: string
  slug: string
  description: string
  coverImage: string
  imageCount: number
  createdAt: string
}

// 图片
export interface Image {
  id: number
  title: string
  description: string
  url: string
  thumbnailUrl: string
  width: number
  height: number
  size: number
  albumId: number
  createdAt: string
}

// 视频
export interface Video {
  id: number
  title: string
  description: string
  coverImage: string
  type: string
  platform: string
  videoId: string
  duration: number
  embedUrl: string
  viewCount: number
  createdAt: string
}

// 音频
export interface Audio {
  id: number
  title: string
  description: string
  coverImage: string
  type: string
  platform: string
  audioId: string
  url: string
  duration: number
  viewCount: number
  createdAt: string
}

// 相册 API
export const albumApi = {
  getList(): Promise<Album[]> {
    return http.get(`${API_PREFIX}/albums`)
  },

  getBySlug(slug: string): Promise<Album> {
    return http.get(`${API_PREFIX}/albums/${slug}`)
  },

  getImages(slug: string, page = 1, size = 20): Promise<PageResult<Image>> {
    return http.get(`${API_PREFIX}/albums/${slug}/images`, { params: { page, size } })
  }
}

// 视频 API
export const videoApi = {
  getList(page = 1, size = 20): Promise<PageResult<Video>> {
    return http.get(`${API_PREFIX}/videos`, { params: { page, size } })
  },

  getById(id: number): Promise<Video> {
    return http.get(`${API_PREFIX}/videos/${id}`)
  }
}

// 音频 API
export const audioApi = {
  getList(page = 1, size = 20): Promise<PageResult<Audio>> {
    return http.get(`${API_PREFIX}/audios`, { params: { page, size } })
  },

  getById(id: number): Promise<Audio> {
    return http.get(`${API_PREFIX}/audios/${id}`)
  }
}
