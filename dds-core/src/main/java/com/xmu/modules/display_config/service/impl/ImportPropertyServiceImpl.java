package com.xmu.modules.display_config.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xmu.exception.EntityNotFoundException;
import com.xmu.modules.display_config.domain.ImportProperty;
import com.xmu.modules.display_config.domain.ResourceProperty;
import com.xmu.modules.display_config.domain.RuleSet;
import com.xmu.modules.display_config.mapper.ImportPropertyMapper;
import com.xmu.modules.display_config.request.ImportPropertyDTO;
import com.xmu.modules.display_config.request.SaveImportPropertyDTO;
import com.xmu.modules.display_config.service.ImportPropertyService;
import com.xmu.modules.display_config.service.ResourcePropertyService;
import com.xmu.modules.display_config.service.RuleSetService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Xing
 */
@Service
public class ImportPropertyServiceImpl extends ServiceImpl<ImportPropertyMapper, ImportProperty> implements ImportPropertyService {

    @Autowired
    private RuleSetService ruleSetService;
    @Autowired
    private ResourcePropertyService resourcePropertyService;

    @Override
    public List<ImportPropertyDTO> listImportProperty(Long ruleSetId) {
        // 查规则集
        RuleSet ruleSet = ruleSetService.getById(ruleSetId);
        if (null == ruleSet) {
            throw new EntityNotFoundException(RuleSet.class, "id", ruleSetId);
        }
        List<ImportProperty> list = this.list(Wrappers.<ImportProperty>lambdaQuery().eq(ImportProperty::getRuleSetId, ruleSetId));
        List<ImportPropertyDTO> result = Lists.newArrayListWithCapacity(list.size());
        if (CollectionUtils.isNotEmpty(list)) {
            List<Long> resourcePropertyIds = list.stream().map(ImportProperty::getResourcePropertyId).collect(Collectors.toList());
            List<ResourceProperty> resourcePropertyList = resourcePropertyService.listByIds(resourcePropertyIds);
            // K-资源属性id V-资源属性名称
            Map<Long, String> map = resourcePropertyList.stream().collect(Collectors.toMap(ResourceProperty::getId, ResourceProperty::getName));
            list.forEach(importProperty -> {
                ImportPropertyDTO importPropertyDTO = new ImportPropertyDTO();
                BeanUtils.copyProperties(importProperty, importPropertyDTO);
                importPropertyDTO.setPropertyName(map.get(importProperty.getResourcePropertyId()));
                result.add(importPropertyDTO);
            });
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity saveImportProperty(SaveImportPropertyDTO saveImportPropertyDTO) {
        // 查规则集
        RuleSet ruleSet = ruleSetService.getById(saveImportPropertyDTO.getRuleSetId());
        if (null == ruleSet) {
            throw new EntityNotFoundException(RuleSet.class, "id", saveImportPropertyDTO.getRuleSetId());
        }
        // 删除旧规则
        this.remove(Wrappers.<ImportProperty>lambdaQuery().eq(ImportProperty::getRuleSetId, saveImportPropertyDTO.getRuleSetId()));
        List<ImportProperty> list = Lists.newArrayListWithCapacity(saveImportPropertyDTO.getSimpleImportPropertyList().size());
        saveImportPropertyDTO.getSimpleImportPropertyList().forEach(simpleImportPropertyDTO -> {
            ImportProperty importProperty = new ImportProperty(saveImportPropertyDTO.getRuleSetId(),
                    simpleImportPropertyDTO.getExcelHeader(), simpleImportPropertyDTO.getResourcePropertyId());
            list.add(importProperty);
        });
        this.saveBatch(list);
        return new ResponseEntity(HttpStatus.OK);
    }

}
