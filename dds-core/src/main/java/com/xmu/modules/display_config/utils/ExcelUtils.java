package com.xmu.modules.display_config.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.xmu.modules.display_config.request.ImportPropertyDTO;
import com.xmu.modules.display_config.response.ExcelDataDTO;

import java.util.List;

/**
 * @author Xing
 */
public class ExcelUtils {

    /**
     * 将表数据转化为excel数据
     * @param importPropertyList 导入属性
     * @param jsonList           表数据集合
     * @return
     */
    public static ExcelDataDTO handlerExcelData(List<ImportPropertyDTO> importPropertyList, List<JSONObject> jsonList) {
        // 表头
        List<List<String>> head = Lists.newArrayList();
        importPropertyList.forEach(importProperty -> {
            head.add(Lists.newArrayList(importProperty.getExcelHeader()));
        });
        // 表数据
        List<List<Object>> result = Lists.newArrayList();
        jsonList.forEach(jsonObject -> {
            List<Object> data = Lists.newArrayList();
            importPropertyList.forEach(displayProperty -> {
                String propertyData = jsonObject.getString(displayProperty.getPropertyName());
                if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(propertyData)) {
                    data.add(null);
                    return;
                }
                data.add(propertyData);
            });
            result.add(data);
        });
        return new ExcelDataDTO(head, result);
    }
}
