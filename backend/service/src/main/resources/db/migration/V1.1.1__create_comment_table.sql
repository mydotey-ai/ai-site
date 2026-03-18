-- V1.1.1__create_comment_table.sql
-- 创建评论表

CREATE TABLE IF NOT EXISTS `comment` (
    `id` BIGINT PRIMARY KEY,
    `article_id` BIGINT NOT NULL COMMENT '文章ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父评论ID, 0表示顶级评论',
    `nickname` VARCHAR(50) NOT NULL COMMENT '昵称',
    `email` VARCHAR(255) COMMENT '邮箱(AES加密存储)',
    `website` VARCHAR(200) COMMENT '网站',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `ip` VARCHAR(50) COMMENT 'IP地址',
    `user_agent` VARCHAR(500) COMMENT '用户代理',
    `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING-待审核, APPROVED-已通过, SPAM-垃圾',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    KEY `idx_article_id` (`article_id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';
