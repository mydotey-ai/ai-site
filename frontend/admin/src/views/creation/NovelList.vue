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
import { novelApi } from '@/api/creation'
import type { Novel, NovelCategory, NovelQuery } from '@/types'

const router = useRouter()
const message = useMessage()

const novels = ref<Novel[]>([])
const categories = ref<NovelCategory[]>([])
const loading = ref(false)
const total = ref(0)

const query = ref<NovelQuery>({
  page: 1,
  size: 10,
  keyword: '',
  status: undefined,
  categoryId: undefined
})

const statusOptions = [
  { label: '全部', value: undefined },
  { label: '草稿', value: 'DRAFT' },
  { label: '连载中', value: 'PUBLISHED' },
  { label: '已完结', value: 'COMPLETED' }
]

const categoryOptions = computed(() => [
  { label: '全部分类', value: undefined },
  ...categories.value.map(c => ({ label: c.name, value: c.id }))
])

const columns: DataTableColumns<Novel> = [
  { title: '小说名称', key: 'title', ellipsis: { tooltip: true } },
  { title: '作者', key: 'author', width: 100 },
  {
    title: '分类',
    key: 'category',
    width: 100,
    render: (row) => row.category?.name || '-'
  },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row) => {
      const map: Record<string, { type: 'default' | 'success' | 'warning' | 'info'; label: string }> = {
        DRAFT: { type: 'default', label: '草稿' },
        PUBLISHED: { type: 'success', label: '连载中' },
        COMPLETED: { type: 'info', label: '已完结' }
      }
      const { type, label } = map[row.status] || { type: 'default', label: row.status }
      return h(NTag, { type, size: 'small' }, () => label)
    }
  },
  { title: '字数', key: 'wordCount', width: 100, render: (row) => formatNumber(row.wordCount) },
  { title: '章节', key: 'chapterCount', width: 80 },
  { title: '浏览', key: 'viewCount', width: 80 },
  {
    title: '创建时间',
    key: 'createdAt',
    width: 160,
    render: (row) => formatDate(row.createdAt)
  },
  {
    title: '操作',
    key: 'actions',
    width: 250,
    render: (row) => {
      return h(NSpace, { size: 'small' }, () => [
        h(NButton, { size: 'small', onClick: () => handleEdit(row.id) }, () => '编辑'),
        h(NButton, { size: 'small', onClick: () => handleChapters(row.id) }, () => '章节'),
        row.status === 'DRAFT'
          ? h(NButton, { size: 'small', type: 'primary', onClick: () => handlePublish(row.id) }, () => '发布')
          : row.status === 'PUBLISHED'
            ? h(NButton, { size: 'small', onClick: () => handleComplete(row.id) }, () => '完结')
            : null,
        h(
          NPopconfirm,
          { onPositiveClick: () => handleDelete(row.id) },
          {
            trigger: () => h(NButton, { size: 'small', type: 'error' }, () => '删除'),
            default: () => '确定删除该小说吗？'
          }
        )
      ])
    }
  }
]

async function loadData() {
  loading.value = true
  try {
    const [novelRes, catRes] = await Promise.all([
      novelApi.getList(query.value),
      categories.value.length ? Promise.resolve(categories.value) : novelApi.getCategories()
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

function handleSearch() {
  query.value.page = 1
  loadData()
}

function handleReset() {
  query.value = { page: 1, size: 10 }
  loadData()
}

function handleCreate() {
  router.push('/creation/novels/create')
}

function handleEdit(id: number) {
  router.push(`/creation/novels/edit/${id}`)
}

function handleChapters(id: number) {
  router.push(`/creation/novels/${id}/chapters`)
}

async function handlePublish(id: number) {
  try {
    await novelApi.publish(id)
    message.success('发布成功')
    loadData()
  } catch {
    message.error('发布失败')
  }
}

async function handleComplete(id: number) {
  try {
    await novelApi.complete(id)
    message.success('已标记为完结')
    loadData()
  } catch {
    message.error('操作失败')
  }
}

async function handleDelete(id: number) {
  try {
    await novelApi.delete(id)
    message.success('删除成功')
    loadData()
  } catch {
    message.error('删除失败')
  }
}

function handlePageChange(page: number) {
  query.value.page = page
  loadData()
}

function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

function formatNumber(num: number) {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '万'
  }
  return num
}

onMounted(loadData)
</script>

<template>
  <div class="novel-list">
    <div class="header">
      <h1>小说管理</h1>
      <NButton type="primary" @click="handleCreate">新建小说</NButton>
    </div>

    <div class="toolbar">
      <NSpace>
        <NInput
          v-model:value="query.keyword"
          placeholder="搜索小说名称"
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
          v-model:value="query.categoryId"
          :options="categoryOptions"
          placeholder="分类"
          style="width: 150px"
          @update:value="handleSearch"
        />
        <NButton @click="handleSearch">搜索</NButton>
        <NButton @click="handleReset">重置</NButton>
      </NSpace>
    </div>

    <NDataTable
      :columns="columns"
      :data="novels"
      :loading="loading"
      :pagination="{
        page: query.page,
        pageSize: query.size,
        itemCount: total,
        onChange: handlePageChange
      }"
      :row-key="(row: Novel) => row.id"
    />
  </div>
</template>

<style scoped lang="scss">
.novel-list {
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
