package org.mydotey.ai.site.media.application.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.mydotey.ai.site.media.domain.entity.Video;
import org.mydotey.ai.site.media.domain.repository.VideoRepository;
import org.mydotey.ai.site.media.infrastructure.service.FileValidationService;
import org.mydotey.ai.site.media.infrastructure.storage.LocalStorageService;
import org.mydotey.ai.site.media.infrastructure.storage.StorageResult;
import org.mydotey.ai.site.media.interfaces.dto.VideoRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 视频命令服务
 *
 * @author AI-Site
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VideoCommandService {

    private final VideoRepository videoRepository;
    private final LocalStorageService storageService;
    private final FileValidationService fileValidationService;

    /**
     * 创建视频（外链）
     */
    public Long create(VideoRequest request) {
        Video video = new Video();
        video.setTitle(request.getTitle());
        video.setDescription(request.getDescription());
        video.setCoverImage(request.getCoverImage());
        video.setType(request.getType());
        video.setPlatform(request.getPlatform());
        video.setVideoId(request.getVideoId());
        video.setCategory(request.getCategory());
        video.setTags(request.getTags());
        video.setIsPublic(request.getIsPublic());
        video.setViewCount(0);

        video = videoRepository.save(video);
        log.info("视频创建成功: id={}, title={}", video.getId(), video.getTitle());
        return video.getId();
    }

    /**
     * 上传视频（本地）
     */
    public Long upload(MultipartFile file, VideoRequest request) throws Exception {
        // 校验文件
        fileValidationService.validateVideo(file);

        // 生成存储路径
        String path = storageService.generatePath("videos", file.getOriginalFilename());

        // 上传文件
        StorageResult result = storageService.upload(file, path);

        Video video = new Video();
        video.setTitle(request.getTitle() != null ? request.getTitle() : file.getOriginalFilename());
        video.setDescription(request.getDescription());
        video.setCoverImage(request.getCoverImage());
        video.setType("LOCAL");
        video.setPlatform("LOCAL");
        video.setUrl(result.getUrl());
        video.setFileName(extractFileName(path));
        video.setSize(file.getSize());
        video.setCategory(request.getCategory());
        video.setTags(request.getTags());
        video.setIsPublic(request.getIsPublic());
        video.setViewCount(0);

        video = videoRepository.save(video);
        log.info("视频上传成功: id={}, url={}", video.getId(), video.getUrl());
        return video.getId();
    }

    /**
     * 更新视频
     */
    public void update(Long id, VideoRequest request) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.VIDEO_NOT_FOUND));

        video.setTitle(request.getTitle());
        video.setDescription(request.getDescription());
        video.setCoverImage(request.getCoverImage());
        video.setPlatform(request.getPlatform());
        video.setVideoId(request.getVideoId());
        video.setCategory(request.getCategory());
        video.setTags(request.getTags());
        video.setIsPublic(request.getIsPublic());

        videoRepository.update(video);
        log.info("视频更新成功: id={}", id);
    }

    /**
     * 删除视频
     */
    public void delete(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.VIDEO_NOT_FOUND));

        // 删除本地文件
        if ("LOCAL".equals(video.getType()) && video.getFileName() != null) {
            String path = "videos/" + extractPathFromUrl(video.getUrl());
            storageService.delete(path);
        }

        videoRepository.deleteById(id);
        log.info("视频删除成功: id={}", id);
    }

    /**
     * 增加浏览量
     */
    public void incrementViewCount(Long id) {
        videoRepository.incrementViewCount(id);
    }

    /**
     * 从路径提取文件名
     */
    private String extractFileName(String path) {
        int slashIndex = path.lastIndexOf('/');
        return slashIndex >= 0 ? path.substring(slashIndex + 1) : path;
    }

    /**
     * 从URL提取路径
     */
    private String extractPathFromUrl(String url) {
        if (url == null) return "";
        String prefix = "/uploads/";
        if (url.startsWith(prefix)) {
            return url.substring(prefix.length());
        }
        return url;
    }
}
