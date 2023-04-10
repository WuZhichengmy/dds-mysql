package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 通用图表配置类
 * @author summer
 * @date 2021/4/3 21:04
 */
@Data
public class ChartConfigInfoDTO {

    /**
     * 图表类型,可以改为枚举
     */
    private String type;

    /**
     * 按照哪个字段进行配置
     */
    private String field;

    @ApiModelProperty(value = "检索资源id", required = true)
    @NotNull
    private Long resourceId;

    @ApiModelProperty("检索关键字")
    private String keyword;

    @ApiModelProperty("精炼条件")
    private List<ESRefineDTO> refines;

    @ApiModelProperty("排序字段")
    private String orderColumn;

    @ApiModelProperty("排序方式: DESC-降序, ASC-升序")
    private String orderType;

}
