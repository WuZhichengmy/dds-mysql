package com.xmu.modules.display_config.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author summer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchInfo {
    @ApiModelProperty(value = "检索组合条件")
    private List<SearchLogicDTO> searchLogic;

    @ApiModelProperty("精炼条件")
    private List<ESRefineDTO> refines;
}
