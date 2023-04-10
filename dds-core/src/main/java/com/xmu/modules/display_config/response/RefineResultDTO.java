package com.xmu.modules.display_config.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 精炼结果
 * @author Xing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefineResultDTO {

    @ApiModelProperty("精炼标题")
    private String title;

    @ApiModelProperty("精炼字段")
    private String field;

    @ApiModelProperty("精炼结果")
    List<ESStatisticsResultDTO> esStatisticsResults;

}
