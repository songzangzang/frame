package org.smart4j.framework.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 读取配置工具类
 * @author Administrator
 *
 */
public final class PropsUtil {

	private static final Logger LOGGER = Logger.getLogger(PropsUtil.class);
	
	/**
	 * 加载属性文件
	 * @throws FileNotFoundException 
	 */
	public static Properties loadProps (String fileName){
		InputStream is = null;
		Properties  props = null;
		props = new Properties();
		is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		if (is == null) {
			try {
				throw new FileNotFoundException(fileName + "noe found");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				LOGGER.error("找不到指定文件  not find "+fileName+"");
			}
		}
		
		try {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("加载配置文件失败 load prps failure");
		}finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					LOGGER.error("关闭配置文件流失败 close InputStream failure");
				}
			}
		}
		
		return props;
	}
	
	/**
	 * 获取字符串型属性
	 */
	public static String getString(Properties props, String key){
		return getString(props, key, "");
	}
	
	public static String getString(Properties props, String key, String defaultValue){
		String value = defaultValue;
		if (props.containsKey(key)) {
			value = props.getProperty(key);
		}
		return value;
	}
	
	/**
	 * 获取数值型属性
	 */
	public static int getInt(Properties props, String key){
		return getInt(props, key, 0);
	}
	
	public static int getInt(Properties props, String key, int defaultValue){
		int value = defaultValue;
		if (props.contains(key)) {
			value = CastUtil.castInt(props.getProperty(key));
		}
		return value;
	}
	
	/**
	 * 获取布尔类型
	 */
	public static boolean getBoolean(Properties props, String key){
		return getBoolean(props, key, false);
	}
	
	public static boolean getBoolean(Properties props, String key, Boolean defaultValue){
		boolean value = defaultValue;
		if (props.containsKey(key)) {
			value = CastUtil.castBoolean(props.getProperty(key));
		}
		return value;
	}
	
}
