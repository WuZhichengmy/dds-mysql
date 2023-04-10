package com.xmu.modules.display_config.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ConfigDTO {
    private String compName;
    private int type;
    private int dataSourceType;
    private String config;
    private String algorithmParam;
    private List<DataSourceDTO> dataSourceDTOs;
}
