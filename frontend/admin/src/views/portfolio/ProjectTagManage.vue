<script setup lang="ts">
import { ref, onMounted, h } from 'vue'
import {
  NButton,
  NDataTable,
  NSpace,
  NTag,
  NPopconfirm,
  NModal,
  NForm,
  NFormItem,
  NInput,
  NInputNumber,
  NColorPicker,
  useMessage,
  type DataTableColumns
} from 'naive-ui'
import { projectTagApi } from '@/api/portfolio'
import type { ProjectTag, ProjectTagRequest } from '@/types'

const message = useMessage()

// 数据
const tags = ref<ProjectTag[]>([])
const loading = ref(false)

// 弹窗
const showModal = ref(false)
const isEdit = ref(false)
const editingId = ref<number | null>(null)
const submitting = ref(false)

// 表单
const form = ref<ProjectTagRequest>({
  name: '',
  slug: '',
  color: '#3b82f6',
  sort: 0
})

// 表格列
const columns: DataTableColumns<ProjectTag> = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '名称', key: 'name', width: 150 },
  { title: 'Slug', key: 'slug', width: 150 },
  {
    title: '颜色',
    key: 'color',
    width: 120,
    render: (row) => h(NTag, { style: { backgroundColor: row.color, color: '#fff' } }, () => row.color)
  },
  { title: '排序', key: 'sort', width: 80 },
  { title: '项目数', key: 'projectCount', width: 80 },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    render: (row) => h(NSpace, { size: 'small' }, () => [
      h(NButton, { size: 'small', onClick: () => handleEdit(row) }, () => '编辑'),
      h(
        NPopconfirm,
        { onPositiveClick: () => handleDelete(row.id) },
        {
          trigger: () => h(NButton, { size: 'small', type: 'error' }, () => '删除'),
          default: () => '确定删除该标签吗？'
        }
      )
    ])
  }
]

// 加载数据
async function loadData() {
  loading.value = true
  try {
    tags.value = await projectTagApi.getList() as ProjectTag[]
  } catch {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 新建
function handleCreate() {
  isEdit.value = false
  editingId.value = null
  form.value = { name: '', slug: '', color: '#3b82f6', sort: 0 }
  showModal.value = true
}

// 编辑
function handleEdit(tag: ProjectTag) {
  isEdit.value = true
  editingId.value = tag.id
  form.value = {
    name: tag.name,
    slug: tag.slug,
    color: tag.color,
    sort: tag.sort
  }
  showModal.value = true
}

// 提交
async function handleSubmit() {
  if (!form.value.name) {
    message.error('请输入标签名称')
    return
  }
  submitting.value = true
  try {
    if (isEdit.value && editingId.value) {
      await projectTagApi.update(editingId.value, form.value)
      message.success('更新成功')
    } else {
      await projectTagApi.create(form.value)
      message.success('创建成功')
    }
    showModal.value = false
    loadData()
  } catch {
    message.error(isEdit.value ? '更新失败' : '创建失败')
  } finally {
    submitting.value = false
  }
}

// 删除
async function handleDelete(id: number) {
  try {
    await projectTagApi.delete(id)
    message.success('删除成功')
    loadData()
  } catch {
    message.error('删除失败')
  }
}

onMounted(loadData)
</script>

<template>
  <div class="tag-manage">
    <div class="header">
      <h1>项目标签管理</h1>
      <NButton type="primary" @click="handleCreate">新建标签</NButton>
    </div>

    <NDataTable
      :columns="columns"
      :data="tags"
      :loading="loading"
      :row-key="(row: ProjectTag) => row.id"
    />

    <NModal
      v-model:show="showModal"
      :title="isEdit ? '编辑标签' : '新建标签'"
      preset="card"
      style="width: 400px"
    >
      <NForm :model="form" label-placement="left" label-width="80px">
        <NFormItem label="名称" required>
          <NInput v-model:value="form.name" placeholder="请输入标签名称" />
        </NFormItem>
        <NFormItem label="Slug">
          <NInput v-model:value="form.slug" placeholder="URL别名，留空自动生成" />
        </NFormItem>
        <NFormItem label="颜色">
          <NColorPicker v-model:value="form.color" :modes="['hex']" />
        </NFormItem>
        <NFormItem label="排序">
          <NInputNumber v-model:value="form.sort" placeholder="排序值" :min="0" style="width: 100%" />
        </NFormItem>
      </NForm>
      <template #footer>
        <NSpace justify="end">
          <NButton @click="showModal = false">取消</NButton>
          <NButton type="primary" :loading="submitting" @click="handleSubmit">确定</NButton>
        </NSpace>
      </template>
    </NModal>
  </div>
</template>

<style scoped lang="scss">
.tag-manage {
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
}
</style>
