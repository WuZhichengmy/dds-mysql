package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Xing
 */
@Data
public class SimpleImportPropertyDTO {

    @ApiModelProperty(value = "excel表头", required = true)
    @NotBlank
    private String excelHeader;

    @ApiModelProperty(value = "资源属性id", required = true)
    @NotNull
    private Long resourcePropertyId;

}
