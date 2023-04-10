package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Xing
 */
@Data
public class RuleSetDTO {

    @ApiModelProperty(value = "id为空时新增,不为空时修改")
    private Long id;

    @ApiModelProperty(value = "项目id", required = true)
    @NotNull
    private Long projectId;

    @ApiModelProperty(value = "资源id", required = true)
    @NotNull
    private Long resourceId;

    @ApiModelProperty(value = "规则集名称", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "规则集描述信息")
    private String description;

    private Date createTime;
    private Date modifyTime;

}
