package org.mydotey.ai.site.creation.domain.repository;

import org.mydotey.ai.site.creation.application.query.CreationQuery;
import org.mydotey.ai.site.creation.domain.entity.Essay;

import java.util.List;
import java.util.Optional;

/**
 * 散文仓储接口
 *
 * @author AI-Site
 */
public interface EssayRepository {

    Optional<Essay> findById(Long id);

    Optional<Essay> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugExcludeId(String slug, Long excludeId);

    void save(Essay essay);

    void update(Essay essay);

    void deleteById(Long id);

    List<Essay> findPage(CreationQuery query);

    long count(CreationQuery query);
}
