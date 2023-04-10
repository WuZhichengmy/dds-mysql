package com.xmu.modules.display_config.controller;

import com.xmu.annotation.AnonymousAccess;
import com.xmu.modules.display_config.request.CategoryDTO;
import com.xmu.modules.display_config.request.WidgetConfig;
import com.xmu.modules.display_config.service.DisplayWidgetsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Xing
 */
@RestController
@RequestMapping("/widgets")
@Api(tags = "展示组件配置")
public class DisplayWidgetsController {

    @Autowired
    private DisplayWidgetsService displayWidgetsService;

    /**
     * ⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐核心⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐
     */
    @ApiOperation("获取项目组件数据")
    @AnonymousAccess
    @GetMapping("{projectId}/widgets/{id}")
    public Object getWidgets(@PathVariable Long projectId, @PathVariable Long id) {
        return new ResponseEntity<>(displayWidgetsService.getWidgets(projectId, id), HttpStatus.OK);
    }

    @PostMapping("/preview/widgets/")
    public Object previewWidgets(@RequestBody WidgetConfig<List<CategoryDTO>> widgetConfig) {
        return new ResponseEntity<>(displayWidgetsService.previewWidgetsO(widgetConfig), HttpStatus.OK);
    }

    @PostMapping("/get")
    public Object getWidgetsConfig(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(displayWidgetsService.getWidgetsConfig(ids), HttpStatus.OK);
    }

}
