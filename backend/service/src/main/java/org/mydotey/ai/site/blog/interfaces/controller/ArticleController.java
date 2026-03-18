package org.mydotey.ai.site.blog.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.application.query.ArticleQuery;
import org.mydotey.ai.site.blog.application.query.ArticleQueryService;
import org.mydotey.ai.site.blog.domain.entity.Article;
import org.mydotey.ai.site.blog.interfaces.dto.ArticleResponse;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章控制器（公开接口）
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/v1/articles")
@Tag(name = "文章", description = "文章公开接口")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleQueryService articleQueryService;

    @GetMapping
    @Operation(summary = "分页查询文章")
    public Result<PageResult<ArticleResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false) String keyword) {

        ArticleQuery query = new ArticleQuery();
        query.setPage(page);
        query.setSize(size);
        query.setCategoryId(categoryId);
        query.setTagId(tagId);
        query.setKeyword(keyword);

        PageResult<Article> result = articleQueryService.findPublished(page, size);
        return Result.success(toResponsePage(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询文章")
    public Result<ArticleResponse> getById(@PathVariable Long id) {
        Article article = articleQueryService.findById(id);
        return Result.success(toResponse(article));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "根据Slug查询文章")
    public Result<ArticleResponse> getBySlug(@PathVariable String slug) {
        Article article = articleQueryService.findBySlug(slug);
        return Result.success(toResponse(article));
    }

    @GetMapping("/{id}/related")
    @Operation(summary = "获取相关文章")
    public Result<List<ArticleResponse>> getRelated(@PathVariable Long id, @RequestParam(defaultValue = "5") int limit) {
        List<Article> articles = articleQueryService.findRelated(id, limit);
        return Result.success(articles.stream().map(this::toResponse).toList());
    }

    @GetMapping("/search")
    @Operation(summary = "搜索文章")
    public Result<PageResult<ArticleResponse>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResult<Article> result = articleQueryService.search(keyword, page, size);
        return Result.success(toResponsePage(result));
    }

    /**
     * 转换为响应DTO
     */
    private ArticleResponse toResponse(Article article) {
        ArticleResponse.ArticleResponseBuilder builder = ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .slug(article.getSlug())
                .summary(article.getSummary())
                .content(article.getContent())
                .contentType(article.getContentType())
                .coverImage(article.getCoverImage())
                .categoryId(article.getCategoryId())
                .status(article.getStatus())
                .viewCount(article.getViewCount())
                .likeCount(article.getLikeCount())
                .isTop(article.getIsTop())
                .allowComment(article.getAllowComment())
                .seoTitle(article.getSeoTitle())
                .seoDescription(article.getSeoDescription())
                .seoKeywords(article.getSeoKeywords())
                .publishedAt(article.getPublishedAt())
                .authorId(article.getAuthorId())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt());

        if (article.getCategory() != null) {
            builder.category(ArticleResponse.CategoryVO.builder()
                    .id(article.getCategory().getId())
                    .name(article.getCategory().getName())
                    .slug(article.getCategory().getSlug())
                    .build());
        }

        if (article.getTags() != null) {
            builder.tags(article.getTags().stream()
                    .map(tag -> ArticleResponse.TagVO.builder()
                            .id(tag.getId())
                            .name(tag.getName())
                            .slug(tag.getSlug())
                            .color(tag.getColor())
                            .build())
                    .toList());
        }

        return builder.build();
    }

    /**
     * 转换为分页响应
     */
    private PageResult<ArticleResponse> toResponsePage(PageResult<Article> result) {
        List<ArticleResponse> list = result.getList().stream()
                .map(this::toResponse)
                .toList();
        return PageResult.of(list, result.getTotal());
    }
}
