package org.folio.marccat.dao.persistence;

import java.util.Set;
import java.util.TreeSet;

import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.cataloguing.common.Browsable;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

/**
 * @author elena
 *
 */
@SuppressWarnings("serial")
public class AuthorityHeadingTag extends VariableField implements PersistsViaItem, Browsable, HasHeadingType {
	private AUT autItm;
	Descriptor descriptor;

	public AuthorityHeadingTag(Descriptor d) {
		setDescriptor(d);
	}

	public ItemEntity getItemEntity() {
		return autItm;
	}

	public void setItemEntity(ItemEntity item) {
		autItm = (AUT) item;
	}

	private AUT getAutItm() {
		return autItm;
	}

	public Descriptor getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(Descriptor descriptor) {
		this.descriptor = descriptor;
		setHeadingNumber(descriptor.getKey().getHeadingNumber());
	}

	public StringText getStringText() {
		return new StringText(getDescriptor().getStringText());
	}

	public void setStringText(StringText stringText) {
		setDescriptorStringText(stringText);
	}

	public CorrelationValues getCorrelationValues() {
		return descriptor.getCorrelationValues();
	}

	public void setCorrelationValues(CorrelationValues v) {
		descriptor.setCorrelationValues(v);
	}

	@Override
	public boolean isBrowsable() {
		return true;
	}

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

	public StringText getEditableSubfields() {
		return new StringText();
	}

	@Override
	public boolean isAbleToBeDeleted() {
		return false;
	}

	public Set<Object> getValidEditableSubfields() {
		return new TreeSet<>();
	}

	public void setDescriptorStringText(StringText tagStringText) {
		getDescriptor().setStringText(tagStringText.toString());
	}

	@Override
	public int getCategory() {
		return 0;
	}
	
	@Override
	public boolean equals(Object anObject) {
		if (anObject instanceof AuthorityHeadingTag) {
			AuthorityHeadingTag aKey = (AuthorityHeadingTag) anObject;
			return (super.equals(aKey) && autItm == aKey.getAutItm() && descriptor == aKey.getDescriptor());
		} else {
			return false;
		}
	}

	@Override
	public String getVariantCodes() {
		return null;
	}

	@Override
	public String buildBrowseTerm() {
		return getDescriptor().buildBrowseTerm();
	}

	@Override	
	public int hashCode() {
		return super.hashCode()+ getDescriptor().getHeadingNumber();
	}

	@Override
	public Integer getHeadingNumber() {
		// TODO Auto-generated method stub
		return null;
	}
}
