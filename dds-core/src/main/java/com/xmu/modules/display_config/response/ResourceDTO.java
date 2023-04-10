package com.xmu.modules.display_config.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Xing
 */
@Data
public class ResourceDTO {

    @ApiModelProperty("资源id")
    private Long id;

    @ApiModelProperty("资源名称")
    private String name;

    @ApiModelProperty("资源对象")
    private String target;

    @ApiModelProperty("实体名")
    private String entity;

    @ApiModelProperty("是否默认资源")
    private Integer isDefault;

    @ApiModelProperty("描述")
    private String description;

    private Date createTime;

    private Date modifyTime;

    @ApiModelProperty("图标")
    private String icon;
}
