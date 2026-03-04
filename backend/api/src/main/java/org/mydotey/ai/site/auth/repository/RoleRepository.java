package org.mydotey.ai.site.auth.repository;

import org.mydotey.ai.site.auth.entity.Role;

import java.util.List;
import java.util.Optional;

/**
 * 角色仓储接口
 *
 * @author AI-Site
 */
public interface RoleRepository {

    /**
     * 根据ID查找角色
     */
    Optional<Role> findById(Long id);

    /**
     * 根据角色代码查找角色
     */
    Optional<Role> findByCode(String code);

    /**
     * 查找所有角色
     */
    List<Role> findAll();

    /**
     * 根据用户ID查找角色列表
     */
    List<Role> findByUserId(Long userId);

    /**
     * 保存角色
     */
    void save(Role role);
}