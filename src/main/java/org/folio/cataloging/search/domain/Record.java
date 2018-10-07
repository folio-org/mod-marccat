package org.folio.cataloging.search.domain;

import org.folio.cataloging.search.XmlDocumentException;
import org.folio.cataloging.search.XmlParserConfigurationException;
import org.folio.cataloging.search.XslTransformerConfigurationException;
import org.folio.cataloging.search.XslTransformerException;
import org.w3c.dom.Document;

import java.net.URL;
import java.util.Map;

/**
 * This is an interface for database records.
 *
 * @author Wim Crols
 * @author agazzarini
 * @since 1.0
 */
public interface Record {
  String getCclQuery();

  void setCclQuery(String cclQuery);

  /**
   * Returns the record view associated with this record.
   *
   * @return the record view associated with this record.
   */
  int getRecordView();

  /**
   * Sets the record view associated with this record.
   *
   * @param recordView the record view associated with this record.
   */
  void setRecordView(int recordView);

  /**
   * Checks if this record contains data associated with the requested element set name.
   *
   * @param elementSetName the element set name.
   * @return if this record contains data associated with the requested element set name.
   */
  boolean hasContent(String elementSetName);

  /**
   * Associates the given data with an element set name.
   *
   * @param elementSetName the element set name.
   * @param data           the record data.
   */
  void setContent(String elementSetName, Object data);

  /**
   * Returns the data associated with the input element set name.
   *
   * @param elementSetName the element set name.
   * @return the data associated with the input element set name.
   */
  Object getContent(String elementSetName);

  /**
   * Returns an XML representation of the data associated with the given element set name.
   *
   * @param elementSetName the element set name.
   * @return an XML representation of the data associated with the given element set name.
   */
  String toXmlString(String elementSetName);

  /**
   * Returns the {@link Document} representation of the data associated with the given element set name.
   *
   * @param elementSetName the element set name.
   * @return an {@link Document} representation of the data associated with the given element set name.
   */
  Document toXmlDocument(String elementSetName);

  /**
   * Applies an XSLT transformation to this record.
   *
   * @param elementSetName the element set name.
   * @param stylesheet     the stylesjeet
   * @param xsltParameters the input parameters to the XSLT engine.
   * @return the transformed XML {@link Document}.
   * @throws XmlParserConfigurationException      in case of parser failure.
   * @throws XslTransformerConfigurationException in case of transformation configuration failure.
   * @throws XslTransformerException              in case of transformation failure.
   */
  Document toXmlStyledDocument(
    String elementSetName,
    String stylesheet,
    Map xsltParameters) throws XmlParserConfigurationException, XslTransformerConfigurationException, XslTransformerException;

  /**
   * Applies an XSLT transformation to this record.
   *
   * @param elementSetName the element set name.
   * @param stylesheet     the stylesjeet
   * @param xsltParameters the input parameters to the XSLT engine.
   * @return the transformed XML {@link Document}.
   * @throws XmlParserConfigurationException      in case of parser failure.
   * @throws XslTransformerConfigurationException in case of transformation configuration failure.
   * @throws XslTransformerException              in case of transformation failure.
   */
  Document toXmlStyledDocument(
    String elementSetName,
    URL stylesheet,
    Map xsltParameters) throws XmlParserConfigurationException, XslTransformerConfigurationException, XslTransformerException;

  String toStyledDocument(
    String elementSetName,
    String stylesheet,
    Map xsltParameters) throws XmlDocumentException, XslTransformerConfigurationException, XslTransformerException;

  /**
   * Applies an XSLT transformation to this record.
   *
   * @param elementSetName the element set name.
   * @param stylesheet     the stylesjeet
   * @param xsltParameters the input parameters to the XSLT engine.
   * @return the transformed XML {@link Document}.
   * @throws XmlParserConfigurationException      in case of parser failure.
   * @throws XslTransformerConfigurationException in case of transformation configuration failure.
   * @throws XslTransformerException              in case of transformation failure.
   */
  String toStyledDocument(
    String elementSetName,
    URL stylesheet,
    Map xsltParameters) throws XmlDocumentException, XslTransformerConfigurationException, XslTransformerException;
}

