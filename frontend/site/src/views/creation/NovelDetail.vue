<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NButton, NSpace, NTag, useMessage } from 'naive-ui'
import { creationApi } from '@/api/creation'
import type { Novel, Chapter } from '@/types'

const route = useRoute()
const router = useRouter()
const message = useMessage()

const novel = ref<Novel | null>(null)
const chapters = ref<Chapter[]>([])
const loading = ref(false)

const novelSlug = computed(() => route.params.slug as string)

async function loadData() {
  loading.value = true
  try {
    const novelData = await creationApi.getNovelBySlug(novelSlug.value)
    novel.value = novelData
    chapters.value = await creationApi.getChapters(novelData.id)
  } catch {
    message.error('加载失败')
    router.push('/creation/novels')
  } finally {
    loading.value = false
  }
}

function readChapter(chapter: Chapter) {
  router.push(`/creation/chapters/${chapter.id}`)
}

function startReading() {
  if (chapters.value.length) {
    readChapter(chapters.value[0])
  }
}

function formatNumber(num: number) {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '万'
  }
  return num
}

function getStatusLabel(status: string) {
  const map: Record<string, string> = {
    DRAFT: '草稿',
    PUBLISHED: '连载中',
    COMPLETED: '已完结'
  }
  return map[status] || status
}

onMounted(loadData)
</script>

<template>
  <div class="novel-detail" v-if="novel">
    <div class="novel-header">
      <div class="cover-section">
        <div class="cover" v-if="novel.coverImage">
          <img :src="novel.coverImage" :alt="novel.title" />
        </div>
        <div class="cover placeholder" v-else>
          <span>小说</span>
        </div>
      </div>
      <div class="info-section">
        <h1>{{ novel.title }}</h1>
        <div class="meta">
          <span class="author">{{ novel.author || '佚名' }}</span>
          <NTag size="small" :type="novel.status === 'COMPLETED' ? 'success' : 'info'">
            {{ getStatusLabel(novel.status) }}
          </NTag>
          <span v-if="novel.category">{{ novel.category.name }}</span>
        </div>
        <div class="stats">
          <span>{{ formatNumber(novel.wordCount) }} 字</span>
          <span>{{ novel.chapterCount }} 章</span>
          <span>{{ formatNumber(novel.viewCount) }} 阅读</span>
        </div>
        <p class="summary">{{ novel.summary || '暂无简介' }}</p>
        <NButton type="primary" size="large" :disabled="!chapters.length" @click="startReading">
          {{ chapters.length ? '开始阅读' : '暂无章节' }}
        </NButton>
      </div>
    </div>

    <div class="chapters-section">
      <h2>章节目录</h2>
      <div class="chapter-list" v-if="chapters.length">
        <div
          v-for="chapter in chapters"
          :key="chapter.id"
          class="chapter-item"
          @click="readChapter(chapter)"
        >
          <span class="chapter-no">第{{ chapter.chapterNo }}章</span>
          <span class="chapter-title">{{ chapter.title }}</span>
          <span class="chapter-words">{{ chapter.wordCount }} 字</span>
        </div>
      </div>
      <div v-else class="empty">
        <p>暂无章节</p>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.novel-detail {
  max-width: 900px;
  margin: 0 auto;
  padding: 40px 20px;
}

.novel-header {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;

  @media (max-width: 768px) {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
}

.cover-section {
  flex-shrink: 0;

  .cover {
    width: 200px;
    height: 280px;
    border-radius: 8px;
    overflow: hidden;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    &.placeholder {
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 2rem;
      color: white;
      font-weight: bold;
    }
  }
}

.info-section {
  flex: 1;

  h1 {
    font-size: 2rem;
    margin-bottom: 12px;
  }

  .meta {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 12px;
    color: var(--text-secondary, #94a3b8);
  }

  .stats {
    display: flex;
    gap: 20px;
    margin-bottom: 16px;
    color: var(--text-secondary, #94a3b8);
    font-size: 0.9rem;
  }

  .summary {
    line-height: 1.8;
    margin-bottom: 20px;
    color: var(--text-secondary, #94a3b8);
  }
}

.chapters-section {
  h2 {
    font-size: 1.5rem;
    margin-bottom: 20px;
    padding-bottom: 10px;
    border-bottom: 1px solid var(--border-color, #e5e7eb);
  }
}

.chapter-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 8px;
}

.chapter-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: var(--card-bg, #ffffff);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    background: var(--hover-bg, #f3f4f6);
    transform: translateX(4px);
  }

  .chapter-no {
    color: var(--color-primary, #3b82f6);
    font-weight: 500;
    min-width: 60px;
  }

  .chapter-title {
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .chapter-words {
    color: var(--text-secondary, #94a3b8);
    font-size: 0.8rem;
    margin-left: 10px;
  }
}

.empty {
  text-align: center;
  padding: 40px;
  color: var(--text-secondary, #94a3b8);
}
</style>
