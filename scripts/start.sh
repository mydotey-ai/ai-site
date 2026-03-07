#!/bin/bash

# AI-Site 一键启动脚本
# 启动后端服务、Admin 前端、Site 前端

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 项目根目录
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BACKEND_DIR="$PROJECT_ROOT/backend"
FRONTEND_ADMIN_DIR="$PROJECT_ROOT/frontend/admin"
FRONTEND_SITE_DIR="$PROJECT_ROOT/frontend/site"

# 日志目录
LOG_DIR="$PROJECT_ROOT/logs"
mkdir -p "$LOG_DIR"

# PID 文件目录
PID_DIR="$PROJECT_ROOT/.pids"
mkdir -p "$PID_DIR"

# 打印带颜色的消息
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查端口是否被占用
check_port() {
    local port=$1
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
        return 0  # 端口被占用
    else
        return 1  # 端口空闲
    fi
}

# 等待端口可用
wait_for_port() {
    local port=$1
    local service=$2
    local max_wait=60
    local count=0

    print_info "等待 $service 启动 (端口 $port)..."

    while ! check_port $port; do
        sleep 1
        count=$((count + 1))
        if [ $count -ge $max_wait ]; then
            print_error "$service 启动超时"
            return 1
        fi
    done

    print_success "$service 已启动 (端口 $port)"
    return 0
}

# 启动后端服务
start_backend() {
    print_info "启动后端服务..."

    if check_port 8080; then
        print_warning "端口 8080 已被占用，跳过后端启动"
        return 0
    fi

    cd "$BACKEND_DIR"

    # 检查是否需要构建
    if [ ! -f "service/target/service-1.0.0-SNAPSHOT.jar" ]; then
        print_info "构建后端项目..."
        mvn clean install -DskipTests -q
    fi

    # 启动后端
    cd "$BACKEND_DIR/service"
    nohup mvn spring-boot:run -q > "$LOG_DIR/backend.log" 2>&1 &
    echo $! > "$PID_DIR/backend.pid"

    # 等待启动
    if wait_for_port 8080 "后端服务"; then
        print_success "后端服务: http://localhost:8080"
    fi
}

# 启动 Admin 前端
start_admin() {
    print_info "启动 Admin 前端..."

    if check_port 3000; then
        print_warning "端口 3000 已被占用，跳过 Admin 启动"
        return 0
    fi

    cd "$FRONTEND_ADMIN_DIR"

    # 检查是否需要安装依赖
    if [ ! -d "node_modules" ]; then
        print_info "安装 Admin 依赖..."
        npm install --silent
    fi

    # 启动前端
    nohup npm run dev > "$LOG_DIR/admin.log" 2>&1 &
    echo $! > "$PID_DIR/admin.pid"

    # 等待启动
    if wait_for_port 3000 "Admin 前端"; then
        print_success "Admin 前端: http://localhost:3000"
    fi
}

# 启动 Site 前端
start_site() {
    print_info "启动 Site 前端..."

    if check_port 3001; then
        print_warning "端口 3001 已被占用，跳过 Site 启动"
        return 0
    fi

    cd "$FRONTEND_SITE_DIR"

    # 检查是否需要安装依赖
    if [ ! -d "node_modules" ]; then
        print_info "安装 Site 依赖..."
        npm install --silent
    fi

    # 启动前端
    nohup npm run dev > "$LOG_DIR/site.log" 2>&1 &
    echo $! > "$PID_DIR/site.pid"

    # 等待启动
    if wait_for_port 3001 "Site 前端"; then
        print_success "Site 前端: http://localhost:3001"
    fi
}

# 显示启动信息
show_info() {
    echo ""
    echo "=========================================="
    echo -e "${GREEN}AI-Site 服务已启动${NC}"
    echo "=========================================="
    echo ""
    echo "服务地址:"
    echo "  - 后端 API:  http://localhost:8080"
    echo "  - Admin:     http://localhost:3000"
    echo "  - Site:      http://localhost:3001"
    echo ""
    echo "登录凭据:"
    echo "  - 用户名: admin"
    echo "  - 密码:   admin123"
    echo ""
    echo "日志目录: $LOG_DIR"
    echo ""
    echo "停止服务: ./scripts/stop.sh"
    echo "=========================================="
}

# 主函数
main() {
    echo ""
    echo "=========================================="
    echo "       AI-Site 一键启动脚本"
    echo "=========================================="
    echo ""

    start_backend
    start_admin
    start_site

    show_info
}

# 运行
main "$@"