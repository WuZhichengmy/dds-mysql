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
public class LineRetDTO {
    private String title="折线图";
    //{name:'年份',type:'category',data:[]}
    @JsonProperty(value = "xAxis")
    private Map<String, Object> xAxis;
    //{type:'value',name:'发表量'}
    @JsonProperty(value = "yAxis")
    private Map<String,Object> yAxis;
    //[{name:'美国',data:['237.08','248.73','261.71']}]
    private List<Map<String,Object>> series;
}
