package org.folio.cataloging.bean.searching;

import org.folio.cataloging.bean.LibrisuiteBean;
import org.folio.cataloging.business.cataloguing.common.Catalog;

import javax.servlet.http.HttpServletRequest;

/**
 * Manages searching and cataloguing activities that vary by the type of catalog item
 * being searched/catalogued (i.e. bib or auth)
 * @author paulm
  * 
 * @since 1.0
 */
@Deprecated
public class SearchTypeBean extends LibrisuiteBean {

	private int searchingView = 1;

	@Deprecated
	public static SearchTypeBean getInstance(HttpServletRequest request) {
		throw new IllegalArgumentException("Don't call me!");
/*
		SearchTypeBean bean = (SearchTypeBean)getSessionAttribute(request, SearchTypeBean.class);
		if (bean == null) {
			bean = new SearchTypeBean();
			bean.setSessionAttribute(request, SearchTypeBean.class);
		}
		return bean;
*/
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
}
