<script setup lang="ts">
import { ref, onMounted, h, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
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
  NSelect,
  useMessage,
  type DataTableColumns
} from 'naive-ui'
import { novelApi } from '@/api/creation'
import type { Chapter, ChapterRequest, Novel } from '@/types'

const router = useRouter()
const route = useRoute()
const message = useMessage()

const novelId = computed(() => Number(route.params.novelId))
const novel = ref<Novel | null>(null)
const chapters = ref<Chapter[]>([])
const loading = ref(false)

// 编辑弹窗
const showModal = ref(false)
const editingChapter = ref<Chapter | null>(null)
const formLoading = ref(false)

const form = ref<ChapterRequest>({
  novelId: 0,
  title: '',
  content: '',
  chapterNo: undefined,
  status: 'DRAFT'
})

const statusOptions = [
  { label: '草稿', value: 'DRAFT' },
  { label: '已发布', value: 'PUBLISHED' }
]

const columns: DataTableColumns<Chapter> = [
  { title: '章节', key: 'chapterNo', width: 80 },
  { title: '标题', key: 'title', ellipsis: { tooltip: true } },
  { title: '字数', key: 'wordCount', width: 100 },
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
            default: () => '确定删除该章节吗？'
          }
        )
      ])
    }
  }
]

async function loadData() {
  loading.value = true
  try {
    const [novelRes, chapterRes] = await Promise.all([
      novel.value ? Promise.resolve(novel.value) : novelApi.getById(novelId.value),
      novelApi.getChapters(novelId.value)
    ])
    novel.value = novelRes as Novel
    chapters.value = chapterRes
  } catch {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

function handleCreate() {
  editingChapter.value = null
  const maxNo = chapters.value.reduce((max, c) => Math.max(max, c.chapterNo), 0)
  form.value = {
    novelId: novelId.value,
    title: '',
    content: '',
    chapterNo: maxNo + 1,
    status: 'DRAFT'
  }
  showModal.value = true
}

function handleEdit(chapter: Chapter) {
  editingChapter.value = chapter
  form.value = {
    novelId: chapter.novelId,
    title: chapter.title,
    content: chapter.content || '',
    chapterNo: chapter.chapterNo,
    status: chapter.status
  }
  showModal.value = true
}

async function handleSave() {
  if (!form.value.title) {
    message.error('请输入章节标题')
    return
  }

  formLoading.value = true
  try {
    if (editingChapter.value) {
      await novelApi.updateChapter(editingChapter.value.id, form.value)
      message.success('更新成功')
    } else {
      await novelApi.createChapter(novelId.value, form.value)
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
    await novelApi.publishChapter(id)
    message.success('发布成功')
    loadData()
  } catch {
    message.error('发布失败')
  }
}

async function handleDelete(id: number) {
  try {
    await novelApi.deleteChapter(id)
    message.success('删除成功')
    loadData()
  } catch {
    message.error('删除失败')
  }
}

function handleBack() {
  router.push('/creation/novels')
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
  <div class="chapter-manage">
    <div class="header">
      <div class="title-area">
        <NButton text @click="handleBack">
          <template #icon>
            <span class="back-icon">←</span>
          </template>
          返回
        </NButton>
        <h1>{{ novel?.title }} - 章节管理</h1>
      </div>
      <NButton type="primary" @click="handleCreate">新建章节</NButton>
    </div>

    <NDataTable
      :columns="columns"
      :data="chapters"
      :loading="loading"
      :row-key="(row: Chapter) => row.id"
    />

    <NModal
      v-model:show="showModal"
      :title="editingChapter ? '编辑章节' : '新建章节'"
      preset="card"
      style="width: 800px"
    >
      <NForm :model="form" label-width="80">
        <NFormItem label="章节序号">
          <NInputNumber v-model:value="form.chapterNo" :min="1" style="width: 120px" />
        </NFormItem>

        <NFormItem label="章节标题" required>
          <NInput v-model:value="form.title" placeholder="请输入章节标题" />
        </NFormItem>

        <NFormItem label="内容">
          <NInput
            v-model:value="form.content"
            type="textarea"
            placeholder="章节内容"
            :rows="15"
          />
        </NFormItem>

        <NFormItem label="状态">
          <NSelect v-model:value="form.status" :options="statusOptions" />
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
.chapter-manage {
  padding: 20px;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .title-area {
      display: flex;
      align-items: center;
      gap: 16px;

      h1 {
        margin: 0;
        font-size: 24px;
      }

      .back-icon {
        font-size: 18px;
      }
    }
  }
}
</style>
