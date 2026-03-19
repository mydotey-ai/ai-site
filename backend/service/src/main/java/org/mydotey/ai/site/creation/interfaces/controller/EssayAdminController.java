package org.mydotey.ai.site.creation.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.creation.application.command.EssayCommandService;
import org.mydotey.ai.site.creation.application.query.CreationQuery;
import org.mydotey.ai.site.creation.application.query.EssayQueryService;
import org.mydotey.ai.site.creation.domain.entity.Essay;
import org.mydotey.ai.site.creation.domain.entity.EssayCategory;
import org.mydotey.ai.site.creation.domain.repository.EssayCategoryRepository;
import org.mydotey.ai.site.creation.interfaces.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 散文管理控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/admin/v1/essays")
@Tag(name = "散文管理", description = "散文管理接口")
@RequiredArgsConstructor
public class EssayAdminController {

    private final EssayCommandService essayCommandService;
    private final EssayQueryService essayQueryService;
    private final EssayCategoryRepository essayCategoryRepository;

    @GetMapping
    @Operation(summary = "分页查询散文（管理端）")
    public Result<PageResult<EssayResponse>> list(
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

        PageResult<Essay> result = essayQueryService.findPage(query);
        return Result.success(toResponsePage(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询散文")
    public Result<EssayResponse> getById(@PathVariable Long id) {
        Essay essay = essayQueryService.findById(id);
        return Result.success(toResponse(essay));
    }

    @PostMapping
    @Operation(summary = "创建散文")
    public Result<Long> create(@Valid @RequestBody EssayRequest request) {
        Essay essay = toEntity(request);
        Long id = essayCommandService.createEssay(essay);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新散文")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody EssayRequest request) {
        Essay essay = toEntity(request);
        essay.setId(id);
        essayCommandService.updateEssay(essay);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除散文")
    public Result<Void> delete(@PathVariable Long id) {
        essayCommandService.deleteEssay(id);
        return Result.success();
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "发布散文")
    public Result<Void> publish(@PathVariable Long id) {
        essayCommandService.publishEssay(id);
        return Result.success();
    }

    @PostMapping("/batch")
    @Operation(summary = "批量操作")
    public Result<Void> batch(@Valid @RequestBody BatchRequest request) {
        switch (request.getAction().toLowerCase()) {
            case "delete" -> essayCommandService.batchDelete(request.getIds());
            case "publish" -> essayCommandService.batchUpdateStatus(request.getIds(), "PUBLISHED");
            default -> throw new IllegalArgumentException("不支持的操作类型: " + request.getAction());
        }
        return Result.success();
    }

    @GetMapping("/categories")
    @Operation(summary = "获取所有分类")
    public Result<List<EssayCategoryResponse>> getCategories() {
        List<EssayCategory> categories = essayCategoryRepository.findAll();
        return Result.success(categories.stream().map(this::toCategoryResponse).toList());
    }

    private Essay toEntity(EssayRequest request) {
        Essay essay = new Essay();
        essay.setTitle(request.getTitle());
        essay.setSlug(request.getSlug());
        essay.setAuthor(request.getAuthor());
        essay.setSummary(request.getSummary());
        essay.setContent(request.getContent());
        essay.setCategoryId(request.getCategoryId());
        essay.setStatus(request.getStatus());
        return essay;
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

    private EssayCategoryResponse toCategoryResponse(EssayCategory category) {
        return EssayCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .sort(category.getSort())
                .createdAt(category.getCreatedAt())
                .build();
    }

    private PageResult<EssayResponse> toResponsePage(PageResult<Essay> result) {
        List<EssayResponse> list = result.getList().stream()
                .map(this::toResponse)
                .toList();
        return PageResult.of(list, result.getTotal());
    }
}
