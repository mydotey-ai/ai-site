<template>
  <div class="video-detail">
    <div class="video-player">
      <iframe
        v-if="video?.embedUrl"
        :src="video.embedUrl"
        frameborder="0"
        allowfullscreen
      />
      <video v-else-if="video?.url" :src="video.url" controls />
    </div>
    <div class="video-info">
      <h1>{{ video?.title }}</h1>
      <p v-if="video?.description">{{ video.description }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useMessage } from 'naive-ui'
import { videoApi, type Video } from '@/api/media'

const route = useRoute()
const message = useMessage()

const loading = ref(false)
const video = ref<Video | null>(null)

onMounted(loadData)

async function loadData() {
  const id = Number(route.params.id)
  if (!id) return

  loading.value = true
  try {
    video.value = await videoApi.getById(id)
  } catch (e: any) {
    message.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.video-detail {
  max-width: 900px;
  margin: 0 auto;
  padding: 40px 20px;
}

.video-player {
  aspect-ratio: 16/9;
  background: #000;
  border-radius: 12px;
  overflow: hidden;

  iframe, video {
    width: 100%;
    height: 100%;
  }
}

.video-info {
  margin-top: 24px;

  h1 {
    font-size: 24px;
    margin: 0 0 12px;
  }

  p {
    color: var(--text-secondary, #94a3b8);
    margin: 0;
  }
}
</style>
