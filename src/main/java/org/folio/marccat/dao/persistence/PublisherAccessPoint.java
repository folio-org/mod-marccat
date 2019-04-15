package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.bibliographic.BibliographicAccessPoint;
import org.folio.marccat.business.descriptor.PublisherTagDescriptor;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

import java.util.Collections;
import java.util.List;

/**
 * Persistent class for PUBL_ACS_PNT.
 *
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class PublisherAccessPoint extends BibliographicAccessPoint {
  private static final long serialVersionUID = 8837067946333999273L;

  private String otherSubfields;
  private int sequenceNumber;
  private PublisherTagDescriptor descriptor = new PublisherTagDescriptor();

  public PublisherAccessPoint() {
    super();
  }

  public PublisherAccessPoint(final int itemNbr) {
    super(itemNbr);
  }

  /**
   * Return descriptor associated to publisher access point.
   *
   * @return descriptor.
   */
  public Descriptor getDescriptor() {
    return descriptor;
  }

  /**
   * Sets descriptor.
   *
   * @param publisherTagDescriptor -- the descriptor associated to publisher access point.
   */
  public void setDescriptor(Descriptor publisherTagDescriptor) {
    descriptor = (PublisherTagDescriptor) publisherTagDescriptor;
  }

  /**
   * @return the content of subfield c
   */
  public String getDate() {
    return new StringText(getOtherSubfields()).getSubfieldsWithCodes("c").toDisplayString();
  }

  /**
   * @return the content of other subfields.
   */
  public String getOtherSubfields() {
    return otherSubfields;
  }

  /**
   * Sets content of other subfields.
   *
   * @param other -- the content to set.
   */
  public void setOtherSubfields(final String other) {
    otherSubfields = other;
  }

  /**
   * @return sequence number of tag.
   */
  @Override
  public Integer getSequenceNumber() {
    return sequenceNumber;
  }

  /**
   * Sets the sequence number of tag.
   *
   * @param sequence -- the sequence number to set.
   */
  public void setSequenceNumber(final int sequence) {
    sequenceNumber = sequence;
    super.setSequenceNumber(sequenceNumber);
  }

  /**
   * Compares an object with another one.
   *
   * @param obj -- the object to compare.
   * @return true if equals.
   */
  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof PublisherAccessPoint))
      return false;

    final PublisherAccessPoint other = (PublisherAccessPoint) obj;
    if (this.getHeadingNumber() == null) {
      return (other.getItemNumber() == this.getItemNumber() && other.getUserViewString().equals(this.getUserViewString())
        && other.getHeadingNumber() == null && other.getFunctionCode() == this.getFunctionCode());
    } else {
      return (other.getItemNumber() == this.getItemNumber() && other.getUserViewString().equals(this.getUserViewString())
        && (!(other.getHeadingNumber() == null)) && other.getHeadingNumber().equals(this.getHeadingNumber())
        && other.getFunctionCode() == this.getFunctionCode());
    }
  }

  /**
   * @return hashCode.
   */
  @Override
  public int hashCode() {
    return this.getItemNumber() + 3 * this.getFunctionCode();
  }

  @Deprecated
  public List getFirstCorrelationList() {
    return Collections.emptyList();
  }

  /**
   * Gets permission string to compare with authorization agent.
   *
   * @return "editNotes".
   */
  @Override
  public String getRequiredEditPermission() {
    return Global.PUBLISHER_REQUIRED_PERMISSION;
  }

  /**
   * Gets correlation values of publisher descriptor.
   *
   * @return correlationValues.
   */
  public CorrelationValues getCorrelationValues() {
    return getDescriptor().getCorrelationValues().change(1, getFunctionCode());
  }

  /**
   * Sets correlation values to publisher descriptor.
   *
   * @param v -- the correlation values to set.
   */
  public void setCorrelationValues(final CorrelationValues v) {
    setFunctionCode(v.getValue(1));
    getDescriptor().setCorrelationValues(v);
  }

  /**
   * Gets publisher marc category code.
   *
   * @return category.
   */
  @Override
  public int getCategory() {
    return Global.PUBLISHER_CATEGORY;
  }

  /**
   * Gets the stringText associated to publisher access point.
   *
   * @return stringText.
   */
  public StringText getAccessPointStringText() {
    return new StringText(otherSubfields);
  }

  /**
   * Sets stringText to publisher access point.
   *
   * @param stringText -- the stringText to set.
   */
  public void setAccessPointStringText(final StringText stringText) {
    otherSubfields = stringText.getSubfieldsWithCodes(Global.PUBLISHER_OTHER_SUBFIELD_CODES).toString();
  }

  /**
   * Sets descriptor string text.
   *
   * @param stringText -- the string text to set.
   */
  public void setDescriptorStringText(final StringText stringText) {
    getDescriptor().setStringText(stringText.getSubfieldsWithoutCodes(Global.PUBLISHER_VARIANT_CODES).toString());
  }

  /**
   * Sets the heading number to publisher access point.
   *
   * @param i -- the heading number to set.
   */
  @Override
  public void setHeadingNumber(final Integer i) {
    headingNumber = (i != null && i == 0) ? null : i;
  }

  /**
   * @return variant codes associated.
   */
  @Override
  public String getVariantCodes() {
    return Global.PUBLISHER_VARIANT_CODES;
  }

  /**
   * Mark descriptor as "changed".
   */
  public void markChanged() {
    if (getDescriptor() != null)
      getDescriptor().markChanged();
  }
}
