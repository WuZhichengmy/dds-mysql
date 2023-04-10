package com.xmu.modules.display_config.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmu.modules.display_config.domain.DetailLog;
import com.xmu.modules.display_config.mapper.DetailLogMapper;
import com.xmu.modules.display_config.service.DetailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Xing
 */
@Service
public class DetailLogServiceImpl extends ServiceImpl<DetailLogMapper, DetailLog> implements DetailLogService {

    private Logger logger = LoggerFactory.getLogger(DetailLogServiceImpl.class);

}