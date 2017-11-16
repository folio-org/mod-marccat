/*
 * (c) LibriCore
 * 
 * Created on Nov 4, 2004
 * 
 * VariableHeader.java
 */
package librisuite.business.cataloguing.bibliographic;

import java.util.List;

import librisuite.business.cataloguing.common.HeaderField;
import librisuite.business.cataloguing.common.HeaderFieldHelper;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DataAccessException;

import com.libricore.librisuite.common.StringText;

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
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#getStringText()
	 */
	public StringText getStringText() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#setStringText(com.libricore.librisuite.common.StringText)
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
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#getCategory()
	 */
	public short getCategory() {
		return headerField.getCategory();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#getFirstCorrelationList()
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		return headerField.getFirstCorrelationList();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#getSecondCorrelationList(short)
	 */
	public List getSecondCorrelationList(short value1)
		throws DataAccessException {
		return headerField.getSecondCorrelationList(value1);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#getThirdCorrelationList(short, short)
	 */
	public List getThirdCorrelationList(short value1, short value2)
		throws DataAccessException {
		return headerField.getThirdCorrelationList(value1, value2);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#isHeaderField()
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
