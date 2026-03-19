# 问题排查记录

本文档记录项目开发过程中遇到的问题及解决方案。

---

## 模板

```markdown
## 问题标题

**日期**: YYYY-MM-DD

**环境**:
- JDK 版本:
- Spring Boot 版本:
- Node 版本:
- 其他:

**问题描述**:
详细描述问题的现象。

**原因分析**:
分析问题的根本原因。

**解决方案**:
描述如何解决问题。

**参考链接**:
- 相关文档或讨论链接
```

---

## Java 25 + Lombok 兼容性问题

**日期**: 2026-03-07

**环境**:
- JDK 版本: 25
- Spring Boot 版本: 3.5.0
- Lombok 版本: 1.18.36 → 1.18.38

**问题描述**:
使用 JDK 25 编译项目时，Lombok 注解处理器报错：
```
java.lang.IllegalAccessError: class lombok.javac.apt.JavacPlugin (in module lombok.compiler) cannot access class com.sun.tools.javac.processing.JavacProcessingEnvironment (in module jdk.compiler)
```

**原因分析**:
JDK 25 加强了模块封装，Lombok 需要访问 JDK 编译器内部 API，但这些 API 被模块系统限制访问。

**解决方案**:
1. 升级 Lombok 到 `1.18.38`（支持 JDK 25）
2. 在 `maven-compiler-plugin` 中添加 JVM 参数：
```xml
<compilerArgs>
    <arg>-J--add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED</arg>
    <arg>-J--add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED</arg>
    <arg>-J--add-opens=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED</arg>
    <arg>-J--add-opens=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED</arg>
    <arg>-J--add-opens=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED</arg>
    <arg>-J--add-opens=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED</arg>
    <arg>-J--add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED</arg>
    <arg>-J--add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED</arg>
    <arg>-J--add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED</arg>
    <arg>-J--add-opens=jdk.compiler/com.sun.tools.javac.jvm=ALL-UNNAMED</arg>
</compilerArgs>
```

**参考链接**:
- [Lombok Changelog](https://projectlombok.org/changelog)
- [JEP 403: Strongly Encapsulate JDK Internals](https://openjdk.org/jeps/403)

---

## 前端 API 路径重复导致 500 错误

**日期**: 2026-03-19

**环境**:
- 前端: Vue 3.5 + Axios
- 后端: Spring Boot 3.5

**问题描述**:
前端请求 API 时返回 500 错误，后端日志显示：
```
org.springframework.web.servlet.resource.NoResourceFoundException: No static resource api/api/v1/project-tags.
```
请求路径中出现了重复的 `/api` 前缀。

**原因分析**:
前端 `request.ts` 中 baseURL 已配置为 `/api`：
```typescript
baseURL: import.meta.env.VITE_API_BASE_URL || '/api'
```
但 API 文件中的路径又包含了 `/api` 前缀：
```typescript
// 错误示例
return http.get('/api/admin/v1/images', { params })
```
导致最终请求路径变成 `/api/api/admin/v1/images`。

**解决方案**:
API 文件中的路径不应包含 `/api` 前缀：
```typescript
// 正确示例
return http.get('/admin/v1/images', { params })
```

**预防措施**:
- API 路径统一使用相对路径，不带 baseURL 前缀
- Code Review 时注意检查新增 API 文件的路径配置

---

## 记录区域

（问题排查记录将在开发过程中逐步添加）