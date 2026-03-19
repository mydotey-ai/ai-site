package org.mydotey.ai.site.media.domain.repository;

import org.mydotey.ai.site.media.domain.entity.Image;

import java.util.List;
import java.util.Optional;

/**
 * 图片仓储接口
 *
 * @author AI-Site
 */
public interface ImageRepository {

    /**
     * 保存图片
     */
    Image save(Image image);

    /**
     * 更新图片
     */
    void update(Image image);

    /**
     * 根据ID删除图片
     */
    void deleteById(Long id);

    /**
     * 根据ID查找图片
     */
    Optional<Image> findById(Long id);

    /**
     * 查找所有图片（分页）
     */
    List<Image> findPage(Integer page, Integer size, Long albumId, Long folderId, String keyword, Integer isPublic);

    /**
     * 统计图片数量
     */
    long count(Long albumId, Long folderId, String keyword, Integer isPublic);

    /**
     * 根据相册ID查找图片
     */
    List<Image> findByAlbumId(Long albumId, Integer page, Integer size);

    /**
     * 统计相册图片数量
     */
    long countByAlbumId(Long albumId);

    /**
     * 批量更新图片相册
     */
    void batchUpdateAlbum(List<Long> ids, Long albumId);

    /**
     * 批量更新图片文件夹
     */
    void batchUpdateFolder(List<Long> ids, Long folderId);

    /**
     * 批量更新公开状态
     */
    void batchUpdatePublic(List<Long> ids, Integer isPublic);

    /**
     * 批量删除图片
     */
    void batchDelete(List<Long> ids);

    /**
     * 增加浏览量
     */
    void incrementViewCount(Long id);
}
