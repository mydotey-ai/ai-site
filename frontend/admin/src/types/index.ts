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
  id: string | number
  username: string
  email: string
  nickname: string
  avatar: string
  bio: string
  status: number
  roles: string[]
  createdAt: string
  updatedAt: string
}

// 用户请求
export interface UserRequest {
  username: string
  password?: string
  email?: string
  nickname?: string
  avatar?: string
  bio?: string
  status?: number
  roleIds?: (string | number)[]
}

// 用户查询参数
export interface UserQuery extends PageQuery {
  keyword?: string
  status?: number
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
  id: string | number
  title: string
  slug: string
  summary: string
  content: string
  contentType: string
  coverImage: string
  categoryId: string | number
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
  authorId: string | number
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
  categoryId?: string | number
  tagIds?: (string | number)[]
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
  categoryId?: string | number
  keyword?: string
}

// 分类
export interface Category {
  id: string | number
  name: string
  slug: string
  description: string
  parentId: string | number
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
  parentId?: string | number
  sortOrder?: number
}

// 标签
export interface Tag {
  id: string | number
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
  id: string | number
  articleId: string | number
  parentId: string | number
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
  articleId?: string | number
}

// 项目/作品
export interface Project {
  id: string | number
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
  id: string | number
  name: string
  slug: string
  color: string
  sort?: number
  projectCount?: number
}

// 项目链接
export interface ProjectLink {
  type: string
  label: string
  url: string
}

// 项目请求
export interface ProjectRequest {
  name: string
  slug?: string
  description?: string
  content?: string
  coverImage?: string
  techStack?: string[]
  tagIds?: (string | number)[]
  links?: ProjectLinkRequest[]
  status?: string
}

// 项目链接请求
export interface ProjectLinkRequest {
  type: string
  label: string
  url: string
}

// 项目查询参数
export interface ProjectQuery extends PageQuery {
  tagId?: string | number
  status?: string
  keyword?: string
}

// 项目标签请求
export interface ProjectTagRequest {
  name: string
  slug?: string
  color?: string
  sort?: number
}

// 小说
export interface Novel {
  id: string | number
  title: string
  slug: string
  author: string
  summary: string
  coverImage: string
  categoryId: string | number
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
  id: string | number
  name: string
  slug: string
  sort: number
}

// 小说请求
export interface NovelRequest {
  title: string
  slug?: string
  author?: string
  summary?: string
  coverImage?: string
  categoryId?: string | number
  status?: string
}

// 小说查询参数
export interface NovelQuery extends PageQuery {
  categoryId?: string | number
  status?: string
  keyword?: string
}

// 章节
export interface Chapter {
  id: string | number
  novelId: string | number
  novelTitle?: string
  title: string
  content: string
  wordCount: number
  chapterNo: number
  status: string
  viewCount: number
  prevChapterId?: string | number
  nextChapterId?: string | number
  createdAt: string
  updatedAt: string
}

// 章节请求
export interface ChapterRequest {
  novelId: string | number
  title: string
  content?: string
  chapterNo?: number
  status?: string
}

// 诗歌分类
export interface PoetryCategory {
  id: string | number
  name: string
  slug: string
  sort: number
}

// 诗歌
export interface Poetry {
  id: string | number
  title: string
  slug: string
  author: string
  content: string
  categoryId: string | number
  category?: PoetryCategory
  status: string
  viewCount: number
  createdAt: string
  updatedAt: string
}

// 诗歌请求
export interface PoetryRequest {
  title: string
  slug?: string
  author?: string
  content?: string
  categoryId?: string | number
  status?: string
}

// 诗歌查询参数
export interface PoetryQuery extends PageQuery {
  categoryId?: string | number
  status?: string
  keyword?: string
}

// 散文分类
export interface EssayCategory {
  id: string | number
  name: string
  slug: string
  sort: number
}

// 散文
export interface Essay {
  id: string | number
  title: string
  slug: string
  author: string
  summary: string
  content: string
  categoryId: string | number
  category?: EssayCategory
  status: string
  viewCount: number
  createdAt: string
  updatedAt: string
}

// 散文请求
export interface EssayRequest {
  title: string
  slug?: string
  author?: string
  summary?: string
  content?: string
  categoryId?: string | number
  status?: string
}

// 散文查询参数
export interface EssayQuery extends PageQuery {
  categoryId?: string | number
  status?: string
  keyword?: string
}

// 图片
export interface Image {
  id: string | number
  title: string
  description: string
  originalName: string
  url: string
  thumbnailUrl: string
  albumId: string | number
  album?: AlbumVO
  folderId: string | number
  width: number
  height: number
  size: number
  mimeType: string
  tags: string[]
  isPublic: number
  viewCount: number
  createdAt: string
}

// 图片请求
export interface ImageRequest {
  title?: string
  description?: string
  albumId?: string | number
  folderId?: string | number
  tags?: string[]
  isPublic?: number
}

// 图片上传响应
export interface ImageUploadResponse {
  id: string | number
  title: string
  url: string
  thumbnailUrl: string
  width: number
  height: number
  size: number
  createdAt: string
}

// 图片查询参数
export interface ImageQuery extends PageQuery {
  albumId?: string | number
  folderId?: string | number
  keyword?: string
  isPublic?: number
}

// 批量操作请求
export interface BatchRequest {
  action: 'move' | 'delete' | 'setPublic' | 'setPrivate'
  ids: (string | number)[]
  targetAlbumId?: string | number
  targetFolderId?: string | number
}

// 相册
export interface Album {
  id: string | number
  name: string
  slug: string
  description: string
  coverImage: string
  imageCount: number
  isPublic: number
  sort: number
  createdAt: string
  updatedAt: string
  images?: Image[]
}

// 相册请求
export interface AlbumRequest {
  name: string
  slug?: string
  description?: string
  coverImage?: string
  isPublic?: number
  sort?: number
}

// 相册查询参数
export interface AlbumQuery extends PageQuery {
  isPublic?: number
}

// 相册 VO（嵌套在图片响应中）
export interface AlbumVO {
  id: string | number
  name: string
  slug: string
}

// 视频
export interface Video {
  id: string | number
  title: string
  description: string
  coverImage: string
  type: string
  platform: string
  videoId: string
  url: string
  duration: number
  size: number
  category: string
  tags: string[]
  isPublic: number
  viewCount: number
  embedUrl: string
  createdAt: string
}

// 视频请求
export interface VideoRequest {
  title: string
  description?: string
  coverImage?: string
  type: string
  platform?: string
  videoId?: string
  category?: string
  tags?: string[]
  isPublic?: number
}

// 视频查询参数
export interface VideoQuery extends PageQuery {
  type?: string
  platform?: string
  category?: string
  isPublic?: number
}

// 音频
export interface Audio {
  id: string | number
  title: string
  description: string
  coverImage: string
  type: string
  platform: string
  audioId: string
  url: string
  duration: number
  size: number
  category: string
  tags: string[]
  isPublic: number
  viewCount: number
  createdAt: string
}

// 音频请求
export interface AudioRequest {
  title: string
  description?: string
  coverImage?: string
  type: string
  platform?: string
  audioId?: string
  category?: string
  tags?: string[]
  isPublic?: number
}

// 音频查询参数
export interface AudioQuery extends PageQuery {
  type?: string
  platform?: string
  category?: string
  isPublic?: number
}

// 文件夹
export interface Folder {
  id: string | number
  name: string
  parentId: string | number
  type: string
  sort: number
  createdAt: string
  children?: Folder[]
}

// 文件夹请求
export interface FolderRequest {
  name: string
  parentId?: string | number
  type: string
  sort?: number
}
