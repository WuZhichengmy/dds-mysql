package com.xmu.modules.display_config.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmu.exception.EntityNotFoundException;
import com.xmu.modules.display_config.domain.DisplayWidgets;
import com.xmu.modules.display_config.domain.Project;
import com.xmu.modules.display_config.domain.Resource;
import com.xmu.modules.display_config.domain.ResourceProperty;
import com.xmu.modules.display_config.enums.DefaultStyle;
import com.xmu.modules.display_config.enums.WidgetDataSourceTypeEnum;
import com.xmu.modules.display_config.enums.WidgetTypeEnum;
import com.xmu.modules.display_config.mapper.DisplayWidgetsMapper;
import com.xmu.modules.display_config.request.*;
import com.xmu.modules.display_config.response.LeaderBoardRetDTO;
import com.xmu.modules.display_config.response.WidgetDTO;
import com.xmu.modules.display_config.service.DisplayWidgetsService;
import com.xmu.modules.display_config.service.ProjectService;
import com.xmu.modules.display_config.service.ResourcePropertyService;
import com.xmu.modules.display_config.service.ResourceService;
import com.xmu.modules.display_config.utils.Response;
import com.xmu.modules.display_config.utils.SqlUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Xing
 */
@Service
public class DisplayWidgetsServiceImpl extends ServiceImpl<DisplayWidgetsMapper, DisplayWidgets> implements DisplayWidgetsService {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourcePropertyService propertyService;

    //⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐获取组件数据⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐
    @Override
    public Object getWidgets(Long projectId, Long id) {
        // 查找项目
        Project project = projectService.getById(projectId);
        if (null == project) {
            throw new EntityNotFoundException(Project.class, "id", projectId);
        }
        // 查找组件
        DisplayWidgets displayWidgets = this.getById(id);
        if (null == displayWidgets) {
            throw new EntityNotFoundException(DisplayWidgets.class, "id", id);
        }
        WidgetDTO widgetDTO = new WidgetDTO();
        BeanUtils.copyProperties(displayWidgets, widgetDTO);
        // 配置信息
        widgetDTO.setConfig(JSONObject.parseObject(displayWidgets.getConfig(), new TypeReference<Map<String, Object>>() {
        }));
        //组件类型：轮播图、排行榜之类的
        Integer compType = displayWidgets.getType();
        //数据源类型：SQL、静态数据、算法等等。
        Integer dataSourceType = displayWidgets.getDataSourceType();
        String dataSource = displayWidgets.getDataSource();
        //如果是SQL类型。
        if (dataSourceType.equals(WidgetDataSourceTypeEnum.SQL.getValue())) {
            widgetDTO.setData(this.getData(compType, dataSource));
            //静态数据及其他
        } else {
            widgetDTO.setData(this.getWidgetData(displayWidgets.getDataSourceType(), dataSource));
        }
        return new Response<>().setData(widgetDTO);
    }

