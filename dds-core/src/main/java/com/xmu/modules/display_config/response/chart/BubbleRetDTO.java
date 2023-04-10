package com.xmu.modules.display_config.response.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BubbleRetDTO {
    private String title;
    //{name:'时间',data:['12a','1a','2a','3a']}
    @JsonProperty(value = "xAxis")
    private Map<String, Object> xAxis;
    //{name:'星期',data:['Saturday','Friday','Thursday','Wednesday']}
    @JsonProperty(value = "yAxis")
    private Map<String, Object> yAxis;
    //[[x,y,data]]-----[[0,0,5]]
    private List<List<Integer>> arr;
    private String contentDesc;
}
