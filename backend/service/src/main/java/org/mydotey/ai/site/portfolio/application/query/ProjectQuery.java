package org.mydotey.ai.site.portfolio.application.query;

import lombok.Data;

/**
 * 项目查询参数
 *
 * @author AI-Site
 */
@Data
public class ProjectQuery {

    /**
     * 页码
     */
    private int page = 1;

    /**
     * 每页数量
     */
    private int size = 12;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 状态
     */
    private String status;

    /**
     * 关键词
     */
    private String keyword;
}
