package com.xmu.modules.display_config.domain;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xmu.model.IdEntity;
import com.xmu.modules.display_config.request.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author Xing
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AnalysisDetail extends IdEntity {

    private Long menuId;

    private Integer type;

    private String css;

    private String config;

    private Integer searchType;

    private String info;

    private String executeSql;

    private String algorithmUrl;

    public AnalysisDetailDTO<?> toDTO() {
        return new AnalysisDetailDTO<>(this).setConfig(config());
    }

    public Object config() {
        TypeReference<?> ref;
        switch (type) {
            case 0:
            case 1:
                ref = new TypeReference<Map<String, LineConfig>>() {};
                break;
            case 2:
                ref = new TypeReference<Map<String, PieConfig>>() {};
                break;
            case 4:
                ref = new TypeReference<Map<String, BubbleConfig>>() {};
                break;
            case 6:
                ref = new TypeReference<Map<String, RadarConfig>>() {};
                break;
            case 7:
                ref = new TypeReference<Map<String, TableConfig>>() {};
                break;
            case 5:
            case 8:
            case 3:
            default:
                ref = new TypeReference<Object>() {};
                break;
        }
        return JSONObject.parseObject(config, ref);
    }
}
