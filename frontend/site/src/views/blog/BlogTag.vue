<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NTag, NSkeleton, useMessage } from 'naive-ui'
import { blogApi } from '@/api/blog'
import type { Article } from '@/types'

const route = useRoute()
const router = useRouter()
const message = useMessage()

// 数据
const articles = ref<Article[]>([])
const loading = ref(false)
const total = ref(0)

// 标签信息（从路由参数或文章中获取）
const tagName = ref('')

// 加载数据
async function loadData() {
  loading.value = true
  try {
    const res = await blogApi.getArticles({
      tagId: Number(route.params.id),
      page: 1,
      size: 20
    })
    articles.value = res.list
    total.value = res.total
    if (res.list.length && res.list[0].tags) {
      const tag = res.list[0].tags.find(t => t.id === Number(route.params.id))
      if (tag) tagName.value = tag.name
    }
  } catch {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 查看文章
function viewArticle(article: Article) {
  router.push(`/blog/${article.slug}`)
}

// 格式化日期
function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

onMounted(loadData)
</script>

<template>
  <div class="blog-tag">
    <div class="page-header">
      <h1>标签：{{ tagName || '文章' }}</h1>
      <p class="count">共 {{ total }} 篇文章</p>
    </div>

    <template v-if="loading">
      <NSkeleton text :repeat="3" />
    </template>

    <template v-else>
      <div class="article-list">
        <div
          v-for="article in articles"
          :key="article.id"
          class="article-item"
          @click="viewArticle(article)"
        >
          <div class="article-meta">
            <span class="date">{{ formatDate(article.publishedAt) }}</span>
            <span class="views">{{ article.viewCount }} 次浏览</span>
          </div>
          <h2 class="article-title">{{ article.title }}</h2>
          <p class="article-summary">{{ article.summary }}</p>
          <div v-if="article.tags?.length" class="article-tags">
            <NTag
              v-for="tag in article.tags"
              :key="tag.id"
              size="small"
              :style="{ borderColor: tag.color, color: tag.color }"
              round
            >
              {{ tag.name }}
            </NTag>
          </div>
        </div>
      </div>

      <div v-if="!articles.length" class="empty">
        该标签下暂无文章
      </div>
    </template>
  </div>
</template>

<style scoped lang="scss">
.blog-tag {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
}

.page-header {
  margin-bottom: 40px;

  h1 {
    margin-bottom: 10px;
  }

  .count {
    color: var(--text-secondary, #94a3b8);
  }
}

.article-item {
  padding: 20px 0;
  border-bottom: 1px solid var(--border-primary, #2d2d3a);
  cursor: pointer;
  transition: background 0.2s;

  &:hover {
    background: var(--bg-secondary, #1e1e2e);
    margin: 0 -20px;
    padding: 20px;
  }
}

.article-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 8px;
  font-size: 0.875rem;
  color: var(--text-secondary, #94a3b8);
}

.article-title {
  font-size: 1.25rem;
  margin-bottom: 8px;
}

.article-summary {
  color: var(--text-secondary, #94a3b8);
  margin-bottom: 10px;
  line-height: 1.6;
}

.article-tags {
  display: flex;
  gap: 8px;
  margin-top: 8px;
}

.empty {
  text-align: center;
  padding: 60px 20px;
  color: var(--text-secondary, #94a3b8);
}
</style>
