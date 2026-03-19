<template>
  <div class="audio-list">
    <div class="page-header">
      <h1>音频</h1>
      <p class="subtitle">聆听美妙旋律</p>
    </div>

    <n-spin :show="loading">
      <div v-if="audios.length === 0" class="empty">
        <n-empty description="暂无音频" />
      </div>
      <div v-else class="audio-list-content">
        <div
          v-for="audio in audios"
          :key="audio.id"
          class="audio-item"
        >
          <div class="audio-cover">
            <img :src="audio.coverImage || defaultCover" :alt="audio.title" />
          </div>
          <div class="audio-info">
            <h3>{{ audio.title }}</h3>
            <p v-if="audio.description">{{ audio.description }}</p>
          </div>
          <div class="audio-player">
            <audio :src="audio.url" controls />
          </div>
        </div>
      </div>
    </n-spin>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useMessage } from 'naive-ui'
import { audioApi, type Audio } from '@/api/media'

const message = useMessage()

const loading = ref(false)
const audios = ref<Audio[]>([])
const defaultCover = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="100" height="100" viewBox="0 0 100 100"%3E%3Crect fill="%231a1a24" width="100" height="100"/%3E%3Ctext fill="%23666" font-family="sans-serif" font-size="12" x="50%25" y="50%25" text-anchor="middle"%3E音频%3C/text%3E%3C/svg%3E'

onMounted(loadData)

async function loadData() {
  loading.value = true
  try {
    audios.value = await audioApi.getList()
  } catch (e: any) {
    message.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.audio-list {
  max-width: 800px;
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

.audio-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background: var(--bg-tertiary, #1a1a24);
  border-radius: 12px;
  margin-bottom: 12px;
}

.audio-cover {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.audio-info {
  flex: 1;
  min-width: 0;

  h3 {
    margin: 0 0 4px;
    font-size: 16px;
  }

  p {
    margin: 0;
    font-size: 14px;
    color: var(--text-secondary, #94a3b8);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}

.audio-player {
  flex-shrink: 0;

  audio {
    height: 40px;
  }
}
</style>
