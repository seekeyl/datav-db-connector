package com.seekey.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 权限管理可以在这个位置增加
 * 
 * @author Seekey
 * @version 1.0
 */
@Configuration
public class WebGlobalConfig extends WebMvcConfigurationSupport{

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		//添加映射路径
        registry.addMapping("/**")
                //是否发送Cookie
                .allowCredentials(true)
                //设置放行哪些原始域
                .allowedOriginPatterns("*")
                //放行哪些请求方式
                .allowedMethods("*") //放行全部
                //放行哪些原始请求头部信息
                .allowedHeaders("*")
                //暴露哪些原始请求头部信息
                .exposedHeaders("*");
	}

	
}
