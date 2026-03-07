# 开发环境搭建

## 环境要求

### 必需软件

| 软件 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 25+ | Java 运行环境 |
| Node.js | 20+ | JavaScript 运行环境 |
| pnpm | 9+ | 包管理器 |
| Git | 2.x | 版本控制 |
| Maven | 3.9+ | Java 构建工具 (可选，项目包含 Maven Wrapper) |

### 可选软件

| 软件 | 说明 |
|------|------|
| MySQL 8.0+ | 生产数据库 |
| SQLite 3.x | 开发数据库 |
| Docker | 容器化部署 (可选) |
| IDE | IntelliJ IDEA / VS Code |

## 环境安装

### JDK 25

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-25-jdk

# macOS (使用 SDKMAN)
curl -s "https://get.sdkman.io" | bash
sdk install java 25-open

# 验证安装
java -version
```

### Node.js 20+

```bash
# Ubuntu/Debian (使用 NodeSource)
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt install -y nodejs

# macOS (使用 nvm)
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
nvm install 20
nvm use 20

# 安装 pnpm
npm install -g pnpm

# 验证安装
node -v
pnpm -v
```

### Git

```bash
# Ubuntu/Debian
sudo apt install git

# macOS
brew install git

# 验证安装
git --version
```

### MySQL (可选)

```bash
# Ubuntu/Debian
sudo apt install mysql-server

# macOS
brew install mysql

# 启动服务
sudo systemctl start mysql  # Linux
brew services start mysql   # macOS
```

## 项目设置

### 1. 克隆项目

```bash
git clone <repository-url>
cd ai-site
```

### 2. 安装前端依赖

```bash
cd frontend
pnpm install
```

### 3. 配置后端

```bash
cd backend

# 创建配置文件
cp src/main/resources/application.yml.example src/main/resources/application.yml

# 编辑配置 (根据需要修改数据库连接等)
vim src/main/resources/application.yml
```

### 4. 初始化数据库

**使用 SQLite (开发环境)**:

```bash
# SQLite 数据库会在首次启动时自动创建
# 无需额外配置
```

**使用 MySQL**:

```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE ai_site CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'ai_site'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON ai_site.* TO 'ai_site'@'localhost';
FLUSH PRIVILEGES;

# 配置 application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_site
    username: ai_site
    password: your_password
```

## 启动项目

### 前端开发服务

```bash
# 管理后台
cd frontend/admin
pnpm dev
# 访问 http://localhost:3000

# 用户网站
cd frontend/site
pnpm dev
# 访问 http://localhost:3001
```

### 后端服务

```bash
cd backend

# 启动服务
./mvnw spring-boot:run -pl service
# 访问 http://localhost:8080
# API 文档 http://localhost:8080/swagger-ui.html
```

## IDE 配置

### IntelliJ IDEA

1. 打开项目根目录
2. 等待 Maven 自动导入依赖
3. 安装插件:
   - Lombok
   - MyBatis Plus
   - Vue.js

### VS Code

推荐扩展:

```
- Vue.volar
- Vue.vscode-typescript-vue-plugin
- dbaeumer.vscode-eslint
- esbenp.prettier-vscode
- vscjava.vscode-java-pack
- redhat.java
```