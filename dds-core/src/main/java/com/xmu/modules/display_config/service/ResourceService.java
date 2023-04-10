package com.xmu.modules.display_config.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xmu.modules.display_config.domain.Resource;
import com.xmu.modules.display_config.request.*;
import com.xmu.modules.display_config.response.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author Xing
 */
public interface ResourceService extends IService<Resource> {

    /**
     * 根据项目id获取数据资源
     * @param projectId
     * @return
     */
    List<ResourceDTO> getResources(Long projectId);

    Object getColumns(Long resourceId, boolean useId);
//    Object getColumns(Long resourceId);

    /**
     * 获取检索结果列表字段配置信息
     * @param resourceId
     * @return
     */
    List<SearchFieldDTO> getSearchFields(Long resourceId);

    /**
     * 获取检索结果详情字段配置信息
     * @param resourceId
     * @return
     */
    List<DetailFieldDTO> getDetailFields(Long resourceId);

    /**
     * 高级检索
     * @param advancedSearchResource
     * @return
     */
    SearchResultDTO advancedSearch(AdvancedSearchResourceDTO advancedSearchResource);

    /**
     * 获取数据详情
     * @param resourceId
     * @param id
     * @return
     */
    List<JSONObject> getDetail(Long resourceId, Long id);

    /**
     * 获取高级检索配置信息
     * @param projectId
     * @param type
     * @return
     */
    List<AdvancedSearchConfigDTO> getAdvancedSearch(Long projectId, Long type);

    /**
     * 获取检索结果分析字段
     * @param resourceId
     * @return
     */
    List<SearchStatisticsFieldDTO> getSearchStatisticsFields(Long resourceId);

    /**
     * 获取高级统计结果
     * @param advancedStatistics
     * @return
     */
    List<StatisticsResultDTO> advancedStatistics(AdvancedStatisticsDTO advancedStatistics);

    /**
     * 统计属性数据数量
     * @param tableName
     * @param enumerationKey
     * @param enumerationValue
     * @return
     */
    Integer countResourceData(String tableName, String enumerationKey, String enumerationValue);

    /**
     * 创建新属性
     * @param tableName
     * @param property
     * @param type
     */
    void createResourceProperty(String tableName, String property, Integer type);

    /**
     * 更新属性
     * @param tableName
     * @param oldProperty
     * @param newProperty
     * @param type
     */
    void updateResourceProperty(String tableName, String oldProperty, String newProperty, Integer type);

    /**
     * 删除属性
     * @param tableName
     * @param property
     */
    void deleteResourceProperty(String tableName, String property);

    /**
     * 导入数据
     * @param importDataDTO
     * @return
     */
    ResponseEntity importData(importDataDTO importDataDTO);

    /**
     * 保存导入数据
     * @param tableName
     * @param importData
     * @return
     */
    Integer saveImportData(String tableName, Map<String, Object> importData);

    Object pageRet(PageDTO page);

    /**
     * 导出数据
     * @param exportDataDTO
     * @param request
     * @param response
     */
    void exportData(ExportDataDTO exportDataDTO, HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据sql获取多条数据
     * @param sql
     * @return
     */
    List<Map<String, Object>> listDataBySql(String sql);

    /**
     * 获取精炼结果
     * @param target
     * @param aggregationField
     * @param wrapper
     * @return
     */
    List<ESStatisticsResultDTO> getEsStatisticsResults(String target, String aggregationField, QueryWrapper wrapper);

    /**
     * 获取统计字段值
     * @param target
     * @param aggregationField
     * @param wrapper
     * @return
     */
    List<String> getFieldStatistic(String target, String aggregationField, QueryWrapper wrapper);

    /**
     * 创建资源
     * @param projectId
     * @param resourceDTO
     * @return
     */
    ResponseEntity addResources(Long projectId, ResourceInfoDTO resourceDTO);

    /**
     * 删除资源
     * @param resource
     */
    void removeResources(Resource resource);

    /**
     * 修改表名
     * @param resourceId
     * @param resourceDTO
     */
    void updateResource(Long resourceId, ResourceInfoDTO resourceDTO);
}
