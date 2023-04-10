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
public class WordCloudRetDTO {
    private String title;
    //[{name:'花鸟市场',value:1446},{name:'汽车',value:928}
    private List<Map<String,Object>> data;
}
