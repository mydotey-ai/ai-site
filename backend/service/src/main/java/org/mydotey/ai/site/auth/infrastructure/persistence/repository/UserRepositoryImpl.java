package org.mydotey.ai.site.auth.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.auth.domain.entity.User;
import org.mydotey.ai.site.auth.domain.repository.UserRepository;
import org.mydotey.ai.site.auth.infrastructure.persistence.mapper.UserMapper;
import org.mydotey.ai.site.common.module.domain.entity.PageQuery;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMapper.selectById(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userMapper.findByUsername(username));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userMapper.findByEmail(email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userMapper.countByUsername(username) > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userMapper.countByEmail(email) > 0;
    }

    @Override
    public void save(User user) {
        userMapper.insert(user);
    }

    @Override
    public void update(User user) {
        userMapper.updateById(user);
    }

    @Override
    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectList(null);
    }

    @Override
    public PageResult<User> findByPage(PageQuery query) {
        Page<User> page = new Page<>(query.getPage(), query.getSize());
        Page<User> result = userMapper.selectPage(page, new LambdaQueryWrapper<User>()
                .orderByDesc(User::getCreatedAt));
        return new PageResult<>(result.getRecords(), result.getTotal());
    }

    @Override
    public long count() {
        return userMapper.selectCount(null);
    }

    @Override
    public List<String> findRoleCodesByUserId(Long userId) {
        return userMapper.findRoleCodesByUserId(userId);
    }
}