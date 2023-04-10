package com.xmu.modules.display_config.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmu.modules.display_config.domain.SearchResultAnalysis;
import com.xmu.modules.display_config.mapper.SearchResultAnalysisMapper;
import com.xmu.modules.display_config.service.SearchResultAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Xing
 */
@Service
public class SearchResultAnalysisServiceImpl extends ServiceImpl<SearchResultAnalysisMapper, SearchResultAnalysis> implements SearchResultAnalysisService {

    private Logger logger = LoggerFactory.getLogger(SearchResultAnalysisServiceImpl.class);

}