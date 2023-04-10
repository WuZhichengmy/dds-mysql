package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 高级统计
 * @author Xing
 */
@Data
public class AdvancedStatisticsDTO {

    @NotNull
    @ApiModelProperty(value = "资源id", required = true)
    private Long resourceId;

    @ApiModelProperty(value = "检索组合条件")
    private List<SearchLogicDTO> searchLogic;

    @ApiModelProperty("精炼条件")
    private List<ESRefineDTO> refines;

    @NotBlank
    @ApiModelProperty(value = "统计字段", required = true)
    private String aggregationField;

}
