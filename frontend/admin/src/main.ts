import { createApp } from 'vue'
import pinia from './stores'
import router from './router'
import { setupRouterGuards } from './router/guards'
import App from './App.vue'

import './assets/styles/global.scss'

const app = createApp(App)

// 状态管理
app.use(pinia)

// 路由
app.use(router)

// 路由守卫
setupRouterGuards(router)

app.mount('#app')