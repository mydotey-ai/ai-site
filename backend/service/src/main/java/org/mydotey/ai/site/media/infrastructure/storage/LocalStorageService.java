package org.mydotey.ai.site.media.infrastructure.storage;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mydotey.ai.site.media.infrastructure.config.StorageProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 本地存储服务实现
 *
 * @author AI-Site
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.type", havingValue = "local", matchIfMissing = true)
public class LocalStorageService implements StorageService {

    private final StorageProperties storageProperties;

    @Value("${server.port:8080}")
    private String serverPort;

    @Override
    public StorageResult upload(MultipartFile file, String path) {
        try {
            String basePath = storageProperties.getLocal().getBasePath();
            Path fullPath = Paths.get(basePath, path);

            // 创建目录
            Files.createDirectories(fullPath.getParent());

            // 保存文件
            file.transferTo(fullPath.toFile());

            log.info("文件上传成功: {}", fullPath);

            return StorageResult.builder()
                    .path(path)
                    .url(getUrl(path))
                    .size(file.getSize())
                    .contentType(file.getContentType())
                    .build();
        } catch (IOException e) {
            log.error("文件上传失败: {}", path, e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String path) {
        String basePath = storageProperties.getLocal().getBasePath();
        Path fullPath = Paths.get(basePath, path);
        try {
            Files.deleteIfExists(fullPath);
            log.info("文件删除成功: {}", fullPath);
        } catch (IOException e) {
            log.error("文件删除失败: {}", path, e);
            throw new RuntimeException("文件删除失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String getUrl(String path) {
        return storageProperties.getLocal().getUrlPrefix() + "/" + path;
    }

    @Override
    public boolean exists(String path) {
        String basePath = storageProperties.getLocal().getBasePath();
        Path fullPath = Paths.get(basePath, path);
        return Files.exists(fullPath);
    }

    /**
     * 生成存储路径
     *
     * @param type     文件类型 (images/videos/audios)
     * @param filename 原始文件名
     * @return 存储路径
     */
    public String generatePath(String type, String filename) {
        String extension = FileUtil.extName(filename);
        String uuid = IdUtil.fastSimpleUUID();
        LocalDate now = LocalDate.now();
        String yearMonth = now.format(DateTimeFormatter.ofPattern("yyyy/MM"));

        return String.format("%s/%s/%s.%s", type, yearMonth, uuid, extension);
    }

    /**
     * 生成缩略图路径
     *
     * @param originalPath 原图路径
     * @return 缩略图路径
     */
    public String generateThumbnailPath(String originalPath) {
        int dotIndex = originalPath.lastIndexOf('.');
        if (dotIndex > 0) {
            return originalPath.substring(0, dotIndex) + "_thumb" + originalPath.substring(dotIndex);
        }
        return originalPath + "_thumb";
    }
}
