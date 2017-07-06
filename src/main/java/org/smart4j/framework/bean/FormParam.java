package org.smart4j.framework.bean;
/**
 * 封装表单参数
 * @author Administrator
 *
 */
public class FormParam {

	private String fieldName;
	
	private String fieldValue;
	
	public FormParam(String fieldName, String fieldValue){
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}
	
}
