package com.xmu.modules.display_config.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MultiPartConfig {
    private String xName;
    private String yName;
    private String dataDesc;
    private String isLine;
    private String stackGroup;
    private String componentName;

}
