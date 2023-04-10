package com.xmu.modules.display_config.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xmu.model.BaseEntity;
import lombok.Data;

/**
 * @author Xing
 */
@Data
public class ResourceProperty extends BaseEntity {

    private Long projectId;

    private Long resourceId;

    private String name;

    /**
     * {@link com.xmu.modules.display_config.enums.ResourcePropertyTypeEnum}
     */
    private Integer type;

    @TableField("is_default")
    @JsonProperty("default")
    private Integer isDefault;

}
