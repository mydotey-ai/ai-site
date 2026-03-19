-- V1.1.2__fix_article_tag_id.sql
-- 修复 article_tag 表 id 列为自增

-- MySQL: 修改 id 列为自增
ALTER TABLE `article_tag` MODIFY COLUMN `id` BIGINT NOT NULL AUTO_INCREMENT;
