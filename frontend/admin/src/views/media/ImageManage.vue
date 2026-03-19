<template>
  <div class="image-manage">
    <div class="header">
      <h1>图片管理</h1>
      <n-space>
        <n-button type="primary" @click="showUploadModal = true">
          上传图片
        </n-button>
      </n-space>
    </div>

    <div class="toolbar">
      <n-space>
        <n-input v-model:value="query.keyword" placeholder="搜索图片..." clearable @keyup.enter="handleSearch" style="width: 200px" />
        <n-select
          v-model:value="query.albumId"
          :options="albumOptions"
          placeholder="选择相册"
          clearable
          style="width: 150px"
        />
        <n-button @click="handleSearch">搜索</n-button>
        <n-button @click="handleReset">重置</n-button>
      </n-space>
      <n-space v-if="selectedIds.length > 0">
        <n-text>已选 {{ selectedIds.length }} 项</n-text>
        <n-button size="small" type="error" @click="handleBatchDelete">批量删除</n-button>
      </n-space>
    </div>

    <div class="content">
      <n-spin :show="loading">
        <div v-if="images.length === 0" class="empty">
          <n-empty description="暂无图片" />
        </div>
        <div v-else class="image-grid">
          <div
            v-for="image in images"
            :key="image.id"
            class="image-card"
            :class="{ selected: selectedIds.includes(image.id) }"
            @click="handleToggleSelect(image.id)"
          >
            <div class="image-wrapper">
              <img :src="image.thumbnailUrl || image.url" :alt="image.title" />
              <div class="image-overlay">
                <n-space>
                  <n-button size="tiny" @click.stop="handlePreview(image)">预览</n-button>
                  <n-button size="tiny" @click.stop="handleEdit(image)">编辑</n-button>
                  <n-button size="tiny" type="error" @click.stop="handleDelete(image.id)">删除</n-button>
                </n-space>
              </div>
            </div>
            <div class="image-info">
              <n-ellipsis :line-clamp="1">{{ image.title || image.originalName }}</n-ellipsis>
              <n-text depth="3" style="font-size: 12px">{{ formatSize(image.size) }}</n-text>
            </div>
          </div>
        </div>
      </n-spin>

      <div class="pagination">
        <n-pagination
          v-model:page="query.page"
          :page-size="query.size"
          :item-count="total"
          @update:page="loadData"
        />
      </div>
    </div>

    <!-- 上传弹窗 -->
    <n-modal v-model:show="showUploadModal" preset="card" title="上传图片" style="width: 500px">
      <n-upload
        multiple
        :custom-request="handleUpload"
        list-type="image-card"
        accept="image/*"
      >
        <n-upload-dragger>
          <div style="margin-bottom: 12px">
            <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="17 8 12 3 7 8"/><line x1="12" y1="3" x2="12" y2="15"/></svg>
          </div>
          <n-text style="font-size: 16px">点击或拖拽文件到此区域上传</n-text>
        </n-upload-dragger>
      </n-upload>
    </n-modal>

    <!-- 编辑弹窗 -->
    <n-modal v-model:show="showEditModal" preset="card" title="编辑图片" style="width: 500px">
      <n-form ref="formRef" :model="editForm" label-placement="left" label-width="80">
        <n-form-item label="标题" path="title">
          <n-input v-model:value="editForm.title" />
        </n-form-item>
        <n-form-item label="描述" path="description">
          <n-input v-model:value="editForm.description" type="textarea" :rows="3" />
        </n-form-item>
        <n-form-item label="相册" path="albumId">
          <n-select v-model:value="editForm.albumId" :options="albumOptions" clearable />
        </n-form-item>
        <n-form-item label="标签" path="tags">
          <n-dynamic-tags v-model:value="editForm.tags" />
        </n-form-item>
        <n-form-item label="公开" path="isPublic">
          <n-switch v-model:value="editForm.isPublic" :checked-value="1" :unchecked-value="0" />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="showEditModal = false">取消</n-button>
          <n-button type="primary" :loading="submitting" @click="handleSave">保存</n-button>
        </n-space>
      </template>
    </n-modal>

    <!-- 预览弹窗 -->
    <n-modal v-model:show="showPreviewModal" preset="card" style="max-width: 90vw; max-height: 90vh">
      <img :src="previewImage?.url" :alt="previewImage?.title" style="max-width: 100%; max-height: 80vh" />
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useMessage, useDialog, type UploadCustomRequestDetails } from 'naive-ui'
import { imageApi, albumApi } from '@/api/media'
import type { Image, ImageRequest, Album } from '@/types'

