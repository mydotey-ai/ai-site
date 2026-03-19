<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NTag, NSkeleton, NButton, useMessage } from 'naive-ui'
import { portfolioApi } from '@/api/portfolio'
import type { Project } from '@/types'

const route = useRoute()
const router = useRouter()
const message = useMessage()

// 数据
const project = ref<Project | null>(null)
const loading = ref(true)

// 获取链接
function getLinkByType(type: string) {
  return project.value?.links?.find(l => l.type === type)
}

// 获取状态文本
function getStatusText(status: string) {
  const statusMap: Record<string, string> = {
    DEVELOPING: '开发中',
    RELEASED: '已发布',
    ARCHIVED: '已归档'
  }
  return statusMap[status] || status
}

// 格式化日期
function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

// 返回列表
function goBack() {
  router.push('/portfolio')
}

// 加载项目
async function loadProject() {
  loading.value = true
  try {
    const slugOrId = route.params.id as string
    if (/^\d+$/.test(slugOrId)) {
      project.value = await portfolioApi.getProjectById(Number(slugOrId))
    } else {
      project.value = await portfolioApi.getProjectBySlug(slugOrId)
    }
  } catch {
    message.error('项目不存在')
    router.push('/portfolio')
  } finally {
    loading.value = false
  }
}

onMounted(loadProject)
</script>

<template>
  <div class="portfolio-detail">
    <template v-if="loading">
      <div class="loading-skeleton">
        <NSkeleton text :repeat="2" />
        <NSkeleton text style="width: 60%" />
      </div>
    </template>

    <template v-else-if="project">
      <!-- 封面图 -->
      <div class="project-cover" v-if="project.coverImage">
        <img :src="project.coverImage" :alt="project.name" />
      </div>

      <!-- 项目信息 -->
      <div class="project-info">
        <div class="project-header">
          <h1 class="project-name">{{ project.name }}</h1>
          <div class="project-meta">
            <NTag :type="project.status === 'RELEASED' ? 'success' : 'default'" size="small">
              {{ getStatusText(project.status) }}
            </NTag>
            <span class="meta-item">创建于 {{ formatDate(project.createdAt) }}</span>
          </div>
        </div>

        <!-- 标签 -->
        <div class="project-tags" v-if="project.tags?.length">
          <NTag
            v-for="tag in project.tags"
            :key="tag.id"
            :style="{ borderColor: tag.color, color: tag.color }"
            round
          >
            {{ tag.name }}
          </NTag>
        </div>

        <!-- 描述 -->
        <p class="project-description">{{ project.description }}</p>

        <!-- 链接 -->
        <div class="project-links" v-if="project.links?.length">
          <a
            v-for="link in project.links"
            :key="link.type"
            :href="link.url"
            target="_blank"
            class="link-btn"
          >
            {{ link.label }}
          </a>
        </div>

        <!-- 技术栈 -->
        <div class="project-tech" v-if="project.techStack?.length">
          <h3>技术栈</h3>
          <div class="tech-list">
            <span v-for="tech in project.techStack" :key="tech" class="tech-badge">
              {{ tech }}
            </span>
          </div>
        </div>

        <!-- 详细内容 -->
        <div class="project-content" v-if="project.content">
          <h3>项目详情</h3>
          <div class="content-body" v-html="project.content"></div>
        </div>
      </div>

      <!-- 返回按钮 -->
      <div class="back-btn">
        <NButton text @click="goBack">
          ← 返回作品集
        </NButton>
      </div>
    </template>
  </div>
</template>

<style scoped lang="scss">
.portfolio-detail {
  max-width: 900px;
  margin: 0 auto;
  padding: 40px 20px;
}

.loading-skeleton {
  padding: 40px;
}

.project-cover {
  width: 100%;
  aspect-ratio: 16 / 9;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 30px;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.project-info {
  background: var(--card-bg, #1e293b);
  border-radius: 12px;
  padding: 30px;
}

.project-header {
  margin-bottom: 20px;
}

.project-name {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 12px;
}

.project-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.meta-item {
  color: var(--text-secondary, #94a3b8);
  font-size: 0.875rem;
}

.project-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 20px;
}

.project-description {
  font-size: 1.1rem;
  line-height: 1.6;
  color: var(--text-secondary, #94a3b8);
  margin-bottom: 24px;
}

.project-links {
  display: flex;
  gap: 16px;
  margin-bottom: 30px;
}

.link-btn {
  display: inline-block;
  padding: 10px 24px;
  background: var(--color-primary, #3b82f6);
  color: white;
  border-radius: 8px;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.2s;

  &:hover {
    background: var(--color-primary-hover, #2563eb);
    transform: translateY(-2px);
  }
}

.project-tech {
  margin-bottom: 30px;

  h3 {
    font-size: 1.25rem;
    margin-bottom: 16px;
  }
}

.tech-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.tech-badge {
  display: inline-block;
  padding: 6px 16px;
  background: var(--bg-secondary, #0f172a);
  color: var(--color-primary, #3b82f6);
  border-radius: 16px;
  font-size: 0.875rem;
}

.project-content {
  h3 {
    font-size: 1.25rem;
    margin-bottom: 16px;
  }
}

.content-body {
  line-height: 1.8;
  color: var(--text-primary, #f1f5f9);
}

.back-btn {
  margin-top: 30px;
}

@media (max-width: 768px) {
  .project-name {
    font-size: 1.5rem;
  }

  .project-info {
    padding: 20px;
  }

  .project-links {
    flex-direction: column;
  }

  .link-btn {
    text-align: center;
  }
}
</style>
