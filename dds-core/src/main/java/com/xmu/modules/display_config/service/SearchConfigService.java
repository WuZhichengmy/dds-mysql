package com.xmu.modules.display_config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmu.modules.display_config.domain.SearchConfig;
import com.xmu.modules.display_config.request.*;

import java.util.List;

/**
 * @author Xing
 */
public interface SearchConfigService extends IService<SearchConfig> {
    Object searchConfig(Long projectId, List<SearchConfigDTO> searchConfigDTOS);

    Object getSearchConfig(Long projectId, Long resourceId);

    /**
     * 删除资源配置信息
     * @param resourceId
     */
    void deleteConfig(Long resourceId);

}
