/*
 * (c) LibriCore
 *
 * Created on Jun 7, 2004
 *
 * MarcRecord.java
 */
package org.folio.cataloging.search.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.util.StringText;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a class for MARC records.
 *
 * @author Wim Crols
 * @version $Revision: 1.7 $, $Date: 2006/07/26 14:31:50 $
 * @since 1.0
 */
public class MarcRecord extends AbstractRecord {

  private static final Log logger = LogFactory.getLog(MarcRecord.class);

  private Map xmlContent = new HashMap();
  private int countDoc;
  private String queryForAssociatedDoc;

  public Document toXmlDocument(String elementSetName) {
    Document xmlDocument = (Document) xmlContent.get(elementSetName);
    String marcRecord;

    if (xmlDocument != null) {
      return xmlDocument;
    } else {
      marcRecord = (String) getContent(elementSetName);
      if (marcRecord == null) {
        return null;
      }
    }

    /* convert the MARC record to a MarcSlim xmldocument */
    try {
      xmlDocument =
        DocumentBuilderFactory
          .newInstance()
          .newDocumentBuilder()
          .newDocument();
    } catch (Exception e) {
      throw new RuntimeException("Unable to create new xml document");
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

  private Document createErrorMessage(Element record, String elementSetName,
                                      Document xmlDocument) {
    // Marc record is in error
    logger.error("record data [" + elementSetName + "]: " + getContent(elementSetName));
    Element error = xmlDocument.createElement("error");
    record.appendChild(error);
    xmlContent.put(elementSetName, xmlDocument);
    return xmlDocument;
  }

  public int getCountDoc() {
    return countDoc;
  }

  public void setCountDoc(int countDoc) {
    this.countDoc = countDoc;
  }

  public String getQueryForAssociatedDoc() {
    return queryForAssociatedDoc;
  }

  public void setQueryForAssociatedDoc(String queryForAssociatedDoc) {
    this.queryForAssociatedDoc = queryForAssociatedDoc;
  }

}
