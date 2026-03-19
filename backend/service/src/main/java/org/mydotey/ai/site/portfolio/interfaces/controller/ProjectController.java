package org.mydotey.ai.site.portfolio.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.portfolio.application.query.ProjectQuery;
import org.mydotey.ai.site.portfolio.application.query.ProjectQueryService;
import org.mydotey.ai.site.portfolio.domain.entity.Project;
import org.mydotey.ai.site.portfolio.interfaces.dto.ProjectResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目控制器（公开接口）
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/v1/projects")
@Tag(name = "项目", description = "项目公开接口")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectQueryService projectQueryService;

    @GetMapping
    @Operation(summary = "分页查询项目")
    public Result<PageResult<ProjectResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) Long tagId) {

        ProjectQuery query = new ProjectQuery();
        query.setPage(page);
        query.setSize(Math.min(size, 50));
        query.setTagId(tagId);

        PageResult<Project> result = projectQueryService.findReleased(query);
        return Result.success(toResponsePage(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询项目")
    public Result<ProjectResponse> getById(@PathVariable Long id) {
        Project project = projectQueryService.findById(id);
        return Result.success(toResponse(project));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "根据Slug查询项目")
    public Result<ProjectResponse> getBySlug(@PathVariable String slug) {
        Project project = projectQueryService.findBySlug(slug);
        return Result.success(toResponse(project));
    }

    /**
     * 转换为响应DTO
     */
    private ProjectResponse toResponse(Project project) {
        ProjectResponse.ProjectResponseBuilder builder = ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .slug(project.getSlug())
                .description(project.getDescription())
                .content(project.getContent())
                .coverImage(project.getCoverImage())
                .techStack(project.getTechStack())
                .status(project.getStatus())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt());

        if (project.getTags() != null) {
            builder.tags(project.getTags().stream()
                    .map(tag -> ProjectResponse.TagVO.builder()
                            .id(tag.getId())
                            .name(tag.getName())
                            .slug(tag.getSlug())
                            .color(tag.getColor())
                            .build())
                    .toList());
        }

        if (project.getLinks() != null) {
            builder.links(project.getLinks().stream()
                    .map(link -> ProjectResponse.LinkVO.builder()
                            .type(link.getType())
                            .label(link.getLabel())
                            .url(link.getUrl())
                            .build())
                    .toList());
        }

        return builder.build();
    }

    /**
     * 转换为分页响应
     */
    private PageResult<ProjectResponse> toResponsePage(PageResult<Project> result) {
        List<ProjectResponse> list = result.getList().stream()
                .map(this::toResponse)
                .toList();
        return PageResult.of(list, result.getTotal());
    }
}
