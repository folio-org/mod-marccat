/*
 * (c) LibriCore
 * 
 * Created on Dec 2, 2005
 * 
 * AuthorityControlNumberAccessPoint.java
 */
package org.folio.cataloging.business.cataloguing.authority;

import java.util.List;

import org.folio.cataloging.business.common.CorrelationValues;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.dao.persistence.CNTL_NBR;
import org.folio.cataloging.dao.persistence.T_AUT_CNTL_NBR_TYP;

import org.folio.cataloging.util.StringText;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/21 08:30:32 $
 * @since 1.0
 */
public class AuthorityControlNumberAccessPoint extends AuthorityAccessPoint {
	private CNTL_NBR descriptor = new CNTL_NBR();
	private char validationCode = 'a';

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public AuthorityControlNumberAccessPoint() {
		super();
	}

	/**
	 * Class constructor
	 *
	 * @param itemNumber
	 * @since 1.0
	 */
	public AuthorityControlNumberAccessPoint(int itemNumber) {
		super(itemNumber);
	}

	/* (non-Javadoc)
	 * @see AccessPoint#getAccessPointStringText()
	 */
	public StringText getAccessPointStringText() {
		return new StringText();
	}

	/* (non-Javadoc)
	 * @see TagInterface#getCategory()
	 */
	public short getCategory() {
		return (short)5;
	}

	/* (non-Javadoc)
	 * @see TagInterface#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return getDescriptor().getCorrelationValues();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public Descriptor getDescriptor() {
		return descriptor;
	}

	/* (non-Javadoc)
	 * @see VariableField#getStringText()
	 */
	public StringText getStringText() {
		StringText s = super.getStringText();
		if (getValidationCode() != 'a') {
			s.getSubfield(0).setCode(String.valueOf(getValidationCode()));
		}
		return s;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getFirstCorrelationList()
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		return getDaoCodeTable().getList(T_AUT_CNTL_NBR_TYP.class,false);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getValidationCode() {
		return validationCode;
	}

	/* (non-Javadoc)
	 * @see AccessPoint#setAccessPointStringText(org.folio.cataloging.util.StringText)
	 */
	public void setAccessPointStringText(StringText stringText) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see TagInterface#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		getDescriptor().setCorrelationValues(v);
	}

	/* (non-Javadoc)
	 * @see Browsable#setDescriptor(librisuite.hibernate.Descriptor)
	 */
	public void setDescriptor(Descriptor descriptor) {
			this.descriptor = (CNTL_NBR)descriptor;
	}

	/* (non-Javadoc)
	 * @see AccessPoint#setDescriptorStringText(org.folio.cataloging.util.StringText)
	 */
	public void setDescriptorStringText(StringText tagStringText) {
		getDescriptor().setStringText(tagStringText.toString());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setValidationCode(char c) {
		validationCode = c;
	}

}
