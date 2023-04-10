package com.xmu.modules.display_config.domain;

import com.xmu.model.BaseEntity;
import lombok.Data;

/**
 * @author Xing
 */
@Data
public class ImportTask extends BaseEntity {

    /**
     * 资源id
     */
    private Long resourceId;

    /**
     * 规则集id
     */
    private Long ruleSetId;

    /**
     * 文件名称
     */
    private String sourceFileName;

    /**
     * 执行时长
     */
    private Long executionTime;

    /**
     * 成功数
     */
    private Integer successCount;

    /**
     * 失败数
     */
    private Integer failCount;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 失败文件
     */
    private String failFileName;

    /**
     * {@link com.xmu.modules.display_config.enums.ImportTaskStatusEnum}
     */
    private Integer status;
}
