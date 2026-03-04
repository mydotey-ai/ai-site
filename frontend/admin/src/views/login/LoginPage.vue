<script setup lang="ts">
</script>

<template>
  <div class="login-page">
    <div class="login-box">
      <h1>AI-Site Admin</h1>
      <p>管理后台登录页面</p>
    </div>
  </div>
</template>

<style scoped lang="scss">
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
}

.login-box {
  background: #fff;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  text-align: center;

  h1 {
    margin-bottom: 10px;
  }

  p {
    color: #999;
  }
}
</style>