package org.mydotey.ai.site.media.domain.repository;

import org.mydotey.ai.site.media.domain.entity.Video;

import java.util.List;
import java.util.Optional;

/**
 * 视频仓储接口
 *
 * @author AI-Site
 */
public interface VideoRepository {

    /**
     * 保存视频
     */
    Video save(Video video);

    /**
     * 更新视频
     */
    void update(Video video);

    /**
     * 根据ID删除视频
     */
    void deleteById(Long id);

    /**
     * 根据ID查找视频
     */
    Optional<Video> findById(Long id);

    /**
     * 查找所有视频（分页）
     */
    List<Video> findPage(Integer page, Integer size, String type, String platform, String category, Integer isPublic);

    /**
     * 统计视频数量
     */
    long count(String type, String platform, String category, Integer isPublic);

    /**
     * 查找所有公开视频
     */
    List<Video> findAllPublic();

    /**
     * 增加浏览量
     */
    void incrementViewCount(Long id);
}
