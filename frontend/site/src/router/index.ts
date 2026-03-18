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
        path: 'creation',
        name: 'Creation',
        component: () => import('@/views/creation/NovelList.vue'),
        meta: { title: '创作' }
      },
      {
        path: 'novel/:id',
        name: 'NovelReader',
        component: () => import('@/views/creation/NovelReader.vue'),
        meta: { title: '小说阅读' }
      },
      {
        path: 'media',
        name: 'Gallery',
        component: () => import('@/views/media/Gallery.vue'),
        meta: { title: '图库' }
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