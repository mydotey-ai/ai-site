package org.mydotey.ai.site.media.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 上传配置属性
 *
 * @author AI-Site
 */
@Data
@Component
@ConfigurationProperties(prefix = "upload")
public class UploadProperties {

    /**
     * 图片上传配置
     */
    private ImageConfig image = new ImageConfig();

    /**
     * 视频上传配置
     */
    private VideoConfig video = new VideoConfig();

    /**
     * 音频上传配置
     */
    private AudioConfig audio = new AudioConfig();

    @Data
    public static class ImageConfig {
        /**
         * 最大文件大小（字节）
         */
        private long maxSize = 5 * 1024 * 1024; // 5MB

        /**
         * 允许的 MIME 类型
         */
        private String allowedTypes = "image/jpeg,image/png,image/gif,image/webp";
    }

    @Data
    public static class VideoConfig {
        /**
         * 最大文件大小（字节）
         */
        private long maxSize = 100 * 1024 * 1024; // 100MB

        /**
         * 允许的 MIME 类型
         */
        private String allowedTypes = "video/mp4,video/webm";
    }

    @Data
    public static class AudioConfig {
        /**
         * 最大文件大小（字节）
         */
        private long maxSize = 20 * 1024 * 1024; // 20MB

        /**
         * 允许的 MIME 类型
         */
        private String allowedTypes = "audio/mpeg,audio/wav,audio/mp3";
    }
}
