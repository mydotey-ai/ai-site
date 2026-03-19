<template>
  <div class="video-list">
    <div class="page-header">
      <h1>视频</h1>
      <p class="subtitle">精彩视频内容</p>
    </div>

    <n-spin :show="loading">
      <div v-if="videos.length === 0" class="empty">
        <n-empty description="暂无视频" />
      </div>
      <div v-else class="video-grid">
        <div
          v-for="video in videos"
          :key="video.id"
          class="video-card"
          @click="goToVideo(video.id)"
        >
          <div class="video-cover">
            <img :src="video.coverImage || defaultCover" :alt="video.title" />
            <div class="play-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="white"><polygon points="5 3 19 12 5 21 5 3"/></svg>
            </div>
          </div>
          <div class="video-info">
            <h3>{{ video.title }}</h3>
          </div>
        </div>
      </div>
    </n-spin>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import { videoApi, type Video } from '@/api/media'

const router = useRouter()
const message = useMessage()

const loading = ref(false)
const videos = ref<Video[]>([])
const defaultCover = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="300" height="200" viewBox="0 0 300 200"%3E%3Crect fill="%231a1a24" width="300" height="200"/%3E%3Ctext fill="%23666" font-family="sans-serif" font-size="14" x="50%25" y="50%25" text-anchor="middle"%3E视频%3C/text%3E%3C/svg%3E'

onMounted(loadData)

async function loadData() {
  loading.value = true
  try {
    videos.value = await videoApi.getList()
  } catch (e: any) {
    message.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function goToVideo(id: number) {
  router.push(`/media/videos/${id}`)
}
</script>

<style scoped lang="scss">
.video-list {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;

  h1 {
    font-size: 36px;
    margin: 0 0 8px;
    background: linear-gradient(135deg, #3b82f6, #60a5fa);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }

  .subtitle {
    color: var(--text-secondary, #94a3b8);
    margin: 0;
  }
}

.empty {
  padding: 80px 0;
}

.video-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.video-card {
  background: var(--bg-tertiary, #1a1a24);
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);

    .play-icon {
      opacity: 1;
    }
  }
}

.video-cover {
  position: relative;
  aspect-ratio: 16/9;
  overflow: hidden;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .play-icon {
    position: absolute;
    inset: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(0, 0, 0, 0.3);
    opacity: 0;
    transition: opacity 0.2s;
  }
}

.video-info {
  padding: 16px;

  h3 {
    margin: 0;
    font-size: 16px;
  }
}
</style>
