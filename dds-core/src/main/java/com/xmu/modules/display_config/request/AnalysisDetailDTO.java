package com.xmu.modules.display_config.request;

import com.alibaba.fastjson.JSONObject;
import com.xmu.modules.display_config.domain.AnalysisDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AnalysisDetailDTO<T> {

    @NotNull
    @ApiModelProperty(value = "目录id", required = true)
    private Long menuId;

    @NotNull
    @ApiModelProperty(value = "目录id", required = true)
    private Integer type;

    @ApiModelProperty(value = "样式配置")
    private String css;

    @NotNull
    @ApiModelProperty(value = "检索类型", required = true)
    private Integer searchType;

    @ApiModelProperty(value = "分析说明文字")
    private String info;

    @ApiModelProperty(value = "分析sql")
    private String sql;

    @ApiModelProperty(value = "图表配置信息")
    private T config;

    @ApiModelProperty(value = "算法分析请求地址")
    private String algorithmUrl;

    public AnalysisDetail toAnalysisDetail() {
        return new AnalysisDetail()
                .setConfig(JSONObject.toJSONString(config))
                .setCss(css)
                .setInfo(info)
                .setExecuteSql(sql)
                .setSearchType(searchType)
                .setType(type)
                .setMenuId(menuId)
                .setAlgorithmUrl(algorithmUrl);
    }

    public AnalysisDetailDTO(AnalysisDetail analysisDetail) {
        BeanUtils.copyProperties(analysisDetail, this);
        this.sql = analysisDetail.getExecuteSql();
    }
}
