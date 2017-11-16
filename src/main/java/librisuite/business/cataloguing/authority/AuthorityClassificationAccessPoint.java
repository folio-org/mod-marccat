/*
 * (c) LibriCore
 * 
 * Created on Dec 5, 2005
 * 
 * AuthorityClassificationAccessPoint.java
 */
package librisuite.business.cataloguing.authority;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DAOAuthorityCorrelation;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Defaults;
import librisuite.business.descriptor.Descriptor;
import librisuite.hibernate.CLSTN;
import librisuite.hibernate.T_AUT_CLSTN_FNCTN;
import librisuite.hibernate.T_AUT_CLSTN_TYP;

import com.libricore.librisuite.common.StringText;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2006/04/27 12:56:53 $
 * @since 1.0
 */
public class AuthorityClassificationAccessPoint extends AuthorityAccessPoint {

	private static final Log logger = LogFactory.getLog(AuthorityClassificationAccessPoint.class);
	private static final String VARIANT_CODES ="d";

	private CLSTN descriptor = new CLSTN();

	private String volumeDate;

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public AuthorityClassificationAccessPoint() {
		super();
		descriptor.setTypeCode(Defaults.getShort("authority.classification.type"));
		setFunctionCode(Defaults.getShort("authority.classification.function"));
	}

	/**
	 * Class constructor
	 *
	 * @param itemNumber
	 * @since 1.0
	 */
	public AuthorityClassificationAccessPoint(int itemNumber) {
		super(itemNumber);
		descriptor.setTypeCode(Defaults.getShort("authority.classification.type"));
		setFunctionCode(Defaults.getShort("authority.classification.function"));
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.AccessPoint#getAccessPointStringText()
	 */
	public StringText getAccessPointStringText() {
		return new StringText(getVolumeDate());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#getCategory()
	 */
	public short getCategory() {
		return (short) 6;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		CorrelationValues v = getDescriptor().getCorrelationValues();
		return v.change(2, getFunctionCode());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public Descriptor getDescriptor() {
		return descriptor;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getFirstCorrelationList()
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		return getDaoCodeTable().getList(T_AUT_CLSTN_TYP.class,false);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getSecondCorrelationList(short)
	 */
	public List getSecondCorrelationList(short value1)
		throws DataAccessException {
		DAOAuthorityCorrelation dao = new DAOAuthorityCorrelation();
		List l = dao.getSecondCorrelationList(
			getCategory(),
			getHeadingType(),
			value1,
			T_AUT_CLSTN_FNCTN.class);
		Iterator iter = l.iterator();
		logger.debug("cat " + getCategory() + " type " + getHeadingType() + " val1 " + value1);
		while (iter.hasNext()) {
			T_AUT_CLSTN_FNCTN f = (T_AUT_CLSTN_FNCTN)iter.next();
			logger.debug("2nd corr: " + f.getCode() + " -- " + f.getLongText());
		}
		return l;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#getStringText()
	 */
	public StringText getStringText() {
		StringText s = super.getStringText();
		s.parse(getVolumeDate());
		return s;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getVolumeDate() {
		return volumeDate;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.AccessPoint#setAccessPointStringText(com.libricore.librisuite.common.StringText)
	 */
	public void setAccessPointStringText(StringText stringText) {
		setVolumeDate(stringText.getSubfieldsWithCodes("d").toString());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.TagInterface#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		setFunctionCode(v.getValue(2));
		getDescriptor().setCorrelationValues(v);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.Browsable#setDescriptor(librisuite.hibernate.Descriptor)
	 */
	public void setDescriptor(Descriptor descriptor) {
		this.descriptor = (CLSTN) descriptor;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.AccessPoint#setDescriptorStringText(com.libricore.librisuite.common.StringText)
	 */
	public void setDescriptorStringText(StringText tagStringText) {
		getDescriptor().setStringText(
			tagStringText.getSubfieldsWithoutCodes(VARIANT_CODES).toString());
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setVolumeDate(String string) {
		volumeDate = string;
	}
	
	public String getVariantCodes() {
		return VARIANT_CODES;
	}

}
