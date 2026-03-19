package org.mydotey.ai.site.blog.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.application.query.CategoryQueryService;
import org.mydotey.ai.site.blog.domain.entity.Category;
import org.mydotey.ai.site.blog.interfaces.dto.CategoryResponse;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器（公开接口）
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "分类", description = "分类公开接口")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryQueryService categoryQueryService;

    @GetMapping
    @Operation(summary = "查询所有分类")
    public Result<List<CategoryResponse>> list() {
        List<Category> categories = categoryQueryService.findAll();
        return Result.success(categories.stream().map(this::toResponse).toList());
    }

    @GetMapping("/tree")
    @Operation(summary = "查询分类树")
    public Result<List<CategoryResponse>> tree() {
        List<Category> categories = categoryQueryService.findTree();
        return Result.success(categories.stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询分类")
    public Result<CategoryResponse> getById(@PathVariable Long id) {
        Category category = categoryQueryService.findById(id);
        return Result.success(toResponse(category));
    }

    /**
     * 转换为响应DTO
     */
    private CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .parentId(category.getParentId())
                .sortOrder(category.getSortOrder())
                .articleCount(category.getArticleCount())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
