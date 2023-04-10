package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xing
 */
@Data
public class ImportTaskDTO {

    private Long id;

    @ApiModelProperty("规则名称")
    private String ruleSetName;

    @ApiModelProperty("文件名称")
    private String sourceFileName;

    @ApiModelProperty("执行时长")
    private Long executionTime;

    @ApiModelProperty("成功数")
    private Integer successCount;

    @ApiModelProperty("失败数")
    private Integer failCount;

    @ApiModelProperty("总数")
    private Integer total;

    @ApiModelProperty("失败文件")
    private String failFileName;

    @ApiModelProperty("规则名称, 0:失败 1:完成 2:运行中")
    private Integer status;

}
