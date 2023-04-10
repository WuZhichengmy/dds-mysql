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
public class SankeyRetDTO {
    private String title;
    //[{name:'a'}]
    private List<Map<String,Object>> data;
    //[{source:'a',target:'a1',value:5}]
    private List<Map<String,Object>> links;
}
