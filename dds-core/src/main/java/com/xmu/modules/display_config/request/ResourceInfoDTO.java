package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ResourceInfoDTO {

    @NotBlank
    @ApiModelProperty(value = "资源名称", required = true)
    private String name;

    @NotBlank
    @ApiModelProperty(value = "表名", required = true)
    private String target;

    @NotBlank
    @ApiModelProperty(value = "实体名称", required = true)
    private String entity;

    @NotNull
    @ApiModelProperty(value = "资源名称", required = true)
    private Integer isDefault;

    @ApiModelProperty(value = "资源描述")
    private String description;

    @NotNull
    @ApiModelProperty(value = "资源图标", required = true)
    private String icon;

}
