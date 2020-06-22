
package org.folio.marccat.search.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.util.StringText;
import org.marc4j.MarcException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a class for MARC records.
 *
 * @author paulm
 */
public class MarcRecord extends AbstractRecord {

  /** The Constant logger. */
  private static final Log logger = LogFactory.getLog(MarcRecord.class);

  /** The xml content. */
  private Map xmlContent = new HashMap();

  /** The count doc. */
  private int countDoc;

  /** The query for associated doc. */
  private String queryForAssociatedDoc;

  /** The record id. */
  private int recordId;

  /**
   * Convert the binary MARC record to a MarcSlim
   *
   * @param elementSetName the element set name
   * @return the document
   */
  public Document toXmlDocument(String elementSetName) {
    Document xmlDocument = (Document) xmlContent.get(elementSetName);
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    String marcRecord;

    if (xmlDocument != null) {
      return xmlDocument;
    } else {
      marcRecord = (String) getContent(elementSetName);
      if (marcRecord == null) {
        return null;
      }
    }

    try {

      xmlDocument = documentBuilderFactory.newDocumentBuilder().newDocument();
    } catch (Exception e) {
      throw new MarcException("Unable to create new xml document");
    }
    Element record = xmlDocument.createElement("record");
    xmlDocument.appendChild(record);
    String[] tags = marcRecord.split("\u001E");
    if (tags.length <= 2) {
      return createErrorMessage(record, elementSetName, xmlDocument);
    }
    try {
      Element leader = xmlDocument.createElement("leader");
      leader.appendChild(
        xmlDocument.createTextNode(tags[0].substring(0, 24)));
      record.appendChild(leader);
      for (int i = 1; i < tags.length - 1; i++) {
        String tag = tags[0].substring(12 * (i + 1), 12 * (i + 1) + 3);
        if (tag.compareTo("010") < 0) {
          Element controlField =
            xmlDocument.createElement("controlfield");
          controlField.setAttribute("tag", tag);
          controlField.appendChild(xmlDocument.createTextNode(tags[i]));
          record.appendChild(controlField);
        } else {
          Element dataField = xmlDocument.createElement("datafield");
          dataField.setAttribute("tag", tag);
          dataField.setAttribute("ind1", tags[i].substring(0, 1));
          dataField.setAttribute("ind2", tags[i].substring(1, 2));
          new StringText(
            tags[i].substring(2)).generateMarcXmlElementContent(
            dataField, xmlDocument, this.getCclQuery());
          record.appendChild(dataField);
        }
      }
      xmlContent.put(elementSetName, xmlDocument);
    } catch (StringIndexOutOfBoundsException e) {
      logger.error("out of bounds", e);
      return createErrorMessage(record, elementSetName, xmlDocument);
    }
    return xmlDocument;
  }

  /**
   * Creates the error message.
   *
   * @param record the record
   * @param elementSetName the element set name
   * @param xmlDocument the xml document
   * @return the document
   */
  private Document createErrorMessage(Element record, String elementSetName,
                                      Document xmlDocument) {
    logger.error("record data [" + elementSetName + "]: " + getContent(elementSetName));
    Element error = xmlDocument.createElement("error");
    record.appendChild(error);
    xmlContent.put(elementSetName, xmlDocument);
    return xmlDocument;
  }

  /**
   * Gets the count doc.
   *
   * @return the count doc
   */
  @Override
  public int getCountDoc() {
    return countDoc;
  }

  /**
   * Sets the count doc.
   *
   * @param countDoc the new count doc
   */
  @Override
  public void setCountDoc(int countDoc) {
    this.countDoc = countDoc;
  }

  /**
   * Gets the query for associated doc.
   *
   * @return the query for associated doc
   */
  @Override
  public String getQueryForAssociatedDoc() {
    return queryForAssociatedDoc;
  }

  /**
   * Sets the query for associated doc.
   *
   * @param queryForAssociatedDoc the new query for associated doc
   */
  @Override
  public void setQueryForAssociatedDoc(String queryForAssociatedDoc) {
    this.queryForAssociatedDoc = queryForAssociatedDoc;
  }

  /**
   * Gets the record id.
   *
   * @return the record id
   */
  @Override
  public int getRecordId() {
    return recordId;
  }

  /**
   * Sets the record id.
   *
   * @param recordId the new record id
   */
  @Override
  public void setRecordId(int recordId) {
    this.recordId = recordId;
  }

}
