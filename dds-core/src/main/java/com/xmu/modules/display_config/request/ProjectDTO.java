package com.xmu.modules.display_config.request;

import com.xmu.modules.display_config.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProjectDTO {
    private String name;
    private String code;
    private String description;
    private Integer source;
//    private List<ResourceInfoDTO> resources;

    public Project project(){
        Project project = new Project()
                .setCode(code).setDescription(description).setName(name).setSource(source);
        project.setCreateTime(new Date());
        project.setCreateUser("admin");
        project.setModifyTime(new Date());
        project.setModifyUser("admin");
        return project;
    }

//    public List<Resource> resources(){
//        return resources.stream().map(ResourceInfoDTO::resource).collect(Collectors.toList());
//    }
}
