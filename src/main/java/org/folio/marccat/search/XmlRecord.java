package org.folio.marccat.search;

import org.folio.marccat.exception.XmlParserConfigurationException;
import org.folio.marccat.exception.XmlUnsupportedEncodingException;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.search.domain.AbstractRecord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.util.Optional.ofNullable;

/**
 * A MARC XML record.
 *
 * @author Wim Crols
 * @author cchiama
 * @since 1.0
 */
public class XmlRecord extends AbstractRecord {

  private static final Log logger = new Log(XmlRecord.class);

  private final static ThreadLocal <DocumentBuilder> DOCUMENT_BUILDERS =
    ThreadLocal.withInitial(() -> {
      try {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder();
      } catch (final Exception exception) {
        throw new RuntimeException(exception);
      }
    });

  private Document data;

  public Document getData() {
    return data;
  }

  public void setContent(final Document xmlDocument) {
    this.data = xmlDocument;
  }

  public void setContent(final String elementSetName, String stringContent) throws XmlUnsupportedEncodingException, XmlParserConfigurationException {
    ofNullable(stringContent).ifPresent(xmlString -> {
      try (ByteArrayInputStream byteArrayInputStream =
             new ByteArrayInputStream(xmlString.getBytes("UTF-8"))) {
        DOCUMENT_BUILDERS.get().reset();
        this.data = DOCUMENT_BUILDERS.get().parse(byteArrayInputStream);
      } catch (SAXException | IOException exception) {
        logger.error(MessageCatalog._00021_UNABLE_TO_PARSE_RECORD_DATA, exception);
        final Document xmlDocument = DOCUMENT_BUILDERS.get().newDocument();

        DOCUMENT_BUILDERS.get().reset();

        final Element recordElement = xmlDocument.createElement("record");
        final Element errorElement = xmlDocument.createElement("error");

        final Node errorTextNode = xmlDocument.createTextNode(toXmlString(elementSetName));
        xmlDocument.appendChild(recordElement);

        recordElement.appendChild(errorElement);
        errorElement.appendChild(errorTextNode);
      }
    });
  }

  public Document toXmlDocument(final String elementSetName) {
    return (Document) getContent(elementSetName);
  }
}
