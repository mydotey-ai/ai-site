package org.mydotey.ai.site.media.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.media.domain.entity.Video;
import org.mydotey.ai.site.media.domain.repository.VideoRepository;
import org.mydotey.ai.site.media.interfaces.dto.VideoResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 视频查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoQueryService {

    private final VideoRepository videoRepository;

    /**
     * 分页查询视频列表
     */
    public PageResult<VideoResponse> findPage(int page, int size, String type, String platform, String category, Integer isPublic) {
        List<Video> videos = videoRepository.findPage(page, size, type, platform, category, isPublic);
        long total = videoRepository.count(type, platform, category, isPublic);

        List<VideoResponse> list = videos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PageResult<>(list, total);
    }

    /**
     * 查询所有公开视频
     */
    public List<VideoResponse> findAllPublic() {
        return videoRepository.findAllPublic().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID查询视频
     */
    public VideoResponse findById(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new org.mydotey.ai.site.common.exception.BusinessException(
                        org.mydotey.ai.site.common.exception.ErrorCode.VIDEO_NOT_FOUND));
        return toResponse(video);
    }

    /**
     * 转换为响应对象
     */
    private VideoResponse toResponse(Video video) {
        return VideoResponse.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .coverImage(video.getCoverImage())
                .type(video.getType())
                .platform(video.getPlatform())
                .videoId(video.getVideoId())
                .url(video.getUrl())
                .duration(video.getDuration())
                .size(video.getSize())
                .category(video.getCategory())
                .tags(video.getTags())
                .isPublic(video.getIsPublic())
                .viewCount(video.getViewCount())
                .createdAt(video.getCreatedAt())
                .embedUrl(getEmbedUrl(video.getPlatform(), video.getVideoId()))
                .build();
    }

    /**
     * 获取嵌入URL
     */
    private String getEmbedUrl(String platform, String videoId) {
        if (platform == null || videoId == null) {
            return null;
        }
        switch (platform.toUpperCase()) {
            case "BILIBILI":
                return "https://player.bilibili.com/player.html?bvid=" + videoId;
            case "YOUTUBE":
                return "https://www.youtube.com/embed/" + videoId;
            default:
                return null;
        }
    }
}
