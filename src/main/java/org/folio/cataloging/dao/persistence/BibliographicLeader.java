package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.integration.GlobalStorage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class BibliographicLeader extends Leader {
  private static final long serialVersionUID = 3947160281428397002L;

  public BibliographicLeader() {
    super();
    setHeaderType(GlobalStorage.LEADER_HEADER_TYPE);
  }

  @Override
  public String getDisplayString() {
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

  public Element generateModelXmlElementContent(final Document xmlDocument) {
    Element content = null;
    if (xmlDocument != null) {
      content = xmlDocument.createElement("content");
      content.setAttribute("recordStatusCode", "" + getRecordStatusCode());
      content.setAttribute("itemRecordTypeCode", "" + getItemRecordTypeCode());
      content.setAttribute("itemBibliographicLevelCode", "" + getItemBibliographicLevelCode());
      content.setAttribute("CONTROL_TYPE_CODE", "" + getControlTypeCode());
      content.setAttribute("CHARACTER_CODING_SCHEME_CODE", "" + getCharacterCodingSchemeCode());
      content.setAttribute("ENCODING_LEVEL", "" + getEncodingLevel());
      content.setAttribute("DESCRIPTIVE_CATALOGUING_CODE", "" + getDescriptiveCataloguingCode());
      content.setAttribute("LINKED_RECORD_CODE", "" + getLinkedRecordCode());
    }
    return content;
  }

  public void parseModelXmlElementContent(final Element xmlElement) {
    Element content = (Element) xmlElement.getChildNodes().item(0);
    setRecordStatusCode(content.getAttribute("recordStatusCode").charAt(0));
    setItemRecordTypeCode(content.getAttribute("itemRecordTypeCode").charAt(0));
    setItemBibliographicLevelCode(content.getAttribute("itemBibliographicLevelCode").charAt(0));
    setControlTypeCode(content.getAttribute("CONTROL_TYPE_CODE").charAt(0));
    setCharacterCodingSchemeCode(content.getAttribute("CHARACTER_CODING_SCHEME_CODE").charAt(0));
    setEncodingLevel(content.getAttribute("ENCODING_LEVEL").charAt(0));
    setDescriptiveCataloguingCode(content.getAttribute("DESCRIPTIVE_CATALOGUING_CODE").charAt(0));
    setLinkedRecordCode(content.getAttribute("LINKED_RECORD_CODE").charAt(0));
  }

  private BIB_ITM getBibItm() {
    return (BIB_ITM) getItemEntity();
  }

  public char getItemRecordTypeCode() {
    return getBibItm().getItemRecordTypeCode();
  }

  public void setItemRecordTypeCode(char c) {
    getBibItm().setItemRecordTypeCode(c);
  }

  public char getItemBibliographicLevelCode() {
    return getBibItm().getItemBibliographicLevelCode();
  }

  public void setItemBibliographicLevelCode(char c) {
    getBibItm().setItemBibliographicLevelCode(c);
  }

  public char getCharacterCodingSchemeCode() {
    return getBibItm().getCharacterCodingSchemeCode();
  }

  public void setCharacterCodingSchemeCode(char c) {
    getBibItm().setCharacterCodingSchemeCode(c);
  }

  public char getControlTypeCode() {
    return getBibItm().getControlTypeCode();
  }

  public void setControlTypeCode(char c) {
    getBibItm().setControlTypeCode(c);
  }

  public char getDescriptiveCataloguingCode() {
    return getBibItm().getDescriptiveCataloguingCode();
  }

  public void setDescriptiveCataloguingCode(char c) {
    getBibItm().setDescriptiveCataloguingCode(c);
  }

  public char getEncodingLevel() {
    return getBibItm().getEncodingLevel();
  }

  public void setEncodingLevel(char c) {
    getBibItm().setEncodingLevel(c);
  }

  public char getLinkedRecordCode() {
    return getBibItm().getLinkedRecordCode();
  }

  public void setLinkedRecordCode(char c) {
    getBibItm().setLinkedRecordCode(c);
  }

  public char getRecordStatusCode() {
    return getBibItm().getRecordStatusCode();
  }

  public void setRecordStatusCode(char c) {
    getBibItm().setRecordStatusCode(c);
  }
}
