package com.xmu.modules.display_config.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xing
 */
@Data
public class DetailFieldDTO {

    private Long id;

    @ApiModelProperty("字段名称")
    private String name;

    @ApiModelProperty("字段显示名称")
    private String label;

    @ApiModelProperty("字段显示顺序")
    private Integer orderNumber;
}
