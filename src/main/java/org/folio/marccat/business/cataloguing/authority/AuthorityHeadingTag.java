package org.folio.marccat.business.cataloguing.authority;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.cataloguing.common.Browsable;
import org.folio.marccat.dao.persistence.AUT;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.ItemEntity;
import org.folio.marccat.exception.NoHeadingSetException;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;
import org.w3c.dom.Element;

import java.util.Set;

public abstract class AuthorityHeadingTag extends VariableField implements
  PersistsViaItem, Browsable, HasHeadingType {

  private static final String VARIANT_CODES = "ehj";

  private static Log logger = LogFactory.getLog(AuthorityHeadingTag.class);
  Descriptor descriptor;
  private AUT autItm;

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
   * @since 1.0
   */
  private AUT getAutItm() {
    return autItm;
  }

  /**
   * @since 1.0
   */
  public Descriptor getDescriptor() {
    return descriptor;
  }

  /**
   * @since 1.0
   */
  public void setDescriptor(Descriptor descriptor) {
    logger.debug("setDescriptor(" + descriptor + ")");
    this.descriptor = descriptor;
    setHeadingNumber(descriptor.getKey().getHeadingNumber());
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
   * @see VariableField#setStringText(org.folio.marccat.util.StringText)
   */
  public void setStringText(StringText stringText) {
    // paulm aut
    getAutItm().setVariableHeadingStringText(
      stringText.getSubfieldsWithCodes(getVariantCodes()).toString());
  }

  /*
   * (non-Javadoc)
   *
   * @see VariableField#parseModelXmlElementContent(org.w3c.dom.Element)
   */
  @Override
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
    return 0;
  }

  /*
   * (non-Javadoc)
   *
   * @see VariableField#getCorrelationValues()
   */
  public CorrelationValues getCorrelationValues() {
    return descriptor.getCorrelationValues();
  }

  /*
   * (non-Javadoc)
   *
   * @see VariableField#setCorrelationValues(librisuite.business.common.CorrelationValues)
   */
  public void setCorrelationValues(CorrelationValues v) {
    descriptor.setCorrelationValues(v);
  }

  @Override
  public boolean isWorksheetEditable() {
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see TagInterface#isBrowsable()
   */
  @Override
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
        return result;
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
  @Override
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
   * @see Browsable#setDescriptorStringText(org.folio.marccat.util.StringText)
   */
  public void setDescriptorStringText(StringText tagStringText) {
    getDescriptor().setStringText(tagStringText.toString());
  }

  /*
   * (non-Javadoc)
   *
   * @see TagInterface#validate(int)
   */
  @Override
  public void validate(int index) {
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
