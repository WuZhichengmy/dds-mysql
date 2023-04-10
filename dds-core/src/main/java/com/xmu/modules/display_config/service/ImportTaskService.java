package com.xmu.modules.display_config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmu.modules.display_config.domain.ImportTask;
import com.xmu.modules.display_config.request.ImportTaskDTO;
import com.xmu.modules.display_config.request.importDataDTO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Xing
 */
public interface ImportTaskService extends IService<ImportTask> {

    /**
     * 创建导入任务
     * @param importDataDTO
     * @return
     */
    Long saveImportTask(importDataDTO importDataDTO);

    /**
     * 异步导入数据
     * @param id
     */
    void importData(Long id);

    /**
     * 获取导入任务
     * @param resourceId
     * @return
     */
    ResponseEntity<List<ImportTaskDTO>> listImportTask(Long resourceId);

    /**
     * 下载文件
     * @param fileName
     * @param request
     * @param response
     */
    void downloadFile(String fileName, HttpServletRequest request, HttpServletResponse response);
}
