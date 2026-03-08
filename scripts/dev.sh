#!/bin/bash

# AI-Site 服务管理脚本
# 用法: ./scripts/dev.sh {start|stop|restart|status|log}

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# 项目根目录
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BACKEND_DIR="$PROJECT_ROOT/backend"
FRONTEND_ADMIN_DIR="$PROJECT_ROOT/frontend/admin"
FRONTEND_SITE_DIR="$PROJECT_ROOT/frontend/site"

# 日志和 PID 目录
LOG_DIR="$PROJECT_ROOT/logs"
PID_DIR="$PROJECT_ROOT/.pids"
mkdir -p "$LOG_DIR" "$PID_DIR"

# 打印函数
print_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
print_success() { echo -e "${GREEN}[✓]${NC} $1"; }
print_warning() { echo -e "${YELLOW}[!]${NC} $1"; }
print_error() { echo -e "${RED}[✗]${NC} $1"; }

# 检查端口是否被占用
check_port() {
    lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null 2>&1
}

# 获取端口对应的 PID
get_port_pid() {
    lsof -ti :$1 2>/dev/null || echo ""
}

# 等待端口可用
wait_for_port() {
    local port=$1 name=$2 max_wait=${3:-60}
    local count=0
    while ! check_port $port; do
        sleep 1
        count=$((count + 1))
        if [ $count -ge $max_wait ]; then
            return 1
        fi
    done
    return 0
}

# 启动后端服务
start_backend() {
    local port=8080
    if check_port $port; then
        print_warning "后端服务已在运行 (端口 $port)"
        return 0
    fi

    print_info "启动后端服务..."

    # 检查是否需要构建
    if [ ! -f "$BACKEND_DIR/service/target/service-1.0.0-SNAPSHOT.jar" ]; then
        print_info "首次启动，构建后端项目..."
        cd "$BACKEND_DIR"
        mvn clean install -DskipTests -q
    fi

    cd "$BACKEND_DIR/service"
    nohup mvn spring-boot:run -q > "$LOG_DIR/backend.log" 2>&1 &
    echo $! > "$PID_DIR/backend.pid"

    if wait_for_port $port "后端服务"; then
        print_success "后端服务启动成功 (http://localhost:$port)"
    else
        print_error "后端服务启动超时"
        return 1
    fi
}

# 启动前端服务
start_frontend() {
    local name=$1 port=$2 dir=$3

    if check_port $port; then
        print_warning "$name 已在运行 (端口 $port)"
        return 0
    fi

    print_info "启动 $name..."

    cd "$dir"

    # 检查是否需要安装依赖
    if [ ! -d "node_modules" ]; then
        print_info "安装 $name 依赖..."
        npm install --silent
    fi

    nohup npm run dev > "$LOG_DIR/${name,,}.log" 2>&1 &
    echo $! > "$PID_DIR/${name,,}.pid"

    if wait_for_port $port "$name"; then
        print_success "$name 启动成功 (http://localhost:$port)"
    else
        print_error "$name 启动超时"
        return 1
    fi
}

# 启动所有服务
do_start() {
    echo ""
    echo -e "${CYAN}==========================================${NC}"
    echo -e "${CYAN}       AI-Site 服务启动${NC}"
    echo -e "${CYAN}==========================================${NC}"
    echo ""

    start_backend
    start_frontend "Admin" 3000 "$FRONTEND_ADMIN_DIR"
    start_frontend "Site" 3001 "$FRONTEND_SITE_DIR"

    show_info
}

# 停止服务
stop_service() {
    local name=$1 port=$2

    if [ -f "$PID_DIR/${name}.pid" ]; then
        local pid=$(cat "$PID_DIR/${name}.pid")
        if kill -0 "$pid" 2>/dev/null; then
            print_info "停止 $name (PID: $pid)..."
            kill "$pid" 2>/dev/null || true
            print_success "$name 已停止"
        fi
        rm -f "$PID_DIR/${name}.pid"
    fi

    # 通过端口停止
    local pids=$(get_port_pid $port)
    if [ -n "$pids" ]; then
        print_info "停止 $name (端口 $port)..."
        kill $pids 2>/dev/null || true
        print_success "$name 已停止"
    fi
}

