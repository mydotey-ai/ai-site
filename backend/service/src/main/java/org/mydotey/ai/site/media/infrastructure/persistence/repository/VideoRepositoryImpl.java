package org.mydotey.ai.site.media.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.media.domain.entity.Video;
import org.mydotey.ai.site.media.domain.repository.VideoRepository;
import org.mydotey.ai.site.media.infrastructure.persistence.mapper.VideoMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 视频仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class VideoRepositoryImpl implements VideoRepository {

    private final VideoMapper videoMapper;

    @Override
    public Video save(Video video) {
        videoMapper.insert(video);
        return video;
    }

    @Override
    public void update(Video video) {
        videoMapper.updateById(video);
    }

    @Override
    public void deleteById(Long id) {
        videoMapper.deleteById(id);
    }

    @Override
    public Optional<Video> findById(Long id) {
        return Optional.ofNullable(videoMapper.selectById(id));
    }

    @Override
    public List<Video> findPage(Integer page, Integer size, String type, String platform, String category, Integer isPublic) {
        int offset = (page - 1) * size;
        return videoMapper.findPage(offset, size, type, platform, category, isPublic);
    }

    @Override
    public long count(String type, String platform, String category, Integer isPublic) {
        return videoMapper.count(type, platform, category, isPublic);
    }

    @Override
    public List<Video> findAllPublic() {
        return videoMapper.findAllPublic();
    }

    @Override
    public void incrementViewCount(Long id) {
        videoMapper.incrementViewCount(id);
    }
}
