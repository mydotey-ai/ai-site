package org.mydotey.ai.site.media.infrastructure.storage;

import lombok.Builder;
import lombok.Data;

/**
 * 存储结果
 *
 * @author AI-Site
 */
@Data
@Builder
public class StorageResult {

    /**
     * 文件相对路径
     */
    private String path;

    /**
     * 文件访问 URL
     */
    private String url;

    /**
     * 文件大小（字节）
     */
    private long size;

    /**
     * 文件 MIME 类型
     */
    private String contentType;
}
