package com.xmu.modules.display_config.domain;


import com.xmu.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Xing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DisplayWidgets extends BaseEntity {

    private Long projectId;

    private String name;

    private Integer type;

    private Integer dataSourceType;

    private String config;

    private String dataSource;

    private String algorithmParam;

}
