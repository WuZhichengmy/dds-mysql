package com.xmu.modules.display_config.response;

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
public class ChartInfo<T> {

    private T data;
    private Object config;
}
