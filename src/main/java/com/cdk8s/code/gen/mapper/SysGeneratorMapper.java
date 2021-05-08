package com.cdk8s.code.gen.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface SysGeneratorMapper {

	Map<String, String> selectMySQLVersion();

	List<Map<String, Object>> queryList(@Param("tableName") String tableName);

	Map<String, String> queryTable(String tableName);

	List<Map<String, Object>> queryColumns(String tableName);
}
