<script setup lang="ts">
import { ref, onMounted, computed, h } from 'vue'
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
import { essayApi } from '@/api/creation'
import type { Essay, EssayCategory, EssayQuery, EssayRequest } from '@/types'

const message = useMessage()

const essayList = ref<Essay[]>([])
const categories = ref<EssayCategory[]>([])
const loading = ref(false)
const total = ref(0)

const query = ref<EssayQuery>({
  page: 1,
  size: 10,
  keyword: '',
  status: undefined,
  categoryId: undefined
})

// 编辑弹窗
const showModal = ref(false)
const editingEssay = ref<Essay | null>(null)
const formLoading = ref(false)

const form = ref<EssayRequest>({
  title: '',
  slug: '',
  author: '',
  summary: '',
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

const columns: DataTableColumns<Essay> = [
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
            default: () => '确定删除该散文吗？'
          }
        )
      ])
    }
  }
]

async function loadData() {
  loading.value = true
  try {
    const [essayRes, catRes] = await Promise.all([
      essayApi.getList(query.value),
      categories.value.length ? Promise.resolve(categories.value) : essayApi.getCategories()
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

function handleSearch() {
  query.value.page = 1
  loadData()
}

function handleReset() {
  query.value = { page: 1, size: 10 }
  loadData()
}

function handleCreate() {
  editingEssay.value = null
  form.value = {
    title: '',
    slug: '',
    author: '',
    summary: '',
    content: '',
    categoryId: undefined,
    status: 'DRAFT'
  }
  showModal.value = true
}

function handleEdit(essay: Essay) {
  editingEssay.value = essay
  form.value = {
    title: essay.title,
    slug: essay.slug,
    author: essay.author,
    summary: essay.summary,
    content: essay.content,
    categoryId: essay.categoryId,
    status: essay.status
  }
  showModal.value = true
}

async function handleSave() {
  if (!form.value.title) {
    message.error('请输入散文标题')
    return
  }

  formLoading.value = true
  try {
    if (editingEssay.value) {
      await essayApi.update(editingEssay.value.id, form.value)
      message.success('更新成功')
    } else {
      await essayApi.create(form.value)
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
    await essayApi.publish(id)
    message.success('发布成功')
    loadData()
  } catch {
    message.error('发布失败')
  }
}

async function handleDelete(id: number) {
  try {
    await essayApi.delete(id)
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
  <div class="essay-list">
    <div class="header">
      <h1>散文管理</h1>
      <NButton type="primary" @click="handleCreate">新建散文</NButton>
    </div>

    <div class="toolbar">
      <NSpace>
        <NInput
          v-model:value="query.keyword"
          placeholder="搜索散文标题"
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
      :data="essayList"
      :loading="loading"
      :pagination="{
        page: query.page,
        pageSize: query.size,
        itemCount: total,
        onChange: handlePageChange
      }"
      :row-key="(row: Essay) => row.id"
    />

    <NModal
      v-model:show="showModal"
      :title="editingEssay ? '编辑散文' : '新建散文'"
      preset="card"
      style="width: 800px"
    >
      <NForm :model="form" label-width="80">
        <NFormItem label="标题" required>
          <NInput v-model:value="form.title" placeholder="请输入散文标题" />
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

        <NFormItem label="摘要">
          <NInput
            v-model:value="form.summary"
            type="textarea"
            placeholder="散文摘要"
            :rows="3"
          />
        </NFormItem>

        <NFormItem label="内容">
          <NInput
            v-model:value="form.content"
            type="textarea"
            placeholder="散文内容"
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
.essay-list {
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
