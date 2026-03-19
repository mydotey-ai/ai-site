package org.mydotey.ai.site.blog.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.application.query.TagQueryService;
import org.mydotey.ai.site.blog.domain.entity.Tag;
import org.mydotey.ai.site.blog.interfaces.dto.TagResponse;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签控制器（公开接口）
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/v1/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "标签", description = "标签公开接口")
@RequiredArgsConstructor
public class TagController {

    private final TagQueryService tagQueryService;

    @GetMapping
    @Operation(summary = "查询所有标签")
    public Result<List<TagResponse>> list() {
        List<Tag> tags = tagQueryService.findAll();
        return Result.success(tags.stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询标签")
    public Result<TagResponse> getById(@PathVariable Long id) {
        Tag tag = tagQueryService.findById(id);
        return Result.success(toResponse(tag));
    }

    /**
     * 转换为响应DTO
     */
    private TagResponse toResponse(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .slug(tag.getSlug())
                .color(tag.getColor())
                .articleCount(tag.getArticleCount())
                .createdAt(tag.getCreatedAt())
                .updatedAt(tag.getUpdatedAt())
                .build();
    }
}
