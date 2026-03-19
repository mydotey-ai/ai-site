package org.mydotey.ai.site.creation.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.creation.domain.entity.Essay;
import org.mydotey.ai.site.creation.domain.entity.EssayCategory;
import org.mydotey.ai.site.creation.domain.enums.CreationStatus;
import org.mydotey.ai.site.creation.domain.repository.EssayCategoryRepository;
import org.mydotey.ai.site.creation.domain.repository.EssayRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.EssayMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 散文查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EssayQueryService {

    private final EssayRepository essayRepository;
    private final EssayCategoryRepository essayCategoryRepository;
    private final EssayMapper essayMapper;

    public PageResult<Essay> findPage(CreationQuery query) {
        List<Essay> list = essayRepository.findPage(query);
        long total = essayRepository.count(query);

        for (Essay essay : list) {
            loadCategory(essay);
        }

        return PageResult.of(list, total);
    }

    public Essay findById(Long id) {
        Essay essay = essayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("散文不存在"));
        loadCategory(essay);
        return essay;
    }

    public Essay findBySlug(String slug) {
        Essay essay = essayRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("散文不存在"));
        loadCategory(essay);
        return essay;
    }

    public PageResult<Essay> findPublished(CreationQuery query) {
        query.setStatus(CreationStatus.PUBLISHED.getCode());
        return findPage(query);
    }

    private void loadCategory(Essay essay) {
        if (essay.getCategoryId() != null) {
            essayCategoryRepository.findById(essay.getCategoryId())
                    .ifPresent(essay::setCategory);
        }
    }
}
