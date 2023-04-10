package com.xmu.modules.display_config.domain;

import com.xmu.model.BaseEntity;
import com.xmu.modules.display_config.request.DetailFieldsDTO;
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
public class DetailFields extends BaseEntity {

    private Long projectId;

    private Long resourceId;

    private String name;

    private String label;

    private Integer orderNumber;

    public DetailFields(DetailFieldsDTO detailField,Long projectId,Long resourceId){
        this.name=detailField.getName();
        this.label=detailField.getLabel();
        this.orderNumber=detailField.getOrderNumber();
        this.projectId=projectId;
        this.resourceId=resourceId;
    }

}
