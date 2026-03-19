package org.mydotey.ai.site.blog.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.application.command.ArticleCommandService;
import org.mydotey.ai.site.blog.application.query.ArticleQuery;
import org.mydotey.ai.site.blog.application.query.ArticleQueryService;
import org.mydotey.ai.site.blog.domain.entity.Article;
import org.mydotey.ai.site.blog.interfaces.dto.ArticleRequest;
import org.mydotey.ai.site.blog.interfaces.dto.ArticleResponse;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章管理控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/admin/v1/articles")
@Tag(name = "文章管理", description = "文章管理接口")
@RequiredArgsConstructor
public class ArticleAdminController {

    private final ArticleCommandService articleCommandService;
    private final ArticleQueryService articleQueryService;

    @GetMapping
    @Operation(summary = "分页查询文章")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<ArticleResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {

        ArticleQuery query = new ArticleQuery();
        query.setPage(page);
        query.setSize(size);
        query.setStatus(status);
        query.setCategoryId(categoryId);
        query.setKeyword(keyword);

        PageResult<Article> result = articleQueryService.findPage(query);
        return Result.success(toResponsePage(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询文章")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<ArticleResponse> getById(@PathVariable Long id) {
        Article article = articleQueryService.findById(id);
        return Result.success(toResponse(article));
    }

    @PostMapping
    @Operation(summary = "创建文章")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Long> create(@Valid @RequestBody ArticleRequest request) {
        Article article = toEntity(request);
        Long id = articleCommandService.createArticle(article, request.getTagIds());
        return Result.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文章")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ArticleRequest request) {
        Article article = toEntity(request);
        article.setId(id);
        articleCommandService.updateArticle(article, request.getTagIds());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文章")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@PathVariable Long id) {
        articleCommandService.deleteArticle(id);
        return Result.success();
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "发布文章")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> publish(@PathVariable Long id) {
        articleCommandService.publishArticle(id);
        return Result.success();
    }

    @PostMapping("/{id}/unpublish")
    @Operation(summary = "取消发布文章")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> unpublish(@PathVariable Long id) {
        articleCommandService.unpublishArticle(id);
        return Result.success();
    }

    @PostMapping("/{id}/top")
    @Operation(summary = "置顶文章")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> top(@PathVariable Long id) {
        articleCommandService.topArticle(id);
        return Result.success();
    }

    @PostMapping("/{id}/untop")
    @Operation(summary = "取消置顶")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> untop(@PathVariable Long id) {
        articleCommandService.untopArticle(id);
        return Result.success();
    }

    /**
     * 转换为实体
     */
    private Article toEntity(ArticleRequest request) {
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setSlug(request.getSlug());
        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setContentType(request.getContentType());
        article.setCoverImage(request.getCoverImage());
        article.setCategoryId(request.getCategoryId());
        article.setStatus(request.getStatus());
        article.setIsTop(request.getIsTop());
        article.setAllowComment(request.getAllowComment());
        article.setSeoTitle(request.getSeoTitle());
        article.setSeoDescription(request.getSeoDescription());
        article.setSeoKeywords(request.getSeoKeywords());
        return article;
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
