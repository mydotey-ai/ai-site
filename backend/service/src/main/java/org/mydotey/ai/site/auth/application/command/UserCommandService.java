package org.mydotey.ai.site.auth.application.command;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.auth.domain.entity.User;
import org.mydotey.ai.site.auth.domain.repository.UserRepository;
import org.mydotey.ai.site.auth.interfaces.dto.UserRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户命令服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 创建用户
     */
    public Long create(UserRequest request) {
        // 检查用户名是否存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("用户名已存在");
        }

        // 检查邮箱是否存在
        if (request.getEmail() != null && !request.getEmail().isEmpty()
                && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("邮箱已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setAvatar(request.getAvatar());
        user.setBio(request.getBio());
        user.setStatus(request.getStatus() != null ? request.getStatus() : 1);

        userRepository.save(user);
        return user.getId();
    }

    /**
     * 更新用户
     */
    public void update(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        // 检查用户名是否被其他用户使用
        if (!user.getUsername().equals(request.getUsername())
                && userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("用户名已存在");
        }

        // 检查邮箱是否被其他用户使用
        if (request.getEmail() != null && !request.getEmail().isEmpty()
                && !request.getEmail().equals(user.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("邮箱已存在");
        }

        user.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setAvatar(request.getAvatar());
        user.setBio(request.getBio());
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        userRepository.update(user);
    }

    /**
     * 删除用户
     */
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * 更新用户状态
     */
    public void updateStatus(Long id, Integer status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        user.setStatus(status);
        userRepository.update(user);
    }

    /**
     * 修改密码
     */
    public void changePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("原密码错误");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.update(user);
    }
}
