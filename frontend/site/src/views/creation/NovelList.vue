<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { NSpace, NTag, NPagination, NCard, NSkeleton, useMessage } from 'naive-ui'
import { creationApi } from '@/api/creation'
import type { Novel, NovelCategory } from '@/types'

const router = useRouter()
const message = useMessage()

const novels = ref<Novel[]>([])
const categories = ref<NovelCategory[]>([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(12)
const selectedCategory = ref<number | undefined>(undefined)

const categoryOptions = computed(() => [
  { id: undefined, name: '全部' },
  ...categories.value
])

async function loadData() {
  loading.value = true
  try {
    const [novelRes, catRes] = await Promise.all([
      creationApi.getNovels({
        page: currentPage.value,
        size: pageSize.value,
        categoryId: selectedCategory.value
      }),
      categories.value.length ? Promise.resolve(categories.value) : creationApi.getNovelCategories()
    ])
    novels.value = novelRes.list
    total.value = novelRes.total
    if (catRes.length && !categories.value.length) {
      categories.value = catRes as NovelCategory[]
    }
  } catch {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

function handleCategorySelect(categoryId: number | undefined) {
  selectedCategory.value = categoryId
  currentPage.value = 1
  loadData()
}

function handlePageChange(page: number) {
  currentPage.value = page
  loadData()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

function viewNovel(novel: Novel) {
  router.push(`/creation/novels/${novel.slug}`)
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
  <div class="novel-list">
    <div class="page-header">
      <h1>小说</h1>
      <p class="subtitle">原创文学，精彩故事</p>
    </div>

    <!-- 分类筛选 -->
    <div class="category-filter">
      <NSpace size="small">
        <NTag
          v-for="cat in categoryOptions"
          :key="cat.id ?? 'all'"
          :type="selectedCategory === cat.id ? 'primary' : 'default'"
          :bordered="false"
          round
          clickable
          @click="handleCategorySelect(cat.id)"
        >
          {{ cat.name }}
        </NTag>
      </NSpace>
    </div>

    <!-- 小说列表 -->
    <div class="novels-grid">
      <template v-if="loading">
        <NCard v-for="i in 6" :key="i" class="novel-card">
          <NSkeleton text :repeat="2" />
          <NSkeleton text style="width: 60%" />
        </NCard>
      </template>

      <template v-else-if="novels.length">
        <NCard
          v-for="novel in novels"
          :key="novel.id"
          class="novel-card"
          hoverable
          @click="viewNovel(novel)"
        >
          <div class="novel-cover" v-if="novel.coverImage">
            <img :src="novel.coverImage" :alt="novel.title" />
          </div>
          <div class="novel-cover placeholder" v-else>
            <span>小说</span>
          </div>
          <div class="novel-info">
            <h3 class="novel-title">{{ novel.title }}</h3>
            <div class="novel-meta">
              <span class="author">{{ novel.author || '佚名' }}</span>
              <NTag size="small" :type="novel.status === 'COMPLETED' ? 'success' : 'info'">
                {{ getStatusLabel(novel.status) }}
              </NTag>
            </div>
            <p class="novel-summary">{{ novel.summary || '暂无简介' }}</p>
            <div class="novel-stats">
              <span>{{ formatNumber(novel.wordCount) }} 字</span>
              <span>{{ novel.chapterCount }} 章</span>
              <span>{{ formatNumber(novel.viewCount) }} 阅读</span>
            </div>
          </div>
        </NCard>
      </template>

      <div v-else class="empty-state">
        <p>暂无小说</p>
      </div>
    </div>

    <!-- 分页 -->
    <div v-if="total > pageSize" class="pagination">
      <NPagination
        :page="currentPage"
        :page-size="pageSize"
        :item-count="total"
        @update:page="handlePageChange"
      />
    </div>
  </div>
</template>

<style scoped lang="scss">
.novel-list {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;

  h1 {
    font-size: 2.5rem;
    margin-bottom: 10px;
  }

  .subtitle {
    color: var(--text-secondary, #94a3b8);
    font-size: 1.1rem;
  }
}

.category-filter {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.novels-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

.novel-card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  overflow: hidden;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);

    .novel-cover img {
      transform: scale(1.05);
    }
  }

  :deep(.n-card__content) {
    padding: 0;
  }
}

.novel-cover {
  height: 180px;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s;
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

.novel-info {
  padding: 16px;
}

.novel-title {
  font-size: 1.2rem;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.novel-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;

  .author {
    color: var(--text-secondary, #94a3b8);
    font-size: 0.875rem;
  }
}

.novel-summary {
  color: var(--text-secondary, #94a3b8);
  font-size: 0.875rem;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 12px;
}

.novel-stats {
  display: flex;
  gap: 16px;
  font-size: 0.8rem;
  color: var(--text-secondary, #94a3b8);
}

.empty-state {
  grid-column: 1 / -1;
  text-align: center;
  padding: 60px 20px;
  color: var(--text-secondary, #94a3b8);
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}
</style>
