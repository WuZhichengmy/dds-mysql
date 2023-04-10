package com.xmu.modules.display_config.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmu.modules.display_config.domain.SearchResultStatistics;
import com.xmu.modules.display_config.mapper.SearchResultStatisticsMapper;
import com.xmu.modules.display_config.service.SearchResultStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Xing
 */
@Service
public class SearchResultStatisticsServiceImpl extends ServiceImpl<SearchResultStatisticsMapper, SearchResultStatistics> implements SearchResultStatisticsService {

    private Logger logger = LoggerFactory.getLogger(SearchResultStatisticsServiceImpl.class);

}