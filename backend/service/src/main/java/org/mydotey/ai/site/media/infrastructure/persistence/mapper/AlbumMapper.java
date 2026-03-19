package org.mydotey.ai.site.media.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.media.domain.entity.Album;

import java.util.List;

/**
 * 相册 Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface AlbumMapper extends BaseMapper<Album> {

    @Select("SELECT * FROM album WHERE slug = #{slug} AND deleted = 0")
    Album findBySlug(@Param("slug") String slug);

    @Select("""
            <script>
            SELECT * FROM album WHERE deleted = 0
            <if test="isPublic != null"> AND is_public = #{isPublic}</if>
            ORDER BY sort ASC, created_at DESC
            LIMIT #{offset}, #{size}
            </script>
            """)
    List<Album> findPage(@Param("offset") int offset, @Param("size") int size, @Param("isPublic") Integer isPublic);

    @Select("""
            <script>
            SELECT COUNT(*) FROM album WHERE deleted = 0
            <if test="isPublic != null"> AND is_public = #{isPublic}</if>
            </script>
            """)
    long count(@Param("isPublic") Integer isPublic);

    @Select("SELECT * FROM album WHERE is_public = 1 AND deleted = 0 ORDER BY sort ASC, created_at DESC")
    List<Album> findAllPublic();

    @Update("UPDATE album SET image_count = #{count}, updated_at = NOW() WHERE id = #{albumId}")
    void updateImageCount(@Param("albumId") Long albumId, @Param("count") int count);

    @Select("SELECT COUNT(*) > 0 FROM album WHERE slug = #{slug} AND deleted = 0")
    boolean existsBySlug(@Param("slug") String slug);
}
