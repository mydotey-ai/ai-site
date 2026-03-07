<script setup lang="ts">
</script>

<template>
  <div class="home-page">
    <!-- Hero Section -->
    <section class="hero">
      <div class="hero__content">
        <h1 class="hero__title">
          欢迎来到
          <span class="hero__title-highlight">AI-Site</span>
        </h1>
        <p class="hero__subtitle">
          个人网站 · 博客 · 作品集 · 创作
        </p>
        <p class="hero__description">
          探索技术、分享知识、记录成长
        </p>
        <div class="hero__actions">
          <router-link to="/blog" class="hero__btn hero__btn--primary">
            浏览博客
          </router-link>
          <router-link to="/portfolio" class="hero__btn hero__btn--secondary">
            查看作品
          </router-link>
        </div>
      </div>
    </section>

    <!-- 最新内容预览 -->
    <section class="preview">
      <div class="preview__grid">
        <!-- 博客预览 -->
        <div class="preview__section">
          <h2 class="preview__title">最新博客</h2>
          <div class="preview__list">
            <div class="preview__placeholder">
              <p>暂无文章</p>
            </div>
          </div>
        </div>

        <!-- 作品预览 -->
        <div class="preview__section">
          <h2 class="preview__title">精选作品</h2>
          <div class="preview__list">
            <div class="preview__placeholder">
              <p>暂无作品</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped lang="scss">
.home-page {
  padding-bottom: 48px;
}

// Hero 区域
.hero {
  padding: 80px 0;
  text-align: center;

  @media (max-width: 768px) {
    padding: 48px 0;
  }

  &__content {
    max-width: 640px;
    margin: 0 auto;
  }

  &__title {
    font-size: 48px;
    font-weight: 700;
    color: var(--text-primary);
    margin: 0 0 16px;
    line-height: 1.2;

    @media (max-width: 768px) {
      font-size: 32px;
    }
  }

  &__title-highlight {
    color: var(--color-primary);
    text-shadow: var(--glow-primary);
  }

  &__subtitle {
    font-size: 20px;
    color: var(--text-secondary);
    margin: 0 0 12px;

    @media (max-width: 768px) {
      font-size: 16px;
    }
  }

  &__description {
    font-size: 16px;
    color: var(--text-muted);
    margin: 0 0 32px;
  }

  &__actions {
    display: flex;
    gap: 16px;
    justify-content: center;
    flex-wrap: wrap;
  }

  &__btn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 12px 24px;
    font-size: 14px;
    font-weight: 500;
    border-radius: 8px;
    text-decoration: none;
    transition: all 0.2s;

    &--primary {
      background: var(--color-primary);
      color: white;

      &:hover {
        background: var(--color-primary-hover);
        box-shadow: var(--glow-primary);
      }
    }

    &--secondary {
      background: var(--bg-tertiary);
      color: var(--text-primary);
      border: 1px solid var(--border-primary);

      &:hover {
        background: var(--bg-hover);
        border-color: var(--color-primary);
      }
    }
  }
}

// 预览区域
.preview {
  &__grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 32px;

    @media (max-width: 768px) {
      grid-template-columns: 1fr;
      gap: 24px;
    }
  }

  &__section {
    background: var(--bg-tertiary);
    border-radius: 12px;
    padding: 24px;
  }

  &__title {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
    margin: 0 0 16px;
  }

  &__list {
    min-height: 120px;
  }

  &__placeholder {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 120px;
    border: 1px dashed var(--border-primary);
    border-radius: 8px;

    p {
      color: var(--text-muted);
      margin: 0;
    }
  }
}
</style>