package com.xmu.modules.display_config.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GraphConfig {
//    componentName: '这是图的名称',
    private String componentName;
//    graphLinkValue: '关系值名（缺省意为采用共现值）',
    private String graphLinkValue;
//    graphNodeValue: '主体值名（以;作为分隔符）',
    private String graphNodeValue;
//    isDirected: '是否是有向图',
    private String isDirected;
//    category: '分类依据',
    private String category;
//    nodeValueThreshold: '主体值阈值（显示的值下限）'
    private String nodeValueThreshold;

    private String linkValueThreshold;

}
