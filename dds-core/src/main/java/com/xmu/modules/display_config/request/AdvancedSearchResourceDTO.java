package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 高级检索
 * @author Xing
 */
@Data
public class AdvancedSearchResourceDTO {

    @ApiModelProperty(value = "检索资源id", required = true)
    @NotNull
    private Long resourceId;

    @ApiModelProperty(value = "检索组合条件")
    private List<SearchLogicDTO> searchLogic;

    @ApiModelProperty("精炼条件")
    private List<ESRefineDTO> refines;

    @ApiModelProperty(value = "页码", required = true)
    @NotNull
    private Integer pageNo;

    @ApiModelProperty(value = "每页条数", required = true)
    @NotNull
    private Integer pageSize;

    @ApiModelProperty("排序字段")
    private String orderColumn;

    @ApiModelProperty("排序方式: DESC-降序, ASC-升序")
    private String orderType;

}
