# 部署架构

## 部署概述

项目采用传统部署方式，使用 JAR 包运行后端服务，Nginx 作为反向代理和静态资源服务器。

## 部署架构图

```
                      ┌─────────────────┐
                      │    Internet     │
                      └────────┬────────┘
                               │
                               ▼
                      ┌─────────────────┐
                      │     Nginx       │
                      │   (Port 80/443) │
                      └────────┬────────┘
                               │
          ┌────────────────────┼────────────────────┐
          │                    │                    │
          ▼                    ▼                    ▼
   ┌─────────────┐      ┌─────────────┐      ┌─────────────┐
   │ 静态资源     │      │  API 服务   │      │ Admin 服务  │
   │ (Frontend)  │      │  :8080      │      │  :8081      │
   └─────────────┘      └──────┬──────┘      └──────┬──────┘
                               │                    │
                               └─────────┬──────────┘
                                         │
                                         ▼
                                ┌─────────────────┐
                                │  MySQL/SQLite   │
                                └─────────────────┘
```

## 部署组件

### Nginx

**职责**:
- 反向代理 API 请求
- 静态资源服务
- SSL 终止
- 负载均衡 (可选)

**配置示例**:
```nginx
# /etc/nginx/sites-available/ai-site.conf
server {
    listen 80;
    server_name example.com;

    # 重定向到 HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name example.com;

    ssl_certificate /etc/letsencrypt/live/example.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/example.com/privkey.pem;

    # 用户网站
    location / {
        root /var/www/ai-site/site;
        try_files $uri $uri/ /index.html;
    }

    # 管理后台
    location /admin {
        alias /var/www/ai-site/admin;
        try_files $uri $uri/ /admin/index.html;
    }

    # API 服务
    location /api {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Admin API 服务
    location /admin-api {
        proxy_pass http://127.0.0.1:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2)$ {
        root /var/www/ai-site;
        expires 30d;
        add_header Cache-Control "public, immutable";
    }
}
```

### Spring Boot 服务

**API 服务** (端口 8080):
- 提供用户网站的 API 接口
- 文章、作品、创作内容的读取

**Admin 服务** (端口 8081):
- 提供管理后台的 API 接口
- 内容管理、用户管理等

**Systemd 服务配置**:
```ini
# /etc/systemd/system/ai-site-api.service
[Unit]
Description=AI-Site API Service
After=network.target

[Service]
Type=simple
User=www-data
WorkingDirectory=/opt/ai-site
ExecStart=/usr/bin/java -jar /opt/ai-site/api.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

```ini
# /etc/systemd/system/ai-site-admin.service
[Unit]
Description=AI-Site Admin Service
After=network.target

[Service]
Type=simple
User=www-data
WorkingDirectory=/opt/ai-site
ExecStart=/usr/bin/java -jar /opt/ai-site/admin.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

### 数据库

**MySQL** (生产环境):
```yaml
# application-prod.yml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST}:3306/ai_site
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

**SQLite** (开发/轻量部署):
```yaml
# application-sqlite.yml
spring:
  datasource:
    url: jdbc:sqlite:./data/ai_site.db
    driver-class-name: org.sqlite.JDBC
```

## 部署流程

### 1. 构建产物

```bash
# 构建前端
cd frontend
pnpm build

# 构建后端
cd backend
./mvnw clean package -DskipTests

# 产物位置
# frontend/admin/dist/   -> 管理后台静态文件
# frontend/site/dist/    -> 用户网站静态文件
# backend/api/target/api.jar
# backend/admin/target/admin.jar
```

### 2. 部署静态资源

```bash
# 创建目录
sudo mkdir -p /var/www/ai-site

# 复制静态文件
sudo cp -r frontend/admin/dist /var/www/ai-site/admin
sudo cp -r frontend/site/dist /var/www/ai-site/site

# 设置权限
sudo chown -R www-data:www-data /var/www/ai-site
```

### 3. 部署后端服务

```bash
# 创建目录
sudo mkdir -p /opt/ai-site

# 复制 JAR 文件
sudo cp backend/api/target/api.jar /opt/ai-site/
sudo cp backend/admin/target/admin.jar /opt/ai-site/

# 设置权限
sudo chown -R www-data:www-data /opt/ai-site

# 启动服务
sudo systemctl enable ai-site-api
sudo systemctl enable ai-site-admin
sudo systemctl start ai-site-api
sudo systemctl start ai-site-admin
```

### 4. 配置 Nginx

```bash
# 复制配置
sudo cp docs/deployment/nginx.conf /etc/nginx/sites-available/ai-site.conf
sudo ln -s /etc/nginx/sites-available/ai-site.conf /etc/nginx/sites-enabled/

# 测试配置
sudo nginx -t

# 重载配置
sudo systemctl reload nginx
```

## 环境变量

```bash
# /opt/ai-site/.env
DB_HOST=localhost
DB_USERNAME=ai_site
DB_PASSWORD=your_password
JWT_SECRET=your_jwt_secret
```

## 日志管理

```bash
# 应用日志位置
/var/log/ai-site/api.log
/var/log/ai-site/admin.log

# Nginx 日志
/var/log/nginx/access.log
/var/log/nginx/error.log

# 日志轮转配置
/etc/logrotate.d/ai-site
```

## 监控与备份

### 健康检查

```bash
# API 服务健康检查
curl http://localhost:8080/actuator/health

# Admin 服务健康检查
curl http://localhost:8081/actuator/health
```

### 数据库备份

```bash
# MySQL 备份脚本
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
mysqldump -u ai_site -p'password' ai_site > /backup/ai_site_$DATE.sql
find /backup -name "ai_site_*.sql" -mtime +7 -delete
```

## 更新部署

```bash
# 1. 拉取最新代码
git pull

# 2. 构建新版本
cd frontend && pnpm build
cd backend && ./mvnw clean package -DskipTests

# 3. 部署静态资源
sudo cp -r frontend/admin/dist /var/www/ai-site/admin
sudo cp -r frontend/site/dist /var/www/ai-site/site

# 4. 更新后端服务
sudo systemctl restart ai-site-api
sudo systemctl restart ai-site-admin

# 5. 验证
curl http://localhost:8080/actuator/health
```