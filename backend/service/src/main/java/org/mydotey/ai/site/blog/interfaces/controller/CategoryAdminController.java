package org.mydotey.ai.site.blog.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.application.command.CategoryCommandService;
import org.mydotey.ai.site.blog.application.query.CategoryQueryService;
import org.mydotey.ai.site.blog.domain.entity.Category;
import org.mydotey.ai.site.blog.interfaces.dto.CategoryRequest;
import org.mydotey.ai.site.blog.interfaces.dto.CategoryResponse;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/admin/v1/categories")
@Tag(name = "分类管理", description = "分类管理接口")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

    @GetMapping
    @Operation(summary = "查询所有分类")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<CategoryResponse>> list() {
        List<Category> categories = categoryQueryService.findAll();
        return Result.success(categories.stream().map(this::toResponse).toList());
    }

    @GetMapping("/tree")
    @Operation(summary = "查询分类树")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<CategoryResponse>> tree() {
        List<Category> categories = categoryQueryService.findTree();
        return Result.success(categories.stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询分类")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<CategoryResponse> getById(@PathVariable Long id) {
        Category category = categoryQueryService.findById(id);
        return Result.success(toResponse(category));
    }

    @PostMapping
    @Operation(summary = "创建分类")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Long> create(@Valid @RequestBody CategoryRequest request) {
        Category category = toEntity(request);
        Long id = categoryCommandService.createCategory(category);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新分类")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        Category category = toEntity(request);
        category.setId(id);
        categoryCommandService.updateCategory(category);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@PathVariable Long id) {
        categoryCommandService.deleteCategory(id);
        return Result.success();
    }

    /**
     * 转换为实体
     */
    private Category toEntity(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setDescription(request.getDescription());
        category.setParentId(request.getParentId());
        category.setSortOrder(request.getSortOrder());
        return category;
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
