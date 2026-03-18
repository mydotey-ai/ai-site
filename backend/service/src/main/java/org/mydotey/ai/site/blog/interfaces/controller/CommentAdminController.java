package org.mydotey.ai.site.blog.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.application.command.CommentCommandService;
import org.mydotey.ai.site.blog.application.query.CommentQuery;
import org.mydotey.ai.site.blog.application.query.CommentQueryService;
import org.mydotey.ai.site.blog.domain.entity.Comment;
import org.mydotey.ai.site.blog.interfaces.dto.CommentRequest;
import org.mydotey.ai.site.blog.interfaces.dto.CommentResponse;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.common.module.interfaces.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论管理控制器
 *
 * @author AI-Site
 */
@RestController
@RequestMapping("/admin/v1/comments")
@Tag(name = "评论管理", description = "评论管理接口")
@RequiredArgsConstructor
public class CommentAdminController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @GetMapping
    @Operation(summary = "分页查询评论")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<CommentResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long articleId) {

        CommentQuery query = new CommentQuery();
        query.setPage(page);
        query.setSize(size);
        query.setStatus(status);
        query.setArticleId(articleId);

        PageResult<Comment> result = commentQueryService.findPage(query);
        return Result.success(toResponsePage(result));
    }

    @GetMapping("/pending")
    @Operation(summary = "查询待审核评论")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<CommentResponse>> pending(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Comment> comments = commentQueryService.findPending(page, size);
        return Result.success(comments.stream().map(this::toResponse).toList());
    }

    @GetMapping("/pending/count")
    @Operation(summary = "统计待审核评论数")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Long> pendingCount() {
        return Result.success(commentQueryService.countPending());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询评论")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<CommentResponse> getById(@PathVariable Long id) {
        Comment comment = commentQueryService.findById(id);
        return Result.success(toResponse(comment));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "审核通过评论")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> approve(@PathVariable Long id) {
        commentCommandService.approveComment(id);
        return Result.success();
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "拒绝评论")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> reject(@PathVariable Long id) {
        commentCommandService.rejectComment(id);
        return Result.success();
    }

    @PostMapping("/batch-approve")
    @Operation(summary = "批量审核通过")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> batchApprove(@RequestBody List<Long> ids) {
        commentCommandService.batchApprove(ids);
        return Result.success();
    }

    @PostMapping("/batch-reject")
    @Operation(summary = "批量拒绝")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> batchReject(@RequestBody List<Long> ids) {
        commentCommandService.batchReject(ids);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除评论")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@PathVariable Long id) {
        commentCommandService.deleteComment(id);
        return Result.success();
    }

    /**
     * 转换为响应DTO
     */
    private CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .articleId(comment.getArticleId())
                .parentId(comment.getParentId())
                .nickname(comment.getNickname())
                .email(comment.getEmail())
                .website(comment.getWebsite())
                .content(comment.getContent())
                .status(comment.getStatus())
                .likeCount(comment.getLikeCount())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    /**
     * 转换为分页响应
     */
    private PageResult<CommentResponse> toResponsePage(PageResult<Comment> result) {
        List<CommentResponse> list = result.getList().stream()
                .map(this::toResponse)
                .toList();
        return PageResult.of(list, result.getTotal());
    }
}
