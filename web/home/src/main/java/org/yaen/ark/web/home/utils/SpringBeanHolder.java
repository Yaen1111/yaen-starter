package org.yaen.ark.web.home.utils;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * @author liyanlei
 *
 */
public class SpringBeanHolder implements ApplicationContextAware {

	    private static ApplicationContext applicationContext;

	    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	    	SpringBeanHolder.applicationContext = applicationContext;
	    }

	    public static ApplicationContext getApplicationContext() {
	        return applicationContext;
	    }

	    public static Object getBean(String beanId) throws BeansException {
	        return applicationContext.getBean(beanId);
	    }
}