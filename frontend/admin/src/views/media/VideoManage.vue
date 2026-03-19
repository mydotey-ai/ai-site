<template>
  <div class="video-manage">
    <div class="header">
      <h1>视频管理</h1>
      <n-space>
        <n-button @click="openUploadModal">本地上传</n-button>
        <n-button type="primary" @click="openModal()">添加外链视频</n-button>
      </n-space>
    </div>

    <n-data-table
      :columns="columns"
      :data="videos"
      :loading="loading"
      :pagination="pagination"
    />

    <!-- 添加/编辑弹窗 -->
    <n-modal v-model:show="showModal" preset="card" :title="editId ? '编辑视频' : '添加视频'" style="width: 600px">
      <n-form ref="formRef" :model="form" :rules="rules" label-placement="left" label-width="80">
        <n-form-item label="标题" path="title">
          <n-input v-model:value="form.title" placeholder="请输入视频标题" />
        </n-form-item>
        <n-form-item label="类型" path="type">
          <n-radio-group v-model:value="form.type">
            <n-radio value="EXTERNAL">外链</n-radio>
            <n-radio value="LOCAL">本地</n-radio>
          </n-radio-group>
        </n-form-item>
        <n-form-item v-if="form.type === 'EXTERNAL'" label="平台" path="platform">
          <n-select v-model:value="form.platform" :options="platformOptions" />
        </n-form-item>
        <n-form-item v-if="form.type === 'EXTERNAL'" label="视频ID" path="videoId">
          <n-input v-model:value="form.videoId" placeholder="B站BV号或YouTube视频ID" />
        </n-form-item>
        <n-form-item label="描述" path="description">
          <n-input v-model:value="form.description" type="textarea" :rows="3" />
        </n-form-item>
        <n-form-item label="分类" path="category">
          <n-input v-model:value="form.category" />
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

    <!-- 上传弹窗 -->
    <n-modal v-model:show="showUploadModal" preset="card" title="上传视频" style="width: 500px">
      <n-upload :custom-request="handleUpload" accept="video/*">
        <n-upload-dragger>
          <div style="margin-bottom: 12px">
            <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="17 8 12 3 7 8"/><line x1="12" y1="3" x2="12" y2="15"/></svg>
          </div>
          <n-text>点击或拖拽上传视频</n-text>
        </n-upload-dragger>
      </n-upload>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted } from 'vue'
import { useMessage, useDialog, NButton, NSpace, NTag, type UploadCustomRequestDetails } from 'naive-ui'
import { videoApi } from '@/api/media'
import type { Video, VideoRequest } from '@/types'

const message = useMessage()
const dialog = useDialog()

const loading = ref(false)
const submitting = ref(false)
const videos = ref<Video[]>([])
const showModal = ref(false)
const showUploadModal = ref(false)
const editId = ref<number | null>(null)

const form = reactive<VideoRequest>({
  title: '',
  type: 'EXTERNAL',
  platform: 'BILIBILI',
  videoId: '',
  description: '',
  category: '',
  isPublic: 1
})

const rules = {
  title: { required: true, message: '请输入标题', trigger: 'blur' },
  type: { required: true }
}

const platformOptions = [
  { label: '哔哩哔哩', value: 'BILIBILI' },
  { label: 'YouTube', value: 'YOUTUBE' }
]

const pagination = reactive({
  page: 1,
  pageSize: 20,
  itemCount: 0,
  onChange: (page: number) => {
    pagination.page = page
    loadData()
  }
})

const columns = [
  { title: '标题', key: 'title', ellipsis: { tooltip: true } },
  { title: '类型', key: 'type', width: 80 },
  { title: '平台', key: 'platform', width: 100 },
  { title: '分类', key: 'category', width: 100 },
  {
    title: '状态',
    key: 'isPublic',
    width: 80,
    render: (row: Video) => h(NTag, { type: row.isPublic ? 'success' : 'default' }, () => row.isPublic ? '公开' : '私有')
  },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    render: (row: Video) => h(NSpace, {}, () => [
      h(NButton, { size: 'small', onClick: () => openModal(row) }, () => '编辑'),
      h(NButton, { size: 'small', type: 'error', onClick: () => handleDelete(row.id) }, () => '删除')
    ])
  }
]

onMounted(loadData)

async function loadData() {
  loading.value = true
  try {
    const res = await videoApi.getList({ page: pagination.page, size: pagination.pageSize })
    videos.value = res.list
    pagination.itemCount = res.total
  } catch (e: any) {
    message.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

function openModal(video?: Video) {
  if (video) {
    editId.value = video.id
    Object.assign(form, {
      title: video.title,
      type: video.type,
      platform: video.platform,
      videoId: video.videoId,
      description: video.description,
      category: video.category,
      isPublic: video.isPublic
    })
  } else {
    editId.value = null
    Object.assign(form, { title: '', type: 'EXTERNAL', platform: 'BILIBILI', videoId: '', description: '', category: '', isPublic: 1 })
  }
  showModal.value = true
}

function openUploadModal() {
  showUploadModal.value = true
}

async function handleSave() {
  submitting.value = true
  try {
    if (editId.value) {
      await videoApi.update(editId.value, form)
      message.success('更新成功')
    } else {
      await videoApi.create(form)
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

async function handleUpload({ file, onFinish, onError }: UploadCustomRequestDetails) {
  const formData = new FormData()
  formData.append('file', file.file as File)
  formData.append('title', file.name)

  try {
    await videoApi.upload(formData)
    message.success('上传成功')
    showUploadModal.value = false
    loadData()
    onFinish()
  } catch (e: any) {
    message.error(e.message || '上传失败')
    onError()
  }
}

function handleDelete(id: number) {
  dialog.warning({
    title: '确认删除',
    content: '确定要删除这个视频吗？',
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await videoApi.delete(id)
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
.video-manage {
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
