package com.xmu.modules.display_config.domain;

import com.xmu.model.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Xing
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AnalysisMenu extends IdEntity {

    private Long projectId;

    private Long resourceId;

    private String title;

    private Integer isParent;

    private Long parentId;
}
