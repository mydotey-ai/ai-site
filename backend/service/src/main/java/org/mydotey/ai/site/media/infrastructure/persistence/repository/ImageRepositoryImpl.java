package org.mydotey.ai.site.media.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.media.domain.entity.Image;
import org.mydotey.ai.site.media.domain.repository.ImageRepository;
import org.mydotey.ai.site.media.infrastructure.persistence.mapper.ImageMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 图片仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {

    private final ImageMapper imageMapper;

    @Override
    public Image save(Image image) {
        imageMapper.insert(image);
        return image;
    }

    @Override
    public void update(Image image) {
        imageMapper.updateById(image);
    }

    @Override
    public void deleteById(Long id) {
        imageMapper.deleteById(id);
    }

    @Override
    public Optional<Image> findById(Long id) {
        return Optional.ofNullable(imageMapper.selectById(id));
    }

    @Override
    public List<Image> findPage(Integer page, Integer size, Long albumId, Long folderId, String keyword, Integer isPublic) {
        int offset = (page - 1) * size;
        return imageMapper.findPage(offset, size, albumId, folderId, keyword, isPublic);
    }

    @Override
    public long count(Long albumId, Long folderId, String keyword, Integer isPublic) {
        return imageMapper.count(albumId, folderId, keyword, isPublic);
    }

    @Override
    public List<Image> findByAlbumId(Long albumId, Integer page, Integer size) {
        int offset = (page - 1) * size;
        return imageMapper.findByAlbumId(albumId, offset, size);
    }

    @Override
    public long countByAlbumId(Long albumId) {
        return imageMapper.countByAlbumId(albumId);
    }

    @Override
    public void batchUpdateAlbum(List<Long> ids, Long albumId) {
        if (ids != null && !ids.isEmpty()) {
            imageMapper.batchUpdateAlbum(ids, albumId);
        }
    }

    @Override
    public void batchUpdateFolder(List<Long> ids, Long folderId) {
        if (ids != null && !ids.isEmpty()) {
            imageMapper.batchUpdateFolder(ids, folderId);
        }
    }

    @Override
    public void batchUpdatePublic(List<Long> ids, Integer isPublic) {
        if (ids != null && !ids.isEmpty()) {
            imageMapper.batchUpdatePublic(ids, isPublic);
        }
    }

    @Override
    public void batchDelete(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            imageMapper.batchDelete(ids);
        }
    }

    @Override
    public void incrementViewCount(Long id) {
        imageMapper.incrementViewCount(id);
    }
}
