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
public class GraphRetDTO {
    private String title;
    //[{name:'杨跃进(24)',value:24}]
    private List<Map<String,Object>> data;
    //{source:'杨跃进(24)',target:'徐波(11)',value:'10'}
    private List<Map<String,Object>> links;
}
