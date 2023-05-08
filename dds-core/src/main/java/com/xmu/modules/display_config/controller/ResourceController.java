package com.xmu.modules.display_config.controller;

import com.alibaba.fastjson.JSONObject;
import com.xmu.modules.display_config.domain.Resource;
import com.xmu.modules.display_config.request.*;
import com.xmu.modules.display_config.response.*;
import com.xmu.modules.display_config.service.*;
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
@Api(tags = "资源管理模块")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping("/resource/page")
    public Object page(@RequestBody PageDTO page) {
        return new ResponseEntity<>(new Response<>().setData(resourceService.pageRet(page)), HttpStatus.OK);
    }

    @ApiOperation("获取项目数据资源")
    @GetMapping("/resource/{projectId}/resources")
    public Object getResources(@PathVariable Long projectId) {
        return new ResponseEntity<>(new Response<>().setData(resourceService.getResources(projectId)), HttpStatus.OK);
    }

    /**
     * drop 基础、首页、检索、resource property、rule set
     */
    @ApiOperation("删除资源")
    @DeleteMapping("/resource/{resourceId}")
    public Object delete(@PathVariable Long resourceId) {
        Resource resource = resourceService.getById(resourceId);
        if (resource == null) {
            return new Response<>().setData(true);
        }
        resourceService.removeResources(resource);
        return new Response<>().setData(true);
    }

    @ApiOperation("创建资源")
    @PostMapping("/resource/{projectId}")
    public ResponseEntity addResources(@PathVariable Long projectId, @RequestBody ResourceInfoDTO resourceDTO) {
        return resourceService.addResources(projectId, resourceDTO);
    }

    @ApiOperation("修改资源")
    @PutMapping("/resource/{resourceId}")
    public Object updateResource(@PathVariable Long resourceId, @Valid @RequestBody ResourceInfoDTO resourceDTO) {
        resourceService.updateResource(resourceId, resourceDTO);
        return new Response<>();
    }

    @ApiOperation("获取资源结构")
    @GetMapping("/resource/columns/{resourceId}")
    public Object getColumns(@PathVariable Long resourceId, @RequestParam boolean useId) {
        return new ResponseEntity<>(new Response<>().setData(resourceService.getColumns(resourceId, useId)), HttpStatus.OK);
    }

    @ApiOperation("修改资源")
    @PutMapping("/resource")
    public Object modifyResource(Resource resource) {
        return new ResponseEntity<>(resourceService.updateById(resource), HttpStatus.OK);
    }

    @PostMapping("/resource/importData")
    @ApiOperation("导入数据")
    public ResponseEntity importData(@Valid @RequestBody importDataDTO importDataDTO) {
        return resourceService.importData(importDataDTO);
    }

    /*
     * 原导入模块
     */
    @Autowired
    private ImportPropertyService importPropertyService;
    @Autowired
    private ImportTaskService importTaskService;
    @Autowired
    private ResourcePropertyService resourcePropertyService;
    @Autowired
    private RuleSetService ruleSetService;

    @GetMapping("/import/listResourceProperty/{projectId}/{resourceId}")
    @ApiOperation("资源属性:获取属性列表")
    public ResponseEntity<List<ResourcePropertyDTO>> listResourceProperty(@PathVariable Long projectId, @PathVariable Long resourceId) {
        return resourcePropertyService.listResourceProperty(projectId, resourceId);
    }

    @PostMapping("/import/saveOrUpdateResourceProperty")
    @ApiOperation("资源属性:创建/修改")
    public ResponseEntity saveOrUpdateResourceProperty(@Valid @RequestBody ResourcePropertyDTO resourcePropertyDTO) {
        return resourcePropertyService.saveOrUpdateResourceProperty(resourcePropertyDTO);
    }

    @DeleteMapping("/import/deleteResourceProperty/{resourceId}/{id}")
    @ApiOperation("资源属性:删除")
    public ResponseEntity deleteProperty(@PathVariable Long resourceId, @PathVariable Long id) {
        return resourcePropertyService.deleteResourceProperty(resourceId, id);
    }

    @GetMapping("/import/listRuleSet/{resourceId}")
    @ApiOperation("规则集:获取资源所有规则集")
    public ResponseEntity<List<RuleSetDTO>> listRuleSet(@PathVariable Long resourceId) {
        return ruleSetService.listRuleSet(resourceId);
    }

    @PostMapping("/import/saveOrUpdateRuleSet")
    @ApiOperation("规则集:创建/修改")
    public ResponseEntity<Long> saveOrUpdateRuleSet(@Valid @RequestBody RuleSetDTO ruleSetDTO) {
        return ruleSetService.saveOrUpdateRuleSet(ruleSetDTO);
    }

    @DeleteMapping("/import/deleteRuleSet/{id}")
    @ApiOperation("规则集:删除")
    public ResponseEntity deleteRuleSet(@PathVariable Long id) {
        return ruleSetService.deleteRuleSet(id);
    }

    @GetMapping("/import/listImportProperty/{ruleSetId}")
    @ApiOperation("导入规则:获取规则集下的所有规则")
    public ResponseEntity<List<ImportPropertyDTO>> listImportProperty(@PathVariable Long ruleSetId) {
        return new ResponseEntity<>(importPropertyService.listImportProperty(ruleSetId), HttpStatus.OK);
    }

    @PostMapping("/import/saveImportProperty")
    @ApiOperation("导入规则:创建/修改")
    public ResponseEntity saveImportProperty(@Valid @RequestBody SaveImportPropertyDTO saveImportPropertyDTO) {
        return importPropertyService.saveImportProperty(saveImportPropertyDTO);
    }

    @DeleteMapping("/import/deleteImportProperty/{id}")
    @ApiOperation("导入规则:删除")
    public ResponseEntity deleteImportProperty(@PathVariable Long id) {
        importPropertyService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/import/listImportTask/{resourceId}")
    @ApiOperation("导入任务:获取资源导入记录")
    public ResponseEntity<List<ImportTaskDTO>> listImportTask(@PathVariable Long resourceId) {
        return importTaskService.listImportTask(resourceId);
    }

    @DeleteMapping("/import/deleteImportTask/{id}")
    @ApiOperation("导入任务:删除")
    public ResponseEntity deleteImportTask(@PathVariable Long id) {
        importTaskService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/import/download")
    @ApiOperation("导入任务:下载文件")
    public void downloadFile(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) {
        importTaskService.downloadFile(fileName, request, response);
    }
}
