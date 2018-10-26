package org.folio.cataloging.business.cataloguing.authority;

import org.folio.cataloging.dao.persistence.AUT;
import org.folio.cataloging.dao.persistence.Leader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author paulm
 * @since 1.0
 */
public class AuthorityLeader extends Leader {

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public AuthorityLeader() {
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType(9);
  }

  @Override
  public Element generateModelXmlElementContent(Document xmlDocument) {
    Element content = null;
    if (xmlDocument != null) {
      content = xmlDocument.createElement("content");
      content.setAttribute("recordStatusCode", "" + getRecordStatusCode());
      content.setAttribute("ENCODING_LEVEL", "" + getEncodingLevel());
    }
    return content;
  }

  private AUT getAutItm() {
    return (AUT) getItemEntity();
  }

  @Override
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

  public char getEncodingLevel() {
    return getAutItm().getEncodingLevel();
  }

  public void setEncodingLevel(char c) {
    getAutItm().setEncodingLevel(c);
  }

  public char getRecordStatusCode() {
    return getAutItm().getRecordStatusCode();
  }

  public void setRecordStatusCode(char c) {
    getAutItm().setRecordStatusCode(c);
  }

  @Override
  public void parseModelXmlElementContent(Element xmlElement) {
    final Element content = (Element) xmlElement.getChildNodes().item(0);
    setRecordStatusCode(content.getAttribute("recordStatusCode").charAt(0));
    setEncodingLevel(content.getAttribute("ENCODING_LEVEL").charAt(0));
  }
}
