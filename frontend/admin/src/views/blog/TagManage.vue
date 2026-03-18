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
  NColorPicker,
  useMessage,
  type DataTableColumns,
  type FormInst,
  type FormRules
} from 'naive-ui'
import { tagApi } from '@/api/tag'
import type { Tag, TagRequest } from '@/types'

const message = useMessage()

// 数据
const tags = ref<Tag[]>([])
const loading = ref(false)
const modalVisible = ref(false)
const modalLoading = ref(false)
const isEdit = ref(false)
const formRef = ref<FormInst | null>(null)

// 表单数据
const formData = ref<TagRequest>({
  name: '',
  slug: '',
  color: '#3b82f6'
})

// 编辑时的 ID
const editId = ref<number | null>(null)

// 表单规则
const rules: FormRules = {
  name: [{ required: true, message: '请输入标签名称', trigger: 'blur' }],
  slug: [
    { required: true, message: '请输入别名', trigger: 'blur' },
    { pattern: /^[a-z0-9-]+$/, message: '别名只能包含小写字母、数字和连字符', trigger: 'blur' }
  ]
}

// 表格列
const columns: DataTableColumns<Tag> = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '名称', key: 'name' },
  { title: '别名', key: 'slug' },
  {
    title: '颜色',
    key: 'color',
    width: 100,
    render: (row) => h('span', {
      style: {
        display: 'inline-block',
        width: '20px',
        height: '20px',
        borderRadius: '4px',
        backgroundColor: row.color,
        marginRight: '8px',
        verticalAlign: 'middle'
      }
    })
  },
  { title: '文章数', key: 'articleCount', width: 100 },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    render: (row) => {
      return h(NSpace, { size: 'small' }, () => [
        h(NButton, { size: 'small', onClick: () => handleEdit(row) }, () => '编辑'),
        h(NButton, { size: 'small', type: 'error', onClick: () => handleDelete(row.id) }, () => '删除')
      ])
    }
  }
]

// 加载数据
async function loadData() {
  loading.value = true
  try {
    tags.value = await tagApi.getList()
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
  formData.value = { name: '', slug: '', color: '#3b82f6' }
  modalVisible.value = true
}

// 编辑
function handleEdit(row: Tag) {
  isEdit.value = true
  editId.value = row.id
  formData.value = {
    name: row.name,
    slug: row.slug,
    color: row.color
  }
  modalVisible.value = true
}

// 删除
async function handleDelete(id: number) {
  try {
    await tagApi.delete(id)
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
      await tagApi.update(editId.value, formData.value)
      message.success('更新成功')
    } else {
      await tagApi.create(formData.value)
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
  <div class="tag-manage">
    <div class="header">
      <h1>标签管理</h1>
      <NButton type="primary" @click="handleCreate">新建标签</NButton>
    </div>

    <NDataTable
      :columns="columns"
      :data="tags"
      :loading="loading"
      :row-key="(row: Tag) => row.id"
    />

    <NModal
      v-model:show="modalVisible"
      :title="isEdit ? '编辑标签' : '新建标签'"
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
          <NInput v-model:value="formData.name" placeholder="标签名称" />
        </NFormItem>
        <NFormItem label="别名" path="slug">
          <NInput v-model:value="formData.slug" placeholder="url-slug" />
        </NFormItem>
        <NFormItem label="颜色" path="color">
          <NColorPicker v-model:value="formData.color" :modes="['hex']" />
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
