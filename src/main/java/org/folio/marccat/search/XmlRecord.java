package org.folio.marccat.search;


import org.folio.marccat.search.domain.AbstractRecord;
import org.w3c.dom.Document;


/**
 * A MARC XML record.
 *
 * @author Wim Crols
 * @author cchiama
 * @since 1.0
 */
public class XmlRecord extends AbstractRecord {


  private Document data;

  public Document getData() {
    return data;
  }

  public void setContent(final Document xmlDocument) {
    this.data = xmlDocument;
  }

  public Document toXmlDocument(final String elementSetName) {
    return (Document) getContent(elementSetName);
  }
}
