package org.mydotey.ai.site.media.application.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.mydotey.ai.site.media.domain.entity.Audio;
import org.mydotey.ai.site.media.domain.repository.AudioRepository;
import org.mydotey.ai.site.media.infrastructure.service.FileValidationService;
import org.mydotey.ai.site.media.infrastructure.storage.LocalStorageService;
import org.mydotey.ai.site.media.infrastructure.storage.StorageResult;
import org.mydotey.ai.site.media.interfaces.dto.AudioRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 音频命令服务
 *
 * @author AI-Site
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AudioCommandService {

    private final AudioRepository audioRepository;
    private final LocalStorageService storageService;
    private final FileValidationService fileValidationService;

    /**
     * 创建音频（外链）
     */
    public Long create(AudioRequest request) {
        Audio audio = new Audio();
        audio.setTitle(request.getTitle());
        audio.setDescription(request.getDescription());
        audio.setCoverImage(request.getCoverImage());
        audio.setType(request.getType());
        audio.setPlatform(request.getPlatform());
        audio.setAudioId(request.getAudioId());
        audio.setCategory(request.getCategory());
        audio.setTags(request.getTags());
        audio.setIsPublic(request.getIsPublic());
        audio.setViewCount(0);

        audio = audioRepository.save(audio);
        log.info("音频创建成功: id={}, title={}", audio.getId(), audio.getTitle());
        return audio.getId();
    }

    /**
     * 上传音频（本地）
     */
    public Long upload(MultipartFile file, AudioRequest request) throws Exception {
        // 校验文件
        fileValidationService.validateAudio(file);

        // 生成存储路径
        String path = storageService.generatePath("audios", file.getOriginalFilename());

        // 上传文件
        StorageResult result = storageService.upload(file, path);

        Audio audio = new Audio();
        audio.setTitle(request.getTitle() != null ? request.getTitle() : file.getOriginalFilename());
        audio.setDescription(request.getDescription());
        audio.setCoverImage(request.getCoverImage());
        audio.setType("LOCAL");
        audio.setPlatform("LOCAL");
        audio.setUrl(result.getUrl());
        audio.setFileName(extractFileName(path));
        audio.setSize(file.getSize());
        audio.setCategory(request.getCategory());
        audio.setTags(request.getTags());
        audio.setIsPublic(request.getIsPublic());
        audio.setViewCount(0);

        audio = audioRepository.save(audio);
        log.info("音频上传成功: id={}, url={}", audio.getId(), audio.getUrl());
        return audio.getId();
    }

    /**
     * 更新音频
     */
    public void update(Long id, AudioRequest request) {
        Audio audio = audioRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.AUDIO_NOT_FOUND));

        audio.setTitle(request.getTitle());
        audio.setDescription(request.getDescription());
        audio.setCoverImage(request.getCoverImage());
        audio.setPlatform(request.getPlatform());
        audio.setAudioId(request.getAudioId());
        audio.setCategory(request.getCategory());
        audio.setTags(request.getTags());
        audio.setIsPublic(request.getIsPublic());

        audioRepository.update(audio);
        log.info("音频更新成功: id={}", id);
    }

    /**
     * 删除音频
     */
    public void delete(Long id) {
        Audio audio = audioRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.AUDIO_NOT_FOUND));

        // 删除本地文件
        if ("LOCAL".equals(audio.getType()) && audio.getFileName() != null) {
            String path = "audios/" + extractPathFromUrl(audio.getUrl());
            storageService.delete(path);
        }

        audioRepository.deleteById(id);
        log.info("音频删除成功: id={}", id);
    }

    /**
     * 增加播放量
     */
    public void incrementViewCount(Long id) {
        audioRepository.incrementViewCount(id);
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
