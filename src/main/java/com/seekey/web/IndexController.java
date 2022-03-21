package com.seekey.web;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.seekey.handler.RequestHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 本来应该封装一下报错信息的，写着写着就没心机写了，等下一版本吧
 * 
 * @author Seekey
 * @version 1.0
 */
@RestController
@Slf4j
public class IndexController {
	
	@Autowired  
	HttpServletRequest request;
	@Autowired
	HttpServletResponse response;
	@Autowired
	RequestHandler	requestHandler;
	
	@RequestMapping(value="/index")
	public String index(Model model) {
		log.debug("Index");
		return "index";
	}
	
	@RequestMapping(value="/status", produces={"application/json;charset=UTF-8"})
	public String status(Model model) {
		return "hi there.";
	}
	
	@RequestMapping(value="/version", produces={"application/json;charset=UTF-8"})
	public String version(Model model) {
		return "0.2.0";
	}
	
	@RequestMapping(value="/database", produces={"application/json;charset=UTF-8"})
	public String database(Model model) {
		JSONObject data = getParam();
		String datasource = "";
		if (data != null) {
			datasource = data.getString("db");
			Map<String, String[]> map = request.getParameterMap();
			Enumeration<String> names = request.getParameterNames();
			String sql = data.getString("sql");
			sql = requestHandler.build(sql, map, names);
			if (!StringUtils.isEmpty(sql)) {
				return requestHandler.query(datasource, sql);
			}else {
				return "{\"isError\": true, \"message\": \"SQL出错："+ sql +"\"}";
			}
		}
		return "{\"isError\": true, \"message\": \"数据库不存在："+ datasource +"\"}";
	}
	
	@RequestMapping(value="/get/databases", produces={"application/json;charset=UTF-8"})
	public String list(Model model) {
		JSONObject data = getParam();
		if (data != null && "getDatabases".equals(data.getString("action"))) {
			JSONObject ret = new JSONObject();
			ret.put("data", requestHandler.datasources());
			return ret.toJSONString();
		}else{
			return "{\"isError\": true, \"message\":\"配置错误\"}";
		}
	}
	
	@RequestMapping(value="/test/connection", produces={"application/json;charset=UTF-8"})
	public String connection(Model model) {
		JSONObject data = getParam();
		if (data != null && "connectTest".equals(data.getString("action"))) {
			JSONObject ret = new JSONObject();
			ret.put("database", "Mysql");
			ret.put("connect", "连接成功");
			return ret.toJSONString();
		}else{
			return "{\"isError\": true, \"message\":\"配置错误\"}";
		}
	}
	
	@RequestMapping(value="/message", produces={"application/json;charset=UTF-8"})
	public String message(Model model) {
		return "{\"isError\": true, \"message\":\"证书登录不成功\"}";
	}
	
	private JSONObject getParam() {
		String key = request.getParameter("_datav_id");
		String st = request.getParameter("_datav_time");
		
		if (StringUtils.isEmpty(st)) st = "0";
		long time = Long.parseLong(st); 
		return requestHandler.decrypt(key, time);
	}
}
