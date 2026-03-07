<script setup lang="ts">
import { NConfigProvider, NMessageProvider, NDialogProvider, NNotificationProvider, darkTheme } from 'naive-ui'
import { useTheme } from '@shared/composables'

const { theme } = useTheme()

// Naive UI 主题配置
const themeOverrides = {
  common: {
    primaryColor: '#3b82f6',
    primaryColorHover: '#60a5fa',
    primaryColorPressed: '#2563eb',
    primaryColorSuppl: '#3b82f6',
    borderRadius: '8px',
    borderRadiusSmall: '4px',
    fontFamily: '"Noto Sans SC", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif',
    fontSize: '14px',
    fontSizeMedium: '14px'
  },
  dark: {
    cardColor: '#1a1a24',
    modalColor: '#12121a',
    popoverColor: '#1a1a24',
    inputColor: '#0a0a0f',
    bodyColor: '#0a0a0f',
    borderColor: '#2d2d3a',
    dividerColor: '#2d2d3a'
  },
  Button: {
    heightMedium: '40px',
    paddingMedium: '0 20px',
    fontSizeMedium: '14px'
  },
  Input: {
    heightMedium: '40px'
  },
  Card: {
    color: '#1a1a24',
    borderColor: '#2d2d3a'
  }
}
</script>

<template>
  <NConfigProvider :theme="darkTheme" :theme-overrides="themeOverrides">
    <NMessageProvider>
      <NDialogProvider>
        <NNotificationProvider>
          <router-view />
        </NNotificationProvider>
      </NDialogProvider>
    </NMessageProvider>
  </NConfigProvider>
</template>

<style scoped>
</style>