package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicAccessPoint;
import org.folio.cataloging.business.cataloguing.common.OrderedTag;
import org.folio.cataloging.integration.GlobalStorage;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;

/**
 * Persistent class for CLSTN_ITM_ACS_PNT.
 *
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */

@SuppressWarnings("unchecked")
public class ClassificationAccessPoint extends BibliographicAccessPoint implements OrderedTag {
  private static final long serialVersionUID = 1L;

  private CLSTN descriptor = new CLSTN ( );
  private Integer sequenceNumber;

  public ClassificationAccessPoint() {
    super ( );
  }

  public ClassificationAccessPoint(final int itemNbr) {
    super (itemNbr);
  }

  /**
   * Return descriptor associated to classification access point.
   *
   * @return descriptor.
   */
  public Descriptor getDescriptor() {
    return descriptor;
  }

  /**
   * Sets descriptor.
   *
   * @param descriptor -- the descriptor associated to classification access point.
   */
  public void setDescriptor(final Descriptor descriptor) {
    this.descriptor = (CLSTN) descriptor;
  }

  /**
   * Sets descriptor string text.
   *
   * @param stringText -- the string text to set.
   */
  public void setDescriptorStringText(final StringText stringText) {
    descriptor.setStringText (stringText.toString ( ));
  }

  /**
   * Gets the stringText associated to classification access point.
   *
   * @return stringText.
   */
  public StringText getAccessPointStringText() {
    final StringText stringText = new StringText ( );
    addEditionNumberIfNeeds (stringText);
    return stringText;
  }

  /**
   * Sets stringText to classification access point.
   *
   * @param stringText -- the stringText to set.
   */
  public void setAccessPointStringText(final StringText stringText) {
    // nothing to do -- no apf text
  }

  /**
   * Checks if is a dewey and add sub-field 2.
   *
   * @param stringText -- the string text to add text.
   */
  private void addEditionNumberIfNeeds(final StringText stringText) {
    if (descriptor.getTypeCode ( ) == GlobalStorage.DEWEY_TYPE_CODE && descriptor.getDeweyEditionNumber ( ) != null)
      stringText.addSubfield (new Subfield ("2", "" + descriptor.getDeweyEditionNumber ( )));
  }

  /**
   * Gets classification marc category code.
   *
   * @return category.
   */
  public int getCategory() {
    return GlobalStorage.CLASSIFICATION_CATEGORY;
  }

  /**
   * Gets correlation values of classification descriptor.
   *
   * @return correlationValues.
   */
  public CorrelationValues getCorrelationValues() {
    return getDescriptor ( ).getCorrelationValues ( ).change (2, getFunctionCode ( ));
  }

  /**
   * Sets correlation values to classification descriptor.
   *
   * @param v -- the correlation values to set.
   */
  public void setCorrelationValues(CorrelationValues v) {
    setFunctionCode (v.getValue (2));
    getDescriptor ( ).setCorrelationValues (v);
  }

  /**
   * Checks correlation key value is changed for classification access point.
   *
   * @param v -- the correlation values.
   * @return boolean.
   */
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return (v.isValueDefined (2)) && (v.getValue (2) != getFunctionCode ( ));
  }

  /**
   * Gets permission string to compare with authorization agent.
   *
   * @return "editClassNumber".
   */
  public String getRequiredEditPermission() {
    return GlobalStorage.CLASSIFICATION_REQUIRED_PERMISSION;
  }

  /**
   * Compares an object with another one.
   *
   * @param obj -- the object to compare.
   * @return true if equals.
   */
  public boolean equals(final Object obj) {
    if (!(obj instanceof ClassificationAccessPoint))
      return false;

    final ClassificationAccessPoint other = (ClassificationAccessPoint) obj;
    return super.equals (obj) && (other.functionCode == this.functionCode)
      && (other.descriptor.getKey ( ).getHeadingNumber ( ) == this.descriptor.getKey ( ).getHeadingNumber ( ));
  }

  /**
   * @return hashCode.
   */
  public int hashCode() {
    return super.hashCode ( ); // TODO this is bad, should be changed
  }

  /**
   * Gets the sequence number associated to access point.
   *
   * @return sequenceNumber.
   */
  public Integer getSequenceNumber() {
    return sequenceNumber;
  }

  /**
   * Sets sequence number to access point.
   *
   * @param sequenceNbr -- the sequence number to set.
   */
  public void setSequenceNumber(final Integer sequenceNbr) {
    sequenceNumber = sequenceNbr;
    super.setSequenceNumber (sequenceNumber);
  }
}
