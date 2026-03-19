package org.mydotey.ai.site.creation.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.module.interfaces.PageResult;
import org.mydotey.ai.site.creation.domain.entity.Poetry;
import org.mydotey.ai.site.creation.domain.entity.PoetryCategory;
import org.mydotey.ai.site.creation.domain.enums.CreationStatus;
import org.mydotey.ai.site.creation.domain.repository.PoetryCategoryRepository;
import org.mydotey.ai.site.creation.domain.repository.PoetryRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.PoetryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 诗歌查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PoetryQueryService {

    private final PoetryRepository poetryRepository;
    private final PoetryCategoryRepository poetryCategoryRepository;
    private final PoetryMapper poetryMapper;

    public PageResult<Poetry> findPage(CreationQuery query) {
        List<Poetry> list = poetryRepository.findPage(query);
        long total = poetryRepository.count(query);

        for (Poetry poetry : list) {
            loadCategory(poetry);
        }

        return PageResult.of(list, total);
    }

    public Poetry findById(Long id) {
        Poetry poetry = poetryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("诗歌不存在"));
        loadCategory(poetry);
        return poetry;
    }

    public Poetry findBySlug(String slug) {
        Poetry poetry = poetryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("诗歌不存在"));
        loadCategory(poetry);
        return poetry;
    }

    public PageResult<Poetry> findPublished(CreationQuery query) {
        query.setStatus(CreationStatus.PUBLISHED.getCode());
        return findPage(query);
    }

    private void loadCategory(Poetry poetry) {
        if (poetry.getCategoryId() != null) {
            poetryCategoryRepository.findById(poetry.getCategoryId())
                    .ifPresent(poetry::setCategory);
        }
    }
}
