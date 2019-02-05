/*
 * (c) LibriCore
 *
 * Created on Nov 30, 2004
 *
 * SortformParameters.java
 */
package org.folio.marccat.business.descriptor;


public class SortFormParameters {

  private int sortFormMainType;
  private int sortFormSubType;
  private int nameTitleOrSubjectType;
  private int nameSubtype;
  private int skipInFiling;


  public SortFormParameters() {
    super();
  }


  public SortFormParameters(
    int sortFormMainType,
    int sortFormSubType,
    int nameTitleOrSubjectType,
    int nameSubType,
    int skipInFiling) {
    setSortFormMainType(sortFormMainType);
    setSortFormSubType(sortFormSubType);
    setNameTitleOrSubjectType(nameTitleOrSubjectType);
    setNameSubtype(nameSubType);
    setSkipInFiling(skipInFiling);
  }

  /**
   * @since 1.0
   */
  public int getNameSubtype() {
    return nameSubtype;
  }

  /**
   * @since 1.0
   */
  public void setNameSubtype(int i) {
    nameSubtype = i;
  }

  /**
   * @since 1.0
   */
  public int getNameTitleOrSubjectType() {
    return nameTitleOrSubjectType;
  }

  /**
   * @since 1.0
   */
  public void setNameTitleOrSubjectType(int i) {
    nameTitleOrSubjectType = i;
  }

  /**
   * @since 1.0
   */
  public int getSkipInFiling() {
    return skipInFiling;
  }

  /**
   * @since 1.0
   */
  public void setSkipInFiling(int i) {
    skipInFiling = i;
  }

  /**
   * @since 1.0
   */
  public int getSortFormMainType() {
    return sortFormMainType;
  }

  /**
   * @since 1.0
   */
  public void setSortFormMainType(int i) {
    sortFormMainType = i;
  }

  /**
   * @since 1.0
   */
  public int getSortFormSubType() {
    return sortFormSubType;
  }

  /**
   * @since 1.0
   */
  public void setSortFormSubType(int i) {
    sortFormSubType = i;
  }

}
