package org.mydotey.ai.site.creation.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.mydotey.ai.site.creation.application.query.ChapterQueryService;
import org.mydotey.ai.site.creation.application.query.NovelQuery;
import org.mydotey.ai.site.creation.application.query.NovelQueryService;
import org.mydotey.ai.site.creation.domain.entity.Chapter;
import org.mydotey.ai.site.creation.domain.entity.Novel;
import org.mydotey.ai.site.creation.domain.entity.NovelCategory;
import org.mydotey.ai.site.creation.domain.repository.NovelCategoryRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.ChapterMapper;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.NovelMapper;
import org.mydotey.ai.site.creation.interfaces.dto.ChapterListItem;
import org.mydotey.ai.site.creation.interfaces.dto.ChapterResponse;
import org.mydotey.ai.site.creation.interfaces.dto.NovelResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小说控制器（公开接口）
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/v1/novels")
@Tag(name = "小说", description = "小说公开接口")
@RequiredArgsConstructor
public class NovelController {

    private final NovelQueryService novelQueryService;
    private final ChapterQueryService chapterQueryService;
    private final NovelCategoryRepository novelCategoryRepository;
    private final NovelMapper novelMapper;
    private final ChapterMapper chapterMapper;

    @GetMapping
    @Operation(summary = "分页查询小说")
    public Result<PageResult<NovelResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId) {

        NovelQuery query = new NovelQuery();
        query.setPage(page);
        query.setSize(Math.min(size, 50));
        query.setCategoryId(categoryId);

        PageResult<Novel> result = novelQueryService.findPublished(query);
        return Result.success(toResponsePage(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询小说")
    public Result<NovelResponse> getById(@PathVariable Long id) {
        Novel novel = novelQueryService.findById(id);
        novelMapper.incrementViewCount(id);
        return Result.success(toResponse(novel));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "根据Slug查询小说")
    public Result<NovelResponse> getBySlug(@PathVariable String slug) {
        Novel novel = novelQueryService.findBySlug(slug);
        novelMapper.incrementViewCount(novel.getId());
        return Result.success(toResponse(novel));
    }

    @GetMapping("/{novelId}/chapters")
    @Operation(summary = "获取小说章节列表")
    public Result<List<ChapterListItem>> getChapters(@PathVariable Long novelId) {
        List<Chapter> chapters = novelQueryService.getChapters(novelId);
        return Result.success(chapters.stream().map(this::toChapterListItem).toList());
    }

    @GetMapping("/chapters/{chapterId}")
    @Operation(summary = "获取章节内容")
    public Result<ChapterResponse> getChapter(@PathVariable Long chapterId) {
        Chapter chapter = chapterQueryService.findById(chapterId);

        // 增加浏览量
        chapterMapper.incrementViewCount(chapterId);
        novelMapper.incrementViewCount(chapter.getNovelId());

        // 获取导航信息
        Long prevId = chapterQueryService.getPrevChapterId(chapter.getNovelId(), chapter.getChapterNo());
        Long nextId = chapterQueryService.getNextChapterId(chapter.getNovelId(), chapter.getChapterNo());

        // 获取小说标题
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
                .prevChapterId(prevId)
                .nextChapterId(nextId)
                .createdAt(chapter.getCreatedAt())
                .updatedAt(chapter.getUpdatedAt())
                .build());
    }

    @GetMapping("/categories")
    @Operation(summary = "获取小说分类列表")
    public Result<List<NovelResponse.CategoryVO>> getCategories() {
        List<NovelCategory> categories = novelCategoryRepository.findAll();
        return Result.success(categories.stream()
                .map(c -> NovelResponse.CategoryVO.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .slug(c.getSlug())
                        .build())
                .toList());
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
     * 转换为分页响应
     */
    private PageResult<NovelResponse> toResponsePage(PageResult<Novel> result) {
        List<NovelResponse> list = result.getList().stream()
                .map(this::toResponse)
                .toList();
        return PageResult.of(list, result.getTotal());
    }
}
