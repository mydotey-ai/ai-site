# MySQL/SQLite 兼容性说明

## 概述

本项目需要同时支持 MySQL 和 SQLite 两种数据库，本文档说明两者的差异及兼容性处理方案。

## 主要差异

### 数据类型

| MySQL | SQLite | 说明 |
|-------|--------|------|
| BIGINT | INTEGER | 整数类型 |
| VARCHAR(n) | TEXT | 字符串类型 |
| LONGTEXT | TEXT | 长文本 |
| DATETIME | TEXT | 日期时间 |
| TINYINT | INTEGER | 小整数 (用于状态) |
| DECIMAL | REAL | 精确小数 |

### 自增主键

**MySQL**:
```sql
id BIGINT AUTO_INCREMENT PRIMARY KEY
```

**SQLite**:
```sql
id INTEGER PRIMARY KEY AUTOINCREMENT
```

### 布尔类型

**MySQL**: 使用 `TINYINT(1)` 表示布尔值

**SQLite**: 使用 `INTEGER` 表示布尔值 (0/1)

### 日期时间

**MySQL**: 支持 `DATETIME`, `TIMESTAMP` 类型

**SQLite**: 使用 `TEXT` 存储 ISO8601 格式字符串

```sql
-- MySQL
created_at DATETIME DEFAULT CURRENT_TIMESTAMP

-- SQLite
created_at TEXT DEFAULT (datetime('now'))
```

### 索引语法

**MySQL**:
```sql
CREATE INDEX idx_name ON table(column);
```

**SQLite**: 相同

### 唯一约束

**MySQL**:
```sql
CREATE UNIQUE INDEX uk_name ON table(column);
-- 或
ALTER TABLE table ADD UNIQUE KEY uk_name (column);
```

**SQLite**:
```sql
CREATE UNIQUE INDEX uk_name ON table(column);
-- 或
CREATE TABLE table (
    column TEXT UNIQUE
);
```

## 兼容性处理方案

### 方案一：MyBatis Plus 类型处理器

使用 MyBatis Plus 的类型处理器自动处理类型转换：

```java
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(
            DbType.MYSQL // 或 DbType.SQLITE
        ));
        return interceptor;
    }
}
```

### 方案二：分离迁移脚本

为 MySQL 和 SQLite 创建不同的迁移脚本：

```
db/migration/
├── mysql/
│   ├── V1.0.0__init_schema.sql
│   └── V1.0.1__add_tables.sql
└── sqlite/
    ├── V1.0.0__init_schema.sql
    └── V1.0.1__add_tables.sql
```

### 方案三：统一 DDL + 运行时适配

编写通用的建表语句，在运行时根据数据库类型进行适配：

```java
public class DatabaseAdapter {

    public String adaptSql(String sql, DbType dbType) {
        if (dbType == DbType.SQLITE) {
            return sql
                .replace("BIGINT", "INTEGER")
                .replace("VARCHAR", "TEXT")
                .replace("DATETIME", "TEXT")
                .replace("TINYINT", "INTEGER");
        }
        return sql;
    }
}
```

## 推荐方案

采用 **方案二**：分离迁移脚本

**原因**:
1. SQL 语法完全兼容，无需运行时转换
2. 可以针对不同数据库优化
3. 维护清晰，不易出错

## 配置示例

### MySQL 配置

```yaml
# application-mysql.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_site?useUnicode=true&characterEncoding=utf-8
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver

  flyway:
    locations: classpath:db/migration/mysql
```

### SQLite 配置

```yaml
# application-sqlite.yml
spring:
  datasource:
    url: jdbc:sqlite:./data/ai_site.db
    driver-class-name: org.sqlite.JDBC

  flyway:
    locations: classpath:db/migration/sqlite
```

## 注意事项

### 1. 日期时间处理

使用 Java 8 的 `LocalDateTime`，MyBatis Plus 会自动处理转换：

```java
@TableField(fill = FieldFill.INSERT)
private LocalDateTime createdAt;
```

### 2. 分页查询

MyBatis Plus 的分页插件会根据数据库类型自动生成分页 SQL：

```java
// MySQL: LIMIT 0, 10
// SQLite: LIMIT 10 OFFSET 0
Page<Article> page = articleMapper.selectPage(new Page<>(1, 10), null);
```

### 3. 事务支持

SQLite 默认不支持并发写入，适合单用户场景。生产环境推荐使用 MySQL。

### 4. 性能考虑

- MySQL: 适合高并发、大数据量场景
- SQLite: 适合开发测试、个人项目、低流量场景