<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  NSpace,
  NTag,
  NCard,
  NButton,
  NInput,
  NSkeleton,
  useMessage
} from 'naive-ui'
import { blogApi } from '@/api/blog'
import type { Article, Comment, CommentRequest } from '@/types'

const route = useRoute()
const router = useRouter()
const message = useMessage()

// 数据
const article = ref<Article | null>(null)
const comments = ref<Comment[]>([])
const relatedArticles = ref<Article[]>([])
const loading = ref(true)

// 评论表单
const commentForm = ref<CommentRequest>({
  articleId: 0,
  nickname: '',
  email: '',
  website: '',
  content: ''
})
const submitting = ref(false)
const replyTo = ref<Comment | null>(null)

// 获取 slug
const slug = computed(() => route.params.slug as string)

// 加载文章
async function loadArticle() {
  loading.value = true
  try {
    article.value = await blogApi.getArticleBySlug(slug.value)
    commentForm.value.articleId = article.value.id

    // 加载评论和相关文章
    if (article.value.allowComment) {
      comments.value = await blogApi.getComments(article.value.id)
    }
    relatedArticles.value = await blogApi.getRelatedArticles(article.value.id, 4)
  } catch {
    message.error('文章不存在')
    router.push('/blog')
  } finally {
    loading.value = false
  }
}

// 提交评论
async function submitComment() {
  if (!commentForm.value.nickname || !commentForm.value.content) {
    message.warning('请填写昵称和评论内容')
    return
  }

  submitting.value = true
  try {
    await blogApi.submitComment(commentForm.value)
    message.success('评论已提交，等待审核')
    commentForm.value.nickname = ''
    commentForm.value.email = ''
    commentForm.value.website = ''
    commentForm.value.content = ''
    replyTo.value = null
  } catch {
    message.error('评论提交失败')
  } finally {
    submitting.value = false
  }
}

// 回复评论
function replyComment(comment: Comment) {
  replyTo.value = comment
  commentForm.value.parentId = comment.id
  window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' })
}

// 点赞评论
async function likeComment(id: number) {
  try {
    await blogApi.likeComment(id)
    message.success('已点赞')
  } catch {
    message.error('点赞失败')
  }
}

// 格式化日期
function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

// 阅读时间
function readingTime(content: string) {
  const words = content?.length || 0
  return Math.max(1, Math.ceil(words / 500))
}

// 查看相关文章
function viewArticle(a: Article) {
  router.push(`/blog/${a.slug}`)
  window.scrollTo({ top: 0 })
}

onMounted(loadArticle)
</script>

<template>
  <div class="blog-detail">
    <template v-if="loading">
      <div class="loading">
        <NSkeleton text :repeat="3" />
        <NSkeleton text style="width: 60%" />
      </div>
    </template>

    <template v-else-if="article">
      <!-- 文章头部 -->
      <article class="article">
        <header class="article-header">
          <h1 class="article-title">{{ article.title }}</h1>
          <div class="article-meta">
            <span class="date">{{ formatDate(article.publishedAt) }}</span>
            <span class="reading-time">{{ readingTime(article.content) }} 分钟阅读</span>
            <span class="views">{{ article.viewCount }} 次浏览</span>
          </div>
          <NSpace v-if="article.tags?.length" class="article-tags" size="small">
            <NTag
              v-for="tag in article.tags"
              :key="tag.id"
              :style="{ borderColor: tag.color, color: tag.color }"
              round
            >
              {{ tag.name }}
            </NTag>
          </NSpace>
        </header>

        <!-- 封面图 -->
        <div v-if="article.coverImage" class="article-cover">
          <img :src="article.coverImage" :alt="article.title" />
        </div>

        <!-- 文章内容 -->
        <div class="article-content" v-html="article.content"></div>

        <!-- 分类信息 -->
        <footer class="article-footer">
          <div v-if="article.category" class="category">
            分类：<NTag>{{ article.category.name }}</NTag>
          </div>
        </footer>
      </article>

      <!-- 相关文章 -->
      <section v-if="relatedArticles.length" class="related-articles">
        <h2>相关文章</h2>
        <div class="related-list">
          <NCard
            v-for="a in relatedArticles"
            :key="a.id"
            class="related-card"
            hoverable
            @click="viewArticle(a)"
          >
            <h3>{{ a.title }}</h3>
            <p>{{ a.summary }}</p>
          </NCard>
        </div>
      </section>

      <!-- 评论区 -->
      <section v-if="article.allowComment" class="comments-section">
        <h2>评论区</h2>

        <!-- 回复提示 -->
        <div v-if="replyTo" class="reply-hint">
          回复 <strong>{{ replyTo.nickname }}</strong>
          <NButton text type="primary" @click="replyTo = null; commentForm.parentId = undefined">
            取消
          </NButton>
        </div>

        <!-- 评论表单 -->
        <NCard class="comment-form">
          <div class="form-row">
            <NInput
              v-model:value="commentForm.nickname"
              placeholder="昵称 *"
              style="width: 200px"
            />
            <NInput
              v-model:value="commentForm.email"
              placeholder="邮箱（不会公开）"
              style="width: 200px"
            />
          </div>
          <NInput
            v-model:value="commentForm.website"
            placeholder="网站（选填）"
            style="margin-bottom: 12px"
          />
          <NInput
            v-model:value="commentForm.content"
            type="textarea"
            :rows="4"
            placeholder="写下你的评论..."
          />
          <div class="form-actions">
            <NButton type="primary" :loading="submitting" @click="submitComment">
              发表评论
            </NButton>
          </div>
        </NCard>

        <!-- 评论列表 -->
        <div class="comments-list">
          <template v-if="comments.length">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <div class="comment-header">
                <span class="nickname">{{ comment.nickname }}</span>
                <span class="date">{{ formatDate(comment.createdAt) }}</span>
              </div>
              <p class="comment-content">{{ comment.content }}</p>
              <div class="comment-actions">
                <NButton text size="small" @click="likeComment(comment.id)">
                  👍 {{ comment.likeCount }}
                </NButton>
                <NButton text size="small" @click="replyComment(comment)">
                  回复
                </NButton>
              </div>
              <!-- 子评论 -->
              <div v-if="comment.children?.length" class="comment-children">
                <div v-for="child in comment.children" :key="child.id" class="comment-item">
                  <div class="comment-header">
                    <span class="nickname">{{ child.nickname }}</span>
                    <span class="date">{{ formatDate(child.createdAt) }}</span>
                  </div>
                  <p class="comment-content">{{ child.content }}</p>
                </div>
              </div>
            </div>
          </template>
          <div v-else class="empty-comments">
            暂无评论，快来抢沙发吧！
          </div>
        </div>
      </section>
    </template>
  </div>
