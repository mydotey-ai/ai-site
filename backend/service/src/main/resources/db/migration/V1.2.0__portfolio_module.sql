-- V1.2.0__portfolio_module.sql
-- 作品集模块数据库迁移

-- 1. 修改 project 表结构
-- 重命名 title -> name, 添加新字段

-- SQLite 不支持直接修改列，需要重建表
-- MySQL 支持直接修改

-- 先备份并删除旧表数据（如果需要保留数据，需要先迁移）
-- 这里假设是全新安装或可以重建

-- 删除旧表（如果存在）
DROP TABLE IF EXISTS `project`;

-- 创建新的 project 表
CREATE TABLE IF NOT EXISTS `project` (
    `id` BIGINT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL COMMENT '项目名称',
    `slug` VARCHAR(100) NOT NULL COMMENT 'URL别名',
    `description` VARCHAR(500) COMMENT '项目描述',
    `content` TEXT COMMENT '项目详情(Markdown)',
    `cover_image` VARCHAR(255) COMMENT '封面图URL',
    `tech_stack` JSON COMMENT '技术栈列表',
    `status` VARCHAR(20) DEFAULT 'DEVELOPING' COMMENT '状态: DEVELOPING/RELEASED/ARCHIVED',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    UNIQUE KEY `uk_slug` (`slug`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目表';

-- 2. 创建项目标签表
CREATE TABLE IF NOT EXISTS `project_tag` (
    `id` BIGINT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    `slug` VARCHAR(50) NOT NULL COMMENT 'URL别名',
    `color` VARCHAR(20) COMMENT '标签颜色(HEX)',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    UNIQUE KEY `uk_slug` (`slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目标签表';

-- 3. 创建项目标签关联表
CREATE TABLE IF NOT EXISTS `project_tag_relation` (
    `project_id` BIGINT NOT NULL,
    `tag_id` BIGINT NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`project_id`, `tag_id`),
    KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目标签关联表';

-- 4. 创建项目链接表
CREATE TABLE IF NOT EXISTS `project_link` (
    `id` BIGINT PRIMARY KEY,
    `project_id` BIGINT NOT NULL COMMENT '项目ID',
    `type` VARCHAR(20) NOT NULL COMMENT '链接类型: DEMO/SOURCE/DOCS/OTHER',
    `label` VARCHAR(50) COMMENT '链接标签',
    `url` VARCHAR(500) NOT NULL COMMENT '链接地址',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    KEY `idx_project_id` (`project_id`),
    CONSTRAINT `fk_project_link_project` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目链接表';

-- 5. 插入默认标签数据
INSERT INTO `project_tag` (`id`, `name`, `slug`, `color`, `sort`) VALUES
(1, 'Web', 'web', '#42b883', 1),
(2, 'Mobile', 'mobile', '#38bdf8', 2),
(3, 'CLI', 'cli', '#f59e0b', 3),
(4, 'Desktop', 'desktop', '#8b5cf6', 4),
(5, 'Library', 'library', '#ec4899', 5),
(6, 'Other', 'other', '#6b7280', 6);
