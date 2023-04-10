package com.xmu.modules.display_config.request;

import com.xmu.modules.display_config.domain.SearchFieldsConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchFieldsDTO {
    private String name;
    private String label;
    private Integer orderNumber;
    private Integer maxWords;

    public SearchFieldsDTO(SearchFieldsConfig field){
        this.name=field.getName();
        this.label=field.getLabel();
        this.orderNumber=field.getOrderNumber();
        this.maxWords=field.getMaxWords();
    }
}
