package org.mydotey.ai.site.common.module.interfaces;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 分页结果
 *
 * @param <T> 数据类型
 * @author AI-Site
 */
@Data
@AllArgsConstructor
public class PageResult<T> {

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 总数
     */
    private long total;

    /**
     * 创建分页结果
     */
    public static <T> PageResult<T> of(List<T> list, long total) {
        return new PageResult<>(list, total);
    }
}