package org.mydotey.ai.site.auth.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mydotey.ai.site.auth.domain.entity.UserRole;

/**
 * 用户角色关联 Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
}