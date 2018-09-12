/*
 * (c) LibriCore
 * 
 * Created on Nov 21, 2005
 * 
 * Browsable.java
 */
package org.folio.cataloging.business.cataloguing.common;

import java.util.Set;

import org.folio.cataloging.dao.persistence.Descriptor;

import org.folio.cataloging.util.StringText;

/**
 * implemented by tags that support browsing for descriptor contents
 * @author paulm
 * @version $Revision: 1.5 $, $Date: 2006/01/05 13:25:59 $
 * @since 1.0
 */
public interface Browsable {
	public abstract Descriptor getDescriptor();

	/**
	 * @return the portion of stringText that can be directly modified on the 
	 * catalog worksheet
	 * 
	 * @since 1.0
	 */
	public abstract StringText getEditableSubfields();
	
	public abstract Integer getHeadingNumber();
	
	/**
	 * Return the set of subfields that are valid for editing on the catalog worksheet
	 * 
	 * @since 1.0
	 */
	public abstract Set getValidEditableSubfields();
	
	public abstract void setDescriptor(Descriptor d);
	
	/**
		 * Extracts the "heading's" subfield codes and updates the stringText of the 
		 * Descriptor
		 * 
		 * @since 1.0
		 */
	public abstract void setDescriptorStringText(StringText tagStringText);
	
	public abstract void setHeadingNumber(Integer i);
	
	/**
	 * @return the AccessPoint's variant codes
	 */
	public abstract String getVariantCodes();

	public abstract String buildBrowseTerm();

}
