package com.xmu.modules.display_config.config;

import com.xmu.exception.BadRequestException;
import com.xmu.model.IdEntity;
import com.xmu.modules.display_config.domain.Test;
import com.xmu.modules.display_config.service.SearchService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xing
 */
/*
    InitializingBean 的用法：对Bean进行此接口的实现，可以实现对接口中的afterPropertiesSet复写以达到初始化Bean的目的。
 */
@Service
public class ServiceConfig implements InitializingBean {

    private static Map<String, SearchService<? extends IdEntity>> serviceMap = new HashMap<>();
    @Autowired
    private ApplicationContext applicationContext;
    public SearchService<IdEntity> getService(String key) {
        SearchService iService = serviceMap.get(key);
        if (null == iService) {
            throw new BadRequestException("检索异常,请检查配置");
        }
        return iService;
    }
    @Override
    public void afterPropertiesSet() {
        // K-泛型实体类名 V-实现类
        Map<String, SearchService> beanMap = applicationContext.getBeansOfType(SearchService.class);
        beanMap.values().forEach(iService ->
                serviceMap.put(iService.getEntityClass().getSimpleName(), iService)
        );
    }

}
