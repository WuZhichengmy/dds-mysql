package com.xmu.modules.display_config.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmu.exception.EntityNotFoundException;
import com.xmu.modules.display_config.domain.*;
import com.xmu.modules.display_config.mapper.SearchConfigMapper;
import com.xmu.modules.display_config.request.DetailFieldsDTO;
import com.xmu.modules.display_config.request.SearchConfigDTO;
import com.xmu.modules.display_config.request.SearchFieldsDTO;
import com.xmu.modules.display_config.request.StatisticsFieldsDTO;
import com.xmu.modules.display_config.response.AdvancedSearchFieldDTO;
import com.xmu.modules.display_config.service.*;
import com.xmu.modules.display_config.utils.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author Xing
 */
@Service
public class SearchConfigServiceImpl extends ServiceImpl<SearchConfigMapper, SearchConfig> implements SearchConfigService {

    @Autowired
    private SearchConfigService advancedFieldService;

    @Autowired
    private SearchFieldsConfigService searchFieldsService;

    @Autowired
    private SearchResultStatisticsService statisticsService;

    @Autowired
    private DetailFieldsService detailFieldsService;

    @Autowired
    private ProjectService projectService;

    /**
     * 四种检索配置
     */
    @Override
    public Object searchConfig(Long projectId, List<SearchConfigDTO> searchConfigDTOS) {

        AtomicBoolean advanced = new AtomicBoolean(false);
        AtomicBoolean fieldB = new AtomicBoolean(false);
        AtomicBoolean staticsB = new AtomicBoolean(false);
        AtomicBoolean detailB = new AtomicBoolean(false);
        searchConfigDTOS.forEach(searchConfig -> {
            Long resourceId = searchConfig.getResourceId();
            //TODO:根据ID查一下,如果有就更新不要save
            SearchConfig advanceGet
                    = advancedFieldService.getOne(Wrappers.<SearchConfig>lambdaQuery().eq(SearchConfig::getResourceId, resourceId));
            List<SearchFieldsConfig> fieldsGet
                    = searchFieldsService.list(Wrappers.<SearchFieldsConfig>lambdaQuery().eq(SearchFieldsConfig::getResourceId, resourceId));
            List<SearchResultStatistics> statisticsGet
                    = statisticsService.list(Wrappers.<SearchResultStatistics>lambdaQuery().eq(SearchResultStatistics::getResourceId, resourceId));
            List<DetailFields> detailsGet
                    = detailFieldsService.list(Wrappers.<DetailFields>lambdaQuery().eq(DetailFields::getResourceId, resourceId));
            List<AdvancedSearchFieldDTO> advancedSearch = searchConfig.getSearch();
            //高级检索字段更新
            if (advanceGet != null) {
                if (advancedSearch != null) {
                    advanceGet.setFields(JSONObject.toJSONString(advancedSearch));
                    advanced.set(advancedFieldService.updateById(advanceGet));
                }
            } else {
                //高级检索字段保存
                if (advancedSearch == null) {
                    advanced.set(true);
                } else {
                    SearchConfig advancedField = new SearchConfig(advancedSearch, projectId, resourceId);
                    advanced.set(advancedFieldService.save(advancedField));
                }
            }
            if (!fieldsGet.isEmpty()) {
                fieldsGet.forEach(fieldsConfig -> searchFieldsService.removeById(fieldsConfig.getId()));
            }
            List<SearchFieldsDTO> fields = searchConfig.getFields();
            if (fields.isEmpty()) {
                fieldB.set(true);
            } else {
                fields.forEach(field -> {
                    SearchFieldsConfig searchFieldsConfig = new SearchFieldsConfig(field, projectId, resourceId);
                    fieldB.set(searchFieldsService.save(searchFieldsConfig));
                });
            }
            if (!statisticsGet.isEmpty()) {
                statisticsGet.forEach(statistics -> statisticsService.removeById(statistics.getId()));
            }
            List<StatisticsFieldsDTO> statistics = searchConfig.getStatistics();
            if (statistics.isEmpty()) {
                staticsB.set(true);
            } else {
                statistics.forEach(statistic -> {
                    SearchResultStatistics resultStatistics = new SearchResultStatistics(statistic, projectId, resourceId);
                    staticsB.set(statisticsService.save(resultStatistics));
                });
            }
            if (!detailsGet.isEmpty()) {
                detailsGet.forEach(detailFields -> detailFieldsService.removeById(detailFields.getId()));
            }
            List<DetailFieldsDTO> details = searchConfig.getDetails();
            if (details.isEmpty()) {
                detailB.set(true);
            } else {
                details.forEach(detail -> {
                    DetailFields detailFields = new DetailFields(detail, projectId, resourceId);
                    detailB.set(detailFieldsService.save(detailFields));
                });
            }

        });
        if (advanced.get() && fieldB.get() && staticsB.get() && detailB.get()) {
            return new Response<>().setData(true);
        } else {
            return new Response<>().setMsg("search widget save failed").setCode(500);
        }
    }

