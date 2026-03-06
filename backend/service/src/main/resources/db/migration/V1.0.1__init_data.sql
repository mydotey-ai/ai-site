-- V1.0.1__init_data.sql
-- 初始化基础数据

-- 插入默认角色
INSERT INTO `role` (`id`, `name`, `code`, `description`) VALUES
(1, '管理员', 'ADMIN', '系统管理员，拥有所有权限'),
(2, '编辑', 'EDITOR', '内容编辑，可以管理文章和作品'),
(3, '用户', 'USER', '普通用户');

-- 插入默认管理员用户
-- 密码: admin123 (BCrypt 加密)
INSERT INTO `user` (`id`, `username`, `password`, `email`, `nickname`, `status`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'admin@ai-site.com', '管理员', 1);

-- 关联管理员角色
INSERT INTO `user_role` (`id`, `user_id`, `role_id`) VALUES
(1, 1, 1);

-- 插入默认分类
INSERT INTO `category` (`id`, `name`, `slug`, `description`, `sort_order`) VALUES
(1, '技术', 'tech', '技术相关文章', 1),
(2, '生活', 'life', '生活随笔', 2),
(3, '随笔', 'essay', '个人随笔', 3);

-- 插入默认标签
INSERT INTO `tag` (`id`, `name`, `slug`) VALUES
(1, 'Java', 'java'),
(2, 'Spring Boot', 'spring-boot'),
(3, 'Vue', 'vue'),
(4, 'TypeScript', 'typescript'),
(5, 'MySQL', 'mysql');