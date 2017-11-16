/*
 * Created on May 6, 2004
 * */
package librisuite.hibernate;

import java.io.Serializable;
import java.util.Locale;

import librisuite.business.cataloguing.bibliographic.PublisherAccessPoint;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Defaults;
import librisuite.business.descriptor.DAOPublisherDescriptor;
import librisuite.business.descriptor.Descriptor;
import librisuite.business.descriptor.SortFormParameters;
import librisuite.business.exception.InvalidDescriptorException;
import librisuite.business.exception.MandatorySubfieldException;
import librisuite.business.searching.DAOIndexList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.StringText;
import com.libricore.librisuite.common.Subfield;

/**
 * Hibernate class for table PUBL_HDG
 * @author paulm
 * @version $Revision: 1.8 $, $Date: 2006/07/12 15:42:56 $
 * @since 1.0
 */
public class PUBL_HDG extends Descriptor implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(PUBL_HDG.class);
	private static Class referenceClass = PUBL_REF.class;
	private short indexingLanguage;
	private String nameSortForm;
	private String placeSortForm;
	private String nameStringText;
	private String placeStringText;

	/**
	 * 
	 * Class constructor - establishes default values for new names
	 *
	 * 
	 * @since 1.0
	 */
	public PUBL_HDG() {
		super();
		/*
		 * The default descriptor constructor initialises the string text to a
		 * blank subfield $a.  For publisher headings we do not want to do this.
		 */
		setPlaceStringText(
			Subfield.SUBFIELD_DELIMITER
				+ "a"
				+ Defaults.getString("publisher.place"));
		setNameStringText(
			Subfield.SUBFIELD_DELIMITER
				+ "b"
				+ Defaults.getString("publisher.name"));
		/*setAccessPointLanguage(
			Defaults.getShort("publisher.accessPointLanguage"));*/
		//TODO add other defaults		
		setVerificationLevel(Defaults.getChar("publisher.verificationLevel"));
	}

	/**
	 * Getter for indexingLanguage
	 * 
	 * @return indexingLanguage
	 */
	public short getIndexingLanguage() {
		return indexingLanguage;
	}

	/**
	 * Setter for indexingLanguage
	 * 
	 * @param s indexingLanguage
	 */
	public void setIndexingLanguage(short s) {
		indexingLanguage = s;
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.business.rdms.Descriptor#getReferenceClass()
	 */
	public Class getReferenceClass(Class targetClazz) {
		if (targetClazz == this.getClass()) {
			return referenceClass;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getDefaultBrowseKey()
	 */
	public String getDefaultBrowseKey() {
		return "243P";
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getDAO()
	 */
	public HibernateUtil getDAO() {
		return new DAOPublisherDescriptor();
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getAccessPointClass()
	 */
	public Class getAccessPointClass() {
		return PublisherAccessPoint.class;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getNextNumberKeyFieldCode()
	 */
	public String getNextNumberKeyFieldCode() {
		return "PU";
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getCategory()
	 */
	public short getCategory() {
		return 21;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return new CorrelationValues(
			CorrelationValues.UNDEFINED,
			CorrelationValues.UNDEFINED,
			CorrelationValues.UNDEFINED);
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		//do nothing
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getSortFormParameters()
	 */
	public SortFormParameters getSortFormParameters() {
		return new SortFormParameters(100, 104, 0,
		// AMICUS code (IDX_LIST) has 1 for name and 2 for place but
		// there doesn't seem to be any need since treatment is (currently)
		// identical
		0, 0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getNameSortForm() {
		return nameSortForm;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getNameStringText() {
		return nameStringText;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getPlaceSortForm() {
		return placeSortForm;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getPlaceStringText() {
		return placeStringText;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setNameSortForm(String string) {
		nameSortForm = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setNameStringText(String string) {
		nameStringText = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setPlaceSortForm(String string) {
		placeSortForm = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setPlaceStringText(String string) {
		placeStringText = string;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getSortForm()
	 */
	//TODO this is used in browse for next page etc, so needs to be geared to browse index
	public String getSortForm() {
		return getPlaceSortForm() + getNameSortForm();
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getStringText()
	 */
	public String getStringText() {
		return getPlaceStringText() + getNameStringText();
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#setSortForm(java.lang.String)
	 */
	public void setSortForm(String string) {
		// do nothing
		logger.warn("Attempt to set Publisher SortForm through default method");
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#setStringText(java.lang.String)
	 */
	public void setStringText(String string) {
		StringText s = new StringText(string);

		setPlaceStringText(s.getSubfieldsWithCodes("a").toString());
		setNameStringText(s.getSubfieldsWithCodes("b").toString());
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getDisplayText()
	 */
	public String getDisplayText() {
		return new StringText(getPlaceStringText()).toDisplayString()
			+ " : "
			+ new StringText(getNameStringText()).toDisplayString();
	}



	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.Descriptor#getHeadingNumberSearchIndexKey()
	 */
	public String getHeadingNumberSearchIndexKey() {
		return "370P";
	}

	/**
	 * 
	 */
	public void validate() throws InvalidDescriptorException {
		StringText text = new StringText(getStringText());
		if (text.getSubfieldsWithCodes("b").getNumberOfSubfields() == 0
				&& (text.getSubfieldsWithCodes("a").getNumberOfSubfields() != 0 
				&& !text.getSubfieldsWithCodes("a").isEmpty()))
			throw new MandatorySubfieldException("260", "b");
		super.validate();
	}
	
	public String getDisplayTextPlace() {
		return new StringText(getPlaceStringText()).toDisplayString();
	}
	
	public String getDisplayTextName() {
		return new StringText(getNameStringText()).toDisplayString();
	}
	public String getLockingEntityType() {
		return "PU";
	}

}
