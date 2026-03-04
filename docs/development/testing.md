# 测试指南

## 测试策略

### 测试金字塔

```
        ┌─────────┐
        │   E2E   │  <- 端到端测试 (少量)
        ├─────────┤
        │ Integration │ <- 集成测试 (适量)
        ├─────────┤
        │  Unit   │  <- 单元测试 (大量)
        └─────────┘
```

### 测试覆盖范围

| 层级 | 测试类型 | 覆盖重点 |
|------|----------|----------|
| 接入层 | 单元测试 | Controller 参数校验 |
| 应用服务层 | 单元测试 + 集成测试 | 业务逻辑、事务 |
| 领域层 | 单元测试 | 领域逻辑、业务规则 |
| 基础设施层 | 集成测试 | 数据库操作、外部服务 |

## 前端测试

### 单元测试 (Vitest)

```typescript
// tests/unit/components/BaseButton.test.ts
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import BaseButton from '@/components/common/BaseButton.vue'

describe('BaseButton', () => {
  it('should render with text', () => {
    const wrapper = mount(BaseButton, {
      slots: { default: 'Click me' }
    })
    expect(wrapper.text()).toBe('Click me')
  })

  it('should emit click event', async () => {
    const wrapper = mount(BaseButton)
    await wrapper.trigger('click')
    expect(wrapper.emitted('click')).toBeTruthy()
  })

  it('should be disabled when disabled prop is true', () => {
    const wrapper = mount(BaseButton, {
      props: { disabled: true }
    })
    expect(wrapper.find('button').attributes('disabled')).toBeDefined()
  })
})
```

### 组件测试

```typescript
// tests/unit/components/ArticleCard.test.ts
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import ArticleCard from '@/components/business/ArticleCard.vue'

describe('ArticleCard', () => {
  const mockArticle = {
    id: 1,
    title: 'Test Article',
    summary: 'Test Summary',
    coverImage: '/test.jpg',
    viewCount: 100
  }

  it('should render article info', () => {
    const wrapper = mount(ArticleCard, {
      props: { article: mockArticle }
    })
    expect(wrapper.text()).toContain('Test Article')
    expect(wrapper.text()).toContain('Test Summary')
  })
})
```

### E2E 测试 (Playwright)

```typescript
// tests/e2e/blog.spec.ts
import { test, expect } from '@playwright/test'

test.describe('Blog Page', () => {
  test('should display article list', async ({ page }) => {
    await page.goto('/blog')
    await expect(page.locator('.article-list')).toBeVisible()
  })

  test('should navigate to article detail', async ({ page }) => {
    await page.goto('/blog')
    await page.click('.article-card:first-child')
    await expect(page).toHaveURL(/\/blog\/\d+/)
  })
})
```

### 运行测试

```bash
# 运行所有测试
pnpm test

# 运行单元测试
pnpm test:unit

# 运行 E2E 测试
pnpm test:e2e

# 生成覆盖率报告
pnpm test:coverage
```

## 后端测试

### 单元测试

```java
@ExtendWith(MockitoExtension.class)
class ArticleCommandServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ArticleCommandService commandService;

    @Test
    void shouldCreateArticle() {
        // given
        CreateArticleCommand command = new CreateArticleCommand();
        command.setTitle("Test Article");
        command.setContent("Test Content");
        command.setCategoryId(1L);

        when(categoryRepository.findById(1L))
            .thenReturn(new Category());

        // when
        Long articleId = commandService.create(command);

        // then
        verify(articleRepository).save(any(Article.class));
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        // given
        CreateArticleCommand command = new CreateArticleCommand();
        command.setCategoryId(999L);

        when(categoryRepository.findById(999L))
            .thenReturn(null);

        // when & then
        assertThrows(BusinessException.class,
            () -> commandService.create(command));
    }
}
```

### 集成测试

```java
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ArticleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void shouldReturnArticleList() throws Exception {
        // given
        Article article = new Article();
        article.setTitle("Test Article");
        articleRepository.save(article);

        // when & then
        mockMvc.perform(get("/api/v1/articles")
                .param("page", "1")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.list").isArray())
            .andExpect(jsonPath("$.data.list[0].title").value("Test Article"));
    }

    @Test
    void shouldCreateArticle() throws Exception {
        // given
        String requestBody = """
            {
                "title": "New Article",
                "content": "Content",
                "categoryId": 1
            }
            """;

        // when & then
        mockMvc.perform(post("/api/v1/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value(200));
    }
}
```

### Repository 测试

```java
@DataJpaTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void shouldSaveAndFindArticle() {
        // given
        Article article = new Article();
        article.setTitle("Test Article");

        // when
        Article saved = articleRepository.save(article);
        Article found = articleRepository.findById(saved.getId()).orElse(null);

        // then
        assertNotNull(found);
        assertEquals("Test Article", found.getTitle());
    }
}
```

### 运行测试

```bash
# 运行所有测试
./mvnw test

# 运行单个测试类
./mvnw test -Dtest=ArticleCommandServiceTest

# 运行单个测试方法
./mvnw test -Dtest=ArticleCommandServiceTest#shouldCreateArticle

# 生成覆盖率报告
./mvnw jacoco:report
```

## 测试数据管理

### 测试数据准备

```java
public class TestDataFactory {

    public static Article createArticle() {
        Article article = new Article();
        article.setTitle("Test Article");
        article.setSlug("test-article");
        article.setContent("Test Content");
        article.setStatus(ArticleStatus.DRAFT);
        return article;
    }

    public static Article createPublishedArticle() {
        Article article = createArticle();
        article.setStatus(ArticleStatus.PUBLISHED);
        article.setPublishedAt(LocalDateTime.now());
        return article;
    }
}
```

### 使用测试配置

```yaml
# src/test/resources/application-test.yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
```

## 最佳实践

### 1. 测试命名

```java
// 使用 should 风格
@Test
void shouldReturnArticleWhenExists() { }

@Test
void shouldThrowExceptionWhenNotFound() { }
```

### 2. 测试结构

```java
@Test
void testMethodName() {
    // given (准备数据)

    // when (执行操作)

    // then (验证结果)
}
```

### 3. 测试隔离

- 每个测试应该独立运行
- 使用 `@BeforeEach` 重置状态
- 使用 `@Transactional` 回滚数据

### 4. Mock 使用

- 外部依赖使用 Mock
- 数据库操作使用真实数据库或内存数据库
- 不要过度 Mock