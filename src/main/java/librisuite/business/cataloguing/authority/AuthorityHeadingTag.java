/*
 * (c) LibriCore
 * 
 * Created on Nov 17, 2005
 * 
 * AuthorityHeadingTag.java
 */
package librisuite.business.cataloguing.authority;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import librisuite.business.cataloguing.bibliographic.PersistsViaItem;
import librisuite.business.cataloguing.bibliographic.VariableField;
import librisuite.business.cataloguing.common.Browsable;
import librisuite.business.cataloguing.common.ItemEntity;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DataAccessException;
import librisuite.business.descriptor.Descriptor;
import librisuite.business.exception.NoHeadingSetException;
import librisuite.business.exception.ValidationException;
import librisuite.hibernate.AUT;

import com.libricore.librisuite.common.StringText;

/**
 * @author paulm
 * @version $Revision: 1.6 $, $Date: 2006/01/11 13:36:22 $
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
	 * @see librisuite.business.cataloguing.bibliographic.PersistsViaItem#getItemEntity()
	 */
	public ItemEntity getItemEntity() {
		return autItm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.PersistsViaItem#setItemEntity(librisuite.business.cataloguing.common.ItemEntity)
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
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#getStringText()
	 */
	public StringText getStringText() {
		StringText result = new StringText(getDescriptor().getStringText());
		result.parse(getAutItm().getVariableHeadingStringText());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#setStringText(com.libricore.librisuite.common.StringText)
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
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#parseModelXmlElementContent(org.w3c.dom.Element)
	 */
	public void parseModelXmlElementContent(Element xmlElement) {
		setDescriptorStringText(StringText
				.parseModelXmlElementContent(xmlElement));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.common.Tag#getCategory()
	 */
	public short getCategory() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.common.TagInterface#getFirstCorrelationList()
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#getCorrelationValues()
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
	 * @see librisuite.business.cataloguing.bibliographic.VariableField#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		descriptor.setCorrelationValues(v);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.common.TagInterface#isBrowsable()
	 */
	public boolean isBrowsable() {
		/*
		 * once a heading has been selected, it cannot be changed. The user must
		 * delete this authority and create a new one with a different heading
		 */
		if (getDescriptor().isNew()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.common.Tag#hasBrowsableContent()
	 */
	public boolean hasBrowsableContent() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.common.Browsable#getHeadingNumber()
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
	 * @see librisuite.business.cataloguing.common.Browsable#setHeadingNumber(java.lang.Integer)
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
	 * @see librisuite.business.cataloguing.common.Browsable#getEditableSubfields()
	 */
	public StringText getEditableSubfields() {
		return new StringText(getAutItm().getVariableHeadingStringText());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.common.TagInterface#isAbleToBeDeleted()
	 */
	public boolean isAbleToBeDeleted() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.common.Browsable#getValidEditableSubfields()
	 */
	public Set getValidEditableSubfields() {
		return getTagImpl().getValidEditableSubfields(getCategory());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.common.Browsable#setDescriptorStringText(com.libricore.librisuite.common.StringText)
	 */
	public void setDescriptorStringText(StringText tagStringText) {
		getDescriptor().setStringText(tagStringText.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.common.TagInterface#validate(int)
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
