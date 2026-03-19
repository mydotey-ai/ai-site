package org.mydotey.ai.site.creation.domain.repository;

import org.mydotey.ai.site.creation.domain.entity.EssayCategory;

import java.util.List;
import java.util.Optional;

/**
 * 散文分类仓储接口
 *
 * @author AI-Site
 */
public interface EssayCategoryRepository {

    Optional<EssayCategory> findById(Long id);

    Optional<EssayCategory> findBySlug(String slug);

    List<EssayCategory> findAll();

    void save(EssayCategory category);

    void update(EssayCategory category);

    void deleteById(Long id);
}
