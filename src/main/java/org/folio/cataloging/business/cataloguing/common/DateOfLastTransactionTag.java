/*
 * (c) LibriCore
 * 
 * Created on Oct 12, 2004
 * 
 * DateOfLastTransactionTag.java
 */
package org.folio.cataloging.business.cataloguing.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.folio.cataloging.business.cataloguing.bibliographic.FixedFieldUsingItemEntity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public abstract class DateOfLastTransactionTag extends FixedFieldUsingItemEntity {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public DateOfLastTransactionTag() {
		super();
	}

	/* (non-Javadoc)
	 * @see FixedField#getDisplayString()
	 */
	public String getDisplayString() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss.S");
		String result = df.format(getItemEntity().getDateOfLastTransaction());
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isEditableHeader()
	 */
	public boolean isEditableHeader() {
		return false;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isAbleToBeDeleted()
	 */
	public boolean isAbleToBeDeleted() {
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof DateOfLastTransactionTag) {
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

	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
		if (xmlDocument != null) {
			content = xmlDocument.createElement("content");
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss.S");
			String result = df.format(getItemEntity().getDateOfLastTransaction());
			Text text = xmlDocument.createTextNode(result);
			content.appendChild(text);
		}
		return content;
	}

	public void parseModelXmlElementContent(Element xmlElement) {
		Element content = (Element) xmlElement.getChildNodes().item(0);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss.S");
//		if ((content.getFirstChild() != null) && (((Text)content.getFirstChild()).getData() != null)) {
			try {
				getItemEntity().setDateOfLastTransaction(df.parse(((Text)content.getFirstChild()).getData()));
			} catch (ParseException parseException) {
			}
//		}
	}

}
