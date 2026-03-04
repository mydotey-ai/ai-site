// API 响应类型
export interface Result<T = unknown> {
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

// 分页查询参数
export interface PageQuery {
  page?: number
  size?: number
}

// 文章
export interface Article {
  id: number
  title: string
  slug: string
  summary: string
  content: string
  coverImage: string
  categoryName: string
  status: number
  viewCount: number
  likeCount: number
  isTop: boolean
  publishedAt: string
  authorName: string
  tags: Tag[]
  createdAt: string
}

// 分类
export interface Category {
  id: number
  name: string
  slug: string
  description: string
  articleCount: number
}

// 标签
export interface Tag {
  id: number
  name: string
  slug: string
}

// 项目/作品
export interface Project {
  id: number
  title: string
  slug: string
  description: string
  content: string
  coverImage: string
  demoUrl: string
  githubUrl: string
  status: number
  sortOrder: number
  startedAt: string
  endedAt: string
}

// 小说
export interface Novel {
  id: number
  title: string
  slug: string
  author: string
  description: string
  coverImage: string
  status: number
  wordCount: number
  viewCount: number
  chapterCount: number
  createdAt: string
}

// 章节
export interface Chapter {
  id: number
  novelId: number
  title: string
  content: string
  wordCount: number
  chapterNumber: number
}

// 图片
export interface Image {
  id: number
  title: string
  description: string
  url: string
  thumbnailUrl: string
  albumId: number
  width: number
  height: number
  createdAt: string
}

// 相册
export interface Album {
  id: number
  name: string
  slug: string
  description: string
  coverImage: string
  imageCount: number
}