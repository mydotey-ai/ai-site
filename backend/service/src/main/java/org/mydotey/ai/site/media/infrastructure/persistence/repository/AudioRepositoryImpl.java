package org.mydotey.ai.site.media.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.media.domain.entity.Audio;
import org.mydotey.ai.site.media.domain.repository.AudioRepository;
import org.mydotey.ai.site.media.infrastructure.persistence.mapper.AudioMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 音频仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class AudioRepositoryImpl implements AudioRepository {

    private final AudioMapper audioMapper;

    @Override
    public Audio save(Audio audio) {
        audioMapper.insert(audio);
        return audio;
    }

    @Override
    public void update(Audio audio) {
        audioMapper.updateById(audio);
    }

    @Override
    public void deleteById(Long id) {
        audioMapper.deleteById(id);
    }

    @Override
    public Optional<Audio> findById(Long id) {
        return Optional.ofNullable(audioMapper.selectById(id));
    }

    @Override
    public List<Audio> findPage(Integer page, Integer size, String type, String platform, String category, Integer isPublic) {
        int offset = (page - 1) * size;
        return audioMapper.findPage(offset, size, type, platform, category, isPublic);
    }

    @Override
    public long count(String type, String platform, String category, Integer isPublic) {
        return audioMapper.count(type, platform, category, isPublic);
    }

    @Override
    public List<Audio> findAllPublic() {
        return audioMapper.findAllPublic();
    }

    @Override
    public void incrementViewCount(Long id) {
        audioMapper.incrementViewCount(id);
    }
}
