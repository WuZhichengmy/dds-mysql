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
public class ChartInfoDTO {
    @JsonProperty(value = "xAxis")
    private String xAxis;
    @JsonProperty(value = "yAxis")
    private String yAxis;
    private String title;
    private Long resourceId;
}
