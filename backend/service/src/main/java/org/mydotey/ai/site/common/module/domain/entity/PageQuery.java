package org.mydotey.ai.site.common.module.domain.entity;

import lombok.Data;

/**
 * 分页查询基类
 *
 * @author AI-Site
 */
@Data
public class PageQuery {

    /**
     * 当前页码
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer size = 10;

    /**
     * 获取偏移量
     */
    public Integer getOffset() {
        return (page - 1) * size;
    }
}