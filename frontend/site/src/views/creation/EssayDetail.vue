<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NButton, useMessage } from 'naive-ui'
import { creationApi } from '@/api/creation'
import type { Essay } from '@/types'

const route = useRoute()
const router = useRouter()
const message = useMessage()

const essay = ref<Essay | null>(null)
const loading = ref(false)

const essaySlug = computed(() => route.params.slug as string)

async function loadData() {
  loading.value = true
  try {
    essay.value = await creationApi.getEssayBySlug(essaySlug.value)
  } catch {
    message.error('加载失败')
    router.push('/creation/essays')
  } finally {
    loading.value = false
  }
}

function goBack() {
  router.push('/creation/essays')
}

function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

onMounted(loadData)
</script>

<template>
  <div class="essay-detail" v-if="essay">
    <article class="essay-article">
      <header class="essay-header">
        <NButton text @click="goBack">
          ← 返回列表
        </NButton>
        <h1>{{ essay.title }}</h1>
        <div class="meta">
          <span class="author">{{ essay.author || '佚名' }}</span>
          <span class="date">{{ formatDate(essay.createdAt) }}</span>
          <span v-if="essay.category" class="category">{{ essay.category.name }}</span>
          <span class="views">{{ essay.viewCount }} 阅读</span>
        </div>
      </header>

      <div class="essay-summary" v-if="essay.summary">
        <p>{{ essay.summary }}</p>
      </div>

      <div class="essay-content">
        <p v-for="(para, index) in essay.content?.split('\n').filter((p: string) => p.trim())" :key="index">
          {{ para }}
        </p>
      </div>
    </article>
  </div>
</template>

<style scoped lang="scss">
.essay-detail {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
}

.essay-header {
  margin-bottom: 30px;

  h1 {
    font-size: 2rem;
    margin: 20px 0 15px;
  }

  .meta {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    color: var(--text-secondary, #94a3b8);
    font-size: 0.9rem;

    .category {
      color: var(--color-primary, #3b82f6);
    }
  }
}

.essay-summary {
  padding: 20px;
  background: var(--card-bg, #f8fafc);
  border-radius: 8px;
  margin-bottom: 30px;
  border-left: 4px solid var(--color-primary, #3b82f6);

  p {
    color: var(--text-secondary, #64748b);
    line-height: 1.8;
    margin: 0;
  }
}

.essay-content {
  line-height: 2;
  font-size: 1.1rem;

  p {
    text-indent: 2em;
    margin-bottom: 1em;
  }
}
</style>
