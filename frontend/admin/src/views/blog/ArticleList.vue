<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
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
import { articleApi } from '@/api/article'
import { categoryApi } from '@/api/category'
import type { Article, Category, ArticleQuery } from '@/types'

const router = useRouter()
const message = useMessage()

// 数据
const articles = ref<Article[]>([])
const categories = ref<Category[]>([])
const loading = ref(false)
const total = ref(0)

// 查询参数
const query = ref<ArticleQuery>({
  page: 1,
  size: 10,
  keyword: '',
  status: undefined,
  categoryId: undefined
})

// 状态选项
const statusOptions = [
  { label: '全部', value: undefined },
  { label: '草稿', value: 0 },
  { label: '已发布', value: 1 },
  { label: '隐藏', value: 2 }
]

// 分类选项
const categoryOptions = computed(() => [
  { label: '全部分类', value: undefined },
  ...categories.value.map(c => ({ label: c.name, value: c.id }))
])

// 表格列
const columns: DataTableColumns<Article> = [
  { title: '标题', key: 'title', ellipsis: { tooltip: true } },
  {
    title: '分类',
    key: 'categoryId',
    width: 120,
    render: (row) => row.category?.name || '-'
  },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row) => {
      const map: Record<number, { type: 'default' | 'success' | 'warning' | 'info'; label: string }> = {
        0: { type: 'default', label: '草稿' },
        1: { type: 'success', label: '已发布' },
        2: { type: 'warning', label: '隐藏' }
      }
      const { type, label } = map[row.status] || { type: 'default', label: '未知' }
      return h(NTag, { type, size: 'small' }, () => label)
    }
  },
  {
    title: '置顶',
    key: 'isTop',
    width: 80,
    render: (row) => row.isTop ? h(NTag, { type: 'warning', size: 'small' }, () => '置顶') : '-'
  },
  { title: '浏览', key: 'viewCount', width: 80 },
  { title: '点赞', key: 'likeCount', width: 80 },
  {
    title: '发布时间',
    key: 'publishedAt',
    width: 160,
    render: (row) => row.publishedAt ? formatDate(row.publishedAt) : '-'
  },
  {
    title: '操作',
    key: 'actions',
    width: 200,
    render: (row) => {
      return h(NSpace, { size: 'small' }, () => [
        h(NButton, { size: 'small', onClick: () => handleEdit(row.id) }, () => '编辑'),
        row.status === 1
          ? h(NButton, { size: 'small', onClick: () => handleUnpublish(row.id) }, () => '下架')
          : h(NButton, { size: 'small', type: 'primary', onClick: () => handlePublish(row.id) }, () => '发布'),
        h(
          NPopconfirm,
          { onPositiveClick: () => handleDelete(row.id) },
          {
            trigger: () => h(NButton, { size: 'small', type: 'error' }, () => '删除'),
            default: () => '确定删除该文章吗？'
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
    const [articleRes, categoryRes] = await Promise.all([
      articleApi.getList(query.value),
      categories.value.length ? Promise.resolve(categories.value) : categoryApi.getList()
    ])
    articles.value = articleRes.list
    total.value = articleRes.total
    if (categoryRes.length) {
      categories.value = categoryRes as Category[]
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
  router.push('/blog/create')
}

// 编辑
function handleEdit(id: number) {
  router.push(`/blog/edit/${id}`)
}

// 发布
async function handlePublish(id: number) {
  try {
    await articleApi.publish(id)
    message.success('发布成功')
    loadData()
  } catch {
    message.error('发布失败')
  }
}

// 下架
async function handleUnpublish(id: number) {
  try {
    await articleApi.unpublish(id)
    message.success('下架成功')
    loadData()
  } catch {
    message.error('下架失败')
  }
}

// 删除
async function handleDelete(id: number) {
  try {
    await articleApi.delete(id)
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

// h 函数
import { h } from 'vue'

onMounted(loadData)
</script>

<template>
  <div class="article-list">
    <div class="header">
      <h1>文章管理</h1>
      <NButton type="primary" @click="handleCreate">新建文章</NButton>
    </div>

    <div class="toolbar">
      <NSpace>
        <NInput
          v-model:value="query.keyword"
          placeholder="搜索标题/摘要"
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
      :data="articles"
      :loading="loading"
      :pagination="{
        page: query.page,
        pageSize: query.size,
        itemCount: total,
        onChange: handlePageChange
      }"
      :row-key="(row: Article) => row.id"
    />
  </div>
</template>

<style scoped lang="scss">
.article-list {
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
