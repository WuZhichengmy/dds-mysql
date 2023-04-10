package com.xmu.modules.display_config.enums;

/**
 * 资源属性字段类型枚举类
 * @author Xing
 */
public enum ResourcePropertyTypeEnum {

    /**
     * 字符串
     */
    VARCHAR(0),

    /**
     * 整数
     */
    INT(1),

    /**
     * 长整数
     */
    BIGINT(2),

    /**
     * 小数
     */
    DECIMAL(3),

    /**
     * 日期
     */
    DATETIME(4),

    /**
     * 文本
     */
    TEXT(5),

    /**
     * 大文本
     */
    LONGTEXT(6);

    ResourcePropertyTypeEnum(Integer type) {
        this.type = type;
    }

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
