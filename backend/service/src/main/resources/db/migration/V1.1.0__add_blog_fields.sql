-- V1.1.0__add_blog_fields.sql
-- 博客模块新增字段

-- 文章表新增字段
ALTER TABLE `article`
    ADD COLUMN `content_type` VARCHAR(20) DEFAULT 'MARKDOWN' COMMENT '内容类型: MARKDOWN, RICHTEXT' AFTER `content`,
    ADD COLUMN `seo_title` VARCHAR(100) COMMENT 'SEO 标题' AFTER `allow_comment`,
    ADD COLUMN `seo_description` VARCHAR(200) COMMENT 'SEO 描述' AFTER `seo_title`,
    ADD COLUMN `seo_keywords` VARCHAR(200) COMMENT 'SEO 关键词' AFTER `seo_description`;

-- 标签表新增字段
ALTER TABLE `tag`
    ADD COLUMN `color` VARCHAR(20) DEFAULT '#3b82f6' COMMENT '标签颜色' AFTER `slug`,
    ADD COLUMN `article_count` INT DEFAULT 0 COMMENT '文章数量' AFTER `color`;

-- 分类表新增字段
ALTER TABLE `category`
    ADD COLUMN `article_count` INT DEFAULT 0 COMMENT '文章数量' AFTER `sort_order`;
