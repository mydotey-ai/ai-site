package org.mydotey.ai.site.media.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.mydotey.ai.site.media.domain.entity.Image;

import java.util.List;

/**
 * 图片 Mapper
 *
 * @author AI-Site
 */
@Mapper
public interface ImageMapper extends BaseMapper<Image> {

    @Select("""
            <script>
            SELECT * FROM image WHERE deleted = 0
            <if test="albumId != null"> AND album_id = #{albumId}</if>
            <if test="folderId != null"> AND folder_id = #{folderId}</if>
            <if test="keyword != null and keyword != ''">
                AND (title LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))
            </if>
            <if test="isPublic != null"> AND is_public = #{isPublic}</if>
            ORDER BY created_at DESC
            LIMIT #{offset}, #{size}
            </script>
            """)
    List<Image> findPage(@Param("offset") int offset, @Param("size") int size,
                         @Param("albumId") Long albumId, @Param("folderId") Long folderId,
                         @Param("keyword") String keyword, @Param("isPublic") Integer isPublic);

    @Select("""
            <script>
            SELECT COUNT(*) FROM image WHERE deleted = 0
            <if test="albumId != null"> AND album_id = #{albumId}</if>
            <if test="folderId != null"> AND folder_id = #{folderId}</if>
            <if test="keyword != null and keyword != ''">
                AND (title LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))
            </if>
            <if test="isPublic != null"> AND is_public = #{isPublic}</if>
            </script>
            """)
    long count(@Param("albumId") Long albumId, @Param("folderId") Long folderId,
               @Param("keyword") String keyword, @Param("isPublic") Integer isPublic);

    @Select("SELECT * FROM image WHERE album_id = #{albumId} AND deleted = 0 ORDER BY created_at DESC LIMIT #{offset}, #{size}")
    List<Image> findByAlbumId(@Param("albumId") Long albumId, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM image WHERE album_id = #{albumId} AND deleted = 0")
    long countByAlbumId(@Param("albumId") Long albumId);

    @Update("""
            <script>
            UPDATE image SET album_id = #{albumId}, updated_at = NOW()
            WHERE id IN
            <foreach collection="ids" item="id" open="(" separator="," close=")">#{id}</foreach>
            AND deleted = 0
            </script>
            """)
    void batchUpdateAlbum(@Param("ids") List<Long> ids, @Param("albumId") Long albumId);

    @Update("""
            <script>
            UPDATE image SET folder_id = #{folderId}, updated_at = NOW()
            WHERE id IN
            <foreach collection="ids" item="id" open="(" separator="," close=")">#{id}</foreach>
            AND deleted = 0
            </script>
            """)
    void batchUpdateFolder(@Param("ids") List<Long> ids, @Param("folderId") Long folderId);

    @Update("""
            <script>
            UPDATE image SET is_public = #{isPublic}, updated_at = NOW()
            WHERE id IN
            <foreach collection="ids" item="id" open="(" separator="," close=")">#{id}</foreach>
            AND deleted = 0
            </script>
            """)
    void batchUpdatePublic(@Param("ids") List<Long> ids, @Param("isPublic") Integer isPublic);

    @Update("""
            <script>
            UPDATE image SET deleted = 1, updated_at = NOW()
            WHERE id IN
            <foreach collection="ids" item="id" open="(" separator="," close=")">#{id}</foreach>
            AND deleted = 0
            </script>
            """)
    void batchDelete(@Param("ids") List<Long> ids);

    @Update("UPDATE image SET view_count = view_count + 1 WHERE id = #{id} AND deleted = 0")
    void incrementViewCount(@Param("id") Long id);
}
