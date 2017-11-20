/*
 * (c) LibriCore
 * 
 * Created on Jan 17, 2006
 * 
 * CrossReferenceType.java
 */
package org.folio.cataloging.bean.cataloguing.bibliographic.codelist;

import org.folio.cataloging.dao.persistence.ReferenceType;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/01/19 12:31:26 $
 * @since 1.0
 */
public class CrossReferenceType extends CodeListBean {

	/**
	 * Class constructor
	 *
	 * @param clazz
	 * @since 1.0
	 */
	public CrossReferenceType() {
		super(ReferenceType.class);
	}

}
