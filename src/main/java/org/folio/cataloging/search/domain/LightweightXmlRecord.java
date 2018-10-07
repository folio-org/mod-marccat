package org.folio.cataloging.search.domain;

import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.w3c.dom.Document;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

/**
 * A simple XML record which retains the data in a lightweight string format.
 *
 * @author agazzarini
 * @since 1.0
 */
public class LightweightXmlRecord extends AbstractRecord {
  private final static Log LOGGER = new Log (LightweightXmlRecord.class);
  private final static String DUMMY_RECORD = "<record></record>";

  private final static ThreadLocal <SAXParser> SAX_PARSERS =
    ThreadLocal.withInitial (() -> {
      try {
        return SAXParserFactory.newInstance ( ).newSAXParser ( );
      } catch (final Exception exception) {
        throw new RuntimeException (exception);
      }
    });

  private Predicate <String> isValidXml = data -> {
    try (final InputStream stream = new ByteArrayInputStream (data.getBytes ("UTF-8"))) {
      SAX_PARSERS.get ( ).parse (stream, new DefaultHandler ( ));
      return true;
    } catch (final Exception exception) {
      LOGGER.error (MessageCatalog._00021_UNABLE_TO_PARSE_RECORD_DATA, data);
      return false;
    }
  };

  private String data;

  @Override
  public void setContent(final String elementSetName, final Object data) {
    this.data = ofNullable (data)
      .map (Object::toString)
      .filter (isValidXml)
      .orElse (DUMMY_RECORD);
  }


  @Override
  public Document toXmlDocument(String elementSetName) {
    throw new IllegalArgumentException ("Don't call me!");
  }

  /**
   * Returns the content of this record.
   * A dummy content is returned is noone inected some valid data.
   *
   * @return the content of this record.
   */
  public String getData() {
    return ofNullable (data).orElse (DUMMY_RECORD);
  }
}
