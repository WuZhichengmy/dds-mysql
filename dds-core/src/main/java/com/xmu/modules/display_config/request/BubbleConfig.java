package com.xmu.modules.display_config.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BubbleConfig {
    private String content;
    private String componentName;
    @JsonProperty("xAxis")
    private String xName;
    @JsonProperty("yAxis")
    private String yName;
}
