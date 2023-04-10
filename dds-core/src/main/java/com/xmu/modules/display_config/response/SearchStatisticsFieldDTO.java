package com.xmu.modules.display_config.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xing
 */
@Data
public class SearchStatisticsFieldDTO {

    @ApiModelProperty("字段名称")
    private String name;

    @ApiModelProperty("字段显示名称")
    private String label;
}