    /**
     * 获取检索配置
     */
    @Override
    public Object getSearchConfig(Long projectId, Long resourceId) {
        Project project = projectService.getById(projectId);
        if (project == null) {
            throw new EntityNotFoundException(Project.class, "id", projectId);
        }
        SearchConfig advanceConfig
                = this.advancedFieldService.getOne(new LambdaQueryWrapper<SearchConfig>().eq(SearchConfig::getResourceId, resourceId));
        if (advanceConfig == null) {
            throw new EntityNotFoundException(SearchConfig.class, "resource", resourceId);
        }
        List<AdvancedSearchFieldDTO> search = advanceConfig.toConfigDTO();
        List<SearchFieldsConfig> fieldsConfig
                = this.searchFieldsService.list(new LambdaQueryWrapper<SearchFieldsConfig>().eq(SearchFieldsConfig::getResourceId, resourceId));
        if (fieldsConfig.isEmpty()) {
            throw new EntityNotFoundException(SearchFieldsConfig.class, "resource", resourceId);
        }
        List<SearchFieldsDTO> fields = fieldsConfig.stream().map(SearchFieldsDTO::new).collect(Collectors.toList());
        List<SearchResultStatistics> statisticsConfig
                = this.statisticsService.list(new LambdaQueryWrapper<SearchResultStatistics>().eq(SearchResultStatistics::getResourceId, resourceId));
        if (statisticsConfig.isEmpty()) {
            throw new EntityNotFoundException(SearchResultStatistics.class, "resource", resourceId);
        }
        List<StatisticsFieldsDTO> statistics = statisticsConfig.stream().map(StatisticsFieldsDTO::new).collect(Collectors.toList());
        List<DetailFields> detailConfig
                = this.detailFieldsService.list(new LambdaQueryWrapper<DetailFields>().eq(DetailFields::getResourceId, resourceId));
        if (detailConfig.isEmpty()) {
            throw new EntityNotFoundException(DetailFields.class, "resource", resourceId);
        }
        List<DetailFieldsDTO> details = detailConfig.stream().map(DetailFieldsDTO::new).collect(Collectors.toList());

        SearchConfigDTO searchConfig = new SearchConfigDTO()
                .setSearch(search)
                .setFields(fields)
                .setStatistics(statistics)
                .setResourceId(resourceId)
                .setDetails(details);
        return new Response<>().setData(searchConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteConfig(Long resourceId) {
        // 检索字段配置
        List<SearchConfig> searchConfigs = this.list(Wrappers.<SearchConfig>lambdaQuery().eq(SearchConfig::getResourceId, resourceId));
        List<Long> searchConfigIds = searchConfigs.stream().map(SearchConfig::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(searchConfigIds)) {
            this.removeByIds(searchConfigIds);
        }
        // 检索结果页字段配置
        List<SearchFieldsConfig> fields = searchFieldsService.list(Wrappers.<SearchFieldsConfig>lambdaQuery().eq(SearchFieldsConfig::getResourceId, resourceId));
        List<Long> fieldIds = fields.stream().map(SearchFieldsConfig::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(fieldIds)) {
            searchFieldsService.removeByIds(fieldIds);
        }
        // 精炼字段配置
        List<SearchResultStatistics> statistics = statisticsService.list(Wrappers.<SearchResultStatistics>lambdaQuery().eq(SearchResultStatistics::getResourceId, resourceId));
        List<Long> statisticsIds = statistics.stream().map(SearchResultStatistics::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(statisticsIds)) {
            statisticsService.removeByIds(statisticsIds);
        }
        // 详情页字段配置
        List<DetailFields> detailFields = detailFieldsService.list(Wrappers.<DetailFields>lambdaQuery().eq(DetailFields::getResourceId, resourceId));
        List<Long> detailIds = detailFields.stream().map(DetailFields::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(detailIds)) {
            detailFieldsService.removeByIds(detailIds);
        }
    }

}


