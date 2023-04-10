package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Xing
 */
@Data
public class ExportDataDTO {

    @NotNull
    @ApiModelProperty(value = "资源id", required = true)
    private Long resourceId;

    @ApiModelProperty(value = "导出id集合,为空导出全部")
    private List<Long> ids;
}
