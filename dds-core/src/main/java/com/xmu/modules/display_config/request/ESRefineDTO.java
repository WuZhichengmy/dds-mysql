package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 检索精炼条件
 * @author Xing
 */
@Data
public class ESRefineDTO {

    @NotBlank
    @ApiModelProperty("精炼字段")
    private String field;

    @NotEmpty
    @ApiModelProperty("字段值")
    private List<String> values;

}
