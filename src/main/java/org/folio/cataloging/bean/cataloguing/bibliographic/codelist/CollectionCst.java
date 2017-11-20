/*
 * (c) LibriCore
 * 
 * Created on Jan 25, 2006
 * 
 * Collection.java
 */
package org.folio.cataloging.bean.cataloguing.bibliographic.codelist;

import org.folio.cataloging.dao.persistence.T_CLCTN_CST_TYP;;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/02/01 14:07:37 $
 * @since 1.0
 */
public class CollectionCst extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public CollectionCst() {
		super(T_CLCTN_CST_TYP.class);
	}

}
