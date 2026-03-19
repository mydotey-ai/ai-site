<script setup lang="ts">
import { ref, onMounted, computed, h } from 'vue'
import { useRouter } from 'vue-router'
import {
  NButton,
  NDataTable,
  NInput,
  NSelect,
  NSpace,
  NTag,
  NPopconfirm,
  useMessage,
  type DataTableColumns
} from 'naive-ui'
import { projectApi, projectTagApi } from '@/api/portfolio'
import type { Project, ProjectTag, ProjectQuery } from '@/types'

const router = useRouter()
const message = useMessage()

// 数据
const projects = ref<Project[]>([])
const tags = ref<ProjectTag[]>([])
const loading = ref(false)
const total = ref(0)

// 查询参数
const query = ref<ProjectQuery>({
  page: 1,
  size: 10,
  keyword: '',
  status: undefined,
  tagId: undefined
})

// 状态选项
const statusOptions = [
  { label: '全部', value: undefined },
  { label: '开发中', value: 'DEVELOPING' },
  { label: '已发布', value: 'RELEASED' },
  { label: '已归档', value: 'ARCHIVED' }
]

// 标签选项
const tagOptions = computed(() => [
  { label: '全部标签', value: undefined },
  ...tags.value.map(t => ({ label: t.name, value: t.id }))
])

// 表格列
const columns: DataTableColumns<Project> = [
  { title: '项目名称', key: 'name', ellipsis: { tooltip: true } },
  {
    title: '标签',
    key: 'tags',
    width: 200,
    render: (row) => {
      if (!row.tags?.length) return '-'
      return h(NSpace, { size: 'small' }, () =>
        row.tags.map(tag =>
          h(NTag, { size: 'small', style: { borderColor: tag.color, color: tag.color } }, () => tag.name)
        )
      )
    }
  },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row) => {
      const map: Record<string, { type: 'default' | 'success' | 'warning' | 'info'; label: string }> = {
        DEVELOPING: { type: 'info', label: '开发中' },
        RELEASED: { type: 'success', label: '已发布' },
        ARCHIVED: { type: 'warning', label: '已归档' }
      }
      const { type, label } = map[row.status] || { type: 'default', label: row.status }
      return h(NTag, { type, size: 'small' }, () => label)
    }
  },
  {
    title: '链接',
    key: 'links',
    width: 120,
    render: (row) => {
      if (!row.links?.length) return '-'
      return h(NSpace, { size: 'small' }, () =>
        row.links.slice(0, 2).map(link =>
          h('a', { href: link.url, target: '_blank', style: { color: 'var(--color-primary)' } }, link.label)
        )
      )
    }
  },
  {
    title: '创建时间',
    key: 'createdAt',
    width: 160,
    render: (row) => formatDate(row.createdAt)
  },
  {
    title: '操作',
    key: 'actions',
    width: 200,
    render: (row) => {
      return h(NSpace, { size: 'small' }, () => [
        h(NButton, { size: 'small', onClick: () => handleEdit(row.id) }, () => '编辑'),
        row.status === 'RELEASED'
          ? h(NButton, { size: 'small', onClick: () => handleArchive(row.id) }, () => '归档')
          : h(NButton, { size: 'small', type: 'primary', onClick: () => handleRelease(row.id) }, () => '发布'),
        h(
          NPopconfirm,
          { onPositiveClick: () => handleDelete(row.id) },
          {
            trigger: () => h(NButton, { size: 'small', type: 'error' }, () => '删除'),
            default: () => '确定删除该项目吗？'
          }
        )
      ])
    }
  }
]

// 加载数据
async function loadData() {
  loading.value = true
  try {
    const [projectRes, tagRes] = await Promise.all([
      projectApi.getList(query.value),
      tags.value.length ? Promise.resolve(tags.value) : projectTagApi.getList()
    ])
    projects.value = projectRes.list
    total.value = projectRes.total
    if (tagRes.length && !tags.value.length) {
      tags.value = tagRes as ProjectTag[]
    }
  } catch (e) {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 搜索
function handleSearch() {
  query.value.page = 1
  loadData()
}

// 重置
function handleReset() {
  query.value = { page: 1, size: 10 }
  loadData()
}

// 新建
function handleCreate() {
  router.push('/portfolio/create')
}

// 编辑
function handleEdit(id: number) {
  router.push(`/portfolio/edit/${id}`)
}

// 发布
async function handleRelease(id: number) {
  try {
    await projectApi.release(id)
    message.success('发布成功')
    loadData()
  } catch {
    message.error('发布失败')
  }
}

// 归档
async function handleArchive(id: number) {
  try {
    await projectApi.archive(id)
    message.success('归档成功')
    loadData()
  } catch {
    message.error('归档失败')
  }
}

// 删除
async function handleDelete(id: number) {
  try {
    await projectApi.delete(id)
    message.success('删除成功')
    loadData()
  } catch {
    message.error('删除失败')
  }
}

// 分页
function handlePageChange(page: number) {
  query.value.page = page
  loadData()
}

// 格式化日期
function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(loadData)
</script>

<template>
  <div class="project-list">
    <div class="header">
      <h1>作品管理</h1>
      <NSpace>
        <NButton @click="router.push('/portfolio/tags')">标签管理</NButton>
        <NButton type="primary" @click="handleCreate">新建项目</NButton>
      </NSpace>
    </div>

    <div class="toolbar">
      <NSpace>
        <NInput
          v-model:value="query.keyword"
          placeholder="搜索项目名称"
          clearable
          style="width: 200px"
          @keyup.enter="handleSearch"
        />
        <NSelect
          v-model:value="query.status"
          :options="statusOptions"
          placeholder="状态"
          style="width: 120px"
          @update:value="handleSearch"
        />
        <NSelect
          v-model:value="query.tagId"
          :options="tagOptions"
          placeholder="标签"
          style="width: 150px"
          @update:value="handleSearch"
        />
        <NButton @click="handleSearch">搜索</NButton>
        <NButton @click="handleReset">重置</NButton>
      </NSpace>
    </div>

    <NDataTable
      :columns="columns"
      :data="projects"
      :loading="loading"
      :pagination="{
        page: query.page,
        pageSize: query.size,
        itemCount: total,
        onChange: handlePageChange
      }"
      :row-key="(row: Project) => row.id"
    />
  </div>
</template>

<style scoped lang="scss">
.project-list {
  padding: 20px;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h1 {
      margin: 0;
      font-size: 24px;
    }
  }

  .toolbar {
    margin-bottom: 16px;
  }
}
</style>
