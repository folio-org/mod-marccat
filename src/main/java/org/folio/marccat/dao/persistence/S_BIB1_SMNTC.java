package org.folio.cataloging.dao.persistence;

/**
 * 2018 Paul Search Engine Java
 *
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2018/01/01 14:09:42 $
 * @since 1.0
 */
public class S_BIB1_SMNTC {
  private int useNumber;
  private int relationNumber;
  private int positionNumber;
  private int structureNumber;
  private int truncationNumber;
  private int completenessNumber;
  private short recordTypeCode;
  private short sortFormSkipInFilingCode;
  private short sortFormFunctionCode;
  private short sortFormTypeCode;
  private short sortFormSubTypeCode;
  private short sortFormMainTypeCode;
  private byte secondaryIndexCode;
  private String queryActionCode;
  private String selectClause;
  private String fromClause;
  private String joinClause;
  private String whereClause;
  private String viewClause;
  private boolean fullText;
  //unused? properties
  private boolean staticQueryWithoutUserView;
  private boolean staticQueryWithUserView;
  private int queryNumber;
  private int timsCollectionNumber;
  private String oracleHint;

  @Override
  public String toString() {
    return "S_BIB1_SMNTC(use=" + getUseNumber() + ", rel=" + getRelationNumber() + ", pos=" +
      getPositionNumber() + ", struc=" + getStructureNumber() + ", trunc=" + getTruncationNumber() +
      ", cmplt=" + getCompletenessNumber() + ")";
  }

  public int getUseNumber() {
    return useNumber;
  }

  public void setUseNumber(int useNumber) {
    this.useNumber = useNumber;
  }

  public int getRelationNumber() {
    return relationNumber;
  }

  public void setRelationNumber(int relationNumber) {
    this.relationNumber = relationNumber;
  }

  public int getPositionNumber() {
    return positionNumber;
  }

  public void setPositionNumber(int positionNumber) {
    this.positionNumber = positionNumber;
  }

  public int getStructureNumber() {
    return structureNumber;
  }

  public void setStructureNumber(int structureNumber) {
    this.structureNumber = structureNumber;
  }

  public int getTruncationNumber() {
    return truncationNumber;
  }

  public void setTruncationNumber(int truncationNumber) {
    this.truncationNumber = truncationNumber;
  }

  public int getCompletenessNumber() {
    return completenessNumber;
  }

  public void setCompletenessNumber(int completenessNumber) {
    this.completenessNumber = completenessNumber;
  }

  public short getRecordTypeCode() {
    return recordTypeCode;
  }

  public void setRecordTypeCode(short recordTypeCode) {
    this.recordTypeCode = recordTypeCode;
  }

  public short getSortFormSkipInFilingCode() {
    return sortFormSkipInFilingCode;
  }

  public void setSortFormSkipInFilingCode(short sortFormSkipInFilingCode) {
    this.sortFormSkipInFilingCode = sortFormSkipInFilingCode;
  }

  public short getSortFormFunctionCode() {
    return sortFormFunctionCode;
  }

  public void setSortFormFunctionCode(short sortFormFunctionCode) {
    this.sortFormFunctionCode = sortFormFunctionCode;
  }

  public short getSortFormTypeCode() {
    return sortFormTypeCode;
  }

  public void setSortFormTypeCode(short sortFormTypeCode) {
    this.sortFormTypeCode = sortFormTypeCode;
  }

  public short getSortFormSubTypeCode() {
    return sortFormSubTypeCode;
  }

  public void setSortFormSubTypeCode(short sortFormSubTypeCode) {
    this.sortFormSubTypeCode = sortFormSubTypeCode;
  }

  public short getSortFormMainTypeCode() {
    return sortFormMainTypeCode;
  }

  public void setSortFormMainTypeCode(short sortFormMainTypeCode) {
    this.sortFormMainTypeCode = sortFormMainTypeCode;
  }

  public byte getSecondaryIndexCode() {
    return secondaryIndexCode;
  }

  public void setSecondaryIndexCode(byte secondaryIndexCode) {
    this.secondaryIndexCode = secondaryIndexCode;
  }

  public String getQueryActionCode() {
    return queryActionCode;
  }

  public void setQueryActionCode(String queryActionCode) {
    this.queryActionCode = queryActionCode;
  }

  public String getSelectClause() {
    return selectClause;
  }

  public void setSelectClause(String selectClause) {
    this.selectClause = selectClause;
  }

  public String getFromClause() {
    return fromClause;
  }

  public void setFromClause(String fromClause) {
    this.fromClause = fromClause;
  }

  public String getJoinClause() {
    return joinClause;
  }

  public void setJoinClause(String joinClause) {
    this.joinClause = joinClause;
  }

  public String getWhereClause() {
    return whereClause;
  }

  public void setWhereClause(String whereClause) {
    this.whereClause = whereClause;
  }

  public String getViewClause() {
    return viewClause;
  }

  public void setViewClause(String viewClause) {
    this.viewClause = viewClause;
  }

  public boolean isFullText() {
    return fullText;
  }

  public void setFullText(boolean fullText) {
    this.fullText = fullText;
  }

  public boolean isStaticQueryWithoutUserView() {
    return staticQueryWithoutUserView;
  }

  public void setStaticQueryWithoutUserView(boolean staticQueryWithoutUserView) {
    this.staticQueryWithoutUserView = staticQueryWithoutUserView;
  }

  public boolean isStaticQueryWithUserView() {
    return staticQueryWithUserView;
  }

  public void setStaticQueryWithUserView(boolean staticQueryWithUserView) {
    this.staticQueryWithUserView = staticQueryWithUserView;
  }

  public int getQueryNumber() {
    return queryNumber;
  }

  public void setQueryNumber(int queryNumber) {
    this.queryNumber = queryNumber;
  }

  public int getTimsCollectionNumber() {
    return timsCollectionNumber;
  }

  public void setTimsCollectionNumber(int timsCollectionNumber) {
    this.timsCollectionNumber = timsCollectionNumber;
  }

  public String getOracleHint() {
    return oracleHint;
  }

  public void setOracleHint(String oracleHint) {
    this.oracleHint = oracleHint;
  }
}
