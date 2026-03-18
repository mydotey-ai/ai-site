<script setup lang="ts">
import { ref, onMounted, h } from 'vue'
import {
  NButton,
  NDataTable,
  NInput,
  NSpace,
  NModal,
  NForm,
  NFormItem,
  NInputNumber,
  useMessage,
  type DataTableColumns,
  type FormInst,
  type FormRules
} from 'naive-ui'
import { categoryApi } from '@/api/category'
import type { Category, CategoryRequest } from '@/types'

const message = useMessage()

// 数据
const categories = ref<Category[]>([])
const loading = ref(false)
const modalVisible = ref(false)
const modalLoading = ref(false)
const isEdit = ref(false)
const formRef = ref<FormInst | null>(null)

// 表单数据
const formData = ref<CategoryRequest>({
  name: '',
  slug: '',
  description: '',
  parentId: undefined,
  sortOrder: 0
})

// 编辑时的 ID
const editId = ref<number | null>(null)

// 表单规则
const rules: FormRules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
  slug: [
    { required: true, message: '请输入别名', trigger: 'blur' },
    { pattern: /^[a-z0-9-]+$/, message: '别名只能包含小写字母、数字和连字符', trigger: 'blur' }
  ]
}

// 表格列
const columns: DataTableColumns<Category> = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '名称', key: 'name' },
  { title: '别名', key: 'slug' },
  { title: '描述', key: 'description', ellipsis: { tooltip: true } },
  { title: '文章数', key: 'articleCount', width: 100 },
  { title: '排序', key: 'sortOrder', width: 80 },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    render: (row) => {
      return h(NSpace, { size: 'small' }, () => [
        h(NButton, { size: 'small', onClick: () => handleEdit(row) }, () => '编辑'),
        h(
          NButton,
          {
            size: 'small',
            type: 'error',
            disabled: row.articleCount > 0,
            onClick: () => handleDelete(row.id)
          },
          () => '删除'
        )
      ])
    }
  }
]

// 加载数据
async function loadData() {
  loading.value = true
  try {
    categories.value = await categoryApi.getList()
  } catch {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 新建
function handleCreate() {
  isEdit.value = false
  editId.value = null
  formData.value = { name: '', slug: '', description: '', parentId: undefined, sortOrder: 0 }
  modalVisible.value = true
}

// 编辑
function handleEdit(row: Category) {
  isEdit.value = true
  editId.value = row.id
  formData.value = {
    name: row.name,
    slug: row.slug,
    description: row.description || '',
    parentId: row.parentId || undefined,
    sortOrder: row.sortOrder
  }
  modalVisible.value = true
}

// 删除
async function handleDelete(id: number) {
  try {
    await categoryApi.delete(id)
    message.success('删除成功')
    loadData()
  } catch {
    message.error('删除失败')
  }
}

// 提交
async function handleSubmit() {
  try {
    await formRef.value?.validate()
  } catch {
    return
  }

  modalLoading.value = true
  try {
    if (isEdit.value && editId.value) {
      await categoryApi.update(editId.value, formData.value)
      message.success('更新成功')
    } else {
      await categoryApi.create(formData.value)
      message.success('创建成功')
    }
    modalVisible.value = false
    loadData()
  } catch {
    message.error(isEdit.value ? '更新失败' : '创建失败')
  } finally {
    modalLoading.value = false
  }
}

onMounted(loadData)
</script>

<template>
  <div class="category-manage">
    <div class="header">
      <h1>分类管理</h1>
      <NButton type="primary" @click="handleCreate">新建分类</NButton>
    </div>

    <NDataTable
      :columns="columns"
      :data="categories"
      :loading="loading"
      :row-key="(row: Category) => row.id"
    />

    <NModal
      v-model:show="modalVisible"
      :title="isEdit ? '编辑分类' : '新建分类'"
      preset="dialog"
      style="width: 500px"
    >
      <NForm
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="left"
        label-width="80px"
      >
        <NFormItem label="名称" path="name">
          <NInput v-model:value="formData.name" placeholder="分类名称" />
        </NFormItem>
        <NFormItem label="别名" path="slug">
          <NInput v-model:value="formData.slug" placeholder="url-slug" />
        </NFormItem>
        <NFormItem label="描述" path="description">
          <NInput v-model:value="formData.description" type="textarea" :rows="3" placeholder="分类描述" />
        </NFormItem>
        <NFormItem label="排序" path="sortOrder">
          <NInputNumber v-model:value="formData.sortOrder" :min="0" />
        </NFormItem>
      </NForm>
      <template #action>
        <NSpace justify="end">
          <NButton @click="modalVisible = false">取消</NButton>
          <NButton type="primary" :loading="modalLoading" @click="handleSubmit">确定</NButton>
        </NSpace>
      </template>
    </NModal>
  </div>
</template>

<style scoped lang="scss">
.category-manage {
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
