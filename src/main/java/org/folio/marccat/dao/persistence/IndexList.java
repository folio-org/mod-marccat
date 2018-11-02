/*
 * (c) LibriCore
 *
 * Created on Jul 20, 2004
 *
 * IndexList.java
 */
package org.folio.marccat.dao.persistence;

/**
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class IndexList {
  private IndexListKey key;
  private short characterUserInterfaceSequenceNumber;
  private short characterUserInterfaceSCNSequenceNumber;
  private int completenessAttribute;
  private int truncationAttribute;
  private int structureAttribute;
  private int positionAttribute;
  private int relationAttribute;
  private int useAttribute;
  private String languageCode;
  private short sortFormMainTypeCode;
  private short sortFormSubTypeCode;
  private short sortFormTypeCode;
  private short sortFormFunctionCode;
  private short sortFormSkipInFiling;
  private short browseTypeCode;
  private String nameTitleColumnYesNo;
  private String crossReferenceColumnYesNo;
  private String authorityColumnYesNo;
  private String browseCode;
  private String browseProcedureName;
  private String languageDescription;
  private String codeTableName;
  private String accessPointTableName;
  private String accessPointKeyName;
  /*modifica barbara 26/04/2007 per distinguere indici di mades da quelli di LC*/
  private String codeLibriCatMades;

  public String getCodeLibriCatMades() {
    return codeLibriCatMades;
  }


  public void setCodeLibriCatMades(String codeLibriCatMades) {
    this.codeLibriCatMades = codeLibriCatMades;
  }


  public String getAccessPointKeyName() {
    return accessPointKeyName;
  }

  public void setAccessPointKeyName(String string) {
    accessPointKeyName = string;
  }

  public String getAccessPointTableName() {
    return accessPointTableName;
  }

  public void setAccessPointTableName(String string) {
    accessPointTableName = string;
  }

  public String getBrowseCode() {
    return browseCode;
  }

  public void setBrowseCode(String string) {
    browseCode = string;
  }

  public String getBrowseProcedureName() {
    return browseProcedureName;
  }

  public void setBrowseProcedureName(String string) {
    browseProcedureName = string;
  }

  public short getBrowseTypeCode() {
    return browseTypeCode;
  }

  public void setBrowseTypeCode(short s) {
    browseTypeCode = s;
  }

  public short getCharacterUserInterfaceSCNSequenceNumber() {
    return characterUserInterfaceSCNSequenceNumber;
  }

  public void setCharacterUserInterfaceSCNSequenceNumber(short s) {
    characterUserInterfaceSCNSequenceNumber = s;
  }

  public short getCharacterUserInterfaceSequenceNumber() {
    return characterUserInterfaceSequenceNumber;
  }

  public void setCharacterUserInterfaceSequenceNumber(short s) {
    characterUserInterfaceSequenceNumber = s;
  }

  public String getCodeTableName() {
    return codeTableName;
  }

  public void setCodeTableName(String string) {
    codeTableName = string;
  }

  public int getCompletenessAttribute() {
    return completenessAttribute;
  }

  public void setCompletenessAttribute(int i) {
    completenessAttribute = i;
  }

  public IndexListKey getKey() {
    return key;
  }

  public void setKey(IndexListKey key) {
    this.key = key;
  }

  public String getLanguageCode() {
    return languageCode;
  }

  public void setLanguageCode(String string) {
    languageCode = string;
  }

  public String getLanguageDescription() {
    return languageDescription;
  }

  public void setLanguageDescription(String string) {
    languageDescription = string;
  }

  public int getPositionAttribute() {
    return positionAttribute;
  }

  public void setPositionAttribute(int i) {
    positionAttribute = i;
  }

  public int getRelationAttribute() {
    return relationAttribute;
  }

  public void setRelationAttribute(int i) {
    relationAttribute = i;
  }

  public short getSortFormFunctionCode() {
    return sortFormFunctionCode;
  }

  public void setSortFormFunctionCode(short s) {
    sortFormFunctionCode = s;
  }

  public short getSortFormMainTypeCode() {
    return sortFormMainTypeCode;
  }

  public void setSortFormMainTypeCode(short s) {
    sortFormMainTypeCode = s;
  }

  public short getSortFormSkipInFiling() {
    return sortFormSkipInFiling;
  }

  public void setSortFormSkipInFiling(short s) {
    sortFormSkipInFiling = s;
  }

  public short getSortFormSubTypeCode() {
    return sortFormSubTypeCode;
  }

  public void setSortFormSubTypeCode(short s) {
    sortFormSubTypeCode = s;
  }

  public short getSortFormTypeCode() {
    return sortFormTypeCode;
  }

  public void setSortFormTypeCode(short s) {
    sortFormTypeCode = s;
  }

  public int getStructureAttribute() {
    return structureAttribute;
  }

  public void setStructureAttribute(int i) {
    structureAttribute = i;
  }

  public int getTruncationAttribute() {
    return truncationAttribute;
  }

  public void setTruncationAttribute(int i) {
    truncationAttribute = i;
  }

  public int getUseAttribute() {
    return useAttribute;
  }

  public void setUseAttribute(int i) {
    useAttribute = i;
  }


  public String getAuthorityColumnYesNo() {
    return authorityColumnYesNo;
  }

  public void setAuthorityColumnYesNo(String string) {
    authorityColumnYesNo = string;
  }

  public String getCrossReferenceColumnYesNo() {
    return crossReferenceColumnYesNo;
  }

  public void setCrossReferenceColumnYesNo(String string) {
    crossReferenceColumnYesNo = string;
  }

  public String getNameTitleColumnYesNo() {
    return nameTitleColumnYesNo;
  }

  public void setNameTitleColumnYesNo(String string) {
    nameTitleColumnYesNo = string;
  }


}
