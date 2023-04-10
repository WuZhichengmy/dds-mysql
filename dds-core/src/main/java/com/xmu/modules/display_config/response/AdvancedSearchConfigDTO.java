package com.xmu.modules.display_config.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 高级检索配置信息
 * @author Xing
 */
@Data
public class AdvancedSearchConfigDTO {

    @ApiModelProperty("资源id")
    private Long resourceId;

    @ApiModelProperty("资源名称")
    private String name;

    @ApiModelProperty("资源检索字段")
    private List<AdvancedSearchFieldDTO> advancedSearchFields;
}
