<script setup lang="ts">
import { NButton } from 'naive-ui'
import { useRouter } from 'vue-router'

const router = useRouter()

function handleCreate() {
  router.push('/blog/create')
}
</script>

<template>
  <div class="article-list">
    <div class="header">
      <h1>文章管理</h1>
      <NButton type="primary" @click="handleCreate">新建文章</NButton>
    </div>
    <p>文章列表页面 - 待实现</p>
  </div>
</template>

<style scoped lang="scss">
.article-list {
  padding: 20px;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
}
</style>