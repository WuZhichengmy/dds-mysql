package com.xmu.modules.display_config.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmu.modules.display_config.domain.SearchFieldsConfig;
import com.xmu.modules.display_config.mapper.SearchFieldsConfigMapper;
import com.xmu.modules.display_config.service.SearchFieldsConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Xing
 */
@Service
public class SearchFieldsConfigServiceImpl extends ServiceImpl<SearchFieldsConfigMapper, SearchFieldsConfig> implements SearchFieldsConfigService {

    private Logger logger = LoggerFactory.getLogger(SearchFieldsConfigServiceImpl.class);

}