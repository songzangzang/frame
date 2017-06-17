package org.smart4j.framework.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Logger;
import org.smart4j.framework.ConfigConstant;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.PropsUtil;

/**
 * 数据库帮助类
 * @author Administrator
 *
 */
public final class DataBaseHelper {
	
	private static final Logger LOGGER = Logger.getLogger(DataBaseHelper.class);
	private static final QueryRunner QUERY_RUNNER = new QueryRunner();
	private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();
	private static final BasicDataSource DATA_SOURCE;
	
	
	static{
		Properties props = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);
		String driver = props.getProperty(ConfigConstant.JDBC_DRIVER);
		String url = props.getProperty(ConfigConstant.JDBC_URL);
		String username = props.getProperty(ConfigConstant.JDBC_USERNAME);
		String password = props.getProperty(ConfigConstant.JDBC_PASSWORK);
		
		//也不需要静态变量接收了 注册服务被连接池代替 同时也可以管理关闭等操作
//		try {
//			Class.forName(DRIVER);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			LOGGER.error("注册数据库失败 load driver failure");
//		}
		
		DATA_SOURCE = new BasicDataSource();
		DATA_SOURCE.setDriverClassName(driver);
		DATA_SOURCE.setUrl(url);
		DATA_SOURCE.setUsername(username);
		DATA_SOURCE.setPassword(password);
	}
	
	/**
	 * 获取数据库连接
	 */
	public static Connection getConnection(){
		Connection conn = CONNECTION_HOLDER.get();
		try {
			if (conn == null) {
				conn = DATA_SOURCE.getConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("建立连接失败  get conn failure");
		}finally {
			CONNECTION_HOLDER.set(conn);
		}
		return conn;
	}
	
//	/**
//	 * 关闭数据库
//	 */
//	public static void closeConnection(){
//		Connection conn = CONNECTION_HOLDER.get();
//		if (conn != null) {
//			try {
//				conn.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//				LOGGER.error("关闭连接失败");
//			}
//		}
//	}
//	
	/**
	 * 查询实体列表
	 */
	public static <T>List<T> queryEntityList(Class<T> entityClass, String sql, Object... params){
		List<T> entityList = null;
		Connection conn = getConnection();
		try {
			entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
		} catch (SQLException e) {
			LOGGER.error("查询实体列表失败");
			throw new RuntimeException();
		}
		return entityList;
	}
	
	/**
	 * 查询单个对象
	 */
	public static <T>T queryEntity(Class<T> entityClass, String sql, Object... params){
		T entity;
		Connection conn = getConnection();
		try {
			entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
		} catch (SQLException e) {
			LOGGER.error("查找对象失败");
			throw new RuntimeException();
		}
		
		return entity;
	}
	
	/**
	 * 执行查询语句(动态)
	 */
	public static List<Map<String, Object>> executeQuery(String sql, Object... params){
		List<Map<String, Object>> result = null;
		
		Connection conn = getConnection();
		try {
			result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
		} catch (SQLException e) {
			LOGGER.error("查找失败");
			throw new RuntimeException();
		}
		return result;
	}
	
	/**
	 * 执行更新方法(通用方法)
	 */
	public static int executeUpdate(String sql, Object... params){
		int rows = 0;
		Connection conn = getConnection();
		try {
			rows = QUERY_RUNNER.update(conn, sql, params);
		} catch (SQLException e) {
			LOGGER.error("更新失败");
			throw new RuntimeException();
		}
		return rows;
	}
	
	/**
	 * 添加方法
	 * @param <T>
	 * @return 
	 */
	public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap){
		if (CollectionUtil.isEmpty(fieldMap)) {
			LOGGER.error("找不到传入的参数 can not insert entity fieldMap is empty");
			return false;
		}
		
		String sql = "INSERT INTO " + getTableName(entityClass);
		StringBuilder columns = new StringBuilder("(");
		StringBuilder values = new StringBuilder("(");
		
		for (String fieldName : fieldMap.keySet()){
			columns.append(fieldName).append(", ");
			values.append("?, ");
		}
		
		columns.replace(columns.lastIndexOf(","), columns.length(), " )");
		values.replace(values.lastIndexOf(","), values.length(), " )");
		
		sql += columns + " VALUES " + values;
		
		Object[] params = fieldMap.values().toArray();
		
		return executeUpdate(sql, params) == 1 ? true : false;
	}
	
	/**
	 * 修改方法 
	 */
	public static boolean updateEntity(Class<?> entityClass, long id, Map<String, Object> fieldMap){
		if (CollectionUtil.isEmpty(fieldMap)) {
			LOGGER.error("找不到传入的参数 can not update entity fieldMap is empty");
			return false;
		}
		
		String sql = "UPDATE " + getTableName(entityClass) + " SET ";
		StringBuilder columns = new StringBuilder("");

		for (String fieldName : fieldMap.keySet()){
			columns.append(fieldName + " = ?, ");
		}
		
		columns.replace(columns.lastIndexOf(","), columns.length(), "");
		sql += columns + "WHERE id = ?";
		
		List<Object> list = new ArrayList<Object>();
		list.addAll(fieldMap.values());
		list.add(id);
		Object[] array = list.toArray();
		return executeUpdate(sql, array) > 0 ? true : false;
	}
	
	/**
	 * 删除方法
	 */
	public static boolean deleteEntity(Class<?> entityClass, long id){
		String sql = "delete from " + getTableName(entityClass) + " where id = ?";
		return executeUpdate(sql, id) > 0 ? true : false;
	}
	
	/**
	 * 获取表名
	 */
	private static String getTableName(Class<?> entityClass){
		return entityClass.getSimpleName();
	}
	
	/**
	 * 执行sql文件
	 * @throws IOException 
	 */
	public static void executeSqlFile(String sqlPath) throws IOException{
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sqlPath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	
		String str;
		while ((str = reader.readLine()) != null) {
			System.out.println(str);
			executeUpdate(str);
		}	
	}
}
