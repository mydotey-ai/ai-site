package org.mydotey.ai.site.creation.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.creation.application.command.PoetryCommandService;
import org.mydotey.ai.site.creation.application.query.CreationQuery;
import org.mydotey.ai.site.creation.application.query.PoetryQueryService;
import org.mydotey.ai.site.creation.domain.entity.Poetry;
import org.mydotey.ai.site.creation.domain.entity.PoetryCategory;
import org.mydotey.ai.site.creation.domain.repository.PoetryCategoryRepository;
import org.mydotey.ai.site.creation.interfaces.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 诗歌管理控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/admin/v1/poetry")
@Tag(name = "诗歌管理", description = "诗歌管理接口")
@RequiredArgsConstructor
public class PoetryAdminController {

    private final PoetryCommandService poetryCommandService;
    private final PoetryQueryService poetryQueryService;
    private final PoetryCategoryRepository poetryCategoryRepository;

    @GetMapping
    @Operation(summary = "分页查询诗歌（管理端）")
    public Result<PageResult<PoetryResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {

        CreationQuery query = new CreationQuery();
        query.setPage(page);
        query.setSize(size);
        query.setCategoryId(categoryId);
        query.setStatus(status);
        query.setKeyword(keyword);

        PageResult<Poetry> result = poetryQueryService.findPage(query);
        return Result.success(toResponsePage(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询诗歌")
    public Result<PoetryResponse> getById(@PathVariable Long id) {
        Poetry poetry = poetryQueryService.findById(id);
        return Result.success(toResponse(poetry));
    }

    @PostMapping
    @Operation(summary = "创建诗歌")
    public Result<Long> create(@Valid @RequestBody PoetryRequest request) {
        Poetry poetry = toEntity(request);
        Long id = poetryCommandService.createPoetry(poetry);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新诗歌")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody PoetryRequest request) {
        Poetry poetry = toEntity(request);
        poetry.setId(id);
        poetryCommandService.updatePoetry(poetry);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除诗歌")
    public Result<Void> delete(@PathVariable Long id) {
        poetryCommandService.deletePoetry(id);
        return Result.success();
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "发布诗歌")
    public Result<Void> publish(@PathVariable Long id) {
        poetryCommandService.publishPoetry(id);
        return Result.success();
    }

    @PostMapping("/batch")
    @Operation(summary = "批量操作")
    public Result<Void> batch(@Valid @RequestBody BatchRequest request) {
        switch (request.getAction().toLowerCase()) {
            case "delete" -> poetryCommandService.batchDelete(request.getIds());
            case "publish" -> poetryCommandService.batchUpdateStatus(request.getIds(), "PUBLISHED");
            default -> throw new IllegalArgumentException("不支持的操作类型: " + request.getAction());
        }
        return Result.success();
    }

    @GetMapping("/categories")
    @Operation(summary = "获取所有分类")
    public Result<List<PoetryCategoryResponse>> getCategories() {
        List<PoetryCategory> categories = poetryCategoryRepository.findAll();
        return Result.success(categories.stream().map(this::toCategoryResponse).toList());
    }

    private Poetry toEntity(PoetryRequest request) {
        Poetry poetry = new Poetry();
        poetry.setTitle(request.getTitle());
        poetry.setSlug(request.getSlug());
        poetry.setAuthor(request.getAuthor());
        poetry.setContent(request.getContent());
        poetry.setCategoryId(request.getCategoryId());
        poetry.setStatus(request.getStatus());
        return poetry;
    }

    private PoetryResponse toResponse(Poetry poetry) {
        PoetryResponse.PoetryResponseBuilder builder = PoetryResponse.builder()
                .id(poetry.getId())
                .title(poetry.getTitle())
                .slug(poetry.getSlug())
                .author(poetry.getAuthor())
                .content(poetry.getContent())
                .status(poetry.getStatus())
                .viewCount(poetry.getViewCount())
                .createdAt(poetry.getCreatedAt())
                .updatedAt(poetry.getUpdatedAt());

        if (poetry.getCategory() != null) {
            builder.category(PoetryResponse.CategoryVO.builder()
                    .id(poetry.getCategory().getId())
                    .name(poetry.getCategory().getName())
                    .slug(poetry.getCategory().getSlug())
                    .build());
        }

        return builder.build();
    }

    private PoetryCategoryResponse toCategoryResponse(PoetryCategory category) {
        return PoetryCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .sort(category.getSort())
                .createdAt(category.getCreatedAt())
                .build();
    }

    private PageResult<PoetryResponse> toResponsePage(PageResult<Poetry> result) {
        List<PoetryResponse> list = result.getList().stream()
                .map(this::toResponse)
                .toList();
        return PageResult.of(list, result.getTotal());
    }
}
