package org.mydotey.ai.site.auth.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.auth.application.command.UserCommandService;
import org.mydotey.ai.site.auth.application.query.UserQueryService;
import org.mydotey.ai.site.auth.interfaces.dto.UserQuery;
import org.mydotey.ai.site.auth.interfaces.dto.UserRequest;
import org.mydotey.ai.site.auth.interfaces.dto.UserResponse;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/admin/v1/users")
@Tag(name = "用户管理", description = "用户管理接口")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @GetMapping
    @Operation(summary = "分页查询用户")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<UserResponse>> list(UserQuery query) {
        return Result.success(userQueryService.findByPage(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<UserResponse> getById(@PathVariable Long id) {
        return Result.success(userQueryService.findById(id));
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有用户")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<UserResponse>> getAll() {
        return Result.success(userQueryService.findAll());
    }

    @PostMapping
    @Operation(summary = "创建用户")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Long> create(@Valid @RequestBody UserRequest request) {
        return Result.success(userCommandService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        userCommandService.update(id, request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@PathVariable Long id) {
        userCommandService.delete(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新用户状态")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        userCommandService.updateStatus(id, status);
        return Result.success();
    }
}
