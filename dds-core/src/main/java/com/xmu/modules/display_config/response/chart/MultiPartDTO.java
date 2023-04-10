package com.xmu.modules.display_config.response.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MultiPartDTO {
    private String title="多成分图";
    //  dataset: {
    //    dimensions: ['product', '2015', '2016', '2017'],
    //    source: [
    //      { product: 'Matcha Latte', 2015: 43.3, 2016: 85.8, 2017: 93.7 },
    //      { product: 'Milk Tea', 2015: 83.1, 2016: 73.4, 2017: 55.1 },
    //      { product: 'Cheese Cocoa', 2015: 86.4, 2016: 65.2, 2017: 82.5 },
    //      { product: 'Walnut Brownie', 2015: 72.4, 2016: 53.9, 2017: 39.1 }
    //    ]
    //  }
    @JsonProperty(value = "dataset")
    private Map<String, Object> dataset;
    //{type:'value',name:'发表量'}
    private List<Map<String,Object>> series;
}
