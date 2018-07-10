package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.cataloguing.bibliographic.PublisherAccessPoint;
import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.dao.PublisherDescriptorDAO;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.exception.InvalidDescriptorException;
import org.folio.cataloging.exception.MandatorySubfieldException;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;
import java.io.Serializable;


/**
 * Hibernate class for table PUBL_HDG.
 *
 * @author paulm
 * @author carment
 */
public class PUBL_HDG extends Descriptor implements Serializable {

	/** The indexing language. */
	private int indexingLanguage;

	/** The name string text. */
	private String nameStringText;

	/** The place string text. */
	private String placeStringText;

	/** The name sort form. */
	private String nameSortForm;

	/** The place sort form. */
	private String placeSortForm;

	/**
	 * Instantiates a new publ hdg.
	 */
	public PUBL_HDG() {
		super();
	}

	/* (non-Javadoc)
	 * @see Descriptor#getIndexingLanguage()
	 */
	public int getIndexingLanguage() {
		return indexingLanguage;
	}

	/**
	 * Sets the indexing language.
	 *
	 * @param s the new indexing language
	 */
	public void setIndexingLanguage(int s) {
		indexingLanguage = s;
	}

	/* (non-Javadoc)
	 * @see Descriptor#getReferenceClass(java.lang.Class)
	 */
	public Class getReferenceClass(Class targetClazz) {
		if (targetClazz == this.getClass()) {
			return PUBL_REF.class;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see Descriptor#getDefaultBrowseKey()
	 */
	public String getDefaultBrowseKey() {
		return "243P";
	}

	/**
	 * Gets the dao.
	 *
	 * @return the dao
	 */
	public HibernateUtil getDAO() {
		return new PublisherDescriptorDAO();
	}


	/* (non-Javadoc)
	 * @see Descriptor#getAccessPointClass()
	 */
	public Class getAccessPointClass() {
		return PublisherAccessPoint.class;
	}


	/* (non-Javadoc)
	 * @see Descriptor#getNextNumberKeyFieldCode()
	 */
	public String getNextNumberKeyFieldCode() {
		return "PU";
	}


	/* (non-Javadoc)
	 * @see Descriptor#getCategory()
	 */
	public int getCategory() {
		return 21;
	}


	/* (non-Javadoc)
	 * @see Descriptor#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return new CorrelationValues(
				CorrelationValues.UNDEFINED,
				CorrelationValues.UNDEFINED,
				CorrelationValues.UNDEFINED);
	}

	/* (non-Javadoc)
	 * @see Descriptor#setCorrelationValues(CorrelationValues)
	 */
	@Override
	public void setCorrelationValues(CorrelationValues v) {

	}

	/* (non-Javadoc)
	 * @see Descriptor#getSortFormParameters()
	 */
	public SortFormParameters getSortFormParameters() {
		return new SortFormParameters(100, 104, 0, 0, 0);
	}


	/**
	 * Gets the name sort form.
	 *
	 * @return the name sort form
	 */
	public String getNameSortForm() { return nameSortForm;}

	/**
	 * Gets the place sort form.
	 *
	 * @return the place sort form
	 */
	public String getPlaceSortForm() { return placeSortForm;}

	/**
	 * Gets the name string text.
	 *
	 * @return the name string text
	 */
	public String getNameStringText() {
		return nameStringText;
	}

	/**
	 * Gets the place string text.
	 *
	 * @return the place string text
	 */
	public String getPlaceStringText() {
		return placeStringText;
	}

	/**
	 * Sets the name string text.
	 *
	 * @param string the new name string text
	 */
	public void setNameStringText(String string) {
		nameStringText = string;
	}

	/**
	 * Sets the place string text.
	 *
	 * @param string the new place string text
	 */
	public void setPlaceStringText(String string) {
		placeStringText = string;
	}

	/* (non-Javadoc)
	 * @see Descriptor#getStringText()
	 */
	public String getStringText() {
		return getPlaceStringText() + getNameStringText();
	}

	/* (non-Javadoc)
	 * @see Descriptor#setSortForm(java.lang.String)
	 */
	public void setSortForm(String string) {

	}

	/* (non-Javadoc)
	 * @see Descriptor#getSortForm()
	 */
	public String getSortForm() {
		return getPlaceSortForm() + getNameSortForm();
	}

	/* (non-Javadoc)
	 * @see Descriptor#setStringText(java.lang.String)
	 */
	@Deprecated
	public void setStringText(String string) {
		StringText s = new StringText(string);
		setPlaceStringText(s.getSubfieldsWithCodes("a").toString());
		setNameStringText(s.getSubfieldsWithCodes("b").toString());
	}

	/* (non-Javadoc)
	 * @see Descriptor#getDisplayText()
	 */
	@Deprecated
	public String getDisplayText() {
		return new StringText(getPlaceStringText()).toDisplayString()
				+ " : "
				+ new StringText(getNameStringText()).toDisplayString();
	}


	/* (non-Javadoc)
	 * @see Descriptor#getHeadingNumberSearchIndexKey()
	 */
	public String getHeadingNumberSearchIndexKey() {
		return "370P";
	}


	/* (non-Javadoc)
	 * @see Descriptor#validate()
	 */
	@Deprecated
	public void validate() throws InvalidDescriptorException {
		StringText text = new StringText(getStringText());
		if (text.getSubfieldsWithCodes("b").getNumberOfSubfields() == 0
				&& (text.getSubfieldsWithCodes("a").getNumberOfSubfields() != 0
				&& !text.getSubfieldsWithCodes("a").isEmpty()))
			throw new MandatorySubfieldException("260", "b");
		super.validate();
	}

	/* (non-Javadoc)
	 * @see Descriptor#getLockingEntityType()
	 */
	public String getLockingEntityType() {
		return "PU";
	}

}
