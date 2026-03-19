package org.mydotey.ai.site.media.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.media.application.query.AudioQueryService;
import org.mydotey.ai.site.media.interfaces.dto.AudioResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 音频公开控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/v1/audios")
@RequiredArgsConstructor
@Tag(name = "音频接口", description = "音频公开接口")
public class AudioController {

    private final AudioQueryService audioQueryService;

    @GetMapping
    @Operation(summary = "获取音频列表", description = "获取公开音频列表")
    public Result<List<AudioResponse>> getList(
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String category) {
        List<AudioResponse> audios = audioQueryService.findAllPublic();
        return Result.success(audios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取音频详情", description = "根据ID获取音频详情")
    public Result<AudioResponse> getById(@PathVariable Long id) {
        AudioResponse audio = audioQueryService.findById(id);
        return Result.success(audio);
    }
}
