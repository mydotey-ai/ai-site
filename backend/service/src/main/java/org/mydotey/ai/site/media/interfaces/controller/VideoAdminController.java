package org.mydotey.ai.site.media.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.media.application.command.VideoCommandService;
import org.mydotey.ai.site.media.application.query.VideoQueryService;
import org.mydotey.ai.site.media.interfaces.dto.VideoRequest;
import org.mydotey.ai.site.media.interfaces.dto.VideoResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 视频管理控制器
 *
 * @author AI-Site
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/v1/videos")
@RequiredArgsConstructor
@Tag(name = "视频管理接口", description = "视频管理接口（需要管理员权限）")
@PreAuthorize("hasRole('ADMIN')")
public class VideoAdminController {

    private final VideoQueryService videoQueryService;
    private final VideoCommandService videoCommandService;

    @GetMapping
    @Operation(summary = "获取视频列表", description = "分页获取视频列表")
    public Result<PageResult<VideoResponse>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer isPublic) {
        PageResult<VideoResponse> result = videoQueryService.findPage(page, size, type, platform, category, isPublic);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取视频详情", description = "根据ID获取视频详情")
    public Result<VideoResponse> getById(@PathVariable Long id) {
        VideoResponse video = videoQueryService.findById(id);
        return Result.success(video);
    }

    @PostMapping
    @Operation(summary = "创建视频（外链）", description = "创建外链视频")
    public Result<Long> create(@Valid @RequestBody VideoRequest request) {
        Long id = videoCommandService.create(request);
        return Result.success(id);
    }

    @PostMapping("/upload")
    @Operation(summary = "上传视频", description = "上传本地视频")
    public Result<Long> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String coverImage,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tags,
            @RequestParam(defaultValue = "1") Integer isPublic) throws Exception {

        VideoRequest request = new VideoRequest();
        request.setTitle(title);
        request.setDescription(description);
        request.setCoverImage(coverImage);
        request.setType("LOCAL");
        request.setCategory(category);
        request.setIsPublic(isPublic);

        if (tags != null && !tags.isEmpty()) {
            request.setTags(java.util.Arrays.asList(tags.split(",")));
        }

        Long id = videoCommandService.upload(file, request);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新视频", description = "更新视频信息")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody VideoRequest request) {
        videoCommandService.update(id, request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除视频", description = "删除视频")
    public Result<Void> delete(@PathVariable Long id) {
        videoCommandService.delete(id);
        return Result.success();
    }
}
