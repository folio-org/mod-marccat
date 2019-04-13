/*
 * (c) LibriCore
 *
 * Created on Nov 21, 2005
 *
 * Browsable.java
 */
package org.folio.marccat.business.cataloguing.common;

import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.util.StringText;

import java.util.Set;

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

  Integer getKeyNumber();

  void setKeyNumber(Integer i);

  /**
   * Return the set of subfields that are valid for editing on the catalog worksheet
   *
   * @since 1.0
   */
  Set getValidEditableSubfields();

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
