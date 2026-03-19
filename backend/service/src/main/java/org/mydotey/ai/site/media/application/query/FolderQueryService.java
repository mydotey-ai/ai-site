package org.mydotey.ai.site.media.application.query;

import lombok.RequiredArgsConstructor;
import org.mydotey.ai.site.media.domain.entity.Folder;
import org.mydotey.ai.site.media.domain.repository.FolderRepository;
import org.mydotey.ai.site.media.interfaces.dto.FolderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文件夹查询服务
 *
 * @author AI-Site
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FolderQueryService {

    private final FolderRepository folderRepository;

    /**
     * 查询所有文件夹
     */
    public List<FolderResponse> findAll() {
        List<Folder> folders = folderRepository.findAll();
        return buildTree(folders, null);
    }

    /**
     * 根据类型查询文件夹
     */
    public List<FolderResponse> findByType(String type) {
        List<Folder> folders = folderRepository.findByType(type);
        return buildTree(folders, null);
    }

    /**
     * 根据ID查询文件夹
     */
    public FolderResponse findById(Long id) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new org.mydotey.ai.site.common.exception.BusinessException(
                        org.mydotey.ai.site.common.exception.ErrorCode.FOLDER_NOT_FOUND));
        return toResponse(folder);
    }

    /**
     * 构建文件夹树
     */
    private List<FolderResponse> buildTree(List<Folder> folders, Long parentId) {
        Map<Long, List<Folder>> folderMap = folders.stream()
                .collect(Collectors.groupingBy(f -> f.getParentId() != null ? f.getParentId() : -1L));

        List<Folder> rootFolders = folderMap.getOrDefault(parentId != null ? parentId : -1L, new ArrayList<>());

        return rootFolders.stream()
                .map(folder -> {
                    FolderResponse response = toResponse(folder);
                    response.setChildren(buildTree(folders, folder.getId()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    /**
     * 转换为响应对象
     */
    private FolderResponse toResponse(Folder folder) {
        return FolderResponse.builder()
                .id(folder.getId())
                .name(folder.getName())
                .parentId(folder.getParentId())
                .type(folder.getType())
                .sort(folder.getSort())
                .createdAt(folder.getCreatedAt())
                .build();
    }
}
