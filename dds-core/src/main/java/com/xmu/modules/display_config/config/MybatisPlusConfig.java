package com.xmu.modules.display_config.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Xing
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件，自动识别数据库类型
     * 多租户，请参考官网【插件扩展】
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.SQLITE));
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        // 用了分页插件必须设置 MybatisConfiguration#useDeprecatedExecutor = false
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new DateMetaObjectHandler();
    }

}
