package com.xmu.modules.display_config.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xmu.modules.display_config.domain.DisplayWidgets;
import com.xmu.modules.display_config.request.BasicConfig;
import com.xmu.modules.display_config.request.CategoryDTO;
import com.xmu.modules.display_config.request.WidgetConfig;

import java.util.List;

/**
 * @author Xing
 */
public interface DisplayWidgetsService extends IService<DisplayWidgets> {

    Object getWidgets(Long projectId, Long id);

    Object basicConfig(Long projectId, BasicConfig basicConfig);

    Object widgetConfig(Long projectId, WidgetConfig<?> widgetConfig);

    Object getColumns(Long resourceId, boolean useId);

    Object previewWidgetsO(WidgetConfig<List<CategoryDTO>> widgetConfig);

    Object getWidgetsConfig(List<Long> ids);

    Object selectByPage(Page<?> page, String sql);
}
