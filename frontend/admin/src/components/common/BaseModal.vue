<script setup lang="ts">
import { NModal, NCard, NButton } from 'naive-ui'

interface Props {
  show: boolean
  title?: string
  width?: string | number
  closable?: boolean
  maskClosable?: boolean
  loading?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  title: '',
  width: 600,
  closable: true,
  maskClosable: true,
  loading: false
})

const emit = defineEmits<{
  (e: 'update:show', value: boolean): void
  (e: 'close'): void
  (e: 'confirm'): void
  (e: 'cancel'): void
}>()

function handleClose() {
  emit('update:show', false)
  emit('close')
}

function handleConfirm() {
  emit('confirm')
}

function handleCancel() {
  emit('update:show', false)
  emit('cancel')
}
</script>

<template>
  <NModal
    :show="show"
    :closable="closable"
    :mask-closable="maskClosable"
    :close-on-esc="true"
    @update:show="(value) => emit('update:show', value)"
    @close="handleClose"
  >
    <NCard
      :title="title"
      :bordered="false"
      :style="{ width: typeof width === 'number' ? `${width}px` : width }"
      role="dialog"
      aria-modal="true"
    >
      <template #header-extra v-if="closable">
        <NButton text @click="handleClose">
          ✕
        </NButton>
      </template>

      <div class="modal-content">
        <slot />
      </div>

      <template #footer v-if="$slots.footer || $slots.actions">
        <div class="modal-footer">
          <slot name="footer">
            <slot name="actions">
              <NButton @click="handleCancel">取消</NButton>
              <NButton type="primary" :loading="loading" @click="handleConfirm">
                确定
              </NButton>
            </slot>
          </slot>
        </div>
      </template>
    </NCard>
  </NModal>
</template>

<style scoped lang="scss">
.modal-content {
  padding: var(--spacing-sm, 8px) 0;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-sm, 8px);
}
</style>