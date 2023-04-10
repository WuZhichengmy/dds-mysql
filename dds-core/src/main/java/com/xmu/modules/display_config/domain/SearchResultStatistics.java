package com.xmu.modules.display_config.domain;

import com.xmu.model.BaseEntity;
import com.xmu.modules.display_config.request.StatisticsFieldsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 精炼分析表
 * @author Xing
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultStatistics extends BaseEntity {

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 资源id
     */
    private Long resourceId;

    /**
     * 精炼标题
     */
    private String title;

    /**
     * 精练字段
     */
    private String field;

    /**
     * 精炼结果数
     */
    private Integer size;

    /**
     * 检索类型(废弃)
     * {@link com.xmu.modules.display_config.enums.SearchTypeEnum}
     */
    private Integer type;

    public SearchResultStatistics(StatisticsFieldsDTO statistic, Long projectId, Long resourceId) {
        this.title = statistic.getTitle();
        this.field = statistic.getField();
        this.size = statistic.getSize();
        this.type = statistic.getType();
        this.projectId = projectId;
        this.resourceId = resourceId;
    }
}
