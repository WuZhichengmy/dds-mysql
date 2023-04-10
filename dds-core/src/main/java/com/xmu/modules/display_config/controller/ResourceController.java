package com.xmu.modules.display_config.controller;

import com.alibaba.fastjson.JSONObject;
import com.xmu.modules.display_config.domain.Resource;
import com.xmu.modules.display_config.request.*;
import com.xmu.modules.display_config.response.*;
import com.xmu.modules.display_config.service.ResourceService;
import com.xmu.modules.display_config.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Xing
 */
@RestController
@RequestMapping("/resource")
@Api(tags = "项目数据配置")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping("/page")
    public Object page(@RequestBody PageDTO page) {
        return new ResponseEntity<>(new Response<>().setData(resourceService.pageRet(page)), HttpStatus.OK);
    }

    @ApiOperation("获取项目数据资源")
    @GetMapping("/{projectId}/resources")
    public Object getResources(@PathVariable Long projectId) {
        return new ResponseEntity<>(new Response<>().setData(resourceService.getResources(projectId)), HttpStatus.OK);
    }

    /**
     * drop 基础、首页、检索、resource property、rule set
     */
    @ApiOperation("删除资源")
    @DeleteMapping("/{resourceId}")
    public Object delete(@PathVariable Long resourceId) {
        Resource resource = resourceService.getById(resourceId);
        if (resource == null) {
            return new Response<>().setData(true);
        }
        resourceService.removeResources(resource);
        return new Response<>().setData(true);
    }

    @ApiOperation("创建资源")
    @PostMapping("/{projectId}")
    public ResponseEntity addResources(@PathVariable Long projectId, @RequestBody ResourceInfoDTO resourceDTO) {
        return resourceService.addResources(projectId, resourceDTO);
    }

    @ApiOperation("修改资源")
    @PutMapping("/{resourceId}")
    public Object updateResource(@PathVariable Long resourceId, @Valid @RequestBody ResourceInfoDTO resourceDTO) {
        resourceService.updateResource(resourceId, resourceDTO);
        return new Response<>();
    }

    @ApiOperation("获取资源结构")
    @GetMapping("/columns/{resourceId}")
    public Object getColumns(@PathVariable Long resourceId, @RequestParam boolean useId) {
        return new ResponseEntity<>(new Response<>().setData(resourceService.getColumns(resourceId, useId)), HttpStatus.OK);
    }

    @ApiOperation("获取高级检索配置")
    @GetMapping("/{projectId}/getAdvancedSearch/{type}")
    public ResponseEntity<List<AdvancedSearchConfigDTO>> getAdvancedSearch(@PathVariable Long projectId, @PathVariable Long type) {
        return new ResponseEntity<>(resourceService.getAdvancedSearch(projectId, type), HttpStatus.OK);
    }

    @ApiOperation("高级检索")
    @PostMapping("/advancedSearch")
    public ResponseEntity<SearchResultDTO> advancedSearch(@Valid @RequestBody AdvancedSearchResourceDTO advancedSearchResource) {
        return new ResponseEntity<>(resourceService.advancedSearch(advancedSearchResource), HttpStatus.OK);
    }

    @ApiOperation("高级统计")
    @PostMapping("/advancedStatistics")
    public ResponseEntity<List<StatisticsResultDTO>> advancedStatistics(@Valid @RequestBody AdvancedStatisticsDTO advancedStatistics) {
        return new ResponseEntity<>(resourceService.advancedStatistics(advancedStatistics), HttpStatus.OK);
    }

    @ApiOperation("获取检索结果显示字段")
    @GetMapping("/{resourceId}/search/fields")
    public ResponseEntity<List<SearchFieldDTO>> getSearchFields(@PathVariable Long resourceId) {
        return new ResponseEntity<>(resourceService.getSearchFields(resourceId), HttpStatus.OK);
    }

    @ApiOperation("获取结果分析字段")
    @GetMapping("/{resourceId}/getSearchStatisticsFields")
    public ResponseEntity<List<SearchStatisticsFieldDTO>> getSearchStatisticsFields(@PathVariable Long resourceId) {
        return new ResponseEntity<>(resourceService.getSearchStatisticsFields(resourceId), HttpStatus.OK);
    }

    @ApiOperation("获取检索详情显示字段")
    @GetMapping("/{resourceId}/detail/fields")
    public ResponseEntity<List<DetailFieldDTO>> getDetailFields(@PathVariable Long resourceId) {
        return new ResponseEntity<>(resourceService.getDetailFields(resourceId), HttpStatus.OK);
    }

    @ApiOperation("获取数据详情")
    @GetMapping("/{resourceId}/result/{id}")
    public ResponseEntity<List<JSONObject>> getDetail(@PathVariable Long resourceId, @PathVariable Long id) {
        return new ResponseEntity<>(resourceService.getDetail(resourceId, id), HttpStatus.OK);
    }

    @PostMapping("/exportData")
    @ApiOperation("导出数据")
    public void exportData(@Valid @RequestBody ExportDataDTO exportDataDTO, HttpServletRequest request, HttpServletResponse response) {
        resourceService.exportData(exportDataDTO, request, response);
    }

    @ApiOperation("修改资源")
    @PutMapping
    public Object modifyResource(Resource resource) {
        return new ResponseEntity<>(resourceService.updateById(resource), HttpStatus.OK);
    }

    @PostMapping("/importData")
    @ApiOperation("导入数据")
    public ResponseEntity importData(@Valid @RequestBody importDataDTO importDataDTO) {
        return resourceService.importData(importDataDTO);
    }

}
