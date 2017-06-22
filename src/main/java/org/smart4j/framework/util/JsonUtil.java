package org.smart4j.framework.util;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json工具类
 * @author Administrator
 *
 */
public final class JsonUtil {
	
	private static final Logger LOGGER = Logger.getLogger(JsonUtil.class);
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	/**
	 * 将POJO 转成 JSON
	 */
	public static <T> String toJson(T obj){
		String json = null;
		try {
			json = OBJECT_MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			LOGGER.error("convert POJO to JSON failure", e);
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 将JSON转换成POJO
	 */
	public static <T> T fromJson(String json, Class<T> type){
		T pojo;
		try {
			pojo = OBJECT_MAPPER.readValue(json, type);
		} catch (Exception e) {
			LOGGER.error("convert JSON to POJO failure", e);
			throw new RuntimeException();
		}
		return pojo; 
	}
	
	
	
	
}
