package com.xmu.modules.display_config.controller;

import com.xmu.annotation.AnonymousAccess;
import com.xmu.modules.display_config.request.AnalysisDetailDTO;
import com.xmu.modules.display_config.request.MenuConfigDTO;
import com.xmu.modules.display_config.request.SearchInfo;
import com.xmu.modules.display_config.service.AnalysisMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Xing
 */
@RestController
@RequestMapping("/analysis")
@Api(tags = "分析报告模块")
public class AnalysisController {

    @Autowired
    private AnalysisMenuService analysisMenuService;

    /**
     * 分析展示配置
     */
    @PostMapping("/menu/project/{projectId}")
    @ApiOperation("分析展示:保存分析目录")
    public Object menuConfig(@PathVariable Long projectId, @Valid @RequestBody List<MenuConfigDTO> configDTOs) {
        return new ResponseEntity<>(analysisMenuService.menuConfig(projectId, configDTOs), HttpStatus.OK);
    }

    @GetMapping("/menu/project/{projectId}/resource/{resourceId}")
    @ApiOperation("分析展示:获取分析目录")
    public Object getMenu(@PathVariable Long projectId, @PathVariable Long resourceId) {
        return new ResponseEntity<>(analysisMenuService.getMenu(projectId, resourceId), HttpStatus.OK);
    }

    @PostMapping("/menu/{menuId}")
    @ApiOperation("分析展示:修改分析目录")
    public Object modifyMenu(@PathVariable Long menuId, @RequestBody MenuConfigDTO config) {
        return new ResponseEntity<>(analysisMenuService.modifyMenu(menuId, config), HttpStatus.OK);
    }

    @PostMapping("/detail/save")
    @ApiOperation("分析展示:保存分析详情")
    public Object detailConfig(@Valid @RequestBody List<AnalysisDetailDTO<?>> details) {
        return new ResponseEntity<>(analysisMenuService.detailConfig(details), HttpStatus.OK);
    }

    @GetMapping("/detail/{resourceId}/{menuId}")
    @ApiOperation("分析展示:获取详情配置")
    public Object getDetail(@PathVariable Long resourceId, @PathVariable Long menuId) {
        return new ResponseEntity<>(analysisMenuService.getDetail(resourceId, menuId), HttpStatus.OK);
    }

    @PostMapping("/detail/{menuId}/result")
    @ApiOperation("分析展示:获取分析结果")
    @AnonymousAccess
    // searchInfo暂时是一个没用的属性, 因为精炼条件暂时在前端没有导入
    public Object getAnalysis(@PathVariable Long menuId, @Valid @RequestBody SearchInfo searchInfo) {
        return new ResponseEntity<>(analysisMenuService.getAnalysisData(menuId, searchInfo), HttpStatus.OK);
    }


    @DeleteMapping("/menu")
    @ApiOperation("分析展示:删除目录")
    public Object deleteMenu(@RequestBody List<Long> menuIds) {
        return new ResponseEntity<>(analysisMenuService.deleteMenu(menuIds), HttpStatus.OK);
    }


}
