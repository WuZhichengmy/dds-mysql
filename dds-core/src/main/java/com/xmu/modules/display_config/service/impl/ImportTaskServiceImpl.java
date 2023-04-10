package com.xmu.modules.display_config.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xmu.config.FileProperties;
import com.xmu.exception.BadRequestException;
import com.xmu.exception.EntityNotFoundException;
import com.xmu.modules.display_config.domain.ImportTask;
import com.xmu.modules.display_config.domain.Resource;
import com.xmu.modules.display_config.domain.ResourceProperty;
import com.xmu.modules.display_config.domain.RuleSet;
import com.xmu.modules.display_config.enums.ImportTaskStatusEnum;
import com.xmu.modules.display_config.mapper.ImportTaskMapper;
import com.xmu.modules.display_config.request.ImportPropertyDTO;
import com.xmu.modules.display_config.request.ImportTaskDTO;
import com.xmu.modules.display_config.request.importDataDTO;
import com.xmu.modules.display_config.response.ExcelDataDTO;
import com.xmu.modules.display_config.service.*;
import com.xmu.modules.display_config.utils.ExcelUtils;
import com.xmu.modules.display_config.utils.ImportDataListener;
import com.xmu.service.LocalStorageService;
import com.xmu.service.dto.LocalStorageDto;
import com.xmu.utils.FileUtil;
import com.xmu.utils.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Xing
 */
@Slf4j
@Service
public class ImportTaskServiceImpl extends ServiceImpl<ImportTaskMapper, ImportTask> implements ImportTaskService {

    @Autowired
    private LocalStorageService localStorageService;
    @Autowired
    private RuleSetService ruleSetService;
    @Autowired
    private ImportPropertyService importPropertyService;
    @Autowired
    private ResourcePropertyService resourcePropertyService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private FileProperties properties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveImportTask(importDataDTO importDataDTO) {
        // 查文件
        LocalStorageDto localStorage = localStorageService.findById(importDataDTO.getLocalStorageId());
        if (null == localStorage) {
            throw new BadRequestException("文件不存在!");
        }
        ImportTask importTask = new ImportTask();
        importTask.setId(SnowFlakeUtil.getFlowIdInstance().nextId());
        importTask.setRuleSetId(importDataDTO.getRuleSetId());
        importTask.setResourceId(importDataDTO.getResourceId());
        importTask.setSourceFileName(localStorage.getRealName());
        importTask.setStatus(ImportTaskStatusEnum.IMPORTING.getValue());
        importTask.setSuccessCount(0);
        importTask.setFailCount(0);
        importTask.setTotal(0);
        importTask.setExecutionTime(0L);
        this.save(importTask);
        return importTask.getId();
    }

