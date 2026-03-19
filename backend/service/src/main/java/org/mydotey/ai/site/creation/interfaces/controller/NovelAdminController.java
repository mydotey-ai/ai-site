package org.mydotey.ai.site.creation.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.creation.application.command.ChapterCommandService;
import org.mydotey.ai.site.creation.application.command.NovelCommandService;
import org.mydotey.ai.site.creation.application.query.ChapterQueryService;
import org.mydotey.ai.site.creation.application.query.NovelQuery;
import org.mydotey.ai.site.creation.application.query.NovelQueryService;
import org.mydotey.ai.site.creation.domain.entity.Chapter;
import org.mydotey.ai.site.creation.domain.entity.Novel;
import org.mydotey.ai.site.creation.domain.entity.NovelCategory;
import org.mydotey.ai.site.creation.domain.repository.NovelCategoryRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.ChapterMapper;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.NovelMapper;
import org.mydotey.ai.site.creation.interfaces.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小说管理控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/admin/v1/novels")
@Tag(name = "小说管理", description = "小说管理接口")
@RequiredArgsConstructor
public class NovelAdminController {

    private final NovelCommandService novelCommandService;
    private final NovelQueryService novelQueryService;
    private final ChapterCommandService chapterCommandService;
    private final ChapterQueryService chapterQueryService;
    private final NovelCategoryRepository novelCategoryRepository;
    private final NovelMapper novelMapper;
    private final ChapterMapper chapterMapper;

