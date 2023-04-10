package com.xmu.modules.display_config.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmu.modules.display_config.domain.DetailFields;
import com.xmu.modules.display_config.mapper.DetailFieldsMapper;
import com.xmu.modules.display_config.service.DetailFieldsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Xing
 */
@Service
public class DetailFieldsServiceImpl extends ServiceImpl<DetailFieldsMapper, DetailFields> implements DetailFieldsService {

    private Logger logger = LoggerFactory.getLogger(DetailFieldsServiceImpl.class);

}