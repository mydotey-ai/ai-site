<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  NForm,
  NFormItem,
  NInput,
  NSelect,
  NSpace,
  NButton,
  NCard,
  NDynamicTags,
  NIcon,
  useMessage
} from 'naive-ui'
import { AddOutline, CloseOutline } from '@vicons/ionicons5'
import { projectApi, projectTagApi } from '@/api/portfolio'
import type { ProjectTag, ProjectRequest, ProjectLinkRequest } from '@/types'

const route = useRoute()
const router = useRouter()
const message = useMessage()

// 是否编辑模式
const isEdit = computed(() => !!route.params.id)
const projectId = computed(() => Number(route.params.id))

// 标签列表
const tags = ref<ProjectTag[]>([])

// 表单数据
const form = ref<ProjectRequest>({
  name: '',
  slug: '',
  description: '',
  content: '',
  coverImage: '',
  techStack: [],
  tagIds: [],
  links: [],
  status: 'DEVELOPING'
})

// 链接列表
const links = ref<ProjectLinkRequest[]>([])

// 加载中
const loading = ref(false)
const submitting = ref(false)

// 状态选项
const statusOptions = [
  { label: '开发中', value: 'DEVELOPING' },
  { label: '已发布', value: 'RELEASED' },
  { label: '已归档', value: 'ARCHIVED' }
]

// 链接类型选项
const linkTypeOptions = [
  { label: '演示', value: 'DEMO' },
  { label: '源码', value: 'SOURCE' },
  { label: '文档', value: 'DOCS' },
  { label: '其他', value: 'OTHER' }
]

// 标签选项
const tagOptions = computed(() =>
  tags.value.map(t => ({ label: t.name, value: t.id }))
)

// 加载标签
async function loadTags() {
  try {
    tags.value = await projectTagApi.getList() as ProjectTag[]
  } catch {
    // ignore
  }
}

// 加载项目详情
async function loadProject() {
  if (!isEdit.value) return
  loading.value = true
  try {
    const project = await projectApi.getById(projectId.value)
    form.value = {
      name: project.name,
      slug: project.slug,
      description: project.description,
      content: project.content,
      coverImage: project.coverImage,
      techStack: project.techStack || [],
      tagIds: project.tags?.map(t => t.id) || [],
      links: [],
      status: project.status
    }
    links.value = project.links || []
  } catch {
    message.error('加载失败')
    router.push('/portfolio')
  } finally {
    loading.value = false
  }
}

// 添加链接
function addLink() {
  links.value.push({ type: 'DEMO', label: '', url: '' })
}

// 删除链接
function removeLink(index: number) {
  links.value.splice(index, 1)
}

// 提交
async function handleSubmit() {
  if (!form.value.name) {
    message.error('请输入项目名称')
    return
  }
  submitting.value = true
  try {
    const data: ProjectRequest = {
      ...form.value,
      links: links.value.filter(l => l.url)
    }
    if (isEdit.value) {
      await projectApi.update(projectId.value, data)
      message.success('更新成功')
    } else {
      await projectApi.create(data)
      message.success('创建成功')
    }
    router.push('/portfolio')
  } catch {
    message.error(isEdit.value ? '更新失败' : '创建失败')
  } finally {
    submitting.value = false
  }
}

// 返回
function handleBack() {
  router.push('/portfolio')
}

onMounted(() => {
  loadTags()
  loadProject()
})
</script>

<template>
  <div class="project-edit">
    <div class="header">
      <h1>{{ isEdit ? '编辑项目' : '新建项目' }}</h1>
    </div>

    <NCard :loading="loading">
      <NForm :model="form" label-placement="left" label-width="100px">
        <NFormItem label="项目名称" required>
          <NInput v-model:value="form.name" placeholder="请输入项目名称" />
        </NFormItem>

        <NFormItem label="Slug">
          <NInput v-model:value="form.slug" placeholder="URL别名，留空自动生成" />
        </NFormItem>

        <NFormItem label="描述">
          <NInput
            v-model:value="form.description"
            type="textarea"
            placeholder="项目简短描述"
            :rows="3"
          />
        </NFormItem>

        <NFormItem label="封面图">
          <NInput v-model:value="form.coverImage" placeholder="封面图URL" />
        </NFormItem>

        <NFormItem label="技术栈">
          <NDynamicTags v-model:value="form.techStack" />
        </NFormItem>

        <NFormItem label="标签">
          <NSelect
            v-model:value="form.tagIds"
            :options="tagOptions"
            multiple
            placeholder="选择标签"
          />
        </NFormItem>

        <NFormItem label="状态">
          <NSelect v-model:value="form.status" :options="statusOptions" />
        </NFormItem>

        <NFormItem label="项目链接">
          <div class="links-editor">
            <div v-for="(link, index) in links" :key="index" class="link-item">
              <NSelect
                v-model:value="link.type"
                :options="linkTypeOptions"
                style="width: 100px"
              />
              <NInput v-model:value="link.label" placeholder="标签" style="width: 120px" />
              <NInput v-model:value="link.url" placeholder="链接地址" style="flex: 1" />
              <NButton text type="error" @click="removeLink(index)">
                <template #icon>
                  <NIcon :component="CloseOutline" />
                </template>
              </NButton>
            </div>
            <NButton dashed block @click="addLink">
              <template #icon>
                <NIcon :component="AddOutline" />
              </template>
              添加链接
            </NButton>
          </div>
        </NFormItem>

        <NFormItem label="详细内容">
          <NInput
            v-model:value="form.content"
            type="textarea"
            placeholder="项目详细介绍（Markdown格式）"
            :rows="10"
          />
        </NFormItem>

        <NFormItem :show-label="false">
          <NSpace>
            <NButton type="primary" :loading="submitting" @click="handleSubmit">
              {{ isEdit ? '更新' : '创建' }}
            </NButton>
            <NButton @click="handleBack">取消</NButton>
          </NSpace>
        </NFormItem>
      </NForm>
    </NCard>
  </div>
</template>

<style scoped lang="scss">
.project-edit {
  padding: 20px;

  .header {
    margin-bottom: 20px;

    h1 {
      margin: 0;
      font-size: 24px;
    }
  }
}

.links-editor {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.link-item {
  display: flex;
  gap: 8px;
  align-items: center;
}
</style>
