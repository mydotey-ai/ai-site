<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { NSpace, NTag, NPagination, NCard, NSkeleton, useMessage } from 'naive-ui'
import { creationApi } from '@/api/creation'
import type { Essay, EssayCategory } from '@/types'

const router = useRouter()
const message = useMessage()

const essayList = ref<Essay[]>([])
const categories = ref<EssayCategory[]>([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const selectedCategory = ref<number | undefined>(undefined)

const categoryOptions = computed(() => [
  { id: undefined, name: '全部' },
  ...categories.value
])

async function loadData() {
  loading.value = true
  try {
    const [essayRes, catRes] = await Promise.all([
      creationApi.getEssayList({
        page: currentPage.value,
        size: pageSize.value,
        categoryId: selectedCategory.value
      }),
      categories.value.length ? Promise.resolve(categories.value) : creationApi.getEssayCategories()
    ])
    essayList.value = essayRes.list
    total.value = essayRes.total
    if (catRes.length && !categories.value.length) {
      categories.value = catRes as EssayCategory[]
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

function viewEssay(essay: Essay) {
  router.push(`/creation/essays/${essay.slug}`)
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
  <div class="essay-list">
    <div class="page-header">
      <h1>散文</h1>
      <p class="subtitle">随笔杂谈，感悟生活</p>
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

    <!-- 散文列表 -->
    <div class="essays">
      <template v-if="loading">
        <NCard v-for="i in 3" :key="i" class="essay-card">
          <NSkeleton text :repeat="2" />
          <NSkeleton text style="width: 60%" />
        </NCard>
      </template>

      <template v-else-if="essayList.length">
        <NCard
          v-for="essay in essayList"
          :key="essay.id"
          class="essay-card"
          hoverable
          @click="viewEssay(essay)"
        >
          <h2 class="essay-title">{{ essay.title }}</h2>
          <div class="essay-meta">
            <span class="author">{{ essay.author || '佚名' }}</span>
            <span class="date">{{ formatDate(essay.createdAt) }}</span>
            <span v-if="essay.category" class="category">{{ essay.category.name }}</span>
          </div>
          <p class="essay-summary">{{ essay.summary || '暂无摘要' }}</p>
          <div class="essay-footer">
            <span class="views">{{ essay.viewCount }} 阅读</span>
            <span class="read-more">阅读全文 →</span>
          </div>
        </NCard>
      </template>

      <div v-else class="empty-state">
        <p>暂无散文</p>
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
.essay-list {
  max-width: 900px;
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

.essay-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);

    .read-more {
      opacity: 1;
    }
  }
}

.essay-title {
  font-size: 1.5rem;
  margin-bottom: 10px;
}

.essay-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 12px;
  font-size: 0.875rem;
  color: var(--text-secondary, #94a3b8);

  .category {
    color: var(--color-primary, #3b82f6);
  }
}

.essay-summary {
  color: var(--text-secondary, #94a3b8);
  line-height: 1.6;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.essay-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .read-more {
    color: var(--color-primary, #3b82f6);
    font-size: 0.875rem;
    opacity: 0.7;
    transition: opacity 0.2s;
  }
}

.empty-state {
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
