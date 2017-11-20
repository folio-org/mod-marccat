/*
 * (c) LibriCore
 * 
 * Created on Jul 28, 2005
 * 
 * SortCriteria.java
 */
package org.folio.cataloging.bean.searching;

import org.folio.cataloging.bean.cataloguing.bibliographic.codelist.CodeListBean;
import org.folio.cataloging.dao.persistence.T_SRT_CRTRIA;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/07/28 11:41:23 $
 * @since 1.0
 */
public class SortCriteria extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public SortCriteria() {
		super(T_SRT_CRTRIA.class);
	}

}
