package com.xmu.modules.display_config.enums;

/**
 * @author Xing
 */
public enum SearchTypeEnum {

    /**
     * ElasticSearch全文检索
     */
    ES(0),

    /**
     * 算法智能检索
     */
    ALGORITHM(1);

    private Integer value;

    SearchTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
