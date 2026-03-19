package org.mydotey.ai.site.media.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.media.domain.entity.Album;
import org.mydotey.ai.site.media.domain.entity.Image;
import org.mydotey.ai.site.media.domain.repository.AlbumRepository;
import org.mydotey.ai.site.media.domain.repository.ImageRepository;
import org.mydotey.ai.site.media.interfaces.dto.ImageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 图片查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageQueryService {

    private final ImageRepository imageRepository;
    private final AlbumRepository albumRepository;

    /**
     * 分页查询图片列表
     */
    public PageResult<ImageResponse> findPage(int page, int size, Long albumId, Long folderId, String keyword, Integer isPublic) {
        List<Image> images = imageRepository.findPage(page, size, albumId, folderId, keyword, isPublic);
        long total = imageRepository.count(albumId, folderId, keyword, isPublic);

        // 填充相册信息
        fillAlbumInfo(images);

        List<ImageResponse> list = images.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PageResult<>(list, total);
    }

    /**
     * 根据ID查询图片
     */
    public ImageResponse findById(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new org.mydotey.ai.site.common.exception.BusinessException(
                        org.mydotey.ai.site.common.exception.ErrorCode.IMAGE_NOT_FOUND));

        // 填充相册信息
        if (image.getAlbumId() != null) {
            albumRepository.findById(image.getAlbumId()).ifPresent(album -> image.setAlbum(album));
        }

        return toResponse(image);
    }

    /**
     * 查询相册中的图片
     */
    public PageResult<ImageResponse> findByAlbum(Long albumId, int page, int size) {
        List<Image> images = imageRepository.findByAlbumId(albumId, page, size);
        long total = imageRepository.countByAlbumId(albumId);

        List<ImageResponse> list = images.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PageResult<>(list, total);
    }

    /**
     * 填充相册信息
     */
    private void fillAlbumInfo(List<Image> images) {
        List<Long> albumIds = images.stream()
                .map(Image::getAlbumId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        if (albumIds.isEmpty()) {
            return;
        }

        Map<Long, Album> albumMap = albumIds.stream()
                .map(albumRepository::findById)
                .filter(opt -> opt.isPresent())
                .map(opt -> opt.get())
                .collect(Collectors.toMap(Album::getId, a -> a));

        images.forEach(image -> {
            if (image.getAlbumId() != null) {
                image.setAlbum(albumMap.get(image.getAlbumId()));
            }
        });
    }

    /**
     * 转换为响应对象
     */
    private ImageResponse toResponse(Image image) {
        ImageResponse.ImageResponseBuilder builder = ImageResponse.builder()
                .id(image.getId())
                .title(image.getTitle())
                .description(image.getDescription())
                .originalName(image.getOriginalName())
                .url(image.getUrl())
                .thumbnailUrl(image.getThumbnailUrl())
                .width(image.getWidth())
                .height(image.getHeight())
                .size(image.getSize())
                .mimeType(image.getMimeType())
                .albumId(image.getAlbumId())
                .folderId(image.getFolderId())
                .tags(image.getTags())
                .isPublic(image.getIsPublic())
                .viewCount(image.getViewCount())
                .createdAt(image.getCreatedAt());

        if (image.getAlbum() != null) {
            builder.album(ImageResponse.AlbumVO.builder()
                    .id(image.getAlbum().getId())
                    .name(image.getAlbum().getName())
                    .slug(image.getAlbum().getSlug())
                    .build());
        }

        return builder.build();
    }
}
