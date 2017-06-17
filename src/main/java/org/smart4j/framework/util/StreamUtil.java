package org.smart4j.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * 流操作工具类
 * @author Administrator
 *
 */
public final class StreamUtil {

	private static final Logger LOGGER = Logger.getLogger(StreamUtil.class);
	
	/**
	 * 从流中截取字符串
	 */
	public static String getString(InputStream is){
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			while((line = reader.readLine()) != null){
				sb.append(line);	
			}
		} catch (IOException e) {
			LOGGER.error("get string failure", e);
			throw new RuntimeException();
		}
		return sb.toString();
	}
	
}
