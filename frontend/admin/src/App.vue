<script setup lang="ts">
import { NConfigProvider, NMessageProvider, NDialogProvider, NNotificationProvider, darkTheme } from 'naive-ui'

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
    fontSizeMini: '12px',
    fontSizeTiny: '12px',
    fontSizeSmall: '13px',
    fontSizeMedium: '14px',
    fontSizeLarge: '15px',
    fontSizeHuge: '16px',
    height: '36px',
    heightMini: '24px',
    heightTiny: '28px',
    heightSmall: '32px',
    heightMedium: '36px',
    heightLarge: '40px',
    heightHuge: '44px'
  },
  dark: {
    cardColor: '#1a1a24',
    modalColor: '#12121a',
    popoverColor: '#1a1a24',
    tableColor: '#1a1a24',
    inputColor: '#0a0a0f',
    actionColor: '#12121a',
    bodyColor: '#0a0a0f',
    invertedColor: '#f8fafc',
    borderColor: '#2d2d3a',
    dividerColor: '#2d2d3a'
  },
  Button: {
    heightMedium: '36px',
    paddingMedium: '0 16px',
    fontSizeMedium: '14px'
  },
  Input: {
    heightMedium: '36px',
    fontSizeMedium: '14px'
  },
  DataTable: {
    thColor: '#12121a',
    tdColor: '#1a1a24',
    thTextColor: '#64748b',
    tdTextColor: '#f8fafc',
    borderColor: '#2d2d3a',
    thColorHover: '#22222e',
    tdColorHover: '#22222e'
  },
  Menu: {
    itemColorActive: '#3b82f6',
    itemColorActiveHover: '#60a5fa',
    itemTextColorActive: '#ffffff',
    itemTextColorActiveHover: '#ffffff',
    itemTextColor: '#94a3b8',
    itemTextColorHover: '#f8fafc',
    itemColorHover: '#22222e',
    arrowColor: '#94a3b8'
  },
  Card: {
    color: '#1a1a24',
    borderColor: '#2d2d3a',
    titleTextColor: '#f8fafc'
  },
  Layout: {
    siderColor: '#12121a',
    headerColor: '#12121a',
    color: '#0a0a0f'
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