package com.xmu.modules.display_config.response;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 检索结果
 * @author Xing
 */
@Data
public class SearchResultDTO {

    @ApiModelProperty("检索结果")
    private Map<String, Object> searchResults;

    @ApiModelProperty("精炼结果")
    private List<RefineResultDTO> refineResults;

    public void setSearchResults(List<Map<String, Object>> list, Long totalElements) {
        this.searchResults = Maps.newHashMapWithExpectedSize(2);
        searchResults.put("content", list);
        searchResults.put("totalElements", totalElements);
    }

    public List<Long> ids() {
        List<JSONObject> content = (List<JSONObject>) searchResults.get("content");
        return content.stream().map(jsonObject -> Long.valueOf((String) jsonObject.get("id"))).collect(Collectors.toList());
    }
}
