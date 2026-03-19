-- V1.4.0__media_module.sql
-- 多媒体模块数据库迁移

-- =====================================================
-- 1. 文件夹表
-- =====================================================
CREATE TABLE IF NOT EXISTS `folder` (
    `id` BIGINT PRIMARY KEY COMMENT '文件夹ID',
    `name` VARCHAR(100) NOT NULL COMMENT '文件夹名称',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父文件夹ID',
    `type` VARCHAR(20) NOT NULL DEFAULT 'IMAGE' COMMENT '类型: IMAGE/VIDEO/AUDIO',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件夹表';

-- =====================================================
-- 2. 相册表
-- =====================================================
CREATE TABLE IF NOT EXISTS `album` (
    `id` BIGINT PRIMARY KEY COMMENT '相册ID',
    `name` VARCHAR(100) NOT NULL COMMENT '相册名称',
    `slug` VARCHAR(100) NOT NULL COMMENT 'URL别名',
    `description` VARCHAR(500) COMMENT '描述',
    `cover_image` VARCHAR(255) COMMENT '封面图URL',
    `image_count` INT DEFAULT 0 COMMENT '图片数量',
    `is_public` TINYINT DEFAULT 1 COMMENT '是否公开: 0-否, 1-是',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    UNIQUE KEY `uk_slug` (`slug`),
    KEY `idx_is_public` (`is_public`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='相册表';

-- =====================================================
-- 3. 图片表
-- =====================================================
CREATE TABLE IF NOT EXISTS `image` (
    `id` BIGINT PRIMARY KEY COMMENT '图片ID',
    `title` VARCHAR(100) COMMENT '标题',
    `description` VARCHAR(500) COMMENT '描述',
    `original_name` VARCHAR(255) COMMENT '原始文件名',
    `file_name` VARCHAR(255) NOT NULL COMMENT '存储文件名',
    `url` VARCHAR(500) NOT NULL COMMENT '图片URL',
    `thumbnail_url` VARCHAR(500) COMMENT '缩略图URL',
    `width` INT COMMENT '宽度',
    `height` INT COMMENT '高度',
    `size` BIGINT COMMENT '文件大小（字节）',
    `mime_type` VARCHAR(50) COMMENT 'MIME类型',
    `album_id` BIGINT COMMENT '相册ID',
    `folder_id` BIGINT COMMENT '文件夹ID',
    `tags` JSON COMMENT '标签列表',
    `is_public` TINYINT DEFAULT 1 COMMENT '是否公开: 0-否, 1-是',
    `view_count` INT DEFAULT 0 COMMENT '浏览量',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    KEY `idx_album_id` (`album_id`),
    KEY `idx_folder_id` (`folder_id`),
    KEY `idx_is_public` (`is_public`),
    KEY `idx_created_at` (`created_at`),
    CONSTRAINT `fk_image_album` FOREIGN KEY (`album_id`) REFERENCES `album` (`id`) ON DELETE SET NULL,
    CONSTRAINT `fk_image_folder` FOREIGN KEY (`folder_id`) REFERENCES `folder` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图片表';

-- =====================================================
-- 4. 视频表
-- =====================================================
CREATE TABLE IF NOT EXISTS `video` (
    `id` BIGINT PRIMARY KEY COMMENT '视频ID',
    `title` VARCHAR(100) NOT NULL COMMENT '标题',
    `description` VARCHAR(500) COMMENT '描述',
    `cover_image` VARCHAR(255) COMMENT '封面图URL',
    `type` VARCHAR(20) NOT NULL DEFAULT 'EXTERNAL' COMMENT '类型: LOCAL/EXTERNAL',
    `platform` VARCHAR(20) COMMENT '平台: BILIBILI/YOUTUBE/LOCAL',
    `video_id` VARCHAR(100) COMMENT '外链视频ID',
    `url` VARCHAR(500) COMMENT '视频URL（本地上传）',
    `file_name` VARCHAR(255) COMMENT '存储文件名（本地上传）',
    `duration` INT COMMENT '时长（秒）',
    `size` BIGINT COMMENT '文件大小（字节）',
    `category` VARCHAR(50) COMMENT '分类',
    `tags` JSON COMMENT '标签列表',
    `is_public` TINYINT DEFAULT 1 COMMENT '是否公开: 0-否, 1-是',
    `view_count` INT DEFAULT 0 COMMENT '浏览量',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    KEY `idx_type` (`type`),
    KEY `idx_platform` (`platform`),
    KEY `idx_is_public` (`is_public`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='视频表';

-- =====================================================
-- 5. 音频表
-- =====================================================
CREATE TABLE IF NOT EXISTS `audio` (
    `id` BIGINT PRIMARY KEY COMMENT '音频ID',
    `title` VARCHAR(100) NOT NULL COMMENT '标题',
    `description` VARCHAR(500) COMMENT '描述',
    `cover_image` VARCHAR(255) COMMENT '封面图URL',
    `type` VARCHAR(20) NOT NULL DEFAULT 'EXTERNAL' COMMENT '类型: LOCAL/EXTERNAL',
    `platform` VARCHAR(20) COMMENT '平台: NETEASE/LOCAL',
    `audio_id` VARCHAR(100) COMMENT '外链音频ID',
    `url` VARCHAR(500) COMMENT '音频URL（本地上传）',
    `file_name` VARCHAR(255) COMMENT '存储文件名（本地上传）',
    `duration` INT COMMENT '时长（秒）',
    `size` BIGINT COMMENT '文件大小（字节）',
    `category` VARCHAR(50) COMMENT '分类',
    `tags` JSON COMMENT '标签列表',
    `is_public` TINYINT DEFAULT 1 COMMENT '是否公开: 0-否, 1-是',
    `view_count` INT DEFAULT 0 COMMENT '播放量',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    KEY `idx_type` (`type`),
    KEY `idx_platform` (`platform`),
    KEY `idx_is_public` (`is_public`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='音频表';

-- =====================================================
-- 6. 插入默认文件夹
-- =====================================================
INSERT INTO `folder` (`id`, `name`, `parent_id`, `type`, `sort`) VALUES
(1, '默认图片', NULL, 'IMAGE', 0),
(2, '默认视频', NULL, 'VIDEO', 0),
(3, '默认音频', NULL, 'AUDIO', 0);

-- =====================================================
-- 7. 插入默认相册
-- =====================================================
INSERT INTO `album` (`id`, `name`, `slug`, `description`, `is_public`, `sort`) VALUES
(1, '默认相册', 'default', '默认相册', 1, 0);
