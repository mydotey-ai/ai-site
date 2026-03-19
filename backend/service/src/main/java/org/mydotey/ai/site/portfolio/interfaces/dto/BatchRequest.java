package org.mydotey.ai.site.portfolio.interfaces.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量操作请求
 *
 * @author AI-Site
 */
@Data
public class BatchRequest {

    /**
     * 操作类型: delete / archive / release
     */
    @NotEmpty(message = "操作类型不能为空")
    private String action;

    /**
     * 项目ID列表
     */
    @NotEmpty(message = "项目ID列表不能为空")
    private List<Long> ids;
}
