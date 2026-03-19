package org.mydotey.ai.site.creation.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.creation.application.query.CreationQuery;
import org.mydotey.ai.site.creation.application.query.EssayQueryService;
import org.mydotey.ai.site.creation.domain.entity.Essay;
import org.mydotey.ai.site.creation.domain.entity.EssayCategory;
import org.mydotey.ai.site.creation.domain.repository.EssayCategoryRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.EssayMapper;
import org.mydotey.ai.site.creation.interfaces.dto.EssayCategoryResponse;
import org.mydotey.ai.site.creation.interfaces.dto.EssayResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 散文控制器（公开接口）
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/v1/essays")
@Tag(name = "散文", description = "散文公开接口")
@RequiredArgsConstructor
public class EssayController {

    private final EssayQueryService essayQueryService;
    private final EssayCategoryRepository essayCategoryRepository;
    private final EssayMapper essayMapper;

    @GetMapping
    @Operation(summary = "分页查询散文")
    public Result<PageResult<EssayResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId) {

        CreationQuery query = new CreationQuery();
        query.setPage(page);
        query.setSize(Math.min(size, 50));
        query.setCategoryId(categoryId);

        PageResult<Essay> result = essayQueryService.findPublished(query);
        return Result.success(toResponsePage(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询散文")
    public Result<EssayResponse> getById(@PathVariable Long id) {
        Essay essay = essayQueryService.findById(id);
        essayMapper.incrementViewCount(id);
        return Result.success(toResponse(essay));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "根据Slug查询散文")
    public Result<EssayResponse> getBySlug(@PathVariable String slug) {
        Essay essay = essayQueryService.findBySlug(slug);
        essayMapper.incrementViewCount(essay.getId());
        return Result.success(toResponse(essay));
    }

    @GetMapping("/categories")
    @Operation(summary = "获取散文分类列表")
    public Result<List<EssayCategoryResponse>> getCategories() {
        List<EssayCategory> categories = essayCategoryRepository.findAll();
        return Result.success(categories.stream()
                .map(c -> EssayCategoryResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .slug(c.getSlug())
                        .sort(c.getSort())
                        .createdAt(c.getCreatedAt())
                        .build())
                .toList());
    }

    private EssayResponse toResponse(Essay essay) {
        EssayResponse.EssayResponseBuilder builder = EssayResponse.builder()
                .id(essay.getId())
                .title(essay.getTitle())
                .slug(essay.getSlug())
                .author(essay.getAuthor())
                .summary(essay.getSummary())
                .content(essay.getContent())
                .status(essay.getStatus())
                .viewCount(essay.getViewCount())
                .createdAt(essay.getCreatedAt())
                .updatedAt(essay.getUpdatedAt());

        if (essay.getCategory() != null) {
            builder.category(EssayResponse.CategoryVO.builder()
                    .id(essay.getCategory().getId())
                    .name(essay.getCategory().getName())
                    .slug(essay.getCategory().getSlug())
                    .build());
        }

        return builder.build();
    }

    private PageResult<EssayResponse> toResponsePage(PageResult<Essay> result) {
        List<EssayResponse> list = result.getList().stream()
                .map(this::toResponse)
                .toList();
        return PageResult.of(list, result.getTotal());
    }
}
