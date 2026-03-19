package org.mydotey.ai.site.creation.interfaces.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量操作请求DTO
 *
 * @author AI-Site
 */
@Data
public class BatchRequest {

    /**
     * 操作类型
     */
    @NotEmpty(message = "操作类型不能为空")
    private String action;

    /**
     * ID列表
     */
    @NotEmpty(message = "ID列表不能为空")
    private List<Long> ids;
}
