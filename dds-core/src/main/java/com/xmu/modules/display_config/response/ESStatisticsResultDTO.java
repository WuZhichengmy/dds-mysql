package com.xmu.modules.display_config.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xing
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ESStatisticsResultDTO {

    private String key;

    private Long value;
}
