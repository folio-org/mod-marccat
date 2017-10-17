package librisuite.business.cataloguing.bibliographic;

import librisuite.business.cataloguing.common.Leader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BibliographicLeader extends Leader 
{
	private static final long serialVersionUID = 3947160281428397002L;

	public BibliographicLeader() 
	{
		super();
		setHeaderType((short) 15);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.FixedField#getDisplayString()
	 */
	public String getDisplayString() 
	{
		String result = "00000";
		result = result
				+ getRecordStatusCode()
				+ getItemRecordTypeCode()
				+ getItemBibliographicLevelCode()
				+ getControlTypeCode()
				+ getCharacterCodingSchemeCode()
				+ "2200000"
				+ getEncodingLevel()
				+ getDescriptiveCataloguingCode()
				+ getLinkedRecordCode()
				+ "4500";
		return result;
	}

	public Element generateModelXmlElementContent(Document xmlDocument) 
	{
		Element content = null;
		if (xmlDocument != null) {
			content = xmlDocument.createElement("content");
			content.setAttribute("recordStatusCode", "" + getRecordStatusCode());
			content.setAttribute("itemRecordTypeCode", "" + getItemRecordTypeCode());
			content.setAttribute("itemBibliographicLevelCode", "" + getItemBibliographicLevelCode());
			content.setAttribute("controlTypeCode", "" + getControlTypeCode());
			content.setAttribute("characterCodingSchemeCode", "" + getCharacterCodingSchemeCode());
			content.setAttribute("encodingLevel", "" + getEncodingLevel());
			content.setAttribute("descriptiveCataloguingCode", "" + getDescriptiveCataloguingCode());
			content.setAttribute("linkedRecordCode", "" + getLinkedRecordCode());
		}
		return content;
	}

	public void parseModelXmlElementContent(Element xmlElement) 
	{
		Element content = (Element) xmlElement.getChildNodes().item(0);
		setRecordStatusCode(content.getAttribute("recordStatusCode").charAt(0));
		setItemRecordTypeCode(content.getAttribute("itemRecordTypeCode").charAt(0));
		setItemBibliographicLevelCode(content.getAttribute("itemBibliographicLevelCode").charAt(0));
		setControlTypeCode(content.getAttribute("controlTypeCode").charAt(0));
		setCharacterCodingSchemeCode(content.getAttribute("characterCodingSchemeCode").charAt(0));
		setEncodingLevel(content.getAttribute("encodingLevel").charAt(0));
		setDescriptiveCataloguingCode(content.getAttribute("descriptiveCataloguingCode").charAt(0));
		setLinkedRecordCode(content.getAttribute("linkedRecordCode").charAt(0));
	}

	private BIB_ITM getBibItm() {
		return (BIB_ITM)getItemEntity();
	}
	
	public char getItemRecordTypeCode() {
		return getBibItm().getItemRecordTypeCode();
	}
	
	public char getItemBibliographicLevelCode() {
		return getBibItm().getItemBibliographicLevelCode();
	}

	public char getCharacterCodingSchemeCode() {
		return getBibItm().getCharacterCodingSchemeCode();
	}

	public char getControlTypeCode() {
		return getBibItm().getControlTypeCode();
	}

	public char getDescriptiveCataloguingCode() {
		return getBibItm().getDescriptiveCataloguingCode();
	}

	public char getEncodingLevel() {
		return getBibItm().getEncodingLevel();
	}

	public char getLinkedRecordCode() {
		return getBibItm().getLinkedRecordCode();
	}

	public char getRecordStatusCode() {
		return getBibItm().getRecordStatusCode();
	}

	public void setCharacterCodingSchemeCode(char c) {
		getBibItm().setCharacterCodingSchemeCode(c);
	}

	public void setControlTypeCode(char c) {
		getBibItm().setControlTypeCode(c);
	}

	public void setDescriptiveCataloguingCode(char c) {
		getBibItm().setDescriptiveCataloguingCode(c);
	}

	public void setEncodingLevel(char c) {
		getBibItm().setEncodingLevel(c);
	}

	public void setLinkedRecordCode(char c) {
		getBibItm().setLinkedRecordCode(c);
	}

	public void setRecordStatusCode(char c) {
		getBibItm().setRecordStatusCode(c);
	}

	public void setItemBibliographicLevelCode(char c) {
		getBibItm().setItemBibliographicLevelCode(c);
	}

	public void setItemRecordTypeCode(char c) {
		getBibItm().setItemRecordTypeCode(c);
	}
}