package org.mydotey.ai.site.creation.domain.repository;

import org.mydotey.ai.site.creation.application.query.CreationQuery;
import org.mydotey.ai.site.creation.domain.entity.Poetry;

import java.util.List;
import java.util.Optional;

/**
 * 诗歌仓储接口
 *
 * @author AI-Site
 */
public interface PoetryRepository {

    Optional<Poetry> findById(Long id);

    Optional<Poetry> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugExcludeId(String slug, Long excludeId);

    void save(Poetry poetry);

    void update(Poetry poetry);

    void deleteById(Long id);

    List<Poetry> findPage(CreationQuery query);

    long count(CreationQuery query);
}
