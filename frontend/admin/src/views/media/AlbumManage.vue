<template>
  <div class="album-manage">
    <div class="header">
      <h1>相册管理</h1>
      <n-button type="primary" @click="openModal()">新建相册</n-button>
    </div>

    <n-data-table
      :columns="columns"
      :data="albums"
      :loading="loading"
      :pagination="pagination"
    />

    <!-- 编辑弹窗 -->
    <n-modal v-model:show="showModal" preset="card" :title="editId ? '编辑相册' : '新建相册'" style="width: 500px">
      <n-form ref="formRef" :model="form" :rules="rules" label-placement="left" label-width="80">
        <n-form-item label="名称" path="name">
          <n-input v-model:value="form.name" placeholder="请输入相册名称" />
        </n-form-item>
        <n-form-item label="别名" path="slug">
          <n-input v-model:value="form.slug" placeholder="URL别名（可选）" />
        </n-form-item>
        <n-form-item label="描述" path="description">
          <n-input v-model:value="form.description" type="textarea" :rows="3" placeholder="相册描述" />
        </n-form-item>
        <n-form-item label="封面" path="coverImage">
          <n-input v-model:value="form.coverImage" placeholder="封面图URL" />
        </n-form-item>
        <n-form-item label="排序" path="sort">
          <n-input-number v-model:value="form.sort" :min="0" />
        </n-form-item>
        <n-form-item label="公开" path="isPublic">
          <n-switch v-model:value="form.isPublic" :checked-value="1" :unchecked-value="0" />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="showModal = false">取消</n-button>
          <n-button type="primary" :loading="submitting" @click="handleSave">保存</n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted } from 'vue'
import { useMessage, useDialog, NButton, NSpace, NTag, type DataTableColumns } from 'naive-ui'
import { albumApi } from '@/api/media'
import type { Album, AlbumRequest } from '@/types'

const message = useMessage()
const dialog = useDialog()

const loading = ref(false)
const submitting = ref(false)
const albums = ref<Album[]>([])
const showModal = ref(false)
const editId = ref<number | null>(null)

const form = reactive<AlbumRequest>({
  name: '',
  slug: '',
  description: '',
  coverImage: '',
  sort: 0,
  isPublic: 1
})

const rules = {
  name: { required: true, message: '请输入名称', trigger: 'blur' }
}

const pagination = reactive({
  page: 1,
  pageSize: 20,
  itemCount: 0,
  onChange: (page: number) => {
    pagination.page = page
    loadData()
  }
})

const columns: DataTableColumns<Album> = [
  { title: '名称', key: 'name' },
  { title: '别名', key: 'slug' },
  { title: '图片数', key: 'imageCount', width: 80 },
  {
    title: '状态',
    key: 'isPublic',
    width: 80,
    render: (row) => h(NTag, { type: row.isPublic ? 'success' : 'default' }, () => row.isPublic ? '公开' : '私有')
  },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    render: (row) => h(NSpace, {}, () => [
      h(NButton, { size: 'small', onClick: () => openModal(row) }, () => '编辑'),
      h(NButton, { size: 'small', type: 'error', onClick: () => handleDelete(row.id) }, () => '删除')
    ])
  }
]

onMounted(loadData)

async function loadData() {
  loading.value = true
  try {
    const res = await albumApi.getList({ page: pagination.page, size: pagination.pageSize })
    albums.value = res.list
    pagination.itemCount = res.total
  } catch (e: any) {
    message.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function openModal(album?: Album) {
  if (album) {
    editId.value = album.id
    Object.assign(form, {
      name: album.name,
      slug: album.slug,
      description: album.description,
      coverImage: album.coverImage,
      sort: album.sort,
      isPublic: album.isPublic
    })
  } else {
    editId.value = null
    Object.assign(form, { name: '', slug: '', description: '', coverImage: '', sort: 0, isPublic: 1 })
  }
  showModal.value = true
}

async function handleSave() {
  submitting.value = true
  try {
    if (editId.value) {
      await albumApi.update(editId.value, form)
      message.success('更新成功')
    } else {
      await albumApi.create(form)
      message.success('创建成功')
    }
    showModal.value = false
    loadData()
  } catch (e: any) {
    message.error(e.message || '保存失败')
  } finally {
    submitting.value = false
  }
}

function handleDelete(id: number) {
  dialog.warning({
    title: '确认删除',
    content: '确定要删除这个相册吗？',
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await albumApi.delete(id)
        message.success('删除成功')
        loadData()
      } catch (e: any) {
        message.error(e.message || '删除失败')
      }
    }
  })
}
</script>

<style scoped lang="scss">
.album-manage {
  padding: 20px;
}

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
</style>
