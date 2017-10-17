/*
 * (c) LibriCore
 * 
 * Created on Nov 4, 2004
 * 
 * FixedField.java
 */
package librisuite.business.cataloguing.bibliographic;

import java.util.List;

import librisuite.business.cataloguing.common.HeaderField;
import librisuite.business.cataloguing.common.HeaderFieldHelper;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DataAccessException;
import librisuite.hibernate.CorrelationKey;

/**
 * @author paulm
 * @version $Revision: 1.20 $, $Date: 2005/12/21 13:33:35 $
 * @since 1.0
 */
public abstract class FixedField extends Tag implements HeaderField {
	private HeaderFieldHelper headerField = new BibliographicHeaderFieldHelper();

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public FixedField() {
	}

	/**
	 * Class constructor
	 *
	 * @param itemNumber
	 * @param itemType
	 * @since 1.0
	 */
	public FixedField(int itemNumber) {
		super(itemNumber);
	}

	public boolean isBrowsable() {
		return false;
	}

	public abstract String getDisplayString();

	public boolean isFixedField() {
		return true;
	}

	public boolean isEditableHeader() {
		return true;
	}

	public boolean isAbleToBeDeleted() {
		return false; //default implementation
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
	public short getCategory() {
		return headerField.getCategory();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		return headerField.getFirstCorrelationList();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getSecondCorrelationList(short value1)
		throws DataAccessException {
		return headerField.getSecondCorrelationList(value1);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public List getThirdCorrelationList(short value1, short value2)
		throws DataAccessException {
		return headerField.getThirdCorrelationList(value1, value2);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isHeaderField() {
		return headerField.isHeaderField();
	}

	public CorrelationKey getMarcEncoding()
		throws DataAccessException, MarcCorrelationException {
		CorrelationKey key = super.getMarcEncoding();
		return new CorrelationKey(
			key.getMarcTag(),
			' ',
			' ',
			key.getMarcTagCategoryCode());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getRequiredEditPermission()
	 */
	public String getRequiredEditPermission() {
		return "editHeader";
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return headerField.getCorrelationValues();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		headerField.setCorrelationValues(v);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isWorksheetEditable()
	 */
	public boolean isWorksheetEditable() {
		return false;
	}


	/**
	 * 
	 * @since 1.0
	 */
	public short getHeaderType() {
		return headerField.getHeaderType();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setHeaderType(short s) {
		headerField.setHeaderType(s);
	}

}
