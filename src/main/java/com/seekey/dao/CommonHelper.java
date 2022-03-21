package com.seekey.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommonHelper {
	
	@Autowired
	CommonMapper commonMapper;
	@Autowired
	SqlSessionFactory sqlSessionFactory;
	
	public List<LinkedHashMap<String,Object>> select(String sql){
		return commonMapper.select(sql);
	}
	
	/**
	 * MyBatis生成结果时，会把null的字段过滤掉，试过好多方法都不行，只好加上metadata取表头
	 * 如果各位有办法请提个issue，谢谢
	 * 
	 * @param sql
	 * @return
	 */
	public Map<String,Object> metadata(String sql){
		Map<String,Object> ret = new LinkedHashMap<>();
		SqlSession session = sqlSessionFactory.openSession();
		Connection con = session.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			for (int i=1; meta!=null && i<=meta.getColumnCount(); i++) {
				String key = meta.getColumnLabel(i);
				String type = meta.getColumnClassName(i);
				
				if ("java.math.BigDecimal".equals(type)) {
					BigDecimal d = new BigDecimal("0");
					ret.put(key, d);
				}else {
					String s = "";
					ret.put(key, s);
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			con.close();
			session.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
