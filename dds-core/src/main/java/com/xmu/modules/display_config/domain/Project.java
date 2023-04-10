package com.xmu.modules.display_config.domain;

import com.xmu.model.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Xing
 */
@Data
@Accessors(chain = true)
public class Project extends BaseEntity {

    private String name;

    private String code;

    private String description;

    private Integer source;

}
