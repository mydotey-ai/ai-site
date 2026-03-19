package org.mydotey.ai.site.media.domain.repository;

import org.mydotey.ai.site.media.domain.entity.Folder;

import java.util.List;
import java.util.Optional;

/**
 * 文件夹仓储接口
 *
 * @author AI-Site
 */
public interface FolderRepository {

    /**
     * 保存文件夹
     */
    Folder save(Folder folder);

    /**
     * 更新文件夹
     */
    void update(Folder folder);

    /**
     * 根据ID删除文件夹
     */
    void deleteById(Long id);

    /**
     * 根据ID查找文件夹
     */
    Optional<Folder> findById(Long id);

    /**
     * 根据类型查找文件夹
     */
    List<Folder> findByType(String type);

    /**
     * 根据父ID查找子文件夹
     */
    List<Folder> findByParentId(Long parentId);

    /**
     * 查找所有文件夹
     */
    List<Folder> findAll();

    /**
     * 检查文件夹下是否有子文件夹
     */
    boolean hasChildren(Long id);
}
