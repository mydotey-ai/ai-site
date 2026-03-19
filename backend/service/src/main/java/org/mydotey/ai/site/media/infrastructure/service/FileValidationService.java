package org.mydotey.ai.site.media.infrastructure.service;

import lombok.extern.slf4j.Slf4j;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.mydotey.ai.site.media.infrastructure.config.UploadProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件校验服务
 *
 * @author AI-Site
 */
@Slf4j
@Service
public class FileValidationService {

    private final UploadProperties uploadProperties;

    /**
     * Magic Number 映射表
     */
    private static final Map<String, List<byte[]>> MAGIC_NUMBERS = new HashMap<>();

    static {
        // JPEG: FF D8 FF
        MAGIC_NUMBERS.put("image/jpeg", List.of(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF}));
        // PNG: 89 50 4E 47
        MAGIC_NUMBERS.put("image/png", List.of(new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47}));
        // GIF: 47 49 46 38
        MAGIC_NUMBERS.put("image/gif", List.of(new byte[]{0x47, 0x49, 0x46, 0x38}));
        // WebP: 52 49 46 46 ... 57 45 42 50
        MAGIC_NUMBERS.put("image/webp", List.of(new byte[]{0x52, 0x49, 0x46, 0x46}));
        // MP4: 00 00 00 xx 66 74 79 70 (ftyp)
        MAGIC_NUMBERS.put("video/mp4", List.of(new byte[]{0x00, 0x00, 0x00}));
        // WebM: 1A 45 DF A3
        MAGIC_NUMBERS.put("video/webm", List.of(new byte[]{0x1A, 0x45, (byte) 0xDF, (byte) 0xA3}));
        // MP3: FF FB or ID3
        MAGIC_NUMBERS.put("audio/mpeg", List.of(
                new byte[]{(byte) 0xFF, (byte) 0xFB},
                new byte[]{0x49, 0x44, 0x33}  // ID3
        ));
        // WAV: 52 49 46 46 ... 57 41 56 45
        MAGIC_NUMBERS.put("audio/wav", List.of(new byte[]{0x52, 0x49, 0x46, 0x46}));
    }

    public FileValidationService(UploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    /**
     * 校验图片文件
     */
    public void validateImage(MultipartFile file) {
        UploadProperties.ImageConfig config = uploadProperties.getImage();
        validate(file, config.getMaxSize(), config.getAllowedTypes());
    }

    /**
     * 校验视频文件
     */
    public void validateVideo(MultipartFile file) {
        UploadProperties.VideoConfig config = uploadProperties.getVideo();
        validate(file, config.getMaxSize(), config.getAllowedTypes());
    }

    /**
     * 校验音频文件
     */
    public void validateAudio(MultipartFile file) {
        UploadProperties.AudioConfig config = uploadProperties.getAudio();
        validate(file, config.getMaxSize(), config.getAllowedTypes());
    }

    /**
     * 校验文件
     *
     * @param file         文件
     * @param maxSize      最大大小（字节）
     * @param allowedTypes 允许的 MIME 类型（逗号分隔）
     */
    public void validate(MultipartFile file, long maxSize, String allowedTypes) {
        // 1. 检查文件是否为空
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.UPLOAD_FAILED, "文件不能为空");
        }

        // 2. 检查文件大小
        if (file.getSize() > maxSize) {
            throw new BusinessException(ErrorCode.UPLOAD_FAILED,
                    "文件大小超出限制，最大允许 " + formatSize(maxSize));
        }

        // 3. 检查 MIME 类型
        String contentType = file.getContentType();
        List<String> allowedTypeList = Arrays.asList(allowedTypes.split(","));
        if (contentType == null || !allowedTypeList.contains(contentType)) {
            throw new BusinessException(ErrorCode.UPLOAD_FAILED,
                    "不支持的文件类型: " + contentType);
        }

        // 4. Magic Number 校验
        try {
            if (!validateMagicNumber(file, contentType)) {
                throw new BusinessException(ErrorCode.UPLOAD_FAILED, "文件内容与类型不匹配");
            }
        } catch (IOException e) {
            log.error("Magic Number 校验失败", e);
            throw new BusinessException(ErrorCode.UPLOAD_FAILED, "文件校验失败");
        }

        log.debug("文件校验通过: {}, size: {}, type: {}",
                file.getOriginalFilename(), file.getSize(), contentType);
    }

    /**
     * Magic Number 校验
     */
    private boolean validateMagicNumber(MultipartFile file, String contentType) throws IOException {
        List<byte[]> expectedMagicNumbers = MAGIC_NUMBERS.get(contentType);
        if (expectedMagicNumbers == null) {
            // 如果没有定义 Magic Number，跳过校验
            return true;
        }

        try (InputStream is = file.getInputStream()) {
            int maxMagicLength = expectedMagicNumbers.stream()
                    .mapToInt(bytes -> bytes.length)
                    .max()
                    .orElse(0);
            byte[] header = new byte[maxMagicLength];
            int bytesRead = is.read(header);

            if (bytesRead < maxMagicLength) {
                return false;
            }

            for (byte[] expected : expectedMagicNumbers) {
                if (startsWith(header, expected)) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * 检查字节数组是否以指定前缀开头
     */
    private boolean startsWith(byte[] array, byte[] prefix) {
        if (array.length < prefix.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (array[i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 格式化文件大小
     */
    private String formatSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
        }
    }
}
