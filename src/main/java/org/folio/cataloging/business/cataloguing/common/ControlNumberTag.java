/*
 * (c) LibriCore
 * 
 * Created on Oct 12, 2004
 * 
 * Leader.java
 */
package org.folio.cataloging.business.cataloguing.common;

import org.folio.cataloging.business.cataloguing.bibliographic.FixedFieldUsingItemEntity;
import org.folio.cataloging.shared.CorrelationValues;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.text.DecimalFormat;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public abstract class ControlNumberTag extends FixedFieldUsingItemEntity {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public ControlNumberTag() {
		super();
		/*
		 * implementers of this class should setHeaderType() in this constructor
		 */
	}

	/* (non-Javadoc)
	 * @see FixedField#getDisplayString()
	 */
	public String getDisplayString() {
		DecimalFormat df = new DecimalFormat("000000000000");
		String result = null;
		if (getItemEntity().getAmicusNumber() == null) {
			result = df.format(0);
		} else {
			result = df.format(getItemEntity().getAmicusNumber());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isAbleToBeDeleted()
	 */
	public boolean isAbleToBeDeleted() {
		return false;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isEditableHeader()
	 */
	public boolean isEditableHeader() {
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof ControlNumberTag) {
			return super.equals(obj); 
		}
		else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return super.hashCode();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
	 */
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return v.isValueDefined(1) && (v.getValue(1) != getHeaderType());
	}

	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
		if (xmlDocument != null) {
			content = xmlDocument.createElement("content");
			content.setAttribute("amicusNumber", "" + "000000000000");
		}
		return content;
	}

	public void parseModelXmlElementContent(Element xmlElement) {
		Element content = (Element) xmlElement.getChildNodes().item(0);
	}


}
