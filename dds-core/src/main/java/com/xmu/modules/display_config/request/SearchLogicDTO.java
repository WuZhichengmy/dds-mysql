package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xing
 */
@Data
public class SearchLogicDTO {

    @ApiModelProperty(value = "检索条件,枚举: AND/OR/NOT", required = true)
    private String logic;

    @ApiModelProperty(value = "检索字段", required = true)
    private String field;

    @ApiModelProperty(value = "检索关键词", required = true)
    private String keyword;

}
