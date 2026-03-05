# 部署指南

详细的部署架构和流程请参考 [部署架构文档](../architecture/deployment.md)。

## 快速部署

### 1. 构建产物

```bash
# 构建前端
cd frontend
pnpm build

# 构建后端
cd backend
./mvnw clean package -DskipTests
```

### 2. 部署静态资源

```bash
# 复制到 Nginx 目录
sudo cp -r frontend/admin/dist /var/www/ai-site/admin
sudo cp -r frontend/site/dist /var/www/ai-site/site
```

### 3. 部署后端服务

```bash
# 复制 JAR 文件
sudo cp backend/service/target/service.jar /opt/ai-site/

# 启动服务
sudo systemctl start ai-site-service
```

## 配置文件

- [Nginx 配置示例](./nginx.conf.example)
- [Systemd 服务配置](./systemd-service.md)