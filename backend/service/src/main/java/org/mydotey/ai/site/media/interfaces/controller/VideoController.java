package org.mydotey.ai.site.media.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.media.application.query.VideoQueryService;
import org.mydotey.ai.site.media.interfaces.dto.VideoResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 视频公开控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
@Tag(name = "视频接口", description = "视频公开接口")
public class VideoController {

    private final VideoQueryService videoQueryService;

    @GetMapping
    @Operation(summary = "获取视频列表", description = "获取公开视频列表")
    public Result<List<VideoResponse>> getList(
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String category) {
        List<VideoResponse> videos = videoQueryService.findAllPublic();
        return Result.success(videos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取视频详情", description = "根据ID获取视频详情")
    public Result<VideoResponse> getById(@PathVariable Long id) {
        VideoResponse video = videoQueryService.findById(id);
        return Result.success(video);
    }
}
