/*
 * (c) LibriCore
 * 
 * Created on Apr 13, 2006
 * 
 * ItemDisplayFormat.java
 */
package org.folio.cataloging.bean.cataloguing.bibliographic.codelist;

import org.folio.cataloging.dao.persistence.T_BIB_DSPLY_FRMT;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/08/31 12:13:01 $
 * @since 1.0
 */
public class BibliographicDisplayFormat extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public BibliographicDisplayFormat() {
		super(T_BIB_DSPLY_FRMT.class);
	}

}
