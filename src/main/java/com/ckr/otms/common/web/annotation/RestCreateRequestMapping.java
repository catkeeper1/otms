package com.ckr.otms.common.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ResponseBody
@RequestMapping(method = {RequestMethod.PUT, RequestMethod.POST}, headers = "If-None-Match=*" )
public @interface RestCreateRequestMapping {
	@AliasFor(annotation = RequestMapping.class, attribute = "value")
	String[] value() default {};
}
