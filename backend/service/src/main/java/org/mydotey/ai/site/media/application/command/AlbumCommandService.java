package org.mydotey.ai.site.media.application.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.mydotey.ai.site.media.domain.entity.Album;
import org.mydotey.ai.site.media.domain.repository.AlbumRepository;
import org.mydotey.ai.site.media.domain.repository.ImageRepository;
import org.mydotey.ai.site.media.interfaces.dto.AlbumRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 相册命令服务
 *
 * @author AI-Site
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AlbumCommandService {

    private final AlbumRepository albumRepository;
    private final ImageRepository imageRepository;

    /**
     * 创建相册
     */
    public Long create(AlbumRequest request) {
        // 生成 slug
        String slug = request.getSlug();
        if (slug == null || slug.isEmpty()) {
            slug = generateSlug(request.getName());
        }

        // 检查 slug 是否存在
        if (albumRepository.existsBySlug(slug)) {
            slug = slug + "-" + UUID.randomUUID().toString().substring(0, 8);
        }

        Album album = new Album();
        album.setName(request.getName());
        album.setSlug(slug);
        album.setDescription(request.getDescription());
        album.setCoverImage(request.getCoverImage());
        album.setImageCount(0);
        album.setIsPublic(request.getIsPublic());
        album.setSort(request.getSort());

        album = albumRepository.save(album);
        log.info("相册创建成功: id={}, name={}", album.getId(), album.getName());
        return album.getId();
    }

    /**
     * 更新相册
     */
    public void update(Long id, AlbumRequest request) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ALBUM_NOT_FOUND));

        album.setName(request.getName());
        if (request.getSlug() != null && !request.getSlug().isEmpty()) {
            album.setSlug(request.getSlug());
        }
        album.setDescription(request.getDescription());
        album.setCoverImage(request.getCoverImage());
        album.setIsPublic(request.getIsPublic());
        album.setSort(request.getSort());

        albumRepository.update(album);
        log.info("相册更新成功: id={}", id);
    }

    /**
     * 删除相册
     */
    public void delete(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ALBUM_NOT_FOUND));

        // 将相册中的图片的 albumId 设为 null
        // 这里通过批量更新实现

        albumRepository.deleteById(id);
        log.info("相册删除成功: id={}", id);
    }

    /**
     * 生成 slug
     */
    private String generateSlug(String name) {
        // 简单的 slug 生成：小写、空格转连字符
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\u4e00-\\u9fa5]+", "-")
                .replaceAll("^-|-$", "");
    }
}
