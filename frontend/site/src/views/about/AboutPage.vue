<script setup lang="ts">
import { ref } from 'vue'

// 个人信息（可后续改为从API获取）
const profile = ref({
  name: 'AI-Site',
  title: '全栈开发者 / 技术爱好者',
  bio: '热爱技术，专注于全栈开发、人工智能与创意编程。善于用代码解决问题，享受从零构建产品的过程。',
  avatar: '',
  location: '中国',
  email: 'hello@ai-site.com',
  website: 'https://ai-site.com'
})

const skills = ref([
  { name: 'Vue.js', level: 90 },
  { name: 'TypeScript', level: 85 },
  { name: 'Java/Spring', level: 80 },
  { name: 'Python', level: 75 },
  { name: 'Docker', level: 70 },
  { name: 'AI/ML', level: 65 }
])

const stats = ref([
  { label: '博客文章', value: 0 },
  { label: '开源项目', value: 0 },
  { label: '创作作品', value: 0 }
])

const socialLinks = ref([
  { name: 'GitHub', url: 'https://github.com', icon: 'github' },
  { name: 'Twitter', url: 'https://twitter.com', icon: 'twitter' },
  { name: 'Email', url: 'mailto:hello@ai-site.com', icon: 'email' }
])
</script>

<template>
  <div class="about-page">
    <!-- Hero Section -->
    <section class="hero">
      <div class="hero__avatar">
        <div class="avatar-placeholder">
          <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
            <circle cx="12" cy="7" r="4"/>
          </svg>
        </div>
      </div>
      <h1 class="hero__name">{{ profile.name }}</h1>
      <p class="hero__title">{{ profile.title }}</p>
      <p class="hero__bio">{{ profile.bio }}</p>
    </section>

    <!-- Stats Section -->
    <section class="stats">
      <div class="stats__grid">
        <div v-for="stat in stats" :key="stat.label" class="stat-card">
          <span class="stat-card__value">{{ stat.value }}</span>
          <span class="stat-card__label">{{ stat.label }}</span>
        </div>
      </div>
    </section>

    <!-- Skills Section -->
    <section class="skills">
      <h2 class="section-title">技能</h2>
      <div class="skills__list">
        <div v-for="skill in skills" :key="skill.name" class="skill-item">
          <div class="skill-item__header">
            <span class="skill-item__name">{{ skill.name }}</span>
            <span class="skill-item__level">{{ skill.level }}%</span>
          </div>
          <div class="skill-item__bar">
            <div class="skill-item__progress" :style="{ width: skill.level + '%' }"></div>
          </div>
        </div>
      </div>
    </section>

    <!-- Contact Section -->
    <section class="contact">
      <h2 class="section-title">联系方式</h2>
      <div class="contact__info">
        <div class="contact-item">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
            <circle cx="12" cy="10" r="3"/>
          </svg>
          <span>{{ profile.location }}</span>
        </div>
        <div class="contact-item">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
            <polyline points="22,6 12,13 2,6"/>
          </svg>
          <a :href="'mailto:' + profile.email">{{ profile.email }}</a>
        </div>
        <div class="contact-item">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="12" r="10"/>
            <line x1="2" y1="12" x2="22" y2="12"/>
            <path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/>
          </svg>
          <a :href="profile.website" target="_blank">{{ profile.website }}</a>
        </div>
      </div>

      <!-- Social Links -->
      <div class="social-links">
        <a v-for="link in socialLinks" :key="link.name" :href="link.url" class="social-link" target="_blank" rel="noopener">
          <span>{{ link.name }}</span>
        </a>
      </div>
    </section>
  </div>
</template>

<style scoped lang="scss">
.about-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
}

// Hero Section
.hero {
  text-align: center;
  padding: 40px 0;

  &__avatar {
    margin-bottom: 24px;
  }

  &__name {
    font-size: 36px;
    font-weight: 700;
    color: var(--text-primary);
    margin: 0 0 8px;
    background: linear-gradient(135deg, #3b82f6, #60a5fa);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }

  &__title {
    font-size: 18px;
    color: var(--text-secondary);
    margin: 0 0 16px;
  }

  &__bio {
    font-size: 16px;
    color: var(--text-muted);
    line-height: 1.8;
    max-width: 500px;
    margin: 0 auto;
  }
}

.avatar-placeholder {
  width: 120px;
  height: 120px;
  margin: 0 auto;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--bg-tertiary), var(--bg-secondary));
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-muted);
  border: 3px solid var(--color-primary);
}

// Stats Section
.stats {
  margin: 40px 0;

  &__grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 16px;

    @media (max-width: 600px) {
      grid-template-columns: 1fr;
    }
  }
}

.stat-card {
  background: var(--bg-tertiary);
  border-radius: 12px;
  padding: 24px;
  text-align: center;
  transition: transform 0.2s;

  &:hover {
    transform: translateY(-4px);
  }

  &__value {
    display: block;
    font-size: 32px;
    font-weight: 700;
    color: var(--color-primary);
    margin-bottom: 4px;
  }

  &__label {
    font-size: 14px;
    color: var(--text-secondary);
  }
}

// Section Title
.section-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 24px;
  padding-bottom: 12px;
  border-bottom: 2px solid var(--border-primary);
}

// Skills Section
.skills {
  margin: 40px 0;

  &__list {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }
}

.skill-item {
  &__header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 8px;
  }

  &__name {
    font-size: 14px;
    font-weight: 500;
    color: var(--text-primary);
  }

  &__level {
    font-size: 12px;
    color: var(--text-muted);
  }

  &__bar {
    height: 8px;
    background: var(--bg-tertiary);
    border-radius: 4px;
    overflow: hidden;
  }

  &__progress {
    height: 100%;
    background: linear-gradient(90deg, #3b82f6, #60a5fa);
    border-radius: 4px;
    transition: width 0.5s ease;
  }
}

// Contact Section
.contact {
  margin: 40px 0;

  &__info {
    display: flex;
    flex-direction: column;
    gap: 16px;
    margin-bottom: 24px;
  }
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 12px;
  color: var(--text-secondary);

  svg {
    color: var(--color-primary);
    flex-shrink: 0;
  }

  a {
    color: var(--text-secondary);
    text-decoration: none;
    transition: color 0.2s;

    &:hover {
      color: var(--color-primary);
    }
  }
}

.social-links {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.social-link {
  display: inline-flex;
  align-items: center;
  padding: 10px 20px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  color: var(--text-primary);
  text-decoration: none;
  font-size: 14px;
  transition: all 0.2s;

  &:hover {
    background: var(--color-primary);
    color: white;
    transform: translateY(-2px);
  }
}
</style>
