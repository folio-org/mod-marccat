package org.folio.cataloging.bean.cas;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.bean.CustomerContextBean;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.dao.persistence.CasCache;

import javax.servlet.http.HttpServletRequest;

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
		//TODO use session
		//return (CasCache)(new CasCacheDAO().getCasCache(bibNumber));
		return null;
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