    @GetMapping
    @Operation(summary = "分页查询小说（管理端）")
    public Result<PageResult<NovelResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {

        NovelQuery query = new NovelQuery();
        query.setPage(page);
        query.setSize(size);
        query.setCategoryId(categoryId);
        query.setStatus(status);
        query.setKeyword(keyword);

        PageResult<Novel> result = novelQueryService.findPage(query);
        return Result.success(toResponsePage(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询小说")
    public Result<NovelResponse> getById(@PathVariable Long id) {
        Novel novel = novelQueryService.findById(id);
        return Result.success(toResponse(novel));
    }

    @PostMapping
    @Operation(summary = "创建小说")
    public Result<Long> create(@Valid @RequestBody NovelRequest request) {
        Novel novel = toEntity(request);
        Long id = novelCommandService.createNovel(novel);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新小说")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody NovelRequest request) {
        Novel novel = toEntity(request);
        novel.setId(id);
        novelCommandService.updateNovel(novel);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除小说")
    public Result<Void> delete(@PathVariable Long id) {
        novelCommandService.deleteNovel(id);
        return Result.success();
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "发布小说")
    public Result<Void> publish(@PathVariable Long id) {
        novelCommandService.publishNovel(id);
        return Result.success();
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "完结小说")
    public Result<Void> complete(@PathVariable Long id) {
        novelCommandService.completeNovel(id);
        return Result.success();
    }

    @PostMapping("/batch")
    @Operation(summary = "批量操作")
    public Result<Void> batch(@Valid @RequestBody BatchRequest request) {
        switch (request.getAction().toLowerCase()) {
            case "delete" -> novelCommandService.batchDelete(request.getIds());
            case "publish" -> novelCommandService.batchUpdateStatus(request.getIds(), "PUBLISHED");
            case "complete" -> novelCommandService.batchUpdateStatus(request.getIds(), "COMPLETED");
            default -> throw new IllegalArgumentException("不支持的操作类型: " + request.getAction());
        }
        return Result.success();
    }

    // ==================== 章节管理 ====================

    @GetMapping("/{novelId}/chapters")
    @Operation(summary = "获取小说所有章节")
    public Result<List<ChapterListItem>> getChapters(@PathVariable Long novelId) {
        List<Chapter> chapters = novelQueryService.getAllChapters(novelId);
        return Result.success(chapters.stream().map(this::toChapterListItem).toList());
    }

    @GetMapping("/chapters/{chapterId}")
    @Operation(summary = "获取章节详情")
    public Result<ChapterResponse> getChapter(@PathVariable Long chapterId) {
        Chapter chapter = chapterQueryService.findById(chapterId);
        Novel novel = novelQueryService.findById(chapter.getNovelId());

        return Result.success(ChapterResponse.builder()
                .id(chapter.getId())
                .novelId(chapter.getNovelId())
                .novelTitle(novel.getTitle())
                .chapterNo(chapter.getChapterNo())
                .title(chapter.getTitle())
                .content(chapter.getContent())
                .wordCount(chapter.getWordCount())
                .status(chapter.getStatus())
                .viewCount(chapter.getViewCount())
                .createdAt(chapter.getCreatedAt())
                .updatedAt(chapter.getUpdatedAt())
                .build());
    }

    @PostMapping("/{novelId}/chapters")
    @Operation(summary = "创建章节")
    public Result<Long> createChapter(@PathVariable Long novelId, @Valid @RequestBody ChapterRequest request) {
        request.setNovelId(novelId);
        Chapter chapter = toChapterEntity(request);
        Long id = chapterCommandService.createChapter(chapter);
        return Result.success(id);
    }

    @PutMapping("/chapters/{chapterId}")
    @Operation(summary = "更新章节")
    public Result<Void> updateChapter(@PathVariable Long chapterId, @Valid @RequestBody ChapterRequest request) {
        Chapter chapter = toChapterEntity(request);
        chapter.setId(chapterId);
        chapterCommandService.updateChapter(chapter);
        return Result.success();
    }

    @DeleteMapping("/chapters/{chapterId}")
    @Operation(summary = "删除章节")
    public Result<Void> deleteChapter(@PathVariable Long chapterId) {
        chapterCommandService.deleteChapter(chapterId);
        return Result.success();
    }

    @PostMapping("/chapters/{chapterId}/publish")
    @Operation(summary = "发布章节")
    public Result<Void> publishChapter(@PathVariable Long chapterId) {
        chapterCommandService.publishChapter(chapterId);
        return Result.success();
    }

    @PostMapping("/chapters/batch")
    @Operation(summary = "批量操作章节")
    public Result<Void> batchChapters(@Valid @RequestBody BatchRequest request) {
        switch (request.getAction().toLowerCase()) {
            case "delete" -> chapterCommandService.batchDelete(request.getIds());
            case "publish" -> chapterCommandService.batchUpdateStatus(request.getIds(), "PUBLISHED");
            default -> throw new IllegalArgumentException("不支持的操作类型: " + request.getAction());
        }
        return Result.success();
    }

    // ==================== 分类管理 ====================

    @GetMapping("/categories")
    @Operation(summary = "获取所有分类")
    public Result<List<NovelCategoryResponse>> getCategories() {
        List<NovelCategory> categories = novelCategoryRepository.findAll();
        return Result.success(categories.stream().map(this::toCategoryResponse).toList());
    }

    /**
     * 转换为实体
     */
    private Novel toEntity(NovelRequest request) {
        Novel novel = new Novel();
        novel.setTitle(request.getTitle());
        novel.setSlug(request.getSlug());
        novel.setAuthor(request.getAuthor());
        novel.setSummary(request.getSummary());
        novel.setCoverImage(request.getCoverImage());
        novel.setCategoryId(request.getCategoryId());
        novel.setStatus(request.getStatus());
        return novel;
    }

    /**
     * 转换为章节实体
     */
    private Chapter toChapterEntity(ChapterRequest request) {
        Chapter chapter = new Chapter();
        chapter.setNovelId(request.getNovelId());
        chapter.setTitle(request.getTitle());
        chapter.setContent(request.getContent());
        chapter.setChapterNo(request.getChapterNo());
        chapter.setStatus(request.getStatus());
        return chapter;
    }

    /**
     * 转换为响应DTO
     */
    private NovelResponse toResponse(Novel novel) {
        NovelResponse.NovelResponseBuilder builder = NovelResponse.builder()
                .id(novel.getId())
                .title(novel.getTitle())
                .slug(novel.getSlug())
                .author(novel.getAuthor())
                .summary(novel.getSummary())
                .coverImage(novel.getCoverImage())
                .status(novel.getStatus())
                .wordCount(novel.getWordCount())
                .chapterCount(novel.getChapterCount())
                .viewCount(novel.getViewCount())
                .createdAt(novel.getCreatedAt())
                .updatedAt(novel.getUpdatedAt());

        if (novel.getCategory() != null) {
            builder.category(NovelResponse.CategoryVO.builder()
                    .id(novel.getCategory().getId())
                    .name(novel.getCategory().getName())
                    .slug(novel.getCategory().getSlug())
                    .build());
        }

        return builder.build();
    }

    /**
     * 转换为章节列表项
     */
    private ChapterListItem toChapterListItem(Chapter chapter) {
        return ChapterListItem.builder()
                .id(chapter.getId())
                .chapterNo(chapter.getChapterNo())
                .title(chapter.getTitle())
                .wordCount(chapter.getWordCount())
                .status(chapter.getStatus())
                .viewCount(chapter.getViewCount())
                .createdAt(chapter.getCreatedAt())
                .build();
    }

    /**
     * 转换为分类响应
     */
    private NovelCategoryResponse toCategoryResponse(NovelCategory category) {
        return NovelCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .sort(category.getSort())
                .createdAt(category.getCreatedAt())
                .build();
    }

    /**
     * 转换为分页响应
     */
    private PageResult<NovelResponse> toResponsePage(PageResult<Novel> result) {
        List<NovelResponse> list = result.getList().stream()
                .map(this::toResponse)
                .toList();
        return PageResult.of(list, result.getTotal());
    }
}
