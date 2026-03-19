package org.mydotey.ai.site.media.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.media.application.command.AudioCommandService;
import org.mydotey.ai.site.media.application.query.AudioQueryService;
import org.mydotey.ai.site.media.interfaces.dto.AudioRequest;
import org.mydotey.ai.site.media.interfaces.dto.AudioResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 音频管理控制器
 *
 * @author AI-Site
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/v1/audios")
@RequiredArgsConstructor
@Tag(name = "音频管理接口", description = "音频管理接口（需要管理员权限）")
@PreAuthorize("hasRole('ADMIN')")
public class AudioAdminController {

    private final AudioQueryService audioQueryService;
    private final AudioCommandService audioCommandService;

    @GetMapping
    @Operation(summary = "获取音频列表", description = "分页获取音频列表")
    public Result<PageResult<AudioResponse>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer isPublic) {
        PageResult<AudioResponse> result = audioQueryService.findPage(page, size, type, platform, category, isPublic);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取音频详情", description = "根据ID获取音频详情")
    public Result<AudioResponse> getById(@PathVariable Long id) {
        AudioResponse audio = audioQueryService.findById(id);
        return Result.success(audio);
    }

    @PostMapping
    @Operation(summary = "创建音频（外链）", description = "创建外链音频")
    public Result<Long> create(@Valid @RequestBody AudioRequest request) {
        Long id = audioCommandService.create(request);
        return Result.success(id);
    }

    @PostMapping("/upload")
    @Operation(summary = "上传音频", description = "上传本地音频")
    public Result<Long> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String coverImage,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tags,
            @RequestParam(defaultValue = "1") Integer isPublic) throws Exception {

        AudioRequest request = new AudioRequest();
        request.setTitle(title);
        request.setDescription(description);
        request.setCoverImage(coverImage);
        request.setType("LOCAL");
        request.setCategory(category);
        request.setIsPublic(isPublic);

        if (tags != null && !tags.isEmpty()) {
            request.setTags(java.util.Arrays.asList(tags.split(",")));
        }

        Long id = audioCommandService.upload(file, request);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新音频", description = "更新音频信息")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody AudioRequest request) {
        audioCommandService.update(id, request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除音频", description = "删除音频")
    public Result<Void> delete(@PathVariable Long id) {
        audioCommandService.delete(id);
        return Result.success();
    }
}
