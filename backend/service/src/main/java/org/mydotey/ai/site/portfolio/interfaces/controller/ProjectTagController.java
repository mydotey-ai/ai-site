package org.mydotey.ai.site.portfolio.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.portfolio.application.query.ProjectTagQueryService;
import org.mydotey.ai.site.portfolio.domain.entity.ProjectTag;
import org.mydotey.ai.site.portfolio.interfaces.dto.ProjectTagResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目标签控制器（公开接口）
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/v1/project-tags")
@Tag(name = "项目标签", description = "项目标签公开接口")
@RequiredArgsConstructor
public class ProjectTagController {

    private final ProjectTagQueryService projectTagQueryService;

    @GetMapping
    @Operation(summary = "获取项目标签列表")
    public Result<List<ProjectTagResponse>> list() {
        List<ProjectTag> tags = projectTagQueryService.findAll();
        List<ProjectTagResponse> response = tags.stream()
                .map(this::toResponse)
                .toList();
        return Result.success(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询标签")
    public Result<ProjectTagResponse> getById(@PathVariable Long id) {
        ProjectTag tag = projectTagQueryService.findById(id);
        return Result.success(toResponse(tag));
    }

    /**
     * 转换为响应DTO
     */
    private ProjectTagResponse toResponse(ProjectTag tag) {
        return ProjectTagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .slug(tag.getSlug())
                .color(tag.getColor())
                .sort(tag.getSort())
                .projectCount(tag.getProjectCount())
                .build();
    }
}
