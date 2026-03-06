package org.mydotey.ai.site.auth.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.auth.interfaces.dto.AuthResponse;
import org.mydotey.ai.site.auth.domain.entity.User;
import org.mydotey.ai.site.auth.domain.repository.UserRepository;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 认证查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthQueryService {

    private final UserRepository userRepository;

    /**
     * 获取当前用户信息
     */
    public AuthResponse.UserVO getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        List<String> roles = userRepository.findRoleCodesByUserId(user.getId());

        return AuthResponse.UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .bio(user.getBio())
                .roles(roles)
                .build();
    }

    /**
     * 根据用户名检查用户是否存在
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}