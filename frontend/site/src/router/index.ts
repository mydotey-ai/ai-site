import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('@/layouts/DefaultLayout.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/home/HomePage.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'blog',
        name: 'BlogList',
        component: () => import('@/views/blog/BlogList.vue'),
        meta: { title: '博客' }
      },
      {
        path: 'blog/:slug',
        name: 'BlogDetail',
        component: () => import('@/views/blog/BlogDetail.vue'),
        meta: { title: '文章详情' }
      },
      {
        path: 'category/:id',
        name: 'BlogCategory',
        component: () => import('@/views/blog/BlogCategory.vue'),
        meta: { title: '分类文章' }
      },
      {
        path: 'tag/:id',
        name: 'BlogTag',
        component: () => import('@/views/blog/BlogTag.vue'),
        meta: { title: '标签文章' }
      },
      {
        path: 'portfolio',
        name: 'Portfolio',
        component: () => import('@/views/portfolio/PortfolioList.vue'),
        meta: { title: '作品集' }
      },
      {
        path: 'portfolio/:id',
        name: 'PortfolioDetail',
        component: () => import('@/views/portfolio/PortfolioDetail.vue'),
        meta: { title: '作品详情' }
      },
      {
        path: 'creation/novels',
        name: 'NovelList',
        component: () => import('@/views/creation/NovelList.vue'),
        meta: { title: '小说' }
      },
      {
        path: 'creation/novels/:slug',
        name: 'NovelDetail',
        component: () => import('@/views/creation/NovelDetail.vue'),
        meta: { title: '小说详情' }
      },
      {
        path: 'creation/chapters/:id',
        name: 'ChapterReader',
        component: () => import('@/views/creation/NovelReader.vue'),
        meta: { title: '章节阅读' }
      },
      {
        path: 'creation/poetry',
        name: 'PoetryList',
        component: () => import('@/views/creation/PoetryList.vue'),
        meta: { title: '诗歌' }
      },
      {
        path: 'creation/poetry/:slug',
        name: 'PoetryDetail',
        component: () => import('@/views/creation/PoetryDetail.vue'),
        meta: { title: '诗歌详情' }
      },
      {
        path: 'creation/essays',
        name: 'EssayList',
        component: () => import('@/views/creation/EssayList.vue'),
        meta: { title: '散文' }
      },
      {
        path: 'creation/essays/:slug',
        name: 'EssayDetail',
        component: () => import('@/views/creation/EssayDetail.vue'),
        meta: { title: '散文详情' }
      },
      {
        path: 'media',
        name: 'Gallery',
        component: () => import('@/views/media/Gallery.vue'),
        meta: { title: '图库' }
      },
      {
        path: 'media/album/:slug',
        name: 'AlbumDetail',
        component: () => import('@/views/media/AlbumDetail.vue'),
        meta: { title: '相册详情' }
      },
      {
        path: 'media/videos',
        name: 'VideoList',
        component: () => import('@/views/media/VideoList.vue'),
        meta: { title: '视频' }
      },
      {
        path: 'media/videos/:id',
        name: 'VideoDetail',
        component: () => import('@/views/media/VideoDetail.vue'),
        meta: { title: '视频详情' }
      },
      {
        path: 'media/audios',
        name: 'AudioList',
        component: () => import('@/views/media/AudioList.vue'),
        meta: { title: '音频' }
      },
      {
        path: 'about',
        name: 'About',
        component: () => import('@/views/about/AboutPage.vue'),
        meta: { title: '关于' }
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