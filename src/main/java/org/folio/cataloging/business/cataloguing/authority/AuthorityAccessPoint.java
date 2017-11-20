/*
 * (c) LibriCore
 * 
 * Created on Dec 2, 2005
 * 
 * AuthorityAccessPoint.java
 */
package org.folio.cataloging.business.cataloguing.authority;

import org.folio.cataloging.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.cataloging.business.cataloguing.common.AccessPoint;
import org.folio.cataloging.business.cataloguing.common.ItemEntity;
import org.folio.cataloging.dao.persistence.AUT;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public abstract class AuthorityAccessPoint extends AccessPoint implements PersistsViaItem {

	private AUT autItm;
	
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public AuthorityAccessPoint() {
		super();
	}

	/**
	 * Class constructor
	 *
	 * @param itemNumber
	 * @since 1.0
	 */
	public AuthorityAccessPoint(int itemNumber) {
		super(itemNumber);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @since 1.0
	 */
	private AUT getAutItm() {
		return autItm;
	}

	/**
	 * 
	 * @since 1.0
	 */
	private void setAutItm(AUT aut) {
		autItm = aut;
	}

	/* (non-Javadoc)
	 * @see PersistsViaItem#getItemEntity()
	 */
	public ItemEntity getItemEntity() {
		return getAutItm();
	}

	/* (non-Javadoc)
	 * @see PersistsViaItem#setItemEntity(ItemEntity)
	 */
	public void setItemEntity(ItemEntity item) {
		setAutItm((AUT)item);
	}

}
