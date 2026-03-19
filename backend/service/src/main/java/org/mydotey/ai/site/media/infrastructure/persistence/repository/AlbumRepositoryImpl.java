package org.mydotey.ai.site.media.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.media.domain.entity.Album;
import org.mydotey.ai.site.media.domain.repository.AlbumRepository;
import org.mydotey.ai.site.media.infrastructure.persistence.mapper.AlbumMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 相册仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class AlbumRepositoryImpl implements AlbumRepository {

    private final AlbumMapper albumMapper;

    @Override
    public Album save(Album album) {
        albumMapper.insert(album);
        return album;
    }

    @Override
    public void update(Album album) {
        albumMapper.updateById(album);
    }

    @Override
    public void deleteById(Long id) {
        albumMapper.deleteById(id);
    }

    @Override
    public Optional<Album> findById(Long id) {
        return Optional.ofNullable(albumMapper.selectById(id));
    }

    @Override
    public Optional<Album> findBySlug(String slug) {
        return Optional.ofNullable(albumMapper.findBySlug(slug));
    }

    @Override
    public List<Album> findPage(Integer page, Integer size, Integer isPublic) {
        int offset = (page - 1) * size;
        return albumMapper.findPage(offset, size, isPublic);
    }

    @Override
    public long count(Integer isPublic) {
        return albumMapper.count(isPublic);
    }

    @Override
    public List<Album> findAllPublic() {
        return albumMapper.findAllPublic();
    }

    @Override
    public void updateImageCount(Long albumId, int count) {
        albumMapper.updateImageCount(albumId, count);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return albumMapper.existsBySlug(slug);
    }
}
