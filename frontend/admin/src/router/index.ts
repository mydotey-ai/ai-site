import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginPage.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/layouts/AdminLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/DashboardPage.vue'),
        meta: { title: '仪表盘' }
      },
      {
        path: 'blog',
        name: 'BlogManage',
        component: () => import('@/views/blog/ArticleList.vue'),
        meta: { title: '文章管理' }
      },
      {
        path: 'blog/create',
        name: 'ArticleCreate',
        component: () => import('@/views/blog/ArticleEdit.vue'),
        meta: { title: '创建文章' }
      },
      {
        path: 'blog/edit/:id',
        name: 'ArticleEdit',
        component: () => import('@/views/blog/ArticleEdit.vue'),
        meta: { title: '编辑文章' }
      },
      {
        path: 'blog/categories',
        name: 'CategoryManage',
        component: () => import('@/views/blog/CategoryManage.vue'),
        meta: { title: '分类管理' }
      },
      {
        path: 'blog/tags',
        name: 'TagManage',
        component: () => import('@/views/blog/TagManage.vue'),
        meta: { title: '标签管理' }
      },
      {
        path: 'blog/comments',
        name: 'CommentManage',
        component: () => import('@/views/blog/CommentManage.vue'),
        meta: { title: '评论管理' }
      },
      {
        path: 'portfolio',
        name: 'PortfolioManage',
        component: () => import('@/views/portfolio/ProjectList.vue'),
        meta: { title: '作品管理' }
      },
      {
        path: 'portfolio/create',
        name: 'ProjectCreate',
        component: () => import('@/views/portfolio/ProjectEdit.vue'),
        meta: { title: '创建项目' }
      },
      {
        path: 'portfolio/edit/:id',
        name: 'ProjectEdit',
        component: () => import('@/views/portfolio/ProjectEdit.vue'),
        meta: { title: '编辑项目' }
      },
      {
        path: 'portfolio/tags',
        name: 'ProjectTagManage',
        component: () => import('@/views/portfolio/ProjectTagManage.vue'),
        meta: { title: '项目标签管理' }
      },
      {
        path: 'creation/novels',
        name: 'NovelList',
        component: () => import('@/views/creation/NovelList.vue'),
        meta: { title: '小说管理' }
      },
      {
        path: 'creation/novels/create',
        name: 'NovelCreate',
        component: () => import('@/views/creation/NovelEdit.vue'),
        meta: { title: '新建小说' }
      },
      {
        path: 'creation/novels/edit/:id',
        name: 'NovelEdit',
        component: () => import('@/views/creation/NovelEdit.vue'),
        meta: { title: '编辑小说' }
      },
      {
        path: 'creation/novels/:novelId/chapters',
        name: 'ChapterManage',
        component: () => import('@/views/creation/ChapterManage.vue'),
        meta: { title: '章节管理' }
      },
      {
        path: 'creation/poetry',
        name: 'PoetryManage',
        component: () => import('@/views/creation/PoetryManage.vue'),
        meta: { title: '诗歌管理' }
      },
      {
        path: 'creation/essays',
        name: 'EssayManage',
        component: () => import('@/views/creation/EssayManage.vue'),
        meta: { title: '散文管理' }
      },
      {
        path: 'media',
        name: 'MediaManage',
        component: () => import('@/views/media/ImageManage.vue'),
        meta: { title: '图片管理' }
      },
      {
        path: 'media/albums',
        name: 'AlbumManage',
        component: () => import('@/views/media/AlbumManage.vue'),
        meta: { title: '相册管理' }
      },
      {
        path: 'media/videos',
        name: 'VideoManage',
        component: () => import('@/views/media/VideoManage.vue'),
        meta: { title: '视频管理' }
      },
      {
        path: 'media/audios',
        name: 'AudioManage',
        component: () => import('@/views/media/AudioManage.vue'),
        meta: { title: '音频管理' }
      },
      {
        path: 'user',
        name: 'UserManage',
        component: () => import('@/views/user/UserList.vue'),
        meta: { title: '用户管理' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/NotFound.vue'),
    meta: { title: '页面不存在' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router