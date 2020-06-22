/*
 * (c) LibriCore
 *
 * Created on Dec 6, 2004
 *
 * S_CACHE_BIB_ITM_DSPLY.java
 */
package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class Cache implements Persistence {
  private int bibItemNumber;
  private short cataloguingView;
  private char formOfItemCode;
  private String languageOfCataloguingCode;
  private char itemRecordTypeCode;
  private boolean relationshipCode;
  private boolean locationCode;
  private String titleVolumeNumberDescription;
  private String date1;
  private String date2;
  private String titleHeadingMainSortForm;
  private String mainEntrySortForm;
  private String codeTableAbreviatedName;
  private String nameMainEntryStringText;
  private String titleHeadingUniformStringText;
  private String titleHeadingMainStringText;
  private String editionStrngTxt;
  private String publisherStrngTxt;
  private String collationStrngTxt;
  private String titleHeadingSeriesStringText;
  private PersistenceState persistenceState = new PersistenceState();


  public Cache() {
    super();

  }

  public Cache(int amicusNumber, short cataloguingView) {
    this();
    this.bibItemNumber = amicusNumber;
    this.cataloguingView = cataloguingView;
  }


  public int getBibItemNumber() {
    return bibItemNumber;
  }


  public void setBibItemNumber(int i) {
    bibItemNumber = i;
  }


  public short getCataloguingView() {
    return cataloguingView;
  }


  public void setCataloguingView(short s) {
    cataloguingView = s;
  }


  public PersistenceState getPersistenceState() {
    return persistenceState;
  }


  public void setPersistenceState(PersistenceState state) {
    persistenceState = state;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof Cache) {
      Cache s = (Cache) arg0;
      return getBibItemNumber() == s.getBibItemNumber() &&
        getCataloguingView() == s.getCataloguingView();
    }
    return false;
  }


  public AbstractDAO getDAO() {
    return persistenceState.getDAO();
  }


  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }


  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getBibItemNumber() + getCataloguingView();
  }


  public boolean isChanged() {
    return persistenceState.isChanged();
  }


  public boolean isDeleted() {
    return persistenceState.isDeleted();
  }


  public boolean isNew() {
    return persistenceState.isNew();
  }


  public boolean isRemoved() {
    return persistenceState.isRemoved();
  }


  public void markChanged() {
    persistenceState.markChanged();
  }


  public void markDeleted() {
    persistenceState.markDeleted();
  }


  public void markNew() {
    persistenceState.markNew();
  }


  public void markUnchanged() {
    persistenceState.markUnchanged();
  }


  public boolean onDelete(Session arg0) throws CallbackException {
    return persistenceState.onDelete(arg0);
  }


  public void onLoad(Session arg0, Serializable arg1) {
    persistenceState.onLoad(arg0, arg1);
  }


  public boolean onSave(Session arg0) throws CallbackException {
    return persistenceState.onSave(arg0);
  }


  public boolean onUpdate(Session arg0) throws CallbackException {
    return persistenceState.onUpdate(arg0);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#generateNewKey()
   */
  public void generateNewKey() {
    // not applicable for this class

  }


  public String getCodeTableAbreviatedName() {
    return codeTableAbreviatedName;
  }


  public void setCodeTableAbreviatedName(String string) {
    codeTableAbreviatedName = string;
  }


  public String getCollationStrngTxt() {
    return collationStrngTxt;
  }


  public void setCollationStrngTxt(String string) {
    collationStrngTxt = string;
  }


  public String getDate1() {
    return date1;
  }


  public void setDate1(String string) {
    date1 = string;
  }


  public String getDate2() {
    return date2;
  }


  public void setDate2(String string) {
    date2 = string;
  }


  public String getEditionStrngTxt() {
    return editionStrngTxt;
  }


  public void setEditionStrngTxt(String string) {
    editionStrngTxt = string;
  }


  public char getFormOfItemCode() {
    return formOfItemCode;
  }


  public void setFormOfItemCode(char c) {
    formOfItemCode = c;
  }


  public char getItemRecordTypeCode() {
    return itemRecordTypeCode;
  }


  public void setItemRecordTypeCode(char c) {
    itemRecordTypeCode = c;
  }


  public String getLanguageOfCataloguingCode() {
    return languageOfCataloguingCode;
  }


  public void setLanguageOfCataloguingCode(String string) {
    languageOfCataloguingCode = string;
  }


  public boolean isLocationCode() {
    return locationCode;
  }


  public void setLocationCode(boolean b) {
    locationCode = b;
  }


  public String getMainEntrySortForm() {
    return mainEntrySortForm;
  }


  public void setMainEntrySortForm(String string) {
    mainEntrySortForm = string;
  }


  public String getNameMainEntryStringText() {
    return nameMainEntryStringText;
  }


  public void setNameMainEntryStringText(String string) {
    nameMainEntryStringText = string;
  }


  public String getPublisherStrngTxt() {
    return publisherStrngTxt;
  }


  public void setPublisherStrngTxt(String string) {
    publisherStrngTxt = string;
  }


  public boolean isRelationshipCode() {
    return relationshipCode;
  }


  public void setRelationshipCode(boolean b) {
    relationshipCode = b;
  }


  public String getTitleHeadingMainSortForm() {
    return titleHeadingMainSortForm;
  }


  public void setTitleHeadingMainSortForm(String string) {
    titleHeadingMainSortForm = string;
  }


  public String getTitleHeadingMainStringText() {
    return titleHeadingMainStringText;
  }


  public void setTitleHeadingMainStringText(String string) {
    titleHeadingMainStringText = string;
  }


  public String getTitleHeadingSeriesStringText() {
    return titleHeadingSeriesStringText;
  }


  public void setTitleHeadingSeriesStringText(String string) {
    titleHeadingSeriesStringText = string;
  }


  public String getTitleHeadingUniformStringText() {
    return titleHeadingUniformStringText;
  }


  public void setTitleHeadingUniformStringText(String string) {
    titleHeadingUniformStringText = string;
  }


  public String getTitleVolumeNumberDescription() {
    return titleVolumeNumberDescription;
  }


  public void setTitleVolumeNumberDescription(String string) {
    titleVolumeNumberDescription = string;
  }

}
