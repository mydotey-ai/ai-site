<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { NIcon } from 'naive-ui'
import { MoonOutline, SunnyOutline, SearchOutline, MenuOutline, CloseOutline } from '@vicons/ionicons5'

const route = useRoute()
const isDark = ref(true)
const isMobileMenuOpen = ref(false)

const navLinks = [
  { to: '/', label: '首页' },
  { to: '/blog', label: '博客' },
  { to: '/portfolio', label: '作品集' },
  { to: '/creation', label: '创作' },
  { to: '/media', label: '图库' },
  { to: '/about', label: '关于' }
]

function toggleTheme() {
  isDark.value = !isDark.value
  document.documentElement.setAttribute('data-theme', isDark.value ? 'dark' : 'light')
}

function toggleMobileMenu() {
  isMobileMenuOpen.value = !isMobileMenuOpen.value
}

function closeMobileMenu() {
  isMobileMenuOpen.value = false
}
</script>

<template>
  <div class="default-layout">
    <!-- 跳过导航链接（无障碍） -->
    <a href="#main-content" class="skip-link">跳到主要内容</a>

    <!-- 顶部导航 -->
    <header class="navbar">
      <div class="navbar__container">
        <!-- Logo -->
        <RouterLink to="/" class="navbar__logo">
          AI-Site
        </RouterLink>

        <!-- 桌面端导航 -->
        <nav class="navbar__menu">
          <RouterLink
            v-for="link in navLinks"
            :key="link.to"
            :to="link.to"
            class="navbar__link"
            :class="{ 'navbar__link--active': route.path === link.to }"
          >
            {{ link.label }}
          </RouterLink>
        </nav>

        <!-- 操作按钮 -->
        <div class="navbar__actions">
          <button class="navbar__icon-btn" title="搜索">
            <NIcon :size="20">
              <SearchOutline />
            </NIcon>
          </button>
          <button class="navbar__icon-btn" title="切换主题" @click="toggleTheme">
            <NIcon :size="20">
              <MoonOutline v-if="isDark" />
              <SunnyOutline v-else />
            </NIcon>
          </button>
          <button class="navbar__icon-btn navbar__hamburger" @click="toggleMobileMenu">
            <NIcon :size="20">
              <CloseOutline v-if="isMobileMenuOpen" />
              <MenuOutline v-else />
            </NIcon>
          </button>
        </div>
      </div>

      <!-- 移动端导航菜单 -->
      <Transition name="slide">
        <nav v-if="isMobileMenuOpen" class="navbar__mobile-menu">
          <RouterLink
            v-for="link in navLinks"
            :key="link.to"
            :to="link.to"
            class="navbar__mobile-link"
            :class="{ 'navbar__mobile-link--active': route.path === link.to }"
            @click="closeMobileMenu"
          >
            {{ link.label }}
          </RouterLink>
        </nav>
      </Transition>
    </header>

    <!-- 主内容区域 -->
    <main id="main-content" class="main">
      <router-view />
    </main>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer__container">
        <p class="footer__copyright">&copy; 2024 AI-Site. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<style scoped lang="scss">
.default-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

// 跳过导航链接
.skip-link {
  position: absolute;
  top: -40px;
  left: 0;
  padding: 8px 16px;
  background: var(--color-primary);
  color: white;
  z-index: 1000;
  transition: top 0.2s;

  &:focus {
    top: 0;
  }
}

// 导航栏
.navbar {
  position: sticky;
  top: 0;
  z-index: 100;
  height: var(--navbar-height, 64px);
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border-primary);
  backdrop-filter: blur(10px);

  &__container {
    max-width: 1440px;
    margin: 0 auto;
    padding: 0 24px;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  &__logo {
    font-size: 20px;
    font-weight: 700;
    color: var(--text-primary);
    text-decoration: none;
    transition: text-shadow 0.2s;

    &:hover {
      text-shadow: var(--glow-primary);
    }
  }

  &__menu {
    display: flex;
    gap: 32px;

    @media (max-width: 768px) {
      display: none;
    }
  }

  &__link {
    color: var(--text-secondary);
    text-decoration: none;
    font-weight: 500;
    transition: color 0.2s;
    position: relative;

    &:hover,
    &--active {
      color: var(--color-primary);
    }

    // 下划线动画
    &::after {
      content: '';
      position: absolute;
      bottom: -4px;
      left: 0;
      width: 0;
      height: 2px;
      background: var(--color-primary);
      transition: width 0.2s;
    }

    &:hover::after,
    &--active::after {
      width: 100%;
    }
  }

  &__actions {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  &__icon-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 36px;
    height: 36px;
    background: transparent;
    border: none;
    border-radius: 8px;
    color: var(--text-secondary);
    cursor: pointer;
    transition: all 0.2s;

    &:hover {
      background: var(--bg-hover);
      color: var(--text-primary);
    }
  }

  &__hamburger {
    @media (min-width: 769px) {
      display: none;
    }
  }

  &__mobile-menu {
    display: none;
    flex-direction: column;
    padding: 16px 24px;
    background: var(--bg-secondary);
    border-bottom: 1px solid var(--border-primary);

    @media (max-width: 768px) {
      display: flex;
    }
  }

  &__mobile-link {
    padding: 12px 0;
    color: var(--text-secondary);
    text-decoration: none;
    font-weight: 500;
    border-bottom: 1px solid var(--border-secondary);
    transition: color 0.2s;

    &:last-child {
      border-bottom: none;
    }

    &:hover,
    &--active {
      color: var(--color-primary);
    }
  }
}

// 滑动动画
.slide-enter-active,
.slide-leave-active {
  transition: all 0.2s ease;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

// 主内容
.main {
  flex: 1;
  max-width: 1440px;
  margin: 0 auto;
  padding: 24px;
  width: 100%;

  @media (max-width: 768px) {
    padding: 16px;
  }
}

// 页脚
.footer {
  background: var(--bg-secondary);
  border-top: 1px solid var(--border-primary);

  &__container {
    max-width: 1440px;
    margin: 0 auto;
    padding: 24px;
  }

  &__copyright {
    text-align: center;
    color: var(--text-muted);
    font-size: 14px;
    margin: 0;
  }
}
</style>