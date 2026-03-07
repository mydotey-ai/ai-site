<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { NCard, NForm, NFormItem, NInput, NButton, NSpace, useMessage } from 'naive-ui'
import { useAuthStore } from '@/stores/auth'
import type { FormInst, FormRules } from 'naive-ui'

const router = useRouter()
const route = useRoute()
const message = useMessage()
const authStore = useAuthStore()

const formRef = ref<FormInst | null>(null)
const loading = ref(false)

const form = ref({
  username: '',
  password: ''
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ]
}

const redirect = computed(() => {
  return (route.query.redirect as string) || '/dashboard'
})

async function handleLogin() {
  try {
    await formRef.value?.validate()
  } catch {
    return
  }

  loading.value = true
  try {
    await authStore.login({
      username: form.value.username,
      password: form.value.password
    })

    message.success('登录成功')
    router.push(redirect.value)
  } catch (error: unknown) {
    const err = error as Error
    message.error(err.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <!-- 背景装饰 -->
    <div class="login-bg">
      <div class="login-bg__grid"></div>
      <div class="login-bg__glow login-bg__glow--1"></div>
      <div class="login-bg__glow login-bg__glow--2"></div>
    </div>

    <!-- 登录卡片 -->
    <NCard class="login-card" :bordered="false">
      <div class="login-header">
        <div class="login-header__logo">AI</div>
        <h1 class="login-header__title">AI-Site Admin</h1>
        <p class="login-header__subtitle">管理后台登录</p>
      </div>

      <NForm
        ref="formRef"
        :model="form"
        :rules="rules"
        label-placement="left"
        label-width="auto"
        class="login-form"
      >
        <NFormItem label="用户名" path="username">
          <NInput
            v-model:value="form.username"
            placeholder="请输入用户名"
            @keyup.enter="handleLogin"
          />
        </NFormItem>

        <NFormItem label="密码" path="password">
          <NInput
            v-model:value="form.password"
            type="password"
            placeholder="请输入密码"
            show-password-on="click"
            @keyup.enter="handleLogin"
          />
        </NFormItem>

        <NFormItem :show-label="false">
          <NSpace vertical style="width: 100%">
            <NButton
              type="primary"
              block
              :loading="loading"
              @click="handleLogin"
            >
              登录
            </NButton>
          </NSpace>
        </NFormItem>
      </NForm>
    </NCard>
  </div>
</template>

<style scoped lang="scss">
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  background: var(--bg-primary, #0a0a0f);
}

// 背景装饰
.login-bg {
  position: absolute;
  inset: 0;
  overflow: hidden;
  pointer-events: none;

  // 网格背景
  &__grid {
    position: absolute;
    inset: 0;
    background-image:
      linear-gradient(var(--border-primary, #2d2d3a) 1px, transparent 1px),
      linear-gradient(90deg, var(--border-primary, #2d2d3a) 1px, transparent 1px);
    background-size: 50px 50px;
    opacity: 0.3;
  }

  // 光晕效果
  &__glow {
    position: absolute;
    border-radius: 50%;
    filter: blur(80px);

    &--1 {
      top: -200px;
      right: -100px;
      width: 400px;
      height: 400px;
      background: rgba(59, 130, 246, 0.15);
    }

    &--2 {
      bottom: -200px;
      left: -100px;
      width: 400px;
      height: 400px;
      background: rgba(6, 182, 212, 0.1);
    }
  }
}

// 登录卡片
.login-card {
  width: 400px;
  max-width: 90%;
  background: var(--bg-tertiary, #1a1a24) !important;
  border: 1px solid var(--border-primary, #2d2d3a);
  border-radius: 16px;
  position: relative;
  z-index: 1;
}

// 登录头部
.login-header {
  text-align: center;
  margin-bottom: 32px;

  &__logo {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 56px;
    height: 56px;
    background: var(--color-primary, #3b82f6);
    border-radius: 12px;
    font-size: 24px;
    font-weight: 700;
    color: white;
    margin-bottom: 16px;
    box-shadow: var(--glow-primary, 0 0 20px rgba(59, 130, 246, 0.3));
  }

  &__title {
    font-size: 24px;
    font-weight: 600;
    color: var(--text-primary, #f8fafc);
    margin: 0 0 8px;
  }

  &__subtitle {
    font-size: 14px;
    color: var(--text-secondary, #94a3b8);
    margin: 0;
  }
}

// 登录表单
.login-form {
  :deep(.n-form-item-label) {
    color: var(--text-primary, #f8fafc);
  }
}
</style>