    /**
     * @param compType   组件类型：轮播图、排行榜之类的
     * @param dataSource 数据源：这里适用于从SQL拿数据的
     */
    private Map<String, Object> getData(Integer compType, String dataSource) {
        // 轮播图
        if (WidgetTypeEnum.CAROUSEL.getType().equals(compType)) {
            return carousel(JSONObject.parseArray(dataSource, CarouselConfigDTO.class));
        }

        // 排行榜
        if (WidgetTypeEnum.ARTICLE_LIST.getType().equals(compType)) {
            return listLeadBoardData(JSONObject.parseArray(dataSource, LeaderBoardDTO.class));
        }

        // 资源统计
        if (WidgetTypeEnum.PANEL.getType().equals(compType)) {
            return panelGroup(JSONObject.parseArray(dataSource, Long.class));
        }

        // 最新资源
        if (WidgetTypeEnum.LATEST_DATA.getType().equals(compType)) {
            return listLatestData(JSONObject.parseArray(dataSource, LatestDataDTO.class));
        }

        // 柱状图
        if (WidgetTypeEnum.CATEGORY.getType().equals(compType)) {
            return category(JSONObject.parseArray(dataSource, CategoryDTO.class));
        }

        // 词云图
        if (WidgetTypeEnum.TAGS_CLOUD.getType().equals(compType)) {
//            return new HashMap<String, Object>() {{
//                put("TagsCloud", List.of(
//                        Map.of("resourceId", 366775090878091264L, "name", "新闻热词", "data", List.of("新冠肺炎疫情", "疫情防控阻击战", "复工复产", "全国哀悼日", "民法典", "北斗全球卫星导航系统", "《香港特别行政区维护国家安全法》", "数字人民币", "中国国际服务贸易交易会", "十九届五中全会", "《区域全面经济伙伴关系协定》", "离婚冷静期")),
//                        Map.of("resourceId", 366775090878091264L, "name", "论文热词", "data", List.of("新冠肺炎疫情", "疫情防控阻击战", "复工复产", "全国哀悼日", "民法典", "北斗全球卫星导航系统", "《香港特别行政区维护国家安全法》", "数字人民币", "中国国际服务贸易交易会", "十九届五中全会", "《区域全面经济伙伴关系协定》", "离婚冷静期"))
//                ));
//            }};
            return null;
        }
        return null;
    }

    /**
     * 柱状图预览
     */
    @Override
    public Object previewWidgetsO(WidgetConfig<List<CategoryDTO>> widgetConfig) {
        WidgetDTO widgetDTO = new WidgetDTO();
        widgetDTO.setType(8);
        widgetDTO.setConfig(JSONObject.parseObject(widgetConfig.getConfig(), new TypeReference<Map<String, Object>>() {
        }));
        List<CategoryDTO> categories = widgetConfig.getData();
        Map<String, Object> result = category(categories);
        widgetDTO.setData(result);
        return new Response<>().setData(widgetDTO);
    }

    /**
     * 获取配置
     */
    @Override
    public Object getWidgetsConfig(List<Long> ids) {
        if (ids.isEmpty()) {
            return new Response<>().setMsg("widget ids empty").setCode(400);
        }
        if (ids.size() == 3) {
            //基础配置
            Map<String, Object> res = new HashMap<>();
            ids.forEach(id -> {
                DisplayWidgets displayWidgets = this.getById(id);
                String dataSource = displayWidgets.getDataSource();
                Integer type = displayWidgets.getType();
                if (type == 0) {
                    Map<String, String> banner = JSONObject.parseObject(dataSource, new TypeReference<Map<String, String>>() {
                    });
                    res.putAll(banner);
                } else {
                    Map<String, Map<String, String>> other
                            = JSONObject.parseObject(dataSource, new TypeReference<Map<String, Map<String, String>>>() {
                    });
                    if (type == 5) {
                        Map<String, String> logo = other.get("logo");
                        res.putAll(logo);
                    } else if (type == 6) {
                        Map<String, String> copyright = other.get("CopyRight");
                        res.putAll(copyright);
                    }
                }
            });
            return new Response<>().setData(res);
        } else {

            //首页配置
            DisplayWidgets widgets = this.getById(ids.get(0));
            Object config = JSONObject.parseObject(widgets.getDataSource(), Object.class);
            WidgetConfig<Object> objectWidgetConfig = new WidgetConfig<>()
                    .setConfig(widgets.getConfig())
                    .setType(widgets.getType())
                    .setData(config);
            return new Response<>().setData(objectWidgetConfig);

        }
    }

    @Override
    public Object selectByPage(Page<?> page, String sql) {
        return this.getBaseMapper().selectByPage(page, sql);
    }

