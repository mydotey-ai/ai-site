<template>
  <div class="album-detail">
    <div class="page-header">
      <h1>{{ album?.name }}</h1>
      <p v-if="album?.description">{{ album.description }}</p>
      <n-button text @click="router.back()">
        <template #icon>
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M19 12H5M12 19l-7-7 7-7"/></svg>
        </template>
        返回
      </n-button>
    </div>

    <n-spin :show="loading">
      <div v-if="images.length === 0" class="empty">
        <n-empty description="暂无图片" />
      </div>
      <div v-else class="waterfall-grid">
        <div
          v-for="image in images"
          :key="image.id"
          class="waterfall-item"
          @click="openLightbox(image)"
        >
          <img :src="image.thumbnailUrl || image.url" :alt="image.title" loading="lazy" />
        </div>
      </div>
    </n-spin>

    <div v-if="total > images.length" class="load-more">
      <n-button @click="loadMore" :loading="loading">加载更多</n-button>
    </div>

    <!-- 图片灯箱 -->
    <n-modal v-model:show="showLightbox" preset="card" style="max-width: 95vw; max-height: 95vh">
      <img :src="currentImage?.url" :alt="currentImage?.title" style="max-width: 100%; max-height: 85vh" />
      <div class="lightbox-info">
        <span>{{ currentImage?.title }}</span>
      </div>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import { albumApi, type Album, type Image } from '@/api/media'

const route = useRoute()
const router = useRouter()
const message = useMessage()

const loading = ref(false)
const album = ref<Album | null>(null)
const images = ref<Image[]>([])
const total = ref(0)
const page = ref(1)
const showLightbox = ref(false)
const currentImage = ref<Image | null>(null)

onMounted(loadData)

async function loadData() {
  const slug = route.params.slug as string
  if (!slug) return

  loading.value = true
  try {
    album.value = await albumApi.getBySlug(slug, page.value, 20)
    images.value = album.value.images || []
    total.value = album.value.imageCount
  } catch (e: any) {
    message.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

async function loadMore() {
  page.value++
  const slug = route.params.slug as string
  loading.value = true
  try {
    const res = await albumApi.getBySlug(slug, page.value, 20)
    images.value.push(...(res.images || []))
  } catch (e: any) {
    message.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function openLightbox(image: Image) {
  currentImage.value = image
  showLightbox.value = true
}
</script>

<style scoped lang="scss">
.album-detail {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.page-header {
  margin-bottom: 30px;

  h1 {
    font-size: 32px;
    margin: 0 0 8px;
  }

  p {
    color: var(--text-secondary, #94a3b8);
    margin: 0 0 16px;
  }
}

.empty {
  padding: 60px 0;
}

.waterfall-grid {
  column-count: 4;
  column-gap: 16px;

  @media (max-width: 1200px) { column-count: 3; }
  @media (max-width: 768px) { column-count: 2; }
}

.waterfall-item {
  break-inside: avoid;
  margin-bottom: 16px;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s;

  &:hover {
    transform: scale(1.02);
  }

  img {
    width: 100%;
    display: block;
  }
}

.load-more {
  text-align: center;
  margin-top: 30px;
}

.lightbox-info {
  text-align: center;
  padding: 12px 0;
  color: var(--text-secondary, #94a3b8);
}
</style>
