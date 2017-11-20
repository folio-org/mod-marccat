/*
 * (c) LibriCore
 * 
 * Created on Nov 24, 2005
 * 
 * AuthorityLeader.java
 */
package org.folio.cataloging.business.cataloguing.authority;

import org.folio.cataloging.business.cataloguing.common.Leader;
import org.folio.cataloging.dao.persistence.AUT;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class AuthorityLeader extends Leader {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public AuthorityLeader() {
		setHeaderField(new AuthorityHeaderFieldHelper());
		setHeaderType((short) 9);
	}

	/* (non-Javadoc)
	 * @see TagInterface#generateModelXmlElementContent(org.w3c.dom.Document)
	 */
	public Element generateModelXmlElementContent(Document xmlDocument) {
		Element content = null;
			if (xmlDocument != null) {
				content = xmlDocument.createElement("content");
				content.setAttribute("recordStatusCode", "" + getRecordStatusCode());
				content.setAttribute("encodingLevel", "" + getEncodingLevel());
			}
			return content;
		}

	private AUT getAutItm() {
		return (AUT)getItemEntity();
	}

	/* (non-Javadoc)
	 * @see FixedField#getDisplayString()
	 */
	public String getDisplayString() {
		String result = "00000";
		result =
			result
				+ getRecordStatusCode()
				+ "z   2200000"
				+ getEncodingLevel()
				+ "  4500";
		return result;
	}
	/**
	 * 
	 * @since 1.0
	 */
	public char getEncodingLevel() {
		return getAutItm().getEncodingLevel();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getRecordStatusCode() {
		return getAutItm().getRecordStatusCode();
	}

	/* (non-Javadoc)
	 * @see TagInterface#parseModelXmlElementContent(org.w3c.dom.Element)
	 */
	public void parseModelXmlElementContent(Element xmlElement) {
		Element content = (Element) xmlElement.getChildNodes().item(0);
		setRecordStatusCode(content.getAttribute("recordStatusCode").charAt(0));
		setEncodingLevel(content.getAttribute("encodingLevel").charAt(0));
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setEncodingLevel(char c) {
		getAutItm().setEncodingLevel(c);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setRecordStatusCode(char c) {
		getAutItm().setRecordStatusCode(c);
	}

}
