/*
 * (c) LibriCore
 * 
 * Created on Dec 2, 2005
 * 
 * AuthorityControlNumberAccessPoint.java
 */
package librisuite.business.cataloguing.authority;

import java.util.List;

import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DataAccessException;
import librisuite.business.descriptor.Descriptor;
import librisuite.hibernate.CNTL_NBR;
import librisuite.hibernate.T_AUT_CNTL_NBR_TYP;

import com.libricore.librisuite.common.StringText;

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
	 * @see librisuite.business.cataloguing.common.AccessPoint#getAccessPointStringText()
	 */
	public StringText getAccessPointStringText() {
		return new StringText();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#getCategory()
	 */
	public short getCategory() {
		return (short)5;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#getCorrelationValues()
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
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#getStringText()
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
	 * @see librisuite.business.cataloguing.common.AccessPoint#setAccessPointStringText(com.libricore.librisuite.common.StringText)
	 */
	public void setAccessPointStringText(StringText stringText) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		getDescriptor().setCorrelationValues(v);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.Browsable#setDescriptor(librisuite.hibernate.Descriptor)
	 */
	public void setDescriptor(Descriptor descriptor) {
			this.descriptor = (CNTL_NBR)descriptor;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.AccessPoint#setDescriptorStringText(com.libricore.librisuite.common.StringText)
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
