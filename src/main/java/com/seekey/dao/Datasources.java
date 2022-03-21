package com.seekey.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;

@Component
public class Datasources implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Datasources.applicationContext = applicationContext;
	}

	public Object getBean(String beanName){
		return Datasources.applicationContext.getBean(beanName);
	}
	
	public String[] getBeanNames() {
		String[] beans = Datasources.applicationContext.getBeanDefinitionNames();
		List<String> ret = new ArrayList<>();
		if (beans != null) {
			for (String beanId : beans) {
				Object obj = getBean(beanId);
				if (obj instanceof DruidDataSource) {
					ret.add(beanId);
				}
			}
		}
		return ret.toArray(new String[ret.size()]);
	}
}
