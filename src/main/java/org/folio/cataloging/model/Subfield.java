package org.folio.cataloging.model;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * MARC Subfield definition.
 *
 * @author Wim Crols
 * @author agazzarini
 * @since 1.0
 */
public class Subfield implements Serializable {

	private static final Log logger = LogFactory.getLog(Subfield.class);

	public static final String SUBFIELD_DELIMITER = "\u001f";
	public static final String FIELD_DELIMITER = "\u001e";
	public static final String RECORD_DELIMITER = "\u001d";

	private String code;
	private String content;

    /**
     * Builds a new {@link Subfield} with the given {@link Subfield} data.
     *
     * @param s the source subfield.
     */
	public Subfield(final Subfield s) {
		this.code = s.getCode();
		this.content = s.getContent();
	}

    /**
     * Builds a new {@link Subfield} with the given data.
     *
     * @param code the subfield code.
     * @param content the subfield content.
     */
	public Subfield(final String code, final String content) {
		this.code = code;
		this.content = content;
	}

    /**
     * Builds a new {@link Subfield} with the given data.
     *
     * @param codeAndContent the subfield data.
     */
	public Subfield(String codeAndContent) {
		String subfield = stripOffDelimiter(codeAndContent);
		this.code = subfield.substring(0, 1);
		this.content = subfield.substring(1);
	}

    /**
     * Strips off the delimiters from the given subfield data.
     *
     * @param delimitedSubfield the subfield data.
     * @return the subfield data without delimiters.
     */
	private static String stripOffDelimiter(final String delimitedSubfield) {
		return (SUBFIELD_DELIMITER.equals(delimitedSubfield.substring(0, 1)))
                ? delimitedSubfield.substring(1)
                : delimitedSubfield;
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

	@Override
	public boolean equals(Object anObject) {
		if (anObject instanceof Subfield) {
			Subfield subfield = (Subfield) anObject;
			return ((subfield.code.equals(this.code)) && (subfield.content
					.equals(this.content)));
		}
		return false;
	}

    @Override
	public String toString() {
		return SUBFIELD_DELIMITER + this.code + this.content;
	}

	/**
	 * Getter for code.
	 * 
	 * @return the subfield code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Getter for subfield content.
	 * 
	 * @return the subfield content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Setter for code.
	 * 
	 * @param code the subfield code.
	 */
	public void setCode(final String code) {
		this.code = code;
	}

	/**
	 * Setter for content.
	 * 
	 * @param content the subfield content.
	 */
	public void setContent(final String content) {
		this.content = content;
	}

	@Override
	public Object clone() {
		return new Subfield(this);
	}

	@Override
	public int hashCode() {
		return (3 * code.hashCode()) + (5 * content.hashCode()); 
	}
	
	/**
	 * Included to provide length of content to jsp page
	 */
	public int getContentLength() {
		return this.content.length();
	}

	/**
	 * Returns true if the subfield has no code or the content has length 0.
     *
	 * @return true if the subfield has no code or the content has length 0
	 */
	public boolean isEmpty(){
		return code.length()==0	|| getContentLength()==0;
	}
}