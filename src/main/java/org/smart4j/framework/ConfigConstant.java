package org.smart4j.framework;

/**
 * 提供相关配置项常量
 * @author Administrator
 *
 */
public interface ConfigConstant {
	
	String CONFIG_FILE = "smart.properties";
	
	String JDBC_DRIVER = "smart.framework.jdbc.driver";
	String JDBC_URL = "smart.framework.jdbc.url";
	String JDBC_USERNAME = "smart.framework.jdbc.username";
	String JDBC_PASSWORK = "smart.framework.jdbc.password";
	
	String APP_BASE_PACKAGE = "smart.framework.app_base_package";
	String APP_UPLOAD_LIMIT = "smart.framework.app_upload_limit";
	String APP_JSP_PATH = "smart.framework.app_jsp_path";
	String APP_ASSET_PATH = "smart.framework.app_asset_path";
}
