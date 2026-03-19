package org.mydotey.ai.site.media.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.media.application.command.AlbumCommandService;
import org.mydotey.ai.site.media.application.query.AlbumQueryService;
import org.mydotey.ai.site.media.interfaces.dto.AlbumRequest;
import org.mydotey.ai.site.media.interfaces.dto.AlbumResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 相册管理控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/admin/v1/albums")
@RequiredArgsConstructor
@Tag(name = "相册管理接口", description = "相册管理接口（需要管理员权限）")
@PreAuthorize("hasRole('ADMIN')")
public class AlbumAdminController {

    private final AlbumQueryService albumQueryService;
    private final AlbumCommandService albumCommandService;

    @GetMapping
    @Operation(summary = "获取相册列表", description = "分页获取相册列表")
    public Result<PageResult<AlbumResponse>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer isPublic) {
        PageResult<AlbumResponse> result = albumQueryService.findPage(page, size, isPublic);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取相册详情", description = "根据ID获取相册详情")
    public Result<AlbumResponse> getById(@PathVariable Long id) {
        AlbumResponse album = albumQueryService.findById(id);
        return Result.success(album);
    }

    @PostMapping
    @Operation(summary = "创建相册", description = "创建新相册")
    public Result<Long> create(@Valid @RequestBody AlbumRequest request) {
        Long id = albumCommandService.create(request);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新相册", description = "更新相册信息")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody AlbumRequest request) {
        albumCommandService.update(id, request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除相册", description = "删除相册")
    public Result<Void> delete(@PathVariable Long id) {
        albumCommandService.delete(id);
        return Result.success();
    }
}
