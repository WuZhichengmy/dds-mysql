package com.xmu.modules.display_config.controller;

import com.xmu.modules.display_config.request.SearchConfigDTO;
import com.xmu.modules.display_config.service.SearchConfigService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Xing
 */
@RestController
@RequestMapping("/search/config")
@Api(tags = "检索配置")
public class SearchConfigController {

    @Autowired
    private SearchConfigService searchConfigService;

    /**
     * search_config
     */
    @PostMapping("/project/{projectId}")
    public Object searchConfig(@PathVariable Long projectId, @RequestBody List<SearchConfigDTO> searchConfigDTOS) {
        return new ResponseEntity<>(searchConfigService.searchConfig(projectId, searchConfigDTOS), HttpStatus.OK);
    }

    @GetMapping("/project/{projectId}/resource/{resourceId}")
    public Object getSearchConfig(@PathVariable Long projectId, @PathVariable Long resourceId) {
        return new ResponseEntity<>(searchConfigService.getSearchConfig(projectId, resourceId), HttpStatus.OK);
    }

}
