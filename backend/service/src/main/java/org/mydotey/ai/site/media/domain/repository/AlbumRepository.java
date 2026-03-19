package org.mydotey.ai.site.media.domain.repository;

import org.mydotey.ai.site.media.domain.entity.Album;

import java.util.List;
import java.util.Optional;

/**
 * 相册仓储接口
 *
 * @author AI-Site
 */
public interface AlbumRepository {

    /**
     * 保存相册
     */
    Album save(Album album);

    /**
     * 更新相册
     */
    void update(Album album);

    /**
     * 根据ID删除相册
     */
    void deleteById(Long id);

    /**
     * 根据ID查找相册
     */
    Optional<Album> findById(Long id);

    /**
     * 根据Slug查找相册
     */
    Optional<Album> findBySlug(String slug);

    /**
     * 查找所有相册（分页）
     */
    List<Album> findPage(Integer page, Integer size, Integer isPublic);

    /**
     * 统计相册数量
     */
    long count(Integer isPublic);

    /**
     * 查找所有公开相册
     */
    List<Album> findAllPublic();

    /**
     * 更新相册图片数量
     */
    void updateImageCount(Long albumId, int count);

    /**
     * 检查Slug是否存在
     */
    boolean existsBySlug(String slug);
}
