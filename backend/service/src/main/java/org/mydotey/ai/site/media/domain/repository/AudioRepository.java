package org.mydotey.ai.site.media.domain.repository;

import org.mydotey.ai.site.media.domain.entity.Audio;

import java.util.List;
import java.util.Optional;

/**
 * 音频仓储接口
 *
 * @author AI-Site
 */
public interface AudioRepository {

    /**
     * 保存音频
     */
    Audio save(Audio audio);

    /**
     * 更新音频
     */
    void update(Audio audio);

    /**
     * 根据ID删除音频
     */
    void deleteById(Long id);

    /**
     * 根据ID查找音频
     */
    Optional<Audio> findById(Long id);

    /**
     * 查找所有音频（分页）
     */
    List<Audio> findPage(Integer page, Integer size, String type, String platform, String category, Integer isPublic);

    /**
     * 统计音频数量
     */
    long count(String type, String platform, String category, Integer isPublic);

    /**
     * 查找所有公开音频
     */
    List<Audio> findAllPublic();

    /**
     * 增加播放量
     */
    void incrementViewCount(Long id);
}
