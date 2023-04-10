package com.xmu.modules.display_config.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 统计结果类
 * @author Xing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDTO {

    /**
     * K-字段值 V-值数量
     */
    private Map<String, Long> map;

    /**
     * 总数量
     */
    private Integer total;
}
