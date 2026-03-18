<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  NForm,
  NFormItem,
  NInput,
  NSelect,
  NSwitch,
  NButton,
  NSpace,
  NCard,
  useMessage,
  type FormRules,
  type FormInst
} from 'naive-ui'
import { articleApi } from '@/api/article'
import { categoryApi } from '@/api/category'
import { tagApi } from '@/api/tag'
import type { ArticleRequest, Category, Tag } from '@/types'

const route = useRoute()
const router = useRouter()
const message = useMessage()

// 表单引用
const formRef = ref<FormInst | null>(null)

// 是否编辑模式
const isEdit = computed(() => !!route.params.id)
const articleId = computed(() => Number(route.params.id))

// 数据
const categories = ref<Category[]>([])
const tags = ref<Tag[]>([])
const loading = ref(false)
const submitting = ref(false)

// 表单数据
const formData = ref<ArticleRequest>({
  title: '',
  slug: '',
  summary: '',
  content: '',
  contentType: 'MARKDOWN',
  coverImage: '',
  categoryId: undefined,
  tagIds: [],
  status: 0,
  isTop: 0,
  allowComment: 1,
  seoTitle: '',
  seoDescription: '',
  seoKeywords: ''
})

// 表单规则
const rules: FormRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  slug: [
    { required: true, message: '请输入别名', trigger: 'blur' },
    { pattern: /^[a-z0-9-]+$/, message: '别名只能包含小写字母、数字和连字符', trigger: 'blur' }
  ]
}

// 分类选项
const categoryOptions = computed(() =>
  categories.value.map(c => ({ label: c.name, value: c.id }))
)

// 标签选项
const tagOptions = computed(() =>
  tags.value.map(t => ({ label: t.name, value: t.id }))
)

// 状态选项
const statusOptions = [
  { label: '草稿', value: 0 },
  { label: '已发布', value: 1 },
  { label: '隐藏', value: 2 }
]

// 加载数据
async function loadData() {
  loading.value = true
  try {
    const [categoryRes, tagRes] = await Promise.all([
      categoryApi.getList(),
      tagApi.getList()
    ])
    categories.value = categoryRes
    tags.value = tagRes

    // 编辑模式加载文章数据
    if (isEdit.value) {
      const article = await articleApi.getById(articleId.value)
      formData.value = {
        title: article.title,
        slug: article.slug,
        summary: article.summary || '',
        content: article.content || '',
        contentType: article.contentType || 'MARKDOWN',
        coverImage: article.coverImage || '',
        categoryId: article.categoryId || undefined,
        tagIds: article.tags?.map(t => t.id) || [],
        status: article.status,
        isTop: article.isTop,
        allowComment: article.allowComment,
        seoTitle: article.seoTitle || '',
        seoDescription: article.seoDescription || '',
        seoKeywords: article.seoKeywords || ''
      }
    }
  } catch {
    message.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 生成 slug
function generateSlug() {
  if (!formData.value.title) return
  // 简单的拼音转换，实际项目可以使用 pinyin 库
  const slug = formData.value.title
    .toLowerCase()
    .replace(/\s+/g, '-')
    .replace(/[^a-z0-9-]/g, '')
  formData.value.slug = slug
}

// 提交
async function handleSubmit() {
  try {
    await formRef.value?.validate()
  } catch {
    return
  }

  submitting.value = true
  try {
    if (isEdit.value) {
      await articleApi.update(articleId.value, formData.value)
      message.success('更新成功')
    } else {
      await articleApi.create(formData.value)
      message.success('创建成功')
    }
    router.push('/blog')
  } catch {
    message.error(isEdit.value ? '更新失败' : '创建失败')
  } finally {
    submitting.value = false
  }
}

// 保存草稿
async function handleSaveDraft() {
  formData.value.status = 0
  await handleSubmit()
}

// 发布
async function handlePublish() {
  formData.value.status = 1
  await handleSubmit()
}

// 取消
function handleCancel() {
  router.push('/blog')
}

onMounted(loadData)
</script>

<template>
  <div class="article-edit">
    <div class="header">
      <h1>{{ isEdit ? '编辑文章' : '新建文章' }}</h1>
      <NSpace>
        <NButton @click="handleCancel">取消</NButton>
        <NButton @click="handleSaveDraft" :loading="submitting">保存草稿</NButton>
        <NButton type="primary" @click="handlePublish" :loading="submitting">发布</NButton>
      </NSpace>
    </div>

    <NCard :loading="loading">
      <NForm
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="left"
        label-width="100px"
      >
        <NFormItem label="标题" path="title">
          <NInput
            v-model:value="formData.title"
            placeholder="请输入文章标题"
            @blur="!formData.slug && generateSlug()"
          />
        </NFormItem>

        <NFormItem label="别名" path="slug">
          <NInput v-model:value="formData.slug" placeholder="url-slug" />
        </NFormItem>

        <NFormItem label="摘要" path="summary">
          <NInput
            v-model:value="formData.summary"
            type="textarea"
            :rows="3"
            placeholder="文章摘要（可选）"
          />
        </NFormItem>

        <NFormItem label="内容" path="content">
          <NInput
            v-model:value="formData.content"
            type="textarea"
            :rows="20"
            placeholder="支持 Markdown 格式"
          />
        </NFormItem>

        <NFormItem label="封面图" path="coverImage">
          <NInput v-model:value="formData.coverImage" placeholder="封面图片URL" />
        </NFormItem>

        <NFormItem label="分类" path="categoryId">
          <NSelect
            v-model:value="formData.categoryId"
            :options="categoryOptions"
            placeholder="选择分类"
            clearable
          />
        </NFormItem>

        <NFormItem label="标签" path="tagIds">
          <NSelect
            v-model:value="formData.tagIds"
            :options="tagOptions"
            placeholder="选择标签"
            multiple
            clearable
          />
        </NFormItem>

        <NFormItem label="状态" path="status">
          <NSelect v-model:value="formData.status" :options="statusOptions" />
        </NFormItem>

        <NFormItem label="置顶" path="isTop">
          <NSwitch v-model:value="formData.isTop" :checked-value="1" :unchecked-value="0" />
        </NFormItem>

        <NFormItem label="允许评论" path="allowComment">
          <NSwitch v-model:value="formData.allowComment" :checked-value="1" :unchecked-value="0" />
        </NFormItem>

        <NFormItem label="SEO 标题" path="seoTitle">
          <NInput v-model:value="formData.seoTitle" placeholder="SEO 标题（可选）" />
        </NFormItem>

        <NFormItem label="SEO 描述" path="seoDescription">
          <NInput v-model:value="formData.seoDescription" placeholder="SEO 描述（可选）" />
        </NFormItem>

        <NFormItem label="SEO 关键词" path="seoKeywords">
          <NInput v-model:value="formData.seoKeywords" placeholder="SEO 关键词（可选）" />
        </NFormItem>
      </NForm>
    </NCard>
  </div>
</template>

<style scoped lang="scss">
.article-edit {
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
