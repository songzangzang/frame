package org.smart4j.framework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

/**
 * 编码解码工具类
 * @author Administrator
 *
 */
public final class CodecUtil {
	
	private static final Logger LOGGER = Logger.getLogger(CodecUtil.class);
	
	/**
	 * 将URL编码
	 */
	public static String encodeURL(String source){
		String target;
		try {
			target = URLEncoder.encode(source, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("encode url failure", e);
			throw new RuntimeException();
		}
		return target;
	}
	
	/**
	 * 将URL解码
	 */
	public static String decodeURL(String source){
		String target;
		try {
			target = URLDecoder.decode(source, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("decode url failure", e);
			throw new RuntimeException();
		}
		return target;
	}
}
