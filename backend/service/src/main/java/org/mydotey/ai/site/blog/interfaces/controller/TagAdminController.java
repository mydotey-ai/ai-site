package org.mydotey.ai.site.blog.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.application.command.TagCommandService;
import org.mydotey.ai.site.blog.application.query.TagQueryService;
import org.mydotey.ai.site.blog.interfaces.dto.TagRequest;
import org.mydotey.ai.site.blog.interfaces.dto.TagResponse;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签管理控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/admin/v1/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "标签管理", description = "标签管理接口")
@RequiredArgsConstructor
public class TagAdminController {

    private final TagCommandService tagCommandService;
    private final TagQueryService tagQueryService;

    @GetMapping
    @Operation(summary = "查询所有标签")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<TagResponse>> list() {
        List<org.mydotey.ai.site.blog.domain.entity.Tag> tags = tagQueryService.findAll();
        return Result.success(tags.stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询标签")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<TagResponse> getById(@PathVariable Long id) {
        org.mydotey.ai.site.blog.domain.entity.Tag tag = tagQueryService.findById(id);
        return Result.success(toResponse(tag));
    }

    @PostMapping
    @Operation(summary = "创建标签")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Long> create(@Valid @RequestBody TagRequest request) {
        org.mydotey.ai.site.blog.domain.entity.Tag tag = toEntity(request);
        Long id = tagCommandService.createTag(tag);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新标签")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody TagRequest request) {
        org.mydotey.ai.site.blog.domain.entity.Tag tag = toEntity(request);
        tag.setId(id);
        tagCommandService.updateTag(tag);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@PathVariable Long id) {
        tagCommandService.deleteTag(id);
        return Result.success();
    }

    /**
     * 转换为实体
     */
    private org.mydotey.ai.site.blog.domain.entity.Tag toEntity(TagRequest request) {
        org.mydotey.ai.site.blog.domain.entity.Tag tag = new org.mydotey.ai.site.blog.domain.entity.Tag();
        tag.setName(request.getName());
        tag.setSlug(request.getSlug());
        tag.setColor(request.getColor());
        return tag;
    }

    /**
     * 转换为响应DTO
     */
    private TagResponse toResponse(org.mydotey.ai.site.blog.domain.entity.Tag tag) {
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
