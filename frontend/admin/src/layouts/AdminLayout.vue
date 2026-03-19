<script setup lang="ts">
import { NLayout, NLayoutSider, NLayoutContent, NLayoutHeader, NMenu, NIcon, NDropdown, NAvatar } from 'naive-ui'
import { h, ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  HomeOutline,
  NewspaperOutline,
  BriefcaseOutline,
  CreateOutline,
  ImagesOutline,
  PeopleOutline,
  FolderOutline,
  PricetagsOutline,
  ChatboxOutline,
  LogOutOutline
} from '@vicons/ionicons5'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const collapsed = ref(false)

const menuOptions = [
  {
    label: '仪表盘',
    key: '/dashboard',
    icon: () => h(NIcon, null, { default: () => h(HomeOutline) })
  },
  {
    label: '文章管理',
    key: '/blog',
    icon: () => h(NIcon, null, { default: () => h(NewspaperOutline) })
  },
  {
    label: '分类管理',
    key: '/blog/categories',
    icon: () => h(NIcon, null, { default: () => h(FolderOutline) })
  },
  {
    label: '标签管理',
    key: '/blog/tags',
    icon: () => h(NIcon, null, { default: () => h(PricetagsOutline) })
  },
  {
    label: '评论管理',
    key: '/blog/comments',
    icon: () => h(NIcon, null, { default: () => h(ChatboxOutline) })
  },
  {
    label: '作品管理',
    key: '/portfolio',
    icon: () => h(NIcon, null, { default: () => h(BriefcaseOutline) })
  },
  {
    label: '创作管理',
    key: 'creation',
    icon: () => h(NIcon, null, { default: () => h(CreateOutline) }),
    children: [
      {
        label: '小说管理',
        key: '/creation/novels'
      },
      {
        label: '诗歌管理',
        key: '/creation/poetry'
      },
      {
        label: '散文管理',
        key: '/creation/essays'
      }
    ]
  },
  {
    label: '媒体管理',
    key: 'media',
    icon: () => h(NIcon, null, { default: () => h(ImagesOutline) }),
    children: [
      {
        label: '图片管理',
        key: '/media'
      },
      {
        label: '相册管理',
        key: '/media/albums'
      },
      {
        label: '视频管理',
        key: '/media/videos'
      },
      {
        label: '音频管理',
        key: '/media/audios'
      }
    ]
  },
  {
    label: '用户管理',
    key: '/user',
    icon: () => h(NIcon, null, { default: () => h(PeopleOutline) })
  }
]

const activeKey = computed({
  get: () => route.path,
  set: () => {}
})

const pageTitle = computed(() => {
  const current = menuOptions.find(item => {
    if (item.children) {
      return item.children.some((child: { key: string }) => child.key === route.path)
    }
    return item.key === route.path
  })
  if (current?.children) {
    const child = current.children.find((c: { key: string }) => c.key === route.path)
    return child?.label || ''
  }
  return current?.label || ''
})

function handleMenuSelect(key: string) {
  router.push(key)
}

const userDropdownOptions = [
  {
    label: '退出登录',
    key: 'logout',
    icon: () => h(NIcon, null, { default: () => h(LogOutOutline) })
  }
]

function handleUserDropdown(key: string) {
  if (key === 'logout') {
    authStore.logout()
    router.push('/login')
  }
}
</script>

<template>
  <NLayout has-sider class="admin-layout">
    <NLayoutSider
      bordered
      collapse-mode="width"
      :collapsed-width="64"
      :width="240"
      :collapsed="collapsed"
      show-trigger
      @collapse="collapsed = true"
      @expand="collapsed = false"
    >
      <div class="sidebar-logo" :class="{ 'sidebar-logo--collapsed': collapsed }">
        <router-link to="/dashboard" class="sidebar-logo__link">
          <span class="sidebar-logo__icon">AI</span>
          <span v-if="!collapsed" class="sidebar-logo__text">AI-Site</span>
        </router-link>
      </div>
      <NMenu
        :collapsed="collapsed"
        :collapsed-width="64"
        :collapsed-icon-size="22"
        :options="menuOptions"
        :value="activeKey"
        @update:value="handleMenuSelect"
      />
    </NLayoutSider>
    <NLayout class="admin-main">
      <NLayoutHeader bordered class="admin-header">
        <div class="header-title">{{ pageTitle }}</div>
        <div class="header-actions">
          <NDropdown
            :options="userDropdownOptions"
            @select="handleUserDropdown"
          >
            <div class="user-info">
              <NAvatar round size="small" :src="authStore.user?.avatar">
                {{ authStore.nickname?.charAt(0)?.toUpperCase() }}
              </NAvatar>
              <span class="user-name">{{ authStore.nickname }}</span>
            </div>
          </NDropdown>
        </div>
      </NLayoutHeader>
      <NLayoutContent class="admin-content">
        <router-view />
      </NLayoutContent>
    </NLayout>
  </NLayout>
</template>

<style scoped lang="scss">
.admin-layout {
  height: 100vh;
}

.admin-main {
  display: flex;
  flex-direction: column;
}

.admin-header {
  height: 64px;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--bg-secondary, #12121a);
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary, #f8fafc);
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s;

  &:hover {
    background: var(--bg-tertiary, #1a1a24);
  }
}

.user-name {
  font-size: 14px;
  color: var(--text-primary, #f8fafc);
}

.sidebar-logo {
  height: 64px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  border-bottom: 1px solid var(--border-primary, #2d2d3a);

  &--collapsed {
    padding: 0;
    justify-content: center;
  }

  &__link {
    display: flex;
    align-items: center;
    gap: 12px;
    text-decoration: none;
    color: var(--text-primary, #f8fafc);
    transition: all 0.2s;

    &:hover {
      text-shadow: var(--glow-primary, 0 0 20px rgba(59, 130, 246, 0.3));
    }
  }

  &__icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    background: var(--color-primary, #3b82f6);
    border-radius: 8px;
    font-size: 14px;
    font-weight: 700;
    color: white;
    flex-shrink: 0;
  }

  &__text {
    font-size: 18px;
    font-weight: 600;
  }
}

.admin-content {
  padding: 24px;
  background: var(--bg-primary, #0a0a0f);
  flex: 1;
}

// 移动端响应式
@media (max-width: 1024px) {
  .admin-content {
    padding: 16px;
  }
}
</style>