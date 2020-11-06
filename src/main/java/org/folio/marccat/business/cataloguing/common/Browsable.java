package org.folio.marccat.business.cataloguing.common;

import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.util.StringText;

/**
 * implemented by tags that support browsing for descriptor contents
 */
public interface Browsable {
  Descriptor getDescriptor();

  void setDescriptor(Descriptor d);

  /**
   * @return the portion of stringText that can be directly modified on the
   * catalog worksheet
   * @since 1.0
   */
  StringText getEditableSubfields();

  Integer getHeadingNumber();

  void setHeadingNumber(Integer i);



  /**
   * Extracts the "heading's" subfield codes and updates the stringText of the
   * Descriptor
   *
   * @since 1.0
   */
  void setDescriptorStringText(StringText tagStringText);

  /**
   * @return the AccessPoint's variant codes
   */
  String getVariantCodes();

  String buildBrowseTerm();

}
