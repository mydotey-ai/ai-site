package org.mydotey.ai.site.media.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.media.domain.entity.Album;
import org.mydotey.ai.site.media.domain.repository.AlbumRepository;
import org.mydotey.ai.site.media.domain.repository.ImageRepository;
import org.mydotey.ai.site.media.interfaces.dto.AlbumResponse;
import org.mydotey.ai.site.media.interfaces.dto.ImageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 相册查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumQueryService {

    private final AlbumRepository albumRepository;
    private final ImageRepository imageRepository;

    /**
     * 分页查询相册列表
     */
    public PageResult<AlbumResponse> findPage(int page, int size, Integer isPublic) {
        List<Album> albums = albumRepository.findPage(page, size, isPublic);
        long total = albumRepository.count(isPublic);

        List<AlbumResponse> list = albums.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PageResult<>(list, total);
    }

    /**
     * 查询所有公开相册
     */
    public List<AlbumResponse> findAllPublic() {
        return albumRepository.findAllPublic().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID查询相册
     */
    public AlbumResponse findById(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new org.mydotey.ai.site.common.exception.BusinessException(
                        org.mydotey.ai.site.common.exception.ErrorCode.ALBUM_NOT_FOUND));
        return toResponse(album);
    }

    /**
     * 根据Slug查询相册（含图片）
     */
    public AlbumResponse findBySlug(String slug, int page, int size) {
        Album album = albumRepository.findBySlug(slug)
                .orElseThrow(() -> new org.mydotey.ai.site.common.exception.BusinessException(
                        org.mydotey.ai.site.common.exception.ErrorCode.ALBUM_NOT_FOUND));

        AlbumResponse response = toResponse(album);

        // 获取相册中的图片
        List<org.mydotey.ai.site.media.domain.entity.Image> images = imageRepository.findByAlbumId(album.getId(), page, size);
        List<ImageResponse> imageResponses = images.stream()
                .map(img -> ImageResponse.builder()
                        .id(img.getId())
                        .title(img.getTitle())
                        .url(img.getUrl())
                        .thumbnailUrl(img.getThumbnailUrl())
                        .width(img.getWidth())
                        .height(img.getHeight())
                        .createdAt(img.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        response.setImages(imageResponses);
        return response;
    }

    /**
     * 转换为响应对象
     */
    private AlbumResponse toResponse(Album album) {
        return AlbumResponse.builder()
                .id(album.getId())
                .name(album.getName())
                .slug(album.getSlug())
                .description(album.getDescription())
                .coverImage(album.getCoverImage())
                .imageCount(album.getImageCount())
                .isPublic(album.getIsPublic())
                .sort(album.getSort())
                .createdAt(album.getCreatedAt())
                .updatedAt(album.getUpdatedAt())
                .build();
    }
}
