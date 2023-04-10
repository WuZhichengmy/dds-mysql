package com.xmu.modules.display_config.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmu.modules.display_config.domain.DisplayWidgets;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Xing
 */
@Mapper
public interface DisplayWidgetsMapper extends BaseMapper<DisplayWidgets> {

    /**
     * 根据表名获取组件数据
     * @param tableName
     * @return
     */
    Map<String, Object> selectDataByTableName(String tableName);

    /**
     * 根据SQL获取
     * @param Sql
     * @return
     */
    Map<String, Object> selectDataBySql(String Sql);

    /**
     * 根据sql获取多条数据
     * @param Sql
     * @return
     */
    List<Map<String, Object>> listDataBySql(String Sql);

    IPage<Map<String, Object>> selectByPage(Page<?> page, String sql);

    /**
     * 获取最新资源
     * @param tableName       表名
     * @param defaultProperty 默认属性
     * @param orderColumns    排序字段
     * @param limit           条数
     * @return
     */
    List<Map<String, Object>> listLatestData(@Param("tableName") String tableName, @Param("defaultProperty") String defaultProperty,
                                             @Param("orderColumns") List<String> orderColumns, @Param("limit") Integer limit);

    /**
     * 获取排行榜
     * @param tableName       表名
     * @param defaultProperty 默认属性
     * @param orderColumn     排序字段
     * @param orderType       排序方式
     * @param limit           条数
     * @return
     */
    List<Map<String, Object>> listLeaderBoardData(@Param("tableName") String tableName, @Param("defaultProperty") String defaultProperty,
                                                  @Param("orderColumn") String orderColumn, @Param("orderType") String orderType, @Param("limit") Integer limit);
}
