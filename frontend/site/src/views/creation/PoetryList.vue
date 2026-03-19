<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { NSpace, NTag, NPagination, NCard, NSkeleton, useMessage } from 'naive-ui'
import { creationApi } from '@/api/creation'
import type { Poetry, PoetryCategory } from '@/types'

const router = useRouter()
const message = useMessage()

const poetryList = ref<Poetry[]>([])
const categories = ref<PoetryCategory[]>([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(20)
const selectedCategory = ref<number | undefined>(undefined)

const categoryOptions = computed(() => [
  { id: undefined, name: '全部' },
  ...categories.value
])

async function loadData() {
  loading.value = true
  try {
    const [poetryRes, catRes] = await Promise.all([
      creationApi.getPoetryList({
        page: currentPage.value,
        size: pageSize.value,
        categoryId: selectedCategory.value
      }),
      categories.value.length ? Promise.resolve(categories.value) : creationApi.getPoetryCategories()
    ])
    poetryList.value = poetryRes.list
    total.value = poetryRes.total
    if (catRes.length && !categories.value.length) {
      categories.value = catRes as PoetryCategory[]
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

function viewPoetry(poetry: Poetry) {
  router.push(`/creation/poetry/${poetry.slug}`)
}

function getExcerpt(content: string, length = 50) {
  if (!content) return ''
  return content.length > length ? content.slice(0, length) + '...' : content
}

onMounted(loadData)
</script>

<template>
  <div class="poetry-list">
    <div class="page-header">
      <h1>诗歌</h1>
      <p class="subtitle">诗意栖居，心灵之韵</p>
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

    <!-- 诗歌列表 -->
    <div class="poetry-grid">
      <template v-if="loading">
        <NCard v-for="i in 6" :key="i" class="poetry-card">
          <NSkeleton text :repeat="3" />
        </NCard>
      </template>

      <template v-else-if="poetryList.length">
        <NCard
          v-for="poetry in poetryList"
          :key="poetry.id"
          class="poetry-card"
          hoverable
          @click="viewPoetry(poetry)"
        >
          <h3 class="poetry-title">{{ poetry.title }}</h3>
          <p class="poetry-author">{{ poetry.author || '佚名' }}</p>
          <p class="poetry-excerpt">{{ getExcerpt(poetry.content) }}</p>
          <div class="poetry-footer">
            <span v-if="poetry.category" class="category">{{ poetry.category.name }}</span>
            <span class="views">{{ poetry.viewCount }} 阅读</span>
          </div>
        </NCard>
      </template>

      <div v-else class="empty-state">
        <p>暂无诗歌</p>
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
.poetry-list {
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

.poetry-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.poetry-card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.12);
  }
}

.poetry-title {
  font-size: 1.2rem;
  margin-bottom: 8px;
}

.poetry-author {
  color: var(--text-secondary, #94a3b8);
  font-size: 0.9rem;
  margin-bottom: 12px;
}

.poetry-excerpt {
  color: var(--text-secondary, #94a3b8);
  font-size: 0.9rem;
  line-height: 1.6;
  margin-bottom: 12px;
}

.poetry-footer {
  display: flex;
  justify-content: space-between;
  font-size: 0.8rem;
  color: var(--text-secondary, #94a3b8);

  .category {
    color: var(--color-primary, #3b82f6);
  }
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
