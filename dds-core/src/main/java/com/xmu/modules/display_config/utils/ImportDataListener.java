package com.xmu.modules.display_config.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xmu.modules.display_config.constant.Constants;
import com.xmu.modules.display_config.domain.ImportTask;
import com.xmu.modules.display_config.enums.ResourcePropertyTypeEnum;
import com.xmu.modules.display_config.service.ResourceService;
import com.xmu.utils.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangchen on 2020-04-21.
 */
@Slf4j
public class ImportDataListener extends AnalysisEventListener<Map<Integer, String>> {

    private SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT);
    private static final int BATCH_COUNT = 1000;
    /**
     * 解析excel的行数据对象
     */
    private List<Map<Integer, String>> list = Lists.newArrayList();
    /**
     * 属性名与excel表头映射
     */
    private Map<String, String> importPropertyExcelMap = Maps.newHashMap();
    /**
     * excel表头序列与属性名映射
     */
    private Map<Integer, String> importPropertyMap = Maps.newHashMap();
    /**
     * 导入属性与属性类型映射
     */
    private Map<String, Integer> importPropertyTypeMap = Maps.newHashMap();

    /**
     * 模型名称
     */
    private String tableName;

    /**
     * 模型数据service
     */
    private ResourceService resourceService;
    /**
     * 导入历史对象
     */
    private ImportTask importTask;
    /**
     * /Excel导入失败数据集合
     */
    private List<JSONObject> failImportDataList;

    public ImportDataListener(ImportTask importTask, String tableName, Map<String, String> importPropertyExcelMap,
                              Map<String, Integer> importPropertyTypeMap, List<JSONObject> failImportDataList, ResourceService resourceService) {

        this.importPropertyExcelMap = importPropertyExcelMap;
        this.importPropertyTypeMap = importPropertyTypeMap;
        this.tableName = tableName;
        this.resourceService = resourceService;
        this.importTask = importTask;
        this.failImportDataList = failImportDataList;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        //获取导入excel的表头List<Integer>
        headMap.forEach((headIndex, headValue) -> {
            importPropertyExcelMap.forEach((importPropertyName, importHeadValue) -> {
                if (importHeadValue.equals(headValue)) {
                    importPropertyMap.put(headIndex, importPropertyName);
                }
            });
        });
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            importTask.setTotal(importTask.getTotal() + list.size());
            saveData();
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        importTask.setTotal(importTask.getTotal() + list.size());
        saveData();
    }

    private void saveData() {
        list.forEach(data -> {
            Map<String, Object> importData = Maps.newHashMap();
            data.forEach((k, v) -> {
                if (importPropertyMap.containsKey(k)) {
                    String propertyName = importPropertyMap.get(k);
                    if (StringUtils.isBlank(v)) {
                        importData.put(propertyName, v);
                        return;
                    }
                    importData.put(propertyName, this.convert(importPropertyTypeMap.get(propertyName), v));
                }
            });
            // 过滤空数据
            if (MapUtils.isEmpty(importData)) {
                return;
            }
            //id,create_user,create_time,modify_user,modify_time要自行设值
            importData.put(Constants.PRIMARY_KEY, SnowFlakeUtil.getFlowIdInstance().nextId());

            try {
                int result = resourceService.saveImportData(tableName, importData);
                if (result >= 1) {
                    importTask.setSuccessCount(importTask.getSuccessCount() + result);
                } else {
                    importTask.setFailCount(importTask.getFailCount() + 1);
                }
            } catch (Exception exception) {
                log.error("saveData deal failed : {}", JSONObject.toJSONString(importData));
                //捕获插入的异常
                //导入错误数据+1
                importTask.setFailCount(importTask.getFailCount() + 1);
                JSONObject failData = new JSONObject();
                failData.putAll(importData);
                failImportDataList.add(failData);
            }
        });
    }

    /**
     * 转换数据
     * @param type 数据类型
     * @param data 导入的数据
     * @return
     */
    private Object convert(Integer type, String data) {
        Object value = null;
        // INTEGER
        if (ResourcePropertyTypeEnum.INT.getType().equals(type)) {
            try {
                value = Integer.valueOf(data);
            } catch (Exception e) {
                log.error("import data convert failed by INTEGER cause : {}", e.getMessage());
            }
            return value;
        }
        // BIGINT
        if (ResourcePropertyTypeEnum.BIGINT.getType().equals(type)) {
            try {
                value = Long.valueOf(data);
            } catch (Exception e) {
                log.error("import data convert failed by BIGINT cause : {}", e.getMessage());
            }
            return value;
        }
        // DECIMAL
        if (ResourcePropertyTypeEnum.DECIMAL.getType().equals(type)) {
            try {
                value = Double.valueOf(data);
            } catch (Exception e) {
                log.error("import data convert failed by DECIMAL cause : {}", e.getMessage());
            }
            return value;
        }
        // DATETIME
        if (ResourcePropertyTypeEnum.DATETIME.getType().equals(type)) {
            //DATETIME:规定格式yyyy-MM-dd HH:mm:ss
            try {
                value = sdf.parse(data);
            } catch (ParseException e) {
                log.error("import data convert failed by DATETIME cause : {}", e.getMessage());
            }
            return value;
        }
        return data;
    }
}

