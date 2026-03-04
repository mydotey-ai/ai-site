<script setup lang="ts">
</script>

<template>
  <div class="not-found">
    <h1>404</h1>
    <p>页面不存在</p>
    <router-link to="/">返回首页</router-link>
  </div>
</template>

<style scoped lang="scss">
.not-found {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;

  h1 {
    font-size: 72px;
    margin-bottom: 20px;
  }

  p {
    margin-bottom: 20px;
    color: #666;
  }
}
</style>