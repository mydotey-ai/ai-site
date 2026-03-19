package org.mydotey.ai.site.auth.interfaces.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mydotey.ai.site.common.module.domain.entity.PageQuery;

/**
 * 用户查询参数
 *
 * @author AI-Site
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQuery extends PageQuery {

    /**
     * 关键词（用户名/邮箱/昵称）
     */
    private String keyword;

    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status;
}
