package com.xmu.modules.display_config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmu.modules.display_config.domain.AnalysisMenu;
import com.xmu.modules.display_config.request.AnalysisDetailDTO;
import com.xmu.modules.display_config.request.MenuConfigDTO;
import com.xmu.modules.display_config.request.SearchInfo;

import java.util.List;

/**
 * @author Xing
 */
public interface AnalysisMenuService extends IService<AnalysisMenu> {

    /**
     * 保存目录
     * @param projectId
     * @param configDTOs
     * @return
     */
    Object menuConfig(Long projectId, List<MenuConfigDTO> configDTOs);

    /**
     * 保存分析详情
     * @param configDTOs
     * @return
     */
    Object detailConfig(List<AnalysisDetailDTO<?>> configDTOs);

    /**
     * 获取分析目录
     * @param projectId
     * @param resourceId
     * @return
     */
    Object getMenu(Long projectId, Long resourceId);

    /**
     * 获取分析详情
     * @param resourceId
     * @param menuId
     * @return
     */
    Object getDetail(Long resourceId, Long menuId);

    /**
     * 获取分析结果
     * @param menuId
     * @param searchInfo
     * @return
     */
    Object getAnalysisData( Long menuId, SearchInfo searchInfo);

    /**
     * 删除分析配置
     * @param projectId
     * @param resourceId
     * @return
     */
    void deleteConfig(Long projectId, Long resourceId);

    /**
     * 删除分析目录
     * @param menuIds
     * @return
     */
    Object deleteMenu(List<Long> menuIds);

    /**
     * 修改分析目录
     * @param menuId
     * @param config
     * @return
     */
    Object modifyMenu(Long menuId, MenuConfigDTO config);
}
