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
public class ResourcePropertyDTO {

    @ApiModelProperty(value = "id为空时新增,不为空时修改")
    private Long id;

    @ApiModelProperty(value = "项目id", required = true)
    @NotNull
    private Long projectId;

    @ApiModelProperty(value = "资源id", required = true)
    @NotNull
    private Long resourceId;

    @ApiModelProperty(value = "字段名称", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "字段类型 0:VARCHAR " +
            "1:INT " +
            "2:BIGINT " +
            "3:DECIMAL " +
            "4:DATETIME " +
            "5:TEXT " +
            "6:LONGTEXT ", required = true)
    @NotNull
    private Integer type;

    private Integer isDefault;
    private Date createTime;
    private Date modifyTime;

}
