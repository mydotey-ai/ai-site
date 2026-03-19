package org.mydotey.ai.site.creation.application.command;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.mydotey.ai.site.creation.domain.entity.Poetry;
import org.mydotey.ai.site.creation.domain.enums.CreationStatus;
import org.mydotey.ai.site.creation.domain.repository.PoetryCategoryRepository;
import org.mydotey.ai.site.creation.domain.repository.PoetryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 诗歌命令服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PoetryCommandService {

    private final PoetryRepository poetryRepository;
    private final PoetryCategoryRepository poetryCategoryRepository;

    public Long createPoetry(Poetry poetry) {
        if (StrUtil.isBlank(poetry.getSlug())) {
            poetry.setSlug(generateSlug(poetry.getTitle()));
        }

        if (poetryRepository.existsBySlug(poetry.getSlug())) {
            poetry.setSlug(poetry.getSlug() + "-" + IdUtil.simpleUUID().substring(0, 6));
        }

        if (poetry.getStatus() == null || poetry.getStatus().isEmpty()) {
            poetry.setStatus(CreationStatus.DRAFT.getCode());
        }
        if (poetry.getViewCount() == null) {
            poetry.setViewCount(0);
        }

        if (poetry.getCategoryId() != null) {
            poetryCategoryRepository.findById(poetry.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "分类不存在"));
        }

        poetryRepository.save(poetry);
        return poetry.getId();
    }

    public void updatePoetry(Poetry poetry) {
        poetryRepository.findById(poetry.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "诗歌不存在"));

        if (poetry.getSlug() != null && poetryRepository.existsBySlugExcludeId(poetry.getSlug(), poetry.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS, "诗歌别名已存在");
        }

        if (poetry.getCategoryId() != null) {
            poetryCategoryRepository.findById(poetry.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "分类不存在"));
        }

        poetryRepository.update(poetry);
    }

    public void deletePoetry(Long id) {
        poetryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "诗歌不存在"));
        poetryRepository.deleteById(id);
    }

    public void publishPoetry(Long id) {
        Poetry poetry = poetryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "诗歌不存在"));
        poetry.setStatus(CreationStatus.PUBLISHED.getCode());
        poetryRepository.update(poetry);
    }

    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            try {
                deletePoetry(id);
            } catch (BusinessException e) {
                // 忽略不存在的
            }
        }
    }

    public void batchUpdateStatus(List<Long> ids, String status) {
        for (Long id : ids) {
            Poetry poetry = poetryRepository.findById(id).orElse(null);
            if (poetry != null) {
                poetry.setStatus(status);
                poetryRepository.update(poetry);
            }
        }
    }

    private String generateSlug(String title) {
        if (StrUtil.isBlank(title)) {
            return "poetry-" + IdUtil.simpleUUID().substring(0, 8);
        }
        String slug = title.toLowerCase()
                .replaceAll("[\\s\\p{Punct}&&[^-]]", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
        if (StrUtil.isBlank(slug)) {
            slug = "poetry-" + IdUtil.simpleUUID().substring(0, 8);
        }
        return slug;
    }
}
