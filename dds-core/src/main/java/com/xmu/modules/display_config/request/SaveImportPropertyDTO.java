package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Xing
 */
@Data
public class SaveImportPropertyDTO {

    @ApiModelProperty(value = "规则集id", required = true)
    @NotNull
    private Long ruleSetId;

    @ApiModelProperty(value = "规则列表", required = true)
    @NotEmpty
    private List<SimpleImportPropertyDTO> simpleImportPropertyList;

}
