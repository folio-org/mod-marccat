package org.folio.cataloging.bean.searching;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class RestrictionBean {
	
	private String name;
	private String value;
	private Date date;
	private String userName;
	private String oldName;
	
	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public static RestrictionBean getInstance(HttpServletRequest request) 
	{
		RestrictionBean bean = (RestrictionBean) RestrictionBean.getSessionAttribute(request,RestrictionBean.class);
		if (bean == null){
			bean = new RestrictionBean();
		}
		request.getSession(false).setAttribute(bean.getClass().getName(), bean);
		return bean;
	}
	
	private static RestrictionBean getSessionAttribute(HttpServletRequest httpServletRequest, Class aClass) 
	{
		return (RestrictionBean) httpServletRequest.getSession(false).getAttribute(aClass.getName());
	}
}
