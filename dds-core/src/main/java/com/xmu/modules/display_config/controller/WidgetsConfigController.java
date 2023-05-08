package com.xmu.modules.display_config.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmu.annotation.AnonymousAccess;
import com.xmu.modules.display_config.domain.DisplayWidgets;
import com.xmu.modules.display_config.domain.Project;
import com.xmu.modules.display_config.request.*;
import com.xmu.modules.display_config.response.ProjectInfoDTO;
import com.xmu.modules.display_config.response.ResourceDTO;
import com.xmu.modules.display_config.service.DisplayWidgetsService;
import com.xmu.modules.display_config.service.ProjectService;
import com.xmu.modules.display_config.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
@RestController
@RequestMapping("/widgets/config")
@Api(tags = "展示组件配置模块")
@Slf4j
public class WidgetsConfigController {

    @Autowired
    private DisplayWidgetsService displayWidgetsService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ProjectService projectService;


    //⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐项目基本信息⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐

    @ApiOperation("获取所有项目")
    @GetMapping("/project/all")
    public Object getProjects() {
        List<Project> projects = projectService.list(new QueryWrapper<>());
        List<ProjectInfoDTO> projectRet = projects.stream().map(ProjectInfoDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(projectRet, HttpStatus.OK);
    }

    @ApiOperation("获取项目所有资源")
    @GetMapping("/{projectId}/resources")
    public ResponseEntity<List<ResourceDTO>> getResources(@PathVariable Long projectId) {
        return new ResponseEntity<>(resourceService.getResources(projectId), HttpStatus.OK);
    }

    @ApiOperation("获取项目所有组件")
    @GetMapping("{projectId}")
    @AnonymousAccess
    public Object getWidgets(@PathVariable Long projectId) {
        List<DisplayWidgets> list = displayWidgetsService.list(new LambdaQueryWrapper<DisplayWidgets>().eq(DisplayWidgets::getProjectId, projectId));
        return list.stream().collect(Collectors.toMap(DisplayWidgets::getType, DisplayWidgets::getId));
    }
    //⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐项目基本信息⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐


    //⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐首页处的全局配置==资源无关==共7项==且不需要联表⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐

    @ApiOperation("基础信息配置：logo,banner和版权信息")
    @PostMapping("/basic/{projectId}")
    public Object basicConfig(@PathVariable Long projectId, @Valid @RequestBody BasicConfig basicConfig) {
        return new ResponseEntity<>(displayWidgetsService.basicConfig(projectId, basicConfig), HttpStatus.OK);
    }

    @ApiOperation("轮播图配置")
    @PostMapping("/carouselItem/{projectId}")
    public Object carouselConfig(@PathVariable Long projectId, @Valid @RequestBody WidgetConfig<List<CarouselConfigDTO>> carouselConfig) {
        return new ResponseEntity<>(displayWidgetsService.widgetConfig(projectId, carouselConfig), HttpStatus.OK);
    }

    @ApiOperation("排行榜配置")
    @PostMapping("/leadBoard/{projectId}")
    public Object leadBoardConfig(@PathVariable Long projectId, @Valid @RequestBody WidgetConfig<List<LeaderBoardDTO>> leaderBoardDTO) {
        return new ResponseEntity<>(displayWidgetsService.widgetConfig(projectId, leaderBoardDTO), HttpStatus.OK);
    }

    @ApiOperation("词云配置")
    @PostMapping("/tagsCloud/{projectId}")
    public Object TagsCloud(@PathVariable("projectId") Long projectId, @RequestBody WidgetConfig<List<Long>> tagsCloudDTO) {
        return new ResponseEntity<>(displayWidgetsService.widgetConfig(projectId, tagsCloudDTO), HttpStatus.OK);
    }

    @ApiOperation("柱状图配置")
    @PostMapping("/category/{projectId}")
    public Object Category(@PathVariable("projectId") Long projectId, @Valid @RequestBody WidgetConfig<List<CategoryDTO>> categoryDTO) {
        return new ResponseEntity<>(displayWidgetsService.widgetConfig(projectId, categoryDTO), HttpStatus.OK);
    }

    @ApiOperation("数量统计配置")
    @PostMapping("/panelGroup/{projectId}")
    public Object PanelGroup(@PathVariable Long projectId, @RequestBody WidgetConfig<List<Long>> panelGroupDTO) {
        return new ResponseEntity<>(displayWidgetsService.widgetConfig(projectId, panelGroupDTO), HttpStatus.OK);
    }

    @ApiOperation("最新资源配置")
    @PostMapping("/articleList/{projectId}")
    public Object ArticleList(@PathVariable Long projectId, @RequestBody WidgetConfig<List<LatestDataDTO>> articleListDTO) {
        return new ResponseEntity<>(displayWidgetsService.widgetConfig(projectId, articleListDTO), HttpStatus.OK);
    }
    //⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐首页处的全局配置==资源无关==共7项==且不需要联表⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐

    //⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐同步数据⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐
    @PostMapping("/data")
    public Object data() {
        //TODO:数据同步
        return null;
    }
    //⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐同步数据⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐

    //⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐sql测试⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐
    @PostMapping("/sql/test")
    public Object sqlTest() {
        //TODO:sql语句测试
        return null;
    }
    //⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐sql测试⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐


    //⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐默认配置⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐
    @PostMapping("/default/project/{projectId}")
    public Object defaultConfig(@PathVariable Long projectId, @RequestBody List<DefaultConfig> defaultConfigs) {
        //TODO:准备好默认配置对象即可
        return null;
    }

    /**
     * 将整套配置保存为默认配置
     */
    @PostMapping("/default/save")
    public Object saveAsDefault() {
        return null;
    }
    //⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐默认配置⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐


    @PostMapping("/tags")
    public Object tags() {
        HashMap<String, Object> map = new HashMap<String, Object>() {{
            put("id", 121231312L);
            put("name", "新闻");
        }};
        return new ArrayList<Tags>() {{
            add(new Tags().setHotList(Collections.singletonList(new HashMap<String, String>() {{
                put("label", "1");
                put("name", "热词1");
            }})).setResource(map));
        }};

    }
}

