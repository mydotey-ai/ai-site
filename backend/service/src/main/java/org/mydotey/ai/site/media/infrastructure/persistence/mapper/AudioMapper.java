package org.mydotey.ai.site.media.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.media.domain.entity.Audio;

import java.util.List;

/**
 * 音频 Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface AudioMapper extends BaseMapper<Audio> {

    @Select("""
            <script>
            SELECT * FROM audio WHERE deleted = 0
            <if test="type != null and type != ''"> AND type = #{type}</if>
            <if test="platform != null and platform != ''"> AND platform = #{platform}</if>
            <if test="category != null and category != ''"> AND category = #{category}</if>
            <if test="isPublic != null"> AND is_public = #{isPublic}</if>
            ORDER BY created_at DESC
            LIMIT #{offset}, #{size}
            </script>
            """)
    List<Audio> findPage(@Param("offset") int offset, @Param("size") int size,
                         @Param("type") String type, @Param("platform") String platform,
                         @Param("category") String category, @Param("isPublic") Integer isPublic);

    @Select("""
            <script>
            SELECT COUNT(*) FROM audio WHERE deleted = 0
            <if test="type != null and type != ''"> AND type = #{type}</if>
            <if test="platform != null and platform != ''"> AND platform = #{platform}</if>
            <if test="category != null and category != ''"> AND category = #{category}</if>
            <if test="isPublic != null"> AND is_public = #{isPublic}</if>
            </script>
            """)
    long count(@Param("type") String type, @Param("platform") String platform,
               @Param("category") String category, @Param("isPublic") Integer isPublic);

    @Select("SELECT * FROM audio WHERE is_public = 1 AND deleted = 0 ORDER BY created_at DESC")
    List<Audio> findAllPublic();

    @Update("UPDATE audio SET view_count = view_count + 1 WHERE id = #{id} AND deleted = 0")
    void incrementViewCount(@Param("id") Long id);
}
