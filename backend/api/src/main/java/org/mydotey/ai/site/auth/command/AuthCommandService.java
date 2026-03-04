package org.mydotey.ai.site.auth.command;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.auth.dto.AuthResponse;
import org.mydotey.ai.site.auth.entity.User;
import org.mydotey.ai.site.auth.entity.UserRole;
import org.mydotey.ai.site.auth.repository.RoleRepository;
import org.mydotey.ai.site.auth.repository.UserRepository;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.mydotey.ai.site.common.security.JwtTokenProvider;
import org.mydotey.ai.site.infrastructure.persistence.mapper.UserRoleMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 认证命令服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 用户登录
     */
    public AuthResponse login(LoginCommand command) {
        // 查找用户
        User user = userRepository.findByUsername(command.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED, "用户名或密码错误"));

        // 验证密码
        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED, "用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ErrorCode.USER_DISABLED);
        }

        // 生成 Token
        String token = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        // 获取用户角色
        List<String> roles = userRepository.findRoleCodesByUserId(user.getId());

        // 构建响应
        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(AuthResponse.UserVO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .avatar(user.getAvatar())
                        .bio(user.getBio())
                        .roles(roles)
                        .build())
                .build();
    }

    /**
     * 用户注册
     */
    public Long register(RegisterCommand command) {
        // 检查用户名是否存在
        if (userRepository.existsByUsername(command.getUsername())) {
            throw new BusinessException(ErrorCode.USERNAME_EXISTS);
        }

        // 检查邮箱是否存在
        if (userRepository.existsByEmail(command.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_EXISTS);
        }

        // 创建用户
        User user = new User();
        user.setUsername(command.getUsername());
        user.setPassword(passwordEncoder.encode(command.getPassword()));
        user.setEmail(command.getEmail());
        user.setNickname(command.getNickname() != null ? command.getNickname() : command.getUsername());
        user.setStatus(1);

        userRepository.save(user);

        // 分配默认角色
        roleRepository.findByCode("USER").ifPresent(role -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRoleMapper.insert(userRole);
        });

        return user.getId();
    }
}