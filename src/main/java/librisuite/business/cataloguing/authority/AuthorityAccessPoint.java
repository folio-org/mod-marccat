/*
 * (c) LibriCore
 * 
 * Created on Dec 2, 2005
 * 
 * AuthorityAccessPoint.java
 */
package librisuite.business.cataloguing.authority;

import librisuite.business.cataloguing.bibliographic.PersistsViaItem;
import librisuite.business.cataloguing.common.AccessPoint;
import librisuite.business.cataloguing.common.ItemEntity;
import librisuite.hibernate.AUT;

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
	 * @see librisuite.business.cataloguing.bibliographic.PersistsViaItem#getItemEntity()
	 */
	public ItemEntity getItemEntity() {
		return getAutItm();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.PersistsViaItem#setItemEntity(librisuite.business.cataloguing.common.ItemEntity)
	 */
	public void setItemEntity(ItemEntity item) {
		setAutItm((AUT)item);
	}

}
