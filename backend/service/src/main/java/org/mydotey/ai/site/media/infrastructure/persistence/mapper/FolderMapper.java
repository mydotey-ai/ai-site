package org.mydotey.ai.site.media.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.media.domain.entity.Folder;

import java.util.List;

/**
 * 文件夹 Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface FolderMapper extends BaseMapper<Folder> {

    @Select("SELECT * FROM folder WHERE type = #{type} AND deleted = 0 ORDER BY sort ASC, created_at ASC")
    List<Folder> findByType(@Param("type") String type);

    @Select("SELECT * FROM folder WHERE parent_id = #{parentId} AND deleted = 0 ORDER BY sort ASC, created_at ASC")
    List<Folder> findByParentId(@Param("parentId") Long parentId);

    @Select("SELECT * FROM folder WHERE deleted = 0 ORDER BY type ASC, sort ASC, created_at ASC")
    List<Folder> findAll();

    @Select("SELECT COUNT(*) > 0 FROM folder WHERE parent_id = #{id} AND deleted = 0")
    boolean hasChildren(@Param("id") Long id);
}
