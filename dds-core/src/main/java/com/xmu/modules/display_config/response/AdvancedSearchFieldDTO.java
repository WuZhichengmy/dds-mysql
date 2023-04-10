package com.xmu.modules.display_config.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 高级检索的字段
 * @author Xing
 */
@Data
@Accessors(chain = true)

public class AdvancedSearchFieldDTO {

    @ApiModelProperty("字段名称")
    private String name;

    @ApiModelProperty("字段显示名称")
    private String label;
}
