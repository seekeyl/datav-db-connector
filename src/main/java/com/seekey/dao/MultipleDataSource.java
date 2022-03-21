package com.seekey.dao;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 多数据源配置
 * 
 * @author Seekey
 * @version 1.0
 */
public class MultipleDataSource extends AbstractRoutingDataSource {

	private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<>();
	
	public static void setDataSourceKey(String dataSource) {
		dataSourceKey.set(dataSource);
	}
	
	@Override
	protected Object determineCurrentLookupKey() {
		return dataSourceKey.get();
	}
	
	public void unload() {
		dataSourceKey.remove(); // Compliant
	}
}
