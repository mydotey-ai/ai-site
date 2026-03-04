package org.mydotey.ai.site.common.entity;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 分页查询参数
 *
 * @author AI-Site
 */
@Data
public class PageQuery {

    /**
     * 页码（从1开始）
     */
    @Min(value = 1, message = "页码必须大于0")
    private int page = 1;

    /**
     * 每页数量
     */
    @Min(value = 1, message = "每页数量必须大于0")
    private int size = 20;

    /**
     * 排序字段
     */
    private String sort;

    /**
     * 获取偏移量
     */
    public int getOffset() {
        return (page - 1) * size;
    }
}