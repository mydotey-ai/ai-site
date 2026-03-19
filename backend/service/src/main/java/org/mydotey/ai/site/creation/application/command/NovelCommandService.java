package org.mydotey.ai.site.creation.application.command;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.mydotey.ai.site.creation.domain.entity.Chapter;
import org.mydotey.ai.site.creation.domain.entity.Novel;
import org.mydotey.ai.site.creation.domain.enums.ChapterStatus;
import org.mydotey.ai.site.creation.domain.enums.NovelStatus;
import org.mydotey.ai.site.creation.domain.repository.ChapterRepository;
import org.mydotey.ai.site.creation.domain.repository.NovelCategoryRepository;
import org.mydotey.ai.site.creation.domain.repository.NovelRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.ChapterMapper;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.NovelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 小说命令服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NovelCommandService {

    private final NovelRepository novelRepository;
    private final NovelCategoryRepository novelCategoryRepository;
    private final ChapterRepository chapterRepository;
    private final NovelMapper novelMapper;

    /**
     * 创建小说
     */
    public Long createNovel(Novel novel) {
        // 自动生成 slug
        if (StrUtil.isBlank(novel.getSlug())) {
            novel.setSlug(generateSlug(novel.getTitle()));
        }

        // 检查 slug 是否已存在
        if (novelRepository.existsBySlug(novel.getSlug())) {
            novel.setSlug(novel.getSlug() + "-" + IdUtil.simpleUUID().substring(0, 6));
        }

        // 设置默认值
        if (novel.getStatus() == null || novel.getStatus().isEmpty()) {
            novel.setStatus(NovelStatus.DRAFT.getCode());
        }
        if (novel.getWordCount() == null) {
            novel.setWordCount(0);
        }
        if (novel.getChapterCount() == null) {
            novel.setChapterCount(0);
        }
        if (novel.getViewCount() == null) {
            novel.setViewCount(0);
        }

        // 验证分类是否存在
        if (novel.getCategoryId() != null) {
            novelCategoryRepository.findById(novel.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "分类不存在"));
        }

        novelRepository.save(novel);
        return novel.getId();
    }

    /**
     * 更新小说
     */
    public void updateNovel(Novel novel) {
        // 检查小说是否存在
        novelRepository.findById(novel.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "小说不存在"));

        // 检查 slug 是否已被其他小说使用
        if (novel.getSlug() != null && novelRepository.existsBySlugExcludeId(novel.getSlug(), novel.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS, "小说别名已存在");
        }

        // 验证分类是否存在
        if (novel.getCategoryId() != null) {
            novelCategoryRepository.findById(novel.getCategoryId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "分类不存在"));
        }

        novelRepository.update(novel);
    }

    /**
     * 删除小说
     */
    public void deleteNovel(Long id) {
        novelRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "小说不存在"));

        // 删除所有章节
        chapterRepository.deleteByNovelId(id);

        // 删除小说
        novelRepository.deleteById(id);
    }

    /**
     * 发布小说
     */
    public void publishNovel(Long id) {
        Novel novel = novelRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "小说不存在"));

        novel.setStatus(NovelStatus.PUBLISHED.getCode());
        novelRepository.update(novel);
    }

    /**
     * 完结小说
     */
    public void completeNovel(Long id) {
        Novel novel = novelRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "小说不存在"));

        novel.setStatus(NovelStatus.COMPLETED.getCode());
        novelRepository.update(novel);
    }

    /**
     * 批量删除小说
     */
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            try {
                deleteNovel(id);
            } catch (BusinessException e) {
                // 忽略不存在的小说
            }
        }
    }

    /**
     * 批量修改状态
     */
    public void batchUpdateStatus(List<Long> ids, String status) {
        for (Long id : ids) {
            Novel novel = novelRepository.findById(id).orElse(null);
            if (novel != null) {
                novel.setStatus(status);
                novelRepository.update(novel);
            }
        }
    }

    /**
     * 生成 slug
     */
    private String generateSlug(String title) {
        if (StrUtil.isBlank(title)) {
            return "novel-" + IdUtil.simpleUUID().substring(0, 8);
        }
        String slug = title.toLowerCase()
                .replaceAll("[\\s\\p{Punct}&&[^-]]", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
        if (StrUtil.isBlank(slug)) {
            slug = "novel-" + IdUtil.simpleUUID().substring(0, 8);
        }
        return slug;
    }
}
