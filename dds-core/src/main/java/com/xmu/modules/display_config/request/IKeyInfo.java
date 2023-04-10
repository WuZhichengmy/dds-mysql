package com.xmu.modules.display_config.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class IKeyInfo {
    //关键字
    private String keyword;
    //关键字对应的图片的URL
    private String imgUrl;
    //按照哪一列进行排序
    private String orderColumn;
    //ASC  or  DESC
    private String orderType;
    //is high light or not
    private Boolean isHighLighter;
    //检索字段
    private List<String> fields;
    //制定检索条件
    private List<String> fetchSources;
}
