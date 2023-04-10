package com.xmu.modules.display_config.controller;

import com.xmu.modules.display_config.request.*;
import com.xmu.modules.display_config.service.ImportPropertyService;
import com.xmu.modules.display_config.service.ImportTaskService;
import com.xmu.modules.display_config.service.ResourcePropertyService;
import com.xmu.modules.display_config.service.RuleSetService;
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
@RequestMapping("/import")
@Api(tags = "导入模块")
public class ImportController {

    @Autowired
    private ImportPropertyService importPropertyService;
    @Autowired
    private ImportTaskService importTaskService;
    @Autowired
    private ResourcePropertyService resourcePropertyService;
    @Autowired
    private RuleSetService ruleSetService;

    @GetMapping("/listResourceProperty/{projectId}/{resourceId}")
    @ApiOperation("资源属性:获取属性列表")
    public ResponseEntity<List<ResourcePropertyDTO>> listResourceProperty(@PathVariable Long projectId, @PathVariable Long resourceId) {
        return resourcePropertyService.listResourceProperty(projectId, resourceId);
    }

    @PostMapping("/saveOrUpdateResourceProperty")
    @ApiOperation("资源属性:创建/修改")
    public ResponseEntity saveOrUpdateResourceProperty(@Valid @RequestBody ResourcePropertyDTO resourcePropertyDTO) {
        return resourcePropertyService.saveOrUpdateResourceProperty(resourcePropertyDTO);
    }

    @DeleteMapping("/deleteResourceProperty/{resourceId}/{id}")
    @ApiOperation("资源属性:删除")
    public ResponseEntity deleteProperty(@PathVariable Long resourceId, @PathVariable Long id) {
        return resourcePropertyService.deleteResourceProperty(resourceId, id);
    }

    @GetMapping("/listRuleSet/{resourceId}")
    @ApiOperation("规则集:获取资源所有规则集")
    public ResponseEntity<List<RuleSetDTO>> listRuleSet(@PathVariable Long resourceId) {
        return ruleSetService.listRuleSet(resourceId);
    }

    @PostMapping("/saveOrUpdateRuleSet")
    @ApiOperation("规则集:创建/修改")
    public ResponseEntity<Long> saveOrUpdateRuleSet(@Valid @RequestBody RuleSetDTO ruleSetDTO) {
        return ruleSetService.saveOrUpdateRuleSet(ruleSetDTO);
    }

    @DeleteMapping("/deleteRuleSet/{id}")
    @ApiOperation("规则集:删除")
    public ResponseEntity deleteRuleSet(@PathVariable Long id) {
        return ruleSetService.deleteRuleSet(id);
    }

    @GetMapping("/listImportProperty/{ruleSetId}")
    @ApiOperation("导入规则:获取规则集下的所有规则")
    public ResponseEntity<List<ImportPropertyDTO>> listImportProperty(@PathVariable Long ruleSetId) {
        return new ResponseEntity<>(importPropertyService.listImportProperty(ruleSetId), HttpStatus.OK);
    }

    @PostMapping("/saveImportProperty")
    @ApiOperation("导入规则:创建/修改")
    public ResponseEntity saveImportProperty(@Valid @RequestBody SaveImportPropertyDTO saveImportPropertyDTO) {
        return importPropertyService.saveImportProperty(saveImportPropertyDTO);
    }

    @DeleteMapping("/deleteImportProperty/{id}")
    @ApiOperation("导入规则:删除")
    public ResponseEntity deleteImportProperty(@PathVariable Long id) {
        importPropertyService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/listImportTask/{resourceId}")
    @ApiOperation("导入任务:获取资源导入记录")
    public ResponseEntity<List<ImportTaskDTO>> listImportTask(@PathVariable Long resourceId) {
        return importTaskService.listImportTask(resourceId);
    }

    @DeleteMapping("/deleteImportTask/{id}")
    @ApiOperation("导入任务:删除")
    public ResponseEntity deleteImportTask(@PathVariable Long id) {
        importTaskService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/download")
    @ApiOperation("导入任务:下载文件")
    public void downloadFile(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) {
        importTaskService.downloadFile(fileName, request, response);
    }
}
