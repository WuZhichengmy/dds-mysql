package com.xmu.modules.display_config.controller;

import com.alibaba.fastjson.JSONObject;
import com.xmu.modules.display_config.request.AdvancedSearchResourceDTO;
import com.xmu.modules.display_config.request.AdvancedStatisticsDTO;
import com.xmu.modules.display_config.request.ExportDataDTO;
import com.xmu.modules.display_config.response.*;
import com.xmu.modules.display_config.service.ResourceService;
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

@RestController
@Api(tags = "检索模块")
public class SearchController {
    @Autowired
    private ResourceService resourceService;
    @ApiOperation("获取高级检索配置")
    @GetMapping("/resource/{projectId}/getAdvancedSearch/{type}")
    public ResponseEntity<List<AdvancedSearchConfigDTO>> getAdvancedSearch(@PathVariable Long projectId, @PathVariable Long type) {
        return new ResponseEntity<>(resourceService.getAdvancedSearch(projectId, type), HttpStatus.OK);
    }

    @ApiOperation("高级检索")
    @PostMapping("/resource/advancedSearch")
    public ResponseEntity<SearchResultDTO> advancedSearch(@Valid @RequestBody AdvancedSearchResourceDTO advancedSearchResource) {
        return new ResponseEntity<>(resourceService.advancedSearch(advancedSearchResource), HttpStatus.OK);
    }

    @ApiOperation("高级统计")
    @PostMapping("/resource/advancedStatistics")
    public ResponseEntity<List<StatisticsResultDTO>> advancedStatistics(@Valid @RequestBody AdvancedStatisticsDTO advancedStatistics) {
        return new ResponseEntity<>(resourceService.advancedStatistics(advancedStatistics), HttpStatus.OK);
    }

    @ApiOperation("获取检索结果显示字段")
    @GetMapping("/resource/{resourceId}/search/fields")
    public ResponseEntity<List<SearchFieldDTO>> getSearchFields(@PathVariable Long resourceId) {
        return new ResponseEntity<>(resourceService.getSearchFields(resourceId), HttpStatus.OK);
    }

    @ApiOperation("获取结果分析字段")
    @GetMapping("/resource/{resourceId}/getSearchStatisticsFields")
    public ResponseEntity<List<SearchStatisticsFieldDTO>> getSearchStatisticsFields(@PathVariable Long resourceId) {
        return new ResponseEntity<>(resourceService.getSearchStatisticsFields(resourceId), HttpStatus.OK);
    }

    @ApiOperation("获取检索详情显示字段")
    @GetMapping("/resource/{resourceId}/detail/fields")
    public ResponseEntity<List<DetailFieldDTO>> getDetailFields(@PathVariable Long resourceId) {
        return new ResponseEntity<>(resourceService.getDetailFields(resourceId), HttpStatus.OK);
    }

    @ApiOperation("获取数据详情")
    @GetMapping("/resource/{resourceId}/result/{id}")
    public ResponseEntity<List<JSONObject>> getDetail(@PathVariable Long resourceId, @PathVariable Long id) {
        return new ResponseEntity<>(resourceService.getDetail(resourceId, id), HttpStatus.OK);
    }

    @PostMapping("/resource/exportData")
    @ApiOperation("导出数据")
    public void exportData(@Valid @RequestBody ExportDataDTO exportDataDTO, HttpServletRequest request, HttpServletResponse response) {
        resourceService.exportData(exportDataDTO, request, response);
    }
}
