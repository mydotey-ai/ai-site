package org.mydotey.ai.site.auth.domain.repository;

import org.mydotey.ai.site.auth.domain.entity.User;
import org.mydotey.ai.site.common.module.domain.entity.PageQuery;
import org.mydotey.ai.site.common.module.interfaces.PageResult;

import java.util.List;
import java.util.Optional;

/**
 * 用户仓储接口
 *
 * @author AI-Site
 */
public interface UserRepository {

    /**
     * 根据ID查找用户
     */
    Optional<User> findById(Long id);

    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 保存用户
     */
    void save(User user);

    /**
     * 更新用户
     */
    void update(User user);

    /**
     * 删除用户
     */
    void deleteById(Long id);

    /**
     * 查找所有用户
     */
    List<User> findAll();

    /**
     * 分页查询用户
     */
    PageResult<User> findByPage(PageQuery query);

    /**
     * 统计用户总数
     */
    long count();

    /**
     * 获取用户的角色代码列表
     */
    List<String> findRoleCodesByUserId(Long userId);
}