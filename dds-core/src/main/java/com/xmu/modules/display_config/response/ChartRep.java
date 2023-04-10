package com.xmu.modules.display_config.response;

import com.alibaba.druid.sql.visitor.functions.Char;
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
public class ChartRep {

    private int code;
    private String errMsg;
    private ChartInfo<?> data;
}
