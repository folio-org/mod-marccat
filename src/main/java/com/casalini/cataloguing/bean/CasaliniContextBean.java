package com.casalini.cataloguing.bean;

import javax.servlet.http.HttpServletRequest;

import librisuite.bean.CustomerContextBean;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Defaults;
import librisuite.hibernate.CasCache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.casalini.cataloguing.business.DAOCasCache;

// ## @LIBRICAT ## 

public class CasaliniContextBean extends CustomerContextBean {

	private static final Log logger = LogFactory.getLog(CasaliniContextBean.class);

	private static boolean casaliniEnabled = Defaults.getBoolean("customer.casalini.enabled", false);
	private boolean swapApplication = Defaults.getBoolean("customer.casalini.swap.application", false);
	
	private CasCache casCache;
	

	public CasCache getCasCache() {
		return casCache;
	}

	public void setCasCache(CasCache casCache) {
		this.casCache = casCache;
	}
	
	public CasCache createCasCache(){
		return casCache = new CasCache();
	}
	
	public CasCache loadCasCache(int bibNumber) throws DataAccessException{
		return (CasCache)(new DAOCasCache().getCasCache(bibNumber));
	}

	/**
	 * Public Testing Constructor
	 */
	public CasaliniContextBean() {}

	/**
	 * @param request
	 * @return
	 */
	public static CasaliniContextBean getInstance(HttpServletRequest request) {
		CasaliniContextBean bean =
			(CasaliniContextBean) CasaliniContextBean.getSessionAttribute(
				request,
				CasaliniContextBean.class);
		if (bean == null) {
			bean = new CasaliniContextBean();
			bean.setSessionAttribute(request, CasaliniContextBean.class);
		}
		return bean;
	}

	public boolean isEnabled() {
		return casaliniEnabled;
	}
	
	/**
	 * To access in static way
	 * @return
	 */
	public static boolean isCasaliniEnabled() {
		return casaliniEnabled;
	}

	public boolean isSwapApplication() {
		return swapApplication;
	}

	public String getDisplayName() {
		return "Casalini";
	}
	
	public String toString() {
		return super.toString() 
			+ " ["
			+ " enabled:"+casaliniEnabled
			+ ", is swap:"+swapApplication
			+ "]";
	}
	
}
