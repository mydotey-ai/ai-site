package org.mydotey.ai.site.media.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 图片处理配置属性
 *
 * @author AI-Site
 */
@Data
@Component
@ConfigurationProperties(prefix = "image")
public class ImageProcessProperties {

    /**
     * 缩略图配置
     */
    private ThumbnailConfig thumbnail = new ThumbnailConfig();

    /**
     * 压缩配置
     */
    private CompressConfig compress = new CompressConfig();

    /**
     * 水印配置
     */
    private WatermarkConfig watermark = new WatermarkConfig();

    @Data
    public static class ThumbnailConfig {
        /**
         * 是否启用缩略图
         */
        private boolean enabled = true;

        /**
         * 缩略图宽度
         */
        private int width = 300;
    }

    @Data
    public static class CompressConfig {
        /**
         * 是否启用压缩
         */
        private boolean enabled = true;

        /**
         * 压缩质量 (0.0 - 1.0)
         */
        private float quality = 0.8f;
    }

    @Data
    public static class WatermarkConfig {
        /**
         * 是否启用水印
         */
        private boolean enabled = false;

        /**
         * 水印文字
         */
        private String text = "";

        /**
         * 水印位置: top-left, top-right, bottom-left, bottom-right, center
         */
        private String position = "bottom-right";

        /**
         * 水印透明度 (0.0 - 1.0)
         */
        private float opacity = 0.5f;
    }
}
