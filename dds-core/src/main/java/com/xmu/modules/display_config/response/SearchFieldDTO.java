package com.xmu.modules.display_config.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xing
 */
@Data
public class SearchFieldDTO {

    @ApiModelProperty("字段id")
    private Long id;

    @ApiModelProperty("字段名称")
    private String name;

    @ApiModelProperty("字段显示名称")
    private String label;

    @ApiModelProperty("字段排序")
    private Integer orderNumber;

    @ApiModelProperty("超出隐藏字数")
    private Integer maxWords;
}
