package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Xing
 */
@Data
@Accessors(chain = true)
public class BaseSearchResourceDTO {

    @ApiModelProperty(value = "检索资源id", required = true)
    @NotNull
    private Long resourceId;

    @ApiModelProperty("检索关键字")
    private String keyword;

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
