#!/bin/bash

# AI-Site 重启脚本
# 重启所有服务

set -e

# 项目根目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "重启 AI-Site 服务..."

# 停止服务
"$SCRIPT_DIR/stop.sh"

# 等待端口释放
sleep 2

# 启动服务
"$SCRIPT_DIR/start.sh"