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
public class TableRetDTO {
    private String title;
    //[{title:'年份',key:'name'},{title:'2010',key:'2010'}]
    private List<Map<String,Object>> columns;
    //[{name:'发表数量',2010:1457.86}]
    private List<Map<String,Object>> data;

}
