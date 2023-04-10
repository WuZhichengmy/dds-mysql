package com.xmu.modules.display_config.response;

import com.xmu.modules.display_config.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProjectInfoDTO {
    private String name;
    private Long id;
    public ProjectInfoDTO(Project project){
        this.name=project.getName();
        this.id=project.getId();
    }
}
