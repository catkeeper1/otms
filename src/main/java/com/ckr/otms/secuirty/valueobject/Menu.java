package com.ckr.otms.secuirty.valueobject;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity(name="Menu")
@Table(name="MENU")
public class Menu implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9008334019361686964L;
	private String code;
	private String parentCode;
	private String description;
	private String functionPoint;
	private String module;
	
	
	@Id
	@Column(name = "CODE", unique = true, nullable = false, length = 100)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "PARENT_CODE")
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "FUNCTION_POINT")
	public String getFunctionPoint() {
		return functionPoint;
	}
	public void setFunctionPoint(String functionPoint) {
		this.functionPoint = functionPoint;
	}
	
	@Column(name = "MODULE")
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}

}