# 停止所有服务
do_stop() {
    echo ""
    echo -e "${CYAN}==========================================${NC}"
    echo -e "${CYAN}       AI-Site 服务停止${NC}"
    echo -e "${CYAN}==========================================${NC}"
    echo ""

    stop_service "backend" 8080
    stop_service "admin" 3000
    stop_service "site" 3001

    echo ""
    print_success "所有服务已停止"
    echo ""
}

# 重启所有服务
do_restart() {
    do_stop
    sleep 2
    do_start
}

# 显示服务状态
do_status() {
    echo ""
    echo -e "${CYAN}==========================================${NC}"
    echo -e "${CYAN}       AI-Site 服务状态${NC}"
    echo -e "${CYAN}==========================================${NC}"
    echo ""

    printf "%-12s %-10s %-8s %s\n" "服务" "状态" "端口" "地址"
    echo "--------------------------------------------"

    # 后端
    if check_port 8080; then
        printf "%-12s ${GREEN}%-10s${NC} %-8s %s\n" "后端 API" "运行中" "8080" "http://localhost:8080"
    else
        printf "%-12s ${RED}%-10s${NC} %-8s %s\n" "后端 API" "已停止" "8080" "-"
    fi

    # Admin
    if check_port 3000; then
        printf "%-12s ${GREEN}%-10s${NC} %-8s %s\n" "Admin" "运行中" "3000" "http://localhost:3000"
    else
        printf "%-12s ${RED}%-10s${NC} %-8s %s\n" "Admin" "已停止" "3000" "-"
    fi

    # Site
    if check_port 3001; then
        printf "%-12s ${GREEN}%-10s${NC} %-8s %s\n" "Site" "运行中" "3001" "http://localhost:3001"
    else
        printf "%-12s ${RED}%-10s${NC} %-8s %s\n" "Site" "已停止" "3001" "-"
    fi

    echo ""
}

# 查看日志
do_log() {
    local service=${1:-all}

    case $service in
        backend|admin|site)
            local log_file="$LOG_DIR/${service}.log"
            if [ -f "$log_file" ]; then
                print_info "查看 $service 日志 (Ctrl+C 退出)..."
                tail -f "$log_file"
            else
                print_error "日志文件不存在: $log_file"
            fi
            ;;
        all)
            print_info "查看所有日志 (Ctrl+C 退出)..."
            tail -f "$LOG_DIR"/*.log
            ;;
        *)
            print_error "未知服务: $service"
            echo "用法: $0 log {backend|admin|site|all}"
            exit 1
            ;;
    esac
}

# 显示启动信息
show_info() {
    echo ""
    echo -e "${CYAN}==========================================${NC}"
    echo -e "${GREEN}  AI-Site 服务已启动${NC}"
    echo -e "${CYAN}==========================================${NC}"
    echo ""
    echo -e "服务地址:"
    echo -e "  ${BLUE}后端 API${NC}:  http://localhost:8080"
    echo -e "  ${BLUE}Admin${NC}:     http://localhost:3000"
    echo -e "  ${BLUE}Site${NC}:      http://localhost:3001"
    echo ""
    echo -e "登录凭据:"
    echo -e "  用户名: ${YELLOW}admin${NC}"
    echo -e "  密码:   ${YELLOW}admin123${NC}"
    echo ""
    echo -e "其他命令:"
    echo -e "  查看状态: ${CYAN}$0 status${NC}"
    echo -e "  查看日志: ${CYAN}$0 log${NC}"
    echo -e "  停止服务: ${CYAN}$0 stop${NC}"
    echo ""
    echo -e "${CYAN}==========================================${NC}"
}

# 显示帮助
show_help() {
    echo ""
    echo -e "${CYAN}AI-Site 服务管理脚本${NC}"
    echo ""
    echo "用法: $0 <命令> [参数]"
    echo ""
    echo "命令:"
    echo "  start       启动所有服务"
    echo "  stop        停止所有服务"
    echo "  restart     重启所有服务"
    echo "  status      查看服务状态"
    echo "  log [服务]  查看日志 (backend|admin|site|all)"
    echo "  help        显示帮助信息"
    echo ""
}

# 主函数
main() {
    local command=${1:-help}

    case $command in
        start)
            do_start
            ;;
        stop)
            do_stop
            ;;
        restart)
            do_restart
            ;;
        status)
            do_status
            ;;
        log)
            do_log "${2:-all}"
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            print_error "未知命令: $command"
            show_help
            exit 1
            ;;
    esac
}

main "$@"