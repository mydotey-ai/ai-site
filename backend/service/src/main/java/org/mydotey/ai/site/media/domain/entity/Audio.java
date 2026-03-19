package org.mydotey.ai.site.media.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.BaseEntity;

import java.util.List;

/**
 * 音频实体
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "audio", autoResultMap = true)
public class Audio extends BaseEntity {

    /**
     * 音频ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 封面图URL
     */
    private String coverImage;

    /**
     * 类型: LOCAL / EXTERNAL
     */
    private String type;

    /**
     * 平台: NETEASE / LOCAL
     */
    private String platform;

    /**
     * 外链音频ID
     */
    private String audioId;

    /**
     * 音频URL（本地上传）
     */
    private String url;

    /**
     * 存储文件名（本地上传）
     */
    private String fileName;

    /**
     * 时长（秒）
     */
    private Integer duration;

    /**
     * 文件大小（字节）
     */
    private Long size;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> tags;

    /**
     * 是否公开
     */
    private Integer isPublic;

    /**
     * 播放量
     */
    private Integer viewCount;

    /**
     * 是否外链音频
     */
    public boolean isExternal() {
        return "EXTERNAL".equals(this.type);
    }

    /**
     * 是否公开
     */
    public boolean isPublic() {
        return Integer.valueOf(1).equals(this.isPublic);
    }
}
