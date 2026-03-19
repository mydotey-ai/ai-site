package org.mydotey.ai.site.media.application.command;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.mydotey.ai.site.media.domain.entity.Image;
import org.mydotey.ai.site.media.infrastructure.config.ImageProcessProperties;
import org.mydotey.ai.site.media.infrastructure.service.FileValidationService;
import org.mydotey.ai.site.media.infrastructure.service.ImageProcessService;
import org.mydotey.ai.site.media.infrastructure.storage.InMemoryMultipartFile;
import org.mydotey.ai.site.media.infrastructure.storage.LocalStorageService;
import org.mydotey.ai.site.media.infrastructure.storage.StorageResult;
import org.mydotey.ai.site.media.interfaces.dto.BatchRequest;
import org.mydotey.ai.site.media.interfaces.dto.ImageRequest;
import org.mydotey.ai.site.media.interfaces.dto.ImageUploadResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 图片命令服务
 *
 * @author AI-Site
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ImageCommandService {

    private final org.mydotey.ai.site.media.domain.repository.ImageRepository imageRepository;
    private final org.mydotey.ai.site.media.domain.repository.AlbumRepository albumRepository;
    private final LocalStorageService storageService;
    private final FileValidationService fileValidationService;
    private final ImageProcessService imageProcessService;
    private final ImageProcessProperties imageProcessProperties;

    /**
     * 上传图片
     */
    public ImageUploadResponse upload(MultipartFile file, ImageRequest request) throws IOException {
        // 校验文件
        fileValidationService.validateImage(file);

        // 处理图片
        ImageProcessService.ProcessedImage processedImage = imageProcessService.process(
                file.getInputStream(), getFileExtension(file.getOriginalFilename()));

        // 生成存储路径
        String path = storageService.generatePath("images", file.getOriginalFilename());
        String thumbnailPath = storageService.generateThumbnailPath(path);

        // 保存原图
        StorageResult result = saveProcessedImage(processedImage.image(), path, file.getContentType());

        // 保存缩略图
        String thumbnailUrl = null;
        if (processedImage.thumbnail() != null && imageProcessProperties.getThumbnail().isEnabled()) {
            StorageResult thumbResult = saveProcessedImage(processedImage.thumbnail(), thumbnailPath, file.getContentType());
            thumbnailUrl = thumbResult.getUrl();
        }

        // 创建图片实体
        Image image = new Image();
        image.setTitle(request.getTitle() != null ? request.getTitle() : file.getOriginalFilename());
        image.setDescription(request.getDescription());
        image.setOriginalName(file.getOriginalFilename());
        image.setFileName(extractFileName(path));
        image.setUrl(result.getUrl());
        image.setThumbnailUrl(thumbnailUrl);
        image.setWidth(processedImage.width());
        image.setHeight(processedImage.height());
        image.setSize(file.getSize());
        image.setMimeType(file.getContentType());
        image.setAlbumId(request.getAlbumId());
        image.setFolderId(request.getFolderId());
        image.setTags(request.getTags());
        image.setIsPublic(request.getIsPublic());
        image.setViewCount(0);

        // 保存到数据库
        image = imageRepository.save(image);

        // 更新相册图片数量
        if (image.getAlbumId() != null) {
            updateAlbumImageCount(image.getAlbumId());
        }

        log.info("图片上传成功: id={}, url={}", image.getId(), image.getUrl());

        return ImageUploadResponse.builder()
                .id(image.getId())
                .title(image.getTitle())
                .url(image.getUrl())
                .thumbnailUrl(image.getThumbnailUrl())
                .width(image.getWidth())
                .height(image.getHeight())
                .size(image.getSize())
                .createdAt(image.getCreatedAt())
                .build();
    }

    /**
     * 更新图片信息
     */
    public void update(Long id, ImageRequest request) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.IMAGE_NOT_FOUND));

        Long oldAlbumId = image.getAlbumId();

        image.setTitle(request.getTitle());
        image.setDescription(request.getDescription());
        image.setAlbumId(request.getAlbumId());
        image.setFolderId(request.getFolderId());
        image.setTags(request.getTags());
        image.setIsPublic(request.getIsPublic());

        imageRepository.update(image);

        // 更新相册图片数量
        if (!java.util.Objects.equals(oldAlbumId, request.getAlbumId())) {
            if (oldAlbumId != null) {
                updateAlbumImageCount(oldAlbumId);
            }
            if (request.getAlbumId() != null) {
                updateAlbumImageCount(request.getAlbumId());
            }
        }
    }

    /**
     * 删除图片
     */
    public void delete(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.IMAGE_NOT_FOUND));

        // 删除文件
        if (image.getFileName() != null) {
            String path = "images/" + extractPathFromUrl(image.getUrl());
            storageService.delete(path);
            if (image.getThumbnailUrl() != null) {
                String thumbPath = "images/" + extractPathFromUrl(image.getThumbnailUrl());
                storageService.delete(thumbPath);
            }
        }

        // 删除数据库记录
        imageRepository.deleteById(id);

        // 更新相册图片数量
        if (image.getAlbumId() != null) {
            updateAlbumImageCount(image.getAlbumId());
        }

        log.info("图片删除成功: id={}", id);
    }

    /**
     * 批量操作
     */
    public void batchOperation(BatchRequest request) {
        switch (request.getAction().toLowerCase()) {
            case "move":
                if (request.getTargetAlbumId() != null) {
                    imageRepository.batchUpdateAlbum(request.getIds(), request.getTargetAlbumId());
                    updateAlbumImageCount(request.getTargetAlbumId());
                }
                if (request.getTargetFolderId() != null) {
                    imageRepository.batchUpdateFolder(request.getIds(), request.getTargetFolderId());
                }
                break;
            case "delete":
                for (Long id : request.getIds()) {
                    try {
                        delete(id);
                    } catch (Exception e) {
                        log.error("删除图片失败: id={}", id, e);
                    }
                }
                break;
            case "setpublic":
                imageRepository.batchUpdatePublic(request.getIds(), 1);
                break;
            case "setprivate":
                imageRepository.batchUpdatePublic(request.getIds(), 0);
                break;
            default:
                throw new BusinessException(ErrorCode.BAD_REQUEST, "不支持的操作类型: " + request.getAction());
        }
    }

    /**
     * 增加浏览量
     */
    public void incrementViewCount(Long id) {
        imageRepository.incrementViewCount(id);
    }

    /**
     * 保存处理后的图片
     */
    private StorageResult saveProcessedImage(BufferedImage image, String path, String contentType) throws IOException {
        String format = getFileExtension(path);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, format, os);

        byte[] bytes = os.toByteArray();

        // 创建临时 MultipartFile
        MultipartFile tempFile = new InMemoryMultipartFile(
                "file", path, contentType, bytes);

        return storageService.upload(tempFile, path);
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null) return "jpg";
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex > 0 ? filename.substring(dotIndex + 1).toLowerCase() : "jpg";
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

    /**
     * 更新相册图片数量
     */
    private void updateAlbumImageCount(Long albumId) {
        long count = imageRepository.countByAlbumId(albumId);
        albumRepository.updateImageCount(albumId, (int) count);
    }
}
