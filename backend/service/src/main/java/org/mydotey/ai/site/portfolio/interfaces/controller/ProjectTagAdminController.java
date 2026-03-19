package org.mydotey.ai.site.portfolio.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.portfolio.application.command.ProjectTagCommandService;
import org.mydotey.ai.site.portfolio.application.query.ProjectTagQueryService;
import org.mydotey.ai.site.portfolio.domain.entity.ProjectTag;
import org.mydotey.ai.site.portfolio.interfaces.dto.ProjectTagRequest;
import org.mydotey.ai.site.portfolio.interfaces.dto.ProjectTagResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目标签管理控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/admin/v1/project-tags")
@Tag(name = "项目标签管理", description = "项目标签管理接口")
@RequiredArgsConstructor
public class ProjectTagAdminController {

    private final ProjectTagCommandService projectTagCommandService;
    private final ProjectTagQueryService projectTagQueryService;

    @GetMapping
    @Operation(summary = "获取标签列表")
    public Result<List<ProjectTagResponse>> list() {
        List<ProjectTag> tags = projectTagQueryService.findAll();
        List<ProjectTagResponse> response = tags.stream()
                .map(this::toResponse)
                .toList();
        return Result.success(response);
    }

    @PostMapping
    @Operation(summary = "创建标签")
    public Result<Long> create(@Valid @RequestBody ProjectTagRequest request) {
        ProjectTag tag = toEntity(request);
        Long id = projectTagCommandService.createTag(tag);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新标签")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ProjectTagRequest request) {
        ProjectTag tag = toEntity(request);
        tag.setId(id);
        projectTagCommandService.updateTag(tag);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签")
    public Result<Void> delete(@PathVariable Long id) {
        projectTagCommandService.deleteTag(id);
        return Result.success();
    }

    /**
     * 转换为实体
     */
    private ProjectTag toEntity(ProjectTagRequest request) {
        ProjectTag tag = new ProjectTag();
        tag.setName(request.getName());
        tag.setSlug(request.getSlug());
        tag.setColor(request.getColor());
        tag.setSort(request.getSort());
        return tag;
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
