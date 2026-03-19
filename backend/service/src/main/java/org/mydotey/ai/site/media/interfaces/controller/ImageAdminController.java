package org.mydotey.ai.site.media.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.media.application.command.ImageCommandService;
import org.mydotey.ai.site.media.application.query.ImageQueryService;
import org.mydotey.ai.site.media.interfaces.dto.BatchRequest;
import org.mydotey.ai.site.media.interfaces.dto.ImageRequest;
import org.mydotey.ai.site.media.interfaces.dto.ImageResponse;
import org.mydotey.ai.site.media.interfaces.dto.ImageUploadResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 图片管理控制器
 *
 * @author AI-Site
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/v1/images")
@RequiredArgsConstructor
@Tag(name = "图片管理接口", description = "图片管理接口（需要管理员权限）")
@PreAuthorize("hasRole('ADMIN')")
public class ImageAdminController {

    private final ImageQueryService imageQueryService;
    private final ImageCommandService imageCommandService;

    @GetMapping
    @Operation(summary = "获取图片列表", description = "分页获取图片列表（管理端）")
    public Result<PageResult<ImageResponse>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long albumId,
            @RequestParam(required = false) Long folderId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer isPublic) {
        PageResult<ImageResponse> result = imageQueryService.findPage(page, size, albumId, folderId, keyword, isPublic);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取图片详情", description = "根据ID获取图片详情")
    public Result<ImageResponse> getById(@PathVariable Long id) {
        ImageResponse image = imageQueryService.findById(id);
        return Result.success(image);
    }

    @PostMapping
    @Operation(summary = "上传图片", description = "上传图片文件")
    public Result<ImageUploadResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long albumId,
            @RequestParam(required = false) Long folderId,
            @RequestParam(required = false) String tags,
            @RequestParam(defaultValue = "1") Integer isPublic) throws IOException {

        ImageRequest request = new ImageRequest();
        request.setTitle(title);
        request.setDescription(description);
        request.setAlbumId(albumId);
        request.setFolderId(folderId);
        request.setIsPublic(isPublic);

        // 解析标签
        if (tags != null && !tags.isEmpty()) {
            request.setTags(java.util.Arrays.asList(tags.split(",")));
        }

        ImageUploadResponse response = imageCommandService.upload(file, request);
        return Result.success(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新图片信息", description = "更新图片信息")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ImageRequest request) {
        imageCommandService.update(id, request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除图片", description = "删除图片")
    public Result<Void> delete(@PathVariable Long id) {
        imageCommandService.delete(id);
        return Result.success();
    }

    @PostMapping("/batch")
    @Operation(summary = "批量操作", description = "批量移动、删除、设置公开状态")
    public Result<Void> batchOperation(@Valid @RequestBody BatchRequest request) {
        imageCommandService.batchOperation(request);
        return Result.success();
    }
}
