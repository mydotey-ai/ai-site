package org.mydotey.ai.site.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mydotey.ai.site.auth.entity.Role;

import java.util.List;

/**
 * 角色 Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色代码查找角色
     */
    @Select("SELECT * FROM role WHERE code = #{code} AND deleted = 0")
    Role findByCode(@Param("code") String code);

    /**
     * 根据用户ID查找角色列表
     */
    @Select("SELECT r.* FROM role r " +
            "INNER JOIN user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.deleted = 0")
    List<Role> findByUserId(@Param("userId") Long userId);
}