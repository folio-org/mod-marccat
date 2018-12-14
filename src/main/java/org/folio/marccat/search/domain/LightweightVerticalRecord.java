package org.folio.marccat.search.domain;

import org.marc4j.MarcReader;
import org.marc4j.MarcXmlReader;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;

import static java.util.Optional.ofNullable;

/**
 * A simple XML record which retains the data in a lightweight string format.
 *
 * @author cchiama
 * @since 1.0
 */
public class LightweightVerticalRecord extends AbstractRecord {
  private static final String DUMMY_RECORD = "";
  private int countDoc;
  private String queryForAssociatedDoc;
  private String data;

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

  @Override
  public int getCountDoc() {
    return countDoc;
  }

  @Override
  public void setCountDoc(int countDoc) {
    this.countDoc = countDoc;
  }

  @Override
  public String getQueryForAssociatedDoc() {
    return queryForAssociatedDoc;
  }

  @Override
  public void setQueryForAssociatedDoc(String queryForAssociatedDoc) {
    this.queryForAssociatedDoc = queryForAssociatedDoc;
  }

}
