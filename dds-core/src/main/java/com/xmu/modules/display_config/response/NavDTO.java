package com.xmu.modules.display_config.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;
import java.util.List;

/**
 * @Author: Yifei Wang
 * @Date: 2021/4/10 16:57
 */

@Data
public class NavDTO {

    private Long id;

    @ApiModelProperty("导航一级名称")
    private String name;

    @ApiModelProperty("导航二级列表")
    private List<Map<String,Object>> subList;
}
