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
  sort?: string
}

// 用户相关类型
export interface User {
  id: number
  username: string
  email: string
  nickname: string
  avatar: string
  bio: string
  roles: string[]
}

// 登录请求
export interface LoginRequest {
  username: string
  password: string
}

// 登录响应
export interface LoginResponse {
  token: string
  refreshToken: string
  user: User
}

// 文章相关类型
export interface Article {
  id: number
  title: string
  slug: string
  summary: string
  content: string
  contentType: string
  coverImage: string
  categoryId: number
  category?: Category
  status: number
  viewCount: number
  likeCount: number
  isTop: number
  allowComment: number
  seoTitle: string
  seoDescription: string
  seoKeywords: string
  publishedAt: string
  authorId: number
  tags: Tag[]
  createdAt: string
  updatedAt: string
}

// 文章请求
export interface ArticleRequest {
  title: string
  slug: string
  summary?: string
  content?: string
  contentType?: string
  coverImage?: string
  categoryId?: number
  tagIds?: number[]
  status?: number
  isTop?: number
  allowComment?: number
  seoTitle?: string
  seoDescription?: string
  seoKeywords?: string
}

// 文章查询参数
export interface ArticleQuery extends PageQuery {
  status?: number
  categoryId?: number
  keyword?: string
}

// 分类
export interface Category {
  id: number
  name: string
  slug: string
  description: string
  parentId: number
  sortOrder: number
  articleCount: number
  createdAt: string
  updatedAt: string
}

// 分类请求
export interface CategoryRequest {
  name: string
  slug: string
  description?: string
  parentId?: number
  sortOrder?: number
}

// 标签
export interface Tag {
  id: number
  name: string
  slug: string
  color: string
  articleCount: number
  createdAt: string
  updatedAt: string
}

// 标签请求
export interface TagRequest {
  name: string
  slug: string
  color?: string
}

// 评论
export interface Comment {
  id: number
  articleId: number
  parentId: number
  nickname: string
  email: string
  website: string
  content: string
  status: string
  likeCount: number
  createdAt: string
  children?: Comment[]
}

// 评论查询参数
export interface CommentQuery extends PageQuery {
  status?: string
  articleId?: number
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
  createdAt: string
  updatedAt: string
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
  chapters: Chapter[]
  createdAt: string
  updatedAt: string
}

// 章节
export interface Chapter {
  id: number
  novelId: number
  title: string
  content: string
  wordCount: number
  chapterNumber: number
  status: number
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
  size: number
  mimeType: string
  createdAt: string
}

// 相册
export interface Album {
  id: number
  name: string
  slug: string
  description: string
  coverImage: string
  sortOrder: number
  imageCount: number
  createdAt: string
}