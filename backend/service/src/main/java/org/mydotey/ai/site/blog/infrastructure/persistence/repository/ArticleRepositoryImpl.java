package org.mydotey.ai.site.blog.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.blog.domain.entity.Article;
import org.mydotey.ai.site.blog.domain.repository.ArticleRepository;
import org.mydotey.ai.site.blog.infrastructure.persistence.mapper.ArticleMapper;
import org.mydotey.ai.site.blog.application.query.ArticleQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 文章仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final ArticleMapper articleMapper;

    @Override
    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(articleMapper.selectById(id));
    }

    @Override
    public Optional<Article> findBySlug(String slug) {
        return Optional.ofNullable(articleMapper.findBySlug(slug));
    }

    @Override
    public boolean existsBySlug(String slug) {
        return articleMapper.countBySlug(slug) > 0;
    }

    @Override
    public boolean existsBySlugExcludeId(String slug, Long excludeId) {
        return articleMapper.countBySlugExcludeId(slug, excludeId) > 0;
    }

    @Override
    public void save(Article article) {
        articleMapper.insert(article);
    }

    @Override
    public void update(Article article) {
        articleMapper.updateById(article);
    }

    @Override
    public void deleteById(Long id) {
        articleMapper.deleteById(id);
    }

    @Override
    public List<Article> findPage(ArticleQuery query) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Article::getIsTop)
               .orderByDesc(Article::getCreatedAt);

        if (query.getStatus() != null) {
            wrapper.eq(Article::getStatus, query.getStatus());
        }
        if (query.getCategoryId() != null) {
            wrapper.eq(Article::getCategoryId, query.getCategoryId());
        }
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.and(w -> w
                    .like(Article::getTitle, query.getKeyword())
                    .or()
                    .like(Article::getSummary, query.getKeyword()));
        }

        Page<Article> page = new Page<>(query.getPage(), query.getSize());
        return articleMapper.selectPage(page, wrapper).getRecords();
    }

    @Override
    public long count(ArticleQuery query) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();

        if (query.getStatus() != null) {
            wrapper.eq(Article::getStatus, query.getStatus());
        }
        if (query.getCategoryId() != null) {
            wrapper.eq(Article::getCategoryId, query.getCategoryId());
        }
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.and(w -> w
                    .like(Article::getTitle, query.getKeyword())
                    .or()
                    .like(Article::getSummary, query.getKeyword()));
        }

        return articleMapper.selectCount(wrapper);
    }

    @Override
    public List<Article> findPublished(int page, int size) {
        Page<Article> pageParam = new Page<>(page, size);
        return articleMapper.findPublished(pageParam).getRecords();
    }

    @Override
    public List<Article> findByCategoryId(Long categoryId, int page, int size) {
        Page<Article> pageParam = new Page<>(page, size);
        return articleMapper.findByCategoryId(pageParam, categoryId).getRecords();
    }

    @Override
    public List<Article> findByTagId(Long tagId, int page, int size) {
        Page<Article> pageParam = new Page<>(page, size);
        return articleMapper.findByTagId(pageParam, tagId).getRecords();
    }

    @Override
    public List<Article> search(String keyword, int page, int size) {
        Page<Article> pageParam = new Page<>(page, size);
        return articleMapper.search(pageParam, keyword).getRecords();
    }

    @Override
    public void incrementViewCount(Long id) {
        articleMapper.incrementViewCount(id);
    }

    @Override
    public void incrementLikeCount(Long id) {
        articleMapper.incrementLikeCount(id);
    }

    @Override
    public List<Long> findTagIdsByArticleId(Long articleId) {
        return articleMapper.findTagIdsByArticleId(articleId);
    }

    @Override
    public void saveArticleTags(Long articleId, List<Long> tagIds) {
        if (tagIds != null && !tagIds.isEmpty()) {
            articleMapper.insertArticleTags(articleId, tagIds);
        }
    }

    @Override
    public void deleteArticleTags(Long articleId) {
        articleMapper.deleteArticleTags(articleId);
    }
}
