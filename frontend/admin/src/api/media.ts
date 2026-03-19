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
  PageResult,
  ID
} from '@/types'

// 图片 API
export const imageApi = {
  getList(params: ImageQuery): Promise<PageResult<Image>> {
    return http.get('/v1/images', { params })
  },

  getById(id: ID): Promise<Image> {
    return http.get(`/v1/images/${id}`)
  },

  upload(formData: FormData): Promise<ImageUploadResponse> {
    return http.post('/v1/images', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },

  update(id: ID, data: ImageRequest): Promise<void> {
    return http.put(`/v1/images/${id}`, data)
  },

  delete(id: ID): Promise<void> {
    return http.delete(`/v1/images/${id}`)
  },

  batch(data: BatchRequest): Promise<void> {
    return http.post('/v1/images/batch', data)
  }
}

// 相册 API
export const albumApi = {
  getList(params: AlbumQuery): Promise<PageResult<Album>> {
    return http.get('/v1/albums', { params })
  },

  getById(id: ID): Promise<Album> {
    return http.get(`/v1/albums/${id}`)
  },

  create(data: AlbumRequest): Promise<ID> {
    return http.post('/v1/albums', data)
  },

  update(id: ID, data: AlbumRequest): Promise<void> {
    return http.put(`/v1/albums/${id}`, data)
  },

  delete(id: ID): Promise<void> {
    return http.delete(`/v1/albums/${id}`)
  }
}

// 视频 API
export const videoApi = {
  getList(params: VideoQuery): Promise<PageResult<Video>> {
    return http.get('/v1/videos', { params })
  },

  getById(id: ID): Promise<Video> {
    return http.get(`/v1/videos/${id}`)
  },

  create(data: VideoRequest): Promise<ID> {
    return http.post('/v1/videos', data)
  },

  upload(formData: FormData): Promise<ID> {
    return http.post('/v1/videos/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },

  update(id: ID, data: VideoRequest): Promise<void> {
    return http.put(`/v1/videos/${id}`, data)
  },

  delete(id: ID): Promise<void> {
    return http.delete(`/v1/videos/${id}`)
  }
}

// 音频 API
export const audioApi = {
  getList(params: AudioQuery): Promise<PageResult<Audio>> {
    return http.get('/v1/audios', { params })
  },

  getById(id: ID): Promise<Audio> {
    return http.get(`/v1/audios/${id}`)
  },

  create(data: AudioRequest): Promise<ID> {
    return http.post('/v1/audios', data)
  },

  upload(formData: FormData): Promise<ID> {
    return http.post('/v1/audios/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },

  update(id: ID, data: AudioRequest): Promise<void> {
    return http.put(`/v1/audios/${id}`, data)
  },

  delete(id: ID): Promise<void> {
    return http.delete(`/v1/audios/${id}`)
  }
}

// 文件夹 API
export const folderApi = {
  getList(type?: string): Promise<Folder[]> {
    return http.get('/v1/folders', { params: { type } })
  },

  getById(id: ID): Promise<Folder> {
    return http.get(`/v1/folders/${id}`)
  },

  create(data: FolderRequest): Promise<ID> {
    return http.post('/v1/folders', data)
  },

  update(id: ID, data: FolderRequest): Promise<void> {
    return http.put(`/v1/folders/${id}`, data)
  },

  delete(id: ID): Promise<void> {
    return http.delete(`/v1/folders/${id}`)
  }
}
