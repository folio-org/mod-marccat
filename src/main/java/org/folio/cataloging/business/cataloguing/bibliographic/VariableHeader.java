/*
 * (c) LibriCore
 * 
 * Created on Nov 4, 2004
 * 
 * VariableHeader.java
 */
package org.folio.cataloging.business.cataloguing.bibliographic;

import java.util.List;

import org.folio.cataloging.business.cataloguing.common.HeaderField;
import org.folio.cataloging.business.cataloguing.common.HeaderFieldHelper;
import org.folio.cataloging.business.common.CorrelationValues;
import org.folio.cataloging.business.common.DataAccessException;

import org.folio.cataloging.util.StringText;

/**
 * @author paulm
 * @version $Revision: 1.6 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public abstract class VariableHeader extends VariableField implements HeaderField {
	private HeaderFieldHelper headerField = new BibliographicHeaderFieldHelper();

	/**
	 * Class constructor
	 *
	 * @param itemNumber
	 * @param itemType
	 * @since 1.0
	 */
	public VariableHeader(int itemNumber) {
		super(itemNumber);
	}

	/**
	 * Class constructor
	 *
	 * @param itemType
	 * @since 1.0
	 */
	public VariableHeader() {
		super();
	}

	/* (non-Javadoc)
	 * @see VariableField#getStringText()
	 */
	public StringText getStringText() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see VariableField#setStringText(org.folio.cataloging.util.StringText)
	 */
	public void setStringText(StringText stringText) {
		// TODO Auto-generated method stub

	}

	public String getDisplayString() {
		return getStringText().getMarcDisplayString("$");
	}

	/**
	 * 
	 * @since 1.0
	 */
	public HeaderFieldHelper getHeaderField() {
		return headerField;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setHeaderField(HeaderFieldHelper helper) {
		headerField = helper;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public short getHeaderType() {
		return headerField.getHeaderType();
	}

	/* (non-Javadoc)
	 * @see VariableField#getCategory()
	 */
	public short getCategory() {
		return headerField.getCategory();
	}

	/* (non-Javadoc)
	 * @see VariableField#getFirstCorrelationList()
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		return headerField.getFirstCorrelationList();
	}

	/* (non-Javadoc)
	 * @see VariableField#getSecondCorrelationList(short)
	 */
	public List getSecondCorrelationList(short value1)
		throws DataAccessException {
		return headerField.getSecondCorrelationList(value1);
	}

	/* (non-Javadoc)
	 * @see VariableField#getThirdCorrelationList(short, short)
	 */
	public List getThirdCorrelationList(short value1, short value2)
		throws DataAccessException {
		return headerField.getThirdCorrelationList(value1, value2);
	}

	/* (non-Javadoc)
	 * @see VariableField#isHeaderField()
	 */
	public boolean isHeaderField() {
		return headerField.isHeaderField();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setHeaderType(short s) {
		headerField.setHeaderType(s);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return headerField.getCorrelationValues();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getRequiredEditPermission()
	 */
	public String getRequiredEditPermission() {
		return "editHeader";
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		headerField.setCorrelationValues(v);
	}

}
