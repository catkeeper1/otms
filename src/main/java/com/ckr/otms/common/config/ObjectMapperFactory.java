package com.ckr.otms.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ObjectMapperFactory implements FactoryBean<ObjectMapper>, InitializingBean {
    private ObjectMapper mapper;

    @Override
    public ObjectMapper getObject() throws Exception {

        return mapper;
    }

    @Override
    public Class<ObjectMapper> getObjectType() {

        return ObjectMapper.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mapper = new ObjectMapper();

        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        //mapper.configure(DeserializationFeature, false);

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        mapper.setDateFormat(dateFormat);

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
