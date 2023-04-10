package com.xmu.modules.display_config.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计结果
 * @author Xing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsResultDTO {

    @ApiModelProperty("结果名称")
    private String key;

    @ApiModelProperty("结果数量")
    private Long value;

    @ApiModelProperty("占比")
    private String rate;

}
