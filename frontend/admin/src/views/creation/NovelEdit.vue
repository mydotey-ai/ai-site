<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  NForm,
  NFormItem,
  NInput,
  NSelect,
  NButton,
  NSpace,
  useMessage,
  type FormInst
} from 'naive-ui'
import { novelApi } from '@/api/creation'
import type { NovelRequest, NovelCategory } from '@/types'

const router = useRouter()
const route = useRoute()
const message = useMessage()

const formRef = ref<FormInst | null>(null)
const loading = ref(false)
const categories = ref<NovelCategory[]>([])
const isEdit = computed(() => !!route.params.id)

const form = ref<NovelRequest>({
  title: '',
  slug: '',
  author: '',
  summary: '',
  coverImage: '',
  categoryId: undefined,
  status: 'DRAFT'
})

const statusOptions = [
  { label: '草稿', value: 'DRAFT' },
  { label: '连载中', value: 'PUBLISHED' },
  { label: '已完结', value: 'COMPLETED' }
]

const categoryOptions = computed(() =>
  categories.value.map(c => ({ label: c.name, value: c.id }))
)

const rules = {
  title: [{ required: true, message: '请输入小说标题', trigger: 'blur' }]
}

async function loadCategories() {
  try {
    categories.value = await novelApi.getCategories()
  } catch {
    message.error('加载分类失败')
  }
}

async function loadNovel() {
  if (!route.params.id) return
  loading.value = true
  try {
    const novel = await novelApi.getById(Number(route.params.id))
    form.value = {
      title: novel.title,
      slug: novel.slug,
      author: novel.author,
      summary: novel.summary,
      coverImage: novel.coverImage,
      categoryId: novel.categoryId,
      status: novel.status
    }
  } catch {
    message.error('加载失败')
    router.back()
  } finally {
    loading.value = false
  }
}

async function handleSubmit() {
  try {
    await formRef.value?.validate()
  } catch {
    return
  }

  loading.value = true
  try {
    if (isEdit.value) {
      await novelApi.update(Number(route.params.id), form.value)
      message.success('更新成功')
    } else {
      await novelApi.create(form.value)
      message.success('创建成功')
    }
    router.push('/creation/novels')
  } catch {
    message.error('保存失败')
  } finally {
    loading.value = false
  }
}

function handleCancel() {
  router.back()
}

onMounted(async () => {
  await loadCategories()
  if (isEdit.value) {
    await loadNovel()
  }
})

import { computed } from 'vue'
</script>

<template>
  <div class="novel-edit">
    <div class="header">
      <h1>{{ isEdit ? '编辑小说' : '新建小说' }}</h1>
    </div>

    <NForm ref="formRef" :model="form" :rules="rules" label-width="100">
      <NFormItem label="标题" path="title">
        <NInput v-model:value="form.title" placeholder="请输入小说标题" />
      </NFormItem>

      <NFormItem label="别名" path="slug">
        <NInput v-model:value="form.slug" placeholder="URL别名，留空自动生成" />
      </NFormItem>

      <NFormItem label="作者" path="author">
        <NInput v-model:value="form.author" placeholder="作者名称" />
      </NFormItem>

      <NFormItem label="分类" path="categoryId">
        <NSelect
          v-model:value="form.categoryId"
          :options="categoryOptions"
          placeholder="选择分类"
          clearable
        />
      </NFormItem>

      <NFormItem label="简介" path="summary">
        <NInput
          v-model:value="form.summary"
          type="textarea"
          placeholder="小说简介"
          :rows="4"
        />
      </NFormItem>

      <NFormItem label="封面图" path="coverImage">
        <NInput v-model:value="form.coverImage" placeholder="封面图URL" />
      </NFormItem>

      <NFormItem label="状态" path="status">
        <NSelect v-model:value="form.status" :options="statusOptions" />
      </NFormItem>

      <NFormItem>
        <NSpace>
          <NButton type="primary" :loading="loading" @click="handleSubmit">
            保存
          </NButton>
          <NButton @click="handleCancel">取消</NButton>
        </NSpace>
      </NFormItem>
    </NForm>
  </div>
</template>

<style scoped lang="scss">
.novel-edit {
  padding: 20px;

  .header {
    margin-bottom: 20px;

    h1 {
      margin: 0;
      font-size: 24px;
    }
  }
}
</style>
