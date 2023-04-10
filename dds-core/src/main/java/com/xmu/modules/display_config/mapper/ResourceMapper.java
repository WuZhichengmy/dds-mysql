package com.xmu.modules.display_config.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmu.modules.display_config.domain.Resource;
import com.xmu.modules.display_config.response.ESStatisticsResultDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Xing
 */
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 统计属性数据数量
     * @param tableName
     * @param enumerationKey
     * @param enumerationValue
     * @return
     */
    Integer countResourceData(@Param("tableName") String tableName, @Param("enumerationKey") String enumerationKey,
                              @Param("enumerationValue") String enumerationValue);

    /**
     * 创建新属性
     * @param tableName
     * @param property
     * @param type
     */
    void createResourceProperty(@Param("tableName") String tableName, @Param("property") String property,
                                @Param("type") Integer type);

    /**
     * 更新属性
     * @param tableName
     * @param oldProperty
     * @param newProperty
     * @param type
     */
    void updateResourceProperty(@Param("tableName") String tableName, @Param("oldProperty") String oldProperty,
                                @Param("newProperty") String newProperty, @Param("type") Integer type);

    /**
     * 删除属性
     * @param tableName
     * @param property
     */
    void deleteResourceProperty(@Param("tableName") String tableName, @Param("property") String property);

    /**
     * 保存导入数据
     * @param tableName
     * @param importData
     * @return
     */
    Integer saveImportData(@Param("tableName") String tableName, @Param("importData") Map<String, Object> importData);


    void creatTable(String tableName);

    void dropTable(String tableName);

    /**
     * 获取精炼结果
     * @param tableName
     * @param field
     * @param wrapper
     * @return
     */
    List<ESStatisticsResultDTO> getEsStatisticsResults(@Param("tableName") String tableName, @Param("field") String field, @Param("ew") Wrapper wrapper);

    /**
     * 获取统计字段值
     * @param tableName
     * @param field
     * @param wrapper
     * @return
     */
    List<String> getFieldStatistic(@Param("tableName") String tableName, @Param("field") String field, @Param("ew") Wrapper wrapper);

    /**
     * 根据id检索目标资源
     * id为空检索全部
     * @param tableName
     * @param ids
     * @return
     */
    List<JSONObject> listTargetResourceByIds(@Param("tableName") String tableName, @Param("ids") List<Long> ids);

    /**
     * 根据sql获取多条数据
     * @param sql
     * @return
     */
    List<Map<String, Object>> listDataBySql(@Param("sql") String sql);

    /**
     * 修改表名
     * @param oldName
     * @param newName
     */
    void updateTableName(@Param("oldName") String oldName, @Param("newName") String newName);
}
