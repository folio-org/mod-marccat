package org.folio.marccat.dao.persistence;

import java.io.Serializable;
import java.util.Set;

import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.cataloguing.common.Browsable;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.descriptor.DescriptorFactory;
import org.folio.marccat.business.descriptor.SkipInFiling;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.AuthorityReferenceTagDAO;
import org.folio.marccat.exception.ModMarccatException;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

public abstract class AuthorityReferenceTag extends VariableField
		implements PersistsViaItem, PersistentObjectWithView, Browsable, SkipInFiling {

	private static final String VARIANT_CODES = "wehij4";

	private AUT autItm;

	private REF reference;

	private Integer refTypeCorrelationPosition;
	private Descriptor targetDescriptor;

	public AuthorityReferenceTag() {
		super();
		reference = new NME_REF();
		targetDescriptor = new NME_HDG();
	}

	@Override
	public String buildBrowseTerm() {
		return getDescriptor().buildBrowseTerm();
	}

	public void changeHeadingType(short headingType) {
		Descriptor d = DescriptorFactory.createDescriptor(headingType);
		setTargetDescriptor(d);
	}

	@Override
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return !v.isValueDefined(getRefTypeCorrelationPosition());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().equals(this.getClass())) {
			AuthorityReferenceTag aRef = (AuthorityReferenceTag) obj;
			return aRef.getReference().equals(this.getReference());
		}
		return false;
	}

	public void generateNewKey() {
		getReference().setSource(getAutItm().getHeadingNumber());
	}

	public char getAuthorityStructure() {
		return reference.getAuthorityStructure();
	}

	public void setAuthorityStructure(char b) {
		reference.setAuthorityStructure(b);
	}

	public AUT getAutItm() {
		return autItm;
	}

	public void setAutItm(AUT aut) {
		autItm = aut;
	}

	public int getCategory() {
		try {
			return ((AccessPoint) getDescriptor().getAccessPointClass().newInstance()).getCategory();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ModMarccatException("Could not create an AccessPoint");
		}
	}

	public CorrelationValues getCorrelationValues() {
		CorrelationValues c = getTargetDescriptor().getCorrelationValues();
		setRefTypeCorrelationPosition(c.getFirstUnusedPosition());
		c = c.change(getRefTypeCorrelationPosition(), getReference().getType());

		for (int i = getRefTypeCorrelationPosition() + 1; i <= 3; i++) {
			c = c.change(i, CorrelationValues.UNDEFINED);
		}
		return c;
	}

	public void setCorrelationValues(CorrelationValues v) {
		getReference().setType(v.getValue(getRefTypeCorrelationPosition()));
		getTargetDescriptor().setCorrelationValues(v);
	}

	public AbstractDAO getDAO() {
		return new AuthorityReferenceTagDAO();
	}

	public Descriptor getDescriptor() {
		return getTargetDescriptor();
	}

	public void setDescriptor(Descriptor d) {
		setTargetDescriptor(d);
	}

	@Override
	public int getDisplayCategory() {
		return 16;
	}

	@Override
	public boolean getDisplaysHeadingType() {
		return true;
	}

	public short getDualReferenceIndicator() {
		return T_DUAL_REF.NO;
	}

	public char getEarlierRules() {
		return reference.getEarlierRules();
	}

	public void setEarlierRules(char b) {
		reference.setEarlierRules(b);
	}

	public StringText getEditableSubfields() {
		return new StringText(getReference().getStringText());
	}

	public Integer getHeadingNumber() {
		int result = getAutItm().getHeadingNumber();
		if (result > 0) {
			return result;
		} else {
			return null;
		}
	}

	public void setHeadingNumber(Integer i) {
		int setting;
		if (i == null) {
			setting = -1;
		} else {
			setting = i.intValue();
		}
		getReference().setTarget(setting);
	}

	public ItemEntity getItemEntity() {
		return getAutItm();
	}

	public void setItemEntity(ItemEntity item) {
		setAutItm((AUT) item);
	}

	@Override
	public CorrelationKey getMarcEncoding() {
		return super.getMarcEncoding().changeSkipInFilingIndicator(getSkipInFiling());
	}

	public char getNoteGeneration() {
		return reference.getNoteGeneration();
	}

	public void setNoteGeneration(char b) {
		reference.setNoteGeneration(b);
	}

	public char getPrintConstant() {
		return reference.getPrintConstant();
	}

	public void setPrintConstant(char b) {
		reference.setPrintConstant(b);
	}

	public REF getReference() {
		return reference;
	}

	public void setReference(REF ref) {
		reference = ref;
	}

	protected int getRefTypeCorrelationPosition() {
		if (refTypeCorrelationPosition == null) {
			getCorrelationValues();
		}
		return refTypeCorrelationPosition.intValue();
	}

	public void setRefTypeCorrelationPosition(Integer i) {
		refTypeCorrelationPosition = i;
	}

	public int getSkipInFiling() {
		if (getDescriptor() instanceof SkipInFiling) {
			return getDescriptor().getSkipInFiling();
		} else {
			return 0;
		}
	}

	public void setSkipInFiling(int i) {
		if (getDescriptor() instanceof SkipInFiling) {
			getDescriptor().setSkipInFiling(i);
		}
	}

	@Override
	public StringText getStringText() {
		StringText result = new StringText(getTargetDescriptor().getStringText());
		StringText variantCodes = new StringText(getReference().getStringText());
		StringText subi = variantCodes.getSubfieldsWithCodes("i");
		if (subi.getNumberOfSubfields() == 1) {
			result.addSubfield(0, subi.getSubfield(0));
		}
		result.add(variantCodes.getSubfieldsWithCodes("ej"));
		return result;
	}

	public void setStringText(StringText stringText) {
		getReference().setStringText(stringText.getSubfieldsWithCodes(getVariantCodes()).toString());
	}

	public Descriptor getTargetDescriptor() {
		return targetDescriptor;
	}

	public void setTargetDescriptor(Descriptor descriptor) {
		targetDescriptor = descriptor;
		/*
		 * make sure that the new descriptor is compatible with the reference class
		 */
		reference.setTarget(descriptor.getKey().getHeadingNumber());
	}

	public int getUpdateStatus() {
		return reference.getUpdateStatus();
	}

	public void setUpdateStatus(int i) {
		reference.setUpdateStatus(i);
	}

	public String getUserViewString() {
		return getReference().getUserViewString();
	}

	public void setUserViewString(String s) {
		getReference().setUserViewString(s);
	}

	public Set getValidEditableSubfields() {
		return getTagImpl().getValidEditableSubfields(getCategory());
	}

	public String getVariantCodes() {
		return VARIANT_CODES;
	}

	@Override
	public int hashCode() {
		return getReference().hashCode();
	}

	@Override
	public boolean isBrowsable() {
		return true;
	}

	public boolean isChanged() {
		return reference.isChanged();
	}

	public boolean isDeleted() {
		return reference.isDeleted();
	}

	public boolean isHasDualIndicator() {
		return false; // default implementation
	}

	public boolean isNew() {
		return reference.isNew();
	}

	@Override
	public boolean isRemoved() {
		return reference.isRemoved();
	}

	public void markChanged() {
		reference.markChanged();
	}

	public void markDeleted() {
		reference.markDeleted();
	}

	public void markNew() {
		reference.markNew();
	}

	public void markUnchanged() {
		reference.markUnchanged();
	}

	public boolean onDelete(Session arg0) throws CallbackException {
		return reference.onDelete(arg0);
	}

	public void onLoad(Session arg0, Serializable arg1) {
		reference.onLoad(arg0, arg1);
	}

	public boolean onSave(Session arg0) throws CallbackException {
		return reference.onSave(arg0);
	}

	public boolean onUpdate(Session arg0) throws CallbackException {
		return reference.onUpdate(arg0);
	}

	public void setDescriptorStringText(StringText tagStringText) {
		getTargetDescriptor().setStringText(tagStringText.getSubfieldsWithoutCodes("w").toString());
	}

	public Character getLinkDisplay() {
		return reference.getLinkDisplay();
	}

	public void setLinkDisplay(Character linkDisplay) {
		reference.setLinkDisplay(linkDisplay);
	}

	public Character getReplacementComplexity() {
		return reference.getReplacementComplexity();
	}

	public void setReplacementComplexity(Character replacementComplexity) {
		reference.setReplacementComplexity(replacementComplexity);
	}

	@Override
	public void validate(int index) {
		// TODO
	}

}
