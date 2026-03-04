// 类型定义

// API 响应
export interface Result<T> {
  code: number
  message: string
  data: T
  timestamp: string
}

// 分页结果
export interface PageResult<T> {
  list: T[]
  total: number
}

// 分页参数
export interface PageParams {
  page: number
  size: number
  sort?: string
}

// 用户
export interface User {
  id: number
  username: string
  email: string
  nickname: string
  avatar: string
  roles: string[]
  createdAt: string
}

// 文章
export interface Article {
  id: number
  title: string
  slug: string
  summary: string
  content: string
  coverImage: string
  categoryId: number
  categoryName: string
  tags: Tag[]
  status: ArticleStatus
  viewCount: number
  likeCount: number
  isTop: boolean
  publishedAt: string
  createdAt: string
}

export type ArticleStatus = 'DRAFT' | 'PUBLISHED' | 'ARCHIVED'

// 分类
export interface Category {
  id: number
  name: string
  slug: string
  parentId: number | null
  sort: number
}

// 标签
export interface Tag {
  id: number
  name: string
  slug: string
  color: string
}

// 项目
export interface Project {
  id: number
  name: string
  slug: string
  description: string
  content: string
  coverImage: string
  images: string[]
  techStack: string[]
  demoUrl: string
  sourceUrl: string
  status: number
  sort: number
}

// 小说
export interface Novel {
  id: number
  title: string
  author: string
  summary: string
  coverImage: string
  category: string
  status: NovelStatus
  wordCount: number
  chapterCount: number
  viewCount: number
  isFinished: boolean
}

export type NovelStatus = 'ONGOING' | 'COMPLETED'

// 章节
export interface Chapter {
  id: number
  novelId: number
  title: string
  content: string
  wordCount: number
  chapterNo: number
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
  tags: string[]
  isPublic: boolean
}

// 相册
export interface Album {
  id: number
  name: string
  description: string
  coverImage: string
  imageCount: number
  isPublic: boolean
}