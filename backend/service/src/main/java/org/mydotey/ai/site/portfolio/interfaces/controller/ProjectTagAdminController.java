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

/**
 * 项目标签管理控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/admin/v1/project-tags")
@Tag(name = "项目标签管理", description = "项目标签管理接口")
@RequiredArgsConstructor
public class ProjectTagAdminController {

    private final ProjectTagCommandService projectTagCommandService;
    private final ProjectTagQueryService projectTagQueryService;

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
}
