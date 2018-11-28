package org.folio.marccat.search.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.folio.marccat.config.log.Log;
import org.marc4j.MarcJsonWriter;
import org.marc4j.MarcReader;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlReader;
import org.w3c.dom.Document;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static java.util.Optional.ofNullable;

/**
 * A simple XML record which retains the data in a lightweight string format.
 *
 * @author cchiama
 * @since 1.0
 */
public class LightweightJsonRecord extends AbstractRecord {
  private final static JsonNode DUMMY_RECORD = null;
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
  private JsonNode data;
  /**
   * setContent, converting marcxml to jsonxml
   *
   * @param elementSetName
   * @param data
   */
  @Override
  public void setContent(final String elementSetName, final Object data) {
    String jsonString = ofNullable(data)
      .map(o -> {
        String record = o.toString();
        if (!record.equals("")) {
          MarcReader reader = new MarcXmlReader(new ByteArrayInputStream(record.getBytes()));
          OutputStream output = new ByteArrayOutputStream();
          MarcWriter writer = new MarcJsonWriter(output);
          while (reader.hasNext()) {
            org.marc4j.marc.Record marcRecord = reader.next();
            writer.write(marcRecord);
          }
          writer.close();
          return output.toString();
        } else
          return "";

      })
      .orElse("");
    try {
      this.data = new ObjectMapper().readTree(jsonString);
    } catch (Exception e) {
      this.data = DUMMY_RECORD;
    }
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
  public JsonNode getData() {
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
