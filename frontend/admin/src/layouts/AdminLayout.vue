<script setup lang="ts">
import { NLayout, NLayoutSider, NLayoutContent, NMenu, NIcon } from 'naive-ui'
import { h, ref } from 'vue'
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

const activeKey = ref(route.path)

function handleMenuSelect(key: string) {
  router.push(key)
}
</script>

<template>
  <NLayout has-sider style="height: 100vh">
    <NLayoutSider
      bordered
      collapse-mode="width"
      :collapsed-width="64"
      :width="200"
      :collapsed="collapsed"
      show-trigger
      @collapse="collapsed = true"
      @expand="collapsed = false"
    >
      <div class="logo">
        <h2 v-if="!collapsed">AI-Site Admin</h2>
        <span v-else>AI</span>
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
    <NLayoutContent>
      <slot />
    </NLayoutContent>
  </NLayout>
</template>

<style scoped lang="scss">
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid var(--n-border-color);

  h2 {
    margin: 0;
    font-size: 16px;
  }
}
</style>