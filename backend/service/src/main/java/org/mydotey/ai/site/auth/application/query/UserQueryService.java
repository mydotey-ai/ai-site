package org.mydotey.ai.site.auth.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.auth.domain.entity.User;
import org.mydotey.ai.site.auth.domain.repository.UserRepository;
import org.mydotey.ai.site.auth.interfaces.dto.UserQuery;
import org.mydotey.ai.site.auth.interfaces.dto.UserResponse;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    /**
     * 分页查询用户
     */
    public PageResult<UserResponse> findByPage(UserQuery query) {
        PageResult<User> result = userRepository.findByPage(query);
        List<UserResponse> list = result.getList().stream()
                .map(this::toResponse)
                .toList();
        return new PageResult<>(list, result.getTotal());
    }

    /**
     * 根据ID查询用户
     */
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        return toResponse(user);
    }

    /**
     * 获取所有用户
     */
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * 转换为响应
     */
    private UserResponse toResponse(User user) {
        List<String> roles = userRepository.findRoleCodesByUserId(user.getId());
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .bio(user.getBio())
                .status(user.getStatus())
                .roles(roles)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
