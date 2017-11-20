/*
 * (c) LibriCore
 * 
 * Created on Nov 23, 2005
 * 
 * AuthorityStructure.java
 */
package org.folio.cataloging.bean.cataloguing.bibliographic.codelist;

import org.folio.cataloging.dao.persistence.T_REF_AUT_STRCT;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class AuthorityStructure extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public AuthorityStructure() {
		super(T_REF_AUT_STRCT.class);
	}

}
