package com.seekey.handler;

import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.seekey.commons.Crypto;
import com.seekey.dao.CommonHelper;
import com.seekey.dao.Datasources;
import com.seekey.dao.MultipleDataSource;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RequestHandler {
	
	@Autowired
	CommonHelper commonHelper;
	@Value("${datav.expired}")
	int		expired;
	@Value("${datav.visitkey}")
	String	visitkey;	//32位
	@Value("${datav.secret}")
	String	secret;		//16位
	@Value("${datav.validatetime}")
	boolean validatetime;
	
	public String build(String sql, Map<String, String[]>map, 
			Enumeration<String> names) {
		String ret = sql;
		if (names != null) {
			do {
				String name = names.nextElement();
				if (name.startsWith("_datav")) continue;
				
				String[] data = map.get(name);
				name = ":" + name;
				String val = "";
				StringBuilder valb = new StringBuilder();
				
				if (data != null && data.length > 0) {
					for (int i=0; i<data.length; i++) {
						if (i > 0) valb.append(",");
						valb.append("'").append(data[i]).append("'");
					}
					ret = ret.replaceAll(name, val);
				}
			}while(names.hasMoreElements());
		}else {
			log.warn("No names!");
		}
		
		String regex = ":[0-9a-zA-Z]+";
		ret = ret.replaceAll(regex, "''");
		return ret;
	}
	
	public String[] datasources() {
		Datasources ds = new Datasources();
		return ds.getBeanNames();
	}
	
	public String query(String datasource, String sql) {
		MultipleDataSource.setDataSourceKey(datasource);
		List<LinkedHashMap<String,Object>> list = commonHelper.select(sql);
		if (list != null && !list.isEmpty()) {
			return JSON.toJSONString(list);
		} else {
			MultipleDataSource.setDataSourceKey(datasource);
			Map<String,Object> map = commonHelper.metadata(sql);
			return "[" + JSON.toJSONString(map, SerializerFeature.WriteMapNullValue) + "]";
		}
	}
	
	public JSONObject decrypt(String data, long time) {
		if (validatetime && 
				((new Date()).getTime() / 1000) > (time + expired)) 
			return null;
		String key = time + visitkey.substring(String.valueOf(time).length());
		data = data.replace(" ", "+");
		
		String ret = Crypto.decrypt(data, key, secret);
		return JSON.parseObject(ret);
	}
	
}
