package com.xmu.modules.display_config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmu.modules.display_config.domain.ResourceProperty;
import com.xmu.modules.display_config.request.ResourcePropertyDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author Xing
 */
public interface ResourcePropertyService extends IService<ResourceProperty> {

    /**
     * 获取属性列表
     * @param projectId
     * @param resourceId
     * @return
     */
    ResponseEntity<List<ResourcePropertyDTO>> listResourceProperty(Long projectId, Long resourceId);

    /**
     * 创建/更新资源属性
     * @param resourcePropertyDTO
     * @return
     */
    ResponseEntity saveOrUpdateResourceProperty(ResourcePropertyDTO resourcePropertyDTO);

    /**
     * 删除资源属性
     * @param resourceId
     * @param id
     * @return
     */
    ResponseEntity deleteResourceProperty(Long resourceId, Long id);

}
