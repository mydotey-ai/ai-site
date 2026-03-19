-- V1.3.0__creation_module.sql
-- 创作模块数据库迁移

-- =====================================================
-- 1. 小说分类表
-- =====================================================
CREATE TABLE IF NOT EXISTS `novel_category` (
    `id` BIGINT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `slug` VARCHAR(50) NOT NULL COMMENT 'URL别名',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    UNIQUE KEY `uk_slug` (`slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='小说分类表';

-- 插入默认小说分类
INSERT INTO `novel_category` (`id`, `name`, `slug`, `sort`) VALUES
(1, '玄幻奇幻', 'xuanhuan', 1),
(2, '武侠仙侠', 'wuxia', 2),
(3, '都市言情', 'dushi', 3),
(4, '历史军事', 'lishi', 4),
(5, '科幻灵异', 'kehuan', 5),
(6, '其他', 'other', 6);

-- =====================================================
-- 2. 小说表
-- =====================================================
CREATE TABLE IF NOT EXISTS `novel` (
    `id` BIGINT PRIMARY KEY,
    `title` VARCHAR(100) NOT NULL COMMENT '小说标题',
    `slug` VARCHAR(100) NOT NULL COMMENT 'URL别名',
    `author` VARCHAR(50) COMMENT '作者',
    `summary` VARCHAR(1000) COMMENT '简介',
    `cover_image` VARCHAR(255) COMMENT '封面图URL',
    `category_id` BIGINT COMMENT '分类ID',
    `status` VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态: DRAFT/PUBLISHED/COMPLETED',
    `word_count` INT DEFAULT 0 COMMENT '总字数',
    `chapter_count` INT DEFAULT 0 COMMENT '章节数',
    `view_count` INT DEFAULT 0 COMMENT '浏览量',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    UNIQUE KEY `uk_slug` (`slug`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`),
    CONSTRAINT `fk_novel_category` FOREIGN KEY (`category_id`) REFERENCES `novel_category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='小说表';

-- =====================================================
-- 3. 章节表
-- =====================================================
CREATE TABLE IF NOT EXISTS `chapter` (
    `id` BIGINT PRIMARY KEY,
    `novel_id` BIGINT NOT NULL COMMENT '小说ID',
    `title` VARCHAR(100) NOT NULL COMMENT '章节标题',
    `content` LONGTEXT COMMENT '章节内容',
    `word_count` INT DEFAULT 0 COMMENT '字数',
    `chapter_no` INT NOT NULL COMMENT '章节序号',
    `status` VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态: DRAFT/PUBLISHED',
    `view_count` INT DEFAULT 0 COMMENT '浏览量',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    UNIQUE KEY `uk_novel_chapter_no` (`novel_id`, `chapter_no`),
    KEY `idx_novel_id` (`novel_id`),
    CONSTRAINT `fk_chapter_novel` FOREIGN KEY (`novel_id`) REFERENCES `novel` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='章节表';

-- =====================================================
-- 4. 诗歌分类表
-- =====================================================
CREATE TABLE IF NOT EXISTS `poetry_category` (
    `id` BIGINT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `slug` VARCHAR(50) NOT NULL COMMENT 'URL别名',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    UNIQUE KEY `uk_slug` (`slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='诗歌分类表';

-- 插入默认诗歌分类
INSERT INTO `poetry_category` (`id`, `name`, `slug`, `sort`) VALUES
(1, '古体诗', 'guti', 1),
(2, '近体诗', 'jinti', 2),
(3, '词', 'ci', 3),
(4, '现代诗', 'xiandai', 4),
(5, '其他', 'other', 5);

-- =====================================================
-- 5. 诗歌表
-- =====================================================
CREATE TABLE IF NOT EXISTS `poetry` (
    `id` BIGINT PRIMARY KEY,
    `title` VARCHAR(100) NOT NULL COMMENT '标题',
    `slug` VARCHAR(100) NOT NULL COMMENT 'URL别名',
    `author` VARCHAR(50) COMMENT '作者',
    `content` TEXT COMMENT '内容',
    `category_id` BIGINT COMMENT '分类ID',
    `status` VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态: DRAFT/PUBLISHED',
    `view_count` INT DEFAULT 0 COMMENT '浏览量',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    UNIQUE KEY `uk_slug` (`slug`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`),
    CONSTRAINT `fk_poetry_category` FOREIGN KEY (`category_id`) REFERENCES `poetry_category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='诗歌表';

-- =====================================================
-- 6. 散文分类表
-- =====================================================
CREATE TABLE IF NOT EXISTS `essay_category` (
    `id` BIGINT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `slug` VARCHAR(50) NOT NULL COMMENT 'URL别名',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    UNIQUE KEY `uk_slug` (`slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='散文分类表';

-- 插入默认散文分类
INSERT INTO `essay_category` (`id`, `name`, `slug`, `sort`) VALUES
(1, '叙事散文', 'xushi', 1),
(2, '抒情散文', 'shuqing', 2),
(3, '议论散文', 'yilun', 3),
(4, '游记', 'youji', 4),
(5, '其他', 'other', 5);

-- =====================================================
-- 7. 散文表
-- =====================================================
CREATE TABLE IF NOT EXISTS `essay` (
    `id` BIGINT PRIMARY KEY,
    `title` VARCHAR(100) NOT NULL COMMENT '标题',
    `slug` VARCHAR(100) NOT NULL COMMENT 'URL别名',
    `author` VARCHAR(50) COMMENT '作者',
    `summary` VARCHAR(500) COMMENT '摘要',
    `content` LONGTEXT COMMENT '内容',
    `category_id` BIGINT COMMENT '分类ID',
    `status` VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态: DRAFT/PUBLISHED',
    `view_count` INT DEFAULT 0 COMMENT '浏览量',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    UNIQUE KEY `uk_slug` (`slug`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`),
    CONSTRAINT `fk_essay_category` FOREIGN KEY (`category_id`) REFERENCES `essay_category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='散文表';
