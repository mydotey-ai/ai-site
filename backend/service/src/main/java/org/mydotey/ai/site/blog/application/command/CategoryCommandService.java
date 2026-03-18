package org.mydotey.ai.site.blog.application.command;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.domain.entity.Category;
import org.mydotey.ai.site.blog.domain.repository.ArticleRepository;
import org.mydotey.ai.site.blog.domain.repository.CategoryRepository;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分类命令服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryCommandService {

    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    /**
     * 创建分类
     */
    public Long createCategory(Category category) {
        // 检查 slug 是否已存在
        if (categoryRepository.existsBySlug(category.getSlug())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS, "分类别名已存在");
        }

        // 验证父分类是否存在
        if (category.getParentId() != null && category.getParentId() > 0) {
            categoryRepository.findById(category.getParentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "父分类不存在"));
        }

        // 设置默认值
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        if (category.getArticleCount() == null) {
            category.setArticleCount(0);
        }
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }

        categoryRepository.save(category);
        return category.getId();
    }

    /**
     * 更新分类
     */
    public void updateCategory(Category category) {
        // 检查分类是否存在
        categoryRepository.findById(category.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "分类不存在"));

        // 检查 slug 是否已被其他分类使用
        if (categoryRepository.existsBySlugExcludeId(category.getSlug(), category.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS, "分类别名已存在");
        }

        // 验证父分类是否存在
        if (category.getParentId() != null && category.getParentId() > 0) {
            // 不能将自己设为父分类
            if (category.getParentId().equals(category.getId())) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "不能将自己设为父分类");
            }
            categoryRepository.findById(category.getParentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "父分类不存在"));
        }

        categoryRepository.update(category);
    }

    /**
     * 删除分类
     */
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "分类不存在"));

        // 检查是否有子分类
        if (!categoryRepository.findByParentId(id).isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "该分类下有子分类，无法删除");
        }

        // 检查是否有关联文章
        if (category.getArticleCount() > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "该分类下有文章，无法删除");
        }

        categoryRepository.deleteById(id);
    }
}
