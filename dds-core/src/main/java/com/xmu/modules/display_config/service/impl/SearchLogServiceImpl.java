package com.xmu.modules.display_config.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmu.modules.display_config.domain.SearchLog;
import com.xmu.modules.display_config.mapper.SearchLogMapper;
import com.xmu.modules.display_config.service.SearchLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Xing
 */
@Service
public class SearchLogServiceImpl extends ServiceImpl<SearchLogMapper, SearchLog> implements SearchLogService {

    private Logger logger = LoggerFactory.getLogger(SearchLogServiceImpl.class);

}