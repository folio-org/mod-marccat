package org.folio.marccat.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.util.XmlUtils;
import java.util.HashMap;
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

  /** The log. */
  private final Log log = new Log(AbstractRecord.class);

  /** The ccl query. */
  @JsonIgnore
  private String cclQuery = "";

  /** The content. */
  private Map<String, Object> content = new HashMap<>();

  /** The record view. */
  private int recordView;

  /** The count doc. */
  private int countDoc;

  /** The query for associated doc. */
  private String queryForAssociatedDoc;

  /** The tag highlighted. */
  private String tagHighlighted;

  /** The record id. */
  private int recordId;

  /**
   * Checks for content.
   *
   * @param elementSetName the element set name
   * @return true, if successful
   */
  @Override
  public boolean hasContent(final String elementSetName) {
    return content.containsKey(elementSetName);
  }

  /**
   * Sets the content.
   *
   * @param elementSetName the element set name
   * @param data the data
   */
  @Override
  public void setContent(final String elementSetName, final Object data) {
    if (elementSetName != null && data != null) {
      content.put(elementSetName, data);
    }
  }

  /**
   * Gets the content.
   *
   * @param elementSetName the element set name
   * @return the content
   */
  @Override
  public Object getContent(final String elementSetName) {
    return content.get(elementSetName);
  }

  /**
   * To xml string.
   *
   * @param elementSetName the element set name
   * @return the string
   */
  @Override
  public String toXmlString(String elementSetName) {
    if (this.hasContent(elementSetName)) {
      return XmlUtils.documentToString(toXmlDocument(elementSetName));
    } else {
      return null;
    }
  }



  /**
   * Gets the ccl query.
   *
   * @return the ccl query
   */
  public String getCclQuery() {
    return cclQuery;
  }

  /**
   * Sets the ccl query.
   *
   * @param cclQuery the new ccl query
   */
  public void setCclQuery(String cclQuery) {
    this.cclQuery = cclQuery;
  }

  /**
   * Gets the record view.
   *
   * @return the recordView
   */
  public int getRecordView() {
    return recordView;
  }

  /**
   * Sets the record view.
   *
   * @param recordView the recordView to set
   */
  public void setRecordView(int recordView) {
    this.recordView = recordView;
  }

  /**
   * Gets the count doc.
   *
   * @return the count doc
   */
  public int getCountDoc() {
    return countDoc;
  }

  /**
   * Sets the count doc.
   *
   * @param countDoc the new count doc
   */
  public void setCountDoc(int countDoc) {
    this.countDoc = countDoc;
  }

  /**
   * Gets the query for associated doc.
   *
   * @return the query for associated doc
   */
  public String getQueryForAssociatedDoc() {
    return queryForAssociatedDoc;
  }

  /**
   * Sets the query for associated doc.
   *
   * @param queryForAssociatedDoc the new query for associated doc
   */
  public void setQueryForAssociatedDoc(String queryForAssociatedDoc) {
    this.queryForAssociatedDoc = queryForAssociatedDoc;
  }

  /**
   * Gets the tag highlighted.
   *
   * @return the tag highlighted
   */
  public String getTagHighlighted() {
    return tagHighlighted;
  }

  /**
   * Sets the tag highlighted.
   *
   * @param tag the new tag highlighted
   */
  public void setTagHighlighted(String tag) {
    this.tagHighlighted = tag;
  }

  /**
   * Gets the record id.
   *
   * @return the record id
   */
  public int getRecordId() {
    return recordId;
  }

  /**
   * Sets the record id.
   *
   * @param recordId the new record id
   */
  public void setRecordId(int recordId) {
    this.recordId = recordId;
  }


}
