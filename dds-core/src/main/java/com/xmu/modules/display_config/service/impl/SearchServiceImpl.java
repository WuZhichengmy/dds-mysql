package com.xmu.modules.display_config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xmu.modules.display_config.domain.Test;
import com.xmu.modules.display_config.enums.LogicEnum;
import com.xmu.modules.display_config.enums.OrderEnum;
import com.xmu.modules.display_config.mapper.TestMapper;
import com.xmu.modules.display_config.request.AdvancedSearchResourceDTO;
import com.xmu.modules.display_config.request.AdvancedStatisticsDTO;
import com.xmu.modules.display_config.response.ESStatisticsResultDTO;
import com.xmu.modules.display_config.response.StatisticsResultDTO;
import com.xmu.modules.display_config.service.ResourceService;
import com.xmu.modules.display_config.service.SearchService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xing
 */
@Service
public class SearchServiceImpl extends ServiceImpl<TestMapper, Test> implements SearchService {

    @Autowired
    protected ResourceService resourceService;

    /**
     * 构建检索构造器
     * @param advancedSearchResource 检索条件
     * @param target                 表名
     * @param fields                 检索字段
     * @return
     */
    @Override
    public QueryWrapper handlerWrapper(AdvancedSearchResourceDTO advancedSearchResource, String target, List<String> fields) {
        QueryWrapper<Bean> wrapper = Wrappers.query();
        // 检索字段
        if (CollectionUtils.isNotEmpty(fields)) {
            fields.add("id");
            wrapper.select(fields.toArray(new String[0]));
        }
        // todo: 扩展检索条件
        updateWrapper(advancedSearchResource, wrapper);
        // 排序
        if (StringUtils.isNotBlank(advancedSearchResource.getOrderColumn())) {
            wrapper.orderBy(StringUtils.isNotBlank(advancedSearchResource.getOrderType()),
                    OrderEnum.ASC.name().equals(advancedSearchResource.getOrderType()), advancedSearchResource.getOrderColumn());
        }
        return wrapper;
    }

    /**
     * 构建精炼构造器
     * @param advancedSearchResource 检索条件
     * @param target                 表名
     * @param field                  检索字段
     * @param limit                  返回结果数
     * @return
     */
    @Override
    public QueryWrapper handlerStatisticsWrapper(AdvancedSearchResourceDTO advancedSearchResource, String target, String field, Integer limit) {
        QueryWrapper<Bean> wrapper = Wrappers.query();
        // todo: 扩展检索条件
        updateWrapper(advancedSearchResource, wrapper);
        // 过滤精炼结果为空
        wrapper.and(andWrapper -> andWrapper.isNotNull(field).ne(field, ""));
        wrapper.groupBy(field);
        wrapper.orderByDesc("count( id )");
        wrapper.last("limit " + limit);
        return wrapper;
    }

    private void updateWrapper(AdvancedSearchResourceDTO advancedSearchResource, QueryWrapper<Bean> wrapper) {
        if (CollectionUtils.isNotEmpty(advancedSearchResource.getSearchLogic())) {
            wrapper.and(andWrapper -> {
                advancedSearchResource.getSearchLogic().forEach(searchLogicDTO -> {
                    if (StringUtils.isBlank(searchLogicDTO.getKeyword())) {
                        return;
                    }

                    if (LogicEnum.AND.getValue().equals(searchLogicDTO.getLogic())) {
                        andWrapper.like(searchLogicDTO.getField(), searchLogicDTO.getKeyword());
                    }
                    if (LogicEnum.OR.getValue().equals(searchLogicDTO.getLogic())) {
                        andWrapper.or();
                        andWrapper.like(searchLogicDTO.getField(), searchLogicDTO.getKeyword());
                    }
                    if (LogicEnum.NOT.getValue().equals(searchLogicDTO.getLogic())) {
                        andWrapper.notLike(searchLogicDTO.getField(), searchLogicDTO.getKeyword());
                    }
                });
            });
        }
        // 精炼
        if (CollectionUtils.isNotEmpty(advancedSearchResource.getRefines())) {
            advancedSearchResource.getRefines().forEach(refine -> {
                StringBuffer sb = new StringBuffer();
                sb.append(refine.getField())
                        .append(" REGEXP '")
                        .append(String.join("|", refine.getValues()))
                        .append("'");
                wrapper.apply(sb.toString());
            });
        }
    }

    @Override
    public List<ESStatisticsResultDTO> esStatisticsResults(AdvancedSearchResourceDTO advancedSearchResource, String target, String field, Integer limit) {
        QueryWrapper queryWrapper = this.handlerStatisticsWrapper(advancedSearchResource, target, field, limit);
        if (null == queryWrapper) {
            return null;
        }
        return resourceService.getEsStatisticsResults(target, field, queryWrapper);
    }

    @Override
    public List<StatisticsResultDTO> advancedStatistics(AdvancedStatisticsDTO advancedStatistics, String target) {
        AdvancedSearchResourceDTO advancedSearchResourceDTO = new AdvancedSearchResourceDTO();
        BeanUtils.copyProperties(advancedStatistics, advancedSearchResourceDTO);
        QueryWrapper wrapper = this.handlerStatisticsWrapper(advancedSearchResourceDTO, target, advancedStatistics.getAggregationField(), 10);
        if (null == wrapper) {
            return null;
        }
        List<ESStatisticsResultDTO> esStatisticsResults = resourceService.getEsStatisticsResults(target, advancedStatistics.getAggregationField(), wrapper);
        return this.getStatisticsResult(esStatisticsResults);
    }

    /**
     * 获取统计结果
     * @param esStatisticsResultList
     * @return
     */
    protected List<StatisticsResultDTO> getStatisticsResult(List<ESStatisticsResultDTO> esStatisticsResultList) {
        List<StatisticsResultDTO> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(esStatisticsResultList)) {
            // 计算总数
            Long total = esStatisticsResultList.stream().mapToLong(ESStatisticsResultDTO::getValue).sum();
            esStatisticsResultList.forEach(esStatisticsResultDTO -> {
                StatisticsResultDTO statisticsResult = new StatisticsResultDTO();
                BeanUtils.copyProperties(esStatisticsResultDTO, statisticsResult);
                // 计算比率
                statisticsResult.setRate(String.format("%.2f", ((esStatisticsResultDTO.getValue().doubleValue() / total.doubleValue()) * 100)) + "%");
                list.add(statisticsResult);
            });
        }
        return list;
    }
}
