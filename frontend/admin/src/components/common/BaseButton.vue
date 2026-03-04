<script setup lang="ts">
import { computed } from 'vue'
import { NButton } from 'naive-ui'

interface Props {
  type?: 'primary' | 'default' | 'tertiary' | 'success' | 'warning' | 'error' | 'info'
  size?: 'tiny' | 'small' | 'medium' | 'large'
  loading?: boolean
  disabled?: boolean
  block?: boolean
  text?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  type: 'default',
  size: 'medium',
  loading: false,
  disabled: false,
  block: false,
  text: false
})

const emit = defineEmits<{
  (e: 'click', event: MouseEvent): void
}>()

const buttonType = computed(() => {
  if (props.text) return 'default'
  return props.type
})

function handleClick(event: MouseEvent) {
  if (!props.disabled && !props.loading) {
    emit('click', event)
  }
}
</script>

<template>
  <NButton
    :type="buttonType"
    :size="size"
    :loading="loading"
    :disabled="disabled"
    :block="block"
    :text="text"
    @click="handleClick"
  >
    <slot />
  </NButton>
</template>

<style scoped lang="scss">
</style>