package com.xmu.modules.display_config.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xmu.exception.BadRequestException;
import com.xmu.exception.EntityNotFoundException;
import com.xmu.modules.display_config.config.ServiceConfig;
import com.xmu.modules.display_config.domain.*;
import com.xmu.modules.display_config.enums.SearchTypeEnum;
import com.xmu.modules.display_config.mapper.ResourceMapper;
import com.xmu.modules.display_config.request.*;
import com.xmu.modules.display_config.response.*;
import com.xmu.modules.display_config.service.*;
import com.xmu.modules.display_config.utils.ExcelUtils;
import com.xmu.modules.display_config.utils.Response;
import com.xmu.modules.display_config.utils.SqlUtil;
import com.xmu.utils.SnowFlakeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Xing
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private SearchFieldsConfigService searchFieldsConfigService;
    @Autowired
    private DetailFieldsService detailFieldsService;
    @Autowired
    private SearchConfigService searchConfigService;
    @Autowired
    private SearchResultStatisticsService searchResultStatisticsService;
    @Autowired
    private DisplayWidgetsService displayWidgetsService;
    @Autowired
    private ImportTaskService importTaskService;
    @Autowired
    private ServiceConfig serviceConfig;
    @Autowired
    @Lazy
    private RuleSetService ruleSetService;
    @Autowired
    private ImportPropertyService importPropertyService;
    @Autowired
    private AnalysisMenuService analysisMenuService;
    @Autowired
    private ResourcePropertyService resourcePropertyService;

    @Override
    public List<ResourceDTO> getResources(Long projectId) {
        // 查找项目
        Project project = projectService.getById(projectId);
        if (null == project) {
            throw new EntityNotFoundException(Project.class, "id", projectId);
        }
        List<ResourceDTO> resourceDTOList = Lists.newArrayList();
        // 查找项目配置的数据资源
        List<Resource> resourceList = this.list(Wrappers.<Resource>lambdaQuery().eq(Resource::getProjectId, projectId));
        if (CollectionUtils.isNotEmpty(resourceList)) {
            resourceList.forEach(resource -> {
                ResourceDTO resourceDTO = new ResourceDTO();
                BeanUtils.copyProperties(resource, resourceDTO);
                resourceDTOList.add(resourceDTO);
            });
        }
        return resourceDTOList;
    }

    @Override
    public Object getColumns(Long resourceId, boolean useId) {
        return displayWidgetsService.getColumns(resourceId, useId);
    }

    @Override
    public List<SearchFieldDTO> getSearchFields(Long resourceId) {
        List<SearchFieldsConfig> list = searchFieldsConfigService.list(Wrappers.<SearchFieldsConfig>lambdaQuery().eq(SearchFieldsConfig::getResourceId, resourceId));
        List<SearchFieldDTO> searchFieldList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(searchFieldsConfig -> {
                SearchFieldDTO searchFieldDTO = new SearchFieldDTO();
                BeanUtils.copyProperties(searchFieldsConfig, searchFieldDTO);
                searchFieldList.add(searchFieldDTO);
            });
        }
        return searchFieldList;
    }

    @Override
    public List<DetailFieldDTO> getDetailFields(Long resourceId) {
        List<DetailFields> list = detailFieldsService.list(Wrappers.<DetailFields>lambdaQuery().eq(DetailFields::getResourceId, resourceId));
        List<DetailFieldDTO> detailFieldList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(detailFields -> {
                DetailFieldDTO detailFieldDTO = new DetailFieldDTO();
                BeanUtils.copyProperties(detailFields, detailFieldDTO);
                detailFieldList.add(detailFieldDTO);
            });
        }
        return detailFieldList;
    }

    @Override
    public SearchResultDTO advancedSearch(AdvancedSearchResourceDTO advancedSearchResource) {
        // 设置默认排序字段
        if (StringUtils.isBlank(advancedSearchResource.getOrderColumn())) {
            advancedSearchResource.setOrderColumn("id");
        }
        // 查找资源
        Resource resource = this.getById(advancedSearchResource.getResourceId());
        if (null == resource) {
            throw new EntityNotFoundException(Resource.class, "id", advancedSearchResource.getResourceId());
        }
        // 查找检索返回的字段
        List<SearchFieldsConfig> searchFieldsConfigList = searchFieldsConfigService.list(Wrappers.<SearchFieldsConfig>lambdaQuery()
                .eq(SearchFieldsConfig::getResourceId, advancedSearchResource.getResourceId()));
        List<String> fields = searchFieldsConfigList.stream().map(SearchFieldsConfig::getName).collect(Collectors.toList());
        SearchService searchService = serviceConfig.getService(resource.getEntity());
        // 检索
        QueryWrapper wrapper = searchService.handlerWrapper(advancedSearchResource, resource.getTarget(), fields);
        SearchResultDTO searchResultDTO = new SearchResultDTO();
        if (null == wrapper) {
            searchResultDTO.setSearchResults(Lists.newArrayList(), 0L);
            return searchResultDTO;
        }
        Page page = (Page) searchService.page(new Page<>(advancedSearchResource.getPageNo(), advancedSearchResource.getPageSize()), wrapper);
        searchResultDTO.setSearchResults(page.getRecords(), page.getTotal());
        // 精炼字段
        List<SearchResultStatistics> searchResultStatistics = searchResultStatisticsService.list(Wrappers.<SearchResultStatistics>lambdaQuery()
                .eq(SearchResultStatistics::getResourceId, advancedSearchResource.getResourceId()));
        if (CollectionUtils.isEmpty(searchResultStatistics)) {
            return searchResultDTO;
        }
        List<RefineResultDTO> refineResults = Lists.newArrayListWithCapacity(searchResultStatistics.size());
        searchResultStatistics.forEach(statistics -> {
            List<ESStatisticsResultDTO> esStatisticsResults = searchService.esStatisticsResults(advancedSearchResource, resource.getTarget(), statistics.getField(), statistics.getSize());
            if (CollectionUtils.isEmpty(esStatisticsResults)) {
                return;
            }
            refineResults.add(new RefineResultDTO(statistics.getTitle(), statistics.getField(), esStatisticsResults));
        });
        searchResultDTO.setRefineResults(refineResults);
        return searchResultDTO;
    }

    @Override
    public List<JSONObject> getDetail(Long resourceId, Long id) {
        // 查找资源
        Resource resource = this.getById(resourceId);
        if (null == resource) {
            throw new EntityNotFoundException(Resource.class, "id", resourceId);
        }
        return this.getBaseMapper().listTargetResourceByIds(resource.getTarget(), Lists.newArrayList(id));
    }

    @Override
    public List<AdvancedSearchConfigDTO> getAdvancedSearch(Long projectId, Long type) {
        // 查找检索配置
        List<SearchConfig> searchConfigs = searchConfigService.list(Wrappers.<SearchConfig>lambdaQuery()
                .eq(SearchConfig::getProjectId, projectId).eq(SearchConfig::getType, type));
        if (CollectionUtils.isEmpty(searchConfigs)) {
            throw new BadRequestException("检索信息不存在!");
        }
        // 查找对应资源
        List<Long> resourceIds = searchConfigs.stream().map(SearchConfig::getResourceId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(resourceIds)) {
            throw new BadRequestException("检索配置资源不存在!");
        }
        List<Resource> resources = this.getBaseMapper().selectBatchIds(resourceIds);
        // 转map
        Map<Long, SearchConfig> searchConfigMap = searchConfigs.stream().collect(Collectors.toMap(SearchConfig::getResourceId, a -> a, (k1, k2) -> k1));
        List<AdvancedSearchConfigDTO> list = Lists.newArrayList();
        resources.forEach(resource -> {
            AdvancedSearchConfigDTO advancedSearchConfigDTO = new AdvancedSearchConfigDTO();
            advancedSearchConfigDTO.setResourceId(resource.getId());
            advancedSearchConfigDTO.setName(resource.getName());
            SearchConfig searchConfig = searchConfigMap.get(resource.getId());
            if (null == searchConfig) {
                return;
            }
            advancedSearchConfigDTO.setAdvancedSearchFields(JSONObject.parseArray(searchConfig.getFields(), AdvancedSearchFieldDTO.class));
            list.add(advancedSearchConfigDTO);
        });
        return list;
    }

    @Override
    public List<SearchStatisticsFieldDTO> getSearchStatisticsFields(Long resourceId) {
        List<SearchResultStatistics> searchResultStatisticsList = searchResultStatisticsService.list(Wrappers.<SearchResultStatistics>lambdaQuery().eq(SearchResultStatistics::getResourceId, resourceId));
        List<SearchStatisticsFieldDTO> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(searchResultStatisticsList)) {
            searchResultStatisticsList.forEach(searchResultStatistics -> {
                SearchStatisticsFieldDTO simpleField = new SearchStatisticsFieldDTO();
                simpleField.setName(searchResultStatistics.getField());
                simpleField.setLabel(searchResultStatistics.getTitle());
                list.add(simpleField);
            });
        }
        return list;
    }

    @Override
    public List<StatisticsResultDTO> advancedStatistics(AdvancedStatisticsDTO advancedStatistics) {
        // 查找资源
        Resource resource = this.getById(advancedStatistics.getResourceId());
        if (null == resource) {
            throw new EntityNotFoundException(Resource.class, "id", advancedStatistics.getResourceId());
        }
        return serviceConfig.getService(resource.getEntity()).advancedStatistics(advancedStatistics, resource.getTarget());
    }

    @Override
    public Integer countResourceData(String tableName, String enumerationKey, String enumerationValue) {
        return baseMapper.countResourceData(tableName, enumerationKey, enumerationValue);
    }

    @Override
    public void createResourceProperty(String tableName, String property, Integer type) {
        this.baseMapper.createResourceProperty(tableName, property, type);
    }

    @Override
    public void updateResourceProperty(String tableName, String oldProperty, String newProperty, Integer type) {
        this.baseMapper.updateResourceProperty(tableName, oldProperty, newProperty, type);
    }

    @Override
    public void deleteResourceProperty(String tableName, String property) {
        this.baseMapper.deleteResourceProperty(tableName, property);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity importData(importDataDTO importDataDTO) {
        Long importTaskId = importTaskService.saveImportTask(importDataDTO);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                importTaskService.importData(importTaskId);
            }
        });
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveImportData(String tableName, Map<String, Object> importData) {
        return this.getBaseMapper().saveImportData(tableName, importData);
    }

    @Override
    public Object pageRet(PageDTO page) {
        String keyword = page.getKeyword();
        String field = page.getField();
        Long resourceId = page.getResourceId();
        Resource resource = this.getById(resourceId);
        String table = resource.getTarget();
        String sql = SqlUtil.like(table, field, keyword);
        Page<Map<String, Object>> aPage = new Page<>(page.getCurrent(), page.getPageSize());
        return this.displayWidgetsService.selectByPage(aPage, sql);
    }

    private void createTable(String tableName) {
        this.getBaseMapper().creatTable(tableName);
    }

    private void dropTable(String tableName) {
        this.getBaseMapper().dropTable(tableName);
    }

    @Override
    public void exportData(ExportDataDTO exportDataDTO, HttpServletRequest request, HttpServletResponse response) {
        // 查找资源
        Resource resource = this.getById(exportDataDTO.getResourceId());
        if (null == resource) {
            throw new EntityNotFoundException(Resource.class, "id", exportDataDTO.getResourceId());
        }
        // 查规则集
        List<RuleSet> ruleSetList = ruleSetService.list(Wrappers.<RuleSet>lambdaQuery()
                .eq(RuleSet::getResourceId, resource.getId()));
        if (CollectionUtils.isEmpty(ruleSetList)) {
            throw new BadRequestException("规则集不存在!");
        }
        // 默认使用第一个规则集
        List<ImportPropertyDTO> importPropertyList = importPropertyService.listImportProperty(ruleSetList.get(0).getId());
        List<JSONObject> jsonList = this.getBaseMapper().listTargetResourceByIds(resource.getTarget(), exportDataDTO.getIds());
        ExcelDataDTO excelDataDTO = ExcelUtils.handlerExcelData(importPropertyList, jsonList);
        try {
            String fileName = URLEncoder.encode(resource.getName() + "-" + SnowFlakeUtil.getFlowIdInstance().nextId() + ".xlsx", "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 将文件名放到 header 给前端拿
            response.setHeader("filename", fileName);
            response.setHeader("Access-Control-Expose-Headers", "filename");
            EasyExcel.write(response.getOutputStream()).head(excelDataDTO.getHead()).sheet("Sheet0").doWrite(excelDataDTO.getData());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> listDataBySql(String sql) {
        return this.getBaseMapper().listDataBySql(sql);
    }

    @Override
    public List<ESStatisticsResultDTO> getEsStatisticsResults(String target, String aggregationField, QueryWrapper wrapper) {
        return this.getBaseMapper().getEsStatisticsResults(target, aggregationField, wrapper);
    }

    @Override
    public List<String> getFieldStatistic(String target, String aggregationField, QueryWrapper wrapper) {
        return this.getBaseMapper().getFieldStatistic(target, aggregationField, wrapper);
    }

    @Override
    public ResponseEntity addResources(Long projectId, ResourceInfoDTO resourceDTO) {
        String target = resourceDTO.getTarget();
        Resource resourceQuery = this.getOne(Wrappers.<Resource>lambdaQuery().eq(Resource::getTarget, target));
        if (resourceQuery != null) {
            throw new BadRequestException("表名已存在!");
        }
        Resource resource = new Resource();
        BeanUtils.copyProperties(resourceDTO, resource);
        resource.setId(SnowFlakeUtil.getFlowIdInstance().nextId());
        resource.setProjectId(projectId);
        // 创建searchConfig
        SearchConfig searchConfig = new SearchConfig();
        searchConfig.setId(SnowFlakeUtil.getFlowIdInstance().nextId());
        searchConfig.setProjectId(projectId);
        searchConfig.setResourceId(resource.getId());
        searchConfig.setType(SearchTypeEnum.ES.getValue());
        searchConfig.setFields("[]");

        if (this.save(resource)) {
            this.createTable(target);
            searchConfigService.save(searchConfig);
            return new ResponseEntity<>(new Response<>().setMsg("resource and table created"), HttpStatus.CREATED);
        }
        throw new RuntimeException("resource list saved failed");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeResources(Resource resource) {
        // 删除分析配置
        analysisMenuService.deleteConfig(resource.getProjectId(), resource.getId());
        // 删除资源检索信息配置
        searchConfigService.deleteConfig(resource.getId());
        // 删除导入规则集
        List<RuleSet> ruleSetList = ruleSetService.list(Wrappers.<RuleSet>lambdaQuery()
                .eq(RuleSet::getResourceId, resource.getId()));
        if (CollectionUtils.isNotEmpty(ruleSetList)) {
            List<Long> ruleSetIds = ruleSetList.stream().map(RuleSet::getId).collect(Collectors.toList());
            importPropertyService.remove(Wrappers.<ImportProperty>lambdaQuery()
                    .in(ImportProperty::getRuleSetId, ruleSetIds));
        }
        // 删除资源属性
        resourcePropertyService.remove(Wrappers.<ResourceProperty>lambdaQuery()
                .eq(ResourceProperty::getResourceId, resource.getId()));
        // 删除导入任务
        importTaskService.remove(Wrappers.<ImportTask>lambdaQuery()
                .eq(ImportTask::getResourceId, resource.getId()));
        // 删除表
        this.dropTable(resource.getTarget());
        // 删除资源
        this.removeById(resource.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateResource(Long resourceId, ResourceInfoDTO resourceDTO) {
        // 查找资源
        Resource resource = this.getById(resourceId);
        if (null == resource) {
            throw new EntityNotFoundException(Resource.class, "id", resourceId);
        }
        // 修改表名
        if (!resourceDTO.getTarget().equals(resource.getTarget())) {
            Resource target = this.getOne(Wrappers.<Resource>lambdaQuery()
                    .eq(Resource::getTarget, resourceDTO.getTarget()));
            if (null != target) {
                throw new BadRequestException(resourceDTO.getTarget() + " is exist!");
            }
            this.getBaseMapper().updateTableName(resource.getTarget(), resourceDTO.getTarget());
        }
        BeanUtils.copyProperties(resourceDTO, resource);
        this.updateById(resource);
    }

}
