package com.xmu.modules.display_config.enums;

/**
 * 可视化分析图标枚举类
 * @author summer
 */
public enum ChartTypeEnum {

    /**
     * 折线图
     */
    LINE_CHART(0),

    /**
     * 柱状图
     */
    HISTOGRAM(1),

    /**
     * 饼图
     */
    PIE_CHART(2),

    /**
     * 关系图
     */
    RELATION_CHART(3),

    /**
     * 气泡图
     */
    BUBBLE_CHART(4),

    /**
     * 桑基图
     */
    SANKEY_CHART(5),

    /**
     * 雷达图
     */
    RAIDER_CHART(6),

    /**
     * 表格
     */
    TABLE(7),

    /**
     * 词云图
     */
    TAGS_CLOUD(8),

    /*
     * EXTENSION ADDED Ver 1.1
     */

    /**
     * 多成分图
     */
    MULTI_PART(9);

    private Integer type;

    ChartTypeEnum(Integer type) {
        this.type = type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