    @Async
    @Override
    public void importData(Long id) {
        // 开始时间
        Long startTime = System.currentTimeMillis();
        ImportTask importTask = this.getById(id);
        if (null == importTask) {
            log.error("import failed: importTask is null");
            return;
        }
        Resource resource = resourceService.getById(importTask.getResourceId());
        if (null == resource) {
            // 处理失败任务
            this.dealFail(id);
            log.error("import failed: resource is null");
            return;
        }
        // 导入配置
        List<ImportPropertyDTO> importPropertyList = importPropertyService.listImportProperty(importTask.getRuleSetId());
        if (CollectionUtils.isEmpty(importPropertyList)) {
            // 处理失败任务
            this.dealFail(id);
            log.error("import failed: importProperty is empty");
            return;
        }
        // 属性
        List<Long> resourcePropertyIdList = importPropertyList.stream().map(ImportPropertyDTO::getResourcePropertyId).collect(Collectors.toList());
        List<ResourceProperty> resourcePropertyList = resourcePropertyService.listByIds(resourcePropertyIdList);
        if (CollectionUtils.isEmpty(resourcePropertyList)) {
            // 处理失败任务
            this.dealFail(id);
            log.error("import failed: resourceProperty is empty");
            return;
        }
        // K-属性id V-属性
        Map<Long, ResourceProperty> resourcePropertyMap = resourcePropertyList.stream().collect(Collectors.toMap(ResourceProperty::getId, k -> k));
        // K-属性id V-excel表头名称
        Map<Long, String> propertyExcelHeadMap = importPropertyList.stream().collect(Collectors.toMap(ImportPropertyDTO::getResourcePropertyId, ImportPropertyDTO::getExcelHeader));
        // K-属性名 V-excel表头
        Map<String, String> importPropertyExcelMap = Maps.newHashMap();
        // K-属性名 V-属性类型
        Map<String, Integer> importPropertyTypeMap = Maps.newHashMap();
        // Excel导入失败数据集合
        List<JSONObject> failImportDataList = Lists.newArrayList();

        importPropertyList.forEach(importProperty -> {
            ResourceProperty resourceProperty = resourcePropertyMap.get(importProperty.getResourcePropertyId());
            if (null == resourceProperty) {
                return;
            }
            importPropertyExcelMap.put(resourceProperty.getName(), propertyExcelHeadMap.get(importProperty.getResourcePropertyId()));
            importPropertyTypeMap.put(resourceProperty.getName(), resourceProperty.getType());
        });
        String path = properties.getPath().getPath() + FileUtil.TXT + File.separator;
        try {
            EasyExcel.read(path + importTask.getSourceFileName(), new ImportDataListener(importTask, resource.getTarget(), importPropertyExcelMap,
                    importPropertyTypeMap, failImportDataList, resourceService)).sheet().doRead();
        } catch (Exception e) {
            log.error("import task failed :{}", e.getMessage());
            // 处理失败任务
            this.dealFail(id);
            return;
        }
        // 处理失败文件
        if (CollectionUtils.isNotEmpty(failImportDataList)) {
            String failFileName = "ImportFailedData-" + System.currentTimeMillis() + ".xlsx";
            String filePath = path + failFileName;
            ExcelDataDTO excelDataDTO = ExcelUtils.handlerExcelData(importPropertyList, failImportDataList);
            EasyExcel.write(filePath).head(excelDataDTO.getHead()).sheet("Sheet0").doWrite(excelDataDTO.getData());
            importTask.setFailFileName(failFileName);
        }
        importTask.setExecutionTime(System.currentTimeMillis() - startTime);
        importTask.setStatus(ImportTaskStatusEnum.IMPORT_FINISH.getValue());
        this.updateById(importTask);
    }

    @Override
    public ResponseEntity<List<ImportTaskDTO>> listImportTask(Long resourceId) {
        // 查资源
        Resource resource = resourceService.getById(resourceId);
        if (null == resource) {
            throw new EntityNotFoundException(Resource.class, "id", resourceId);
        }
        List<ImportTask> importTaskList = this.list(Wrappers.<ImportTask>lambdaQuery().eq(ImportTask::getResourceId, resourceId));
        List<ImportTaskDTO> result = Lists.newArrayListWithCapacity(importTaskList.size());
        if (CollectionUtils.isNotEmpty(importTaskList)) {
            List<Long> ruleSetIdList = importTaskList.stream().map(ImportTask::getRuleSetId).collect(Collectors.toList());
            List<RuleSet> ruleSets = ruleSetService.listByIds(ruleSetIdList);
            Map<Long, String> ruleSetMap = ruleSets.stream().collect(Collectors.toMap(RuleSet::getId, RuleSet::getName));
            importTaskList.forEach(importTask -> {
                ImportTaskDTO importTaskDTO = new ImportTaskDTO();
                BeanUtils.copyProperties(importTask, importTaskDTO);
                importTaskDTO.setRuleSetName(ruleSetMap.get(importTask.getRuleSetId()));
                result.add(importTaskDTO);
            });
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public void downloadFile(String fileName, HttpServletRequest request, HttpServletResponse response) {
        String path = properties.getPath().getPath() + FileUtil.TXT + File.separator + fileName;
        FileUtil.downloadFile(request, response, new File(path), false);
    }

    /**
     * 处理失败任务
     * @param id
     */
    private void dealFail(Long id) {
        this.update(Wrappers.<ImportTask>lambdaUpdate()
                .eq(ImportTask::getId, id)
                .set(ImportTask::getStatus, ImportTaskStatusEnum.IMPORT_FAIL.getValue()));
    }
}
