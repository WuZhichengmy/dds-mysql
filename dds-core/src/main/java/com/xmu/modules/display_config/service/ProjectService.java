package com.xmu.modules.display_config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmu.modules.display_config.domain.Project;
import com.xmu.modules.display_config.request.ProjectDTO;

/**
 * @author Xing
 */
public interface ProjectService extends IService<Project> {

    Object creatProject(ProjectDTO projectDTO);

    Object deleteProject(Long id);
}
