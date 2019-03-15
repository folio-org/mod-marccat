package org.folio.marccat.business.cataloguing.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.bibliographic.FixedFieldUsingItemEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;


public abstract class DateOfLastTransactionTag extends FixedFieldUsingItemEntity {

  private transient Log logger = LogFactory.getLog(DateOfLastTransactionTag.class);
  private String formatDat = "yyyyMMddHHmmss.S";

public DateOfLastTransactionTag() {
    super();
  }

  /* (non-Javadoc)
   * @see FixedField#getDisplayString()
   */
  public String getDisplayString() {
    SimpleDateFormat df = new SimpleDateFormat(formatDat);
    return df.format(getItemEntity().getDateOfLastTransaction());
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#isEditableHeader()
   */
  @Override
  public boolean isEditableHeader() {
    return false;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#isAbleToBeDeleted()
   */
  @Override
  public boolean isAbleToBeDeleted() {
    return false;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
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
  @Override
  public int hashCode() {
    return super.hashCode();
  }

  public Element generateModelXmlElementContent(Document xmlDocument) {
    Element content = null;
    if (xmlDocument != null) {
      content = xmlDocument.createElement("content");
      SimpleDateFormat df = new SimpleDateFormat(formatDat);
      String result = df.format(getItemEntity().getDateOfLastTransaction());
      Text text = xmlDocument.createTextNode(result);
      content.appendChild(text);
    }
    return content;
  }

  public void parseModelXmlElementContent(Element xmlElement) {
    Element content = (Element) xmlElement.getChildNodes().item(0);
    SimpleDateFormat df = new SimpleDateFormat(formatDat);
    try {
      getItemEntity().setDateOfLastTransaction(df.parse(((Text) content.getFirstChild()).getData()));
    } catch (ParseException parseException) {
    	logger.error(parseException.getMessage());
    }
  }


  @Override
  public void setContentFromMarcString(String s) {
    SimpleDateFormat df = new SimpleDateFormat(formatDat);
    try {
      getItemEntity().setDateOfLastTransaction(df.parse(s));
    } catch (ParseException e) {
      // date not set if parse error
    }
  }
}
