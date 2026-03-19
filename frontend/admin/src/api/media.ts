import { http } from '@/utils/request'
import type {
  Image,
  ImageRequest,
  ImageUploadResponse,
  ImageQuery,
  Album,
  AlbumRequest,
  AlbumQuery,
  Video,
  VideoRequest,
  VideoQuery,
  Audio,
  AudioRequest,
  AudioQuery,
  Folder,
  FolderRequest,
  BatchRequest,
  PageResult
} from '@/types'

// 图片 API
export const imageApi = {
  getList(params: ImageQuery): Promise<PageResult<Image>> {
    return http.get('/api/admin/v1/images', { params })
  },

  getById(id: number): Promise<Image> {
    return http.get(`/api/admin/v1/images/${id}`)
  },

  upload(formData: FormData): Promise<ImageUploadResponse> {
    return http.post('/api/admin/v1/images', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },

  update(id: number, data: ImageRequest): Promise<void> {
    return http.put(`/api/admin/v1/images/${id}`, data)
  },

  delete(id: number): Promise<void> {
    return http.delete(`/api/admin/v1/images/${id}`)
  },

  batch(data: BatchRequest): Promise<void> {
    return http.post('/api/admin/v1/images/batch', data)
  }
}

// 相册 API
export const albumApi = {
  getList(params: AlbumQuery): Promise<PageResult<Album>> {
    return http.get('/api/admin/v1/albums', { params })
  },

  getById(id: number): Promise<Album> {
    return http.get(`/api/admin/v1/albums/${id}`)
  },

  create(data: AlbumRequest): Promise<number> {
    return http.post('/api/admin/v1/albums', data)
  },

  update(id: number, data: AlbumRequest): Promise<void> {
    return http.put(`/api/admin/v1/albums/${id}`, data)
  },

  delete(id: number): Promise<void> {
    return http.delete(`/api/admin/v1/albums/${id}`)
  }
}

// 视频 API
export const videoApi = {
  getList(params: VideoQuery): Promise<PageResult<Video>> {
    return http.get('/api/admin/v1/videos', { params })
  },

  getById(id: number): Promise<Video> {
    return http.get(`/api/admin/v1/videos/${id}`)
  },

  create(data: VideoRequest): Promise<number> {
    return http.post('/api/admin/v1/videos', data)
  },

  upload(formData: FormData): Promise<number> {
    return http.post('/api/admin/v1/videos/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },

  update(id: number, data: VideoRequest): Promise<void> {
    return http.put(`/api/admin/v1/videos/${id}`, data)
  },

  delete(id: number): Promise<void> {
    return http.delete(`/api/admin/v1/videos/${id}`)
  }
}

// 音频 API
export const audioApi = {
  getList(params: AudioQuery): Promise<PageResult<Audio>> {
    return http.get('/api/admin/v1/audios', { params })
  },

  getById(id: number): Promise<Audio> {
    return http.get(`/api/admin/v1/audios/${id}`)
  },

  create(data: AudioRequest): Promise<number> {
    return http.post('/api/admin/v1/audios', data)
  },

  upload(formData: FormData): Promise<number> {
    return http.post('/api/admin/v1/audios/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },

  update(id: number, data: AudioRequest): Promise<void> {
    return http.put(`/api/admin/v1/audios/${id}`, data)
  },

  delete(id: number): Promise<void> {
    return http.delete(`/api/admin/v1/audios/${id}`)
  }
}

// 文件夹 API
export const folderApi = {
  getList(type?: string): Promise<Folder[]> {
    return http.get('/api/admin/v1/folders', { params: { type } })
  },

  getById(id: number): Promise<Folder> {
    return http.get(`/api/admin/v1/folders/${id}`)
  },

  create(data: FolderRequest): Promise<number> {
    return http.post('/api/admin/v1/folders', data)
  },

  update(id: number, data: FolderRequest): Promise<void> {
    return http.put(`/api/admin/v1/folders/${id}`, data)
  },

  delete(id: number): Promise<void> {
    return http.delete(`/api/admin/v1/folders/${id}`)
  }
}
