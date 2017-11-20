/*
 * (c) LibriCore
 * 
 * Created on Jan 20, 2005
 * 
 * NameTitleComponent.java
 */
package org.folio.cataloging.business.cataloguing.bibliographic;


/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public abstract class NameTitleComponent extends BibliographicAccessPoint {

	protected int nameTitleHeadingNumber = 0;

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public NameTitleComponent() {
		super();
	}

	/**
	 * Class constructor
	 *
	 * @param itemNbr
	 * @since 1.0
	 */
	public NameTitleComponent(int itemNbr) {
		super(itemNbr);
	}

	/**
		 * 
		 */
	public int getNameTitleHeadingNumber() {
		return nameTitleHeadingNumber;
	}

	/**
		 * @param i
		 */
	public void setNameTitleHeadingNumber(int i) {
		nameTitleHeadingNumber = i;
	}

	public boolean isPartOfNameTitle() {
		return getNameTitleHeadingNumber() != 0;
	}
}
