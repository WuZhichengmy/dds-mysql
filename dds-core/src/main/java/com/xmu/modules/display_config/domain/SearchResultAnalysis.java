package com.xmu.modules.display_config.domain;

import com.xmu.model.BaseEntity;
import lombok.Data;

/**
 * @author Xing
 */
@Data
public class SearchResultAnalysis extends BaseEntity {

    private Long projectId;

    private Long resourceId;

    private String title;

    private String fieldName;

    private Integer dataSourceType;

    private String dataSource;

}