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
        path: 'creation',
        name: 'CreationManage',
        component: () => import('@/views/creation/NovelManage.vue'),
        meta: { title: '创作管理' }
      },
      {
        path: 'media',
        name: 'MediaManage',
        component: () => import('@/views/media/ImageManage.vue'),
        meta: { title: '媒体管理' }
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