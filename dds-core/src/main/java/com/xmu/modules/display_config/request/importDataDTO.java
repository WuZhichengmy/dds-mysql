package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Xing
 */
@Data
public class importDataDTO {

    @ApiModelProperty(value = "资源id", required = true)
    @NotNull
    private Long resourceId;

    @ApiModelProperty(value = "规则id", required = true)
    @NotNull
    private Long ruleSetId;

    @ApiModelProperty(value = "文件id", required = true)
    @NotNull
    private Long localStorageId;

}
