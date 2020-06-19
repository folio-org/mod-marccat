package org.folio.marccat.search;

import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.exception.XmlParserConfigurationException;
import org.folio.marccat.exception.XmlUnsupportedEncodingException;
import org.folio.marccat.search.domain.AbstractRecord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.util.Optional.ofNullable;

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
