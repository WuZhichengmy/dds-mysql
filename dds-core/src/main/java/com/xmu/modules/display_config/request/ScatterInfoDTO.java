package com.xmu.modules.display_config.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ScatterInfoDTO {
    private Long resourceId;
    private String x;
    private String y;
    private String data;
    private String name;
    private String label;
}
