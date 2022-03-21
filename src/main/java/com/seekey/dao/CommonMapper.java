package com.seekey.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommonMapper {
	
	@Select("${sql}")
	public List<LinkedHashMap<String, Object>> select(@Param("sql") String sql);
}
