package org.mydotey.ai.site.creation.application.command;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.mydotey.ai.site.creation.domain.entity.Essay;
import org.mydotey.ai.site.creation.domain.enums.CreationStatus;
import org.mydotey.ai.site.creation.domain.repository.EssayCategoryRepository;
import org.mydotey.ai.site.creation.domain.repository.EssayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 散文命令服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EssayCommandService {

    private final EssayRepository essayRepository;
    private final EssayCategoryRepository essayCategoryRepository;

    public Long createEssay(Essay essay) {
        if (StrUtil.isBlank(essay.getSlug())) {
            essay.setSlug(generateSlug(essay.getTitle()));
        }

        if (essayRepository.existsBySlug(essay.getSlug())) {
            essay.setSlug(essay.getSlug() + "-" + IdUtil.simpleUUID().substring(0, 6));
        }

        if (essay.getStatus() == null || essay.getStatus().isEmpty()) {
            essay.setStatus(CreationStatus.DRAFT.getCode());
        }
        if (essay.getViewCount() == null) {
            essay.setViewCount(0);
        }

        if (essay.getCategoryId() != null) {
            essayCategoryRepository.findById(essay.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "分类不存在"));
        }

        essayRepository.save(essay);
        return essay.getId();
    }

    public void updateEssay(Essay essay) {
        essayRepository.findById(essay.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "散文不存在"));

        if (essay.getSlug() != null && essayRepository.existsBySlugExcludeId(essay.getSlug(), essay.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS, "散文别名已存在");
        }

        if (essay.getCategoryId() != null) {
            essayCategoryRepository.findById(essay.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "分类不存在"));
        }

        essayRepository.update(essay);
    }

    public void deleteEssay(Long id) {
        essayRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "散文不存在"));
        essayRepository.deleteById(id);
    }

    public void publishEssay(Long id) {
        Essay essay = essayRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "散文不存在"));
        essay.setStatus(CreationStatus.PUBLISHED.getCode());
        essayRepository.update(essay);
    }

    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            try {
                deleteEssay(id);
            } catch (BusinessException e) {
                // 忽略不存在的
            }
        }
    }

    public void batchUpdateStatus(List<Long> ids, String status) {
        for (Long id : ids) {
            Essay essay = essayRepository.findById(id).orElse(null);
            if (essay != null) {
                essay.setStatus(status);
                essayRepository.update(essay);
            }
        }
    }

    private String generateSlug(String title) {
        if (StrUtil.isBlank(title)) {
            return "essay-" + IdUtil.simpleUUID().substring(0, 8);
        }
        String slug = title.toLowerCase()
                .replaceAll("[\\s\\p{Punct}&&[^-]]", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
        if (StrUtil.isBlank(slug)) {
            slug = "essay-" + IdUtil.simpleUUID().substring(0, 8);
        }
        return slug;
    }
}
