package com.xmu.modules.display_config.response.chart;

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
public class BarRetDTO {
    private String title;
    //{name:'年份',type:'category',data:[]}
    private Map<String, Object> xAxis;
    //{type:'value',name:'发表量'}
    private Map<String,Object> yAxis;
    //[{name:'美国',data:['237.08','248.73','261.71']}]
    private List<Map<String,Object>> series;
}
