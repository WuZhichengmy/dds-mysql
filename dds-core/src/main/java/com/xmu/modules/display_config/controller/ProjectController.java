package com.xmu.modules.display_config.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xmu.modules.display_config.domain.Project;
import com.xmu.modules.display_config.request.ProjectDTO;
import com.xmu.modules.display_config.service.ProjectService;
import com.xmu.modules.display_config.utils.Response;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Xing
 */
@RestController
@RequestMapping("/Project")
@Api(tags = "项目模块")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public Object createObject(@RequestBody ProjectDTO project) {
        return new ResponseEntity<>(projectService.creatProject(project), HttpStatus.OK);
    }

    @GetMapping("/all")
    public Object getProjects() {
        List<Project> projects = projectService.list(new QueryWrapper<>());
        return new ResponseEntity<>(new Response<>().setData(projects), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public Object getProject(@PathVariable Long id) {
        return new ResponseEntity<>(new Response<>().setData(projectService.getById(id)), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public Object modifyProject(@PathVariable Long id, @RequestBody Project project) {
        Project projectTmp = projectService.getById(id);
        if (projectTmp == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BeanUtils.copyProperties(project, projectTmp);
        return new ResponseEntity<>(projectService.updateById(projectTmp), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public Object deleteProject(@PathVariable Long id) {
        return new ResponseEntity<>(projectService.deleteProject(id), HttpStatus.OK);
    }
}
