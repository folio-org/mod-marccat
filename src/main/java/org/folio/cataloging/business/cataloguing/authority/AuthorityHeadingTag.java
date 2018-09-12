package org.folio.cataloging.business.cataloguing.authority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableField;
import org.folio.cataloging.business.cataloguing.common.Browsable;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.AUT;
import org.folio.cataloging.dao.persistence.Descriptor;
import org.folio.cataloging.dao.persistence.ItemEntity;
import org.folio.cataloging.exception.NoHeadingSetException;
import org.folio.cataloging.exception.ValidationException;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Set;

/**
 * @author paulm
 * @since 1.0
 */
public abstract class AuthorityHeadingTag extends VariableField implements
		PersistsViaItem, Browsable, HasHeadingType {

	private static final String VARIANT_CODES = "ehj";

	private static Log logger = LogFactory.getLog(AuthorityHeadingTag.class);

	private AUT autItm;
	Descriptor descriptor;

	AuthorityHeadingTag(Descriptor d) {
		setDescriptor(d);
	}

	@Override
	public String buildBrowseTerm() {
		return getDescriptor().buildBrowseTerm();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see PersistsViaItem#getItemEntity()
	 */
	public ItemEntity getItemEntity() {
		return autItm;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see PersistsViaItem#setItemEntity(ItemEntity)
	 */
	public void setItemEntity(ItemEntity item) {
		autItm = (AUT) item;
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
	public Descriptor getDescriptor() {
		return descriptor;
	}

	/**
	 *
	 * @since 1.0
	 */
	public void setDescriptor(Descriptor descriptor) {
		logger.debug("setDescriptor(" + descriptor + ")");
		this.descriptor = descriptor;
		setHeadingNumber(new Integer(descriptor.getKey().getHeadingNumber()));
		logger.debug("headingNumber set to " + getHeadingNumber());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see VariableField#getStringText()
	 */
	public StringText getStringText() {
		StringText result = new StringText(getDescriptor().getStringText());
		result.parse(getAutItm().getVariableHeadingStringText());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see VariableField#setStringText(org.folio.cataloging.util.StringText)
	 */
	public void setStringText(StringText stringText) {
		// paulm aut
		getAutItm().setVariableHeadingStringText(
				stringText.getSubfieldsWithCodes(getVariantCodes()).toString());
		// no setting of descriptor from worksheet
		// setDescriptorStringText(stringText);
		// setDescriptorStringText(stringText);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see VariableField#parseModelXmlElementContent(org.w3c.dom.Element)
	 */
	public void parseModelXmlElementContent(Element xmlElement) {
		setDescriptorStringText(StringText
				.parseModelXmlElementContent(xmlElement));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Tag#getCategory()
	 */
	public int getCategory() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see TagInterface#getFirstCorrelationList()
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see VariableField#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return descriptor.getCorrelationValues();
	}

	@Override
	public boolean isWorksheetEditable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see VariableField#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		descriptor.setCorrelationValues(v);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see TagInterface#isBrowsable()
	 */
	public boolean isBrowsable() {
		/*
		 * once a heading has been selected, it cannot be changed. The user must
		 * delete this authority and create a new one with a different heading
		 */
        return getDescriptor().isNew();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Tag#hasBrowsableContent()
	 */
	public boolean hasBrowsableContent() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Browsable#getHeadingNumber()
	 */
	public Integer getHeadingNumber() {
		int result = -1;
		if (getAutItm() != null) {
			result = getAutItm().getHeadingNumber();
			if (result > 0) {
				return new Integer(result);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Browsable#setHeadingNumber(java.lang.Integer)
	 */
	public void setHeadingNumber(Integer i) {
		int setting;
		if (i == null) {
			setting = -1;
		} else {
			setting = i.intValue();
		}
		if (getAutItm() != null) {
			getAutItm().setHeadingNumber(setting);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Browsable#getEditableSubfields()
	 */
	public StringText getEditableSubfields() {
		return new StringText(getAutItm().getVariableHeadingStringText());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see TagInterface#isAbleToBeDeleted()
	 */
	public boolean isAbleToBeDeleted() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Browsable#getValidEditableSubfields()
	 */
	public Set getValidEditableSubfields() {
		return getTagImpl().getValidEditableSubfields(getCategory());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Browsable#setDescriptorStringText(org.folio.cataloging.util.StringText)
	 */
	public void setDescriptorStringText(StringText tagStringText) {
		getDescriptor().setStringText(tagStringText.toString());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see TagInterface#validate(int)
	 */
	public void validate(int index) throws ValidationException {
		if (getDescriptor().isNew()) {
			throw new NoHeadingSetException(index);
		}
	}

	/**
	 * default implementation Browsable
	 */
	public String getVariantCodes() {
		return VARIANT_CODES;
	}

}
