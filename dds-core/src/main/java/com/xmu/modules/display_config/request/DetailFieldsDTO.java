package com.xmu.modules.display_config.request;

import com.xmu.modules.display_config.domain.DetailFields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DetailFieldsDTO {
    private String name;
    private String label;
    private Integer orderNumber;

    public DetailFieldsDTO(DetailFields detail){
        this.label=detail.getLabel();
        this.name=detail.getName();
        this.orderNumber=detail.getOrderNumber();
    }
}
