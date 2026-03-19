package org.mydotey.ai.site.creation.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.creation.application.query.CreationQuery;
import org.mydotey.ai.site.creation.application.query.PoetryQueryService;
import org.mydotey.ai.site.creation.domain.entity.Poetry;
import org.mydotey.ai.site.creation.domain.entity.PoetryCategory;
import org.mydotey.ai.site.creation.domain.repository.PoetryCategoryRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.PoetryMapper;
import org.mydotey.ai.site.creation.interfaces.dto.PoetryCategoryResponse;
import org.mydotey.ai.site.creation.interfaces.dto.PoetryResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 诗歌控制器（公开接口）
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/v1/poetry")
@Tag(name = "诗歌", description = "诗歌公开接口")
@RequiredArgsConstructor
public class PoetryController {

    private final PoetryQueryService poetryQueryService;
    private final PoetryCategoryRepository poetryCategoryRepository;
    private final PoetryMapper poetryMapper;

    @GetMapping
    @Operation(summary = "分页查询诗歌")
    public Result<PageResult<PoetryResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long categoryId) {

        CreationQuery query = new CreationQuery();
        query.setPage(page);
        query.setSize(Math.min(size, 50));
        query.setCategoryId(categoryId);

        PageResult<Poetry> result = poetryQueryService.findPublished(query);
        return Result.success(toResponsePage(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询诗歌")
    public Result<PoetryResponse> getById(@PathVariable Long id) {
        Poetry poetry = poetryQueryService.findById(id);
        poetryMapper.incrementViewCount(id);
        return Result.success(toResponse(poetry));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "根据Slug查询诗歌")
    public Result<PoetryResponse> getBySlug(@PathVariable String slug) {
        Poetry poetry = poetryQueryService.findBySlug(slug);
        poetryMapper.incrementViewCount(poetry.getId());
        return Result.success(toResponse(poetry));
    }

    @GetMapping("/categories")
    @Operation(summary = "获取诗歌分类列表")
    public Result<List<PoetryCategoryResponse>> getCategories() {
        List<PoetryCategory> categories = poetryCategoryRepository.findAll();
        return Result.success(categories.stream()
                .map(c -> PoetryCategoryResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .slug(c.getSlug())
                        .sort(c.getSort())
                        .createdAt(c.getCreatedAt())
                        .build())
                .toList());
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

    private PageResult<PoetryResponse> toResponsePage(PageResult<Poetry> result) {
        List<PoetryResponse> list = result.getList().stream()
                .map(this::toResponse)
                .toList();
        return PageResult.of(list, result.getTotal());
    }
}
