package org.mydotey.ai.site.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.blog.domain.enums.CommentStatus;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;

import java.util.List;

/**
 * 评论实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("comment")
public class Comment extends BaseEntity {

    /**
     * 评论ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱(AES加密存储)
     */
    private String email;

    /**
     * 网站
     */
    private String website;

    /**
     * 评论内容
     */
    private String content;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 状态: PENDING, APPROVED, SPAM
     */
    private String status;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 子评论列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<Comment> children;

    /**
     * 获取评论状态枚举
     */
    public CommentStatus getStatusEnum() {
        return CommentStatus.fromCode(this.status);
    }

    /**
     * 是否已通过审核
     */
    public boolean isApproved() {
        return CommentStatus.APPROVED.getCode().equals(this.status);
    }

    /**
     * 是否为顶级评论
     */
    public boolean isTopLevel() {
        return this.parentId == null || this.parentId == 0;
    }
}