</template>

<style scoped lang="scss">
.blog-detail {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
}

.loading {
  padding: 40px 0;
}

.article-header {
  margin-bottom: 30px;
}

.article-title {
  font-size: 2.2rem;
  margin-bottom: 15px;
  line-height: 1.3;
}

.article-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
  color: var(--text-secondary, #94a3b8);
  font-size: 0.9rem;
}

.article-tags {
  margin-bottom: 10px;
}

.article-cover {
  margin-bottom: 30px;
  border-radius: 12px;
  overflow: hidden;

  img {
    width: 100%;
    height: auto;
  }
}

.article-content {
  font-size: 1.1rem;
  line-height: 1.8;
  color: var(--text-primary, #e2e8f0);

  :deep(h1), :deep(h2), :deep(h3) {
    margin-top: 2em;
    margin-bottom: 1em;
  }

  :deep(p) {
    margin-bottom: 1.2em;
  }

  :deep(pre) {
    background: var(--bg-secondary, #1e1e2e);
    padding: 16px;
    border-radius: 8px;
    overflow-x: auto;
  }

  :deep(code) {
    background: var(--bg-secondary, #1e1e2e);
    padding: 2px 6px;
    border-radius: 4px;
    font-size: 0.9em;
  }

  :deep(img) {
    max-width: 100%;
    border-radius: 8px;
  }

  :deep(blockquote) {
    border-left: 4px solid var(--color-primary, #3b82f6);
    padding-left: 16px;
    margin: 1.5em 0;
    color: var(--text-secondary, #94a3b8);
  }
}

.article-footer {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid var(--border-primary, #2d2d3a);
}

.related-articles {
  margin-top: 60px;

  h2 {
    margin-bottom: 20px;
  }
}

.related-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 16px;
}

.related-card {
  cursor: pointer;
  transition: transform 0.2s;

  &:hover {
    transform: translateY(-2px);
  }

  h3 {
    font-size: 1rem;
    margin-bottom: 8px;
  }

  p {
    font-size: 0.875rem;
    color: var(--text-secondary, #94a3b8);
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
}

.comments-section {
  margin-top: 60px;

  h2 {
    margin-bottom: 20px;
  }
}

.reply-hint {
  margin-bottom: 16px;
  padding: 10px 16px;
  background: var(--bg-secondary, #1e1e2e);
  border-radius: 8px;
}

.comment-form {
  margin-bottom: 30px;
}

.form-row {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;

  @media (max-width: 500px) {
    flex-direction: column;
  }
}

.form-actions {
  margin-top: 12px;
  text-align: right;
}

.comments-list {
  .comment-item {
    padding: 16px 0;
    border-bottom: 1px solid var(--border-primary, #2d2d3a);
  }

  .comment-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 8px;

    .nickname {
      font-weight: 500;
    }

    .date {
      color: var(--text-secondary, #94a3b8);
      font-size: 0.875rem;
    }
  }

  .comment-content {
    line-height: 1.6;
    margin-bottom: 8px;
  }

  .comment-actions {
    display: flex;
    gap: 12px;
  }

  .comment-children {
    margin-left: 24px;
    margin-top: 12px;
    padding-left: 16px;
    border-left: 2px solid var(--border-primary, #2d2d3a);
  }
}

.empty-comments {
  text-align: center;
  padding: 40px;
  color: var(--text-secondary, #94a3b8);
}
</style>
