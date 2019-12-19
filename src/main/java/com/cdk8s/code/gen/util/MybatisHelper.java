package com.cdk8s.code.gen.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import java.io.IOException;
import java.io.Reader;


/**
 * 使用 Mybatis 核心顺序：*
 * SqlSessionFactoryBuilder：读取配置信 息创建SqlSessionFactory，建造者模式， 方法级别生命周期；
 * SqlSessionFactory：创建Sqlsession，工 厂单例模式，存在于程序的整个生命周 期；
 * SqlSession：代表一次数据库连接，可 以直接发送SQL执行，也可以通过调用 Mapper访问数据库；线程不安全，要保 证线程独享（方法级）；
 * 业务 Mapper 实例：由一个Java接口和XML文 件组成，包含了要执行的SQL语句和结果 集映射规则。方法级别生命周期；
 */
public class MybatisHelper {
	private static SqlSessionFactory sqlSessionFactory;

	static {
		try {
			//通过 SqlSessionFactoryBuilder 创建 SqlSessionFactory，原理是采用了建造者模式
			Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			reader.close();
			//创建数据库
			SqlSession sqlSession = null;
			try {
				sqlSession = sqlSessionFactory.openSession();
				// sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, true); // 批量操作模式，使用该方式需要手动 sqlSession.commit(); 提交事务。
				//创建一个MapperHelper
				MapperHelper mapperHelper = new MapperHelper();
				//特殊配置
				Config config = new Config();
				// 设置UUID生成策略
				// 配置UUID生成策略需要使用OGNL表达式
				// 默认值32位长度:@java.util.UUID@randomUUID().toString().replace("-", "")
				//config.setUUID("");
				// 主键自增回写方法,默认值MYSQL,详细说明请看文档
				//config.setIDENTITY("HSQLDB");
				config.setIDENTITY("MYSQL");
				// 支持方法上的注解
				// 3.3.1版本增加
				config.setEnableMethodAnnotation(true);
				config.setNotEmpty(true);
				//校验Example中的类型是否一致
				config.setCheckExampleEntityClass(true);
				//启用简单类型
				config.setUseSimpleType(true);
				config.setOrder("AFTER");
				//自动关键字 - mysql
				//config.setWrapKeyword("`{0}`");
				//设置配置
				mapperHelper.setConfig(config);
				// 注册通用Mapper接口 - 可以自动注册继承的接口
				mapperHelper.registerMapper(Mapper.class);
				//mapperHelper.registerMapper(HsqldbMapper.class);
				mapperHelper.registerMapper(MySqlMapper.class);
				// mapperHelper.registerMapper(SqlServerMapper.class);
				mapperHelper.registerMapper(IdsMapper.class);
				//配置完成后，执行下面的操作
				mapperHelper.processConfiguration(sqlSession.getConfiguration());
				//OK - mapperHelper的任务已经完成，可以不管了

			} finally {
				if (sqlSession != null) {
					sqlSession.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取Session
	 *
	 * @return
	 */
	public static SqlSession getSqlSession() {
		return sqlSessionFactory.openSession();
	}
}
