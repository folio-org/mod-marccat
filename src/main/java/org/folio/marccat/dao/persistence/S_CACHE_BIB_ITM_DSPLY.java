/*
 * Created on November 11, 2004
 * */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;

/**
 * Hibernate class for table S_CACHE_BIB_ITM_DSPLY
 *
 * @author elena
 * @since 1.0
 */
public class S_CACHE_BIB_ITM_DSPLY implements Serializable {

  private CacheBibItemDisplay key;
  private char formOfItemFirstCode;
  private String languageOfCatalogingCode;
  private char itemRecordTypeCode;
  private int relatioshipCode;
  private int locationCode;
  private String titleVolNumberFirstDescription;
  private String itemDateFirstDescription;
  private String itemDateSecondDescription;
  private String titleHeadingMainSortForm;
  private String mainEntrySortForm;
  private String codeTableAbrevationName;
  private String nameMainEntryStringText;
  private String titleHeadingUniformStringText;
  private String titleHeadingMainStringText;
  private String bibNoteEditionStringText;
  private String bibNoteIprntStringText;
  private String bibNoteExtntStringText;
  private String titleHeadingSeriesStringText;


  /**
   * @return Returns the bibNoteEditionStringText.
   */
  public String getBibNoteEditionStringText() {
    return bibNoteEditionStringText;
  }

  /**
   * @param bibNoteEditionStringText The bibNoteEditionStringText to set.
   */
  public void setBibNoteEditionStringText(String bibNoteEditionStringText) {
    this.bibNoteEditionStringText = bibNoteEditionStringText;
  }

  /**
   * @return Returns the bibNoteExtntStringText.
   */
  public String getBibNoteExtntStringText() {
    return bibNoteExtntStringText;
  }

  /**
   * @param bibNoteExtntStringText The bibNoteExtntStringText to set.
   */
  public void setBibNoteExtntStringText(String bibNoteExtntStringText) {
    this.bibNoteExtntStringText = bibNoteExtntStringText;
  }

  /**
   * @return Returns the bibNoteIprntStringText.
   */
  public String getBibNoteIprntStringText() {
    return bibNoteIprntStringText;
  }

  /**
   * @param bibNoteIprntStringText The bibNoteIprntStringText to set.
   */
  public void setBibNoteIprntStringText(String bibNoteIprntStringText) {
    this.bibNoteIprntStringText = bibNoteIprntStringText;
  }

  /**
   * @return Returns the codeTableAbrevationName.
   */
  public String getCodeTableAbrevationName() {
    return codeTableAbrevationName;
  }

  /**
   * @param codeTableAbrevationName The codeTableAbrevationName to set.
   */
  public void setCodeTableAbrevationName(String codeTableAbrevationName) {
    this.codeTableAbrevationName = codeTableAbrevationName;
  }

  /**
   * @return Returns the formOfItemFirstCode.
   */
  public char getFormOfItemFirstCode() {
    return formOfItemFirstCode;
  }

  /**
   * @param formOfItemFirstCode The formOfItemFirstCode to set.
   */
  public void setFormOfItemFirstCode(char formOfItemFirstCode) {
    this.formOfItemFirstCode = formOfItemFirstCode;
  }

  /**
   * @return Returns the itemDateFirstDescription.
   */
  public String getItemDateFirstDescription() {
    return itemDateFirstDescription;
  }

  /**
   * @param itemDateFirstDescription The itemDateFirstDescription to set.
   */
  public void setItemDateFirstDescription(String itemDateFirstDescription) {
    this.itemDateFirstDescription = itemDateFirstDescription;
  }

  /**
   * @return Returns the itemDateSecondDescription.
   */
  public String getItemDateSecondDescription() {
    return itemDateSecondDescription;
  }

  /**
   * @param itemDateSecondDescription The itemDateSecondDescription to set.
   */
  public void setItemDateSecondDescription(String itemDateSecondDescription) {
    this.itemDateSecondDescription = itemDateSecondDescription;
  }

  /**
   * @return Returns the itemRecordTypeCode.
   */
  public char getItemRecordTypeCode() {
    return itemRecordTypeCode;
  }

  /**
   * @param itemRecordTypeCode The itemRecordTypeCode to set.
   */
  public void setItemRecordTypeCode(char itemRecordTypeCode) {
    this.itemRecordTypeCode = itemRecordTypeCode;
  }

