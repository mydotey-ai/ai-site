package org.mydotey.ai.site.blog.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.application.command.CommentCommandService;
import org.mydotey.ai.site.blog.application.query.CommentQueryService;
import org.mydotey.ai.site.blog.domain.entity.Comment;
import org.mydotey.ai.site.blog.interfaces.dto.CommentRequest;
import org.mydotey.ai.site.blog.interfaces.dto.CommentResponse;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论控制器（公开接口）
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/api/v1/comments")
@Tag(name = "评论", description = "评论公开接口")
@RequiredArgsConstructor
public class CommentController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @GetMapping("/article/{articleId}")
    @Operation(summary = "查询文章评论")
    public Result<List<CommentResponse>> getByArticle(@PathVariable Long articleId) {
        List<Comment> comments = commentQueryService.findByArticleId(articleId);
        return Result.success(comments.stream().map(this::toResponse).toList());
    }

    @PostMapping
    @Operation(summary = "提交评论")
    public Result<Long> submit(@Valid @RequestBody CommentRequest request) {
        Comment comment = toEntity(request);
        Long id = commentCommandService.submitComment(comment);
        return Result.success(id);
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "点赞评论")
    public Result<Void> like(@PathVariable Long id) {
        commentCommandService.likeComment(id);
        return Result.success();
    }

    /**
     * 转换为实体
     */
    private Comment toEntity(CommentRequest request) {
        Comment comment = new Comment();
        comment.setArticleId(request.getArticleId());
        comment.setParentId(request.getParentId());
        comment.setNickname(request.getNickname());
        comment.setEmail(request.getEmail());
        comment.setWebsite(request.getWebsite());
        comment.setContent(request.getContent());
        return comment;
    }

    /**
     * 转换为响应DTO
     */
    private CommentResponse toResponse(Comment comment) {
        CommentResponse.CommentResponseBuilder builder = CommentResponse.builder()
                .id(comment.getId())
                .articleId(comment.getArticleId())
                .parentId(comment.getParentId())
                .nickname(comment.getNickname())
                .website(comment.getWebsite())
                .content(comment.getContent())
                .status(comment.getStatus())
                .likeCount(comment.getLikeCount())
                .createdAt(comment.getCreatedAt());

        if (comment.getChildren() != null) {
            builder.children(comment.getChildren().stream()
                    .map(this::toResponse)
                    .toList());
        }

        return builder.build();
    }
}
