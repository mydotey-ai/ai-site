<script setup lang="ts">
import { ref, onMounted, h } from 'vue'
import {
  NButton,
  NDataTable,
  NSelect,
  NSpace,
  NTag,
  useMessage,
  type DataTableColumns
} from 'naive-ui'
import { commentApi } from '@/api/comment'
import type { Comment, CommentQuery } from '@/types'

const message = useMessage()

// 数据
const comments = ref<Comment[]>([])
const loading = ref(false)
const total = ref(0)

// 查询参数
const query = ref<CommentQuery>({
  page: 1,
  size: 10,
  status: undefined
})

// 状态选项
const statusOptions = [
  { label: '全部', value: undefined },
  { label: '待审核', value: 'PENDING' },
  { label: '已通过', value: 'APPROVED' },
  { label: '垃圾', value: 'SPAM' }
]

// 表格列
const columns: DataTableColumns<Comment> = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '昵称', key: 'nickname', width: 120 },
  {
    title: '内容',
    key: 'content',
    ellipsis: { tooltip: true }
  },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row) => {
      const map: Record<string, { type: 'default' | 'success' | 'warning' | 'error'; label: string }> = {
        PENDING: { type: 'warning', label: '待审核' },
        APPROVED: { type: 'success', label: '已通过' },
        SPAM: { type: 'error', label: '垃圾' }
      }
      const { type, label } = map[row.status] || { type: 'default', label: '未知' }
      return h(NTag, { type, size: 'small' }, () => label)
    }
  },
  { title: '点赞', key: 'likeCount', width: 80 },
  {
    title: '时间',
    key: 'createdAt',
    width: 160,
    render: (row) => formatDate(row.createdAt)
  },
  {
    title: '操作',
    key: 'actions',
    width: 180,
    render: (row) => {
      return h(NSpace, { size: 'small' }, () => [
        row.status === 'PENDING'
          ? h(NButton, { size: 'small', type: 'success', onClick: () => handleApprove(row.id) }, () => '通过')
          : null,
        row.status !== 'SPAM'
          ? h(NButton, { size: 'small', type: 'warning', onClick: () => handleReject(row.id) }, () => '标记垃圾')
          : null,
        h(NButton, { size: 'small', type: 'error', onClick: () => handleDelete(row.id) }, () => '删除')
      ].filter(Boolean))
    }
  }
]

// 加载数据
async function loadData() {
  loading.value = true
  try {
    const res = await commentApi.getList(query.value)
    comments.value = res.list
    total.value = res.total
  } catch {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 通过
async function handleApprove(id: number) {
  try {
    await commentApi.approve(id)
    message.success('审核通过')
    loadData()
  } catch {
    message.error('操作失败')
  }
}

// 拒绝
async function handleReject(id: number) {
  try {
    await commentApi.reject(id)
    message.success('已标记为垃圾')
    loadData()
  } catch {
    message.error('操作失败')
  }
}

// 删除
async function handleDelete(id: number) {
  try {
    await commentApi.delete(id)
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

onMounted(loadData)
</script>

<template>
  <div class="comment-manage">
    <div class="header">
      <h1>评论管理</h1>
    </div>

    <div class="toolbar">
      <NSpace>
        <NSelect
          v-model:value="query.status"
          :options="statusOptions"
          placeholder="状态"
          style="width: 120px"
          @update:value="loadData"
        />
      </NSpace>
    </div>

    <NDataTable
      :columns="columns"
      :data="comments"
      :loading="loading"
      :pagination="{
        page: query.page,
        pageSize: query.size,
        itemCount: total,
        onChange: handlePageChange
      }"
      :row-key="(row: Comment) => row.id"
    />
  </div>
</template>

<style scoped lang="scss">
.comment-manage {
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
