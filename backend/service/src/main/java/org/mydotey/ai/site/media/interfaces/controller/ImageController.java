package org.mydotey.ai.site.media.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.media.application.query.ImageQueryService;
import org.mydotey.ai.site.media.interfaces.dto.ImageResponse;
import org.springframework.web.bind.annotation.*;

/**
 * 图片公开控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@Tag(name = "图片接口", description = "图片公开接口")
public class ImageController {

    private final ImageQueryService imageQueryService;

    @GetMapping
    @Operation(summary = "获取图片列表", description = "分页获取公开图片列表")
    public Result<PageResult<ImageResponse>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long albumId,
            @RequestParam(required = false) String keyword) {
        PageResult<ImageResponse> result = imageQueryService.findPage(page, size, albumId, null, keyword, 1);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取图片详情", description = "根据ID获取图片详情")
    public Result<ImageResponse> getById(@PathVariable Long id) {
        ImageResponse image = imageQueryService.findById(id);
        return Result.success(image);
    }
}
