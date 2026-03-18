<script setup lang="ts">
import { ref, onMounted, watch, h } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  NSpace,
  NInput,
  NTag,
  NPagination,
  NCard,
  NSkeleton,
  useMessage
} from 'naive-ui'
import { blogApi } from '@/api/blog'
import type { Article, Category, Tag } from '@/types'

const route = useRoute()
const router = useRouter()
const message = useMessage()

// 数据
const articles = ref<Article[]>([])
const categories = ref<Category[]>([])
const tags = ref<Tag[]>([])
const loading = ref(false)
const total = ref(0)

// 查询参数
const keyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)

// 加载文章
async function loadArticles() {
  loading.value = true
  try {
    const res = await blogApi.getArticles({
      page: currentPage.value,
      size: pageSize.value,
      keyword: keyword.value || undefined
    })
    articles.value = res.list
    total.value = res.total
  } catch {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 搜索
function handleSearch() {
  currentPage.value = 1
  loadArticles()
}

// 分页
function handlePageChange(page: number) {
  currentPage.value = page
  loadArticles()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 查看详情
function viewArticle(article: Article) {
  router.push(`/blog/${article.slug}`)
}

// 格式化日期
function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

// 阅读时间估算
function readingTime(content: string) {
  const words = content?.length || 0
  return Math.max(1, Math.ceil(words / 500))
}

onMounted(loadArticles)

// 监听路由参数
watch(() => route.query, (query) => {
  if (query.keyword) {
    keyword.value = query.keyword as string
    handleSearch()
  }
}, { immediate: true })
</script>

<template>
  <div class="blog-list">
    <div class="blog-header">
      <h1>博客</h1>
      <p class="subtitle">记录技术成长，分享编程心得</p>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <NInput
        v-model:value="keyword"
        placeholder="搜索文章..."
        clearable
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <span class="search-icon">🔍</span>
        </template>
      </NInput>
    </div>

    <!-- 文章列表 -->
    <div class="articles">
      <template v-if="loading">
        <NCard v-for="i in 3" :key="i" class="article-card">
          <NSkeleton text :repeat="2" />
          <NSkeleton text style="width: 60%" />
        </NCard>
      </template>

      <template v-else-if="articles.length">
        <NCard
          v-for="article in articles"
          :key="article.id"
          class="article-card"
          hoverable
          @click="viewArticle(article)"
        >
          <div class="article-content">
            <div v-if="article.coverImage" class="article-cover">
              <img :src="article.coverImage" :alt="article.title" />
            </div>
            <div class="article-body">
              <div class="article-meta">
                <span class="date">{{ formatDate(article.publishedAt) }}</span>
                <span class="reading-time">{{ readingTime(article.content) }} 分钟阅读</span>
                <span class="views">{{ article.viewCount }} 次浏览</span>
              </div>
              <h2 class="article-title">
                {{ article.title }}
                <NTag v-if="article.isTop" type="warning" size="small">置顶</NTag>
              </h2>
              <p class="article-summary">{{ article.summary }}</p>
              <div class="article-footer">
                <NSpace size="small">
                  <NTag
                    v-for="tag in article.tags"
                    :key="tag.id"
                    size="small"
                    :style="{ borderColor: tag.color, color: tag.color }"
                    round
                  >
                    {{ tag.name }}
                  </NTag>
                </NSpace>
                <span class="read-more">阅读全文 →</span>
              </div>
            </div>
          </div>
        </NCard>
      </template>

      <div v-else class="empty-state">
        <p>暂无文章</p>
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
.blog-list {
  max-width: 900px;
  margin: 0 auto;
  padding: 40px 20px;
}

.blog-header {
  text-align: center;
  margin-bottom: 40px;

  h1 {
    font-size: 2.5rem;
    margin-bottom: 10px;
  }

  .subtitle {
    color: var(--text-secondary, #94a3b8);
    font-size: 1.1rem;
  }
}

.search-bar {
  margin-bottom: 30px;

  .search-icon {
    margin-right: 8px;
  }
}

.article-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
  }
}

.article-content {
  display: flex;
  gap: 20px;

  @media (max-width: 768px) {
    flex-direction: column;
  }
}

.article-cover {
  flex-shrink: 0;
  width: 200px;
  height: 140px;
  border-radius: 8px;
  overflow: hidden;

  @media (max-width: 768px) {
    width: 100%;
    height: 200px;
  }

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.article-body {
  flex: 1;
  min-width: 0;
}

.article-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 8px;
  font-size: 0.875rem;
  color: var(--text-secondary, #94a3b8);
}

.article-title {
  font-size: 1.5rem;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.article-summary {
  color: var(--text-secondary, #94a3b8);
  margin-bottom: 15px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.read-more {
  color: var(--color-primary, #3b82f6);
  font-size: 0.875rem;
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
