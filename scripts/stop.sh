#!/bin/bash

# AI-Site 停止脚本
# 停止所有服务

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 项目根目录
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PID_DIR="$PROJECT_ROOT/.pids"

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

# 停止服务
stop_service() {
    local name=$1
    local pid_file="$PID_DIR/${name}.pid"
    local port=$2

    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if kill -0 "$pid" 2>/dev/null; then
            print_info "停止 $name (PID: $pid)..."
            kill "$pid" 2>/dev/null || true
            rm -f "$pid_file"
            print_success "$name 已停止"
        else
            print_warning "$name 进程不存在"
            rm -f "$pid_file"
        fi
    else
        # 尝试通过端口停止
        if [ -n "$port" ]; then
            local pids=$(lsof -ti :$port 2>/dev/null || true)
            if [ -n "$pids" ]; then
                print_info "通过端口 $port 停止 $name..."
                kill $pids 2>/dev/null || true
                print_success "$name 已停止"
            else
                print_warning "$name 未运行"
            fi
        else
            print_warning "$name 未运行"
        fi
    fi
}

# 主函数
main() {
    echo ""
    echo "=========================================="
    echo "       AI-Site 停止脚本"
    echo "=========================================="
    echo ""

    stop_service "backend" 8080
    stop_service "admin" 3000
    stop_service "site" 3001

    echo ""
    print_success "所有服务已停止"
    echo ""
}

# 运行
main "$@"