package org.mydotey.ai.site.media.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.media.domain.entity.Audio;
import org.mydotey.ai.site.media.domain.repository.AudioRepository;
import org.mydotey.ai.site.media.interfaces.dto.AudioResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 音频查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AudioQueryService {

    private final AudioRepository audioRepository;

    /**
     * 分页查询音频列表
     */
    public PageResult<AudioResponse> findPage(int page, int size, String type, String platform, String category, Integer isPublic) {
        List<Audio> audios = audioRepository.findPage(page, size, type, platform, category, isPublic);
        long total = audioRepository.count(type, platform, category, isPublic);

        List<AudioResponse> list = audios.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PageResult<>(list, total);
    }

    /**
     * 查询所有公开音频
     */
    public List<AudioResponse> findAllPublic() {
        return audioRepository.findAllPublic().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID查询音频
     */
    public AudioResponse findById(Long id) {
        Audio audio = audioRepository.findById(id)
                .orElseThrow(() -> new org.mydotey.ai.site.common.exception.BusinessException(
                        org.mydotey.ai.site.common.exception.ErrorCode.AUDIO_NOT_FOUND));
        return toResponse(audio);
    }

    /**
     * 转换为响应对象
     */
    private AudioResponse toResponse(Audio audio) {
        return AudioResponse.builder()
                .id(audio.getId())
                .title(audio.getTitle())
                .description(audio.getDescription())
                .coverImage(audio.getCoverImage())
                .type(audio.getType())
                .platform(audio.getPlatform())
                .audioId(audio.getAudioId())
                .url(audio.getUrl())
                .duration(audio.getDuration())
                .size(audio.getSize())
                .category(audio.getCategory())
                .tags(audio.getTags())
                .isPublic(audio.getIsPublic())
                .viewCount(audio.getViewCount())
                .createdAt(audio.getCreatedAt())
                .build();
    }
}
