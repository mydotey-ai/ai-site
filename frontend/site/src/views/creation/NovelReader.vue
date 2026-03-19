<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NButton, NSpace, useMessage } from 'naive-ui'
import { creationApi } from '@/api/creation'
import type { Chapter } from '@/types'

const route = useRoute()
const router = useRouter()
const message = useMessage()

const chapter = ref<Chapter | null>(null)
const loading = ref(false)

const chapterId = computed(() => Number(route.params.id))

async function loadData() {
  loading.value = true
  try {
    chapter.value = await creationApi.getChapter(chapterId.value)
  } catch {
    message.error('加载失败')
    router.push('/creation/novels')
  } finally {
    loading.value = false
  }
}

function goToChapter(id: number | undefined) {
  if (id) {
    router.push(`/creation/chapters/${id}`)
  }
}

function goToNovel() {
  if (chapter.value) {
    router.push(`/creation/novels/${chapter.value.novelId}`)
  }
}

onMounted(loadData)
</script>

<template>
  <div class="chapter-reader" v-if="chapter">
    <div class="reader-header">
      <div class="novel-title" @click="goToNovel">{{ chapter.novelTitle }}</div>
      <h1>{{ chapter.title }}</h1>
    </div>

    <div class="reader-content">
      <p v-for="(para, index) in chapter.content?.split('\n').filter((p: string) => p.trim())" :key="index">
        {{ para }}
      </p>
    </div>

    <div class="reader-footer">
      <NSpace justify="space-between">
        <NButton
          :disabled="!chapter.prevChapterId"
          @click="goToChapter(chapter.prevChapterId)"
        >
          上一章
        </NButton>
        <NButton @click="goToNovel">目录</NButton>
        <NButton
          :disabled="!chapter.nextChapterId"
          @click="goToChapter(chapter.nextChapterId)"
        >
          下一章
        </NButton>
      </NSpace>
    </div>
  </div>
</template>

<style scoped lang="scss">
.chapter-reader {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
}

.reader-header {
  text-align: center;
  margin-bottom: 40px;

  .novel-title {
    color: var(--text-secondary, #94a3b8);
    cursor: pointer;
    margin-bottom: 10px;

    &:hover {
      color: var(--color-primary, #3b82f6);
    }
  }

  h1 {
    font-size: 1.8rem;
  }
}

.reader-content {
  line-height: 2;
  font-size: 1.1rem;

  p {
    text-indent: 2em;
    margin-bottom: 1em;
  }
}

.reader-footer {
  margin-top: 60px;
  padding-top: 20px;
  border-top: 1px solid var(--border-color, #e5e7eb);
}
</style>
