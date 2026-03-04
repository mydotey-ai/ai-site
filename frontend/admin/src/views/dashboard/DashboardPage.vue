<script setup lang="ts">
import { NCard, NStatistic, NGrid, NGi } from 'naive-ui'
</script>

<template>
  <div class="dashboard-page">
    <h1>仪表盘</h1>
    <NGrid :cols="4" :x-gap="16" :y-gap="16">
      <NGi>
        <NCard>
          <NStatistic label="文章数量" :value="0" />
        </NCard>
      </NGi>
      <NGi>
        <NCard>
          <NStatistic label="作品数量" :value="0" />
        </NCard>
      </NGi>
      <NGi>
        <NCard>
          <NStatistic label="访问量" :value="0" />
        </NCard>
      </NGi>
      <NGi>
        <NCard>
          <NStatistic label="评论数" :value="0" />
        </NCard>
      </NGi>
    </NGrid>
  </div>
</template>

<style scoped lang="scss">
.dashboard-page {
  padding: 20px;

  h1 {
    margin-bottom: 20px;
  }
}
</style>