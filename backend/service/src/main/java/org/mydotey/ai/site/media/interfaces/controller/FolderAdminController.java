package org.mydotey.ai.site.media.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.media.application.command.FolderCommandService;
import org.mydotey.ai.site.media.application.query.FolderQueryService;
import org.mydotey.ai.site.media.interfaces.dto.FolderRequest;
import org.mydotey.ai.site.media.interfaces.dto.FolderResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文件夹管理控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/admin/v1/folders")
@RequiredArgsConstructor
@Tag(name = "文件夹管理接口", description = "文件夹管理接口（需要管理员权限）")
@PreAuthorize("hasRole('ADMIN')")
public class FolderAdminController {

    private final FolderQueryService folderQueryService;
    private final FolderCommandService folderCommandService;

    @GetMapping
    @Operation(summary = "获取文件夹树", description = "获取所有文件夹树")
    public Result<List<FolderResponse>> getList(@RequestParam(required = false) String type) {
        List<FolderResponse> folders = type != null
                ? folderQueryService.findByType(type)
                : folderQueryService.findAll();
        return Result.success(folders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文件夹详情", description = "根据ID获取文件夹详情")
    public Result<FolderResponse> getById(@PathVariable Long id) {
        FolderResponse folder = folderQueryService.findById(id);
        return Result.success(folder);
    }

    @PostMapping
    @Operation(summary = "创建文件夹", description = "创建新文件夹")
    public Result<Long> create(@Valid @RequestBody FolderRequest request) {
        Long id = folderCommandService.create(request);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文件夹", description = "更新文件夹信息")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody FolderRequest request) {
        folderCommandService.update(id, request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文件夹", description = "删除文件夹")
    public Result<Void> delete(@PathVariable Long id) {
        folderCommandService.delete(id);
        return Result.success();
    }
}
