package org.mydotey.ai.site.portfolio.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.portfolio.application.command.ProjectCommandService;
import org.mydotey.ai.site.portfolio.application.query.ProjectQuery;
import org.mydotey.ai.site.portfolio.application.query.ProjectQueryService;
import org.mydotey.ai.site.portfolio.domain.entity.Project;
import org.mydotey.ai.site.portfolio.domain.entity.ProjectLink;
import org.mydotey.ai.site.portfolio.interfaces.dto.BatchRequest;
import org.mydotey.ai.site.portfolio.interfaces.dto.ProjectRequest;
import org.mydotey.ai.site.portfolio.interfaces.dto.ProjectRequest.LinkRequest;
import org.mydotey.ai.site.portfolio.interfaces.dto.ProjectResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目管理控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/admin/v1/projects")
@Tag(name = "项目管理", description = "项目管理接口")
@RequiredArgsConstructor
public class ProjectAdminController {

    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;

    @GetMapping
    @Operation(summary = "分页查询项目（管理端）")
    public Result<PageResult<ProjectResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {

        ProjectQuery query = new ProjectQuery();
        query.setPage(page);
        query.setSize(size);
        query.setTagId(tagId);
        query.setStatus(status);
        query.setKeyword(keyword);

        PageResult<Project> result = projectQueryService.findPage(query);
        return Result.success(toResponsePage(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询项目")
    public Result<ProjectResponse> getById(@PathVariable Long id) {
        Project project = projectQueryService.findById(id);
        return Result.success(toResponse(project));
    }

    @PostMapping
    @Operation(summary = "创建项目")
    public Result<Long> create(@Valid @RequestBody ProjectRequest request) {
        Project project = toEntity(request);
        List<ProjectLink> links = toLinkEntities(request.getLinks());
        Long id = projectCommandService.createProject(project, request.getTagIds(), links);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新项目")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
        Project project = toEntity(request);
        project.setId(id);
        List<ProjectLink> links = toLinkEntities(request.getLinks());
        projectCommandService.updateProject(project, request.getTagIds(), links);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除项目")
    public Result<Void> delete(@PathVariable Long id) {
        projectCommandService.deleteProject(id);
        return Result.success();
    }

    @PostMapping("/{id}/release")
    @Operation(summary = "发布项目")
    public Result<Void> release(@PathVariable Long id) {
        projectCommandService.releaseProject(id);
        return Result.success();
    }

    @PostMapping("/{id}/archive")
    @Operation(summary = "归档项目")
    public Result<Void> archive(@PathVariable Long id) {
        projectCommandService.archiveProject(id);
        return Result.success();
    }

    @PostMapping("/batch")
    @Operation(summary = "批量操作")
    public Result<Void> batch(@Valid @RequestBody BatchRequest request) {
        switch (request.getAction().toLowerCase()) {
            case "delete" -> projectCommandService.batchDelete(request.getIds());
            case "release" -> projectCommandService.batchUpdateStatus(request.getIds(), "RELEASED");
            case "archive" -> projectCommandService.batchUpdateStatus(request.getIds(), "ARCHIVED");
            default -> throw new IllegalArgumentException("不支持的操作类型: " + request.getAction());
        }
        return Result.success();
    }

    /**
     * 转换为实体
     */
    private Project toEntity(ProjectRequest request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setSlug(request.getSlug());
        project.setDescription(request.getDescription());
        project.setContent(request.getContent());
        project.setCoverImage(request.getCoverImage());
        project.setTechStack(request.getTechStack());
        project.setStatus(request.getStatus());
        return project;
    }

    /**
     * 转换链接列表
     */
    private List<ProjectLink> toLinkEntities(List<LinkRequest> links) {
        if (links == null) return null;
        return links.stream().map(link -> {
            ProjectLink entity = new ProjectLink();
            entity.setType(link.getType());
            entity.setLabel(link.getLabel());
            entity.setUrl(link.getUrl());
            return entity;
        }).toList();
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
