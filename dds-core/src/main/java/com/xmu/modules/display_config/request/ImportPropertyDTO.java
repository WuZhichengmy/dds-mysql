package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xing
 */
@Data
public class ImportPropertyDTO {

    @ApiModelProperty(value = "id为空时新增,不为空时修改")
    private Long id;

    @ApiModelProperty(value = "规则集id", required = true)
    private Long ruleSetId;

    @ApiModelProperty(value = "excel表头", required = true)
    private String excelHeader;

    @ApiModelProperty(value = "资源属性id", required = true)
    private Long resourcePropertyId;

    @ApiModelProperty(value = "资源属性名称")
    private String propertyName;

}
