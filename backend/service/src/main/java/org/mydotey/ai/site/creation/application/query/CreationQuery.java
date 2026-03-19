package org.mydotey.ai.site.creation.application.query;

import lombok.Data;

/**
 * 创作查询对象（诗歌、散文通用）
 *
 * @author AI-Site
 */
@Data
public class CreationQuery {

    /**
     * 页码
     */
    private int page = 1;

    /**
     * 每页大小
     */
    private int size = 10;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 状态
     */
    private String status;

    /**
     * 关键词
     */
    private String keyword;
}
