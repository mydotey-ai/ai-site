package org.mydotey.ai.site.media.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * 存储服务接口
 *
 * @author AI-Site
 */
public interface StorageService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 存储路径（相对路径，如 images/2026/03/xxx.jpg）
     * @return 存储结果
     */
    StorageResult upload(MultipartFile file, String path);

    /**
     * 删除文件
     *
     * @param path 文件相对路径
     */
    void delete(String path);

    /**
     * 获取文件访问 URL
     *
     * @param path 文件相对路径
     * @return 访问 URL
     */
    String getUrl(String path);

    /**
     * 检查文件是否存在
     *
     * @param path 文件相对路径
     * @return 是否存在
     */
    boolean exists(String path);
}
