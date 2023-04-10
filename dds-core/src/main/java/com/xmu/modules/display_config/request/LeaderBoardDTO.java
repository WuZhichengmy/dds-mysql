package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 排行榜组件
 * @author summer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class LeaderBoardDTO {

    @NotNull
    @ApiModelProperty(value = "资源id", required = true)
    private Long resourceId;

    @NotBlank
    @ApiModelProperty(value = "排序字段", required = true)
    private String orderColumn;

    @NotBlank
    @ApiModelProperty(value = "排序方式", required = true)
    private String orderType;

    @NotNull
    @ApiModelProperty(value = "数量", required = true)
    private Integer limit;

}
