package com.xmu.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author Xing
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public Validator getValidator() {
        return validator();
    }
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jackson2HttpMessageConverter());
        converters.add(stringHttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter =
                new MappingJackson2HttpMessageConverter();
        //定义对象映射器
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //定义对象模型
        SimpleModule simpleModule = new SimpleModule();
        //添加对长整型的转换关系
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        //将对象模型添加至对象映射器
        objectMapper.registerModule(simpleModule);
        //将对象映射器添加至Json转换器
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        return jackson2HttpMessageConverter;
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter(){
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        stringHttpMessageConverter.setWriteAcceptCharset(false);
        return stringHttpMessageConverter;
    }
}
