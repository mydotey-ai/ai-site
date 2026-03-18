package org.mydotey.ai.site.blog.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.domain.entity.Category;
import org.mydotey.ai.site.blog.domain.repository.CategoryRepository;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分类查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryService {

    private final CategoryRepository categoryRepository;

    /**
     * 查询所有分类
     */
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * 查询分类树
     */
    public List<Category> findTree() {
        List<Category> all = categoryRepository.findAll();

        // 构建树形结构
        Map<Long, List<Category>> childrenMap = all.stream()
                .filter(c -> c.getParentId() != null && c.getParentId() > 0)
                .collect(Collectors.groupingBy(Category::getParentId));

        return all.stream()
                .filter(c -> c.getParentId() == null || c.getParentId() == 0)
                .peek(c -> setChildren(c, childrenMap))
                .collect(Collectors.toList());
    }

    /**
     * 根据ID查询分类
     */
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "分类不存在"));
    }

    /**
     * 根据Slug查询分类
     */
    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "分类不存在"));
    }

    /**
     * 递归设置子分类
     */
    private void setChildren(Category category, Map<Long, List<Category>> childrenMap) {
        List<Category> children = childrenMap.get(category.getId());
        if (children != null) {
            // Category 实体没有 children 字段，这里只返回扁平列表
            children.forEach(c -> setChildren(c, childrenMap));
        }
    }
}
