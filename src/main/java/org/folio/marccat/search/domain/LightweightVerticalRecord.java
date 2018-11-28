package org.folio.marccat.search.domain;

import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.MessageCatalog;
import org.marc4j.MarcReader;
import org.marc4j.MarcXmlReader;
import org.w3c.dom.Document;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

/**
 * A simple XML record which retains the data in a lightweight string format.
 *
 * @author cchiama
 * @since 1.0
 */
public class LightweightVerticalRecord extends AbstractRecord {
  private final static Log LOGGER = new Log(LightweightVerticalRecord.class);
  private final static String DUMMY_RECORD = "";
  private final static ThreadLocal<SAXParser> SAX_PARSERS =
    ThreadLocal.withInitial(() -> {
      try {
        return SAXParserFactory.newInstance().newSAXParser();
      } catch (final Exception exception) {
        throw new RuntimeException(exception);
      }
    });
  private int countDoc;
  private String queryForAssociatedDoc;
  private Predicate<String> isValidXml = data -> {
    try (final InputStream stream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8))) {
      SAX_PARSERS.get().parse(stream, new DefaultHandler());
      return true;
    } catch (final Exception exception) {
      LOGGER.error(MessageCatalog._00021_UNABLE_TO_PARSE_RECORD_DATA, data);
      return false;
    }
  };

  private String data;
  private String tagHighlighted;

  @Override
  public void setContent(final String elementSetName, final Object data) {
    this.data = ofNullable(data)
      .map(o -> {
        String record = o.toString();
        MarcReader reader = new MarcXmlReader(new ByteArrayInputStream(record.getBytes()));
        while (reader.hasNext()) {
          org.marc4j.marc.Record marcRecord = reader.next();
          record = marcRecord.toString();
        }
        return record;
      })
      .orElse(DUMMY_RECORD);
  }


  @Override
  public Document toXmlDocument(String elementSetName) {
    throw new IllegalArgumentException("Don't call me!");
  }

  /**
   * Returns the content of this record.
   * A dummy content is returned is noone inected some valid data.
   *
   * @return the content of this record.
   */
  public String getData() {
    return ofNullable(data).orElse(DUMMY_RECORD);
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
