package com.xmu.modules.display_config.enums;

/**
 * @author Xing
 */
public enum WidgetDataSourceTypeEnum {

    /**
     * 数据表
     */
    DATA_TABLE(0),

    /**
     * SQL语句
     */
    SQL(1),

    /**
     * 静态数据
     */
    STATIC_DATA(2),

    /**
     * 算法数据
     */
    ALGORITHM(3);

    private Integer value;

    /**
     * 根据枚举值获取枚举类
     * @param value
     * @return
     */
    public static WidgetDataSourceTypeEnum getByValue(Integer value) {
        for (WidgetDataSourceTypeEnum widgetDataSourceTypeEnum : values()) {
            if (value.equals(widgetDataSourceTypeEnum.getValue())) {
                return widgetDataSourceTypeEnum;
            }
        }
        return null;
    }

    WidgetDataSourceTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
