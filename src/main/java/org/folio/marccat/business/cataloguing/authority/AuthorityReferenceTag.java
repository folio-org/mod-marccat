/*
 * (c) LibriCore
 *
 * Created on Nov 22, 2005
 *
 * AuthorityReferenceTag.java
 */
package org.folio.marccat.business.cataloguing.authority;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.cataloguing.common.Browsable;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.business.descriptor.DescriptorFactory;
import org.folio.marccat.business.descriptor.SkipInFiling;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DAOAuthorityCorrelation;
import org.folio.marccat.dao.DAOAuthorityReferenceTag;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.exception.ModMarccatException;
import org.folio.marccat.exception.NoHeadingSetException;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;
import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class AuthorityReferenceTag
  extends VariableField
  implements PersistsViaItem, PersistentObjectWithView, Browsable, SkipInFiling {
  private static final Log logger =
    LogFactory.getLog(AuthorityReferenceTag.class);

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


  /**
   * Used to change the associated targetDescriptor type
   *
   * @since 1.0
   */
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

  public void evict() {
    reference.evict();
  }

  /**
   * @deprecated
   * @since 1.0
   */
  @Deprecated
  public void evict(Object obj) {
    reference.evict(((AuthorityReferenceTag) obj).getReference());
  }

  public void generateNewKey() {
    getReference().setSource(getAutItm().getKeyNumber());
  }

  /**
   * @since 1.0
   */
  public char getAuthorityStructure() {
    return reference.getAuthorityStructure();
  }

  /**
   * @since 1.0
   */
  public void setAuthorityStructure(char b) {
    reference.setAuthorityStructure(b);
  }

  /**
   * @since 1.0
   */
  public AUT getAutItm() {
    return autItm;
  }

  /**
   * @since 1.0
   */
  public void setAutItm(AUT aut) {
    autItm = aut;
  }

  /* (non-Javadoc)
   * @see TagInterface#getCategory()
   */
  public int getCategory() {
    try {
      return (
        (AccessPoint) getDescriptor()
          .getAccessPointClass()
          .newInstance())
        .getCategory();
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
    return new DAOAuthorityReferenceTag();
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

  /**
   * @since 1.0
   */
  public short getDualReferenceIndicator() {
    return T_DUAL_REF.NO;
  }

  /**
   * @since 1.0
   */
  public char getEarlierRules() {
    return reference.getEarlierRules();
  }

  /**
   * @since 1.0
   */
  public void setEarlierRules(char b) {
    reference.setEarlierRules(b);
  }

  /* (non-Javadoc)
   * @see Browsable#getEditableSubfields()
   */
  public StringText getEditableSubfields() {
    return new StringText(getReference().getDisplayValue());
  }


  /* (non-Javadoc)
   * @see Browsable#getKeyNumber()
   */
  public Integer getKeyNumber() {
    int result = getAutItm().getKeyNumber();
    if (result > 0) {
      return result;
    } else {
      return null;
    }
  }

  /* (non-Javadoc)
   * @see Browsable#setKeyNumber(java.lang.Integer)
   */
  public void setKeyNumber(Integer i) {
    int setting;
    if (i == null) {
      setting = -1;
    } else {
      setting = i.intValue();
    }
    getReference().setTarget(setting);
  }

  /* (non-Javadoc)
   * @see PersistsViaItem#getItemEntity()
   */
  public ItemEntity getItemEntity() {
    return getAutItm();
  }

  /* (non-Javadoc)
   * @see PersistsViaItem#setItemEntity(ItemEntity)
   */
  public void setItemEntity(ItemEntity item) {
    setAutItm((AUT) item);
  }

  /**
   * @deprecated
   * @since 1.0
   */
  @Deprecated
  @Override
  public CorrelationKey getMarcEncoding() {
    return super.getMarcEncoding().changeSkipInFilingIndicator(
      getSkipInFiling());
  }

  /**
   * @since 1.0
   */
  public char getNoteGeneration() {
    return reference.getNoteGeneration();
  }

  /**
   * @since 1.0
   */
  public void setNoteGeneration(char b) {
    reference.setNoteGeneration(b);
  }

  /**
   * @since 1.0
   */
  public char getPrintConstant() {
    return reference.getPrintConstant();
  }

  /**
   * @since 1.0
   */
  public void setPrintConstant(char b) {
    reference.setPrintConstant(b);
  }

  /**
   * @since 1.0
   */
  public REF getReference() {
    return reference;
  }

  /**
   * @since 1.0
   */
  public void setReference(REF ref) {
    reference = ref;
  }

  /*
   * The position of the referenceType correlation value varies by the type of
   * heading (3 for names, 1 for titles, etc.).  This attribute is set during the
   * call to getCorrelationValues (and should stay the same for any given instance).
   * @since 1.0
   */
  protected int getRefTypeCorrelationPosition() {
    if (refTypeCorrelationPosition == null) {
      // set the value  by calling getCorrelationValues()
      getCorrelationValues();
    }
    return refTypeCorrelationPosition.intValue();
  }

  /**
   * @since 1.0
   */
  public void setRefTypeCorrelationPosition(Integer i) {
    refTypeCorrelationPosition = i;
  }

  /* (non-Javadoc)
   * @see TagInterface#getSecondCorrelationList(short)
   */
  @Override
  public List getSecondCorrelationList(int value1) {
    if (getRefTypeCorrelationPosition() > 2) {
      if (getTargetDescriptor() instanceof NME_TTL_HDG) {
        return new NameAccessPoint().getSecondCorrelationList(value1);
      } else {
        try {
          return (
            (AccessPoint) getTargetDescriptor()
              .getAccessPointClass()
              .newInstance())
            .getSecondCorrelationList(
              value1);
        } catch (InstantiationException | IllegalAccessException e) {
          throw new ModMarccatException("ErrorCollection creating AccessPoint");
        }
      }
    } else if (getRefTypeCorrelationPosition() == 2) {
      return new DAOAuthorityCorrelation().getValidReferenceTypeList(this);
    } else {
      return new ArrayList<>();
    }
  }

  /* (non-Javadoc)
   * @see SkipInFiling#getSkipInFiling()
   */
  public int getSkipInFiling() {
    if (getDescriptor() instanceof SkipInFiling) {
      return getDescriptor().getSkipInFiling();
    } else {
      return 0;
    }
  }

  /* (non-Javadoc)
   * @see SkipInFiling#setSkipInFiling(short)
   */
  public void setSkipInFiling(int i) {
    if (getDescriptor() instanceof SkipInFiling) {
      getDescriptor().setSkipInFiling(i);
    }
  }

  @Override
  public StringText getStringText() {
    StringText result = new StringText(getTargetDescriptor().getDisplayValue());
    StringText variantCodes = new StringText(getReference().getDisplayValue());
    StringText subi = variantCodes.getSubfieldsWithCodes("i");
    if (subi.getNumberOfSubfields() == 1) {
      result.addSubfield(0, subi.getSubfield(0));
    }
    result.add(variantCodes.getSubfieldsWithCodes("ej"));
    return result;
  }

  /* (non-Javadoc)
   * @see VariableField#setDisplayValue(org.folio.marccat.util.StringText)
   */
  public void setStringText(StringText stringText) {
    //paulm aut
    getReference().setDisplayValue(stringText.getSubfieldsWithCodes(getVariantCodes()).toString());
    logger.debug("REF.StringText = " + getReference().getDisplayValue());
    // no setting of descriptor from worksheet
  }

  /**
   * @since 1.0
   */
  public Descriptor getTargetDescriptor() {
    return targetDescriptor;
  }

  /**
   * @since 1.0
   */
  public void setTargetDescriptor(Descriptor descriptor) {
    targetDescriptor = descriptor;
    /*
     * make sure that the new descriptor is compatible with the reference class
     */
    reference.setTarget(descriptor.getKey().getKeyNumber());
  }

  /* (non-Javadoc)
   * @see TagInterface#getThirdCorrelationList(short, short)
   */
  @Override
  public List getThirdCorrelationList(int value1, int value2) {
    logger.debug("getThirdCorrelationList(" + value1 + ", " + value2 + ")");
    if (getRefTypeCorrelationPosition() == 3) {
      logger.debug("refType is in pos 3");
      return new DAOAuthorityCorrelation().getValidReferenceTypeList(this);
    } else {
      return Collections.emptyList();
    }
  }

  /* (non-Javadoc)
   * @see Tag#getUpdateStatus()
   */
  public int getUpdateStatus() {
    return reference.getUpdateStatus();
  }

  /* (non-Javadoc)
   * @see Tag#setUpdateStatus(int)
   */
  public void setUpdateStatus(int i) {
    reference.setUpdateStatus(i);
  }

  /**
   * @since 1.0
   */
  public String getUserViewString() {
    return getReference().getUserViewString();
  }

  /**
   * @since 1.0
   */
  public void setUserViewString(String s) {
    getReference().setUserViewString(s);
  }

  /* (non-Javadoc)
   * @see Browsable#getValidEditableSubfields()
   */
  public Set getValidEditableSubfields() {
    return getTagImpl().getValidEditableSubfields(getCategory());
  }

  public String getVariantCodes() {
    return VARIANT_CODES;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return getReference().hashCode();
  }

  /* (non-Javadoc)
   * @see TagInterface#isBrowsable()
   */
  @Override
  public boolean isBrowsable() {
    return true;
  }

  /* (non-Javadoc)
   * @see Tag#isChanged()
   */
  public boolean isChanged() {
    return reference.isChanged();
  }

  /* (non-Javadoc)
   * @see Tag#isDeleted()
   */
  public boolean isDeleted() {
    return reference.isDeleted();
  }

  public boolean isHasDualIndicator() {
    return false; // default implementation
  }

  /* (non-Javadoc)
   * @see Tag#isNew()
   */
  public boolean isNew() {
    return reference.isNew();
  }

  /* (non-Javadoc)
   * @see Tag#isRemoved()
   */
  @Override
  public boolean isRemoved() {
    return reference.isRemoved();
  }

  @Override
  public boolean isWorksheetEditable() {
    return true;
  }

  /* (non-Javadoc)
   * @see Tag#markChanged()
   */
  public void markChanged() {
    reference.markChanged();
  }

  /* (non-Javadoc)
   * @see Tag#markDeleted()
   */
  public void markDeleted() {
    reference.markDeleted();
  }

  /* (non-Javadoc)
   * @see Tag#markNew()
   */
  public void markNew() {
    reference.markNew();
  }

  /* (non-Javadoc)
   * @see Tag#markUnchanged()
   */
  public void markUnchanged() {
    reference.markUnchanged();
  }

  /* (non-Javadoc)
   * @see Tag#onDelete(net.sf.hibernate.Session)
   */
  public boolean onDelete(Session arg0) throws CallbackException {
    return reference.onDelete(arg0);
  }

  /* (non-Javadoc)
   * @see Tag#onLoad(net.sf.hibernate.Session, java.io.Serializable)
   */
  public void onLoad(Session arg0, Serializable arg1) {
    reference.onLoad(arg0, arg1);
  }

  /* (non-Javadoc)
   * @see Tag#onSave(net.sf.hibernate.Session)
   */
  public boolean onSave(Session arg0) throws CallbackException {
    return reference.onSave(arg0);
  }

  /* (non-Javadoc)
   * @see Tag#onUpdate(net.sf.hibernate.Session)
   */
  public boolean onUpdate(Session arg0) throws CallbackException {
    return reference.onUpdate(arg0);
  }

  /* (non-Javadoc)
   * @see VariableField#parseModelXmlElementContent(org.w3c.dom.Element)
   */
  @Override
  public void parseModelXmlElementContent(Element xmlElement) {
    setDescriptorStringText(StringText.parseModelXmlElementContent(xmlElement));
  }

  /* (non-Javadoc)
   * @see Browsable#setDescriptorStringText(org.folio.marccat.util.StringText)
   */
  public void setDescriptorStringText(StringText tagStringText) {
    getTargetDescriptor().setDisplayValue(
      tagStringText.getSubfieldsWithoutCodes("w").toString());
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

  /* (non-Javadoc)
   * @see TagInterface#validate()
   */
  @Override
  public void validate(int index) {
    if (getTargetDescriptor().isNew()) {
      throw new NoHeadingSetException(index);
    }
  }


}
