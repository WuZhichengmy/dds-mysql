package com.xmu.modules.display_config.enums;

/**
 * 导入任务状态枚举类
 * @author Xing
 */
public enum ImportTaskStatusEnum {

    /**
     * 处理失败
     */
    IMPORT_FAIL(0),

    /**
     * 处理完成
     */
    IMPORT_FINISH(1),

    /**
     * 处理中
     */
    IMPORTING(2);


    private Integer value;

    ImportTaskStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
