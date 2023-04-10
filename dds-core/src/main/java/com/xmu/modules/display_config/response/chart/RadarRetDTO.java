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
public class RadarRetDTO {
    private String title;
    //[{name:'Sales'}]
    private List<Map<String,Object>> indicator;
    //[{value:[5000,7000,12000,11000,15000,14000],name:'Allocated Budget'}]
    private List<Map<String,Object>> data;
}
