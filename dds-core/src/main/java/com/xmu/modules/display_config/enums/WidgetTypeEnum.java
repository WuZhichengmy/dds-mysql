package com.xmu.modules.display_config.enums;

/**
 * @author summer
 */
public enum WidgetTypeEnum {

    /**
     * banner组件
     */
    BANNER(0, "banner组件"),

    /**
     * 最新资源
     */
    LATEST_DATA(1, "最新资源组件"),

    /**
     * 轮播图组件
     */
    CAROUSEL(2, "轮播图组件"),

    /**
     * 词云图
     */
    TAGS_CLOUD(3, "词云图组件"),

    /**
     * 资源统计
     */
    PANEL(4, "资源统计组件"),

    /**
     * logo组件
     */
    LOGO(5, "logo组件"),

    /**
     * 版权组件
     */
    COPYRIGHT(6, "版权组件"),

    /**
     * 排行榜
     */
    ARTICLE_LIST(7, "排行榜组件"),

    /**
     * 柱状图
     */
    CATEGORY(8, "柱状图组件");

    private Integer type;
    private String name;

    /**
     * 根据组件类型获取组件名称
     * @param type
     * @return
     */
    public static String getNameByType(Integer type) {
        WidgetTypeEnum[] values = WidgetTypeEnum.values();
        for (WidgetTypeEnum value : values) {
            if (value.getType().equals(type)) {
                return value.getName();
            }
        }
        return null;
    }

    WidgetTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
