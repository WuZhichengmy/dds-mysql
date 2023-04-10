package com.xmu.modules.display_config.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Xing
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelDataDTO {

    /**
     * 表头
     */
    private List<List<String>> head;

    /**
     * 表数据
     */
    private List<List<Object>> data;

}
