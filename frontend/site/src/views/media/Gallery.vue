<template>
  <div class="gallery">
    <div class="page-header">
      <h1>图库</h1>
      <p class="subtitle">探索精彩瞬间</p>
    </div>

    <n-spin :show="loading">
      <div v-if="albums.length === 0" class="empty">
        <n-empty description="暂无相册" />
      </div>
      <div v-else class="album-grid">
        <div
          v-for="album in albums"
          :key="album.id"
          class="album-card"
          @click="goToAlbum(album.slug)"
        >
          <div class="album-cover">
            <img :src="album.coverImage || defaultCover" :alt="album.name" />
            <div class="album-count">{{ album.imageCount }} 张</div>
          </div>
          <div class="album-info">
            <h3>{{ album.name }}</h3>
            <p v-if="album.description">{{ album.description }}</p>
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
import { albumApi, type Album } from '@/api/media'

const router = useRouter()
const message = useMessage()

const loading = ref(false)
const albums = ref<Album[]>([])
const defaultCover = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="300" height="200" viewBox="0 0 300 200"%3E%3Crect fill="%231a1a24" width="300" height="200"/%3E%3Ctext fill="%23666" font-family="sans-serif" font-size="14" x="50%25" y="50%25" text-anchor="middle"%3E暂无封面%3C/text%3E%3C/svg%3E'

onMounted(loadData)

async function loadData() {
  loading.value = true
  try {
    albums.value = await albumApi.getList()
  } catch (e: any) {
    message.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function goToAlbum(slug: string) {
  router.push(`/gallery/${slug}`)
}
</script>

<style scoped lang="scss">
.gallery {
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

.album-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

.album-card {
  background: var(--bg-tertiary, #1a1a24);
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
  }
}

.album-cover {
  position: relative;
  aspect-ratio: 3/2;
  overflow: hidden;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s;
  }

  .album-card:hover & img {
    transform: scale(1.05);
  }

  .album-count {
    position: absolute;
    bottom: 8px;
    right: 8px;
    background: rgba(0, 0, 0, 0.6);
    color: white;
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 12px;
  }
}

.album-info {
  padding: 16px;

  h3 {
    margin: 0 0 8px;
    font-size: 18px;
  }

  p {
    margin: 0;
    color: var(--text-secondary, #94a3b8);
    font-size: 14px;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
}
</style>
