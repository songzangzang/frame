package org.smart4j.framework.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.smart4j.framework.bean.FormParam;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CodecUtil;
import org.smart4j.framework.util.StreamUtil;
import org.smart4j.framework.util.StringUtil;

/**
 * 获取请求参数类
 * @author Administrator
 *
 */
public final class RequestHelper {

	public static Param createParam(HttpServletRequest request) throws IOException{
		List<FormParam> listFormParam = new ArrayList<FormParam>();
		getRequestMapper(request, listFormParam);
		getInputStreamMapper(request, listFormParam);
		Param param = new Param(listFormParam);
		return param;
	}

	/**
	 * get请求数据内容
	 * @param request
	 * @param listFormParam
	 * @return
	 */
	public static void getRequestMapper(HttpServletRequest request, List<FormParam> listFormParam){
		
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()){
			String paramName = paramNames.nextElement();
			String paramValue = request.getParameter(paramName);
			FormParam formParam = new FormParam(paramName, paramValue);
			listFormParam.add(formParam);
		}
		
	}
	
	/**
	 * post请求数据内容(从InputStream流中获取)
	 * @param request
	 * @param listFormParam
	 * @throws IOException
	 */
	public static void getInputStreamMapper(HttpServletRequest request, List<FormParam> listFormParam) throws IOException {
		
		String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
		if (StringUtil.isNotEmpty(body)) {
			String[] params = StringUtil.splitString(body, "&");
			if (ArrayUtil.isNotEmpty(params)) {
				for (String param : params) {
					String[] array = StringUtil.splitString(param, "=");
					String paramName = array[0];
					String paramValue = array[1];
					FormParam formParam = new FormParam(paramName, paramValue);
					listFormParam.add(formParam);
				}
			}
		}
		
	}
	
	
}
