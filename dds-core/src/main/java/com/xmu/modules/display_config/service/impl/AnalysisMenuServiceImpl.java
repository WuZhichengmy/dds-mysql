package com.xmu.modules.display_config.service.impl;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.Tuple;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xmu.exception.EntityNotFoundException;
import com.xmu.model.IdEntity;
import com.xmu.modules.display_config.config.ServiceConfig;
import com.xmu.modules.display_config.constant.Constants;
import com.xmu.modules.display_config.domain.AnalysisDetail;
import com.xmu.modules.display_config.domain.AnalysisMenu;
import com.xmu.modules.display_config.domain.Project;
import com.xmu.modules.display_config.domain.Resource;
import com.xmu.modules.display_config.enums.ChartTypeEnum;
import com.xmu.modules.display_config.mapper.AnalysisMenuMapper;
import com.xmu.modules.display_config.request.*;
import com.xmu.modules.display_config.response.AnalysisWidgetDTO;
import com.xmu.modules.display_config.response.Component;
import com.xmu.modules.display_config.response.chart.*;
import com.xmu.modules.display_config.service.*;
import com.xmu.modules.display_config.utils.GraphBuilder;
import com.xmu.modules.display_config.utils.MultiPartDatasetBuilder;
import com.xmu.modules.display_config.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Xing
 */
@Service
@Slf4j
public class AnalysisMenuServiceImpl extends ServiceImpl<AnalysisMenuMapper, AnalysisMenu> implements AnalysisMenuService {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private AnalysisDetailService analysisDetailService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ServiceConfig serviceConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object menuConfig(Long projectId, List<MenuConfigDTO> configDTOs) {
        Project project = projectService.getById(projectId);
        if (null == project) {
            throw new EntityNotFoundException(Project.class, "id", projectId);
        }
        List<AnalysisMenu> menus = configDTOs.stream().map(configDTO -> configDTO.toAnalysisMenu(projectId)).collect(Collectors.toList());
        if (!this.saveBatch(menus)) {
            throw new RuntimeException("analysis menus save failed,transaction has rolled back");
        }
        List<Long> ids = menus.stream().map(AnalysisMenu::getId).collect(Collectors.toList());
        return new Response<>().setData(ids);
    }

    /**
     * 只接受带组件的导航栏ID,且只会取出当前导航栏对应着的那些组件,不会取出它的子导航栏对应着的组件（需要重新请求）
     */
    @Override
    public Object getAnalysisData(Long menuId, SearchInfo searchInfo) {
        AnalysisMenu menu = this.getById(menuId);
        if (null == menu) {
            throw new EntityNotFoundException(AnalysisMenu.class, "id", menuId);
        }
        Resource resource = resourceService.getById(menu.getResourceId());
        if (null == resource) {
            throw new EntityNotFoundException(Resource.class, "id", menu.getResourceId());
        }
        List<Component> components = new ArrayList<>();
        // 目录配置的分析详情
        List<AnalysisDetail> detailsFromParent = analysisDetailService.list(Wrappers.<AnalysisDetail>lambdaQuery()
                .eq(AnalysisDetail::getMenuId, menuId));
        //当前节点对应着的组件为空,说明它的子对应着一些组件
        if (detailsFromParent.isEmpty()) {
            log.error("menu detail is null ,menu id={}", menuId);
            return new Response<>().setMsg("no data");
        }
        // 当前检索结果
        AdvancedSearchResourceDTO advancedSearchResource = new AdvancedSearchResourceDTO();
        BeanUtils.copyProperties(searchInfo, advancedSearchResource);
        advancedSearchResource.setResourceId(resource.getId());
        SearchService<IdEntity> searchService = serviceConfig.getService(resource.getEntity());
        QueryWrapper wrapper = searchService.handlerWrapper(advancedSearchResource, resource.getTarget(), Lists.newArrayList(Constants.PRIMARY_KEY));
        List<IdEntity> list = searchService.list(wrapper);
        // 检索结果为空
        if (CollectionUtils.isEmpty(list)) {
            return new Response<>().setMsg("no data");
        }
        // 检索结果集合
        List<String> ids = list.stream().map(idEntity -> idEntity.getId().toString()).collect(Collectors.toList());
        String idList = String.join(",", ids);
        detailsFromParent.forEach(detail -> {
            String css = detail.getCss();
            String desc = detail.getInfo();
            // 替换占位符
            String sql = detail.getExecuteSql().replaceAll("@ids", idList);
            Integer type = detail.getType();
            String config = detail.getConfig();
            // sql语句执行的入口
            List<Map<String, Object>> queryResult = resourceService.listDataBySql(sql);
            Object formatData = dataFormat(type, queryResult, config);
            components.add(new Component()
                    .setType(String.valueOf(type))
                    .setConfig(css)
                    .setDesc(desc)
                    .setData(formatData));
        });
        if (components.isEmpty()) {
            throw new RuntimeException("get component data failed, menuId=" + menuId);
        }
        return new AnalysisWidgetDTO().setComponents(components).setId(menuId).setTitle(menu.getTitle());
    }

