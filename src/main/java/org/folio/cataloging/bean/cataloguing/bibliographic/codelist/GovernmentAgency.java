/*
 * (c) LibriCore
 * 
 * Created on Dec 2, 2005
 * 
 * GovernmentAgency.java
 */
package org.folio.cataloging.bean.cataloguing.bibliographic.codelist;

import org.folio.cataloging.dao.persistence.T_AUT_GOVT_AGNCY;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public class GovernmentAgency extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public GovernmentAgency() {
		super(T_AUT_GOVT_AGNCY.class);
	}

}
