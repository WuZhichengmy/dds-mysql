package com.xmu.modules.display_config.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xmu.modules.display_config.domain.Test;
import com.xmu.modules.display_config.request.AdvancedSearchResourceDTO;
import com.xmu.modules.display_config.request.AdvancedStatisticsDTO;
import com.xmu.modules.display_config.response.ESStatisticsResultDTO;
import com.xmu.modules.display_config.response.StatisticsResultDTO;

import java.util.List;

/**
 * @author Xing
 */
public interface SearchService extends IService<Test> {

    /**
     * 构建检索构造器
     * @param advancedSearchResource 检索条件
     * @param target                 表名
     * @param fields                 检索字段
     * @return
     */
    QueryWrapper handlerWrapper(AdvancedSearchResourceDTO advancedSearchResource, String target, List<String> fields);

    /**
     * 构建精炼构造器
     * @param advancedSearchResource 检索条件
     * @param target                 表名
     * @param field                  检索字段
     * @param limit                  返回结果数
     * @return
     */
    QueryWrapper handlerStatisticsWrapper(AdvancedSearchResourceDTO advancedSearchResource, String target, String field, Integer limit);

    /**
     * 精炼
     * @param advancedSearchResource
     * @param target
     * @param field
     * @param limit
     * @return
     */
    List<ESStatisticsResultDTO> esStatisticsResults(AdvancedSearchResourceDTO advancedSearchResource, String target, String field, Integer limit);

    /**
     * 统计
     * @param advancedStatistics 检索条件
     * @param target             表名
     * @return
     */
    List<StatisticsResultDTO> advancedStatistics(AdvancedStatisticsDTO advancedStatistics, String target);
}
