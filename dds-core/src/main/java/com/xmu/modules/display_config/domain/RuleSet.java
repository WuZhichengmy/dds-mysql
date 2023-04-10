package com.xmu.modules.display_config.domain;

import com.xmu.model.BaseEntity;
import lombok.Data;

/**
 * @author Xing
 */
@Data
public class RuleSet extends BaseEntity {

    private Long projectId;

    private Long resourceId;

    private String name;

    private String description;
}
