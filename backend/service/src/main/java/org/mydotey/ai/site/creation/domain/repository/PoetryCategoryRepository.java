package org.mydotey.ai.site.creation.domain.repository;

import org.mydotey.ai.site.creation.domain.entity.PoetryCategory;

import java.util.List;
import java.util.Optional;

/**
 * 诗歌分类仓储接口
 *
 * @author AI-Site
 */
public interface PoetryCategoryRepository {

    Optional<PoetryCategory> findById(Long id);

    Optional<PoetryCategory> findBySlug(String slug);

    List<PoetryCategory> findAll();

    void save(PoetryCategory category);

    void update(PoetryCategory category);

    void deleteById(Long id);
}
