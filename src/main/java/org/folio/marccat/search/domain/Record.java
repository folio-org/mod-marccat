package org.folio.marccat.search.domain;

import org.w3c.dom.Document;


/**
 * This is an interface for database records.
 *
 * @author Wim Crols
 * @author cchiama
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

  void setCountDoc(int countDoc);

  void setQueryForAssociatedDoc(String queryForAssociatedDoc);

  void setTagHighlighted(String tags);

  void setRecordId(int recordId);

}

