package com.xmu.modules.display_config.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author Xing
 */
@Data
public class WidgetDTO {

    @ApiModelProperty("组件id")
    private Long id;

    @ApiModelProperty("组件名称")
    private String name;

    @ApiModelProperty("组件类型")
    private Integer type;

    @ApiModelProperty("组件数据")
    private Map<String,Object> data;

    @ApiModelProperty("组件配置信息")
    private Map<String,Object> config;

}