    /**
     * 轮播图组件
     */
    private Map<String, Object> carousel(List<CarouselConfigDTO> carouselDTOs) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            for (CarouselConfigDTO carousel : carouselDTOs) {
                Long recordId = carousel.getRecordId();
                Long resourceId = carousel.getResourceId();
                String target = resourceService.getById(resourceId).getTarget();
                ResourceProperty defaultProperty
                        = propertyService.getOne(Wrappers.<ResourceProperty>lambdaQuery().eq(ResourceProperty::getIsDefault, 1).eq(ResourceProperty::getResourceId, resourceId).last("limit 1"));
                String sql = SqlUtil.getById(recordId, target);
                Map<String, Object> queryResult = this.getBaseMapper().selectDataBySql(sql);
                Map<String, Object> r = new HashMap<>();
                queryResult.forEach((k, v) -> {
                    if (k.equals("id")) {
                        r.put("id", v);
                    }
                    if (k.equals(defaultProperty.getName())) {
                        r.put("text", v);
                    }
                });
                r.put("resourceId", resourceId);
                r.put("img", carousel.getUrl());
                result.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("CarouselItem", result);
        return map;
    }

    /**
     * 资源柱状图
     */
    private Map<String, Object> category(List<CategoryDTO> categories) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();
        HashSet<Object> x = new HashSet<>();
        categories.forEach(category -> {
            Long resourceId = category.getResourceId();
            String xAxis = category.getXAxis();
            List<Integer> y = new ArrayList<>();
            Resource resource = resourceService.getById(resourceId);
            String name = resource.getName();
            String table = resource.getTarget();
            String sql = SqlUtil.category(table, xAxis);
            List<Map<String, Object>> queryResult
                    = this.getBaseMapper().listDataBySql(sql);
            queryResult.forEach(map -> map.forEach((k, v) -> {
                if (k.equals("xAxis")) {
                    x.add(v);
                }
                if (k.equals("yAxis")) {
                    y.add(Math.toIntExact((Long) v));
                }
            }));
            data.add(new HashMap<String, Object>() {{
                put("title", name);
                put("data", y);
            }});
        });
        result.put("Category", new HashMap<String, Object>() {{
            put("axis", x);
            put("data", data);
        }});
        return result;
    }

    /**
     * 最新资源组件
     */
    private Map<String, Object> listLatestData(List<LatestDataDTO> latestDataList) {
        Map<String, Object> result = new HashMap<>();
        List<Object> list = new ArrayList<>();
        latestDataList.forEach(latestData -> {
            HashMap<String, Object> tmp = new HashMap<>();
            Resource resource = resourceService.getById(latestData.getResourceId());
            if (null == resource) {
                log.error("listLatestData failed by resource is null !");
                return;
            }
            ResourceProperty property = propertyService.getOne(Wrappers.<ResourceProperty>lambdaQuery()
                    .eq(ResourceProperty::getIsDefault, 1)
                    .eq(ResourceProperty::getResourceId, latestData.getResourceId()));
            if (null == property) {
                log.error("listLatestData failed by defaultProperty is null !");
                return;
            }
            List<Map<String, Object>> queryResult = this.getBaseMapper().listLatestData(resource.getTarget(),
                    property.getName(), latestData.getOrderColumns(), latestData.getLimit());
            tmp.put("name", resource.getName());
            tmp.put("resourceId", latestData.getResourceId());
            tmp.put("data", queryResult);
            list.add(tmp);
        });
        result.put("ArticleList", list);
        return result;
    }

    /**
     * 资源统计组件
     */
    private Map<String, Object> panelGroup(List<Long> ids) {
        Map<String, Object> result = new HashMap<>();
        List<HashMap<String, Object>> list = new ArrayList<>();
        ids.forEach(resourceId -> {
            Resource resource = resourceService.getById(resourceId);
            HashMap<String, Object> tmp = new HashMap<>();
            String name = resource.getName();
            tmp.put("name", name);
            tmp.put("resourceId", resourceId);
            String sql = SqlUtil.count(resource.getTarget());
            Map<String, Object> queryResult = this.getBaseMapper().selectDataBySql(sql);
            queryResult.forEach((k, v) -> {
                if (k.equals("n")) {
                    tmp.put("count", v);
                }
            });
            list.add(tmp);
        });
        result.put("PanelGroup", list);
        return result;
    }

    /**
     * 排行榜
     */
    private Map<String, Object> listLeadBoardData(List<LeaderBoardDTO> leaderBoardDTOS) {
        Map<String, Object> result = new HashMap<>();
        List<LeaderBoardRetDTO> leaderBoardRetDTOS = new ArrayList<>();
        leaderBoardDTOS.forEach(leaderBoardDTO -> {
            Long resourceId = leaderBoardDTO.getResourceId();
            Resource resource = resourceService.getById(resourceId);
            if (null == resource) {
                log.error("listLeadBoardData failed by resource is null !");
                return;
            }
            String name = resource.getName() + "排行榜";
            ResourceProperty property = propertyService.getOne(Wrappers.<ResourceProperty>lambdaQuery()
                    .eq(ResourceProperty::getIsDefault, 1)
                    .eq(ResourceProperty::getResourceId, resourceId));
            if (null == property) {
                log.error("listLeadBoardData failed by defaultProperty is null !");
                return;
            }
            List<Map<String, Object>> queryResult = this.getBaseMapper().listLeaderBoardData(resource.getTarget(), property.getName(),
                    leaderBoardDTO.getOrderColumn(), leaderBoardDTO.getOrderType(), leaderBoardDTO.getLimit());
            LeaderBoardRetDTO leaderBoardRetDTO = new LeaderBoardRetDTO()
                    .setResourceId(resourceId)
                    .setName(name)
                    .setData(queryResult);
            leaderBoardRetDTOS.add(leaderBoardRetDTO);
        });
        result.put("LeaderBoard", leaderBoardRetDTOS);
        return result;
    }
    //⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐获取组件数据⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐


    //⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐首页处的全局配置==资源无关==共7项==且不需要联表⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐

    /**
     * 基础配置：版权、logo和banner
     */
    @Override
    public Object basicConfig(Long projectId, BasicConfig basicConfig) {
        Project project = projectService.getById(projectId);
        if (null == project) {
            throw new EntityNotFoundException(Project.class, "id", projectId);
        }
        String copyright = basicConfig.getCopyright();
        String titleCn = basicConfig.getTitleCn();
        String titleEn = basicConfig.getTitleEn();
        String record = basicConfig.getRecord();
        String domainRecord = basicConfig.getDomainRecord();
        String bannerUrl = basicConfig.getBannerUrl();
        String logoUrl = basicConfig.getLogoUrl();
        Map<String, String> banner
                = new HashMap<String, String>() {{
            put("Banner", bannerUrl);
        }};
        Map<String, Map<String, String>> logo
                = new HashMap<String, Map<String, String>>() {{
            put("logo",
                    new HashMap<String, String>() {{
                        put("titleEn", titleEn);
                        put("titleCn", titleCn);
                        put("logo", logoUrl);
                    }});
        }};
        Map<String, Map<String, String>> copyR
                = new HashMap<String, Map<String, String>>() {{
            put("CopyRight",
                    new HashMap<String, String>() {{
                        put("copyright", copyright);
                        put("record", record);
                        put("domainRecord", domainRecord);
                    }});
        }};
        DisplayWidgets bannerGet = this.getOne(Wrappers.<DisplayWidgets>lambdaQuery()
                .eq(DisplayWidgets::getProjectId, projectId)
                .eq(DisplayWidgets::getType, WidgetTypeEnum.BANNER.getType()));
        DisplayWidgets logoGet = this.getOne(Wrappers.<DisplayWidgets>lambdaQuery()
                .eq(DisplayWidgets::getProjectId, projectId)
                .eq(DisplayWidgets::getType, WidgetTypeEnum.LOGO.getType()));
        DisplayWidgets copyRightGet = this.getOne(Wrappers.<DisplayWidgets>lambdaQuery()
                .eq(DisplayWidgets::getProjectId, projectId)
                .eq(DisplayWidgets::getType, WidgetTypeEnum.COPYRIGHT.getType()));
        String bannerJson = JSONObject.toJSONString(banner);
        String logoJson = JSONObject.toJSONString(logo);
        String copyRJson = JSONObject.toJSONString(copyR);
        // banner组件
        DisplayWidgets bannerWidgets = new DisplayWidgets().setConfig(DefaultStyle.BANNER.getConfig());
        bannerWidgets
                .setType(WidgetTypeEnum.BANNER.getType())
                .setDataSource(bannerJson)
                .setDataSourceType(2)
                .setName(WidgetTypeEnum.BANNER.getName())
                .setProjectId(projectId)
                .setAlgorithmParam(null);
        // logo组件
        DisplayWidgets logoWidgets = new DisplayWidgets().setConfig(DefaultStyle.LOGO.getConfig());
        logoWidgets
                .setType(WidgetTypeEnum.LOGO.getType())
                .setDataSource(logoJson)
                .setDataSourceType(2)
                .setName(WidgetTypeEnum.LOGO.getName())
                .setProjectId(projectId)
                .setAlgorithmParam(null);
        // 版权组件
        DisplayWidgets copyRWidgets = new DisplayWidgets().setConfig(DefaultStyle.COPYRIGHT.getConfig());
        copyRWidgets
                .setType(WidgetTypeEnum.COPYRIGHT.getType())
                .setDataSource(copyRJson)
                .setDataSourceType(2)
                .setName(WidgetTypeEnum.COPYRIGHT.getName())
                .setProjectId(projectId)
                .setAlgorithmParam(null);
        boolean bannerSave;
        boolean logoSave;
        boolean copyRSave;
        if (bannerGet != null) {
            Long id = bannerGet.getId();
            BeanUtils.copyProperties(bannerWidgets, bannerGet);
            bannerGet.setId(id);
            bannerSave = this.updateById(bannerGet);
        } else {
            bannerSave = this.save(bannerWidgets);
        }
        if (logoGet != null) {
            Long id = logoGet.getId();
            BeanUtils.copyProperties(logoWidgets, logoGet);
            logoGet.setId(id);
            logoSave = this.updateById(logoGet);
        } else {
            logoSave = this.save(logoWidgets);
        }
        if (copyRightGet != null) {
            Long id = copyRightGet.getId();
            BeanUtils.copyProperties(copyRWidgets, copyRightGet);
            copyRightGet.setId(id);
            copyRSave = this.updateById(copyRightGet);
        } else {
            copyRSave = this.save(copyRWidgets);
        }

        if (bannerSave && logoSave && copyRSave) {
            Map<String, Long> data = new HashMap<>();
            data.put(String.valueOf(bannerWidgets.getType()), bannerWidgets.getId());
            data.put(String.valueOf(logoWidgets.getType()), logoWidgets.getId());
            data.put(String.valueOf(copyRWidgets.getType()), copyRWidgets.getId());
            return new Response<>().setData(data);
        } else {
            return new Response<>().setMsg("widgets saved failed").setCode(500);
        }
    }

    /**
     * 首页配置：轮播图、排行榜、柱状图、资源统计、最新资源
     */
    @Override
    public Object widgetConfig(Long projectId, WidgetConfig<?> widgetConfig) {
        Project project = projectService.getById(projectId);
        if (null == project) {
            throw new EntityNotFoundException(Project.class, "id", projectId);
        }

        Integer type = widgetConfig.getType();
        String data = JSONObject.toJSONString(widgetConfig.getData());
        DisplayWidgets widgets
                = new DisplayWidgets().setConfig(widgetConfig.getConfig());
        //TODO:如果是词云，则先调用接口请求热词
        widgets.setDataSourceType(1);
        widgets
                .setType(type)
                .setDataSource(data)
                .setName(WidgetTypeEnum.getNameByType(type))
                .setProjectId(projectId)
                .setAlgorithmParam(null);
        DisplayWidgets existWidget = this.getOne(Wrappers.<DisplayWidgets>lambdaQuery()
                .eq(DisplayWidgets::getProjectId, projectId)
                .eq(DisplayWidgets::getType, type));
        if (existWidget != null) {
            widgets.setId(existWidget.getId());
            this.updateById(widgets);
            return new Response<>().setMsg("widgets update success").setData(new HashMap<String, Long>() {{
                put(String.valueOf(widgets.getType()), existWidget.getId());
            }});
        } else {
            if (this.save(widgets)) {
                return new Response<>().setData(new HashMap<String, Long>() {{
                    put(String.valueOf(widgets.getType()), widgets.getId());
                }});
            } else {
                return new Response<>().setCode(500).setMsg("widgets saved failed,widgets type=" + widgets.getType());
            }
        }
    }

    /**
     * 词云配置模板
     */
    private Object tagsConfig(Long projectId, List<TagsCloudDTO> tagsCloudDTOs) {
        Project project = projectService.getById(projectId);
        if (null == project) {
            throw new EntityNotFoundException(Project.class, "id", projectId);
        }
        List<Object> tagsCloud = new ArrayList<>();
        tagsCloudDTOs.forEach(tagsCloudDTO -> {
            List<String> tags = tagsCloudDTO.getTags();
            String name = tagsCloudDTO.getName();
            Long resourceId = tagsCloudDTO.getResourceId();
            if (resourceService.getById(resourceId) == null) {
                throw new EntityNotFoundException(Resource.class, "id", resourceId);
            }
            tagsCloud.add(new HashMap<String, Object>() {{
                put("resourceId", resourceId);
                put("name", name);
                put("data", tags);
            }});
        });
        String json = JSONObject.toJSONString(new HashMap<String, Object>() {{
            put("TagsCloud", tagsCloud);
        }});
        DisplayWidgets tagsWidgets
                = new DisplayWidgets().setConfig(DefaultStyle.TAGS_CLOUD.getConfig());
        tagsWidgets
                .setType(WidgetTypeEnum.TAGS_CLOUD.getType())
                .setDataSource(json)
                .setDataSourceType(2)
                .setName(WidgetTypeEnum.TAGS_CLOUD.getName())
                .setProjectId(projectId)
                .setAlgorithmParam(null);
        return this.save(tagsWidgets);
    }
    //⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐首页处的全局配置==资源无关==共7项==且不需要联表⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐

    /**
     * 获取表头
     */
    @Override
    public Object getColumns(Long resourceId, boolean useId) {
        Resource resource = resourceService.getById(resourceId);
        String target = resource.getTarget();
        String sql = SqlUtil.columns(target);
//        System.out.println("/**/aaa"+sql);
        List<Map<String, Object>> maps = this.getBaseMapper().listDataBySql(sql);
//        System.out.println("bbb"+maps);
        //过滤掉id
        if (!useId) {
            maps = maps.stream().filter(map -> !map.get("COLUMN_NAME").equals("id")).collect(Collectors.toList());
        }
        return maps;
    }

    /**
     * 获取静态数据、算法数据等
     */
    private Map<String, Object> getWidgetData(Integer type, String dataSource) {
        switch (Objects.requireNonNull(WidgetDataSourceTypeEnum.getByValue(type))) {
            case DATA_TABLE:
                return this.getBaseMapper().selectDataByTableName(dataSource);
            case SQL:
                return this.getBaseMapper().selectDataBySql(dataSource);
            case STATIC_DATA:
                return JSONObject.parseObject(dataSource, new TypeReference<Map<String, Object>>() {
                });
            case ALGORITHM:
                // TODO: 算法获取组件数据
                return null;
            default:
                return null;
        }
    }
}