  /**
   * @return Returns the key.
   */
  public CacheBibItemDisplay getKey() {
    return key;
  }

  /**
   * @param key The key to set.
   */
  public void setKey(CacheBibItemDisplay key) {
    this.key = key;
  }

  /**
   * @return Returns the languageOfCatalogingCode.
   */
  public String getLanguageOfCatalogingCode() {
    return languageOfCatalogingCode;
  }

  /**
   * @param languageOfCatalogingCode The languageOfCatalogingCode to set.
   */
  public void setLanguageOfCatalogingCode(String languageOfCatalogingCode) {
    this.languageOfCatalogingCode = languageOfCatalogingCode;
  }

  /**
   * @return Returns the locationCode.
   */
  public int getLocationCode() {
    return locationCode;
  }

  /**
   * @param locationCode The locationCode to set.
   */
  public void setLocationCode(int locationCode) {
    this.locationCode = locationCode;
  }

  /**
   * @return Returns the mainEntrySortForm.
   */
  public String getMainEntrySortForm() {
    return mainEntrySortForm;
  }

  /**
   * @param mainEntrySortForm The mainEntrySortForm to set.
   */
  public void setMainEntrySortForm(String mainEntrySortForm) {
    this.mainEntrySortForm = mainEntrySortForm;
  }

  /**
   * @return Returns the nameMainEntryStringText.
   */
  public String getNameMainEntryStringText() {
    return nameMainEntryStringText;
  }

  /**
   * @param nameMainEntryStringText The nameMainEntryStringText to set.
   */
  public void setNameMainEntryStringText(String nameMainEntryStringText) {
    this.nameMainEntryStringText = nameMainEntryStringText;
  }

  /**
   * @return Returns the relatioshipCode.
   */
  public int getRelatioshipCode() {
    return relatioshipCode;
  }

  /**
   * @param relatioshipCode The relatioshipCode to set.
   */
  public void setRelatioshipCode(int relatioshipCode) {
    this.relatioshipCode = relatioshipCode;
  }

  /**
   * @return Returns the titleHeadingMainSortForm.
   */
  public String getTitleHeadingMainSortForm() {
    return titleHeadingMainSortForm;
  }

  /**
   * @param titleHeadingMainSortForm The titleHeadingMainSortForm to set.
   */
  public void setTitleHeadingMainSortForm(String titleHeadingMainSortForm) {
    this.titleHeadingMainSortForm = titleHeadingMainSortForm;
  }

  /**
   * @return Returns the titleHeadingMainStringText.
   */
  public String getTitleHeadingMainStringText() {
    return titleHeadingMainStringText;
  }

  /**
   * @param titleHeadingMainStringText The titleHeadingMainStringText to set.
   */
  public void setTitleHeadingMainStringText(String titleHeadingMainStringText) {
    this.titleHeadingMainStringText = titleHeadingMainStringText;
  }

  /**
   * @return Returns the titleHeadingSeriesStringText.
   */
  public String getTitleHeadingSeriesStringText() {
    return titleHeadingSeriesStringText;
  }

  /**
   * @param titleHeadingSeriesStringText The titleHeadingSeriesStringText to set.
   */
  public void setTitleHeadingSeriesStringText(
    String titleHeadingSeriesStringText) {
    this.titleHeadingSeriesStringText = titleHeadingSeriesStringText;
  }

  /**
   * @return Returns the titleHeadingUniformStringText.
   */
  public String getTitleHeadingUniformStringText() {
    return titleHeadingUniformStringText;
  }

  /**
   * @param titleHeadingUniformStringText The titleHeadingUniformStringText to set.
   */
  public void setTitleHeadingUniformStringText(
    String titleHeadingUniformStringText) {
    this.titleHeadingUniformStringText = titleHeadingUniformStringText;
  }

  /**
   * @return Returns the titleVolNumberFirstDescription.
   */
  public String getTitleVolNumberFirstDescription() {
    return titleVolNumberFirstDescription;
  }

  /**
   * @param titleVolNumberFirstDescription The titleVolNumberFirstDescription to set.
   */
  public void setTitleVolNumberFirstDescription(
    String titleVolNumberFirstDescription) {
    this.titleVolNumberFirstDescription = titleVolNumberFirstDescription;
  }
}
