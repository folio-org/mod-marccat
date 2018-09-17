/*
 * (c) LibriCore
 *
 * Created on Nov 21, 2005
 *
 * Browsable.java
 */
package org.folio.cataloging.business.cataloguing.common;

import org.folio.cataloging.dao.persistence.Descriptor;
import org.folio.cataloging.util.StringText;

import java.util.Set;

/**
 * implemented by tags that support browsing for descriptor contents
 * @author paulm
 * @version $Revision: 1.5 $, $Date: 2006/01/05 13:25:59 $
 * @since 1.0
 */
public interface Browsable {
	Descriptor getDescriptor();

	/**
	 * @return the portion of stringText that can be directly modified on the
	 * catalog worksheet
	 *
	 * @since 1.0
	 */
  StringText getEditableSubfields();

	Integer getHeadingNumber();

	/**
	 * Return the set of subfields that are valid for editing on the catalog worksheet
	 *
	 * @since 1.0
	 */
  Set getValidEditableSubfields();

	void setDescriptor(Descriptor d);

	/**
		 * Extracts the "heading's" subfield codes and updates the stringText of the
		 * Descriptor
		 *
		 * @since 1.0
		 */
  void setDescriptorStringText(StringText tagStringText);

	void setHeadingNumber(Integer i);

	/**
	 * @return the AccessPoint's variant codes
	 */
  String getVariantCodes();

	String buildBrowseTerm();

}
