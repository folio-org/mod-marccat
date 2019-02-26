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


  public int getNameSubtype() {
    return nameSubtype;
  }


  public void setNameSubtype(int i) {
    nameSubtype = i;
  }


  public int getNameTitleOrSubjectType() {
    return nameTitleOrSubjectType;
  }


  public void setNameTitleOrSubjectType(int i) {
    nameTitleOrSubjectType = i;
  }


  public int getSkipInFiling() {
    return skipInFiling;
  }


  public void setSkipInFiling(int i) {
    skipInFiling = i;
  }


  public int getSortFormMainType() {
    return sortFormMainType;
  }


  public void setSortFormMainType(int i) {
    sortFormMainType = i;
  }


  public int getSortFormSubType() {
    return sortFormSubType;
  }


  public void setSortFormSubType(int i) {
    sortFormSubType = i;
  }

}
