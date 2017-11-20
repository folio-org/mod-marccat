/*
 * (c) LibriCore
 * 
 * Created on Oct 26, 2005
 * 
 * SearchTypeBean.java
 */
package org.folio.cataloging.bean.searching;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.folio.cataloging.bean.LibrisuiteBean;
import org.folio.cataloging.business.cataloguing.common.Catalog;
import org.folio.cataloging.business.common.DataAccessException;

/**
 * Manages searching and cataloguing activities that vary by the type of catalog item
 * being searched/catalogued (i.e. bib or auth)
 * @author paulm
  * 
 * @version $Revision: 1.4 $, $Date: 2006/12/14 10:31:07 $
 * @since 1.0
 */
public class SearchTypeBean extends LibrisuiteBean {

	private int searchingView = 1;
	
	public static SearchTypeBean getInstance(HttpServletRequest request) {
		SearchTypeBean bean = (SearchTypeBean)getSessionAttribute(request, SearchTypeBean.class);
		if (bean == null) {
			bean = new SearchTypeBean();
			bean.setSessionAttribute(request, SearchTypeBean.class);
		}
		return bean;
	}
	

	public void setSearchingView(int view) {
		searchingView = view;
	}
	
	public int getSearchingView() {
		return searchingView;
	}

	public Catalog getCatalog() {
		return Catalog.getInstanceByView(searchingView);
	}
	
	public List getAvailableModels() throws DataAccessException {
		return getCatalog().getModelDAO().getModelList();
	}

	// MIKE: used for dump.jsp
	public String toString() {
		return getClass().getName()+" searchingView="+searchingView;
	}
	

}
