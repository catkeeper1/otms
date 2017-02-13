package com.ckr.otms.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * This is a util class for i18n handling.
 */
@Component
@Lazy(false)
public class MessageResourceHolder {


    private static AbstractMessageSource messageSource;

    /**
     * A set method that is used to inject message source.
     * @param messageSource a message source that include all properties files that include all message templates.
     */
    @Autowired
    @Qualifier("messageSource")
    public static void setMessageSource(AbstractMessageSource messageSource) {
        MessageResourceHolder.messageSource = messageSource;
    }

    /**
     * Get message base on locale in HTTP request object.
     * @param code The message code that can be used to retrieve message from properties file.
     * @param args The parameter values that will be used to replace the parameter in message template.
     * @param request The HTTP request the include locale info
     * @return a message that is generated with the template from properties file.
     */
    public static String getMessage(String code, Object[] args, HttpServletRequest request) {

        return messageSource.getMessage(code,
                args,
                RequestContextUtils.getLocale(request));
    }

    /**
     * This is the same as {@link #getMessage(String, Object[], HttpServletRequest)} except that the parameter
     * args is always be null.
     * @see MessageResourceHolder#getMessage(String, Object[], HttpServletRequest)
     */
    public static String getMessage(String code, HttpServletRequest request) {

        return messageSource.getMessage(code,
                null,
                RequestContextUtils.getLocale(request));
    }

}
