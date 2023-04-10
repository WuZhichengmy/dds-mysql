package com.xmu.modules.display_config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmu.modules.display_config.domain.ImportProperty;
import com.xmu.modules.display_config.request.ImportPropertyDTO;
import com.xmu.modules.display_config.request.SaveImportPropertyDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author Xing
 */
public interface ImportPropertyService extends IService<ImportProperty> {

    /**
     * 获取规则
     * @param ruleSetId
     * @return
     */
    List<ImportPropertyDTO> listImportProperty(Long ruleSetId);

    /**
     * 保存导入规则
     * @param saveImportPropertyDTO
     * @return
     */
    ResponseEntity saveImportProperty(SaveImportPropertyDTO saveImportPropertyDTO);
}
