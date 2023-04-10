package com.xmu.modules.display_config.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmu.modules.display_config.domain.AnalysisMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author Xing
 */
@Mapper
public interface AnalysisMenuMapper extends BaseMapper<AnalysisMenu> {

    Map<String, Object> selectDataByTableName(String tableName);

    Map<String, Object> selectDataBySql(String Sql);

    List<Map<String, Object>> listDataBySql(String Sql);

    IPage<Map<String,Object>> selectByPage(Page<?> page, String sql);
}
