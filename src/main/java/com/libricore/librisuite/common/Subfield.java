/*
 * (c) LibriCore
 * 
 * Created on Apr 13, 2004
 */
package com.libricore.librisuite.common;

import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Wim Crols
 * @version $Revision: 1.10 $, $Date: 2006/11/23 15:01:47 $
 * @since 1.0
 */
public class Subfield implements Serializable {

	private static final Log logger = LogFactory.getLog(Subfield.class);

	public static final String SUBFIELD_DELIMITER = "\u001f";
	public static final String FIELD_DELIMITER = "\u001e";
	public static final String RECORD_DELIMITER = "\u001d";

	private String code = "";
	private String content = "";

	public Subfield(Subfield s) {
		this.code = s.getCode();
		this.content = s.getContent();
	}

	public Subfield(String code, String content) {
		this.code = code;
		this.content = content;
	}

	public Subfield(String codeAndContent) {
		String subfield = stripOffDelimiter(codeAndContent);
		this.code = subfield.substring(0, 1);
		this.content = subfield.substring(1);
	}

	private static String stripOffDelimiter(String delimitedSubfield) {
		if (SUBFIELD_DELIMITER.equals(delimitedSubfield.substring(0, 1))) {
			return delimitedSubfield.substring(1);
		} else {
			return delimitedSubfield;
		}
	}

	/**
	 * This method creates a XML Docuemnt as follows
	 * <subfield code="a">content</subfield>
	 * 
	 * @return a Document
	 */
	public Document toXmlDocument() {
		DocumentBuilderFactory documentBuilderFactory =
			DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
		Document xmlDocument = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			xmlDocument = documentBuilder.newDocument();
			xmlDocument.appendChild(toXmlElement(xmlDocument));
		} catch (ParserConfigurationException parserConfigurationException) {
			logger.error("", parserConfigurationException);
			//throw new XmlParserConfigurationException(parserConfigurationException);
		}
		return xmlDocument;
	}

	/**
	 * This method creates a XML Element as follows
	 * <subfield code="a">content</subfield>
	 * 
	 * @return an Element
	 */
	public Element toXmlElement(Document xmlDocument) {
		Element subfield = xmlDocument.createElement("subfield");
		subfield.setAttribute("code", code);
		Node contentNode = xmlDocument.createTextNode(content);
		subfield.appendChild(contentNode);
		return subfield;
	}

	public boolean equals(Object anObject) {
		if (anObject instanceof Subfield) {
			Subfield subfield = (Subfield) anObject;
			return ((subfield.code.equals(this.code)) && (subfield.content
					.equals(this.content)));
		}
		return false;
	}

	public String toString() {
		return SUBFIELD_DELIMITER + this.code + this.content;
	}

	/**
	 * Getter for code
	 * 
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Getter for content
	 * 
	 * @return content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Setter for code
	 * 
	 * @param string
	 *            code
	 */
	public void setCode(String string) {
		code = string;
	}

	/**
	 * Setter for content
	 * 
	 * @param string
	 *            content
	 */
	public void setContent(String string) {
		content = string;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	protected Object clone() {
		return new Subfield(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return (3 * code.hashCode()) + (5 * content.hashCode()); 
	}
	
	/**
	 * included to provide length of content to jsp page
	 * @since 1.0
	 */
	public int getContentLength() {
		return this.content.length();
	}

	/**
	 * MIKE
	 * @return true if the subfield has no code or the content has length 0
	 */
	public boolean isEmpty(){
		return code.length()==0	|| getContentLength()==0;
	}
}