package com.xmu.modules.display_config.request;

import com.xmu.modules.display_config.domain.AnalysisMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author summer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MenuConfigDTO {

    @NotNull
    @ApiModelProperty(value = "资源id", required = true)
    private Long resourceId;

    @NotBlank
    @ApiModelProperty(value = "目录标题", required = true)
    private String title;

    @NotNull
    @ApiModelProperty(value = "是否父节点", required = true)
    private Integer parent;

    @ApiModelProperty(value = "父节点id")
    private Long parentId;

    public AnalysisMenu toAnalysisMenu(Long projectId) {
        return new AnalysisMenu()
                .setIsParent(parent)
                .setProjectId(projectId)
                .setTitle(title)
                .setResourceId(resourceId)
                .setParentId(parentId);
    }
}
