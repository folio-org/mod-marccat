package org.folio.marccat.dao.persistence;

import java.util.Set;

import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.cataloguing.common.Browsable;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.descriptor.SkipInFiling;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.AuthorityReferenceTagDAO;
import org.folio.marccat.exception.ModMarccatException;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

public abstract class AuthorityReferenceTag extends VariableField
    implements PersistsViaItem, PersistentObjectWithView, Browsable, SkipInFiling {

  /**
   * 
   */
  private static final long serialVersionUID = -6300884431435412541L;

  private static final String VARIANT_CODES = "wehij4";

  private AUT autItm;

  private REF reference;

  private Integer refTypeCorrelationPosition;
  private Descriptor targetDescriptor;

  protected AuthorityReferenceTag() {
    super();
    reference = new NME_REF();
    targetDescriptor = new NME_HDG();
  }

  @Override
  public String buildBrowseTerm() {
    return getDescriptor().buildBrowseTerm();
  }

  @Override
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return !v.isValueDefined(getRefTypeCorrelationPosition());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj != null && obj.getClass().equals(this.getClass())) {
      AuthorityReferenceTag aRef = (AuthorityReferenceTag) obj;
      return aRef.getReference().equals(this.getReference());
    }
    return false;
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

  public short getDualReferenceIndicator() {
    return T_DUAL_REF.NO;
  }

  public StringText getEditableSubfields() {
    return new StringText(getReference().getStringText());
  }

  public Integer getHeadingNumber() {
    return null;
  }

  public void setHeadingNumber(Integer i) {
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

  public boolean isHasDualIndicator() {
    return false; // default implementation
  }

  public boolean isNew() {
    return reference.isNew();
  }

  public void markNew() {
    reference.markNew();
  }

  public void setDescriptorStringText(StringText tagStringText) {
    getTargetDescriptor().setStringText(tagStringText.getSubfieldsWithoutCodes("w").toString());
  }

  @Override
  public void validate(int index) {
    // TODO
  }

}
