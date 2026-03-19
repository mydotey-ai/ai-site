package org.mydotey.ai.site.media.application.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mydotey.ai.site.common.exception.BusinessException;
import org.mydotey.ai.site.common.exception.ErrorCode;
import org.mydotey.ai.site.media.domain.entity.Folder;
import org.mydotey.ai.site.media.domain.repository.FolderRepository;
import org.mydotey.ai.site.media.interfaces.dto.FolderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文件夹命令服务
 *
 * @author AI-Site
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FolderCommandService {

    private final FolderRepository folderRepository;

    /**
     * 创建文件夹
     */
    public Long create(FolderRequest request) {
        // 验证父文件夹
        if (request.getParentId() != null) {
            folderRepository.findById(request.getParentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.FOLDER_NOT_FOUND, "父文件夹不存在"));
        }

        Folder folder = new Folder();
        folder.setName(request.getName());
        folder.setParentId(request.getParentId());
        folder.setType(request.getType());
        folder.setSort(request.getSort());

        folder = folderRepository.save(folder);
        log.info("文件夹创建成功: id={}, name={}", folder.getId(), folder.getName());
        return folder.getId();
    }

    /**
     * 更新文件夹
     */
    public void update(Long id, FolderRequest request) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOLDER_NOT_FOUND));

        folder.setName(request.getName());
        folder.setParentId(request.getParentId());
        folder.setSort(request.getSort());

        folderRepository.update(folder);
        log.info("文件夹更新成功: id={}", id);
    }

    /**
     * 删除文件夹
     */
    public void delete(Long id) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.FOLDER_NOT_FOUND));

        // 检查是否有子文件夹
        if (folderRepository.hasChildren(id)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "文件夹下有子文件夹，无法删除");
        }

        folderRepository.deleteById(id);
        log.info("文件夹删除成功: id={}", id);
    }
}
