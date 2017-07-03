package org.smart4j.framework.helper;

import java.util.Properties;

import org.smart4j.framework.ConfigConstant;
import org.smart4j.framework.util.PropsUtil;

/**
 * 属性文件助手类
 * @author Administrator
 *
 */
public class ConfigHelper {

	private static final Properties CONFIG_PROPS = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);
	
	/**
	 * 获得jdbc驱动
	 */
	public static String getJdbcDriver(){
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_DRIVER);
	}
	
	/**
	 * 获取url
	 */
	public static String getJdbcUrl(){
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_URL);
	}
	
	/**
	 * 获取用户名
	 */
	public static String getJdbcUsername(){
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_USERNAME);
	}
	
	/**
	 * 获取密码
	 */
	public static String getJdbcPassword(){
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_PASSWORK);
	}
	
	/**
	 * 获取基础包名
	 */
	public static String getAppBasePackage(){
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_BASE_PACKAGE);
	}
	
	/**
	 * 获取jsp路径
	 */
	public static String getAppJspPath(){
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_JSP_PATH, "/WEB-INF/view/");
	}
	
	/**
	 * 获取静态资源路径
	 */
	public static String getAppAssetPath(){
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_ASSET_PATH, "/asset/");
	}
	
	/**
	 * 获取应用文件上传限制
	 */
	public static int getAppUploadLimit(){
		return PropsUtil.getInt(CONFIG_PROPS, ConfigConstant.APP_UPLOAD_LIMIT, 10);
	}
	
	
}
