<script setup lang="ts">
import { NLayout, NLayoutSider, NLayoutContent, NMenu, NIcon } from 'naive-ui'
import { h, ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  HomeOutline,
  NewspaperOutline,
  BriefcaseOutline,
  CreateOutline,
  ImagesOutline,
  PeopleOutline
} from '@vicons/ionicons5'

const router = useRouter()
const route = useRoute()

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
    label: '作品管理',
    key: '/portfolio',
    icon: () => h(NIcon, null, { default: () => h(BriefcaseOutline) })
  },
  {
    label: '创作管理',
    key: '/creation',
    icon: () => h(NIcon, null, { default: () => h(CreateOutline) })
  },
  {
    label: '媒体管理',
    key: '/media',
    icon: () => h(NIcon, null, { default: () => h(ImagesOutline) })
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

function handleMenuSelect(key: string) {
  router.push(key)
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
    <NLayoutContent class="admin-content">
      <slot />
    </NLayoutContent>
  </NLayout>
</template>

<style scoped lang="scss">
.admin-layout {
  height: 100vh;
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
  min-height: calc(100vh - 64px);
}

// 移动端响应式
@media (max-width: 1024px) {
  .admin-content {
    padding: 16px;
  }
}
</style>