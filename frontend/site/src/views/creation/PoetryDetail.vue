<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NButton, useMessage } from 'naive-ui'
import { creationApi } from '@/api/creation'
import type { Poetry } from '@/types'

const route = useRoute()
const router = useRouter()
const message = useMessage()

const poetry = ref<Poetry | null>(null)
const loading = ref(false)

const poetrySlug = computed(() => route.params.slug as string)

async function loadData() {
  loading.value = true
  try {
    poetry.value = await creationApi.getPoetryBySlug(poetrySlug.value)
  } catch {
    message.error('加载失败')
    router.push('/creation/poetry')
  } finally {
    loading.value = false
  }
}

function goBack() {
  router.push('/creation/poetry')
}

onMounted(loadData)
</script>

<template>
  <div class="poetry-detail" v-if="poetry">
    <div class="poetry-header">
      <NButton text @click="goBack">
        ← 返回列表
      </NButton>
      <h1>{{ poetry.title }}</h1>
      <div class="meta">
        <span class="author">{{ poetry.author || '佚名' }}</span>
        <span v-if="poetry.category" class="category">{{ poetry.category.name }}</span>
        <span class="views">{{ poetry.viewCount }} 阅读</span>
      </div>
    </div>

    <div class="poetry-content">
      <p v-for="(line, index) in poetry.content?.split('\n').filter((l: string) => l.trim())" :key="index">
        {{ line }}
      </p>
    </div>
  </div>
</template>

<style scoped lang="scss">
.poetry-detail {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
}

.poetry-header {
  text-align: center;
  margin-bottom: 40px;

  h1 {
    font-size: 2rem;
    margin: 20px 0 15px;
  }

  .meta {
    display: flex;
    justify-content: center;
    gap: 20px;
    color: var(--text-secondary, #94a3b8);
    font-size: 0.9rem;

    .category {
      color: var(--color-primary, #3b82f6);
    }
  }
}

.poetry-content {
  text-align: center;
  line-height: 2.5;
  font-size: 1.1rem;
  padding: 20px;

  p {
    margin-bottom: 0.5em;
  }
}
</style>
