package com.xmu.modules.display_config.domain;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xmu.model.BaseEntity;
import com.xmu.modules.display_config.response.AdvancedSearchFieldDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Xing
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SearchConfig extends BaseEntity {

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 资源id
     */
    private Long resourceId;

    /**
     * 检索类型(废弃)
     * {@link com.xmu.modules.display_config.enums.SearchTypeEnum}
     */
    private Integer type;

    /**
     * 检索字段
     */
    private String fields;

    /**
     * 扩展算法(废弃)
     */
    private String wordExtendAlgorithm;

    /**
     * 排序算法(废弃)
     */
    private String sortAlgorithm;

    public SearchConfig(List<AdvancedSearchFieldDTO> advancedSearchField, Long projectId, Long resourceId) {
        this.fields = JSONObject.toJSONString(advancedSearchField);
        this.projectId = projectId;
        this.resourceId = resourceId;
        this.type = 0;
    }

    public List<AdvancedSearchFieldDTO> toConfigDTO() {
        List<Map<String, String>> maps = JSONObject.parseObject(fields, new TypeReference<List<Map<String, String>>>() {
        });
        return maps.stream().map(map -> new AdvancedSearchFieldDTO().setLabel(map.get("label")).setName(map.get("name"))).collect(Collectors.toList());
    }
}
