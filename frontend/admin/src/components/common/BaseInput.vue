<script setup lang="ts">
import { computed } from 'vue'
import { NInput } from 'naive-ui'

interface Props {
  modelValue?: string
  type?: 'text' | 'password' | 'textarea'
  placeholder?: string
  disabled?: boolean
  readonly?: boolean
  clearable?: boolean
  maxlength?: number
  showCount?: boolean
  rows?: number
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  type: 'text',
  placeholder: '',
  disabled: false,
  readonly: false,
  clearable: false,
  showCount: false,
  rows: 3
})

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
  (e: 'change', value: string): void
  (e: 'focus', event: FocusEvent): void
  (e: 'blur', event: FocusEvent): void
}>()

const inputRows = computed(() => {
  return props.type === 'textarea' ? props.rows : undefined
})

function handleUpdateValue(value: string) {
  emit('update:modelValue', value)
}

function handleChange(value: string) {
  emit('change', value)
}

function handleFocus(event: FocusEvent) {
  emit('focus', event)
}

function handleBlur(event: FocusEvent) {
  emit('blur', event)
}
</script>

<template>
  <NInput
    :value="modelValue"
    :type="type"
    :placeholder="placeholder"
    :disabled="disabled"
    :readonly="readonly"
    :clearable="clearable"
    :maxlength="maxlength"
    :show-count="showCount"
    :rows="inputRows"
    @update:value="handleUpdateValue"
    @change="handleChange"
    @focus="handleFocus"
    @blur="handleBlur"
  />
</template>

<style scoped lang="scss">
</style>