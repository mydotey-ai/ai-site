package org.mydotey.ai.site.media.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 存储配置属性
 *
 * @author AI-Site
 */
@Data
@Component
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    /**
     * 存储类型: local / oss
     */
    private String type = "local";

    /**
     * 本地存储配置
     */
    private LocalStorage local = new LocalStorage();

    /**
     * OSS 存储配置
     */
    private OssStorage oss = new OssStorage();

    @Data
    public static class LocalStorage {
        /**
         * 存储根路径
         */
        private String basePath = "./uploads";

        /**
         * URL 前缀
         */
        private String urlPrefix = "/uploads";
    }

    @Data
    public static class OssStorage {
        /**
         * OSS 端点
         */
        private String endpoint;

        /**
         * Access Key
         */
        private String accessKey;

        /**
         * Secret Key
         */
        private String secretKey;

        /**
         * Bucket 名称
         */
        private String bucketName;

        /**
         * URL 前缀
         */
        private String urlPrefix;
    }
}
