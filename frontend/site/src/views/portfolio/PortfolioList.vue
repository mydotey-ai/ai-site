<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { NSpace, NTag, NPagination, NCard, NSkeleton, useMessage } from 'naive-ui'
import { portfolioApi } from '@/api/portfolio'
import type { Project, ProjectTag } from '@/types'

const router = useRouter()
const message = useMessage()

// 数据
const projects = ref<Project[]>([])
const tags = ref<ProjectTag[]>([])
const loading = ref(false)
const total = ref(0)

// 查询参数
const currentTagId = ref<number | null>(null)
const currentPage = ref(1)
const pageSize = ref(12)

// 当前选中的标签
const selectedTag = computed(() => {
  if (!currentTagId.value) return null
  return tags.value.find(t => t.id === currentTagId.value)
})

// 加载标签
async function loadTags() {
  try {
    tags.value = await portfolioApi.getProjectTags()
  } catch {
    // ignore
  }
}

// 加载项目
async function loadProjects() {
  loading.value = true
  try {
    const res = await portfolioApi.getProjects({
      page: currentPage.value,
      size: pageSize.value,
      tagId: currentTagId.value || undefined
    })
    projects.value = res.list
    total.value = res.total
  } catch {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 选择标签
function selectTag(tag: ProjectTag | null) {
  currentTagId.value = tag?.id || null
  currentPage.value = 1
  loadProjects()
}

// 分页
function handlePageChange(page: number) {
  currentPage.value = page
  loadProjects()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 查看详情
function viewProject(project: Project) {
  router.push(`/portfolio/${project.slug || project.id}`)
}

// 获取链接
function getLinkByType(project: Project, type: string) {
  return project.links?.find(l => l.type === type)
}

onMounted(() => {
  loadTags()
  loadProjects()
})
</script>

<template>
  <div class="portfolio-list">
    <div class="portfolio-header">
      <h1>作品集</h1>
      <p class="subtitle">展示我的项目作品和技术实践</p>
    </div>

    <!-- 标签筛选 -->
    <div class="tag-filter">
      <NSpace size="small">
        <NTag
          :type="!currentTagId ? 'primary' : 'default'"
          :bordered="!currentTagId"
          round
          clickable
          @click="selectTag(null)"
        >
          全部
        </NTag>
        <NTag
          v-for="tag in tags"
          :key="tag.id"
          :type="currentTagId === tag.id ? 'primary' : 'default'"
          :bordered="currentTagId === tag.id"
          :style="currentTagId === tag.id ? {} : { borderColor: tag.color, color: tag.color }"
          round
          clickable
          @click="selectTag(tag)"
        >
          {{ tag.name }}
          <template v-if="tag.projectCount"> ({{ tag.projectCount }})</template>
        </NTag>
      </NSpace>
    </div>

    <!-- 项目列表 -->
    <div class="projects-grid">
      <template v-if="loading">
        <NCard v-for="i in 6" :key="i" class="project-card">
          <NSkeleton text :repeat="2" />
          <NSkeleton text style="width: 60%" />
        </NCard>
      </template>

      <template v-else-if="projects.length">
        <NCard
          v-for="project in projects"
          :key="project.id"
          class="project-card"
          hoverable
          @click="viewProject(project)"
        >
          <div class="project-cover" v-if="project.coverImage">
            <img :src="project.coverImage" :alt="project.name" />
          </div>
          <div class="project-cover project-cover--empty" v-else>
            <span>无封面</span>
          </div>
          <div class="project-content">
            <h3 class="project-name">{{ project.name }}</h3>
            <p class="project-description">{{ project.description }}</p>
            <div class="project-tech" v-if="project.techStack?.length">
              <span
                v-for="tech in project.techStack.slice(0, 4)"
                :key="tech"
                class="tech-badge"
              >
                {{ tech }}
              </span>
              <span v-if="project.techStack.length > 4" class="tech-more">
                +{{ project.techStack.length - 4 }}
              </span>
            </div>
            <div class="project-links" @click.stop>
              <a
                v-if="getLinkByType(project, 'DEMO')"
                :href="getLinkByType(project, 'DEMO')?.url"
                target="_blank"
                class="link-btn link-btn--demo"
              >
                演示
              </a>
              <a
                v-if="getLinkByType(project, 'SOURCE')"
                :href="getLinkByType(project, 'SOURCE')?.url"
                target="_blank"
                class="link-btn link-btn--source"
              >
                源码
              </a>
            </div>
          </div>
        </NCard>
      </template>

      <div v-else class="empty-state">
        <p>暂无项目</p>
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
.portfolio-list {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.portfolio-header {
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

.tag-filter {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.projects-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 24px;
}

.project-card {
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  overflow: hidden;
  padding: 0;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 30px rgba(0, 0, 0, 0.15);
  }

  :deep(.n-card__content) {
    padding: 0;
  }
}

.project-cover {
  width: 100%;
  aspect-ratio: 16 / 9;
  overflow: hidden;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s;
  }

  &:hover img {
    transform: scale(1.05);
  }

  &--empty {
    display: flex;
    align-items: center;
    justify-content: center;
    background: var(--bg-secondary, #1e293b);
    color: var(--text-secondary, #94a3b8);
    font-size: 0.875rem;
  }
}

.project-content {
  padding: 16px;
}

.project-name {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 8px;
}

.project-description {
  color: var(--text-secondary, #94a3b8);
  font-size: 0.875rem;
  line-height: 1.5;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.project-tech {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 12px;
}

.tech-badge {
  display: inline-block;
  padding: 2px 10px;
  background: var(--bg-secondary, #1e293b);
  color: var(--color-primary, #3b82f6);
  border-radius: 12px;
  font-size: 0.75rem;
}

.tech-more {
  color: var(--text-secondary, #94a3b8);
  font-size: 0.75rem;
}

.project-links {
  display: flex;
  gap: 12px;
}

.link-btn {
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 0.875rem;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.2s;

  &--demo {
    background: var(--color-primary, #3b82f6);
    color: white;

    &:hover {
      background: var(--color-primary-hover, #2563eb);
    }
  }

  &--source {
    background: transparent;
    border: 1px solid var(--border-color, #334155);
    color: var(--text-primary, #f1f5f9);

    &:hover {
      border-color: var(--color-primary, #3b82f6);
      color: var(--color-primary, #3b82f6);
    }
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

@media (max-width: 768px) {
  .projects-grid {
    grid-template-columns: 1fr;
  }
}
</style>