    @Override
    public Object getMenu(Long projectId, Long resourceId) {
        return this.list(Wrappers.<AnalysisMenu>lambdaQuery()
                .eq(AnalysisMenu::getProjectId, projectId)
                .eq(AnalysisMenu::getResourceId, resourceId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object detailConfig(List<AnalysisDetailDTO<?>> configDTOs) {
        List<AnalysisDetail> analysisDetails = configDTOs.stream().map(AnalysisDetailDTO::toAnalysisDetail).collect(Collectors.toList());
        // 删除旧配置
        List<Long> menuIdList = configDTOs.stream().map(AnalysisDetailDTO::getMenuId).collect(Collectors.toList());
        analysisDetailService.remove(Wrappers.<AnalysisDetail>lambdaQuery().in(AnalysisDetail::getMenuId, menuIdList));
        try {
            this.analysisDetailService.saveBatch(analysisDetails);
        } catch (Exception e) {
            throw new RuntimeException("analysis Details save failed");
        }
        return new Response<>();
    }

    @Override
    public void deleteConfig(Long projectId, Long resourceId) {
        List<AnalysisMenu> analysisMenuList = this.list(Wrappers.<AnalysisMenu>lambdaQuery().eq(AnalysisMenu::getResourceId, resourceId));
        if (CollectionUtils.isNotEmpty(analysisMenuList)) {
            List<Long> menuIds = analysisMenuList.stream().map(AnalysisMenu::getId).collect(Collectors.toList());
            analysisDetailService.remove(Wrappers.<AnalysisDetail>lambdaQuery().in(AnalysisDetail::getMenuId, menuIds));
        }
    }

    @Override
    public Object deleteMenu(List<Long> menuIds) {
        try {
            this.removeByIds(menuIds);
            analysisDetailService.remove(Wrappers.<AnalysisDetail>lambdaQuery().in(AnalysisDetail::getMenuId, menuIds));
            return new Response<>().setData(true);
        } catch (Exception e) {
            return new Response<>().setData(false);
        }
    }

    @Override
    public Object modifyMenu(Long menuId, MenuConfigDTO config) {
        AnalysisMenu menu = this.getById(menuId);
        if (menu == null) {
            throw new EntityNotFoundException(AnalysisMenu.class, "id", menu);
        }
        AnalysisMenu analysisMenu = config.toAnalysisMenu(menu.getProjectId());
        analysisMenu.setId(menuId);
        if (this.updateById(analysisMenu)) {
            return new Response<>().setData(true);
        } else {
            return new Response<>().setCode(500).setMsg("menu update failed");
        }
    }

    @Override
    public Object getDetail(Long resourceId, Long menuId) {
        Resource resource = resourceService.getById(resourceId);
        if (null == resource) {
            throw new EntityNotFoundException(Resource.class, "id", resourceId);
        }
        List<AnalysisDetail> list = this.analysisDetailService.list(Wrappers.<AnalysisDetail>lambdaQuery()
                .eq(AnalysisDetail::getMenuId, menuId));
        return list.stream().map(AnalysisDetail::toDTO).collect(Collectors.toList());
    }

    private Object dataFormat(Integer type, List<Map<String, Object>> queryResult, String config) {
        // 柱状图、折线图
        if (ChartTypeEnum.LINE_CHART.getType().equals(type) || ChartTypeEnum.HISTOGRAM.getType().equals(type)) {
            return lineFormat(queryResult, config);
        }
        // 饼图
        if (ChartTypeEnum.PIE_CHART.getType().equals(type)) {
            return pieFormat(queryResult, config);
        }
        // 关系图
        if (ChartTypeEnum.RELATION_CHART.getType().equals(type)) {
            return graphFormat(queryResult, config);
        }
        // 气泡图
        if (ChartTypeEnum.BUBBLE_CHART.getType().equals(type)) {
            return bubbleFormat(queryResult, config);
        }
        // TODO: 桑基图
        if (ChartTypeEnum.SANKEY_CHART.getType().equals(type)) {
            return sankeyFormat(queryResult, config);
        }
        // 雷达图
        if (ChartTypeEnum.RAIDER_CHART.getType().equals(type)) {
            return radarFormat(queryResult, config);
        }
        // 表格
        if (ChartTypeEnum.TABLE.getType().equals(type)) {
            return tableFormat(queryResult, config);
        }
        // TODO: 词云图
        if (ChartTypeEnum.TAGS_CLOUD.getType().equals(type)) {
            return tagsCloudFormat(queryResult, config);
        }

        // 多柱状图、多折线图、堆叠柱状图
        if (ChartTypeEnum.MULTI_PART.getType().equals(type)) {
            return multiPartFormat(queryResult, config);
        }

        throw new RuntimeException("type not right,type=" + type);
    }

    private Object multiPartFormat(List<Map<String, Object>> queryResult, String config) {
        Map<String, MultiPartConfig> configMap = JSONObject.parseObject(config, new TypeReference<Map<String, MultiPartConfig>>() {
        });
        MultiPartConfig mpConfig = configMap.get("multiPartConfig");
        String xName = mpConfig.getXName();
        String yName = mpConfig.getYName();
        String isLine = mpConfig.getIsLine();
        String stackGroup = mpConfig.getStackGroup();
        String cName = mpConfig.getComponentName();
        MultiPartDatasetBuilder datasetBuilder = new MultiPartDatasetBuilder(xName, yName, queryResult);
        datasetBuilder.build();
        return new MultiPartDTO().setTitle(cName)
                .setDataset(datasetBuilder.getDataset())
                .setSeries(datasetBuilder.getSeries(
                        isLine.trim().isEmpty()
                                ? MultiPartDatasetBuilder.DataType.LINE : MultiPartDatasetBuilder.DataType.BAR,
                        !stackGroup.trim().isEmpty()));
    }

    private Object graphFormat(List<Map<String, Object>> queryResult, String config) {
        Map<String, GraphConfig> configMap = JSONObject.parseObject(config, new TypeReference<Map<String, GraphConfig>>() {
        });
        GraphConfig graphConfig = configMap.get("graphConfig");
        GraphBuilder graphBuilder = new GraphBuilder(queryResult, graphConfig);
        graphBuilder.build();
        Map<String, Object> series = new HashMap<>();
        series.put("categories", graphBuilder.getCategories());
        series.put("links", graphBuilder.getLinks());
        series.put("data", graphBuilder.getData());
        return new GraphRetDTO().setTitle(graphConfig.getComponentName())
                .setSeries(series);
    }

    private Object tagsCloudFormat(List<Map<String, Object>> queryResult, String config) {
        return null;
    }

    /**
     * 表格要求语句形如：
     * select
     * importance,count(importance) as value
     * from
     * news
     * where
     * clause
     * group by
     * importance
     * order by
     * importance
     * 求配置形如：
     * {
     * "header":"对应着select子句中的header字段",
     * "headerName":"表头的解释",
     * "dataDesc":"描述信息",
     * "componentName":"这是图的名称"
     * }
     */
    private Object tableFormat(List<Map<String, Object>> queryResult, String config) {
        Map<String, TableConfig> configMap = JSONObject.parseObject(config, new TypeReference<Map<String, TableConfig>>() {
        });
        TableConfig tableConfig = configMap.get("tableConfig");
        String header = tableConfig.getHeader();
        String headerName = tableConfig.getHeaderName();
        String dataDesc = tableConfig.getDataDesc();
        String cName = tableConfig.getComponentName();
        List<Map<String, Object>> maps = new ArrayList<>();
        Map<String, Object> e = new HashMap<>();
        e.put("title", header);
        e.put("key", headerName);
        maps.add(e);
        List<Map<String, Object>> columns = new ArrayList<>(maps);
        Map<String, Object> data = new HashMap<String, Object>() {{
            put(headerName, dataDesc);
        }};
        queryResult.forEach(aMap -> {
            AtomicReference<String> colKey = new AtomicReference<>();
            AtomicReference<Integer> colVal = new AtomicReference<>(0);
            aMap.forEach((k, v) -> {
                //说明是列
                if (!"value".equals(k)) {
                    Map<String, Object> e1 = new HashMap<>();
                    e1.put("title", v);
                    e1.put("key", v);
                    columns.add(e1);
                    colKey.set(String.valueOf(v));
                } else {
                    colVal.set(Integer.valueOf(String.valueOf(v)));
                }
            });
            data.put(colKey.get(), colVal.get());
        });
        List<Map<String, Object>> data1 = new ArrayList<>();
        data1.add(data);
        return new TableRetDTO().setTitle(cName).setColumns(columns).setData(data1);
    }

    /**
     * 雷达图要求语句形如：
     * select
     * importance as dimension,count(id) as value
     * from
     * news
     * where
     * clause
     * group by
     * importance
     * order by
     * importance
     * 求配置形如：
     * {
     * "dimensionName":"重要性",
     * "componentName":"这是图的名称"
     * }
     */
    private Object radarFormat(List<Map<String, Object>> queryResult, String config) {
        Map<String, RadarConfig> configMap = JSONObject.parseObject(config, new TypeReference<Map<String, RadarConfig>>() {
        });
        RadarConfig radarConfig = configMap.get("radarConfig");
        String dimensionName = radarConfig.getDimensionName();
        String cName = radarConfig.getComponentName();
        List<Map<String, Object>> data = new ArrayList<>();
        List<Map<String, Object>> indicator = new ArrayList<>();
        HashMap<String, Object> radarData = new HashMap<>();
        List<Integer> values = new ArrayList<>();
        queryResult.forEach(stringObjectMap -> {
            stringObjectMap.forEach((k, v) -> {
                        if ("dimension".equals(k)) {
                            Map<String, Object> e = new HashMap<>();
                            e.put("name", String.valueOf(v));
                            indicator.add(e);
                        }
                        if ("value".equals(k)) {
                            values.add(Integer.parseInt(String.valueOf(v)));
                        }
                    }
            );
        });
        radarData.put("value", values);
        radarData.put("name", dimensionName);
        data.add(radarData);
        return new RadarRetDTO().setData(data).setIndicator(indicator).setTitle(cName);
    }

    private Object sankeyFormat(List<Map<String, Object>> queryResult, String config) {
        return null;
    }

    /**
     * 气泡图要求语句形如：
     * select
     * importance as xAxis,title as yAxis,count(id) as value
     * from
     * news
     * where
     * clause
     * group by
     * importance
     * order by
     * importance,title
     * 求配置形如：
     * {
     * "content":"气泡的意义",
     * "componentName":"这是图的名称"
     * }
     */
    private Object bubbleFormat(List<Map<String, Object>> queryResult, String config) {
        Map<String, BubbleConfig> configMap = JSONObject.parseObject(config, new TypeReference<Map<String, BubbleConfig>>() {
        });
        BubbleConfig bubbleConfig = configMap.get("bubbleConfig");
        LinkedHashSet<String> xData = new LinkedHashSet<>();
        LinkedHashSet<String> yData = new LinkedHashSet<>();
        Map<String, Integer> xIndex = new HashMap<>();
        AtomicInteger x = new AtomicInteger(0);
        Map<String, Integer> yIndex = new HashMap<>();
        AtomicInteger y = new AtomicInteger(0);
        List<List<Integer>> arr = new ArrayList<>();
        queryResult.forEach(map -> {
            AtomicReference<String> xStr = new AtomicReference<>();
            AtomicReference<String> yStr = new AtomicReference<>();
            AtomicInteger data = new AtomicInteger();
            map.forEach((k, v) -> {
                String vStr = String.valueOf(v);
                if ("xAxis".equals(k)) {
                    if (xData.add(vStr)) {
                        xIndex.put(vStr, x.getAndIncrement());
                    }
                    xStr.set(vStr);
                }
                if ("yAxis".equals(k)) {
                    if (yData.add(vStr)) {
                        yIndex.put(vStr, y.getAndIncrement());
                    }
                    yStr.set(vStr);
                }
                if ("value".equals(k)) {
                    data.set(Integer.parseInt(vStr));
                }
            });
            List<Integer> e = new ArrayList<>();
            e.add(xIndex.get(xStr.get()));
            e.add(yIndex.get(yStr.get()));
            e.add(data.get());
            arr.add(e);
        });
        Map<String, Object> xAxis = new HashMap<>();
        xAxis.put("name", bubbleConfig.getXName());
        xAxis.put("data", xData);
        Map<String, Object> yAxis = new HashMap<>();
        yAxis.put("name", bubbleConfig.getYName());
        yAxis.put("data", yData);
        return new BubbleRetDTO()
                .setContentDesc(bubbleConfig.getContent())
                .setArr(arr).setTitle(bubbleConfig.getComponentName())
                .setXAxis(xAxis)
                .setYAxis(yAxis);
    }

    /**
     * 饼图要求语句形如：
     * select
     * importance as name,count(importance) as value
     * from
     * news
     * where
     * clause
     * group by
     * importance
     * order by
     * importance
     * 求配置形如：
     * {
     * "componentName":"这是图的名称"
     * }
     */
    private Object pieFormat(List<Map<String, Object>> queryResult, String config) {
        Map<String, PieConfig> configMap = JSONObject.parseObject(config, new TypeReference<Map<String, PieConfig>>() {
        });
        PieConfig pieConfig = configMap.get("pieConfig");
        String cName = pieConfig.getComponentName();
        List<Object> data = new ArrayList<>();
        queryResult.forEach(map -> {
            Map<String, Object> tmp = new HashMap<>();
            map.forEach((k, v) -> {
                if ("name".equals(k)) {
                    tmp.put(k, String.valueOf(v));
                } else {
                    tmp.put(k, v);
                }
            });
            data.add(tmp);
        });
        Map<String, Object> series = new HashMap<>();
        series.put("data", data);
        return new PieRetDTO()
                .setTitle(cName)
                .setSeries(series);
    }

    /**
     * 折线、柱状图要求语句形如：
     * select
     * importance as xAxis,count(importance) as yAxis
     * from
     * news
     * where
     * clause
     * group by
     * importance
     * order by
     * importance
     * 要求配置形如：
     * {
     * "xName":"重要性",
     * "yName":"统计",
     * "componentName":"这是图的名称",
     * "lineName":"这一条折线是新闻折线"   ###如果展示时不需要可以删除该项
     * }
     */
    private Object lineFormat(List<Map<String, Object>> queryResult, String config) {
        Map<String, LineConfig> configMap = JSONObject.parseObject(config, new TypeReference<Map<String, LineConfig>>() {
        });
        LineConfig lineConfig = configMap.get("lineConfig");
        String xName = lineConfig.getXName();
        String yName = lineConfig.getYName();
        String cName = lineConfig.getLineName();
        List<Map<String, Object>> data = Lists.newArrayList();
        List<Object> yData = Lists.newArrayList();
        List<Object> xData = Lists.newArrayList();
        queryResult.forEach(map -> map.forEach((k, v) -> {
                    if ("xAxis".equals(k)) {
                        xData.add(v);
                    }
                    if ("yAxis".equals(k)) {
                        yData.add(v);
                    }
                }
        ));
        Map<String, Object> e = new HashMap<>();
        e.put("data", yData);
        e.put("name", lineConfig.getLineName());
        data.add(e);
        Map<String, Object> xAxis = new HashMap<>();
        xAxis.put("name", xName);
        xAxis.put("type", "category");
        xAxis.put("data", xData);
        Map<String, Object> yAxis = new HashMap<>();
        yAxis.put("name", yName);
        yAxis.put("type", "value");
        return new LineRetDTO()
                .setTitle(cName)
                .setXAxis(xAxis)
                .setYAxis(yAxis)
                .setSeries(data);
    }
}
