package com.xmu.modules.display_config.request;

import com.xmu.modules.display_config.domain.SearchResultStatistics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StatisticsFieldsDTO {
    private String title;
    private String field;
    private Integer size;
    private Integer type;

    public StatisticsFieldsDTO(SearchResultStatistics statistics){
        this.field=statistics.getField();
        this.title=statistics.getTitle();
        this.size=statistics.getSize();
        this.type=statistics.getType();
    }
}
