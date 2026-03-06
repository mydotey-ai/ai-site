-- V1.0.0__init_schema.sql
-- 初始化数据库表结构

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(100),
    `nickname` VARCHAR(50),
    `avatar` VARCHAR(255),
    `bio` TEXT,
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS `role` (
    `id` BIGINT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL,
    `code` VARCHAR(50) NOT NULL,
    `description` VARCHAR(255),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `user_role` (
    `id` BIGINT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 文章表
CREATE TABLE IF NOT EXISTS `article` (
    `id` BIGINT PRIMARY KEY,
    `title` VARCHAR(200) NOT NULL,
    `slug` VARCHAR(200) NOT NULL,
    `summary` VARCHAR(500),
    `content` LONGTEXT,
    `cover_image` VARCHAR(255),
    `category_id` BIGINT,
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-草稿, 1-已发布',
    `view_count` INT DEFAULT 0,
    `like_count` INT DEFAULT 0,
    `is_top` TINYINT DEFAULT 0,
    `allow_comment` TINYINT DEFAULT 1,
    `published_at` DATETIME,
    `author_id` BIGINT,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    UNIQUE KEY `uk_slug` (`slug`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`),
    KEY `idx_author_id` (`author_id`),
    KEY `idx_published_at` (`published_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';

-- 分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL,
    `slug` VARCHAR(50) NOT NULL,
    `description` VARCHAR(255),
    `parent_id` BIGINT DEFAULT 0,
    `sort_order` INT DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    UNIQUE KEY `uk_slug` (`slug`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- 标签表
CREATE TABLE IF NOT EXISTS `tag` (
    `id` BIGINT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL,
    `slug` VARCHAR(50) NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    UNIQUE KEY `uk_slug` (`slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- 文章标签关联表
CREATE TABLE IF NOT EXISTS `article_tag` (
    `id` BIGINT PRIMARY KEY,
    `article_id` BIGINT NOT NULL,
    `tag_id` BIGINT NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_article_tag` (`article_id`, `tag_id`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章标签关联表';

-- 作品项目表
CREATE TABLE IF NOT EXISTS `project` (
    `id` BIGINT PRIMARY KEY,
    `title` VARCHAR(200) NOT NULL,
    `slug` VARCHAR(200) NOT NULL,
    `description` TEXT,
    `content` LONGTEXT,
    `cover_image` VARCHAR(255),
    `demo_url` VARCHAR(255),
    `github_url` VARCHAR(255),
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-隐藏, 1-显示',
    `sort_order` INT DEFAULT 0,
    `started_at` DATE,
    `ended_at` DATE,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    UNIQUE KEY `uk_slug` (`slug`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='作品项目表';

-- 技能表
CREATE TABLE IF NOT EXISTS `skill` (
    `id` BIGINT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `category` VARCHAR(50),
    `level` INT DEFAULT 1 COMMENT '等级: 1-5',
    `icon` VARCHAR(255),
    `sort_order` INT DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='技能表';

-- 小说表
CREATE TABLE IF NOT EXISTS `novel` (
    `id` BIGINT PRIMARY KEY,
    `title` VARCHAR(200) NOT NULL,
    `slug` VARCHAR(200) NOT NULL,
    `author` VARCHAR(100),
    `description` TEXT,
    `cover_image` VARCHAR(255),
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-连载中, 1-已完结',
    `word_count` INT DEFAULT 0,
    `view_count` INT DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    UNIQUE KEY `uk_slug` (`slug`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='小说表';

-- 章节表
CREATE TABLE IF NOT EXISTS `chapter` (
    `id` BIGINT PRIMARY KEY,
    `novel_id` BIGINT NOT NULL,
    `title` VARCHAR(200) NOT NULL,
    `content` LONGTEXT,
    `word_count` INT DEFAULT 0,
    `chapter_number` INT NOT NULL,
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-草稿, 1-已发布',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    KEY `idx_novel_id` (`novel_id`),
    KEY `idx_chapter_number` (`chapter_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='章节表';

-- 图片表
CREATE TABLE IF NOT EXISTS `image` (
    `id` BIGINT PRIMARY KEY,
    `title` VARCHAR(200),
    `description` VARCHAR(500),
    `url` VARCHAR(500) NOT NULL,
    `thumbnail_url` VARCHAR(500),
    `album_id` BIGINT,
    `width` INT,
    `height` INT,
    `size` BIGINT,
    `mime_type` VARCHAR(50),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    KEY `idx_album_id` (`album_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图片表';

-- 相册表
CREATE TABLE IF NOT EXISTS `album` (
    `id` BIGINT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `slug` VARCHAR(100) NOT NULL,
    `description` VARCHAR(500),
    `cover_image` VARCHAR(500),
    `sort_order` INT DEFAULT 0,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    UNIQUE KEY `uk_slug` (`slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='相册表';