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
  id: bigint
  title: string
  slug: string
  summary: string
  content: string
  contentType: string
  coverImage: string
  categoryId: bigint
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
  authorId: bigint
  tags: Tag[]
  createdAt: string
  updatedAt: string
}

// 文章查询参数
export interface ArticleQuery extends PageQuery {
  categoryId?: bigint
  tagId?: bigint
  keyword?: string
}

// 分类
export interface Category {
  id: bigint
  name: string
  slug: string
  description: string
  parentId: bigint
  sortOrder: number
  articleCount: number
  createdAt: string
}

// 标签
export interface Tag {
  id: bigint
  name: string
  slug: string
  color: string
  articleCount: number
  createdAt: string
}

// 评论
export interface Comment {
  id: bigint
  articleId: bigint
  parentId: bigint
  nickname: string
  email: string
  website: string
  content: string
  status: string
  likeCount: number
  createdAt: string
  children?: Comment[]
}

// 评论请求
export interface CommentRequest {
  articleId: bigint
  parentId?: bigint
  nickname: string
  email?: string
  website?: string
  content: string
}

// 项目/作品
export interface Project {
  id: bigint
  name: string
  slug: string
  description: string
  content: string
  coverImage: string
  techStack: string[]
  status: string
  tags: ProjectTag[]
  links: ProjectLink[]
  createdAt: string
  updatedAt: string
}

// 项目标签
export interface ProjectTag {
  id: bigint
  name: string
  slug: string
  color: string
  projectCount?: number
}

// 项目链接
export interface ProjectLink {
  type: string
  label: string
  url: string
}

// 项目查询参数
export interface ProjectQuery extends PageQuery {
  tagId?: bigint
}

// 小说
export interface Novel {
  id: bigint
  title: string
  slug: string
  author: string
  summary: string
  coverImage: string
  categoryId: bigint
  category?: NovelCategory
  status: string
  wordCount: number
  chapterCount: number
  viewCount: number
  createdAt: string
  updatedAt: string
}

// 小说分类
export interface NovelCategory {
  id: bigint
  name: string
  slug: string
}

// 小说查询参数
export interface NovelQuery extends PageQuery {
  categoryId?: bigint
}

// 章节
export interface Chapter {
  id: bigint
  novelId: bigint
  novelTitle?: string
  title: string
  content: string
  wordCount: number
  chapterNo: number
  status: string
  viewCount: number
  prevChapterId?: bigint
  nextChapterId?: bigint
  createdAt: string
}

// 诗歌分类
export interface PoetryCategory {
  id: bigint
  name: string
  slug: string
}

// 诗歌
export interface Poetry {
  id: bigint
  title: string
  slug: string
  author: string
  content: string
  categoryId: bigint
  category?: PoetryCategory
  status: string
  viewCount: number
  createdAt: string
  updatedAt: string
}

// 诗歌查询参数
export interface PoetryQuery extends PageQuery {
  categoryId?: bigint
}

// 散文分类
export interface EssayCategory {
  id: bigint
  name: string
  slug: string
}

// 散文
export interface Essay {
  id: bigint
  title: string
  slug: string
  author: string
  summary: string
  content: string
  categoryId: bigint
  category?: EssayCategory
  status: string
  viewCount: number
  createdAt: string
  updatedAt: string
}

// 散文查询参数
export interface EssayQuery extends PageQuery {
  categoryId?: bigint
}

// 图片
export interface Image {
  id: bigint
  title: string
  description: string
  url: string
  thumbnailUrl: string
  albumId: bigint
  width: number
  height: number
  createdAt: string
}

// 相册
export interface Album {
  id: bigint
  name: string
  slug: string
  description: string
  coverImage: string
  imageCount: number
}
