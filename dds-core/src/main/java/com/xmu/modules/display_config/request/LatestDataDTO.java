package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 最新资源
 * @author Xing
 */
@Data
public class LatestDataDTO {

    @NotNull
    @ApiModelProperty(value = "资源id", required = true)
    private Long resourceId;

    @NotEmpty
    @ApiModelProperty(value = "排序字段", required = true)
    private List<String> orderColumns;

    @NotNull
    @ApiModelProperty(value = "数量", required = true)
    private Integer limit;

}
