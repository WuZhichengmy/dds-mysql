package com.xmu.modules.display_config.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RadarInfoDTO {
    private Long resourceId;
    private String column;//名称列
    private List<String> names;//查找的全部名称
    private List<String> data;//全部数据项
}
