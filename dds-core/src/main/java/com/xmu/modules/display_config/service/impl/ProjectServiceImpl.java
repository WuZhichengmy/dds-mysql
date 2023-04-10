package com.xmu.modules.display_config.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmu.exception.EntityNotFoundException;
import com.xmu.modules.display_config.domain.DisplayWidgets;
import com.xmu.modules.display_config.domain.Project;
import com.xmu.modules.display_config.domain.Resource;
import com.xmu.modules.display_config.mapper.ProjectMapper;
import com.xmu.modules.display_config.request.ProjectDTO;
import com.xmu.modules.display_config.service.DisplayWidgetsService;
import com.xmu.modules.display_config.service.ProjectService;
import com.xmu.modules.display_config.service.ResourceService;
import com.xmu.modules.display_config.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author Xing
 */
@Service
@Slf4j
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private DisplayWidgetsService displayWidgetsService;

    @Override
    public Object creatProject(ProjectDTO projectDTO) {
        Project project = projectDTO.project();
//        List<Resource> resources = projectDTO.resources();
        boolean save = this.save(project);
//        boolean b = resourceService.saveBatch(resources);
        if (save) {
            Long id = project.getId();
            String name = project.getName();
            return new Response<>().setData(new HashMap<String, Object>() {{
                put("id", id);
                put("name", name);
            }});
        } else {
            return new Response<>().setCode(500).setMsg("project save failed");
        }
    }

    @Override
    public Object deleteProject(Long projectId) {
        Project project = this.getById(projectId);
        if (project == null) {
            throw new EntityNotFoundException(Project.class, "id", projectId);
        }
        List<Resource> resources = resourceService.list(Wrappers.<Resource>lambdaQuery()
                .eq(Resource::getProjectId, projectId));
        resources.forEach(resource -> {
            resourceService.removeResources(resource);
        });
        // 删除组件配置
        displayWidgetsService.remove(Wrappers.<DisplayWidgets>lambdaQuery()
                .eq(DisplayWidgets::getProjectId, projectId));
        return this.removeById(projectId);
    }
}
