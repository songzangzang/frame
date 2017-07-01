package org.smart4j.framework.bean;

import java.util.Map;

import org.smart4j.framework.util.CastUtil;
import org.smart4j.framework.util.CollectionUtil;

/**
 * 请求参数对象
 * @author Administrator
 *
 */
public class Param {

	private Map<String, Object> paramMap;
	
	public Param(Map<String, Object> paramMap){
		this.paramMap = paramMap;
	}
	
	/**
	 * 根据参数名获取long类型参数
	 */
	public long getLong(String name){
		return CastUtil.castLong(paramMap.get(name));
	}
	
	/**
	 * 获取所有字段信息
	 */
	public Map<String, Object> getMap(){
		return paramMap;
	}
	
	/**
	 * 判断传入参数是否为空
	 */
	public boolean isEmpty(){
		return CollectionUtil.isEmpty(paramMap);
	}
	
}
