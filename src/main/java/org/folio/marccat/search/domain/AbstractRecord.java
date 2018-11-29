package org.folio.marccat.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.exception.XmlDocumentException;
import org.folio.marccat.exception.XmlParserConfigurationException;
import org.folio.marccat.exception.XslTransformerConfigurationException;
import org.folio.marccat.exception.XslTransformerException;
import org.folio.marccat.util.XmlUtils;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This is an abstract class for database records. It implements the Record
 * interface.
 *
 * @author Wim Crols
 * @author cchiama
 * @since 1.0
 */
public abstract class AbstractRecord implements Record {

  private static final String XSLT_PATH = "/xslt/";
  private static final Log logger = new Log(AbstractRecord.class);
  @JsonIgnore
  private String cclQuery = "";
  private Map<String, Object> content = new HashMap<>();

  private int recordView;
  private int countDoc;
  private String queryForAssociatedDoc;
  private String tagHighlighted;

  @Override
  public boolean hasContent(final String elementSetName) {
    return content.containsKey(elementSetName);
  }

  @Override
  public void setContent(final String elementSetName, final Object data) {
    if (elementSetName != null && data != null) {
      content.put(elementSetName, data);
    }
  }

  @Override
  public Object getContent(final String elementSetName) {
    return content.get(elementSetName);
  }

  @Override
  public String toXmlString(String elementSetName) {
    if (this.hasContent(elementSetName)) {
      return XmlUtils.documentToString(toXmlDocument(elementSetName));
    } else {
      return null;
    }
  }

  @Override
  public Document toXmlStyledDocument(
    final String elementSetName,
    final String stylesheet,
    final Map xsltParameters) throws XmlParserConfigurationException {
    URL styleURL = AbstractRecord.class.getResource(XSLT_PATH + stylesheet);
    if (styleURL == null)
      throw new XmlParserConfigurationException("stylesheet " + stylesheet + " not found in " + XSLT_PATH);
    return toXmlStyledDocument(elementSetName, styleURL, xsltParameters);
  }

  @Override
  public Document toXmlStyledDocument(
    final String elementSetName,
    URL stylesheet,
    Map xsltParameters) throws XmlParserConfigurationException,
    XslTransformerConfigurationException, XslTransformerException {
    Document xmlStyledDocument = null;

    try {
      // load the transformer using JAXP
      TransformerFactory factory = TransformerFactory.newInstance();
      Transformer transformer = factory.newTransformer(new StreamSource(
        stylesheet.getFile()));

      // First set some parameters if there are any
      if (xsltParameters != null) {
        Iterator iterator = xsltParameters.entrySet().iterator();
        while (iterator.hasNext()) {
          Map.Entry entry = (Map.Entry) iterator.next();
          transformer.setParameter(entry.getKey().toString(), entry
            .getValue().toString());
        }
      }

      // now lets style the given document
      DOMSource source = new DOMSource(this
        .toXmlDocument(elementSetName));
      DocumentBuilderFactory documentBuilderFactory =
        DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = null;
      try {
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        xmlStyledDocument = documentBuilder.newDocument();
        DOMResult result = new DOMResult(xmlStyledDocument);
        transformer.transform(source, result);
      } catch (ParserConfigurationException parserConfigurationException) {
        logger.error("", parserConfigurationException);
        throw new XmlParserConfigurationException(parserConfigurationException);
      }
    } catch (TransformerConfigurationException transformerConfigurationException) {
      logger.error(transformerConfigurationException.getMessage());
      throw new XslTransformerConfigurationException(
        transformerConfigurationException);
    } catch (TransformerException transformerException) {
      logger.error(transformerException.getMessage());
      throw new XslTransformerException(transformerException);
    }

    return xmlStyledDocument;
  }

  public String toStyledDocument(String elementSetName, String stylesheet,
                                 Map xsltParameters) throws XmlDocumentException,
    XslTransformerConfigurationException, XslTransformerException {
    // MIKE: helpful in configuration time
    URL styleURL = AbstractRecord.class.getResource(XSLT_PATH + stylesheet);
    if (styleURL == null) throw new XmlDocumentException("stylesheet " + stylesheet + " not found in " + XSLT_PATH);
    return toStyledDocument(elementSetName, styleURL, xsltParameters);
  }

  public String toStyledDocument(String elementSetName, URL stylesheet,
                                 Map xsltParameters) throws XmlDocumentException,
    XslTransformerConfigurationException, XslTransformerException {
    String styledDocument = new String();
    try {
      // load the transformer using JAXP
      TransformerFactory factory = TransformerFactory.newInstance();
      Transformer transformer = factory.newTransformer(new StreamSource(
        stylesheet.openStream()));

      // First set some parameters if there are any
      if (xsltParameters != null) {
        Iterator iterator = xsltParameters.entrySet().iterator();
        while (iterator.hasNext()) {
          Map.Entry entry = (Map.Entry) iterator.next();
//CAMMILLETTI inizio
//					transformer.setParameter(entry.getKey().toString(), entry.getValue().toString());
          transformer.setParameter(entry.getKey().toString(), entry.getValue());
//CAMMILLETTI fine
        }
      }

      // now lets style the given document
      DOMSource source = new DOMSource(this
        .toXmlDocument(elementSetName));
      StringWriter buffer = new StringWriter();
      StreamResult result = new StreamResult(buffer);
      transformer.transform(source, result);

      // return the transformed document
      styledDocument = buffer.toString();
    } catch (TransformerConfigurationException transformerConfigurationException) {
      logger.error(transformerConfigurationException.getMessage());
      throw new XslTransformerConfigurationException(
        transformerConfigurationException);
    } catch (TransformerException transformerException) {
      logger.error(transformerException.getMessage());
      throw new XslTransformerException(transformerException);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return styledDocument;
  }

  public String getCclQuery() {
    return cclQuery;
  }

  public void setCclQuery(String cclQuery) {
    this.cclQuery = cclQuery;
  }

  /**
   * pm 2011
   *
   * @return the recordView
   */
  public int getRecordView() {
    return recordView;
  }

  /**
   * pm 2011
   *
   * @param recordView the recordView to set
   */
  public void setRecordView(int recordView) {
    this.recordView = recordView;
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

  public String getTagHighlighted() {
    return tagHighlighted;
  }

  public void setTagHighlighted(String tag) {
    this.tagHighlighted = tag;
  }


}
