package org.mydotey.ai.site.media.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.media.domain.entity.Folder;
import org.mydotey.ai.site.media.domain.repository.FolderRepository;
import org.mydotey.ai.site.media.infrastructure.persistence.mapper.FolderMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 文件夹仓储实现
 *
 * @author AI-Site
 */
@Repository
@RequiredArgsConstructor
public class FolderRepositoryImpl implements FolderRepository {

    private final FolderMapper folderMapper;

    @Override
    public Folder save(Folder folder) {
        folderMapper.insert(folder);
        return folder;
    }

    @Override
    public void update(Folder folder) {
        folderMapper.updateById(folder);
    }

    @Override
    public void deleteById(Long id) {
        folderMapper.deleteById(id);
    }

    @Override
    public Optional<Folder> findById(Long id) {
        return Optional.ofNullable(folderMapper.selectById(id));
    }

    @Override
    public List<Folder> findByType(String type) {
        return folderMapper.findByType(type);
    }

    @Override
    public List<Folder> findByParentId(Long parentId) {
        return folderMapper.findByParentId(parentId);
    }

    @Override
    public List<Folder> findAll() {
        return folderMapper.findAll();
    }

    @Override
    public boolean hasChildren(Long id) {
        return folderMapper.hasChildren(id);
    }
}
