package com.xmu.modules.display_config.enums;

/**
 * 排序枚举类
 * @author Xing
 */
public enum OrderEnum {

    /**
     * ASC
     */
    ASC("ASC"),

    /**
     * DESC
     */
    DESC("DESC");

    private String order;

    OrderEnum(String order) {
        this.order = order;
    }
}
