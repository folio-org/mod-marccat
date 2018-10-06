package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Carment
 * @since 1.0
 */
public class IndexMain implements Serializable {
  private int indexKey;
  private int indexValueCode;
  private String indexMainName;
  private String indexType;
  private String language;


  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public String getIndexMainName() {
    return indexMainName;
  }

  /**
   * @param string
   * @throws
   * @see
   * @since 1.0
   */
  public void setIndexMainName(String string) {
    indexMainName = string;
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

  public boolean equals(Object anObject) {
    IndexMain aKey = (IndexMain) anObject;
    if (this.getIndexKey ( ) == aKey.getIndexKey ( )) {
      return true;
    }
    return false;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return this.getIndexKey ( );
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

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public int getIndexKey() {
    return indexKey;
  }

  /**
   * @param i
   * @throws
   * @see
   * @since 1.0
   */
  public void setIndexKey(int i) {
    indexKey = i;
  }

}
