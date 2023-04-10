package com.xmu.modules.display_config.domain;

import com.xmu.model.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Xing
 */
@Data
@Accessors(chain = true)
public class Resource extends BaseEntity {

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 表名
     */
    private String target;

    /**
     * 实体名
     */
    private String entity;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 是否默认资源
     */
    private Integer isDefault;

    /**
     * 图标
     */
    private String icon;
}
