package com.xmu.modules.display_config.request;

import com.xmu.modules.display_config.response.AdvancedSearchFieldDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchConfigDTO {

    private Long resourceId;

    /**
     * 检索配置
     */
    private List<AdvancedSearchFieldDTO> search;

    /**
     * 检索结果页配置
     */
    private List<SearchFieldsDTO> fields;

    /**
     * 统计精炼配置
     */
    private List<StatisticsFieldsDTO> statistics;

    /**
     * 详情页配置
     */
    private List<DetailFieldsDTO> details;
}
