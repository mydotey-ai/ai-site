package org.mydotey.ai.site.auth.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.auth.application.command.AuthCommandService;
import org.mydotey.ai.site.auth.application.command.LoginCommand;
import org.mydotey.ai.site.auth.application.command.RegisterCommand;
import org.mydotey.ai.site.auth.interfaces.dto.AuthResponse;
import org.mydotey.ai.site.auth.interfaces.dto.LoginRequest;
import org.mydotey.ai.site.auth.interfaces.dto.RegisterRequest;
import org.mydotey.ai.site.auth.application.query.AuthQueryService;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/admin/v1/auth")
@Tag(name = "认证", description = "用户认证相关接口")
@RequiredArgsConstructor
public class AuthController {

    private final AuthCommandService authCommandService;
    private final AuthQueryService authQueryService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginCommand command = new LoginCommand();
        command.setUsername(request.getUsername());
        command.setPassword(request.getPassword());

        return Result.success(authCommandService.login(command));
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<Long> register(@Valid @RequestBody RegisterRequest request) {
        RegisterCommand command = new RegisterCommand();
        command.setUsername(request.getUsername());
        command.setPassword(request.getPassword());
        command.setEmail(request.getEmail());
        command.setNickname(request.getNickname());

        return Result.success(authCommandService.register(command));
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息")
    public Result<AuthResponse.UserVO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return Result.success(authQueryService.getCurrentUser(userDetails.getUsername()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新Token")
    public Result<AuthResponse> refreshToken(@AuthenticationPrincipal UserDetails userDetails) {
        // TODO: 实现 refresh token 逻辑
        return Result.success(null);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出")
    public Result<Void> logout() {
        // JWT 无状态，登出只需前端清除 token
        return Result.success();
    }
}