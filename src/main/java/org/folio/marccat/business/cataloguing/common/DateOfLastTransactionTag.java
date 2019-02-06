package org.folio.marccat.business.cataloguing.common;

import org.folio.marccat.business.cataloguing.bibliographic.FixedFieldUsingItemEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public abstract class DateOfLastTransactionTag extends FixedFieldUsingItemEntity {


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
    } else {
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
    try {
      getItemEntity().setDateOfLastTransaction(df.parse(((Text) content.getFirstChild()).getData()));
    } catch (ParseException parseException) {
    }
  }


  @Override
  public void setContentFromMarcString(String s) {
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss.S");
    try {
      getItemEntity().setDateOfLastTransaction(df.parse(s));
    } catch (ParseException e) {
      // date not set if parse error
    }
  }
}
