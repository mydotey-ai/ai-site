package org.mydotey.ai.site.creation.application.command;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.mydotey.ai.site.creation.domain.entity.Chapter;
import org.mydotey.ai.site.creation.domain.entity.Novel;
import org.mydotey.ai.site.creation.domain.enums.ChapterStatus;
import org.mydotey.ai.site.creation.domain.repository.ChapterRepository;
import org.mydotey.ai.site.creation.domain.repository.NovelRepository;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.ChapterMapper;
import org.mydotey.ai.site.creation.infrastructure.persistence.mapper.NovelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 章节命令服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ChapterCommandService {

    private final ChapterRepository chapterRepository;
    private final NovelRepository novelRepository;
    private final NovelMapper novelMapper;
    private final ChapterMapper chapterMapper;

    /**
     * 创建章节
     */
    public Long createChapter(Chapter chapter) {
        // 检查小说是否存在
        Novel novel = novelRepository.findById(chapter.getNovelId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "小说不存在"));

        // 自动分配章节序号
        if (chapter.getChapterNo() == null) {
            int maxChapterNo = chapterRepository.findMaxChapterNo(chapter.getNovelId());
            chapter.setChapterNo(maxChapterNo + 1);
        } else {
            // 检查章节序号是否已存在
            if (chapterRepository.existsByNovelIdAndChapterNo(chapter.getNovelId(), chapter.getChapterNo())) {
                throw new BusinessException(ErrorCode.ALREADY_EXISTS, "章节序号已存在");
            }
        }

        // 设置默认值
        if (chapter.getStatus() == null || chapter.getStatus().isEmpty()) {
            chapter.setStatus(ChapterStatus.DRAFT.getCode());
        }
        if (chapter.getViewCount() == null) {
            chapter.setViewCount(0);
        }

        // 计算字数
        if (StrUtil.isNotBlank(chapter.getContent())) {
            chapter.setWordCount(chapter.getContent().length());
        } else {
            chapter.setWordCount(0);
        }

        chapterRepository.save(chapter);

        // 更新小说统计信息
        novelMapper.updateStats(chapter.getNovelId());

        return chapter.getId();
    }

    /**
     * 更新章节
     */
    public void updateChapter(Chapter chapter) {
        // 检查章节是否存在
        chapterRepository.findById(chapter.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "章节不存在"));

        // 检查章节序号是否已被其他章节使用
        if (chapter.getChapterNo() != null &&
            chapterRepository.existsByNovelIdAndChapterNoExcludeId(
                chapter.getNovelId(), chapter.getChapterNo(), chapter.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS, "章节序号已存在");
        }

        // 计算字数
        if (StrUtil.isNotBlank(chapter.getContent())) {
            chapter.setWordCount(chapter.getContent().length());
        } else {
            chapter.setWordCount(0);
        }

        chapterRepository.update(chapter);

        // 更新小说统计信息
        novelMapper.updateStats(chapter.getNovelId());
    }

    /**
     * 删除章节
     */
    public void deleteChapter(Long id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "章节不存在"));

        Long novelId = chapter.getNovelId();
        chapterRepository.deleteById(id);

        // 更新小说统计信息
        novelMapper.updateStats(novelId);
    }

    /**
     * 发布章节
     */
    public void publishChapter(Long id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "章节不存在"));

        chapter.setStatus(ChapterStatus.PUBLISHED.getCode());
        chapterRepository.update(chapter);

        // 更新小说统计信息
        novelMapper.updateStats(chapter.getNovelId());
    }

    /**
     * 批量删除章节
     */
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            try {
                deleteChapter(id);
            } catch (BusinessException e) {
                // 忽略不存在的章节
            }
        }
    }

    /**
     * 批量修改状态
     */
    public void batchUpdateStatus(List<Long> ids, String status) {
        for (Long id : ids) {
            Chapter chapter = chapterRepository.findById(id).orElse(null);
            if (chapter != null) {
                chapter.setStatus(status);
                chapterRepository.update(chapter);
                novelMapper.updateStats(chapter.getNovelId());
            }
        }
    }
}
