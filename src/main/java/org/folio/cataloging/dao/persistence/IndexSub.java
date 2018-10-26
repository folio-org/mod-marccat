package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author carment
 * @since 1.0
 */
public class IndexSub implements Serializable {
  private int listKey;
  private int indexValueCode;
  private int indexSubValueCode;
  private String indexSubName;
  private String indexSearchCode;
  private String indexType;
  private String language;


  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public String getIndexSubName() {
    return indexSubName;
  }

  /**
   * @param string
   * @throws
   * @see
   * @since 1.0
   */
  public void setIndexSubName(String string) {
    indexSubName = string;
  }

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public int getIndexSubValueCode() {
    return indexSubValueCode;
  }

  /**
   * @param i
   * @throws
   * @see
   * @since 1.0
   */
  public void setIndexSubValueCode(int i) {
    indexSubValueCode = i;
  }

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public int getIndexValueCode() {
    return indexValueCode;
  }

  /**
   * @param i
   * @throws
   * @see
   * @since 1.0
   */
  public void setIndexValueCode(int i) {
    indexValueCode = i;
  }

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public String getIndexSearchCode() {
    return indexSearchCode;
  }

  /**
   * @param string
   * @throws
   * @see
   * @since 1.0
   */
  public void setIndexSearchCode(String string) {
    indexSearchCode = string;
  }

  public boolean equals(Object anObject) {
    IndexSub aKey = (IndexSub) anObject;
    if (this.getListKey() == aKey.getListKey()) {
      return true;
    }
    return false;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return this.getListKey();
  }


  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public int getListKey() {
    return listKey;
  }

  /**
   * @param i
   * @throws
   * @see
   * @since 1.0
   */
  public void setListKey(int i) {
    listKey = i;
  }

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public String getIndexType() {
    return indexType;
  }

  /**
   * @param string
   * @throws
   * @see
   * @since 1.0
   */
  public void setIndexType(String string) {
    indexType = string;
  }


  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public String getLanguage() {
    return language;
  }

  /**
   * @param string
   * @throws
   * @see
   * @since 1.0
   */
  public void setLanguage(String string) {
    language = string;
  }

}
