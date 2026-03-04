package org.mydotey.ai.site.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.auth.entity.Role;
import org.mydotey.ai.site.auth.repository.RoleRepository;
import org.mydotey.ai.site.infrastructure.persistence.mapper.RoleMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleMapper roleMapper;

    @Override
    public Optional<Role> findById(Long id) {
        return Optional.ofNullable(roleMapper.selectById(id));
    }

    @Override
    public Optional<Role> findByCode(String code) {
        return Optional.ofNullable(roleMapper.findByCode(code));
    }

    @Override
    public List<Role> findAll() {
        return roleMapper.selectList(null);
    }

    @Override
    public List<Role> findByUserId(Long userId) {
        return roleMapper.findByUserId(userId);
    }

    @Override
    public void save(Role role) {
        roleMapper.insert(role);
    }
}