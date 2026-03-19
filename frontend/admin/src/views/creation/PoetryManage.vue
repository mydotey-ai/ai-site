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
  NModal,
  NForm,
  NFormItem,
  useMessage,
  type DataTableColumns
} from 'naive-ui'
import { poetryApi } from '@/api/creation'
import type { Poetry, PoetryCategory, PoetryQuery, PoetryRequest } from '@/types'

const router = useRouter()
const message = useMessage()

const poetryList = ref<Poetry[]>([])
const categories = ref<PoetryCategory[]>([])
const loading = ref(false)
const total = ref(0)

const query = ref<PoetryQuery>({
  page: 1,
  size: 10,
  keyword: '',
  status: undefined,
  categoryId: undefined
})

// 编辑弹窗
const showModal = ref(false)
const editingPoetry = ref<Poetry | null>(null)
const formLoading = ref(false)

const form = ref<PoetryRequest>({
  title: '',
  slug: '',
  author: '',
  content: '',
  categoryId: undefined,
  status: 'DRAFT'
})

const statusOptions = [
  { label: '全部', value: undefined },
  { label: '草稿', value: 'DRAFT' },
  { label: '已发布', value: 'PUBLISHED' }
]

const categoryOptions = computed(() => [
  { label: '全部分类', value: undefined },
  ...categories.value.map(c => ({ label: c.name, value: c.id }))
])

const columns: DataTableColumns<Poetry> = [
  { title: '标题', key: 'title', ellipsis: { tooltip: true } },
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
      const map: Record<string, { type: 'default' | 'success'; label: string }> = {
        DRAFT: { type: 'default', label: '草稿' },
        PUBLISHED: { type: 'success', label: '已发布' }
      }
      const { type, label } = map[row.status] || { type: 'default', label: row.status }
      return h(NTag, { type, size: 'small' }, () => label)
    }
  },
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
    width: 200,
    render: (row) => {
      return h(NSpace, { size: 'small' }, () => [
        h(NButton, { size: 'small', onClick: () => handleEdit(row) }, () => '编辑'),
        row.status === 'DRAFT'
          ? h(NButton, { size: 'small', type: 'primary', onClick: () => handlePublish(row.id) }, () => '发布')
          : null,
        h(
          NPopconfirm,
          { onPositiveClick: () => handleDelete(row.id) },
          {
            trigger: () => h(NButton, { size: 'small', type: 'error' }, () => '删除'),
            default: () => '确定删除该诗歌吗？'
          }
        )
      ])
    }
  }
]

async function loadData() {
  loading.value = true
  try {
    const [poetryRes, catRes] = await Promise.all([
      poetryApi.getList(query.value),
      categories.value.length ? Promise.resolve(categories.value) : poetryApi.getCategories()
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

function handleSearch() {
  query.value.page = 1
  loadData()
}

function handleReset() {
  query.value = { page: 1, size: 10 }
  loadData()
}

function handleCreate() {
  editingPoetry.value = null
  form.value = {
    title: '',
    slug: '',
    author: '',
    content: '',
    categoryId: undefined,
    status: 'DRAFT'
  }
  showModal.value = true
}

function handleEdit(poetry: Poetry) {
  editingPoetry.value = poetry
  form.value = {
    title: poetry.title,
    slug: poetry.slug,
    author: poetry.author,
    content: poetry.content,
    categoryId: poetry.categoryId,
    status: poetry.status
  }
  showModal.value = true
}

async function handleSave() {
  if (!form.value.title) {
    message.error('请输入诗歌标题')
    return
  }

  formLoading.value = true
  try {
    if (editingPoetry.value) {
      await poetryApi.update(editingPoetry.value.id, form.value)
      message.success('更新成功')
    } else {
      await poetryApi.create(form.value)
      message.success('创建成功')
    }
    showModal.value = false
    loadData()
  } catch {
    message.error('保存失败')
  } finally {
    formLoading.value = false
  }
}

async function handlePublish(id: number) {
  try {
    await poetryApi.publish(id)
    message.success('发布成功')
    loadData()
  } catch {
    message.error('发布失败')
  }
}

async function handleDelete(id: number) {
  try {
    await poetryApi.delete(id)
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

onMounted(loadData)
</script>

<template>
  <div class="poetry-list">
    <div class="header">
      <h1>诗歌管理</h1>
      <NButton type="primary" @click="handleCreate">新建诗歌</NButton>
    </div>

    <div class="toolbar">
      <NSpace>
        <NInput
          v-model:value="query.keyword"
          placeholder="搜索诗歌标题"
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
      :data="poetryList"
      :loading="loading"
      :pagination="{
        page: query.page,
        pageSize: query.size,
        itemCount: total,
        onChange: handlePageChange
      }"
      :row-key="(row: Poetry) => row.id"
    />

    <NModal
      v-model:show="showModal"
      :title="editingPoetry ? '编辑诗歌' : '新建诗歌'"
      preset="card"
      style="width: 800px"
    >
      <NForm :model="form" label-width="80">
        <NFormItem label="标题" required>
          <NInput v-model:value="form.title" placeholder="请输入诗歌标题" />
        </NFormItem>

        <NFormItem label="别名">
          <NInput v-model:value="form.slug" placeholder="URL别名，留空自动生成" />
        </NFormItem>

        <NFormItem label="作者">
          <NInput v-model:value="form.author" placeholder="作者名称" />
        </NFormItem>

        <NFormItem label="分类">
          <NSelect
            v-model:value="form.categoryId"
            :options="categoryOptions.filter(o => o.value !== undefined)"
            placeholder="选择分类"
            clearable
          />
        </NFormItem>

        <NFormItem label="内容">
          <NInput
            v-model:value="form.content"
            type="textarea"
            placeholder="诗歌内容"
            :rows="10"
          />
        </NFormItem>
      </NForm>

      <template #footer>
        <NSpace justify="end">
          <NButton @click="showModal = false">取消</NButton>
          <NButton type="primary" :loading="formLoading" @click="handleSave">保存</NButton>
        </NSpace>
      </template>
    </NModal>
  </div>
</template>

<style scoped lang="scss">
.poetry-list {
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
