<script setup lang="ts">
</script>

<template>
  <div class="default-layout">
    <header class="header">
      <nav class="nav">
        <router-link to="/" class="logo">AI-Site</router-link>
        <div class="nav-links">
          <router-link to="/blog">博客</router-link>
          <router-link to="/portfolio">作品集</router-link>
          <router-link to="/creation">创作</router-link>
          <router-link to="/media">图库</router-link>
          <router-link to="/about">关于</router-link>
        </div>
      </nav>
    </header>
    <main class="main">
      <slot />
    </main>
    <footer class="footer">
      <p>&copy; 2024 AI-Site. All rights reserved.</p>
    </footer>
  </div>
</template>

<style scoped lang="scss">
.default-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: #fff;
  border-bottom: 1px solid #eee;
}

.nav {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  text-decoration: none;
}

.nav-links {
  display: flex;
  gap: 24px;

  a {
    color: #666;
    text-decoration: none;

    &:hover {
      color: #1890ff;
    }
  }
}

.main {
  flex: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  width: 100%;
}

.footer {
  padding: 20px;
  text-align: center;
  color: #999;
  border-top: 1px solid #eee;

  p {
    margin: 0;
  }
}
</style>