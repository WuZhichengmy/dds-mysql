package com.xmu.modules.display_config.domain;

import com.xmu.model.BaseEntity;
import com.xmu.modules.display_config.request.SearchFieldsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Xing
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SearchFieldsConfig extends BaseEntity {

    private Long projectId;

    private Long resourceId;

    private String name;

    private String label;

    private Integer orderNumber;

    private Integer maxWords;

    public SearchFieldsConfig(SearchFieldsDTO searchFieldsDTO,Long projectId,Long resourceId){
        this.label=searchFieldsDTO.getLabel();
        this.name=searchFieldsDTO.getName();
        this.maxWords=searchFieldsDTO.getMaxWords();
        this.orderNumber=searchFieldsDTO.getOrderNumber();
        this.projectId=projectId;
        this.resourceId=resourceId;
    }

}
