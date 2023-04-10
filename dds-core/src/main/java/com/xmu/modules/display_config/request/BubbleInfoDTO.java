package com.xmu.modules.display_config.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BubbleInfoDTO {
    private Long resourceId;
    private String x;
    private String y;
    private String data;
}
