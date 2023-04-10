package com.xmu.modules.display_config.enums;

/**
 * 检索表达式枚举
 * @author Xing
 */
public enum LogicEnum {

    /**
     * and
     */
    AND("AND"),

    /**
     * or
     */
    OR("OR"),

    /**
     * not
     */
    NOT("NOT");

    private String value;

    LogicEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
