package org.mydotey.ai.site.media.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.media.application.query.AlbumQueryService;
import org.mydotey.ai.site.media.interfaces.dto.AlbumResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 相册公开控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/v1/albums")
@RequiredArgsConstructor
@Tag(name = "相册接口", description = "相册公开接口")
public class AlbumController {

    private final AlbumQueryService albumQueryService;

    @GetMapping
    @Operation(summary = "获取相册列表", description = "获取公开相册列表")
    public Result<List<AlbumResponse>> getList() {
        List<AlbumResponse> albums = albumQueryService.findAllPublic();
        return Result.success(albums);
    }

    @GetMapping("/{slug}")
    @Operation(summary = "获取相册详情", description = "根据Slug获取相册详情（含图片）")
    public Result<AlbumResponse> getBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        AlbumResponse album = albumQueryService.findBySlug(slug, page, size);
        return Result.success(album);
    }
}
