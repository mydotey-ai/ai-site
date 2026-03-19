<script setup lang="ts">
import { ref, onMounted, h } from 'vue'
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
  NInputNumber,
  useMessage,
  type DataTableColumns
} from 'naive-ui'
import { userApi } from '@/api/user'
import type { User, UserRequest, UserQuery } from '@/types'

const message = useMessage()

// 数据
const users = ref<User[]>([])
const loading = ref(false)
const total = ref(0)

// 查询参数
const query = ref<UserQuery>({
  page: 1,
  size: 10,
  keyword: '',
  status: undefined
})

// 状态选项
const statusOptions = [
  { label: '全部', value: undefined },
  { label: '启用', value: 1 },
  { label: '禁用', value: 0 }
]

// 弹窗
const showModal = ref(false)
const modalTitle = ref('新建用户')
const formLoading = ref(false)
const formData = ref<UserRequest>({
  username: '',
  password: '',
  email: '',
  nickname: '',
  avatar: '',
  bio: '',
  status: 1
})
const editingId = ref<number | null>(null)

// 表格列
const columns: DataTableColumns<User> = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '用户名', key: 'username', width: 120 },
  { title: '昵称', key: 'nickname', width: 120 },
  { title: '邮箱', key: 'email', ellipsis: { tooltip: true } },
  {
    title: '状态',
    key: 'status',
    width: 80,
    render: (row) => {
      return row.status === 1
        ? h(NTag, { type: 'success', size: 'small' }, () => '启用')
        : h(NTag, { type: 'default', size: 'small' }, () => '禁用')
    }
  },
  {
    title: '角色',
    key: 'roles',
    width: 120,
    render: (row) => {
      if (!row.roles || row.roles.length === 0) return '-'
      return row.roles.map(r =>
        h(NTag, { type: 'info', size: 'small', style: 'margin-right: 4px' }, () => r)
      )
    }
  },
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
        row.status === 1
          ? h(NButton, { size: 'small', onClick: () => handleDisable(row.id) }, () => '禁用')
          : h(NButton, { size: 'small', type: 'success', onClick: () => handleEnable(row.id) }, () => '启用'),
        h(
          NPopconfirm,
          { onPositiveClick: () => handleDelete(row.id) },
          {
            trigger: () => h(NButton, { size: 'small', type: 'error' }, () => '删除'),
            default: () => '确定删除该用户吗？'
          }
        )
      ])
    }
  }
]

// 加载数据
async function loadData() {
  loading.value = true
  try {
    const res = await userApi.getList(query.value)
    users.value = res.list
    total.value = res.total
  } catch (e) {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 搜索
function handleSearch() {
  query.value.page = 1
  loadData()
}

// 重置
function handleReset() {
  query.value = { page: 1, size: 10 }
  loadData()
}

// 新建
function handleCreate() {
  modalTitle.value = '新建用户'
  editingId.value = null
  formData.value = {
    username: '',
    password: '',
    email: '',
    nickname: '',
    avatar: '',
    bio: '',
    status: 1
  }
  showModal.value = true
}

// 编辑
function handleEdit(user: User) {
  modalTitle.value = '编辑用户'
  editingId.value = user.id
  formData.value = {
    username: user.username,
    password: '',
    email: user.email || '',
    nickname: user.nickname || '',
    avatar: user.avatar || '',
    bio: user.bio || '',
    status: user.status
  }
  showModal.value = true
}

// 保存
async function handleSave() {
  formLoading.value = true
  try {
    if (editingId.value) {
      await userApi.update(editingId.value, formData.value)
      message.success('更新成功')
    } else {
      await userApi.create(formData.value)
      message.success('创建成功')
    }
    showModal.value = false
    loadData()
  } catch (e) {
    message.error('保存失败')
  } finally {
    formLoading.value = false
  }
}

// 启用
async function handleEnable(id: number) {
  try {
    await userApi.updateStatus(id, 1)
    message.success('已启用')
    loadData()
  } catch {
    message.error('操作失败')
  }
}

// 禁用
async function handleDisable(id: number) {
  try {
    await userApi.updateStatus(id, 0)
    message.success('已禁用')
    loadData()
  } catch {
    message.error('操作失败')
  }
}

// 删除
async function handleDelete(id: number) {
  try {
    await userApi.delete(id)
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
  if (!dateStr) return '-'
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
  <div class="user-list">
    <div class="header">
      <h1>用户管理</h1>
      <NButton type="primary" @click="handleCreate">新建用户</NButton>
    </div>

    <div class="toolbar">
      <NSpace>
        <NInput
          v-model:value="query.keyword"
          placeholder="搜索用户名/邮箱/昵称"
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
        <NButton @click="handleSearch">搜索</NButton>
        <NButton @click="handleReset">重置</NButton>
      </NSpace>
    </div>

    <NDataTable
      :columns="columns"
      :data="users"
      :loading="loading"
      :pagination="{
        page: query.page,
        pageSize: query.size,
        itemCount: total,
        onChange: handlePageChange
      }"
      :row-key="(row: User) => row.id"
    />

    <!-- 新建/编辑弹窗 -->
    <NModal
      v-model:show="showModal"
      :title="modalTitle"
      preset="dialog"
      style="width: 500px"
    >
      <NForm :model="formData" label-placement="left" label-width="80">
        <NFormItem label="用户名" required>
          <NInput v-model:value="formData.username" placeholder="请输入用户名" />
        </NFormItem>
        <NFormItem label="密码" :required="!editingId">
          <NInput
            v-model:value="formData.password"
            type="password"
            :placeholder="editingId ? '留空则不修改' : '请输入密码'"
          />
        </NFormItem>
        <NFormItem label="邮箱">
          <NInput v-model:value="formData.email" placeholder="请输入邮箱" />
        </NFormItem>
        <NFormItem label="昵称">
          <NInput v-model:value="formData.nickname" placeholder="请输入昵称" />
        </NFormItem>
        <NFormItem label="头像">
          <NInput v-model:value="formData.avatar" placeholder="头像URL" />
        </NFormItem>
        <NFormItem label="简介">
          <NInput
            v-model:value="formData.bio"
            type="textarea"
            placeholder="个人简介"
            :rows="3"
          />
        </NFormItem>
        <NFormItem label="状态">
          <NSelect
            v-model:value="formData.status"
            :options="[{ label: '启用', value: 1 }, { label: '禁用', value: 0 }]"
          />
        </NFormItem>
      </NForm>
      <template #action>
        <NSpace>
          <NButton @click="showModal = false">取消</NButton>
          <NButton type="primary" :loading="formLoading" @click="handleSave">
            保存
          </NButton>
        </NSpace>
      </template>
    </NModal>
  </div>
</template>

<style scoped lang="scss">
.user-list {
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
