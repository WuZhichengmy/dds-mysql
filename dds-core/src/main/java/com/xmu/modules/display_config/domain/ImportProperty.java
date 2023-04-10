package com.xmu.modules.display_config.domain;

import com.xmu.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xing
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportProperty extends BaseEntity {

    /**
     * 规则集id
     */
    private Long ruleSetId;

    /**
     * 表头
     */
    private String excelHeader;

    /**
     * 资源属性id
     */
    private Long resourcePropertyId;

}