const message = useMessage()
const dialog = useDialog()

const loading = ref(false)
const submitting = ref(false)
const images = ref<Image[]>([])
const total = ref(0)
const albums = ref<Album[]>([])
const selectedIds = ref<number[]>([])

const query = ref({
  page: 1,
  size: 20,
  keyword: '',
  albumId: undefined as number | undefined
})

const showUploadModal = ref(false)
const showEditModal = ref(false)
const showPreviewModal = ref(false)
const previewImage = ref<Image | null>(null)
const editForm = ref<ImageRequest>({
  title: '',
  description: '',
  albumId: undefined,
  tags: [],
  isPublic: 1
})
const editId = ref<number | null>(null)

const albumOptions = computed(() => [
  { label: '全部', value: undefined },
  ...albums.value.map(a => ({ label: a.name, value: a.id }))
])

onMounted(() => {
  loadData()
  loadAlbums()
})

async function loadData() {
  loading.value = true
  try {
    const res = await imageApi.getList(query.value)
    images.value = res.list
    total.value = res.total
  } catch (e: any) {
    message.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

async function loadAlbums() {
  try {
    const res = await albumApi.getList({ page: 1, size: 100 })
    albums.value = res.list
  } catch (e) {
    // ignore
  }
}

function handleSearch() {
  query.value.page = 1
  loadData()
}

function handleReset() {
  query.value = { page: 1, size: 20, keyword: '', albumId: undefined }
  loadData()
}

function handleToggleSelect(id: number) {
  const idx = selectedIds.value.indexOf(id)
  if (idx >= 0) {
    selectedIds.value.splice(idx, 1)
  } else {
    selectedIds.value.push(id)
  }
}

function handlePreview(image: Image) {
  previewImage.value = image
  showPreviewModal.value = true
}

function handleEdit(image: Image) {
  editId.value = image.id
  editForm.value = {
    title: image.title,
    description: image.description,
    albumId: image.albumId,
    tags: image.tags || [],
    isPublic: image.isPublic
  }
  showEditModal.value = true
}

async function handleSave() {
  if (!editId.value) return
  submitting.value = true
  try {
    await imageApi.update(editId.value, editForm.value)
    message.success('保存成功')
    showEditModal.value = false
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
    content: '确定要删除这张图片吗？',
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await imageApi.delete(id)
        message.success('删除成功')
        loadData()
      } catch (e: any) {
        message.error(e.message || '删除失败')
      }
    }
  })
}

async function handleBatchDelete() {
  dialog.warning({
    title: '确认批量删除',
    content: `确定要删除选中的 ${selectedIds.value.length} 张图片吗？`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await imageApi.batch({ action: 'delete', ids: selectedIds.value })
        message.success('删除成功')
        selectedIds.value = []
        loadData()
      } catch (e: any) {
        message.error(e.message || '删除失败')
      }
    }
  })
}

async function handleUpload({ file, onFinish, onError }: UploadCustomRequestDetails) {
  const formData = new FormData()
  formData.append('file', file.file as File)

  try {
    await imageApi.upload(formData)
    message.success('上传成功')
    loadData()
    onFinish()
    showUploadModal.value = false
  } catch (e: any) {
    message.error(e.message || '上传失败')
    onError()
  }
}

function formatSize(bytes: number): string {
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  return (bytes / (1024 * 1024)).toFixed(1) + ' MB'
}
</script>

<style scoped lang="scss">
.image-manage {
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

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.content {
  background: var(--bg-tertiary, #1a1a24);
  border-radius: 12px;
  padding: 20px;
}

.empty {
  padding: 60px 0;
}

.image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 16px;
}

.image-card {
  background: var(--bg-secondary, #12121a);
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  border: 2px solid transparent;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);

    .image-overlay {
      opacity: 1;
    }
  }

  &.selected {
    border-color: #3b82f6;
  }
}

.image-wrapper {
  position: relative;
  aspect-ratio: 1;
  overflow: hidden;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.image-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s;
}

.image-info {
  padding: 8px 12px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
