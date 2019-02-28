/*
 * (c) LibriCore
 *
 * Created on Nov 2, 2005
 *
 * AUT.java
 */
package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2006/01/05 13:25:59 $
 * @since 1.0
 */
public class AUT extends ItemEntity implements Persistence, Serializable {
  private char bilingualUsage = ' ';
  private char cataloguingRules = 'n';
  private char cataloguingSourceCode = 'u';
  private char conferenceOrMeeting = '2';
  private char governmentAgency = ' ';
  private String headingLanguage = "und"; // no longer used in authorities

  private int headingNumber = -1;
  private char headingStatus = 'n';
  private String headingType = "NH";
  private char mainAddedEntryIndicator = 'a';
  private char nonUniqueName = 'n';
  private char recordModification = ' ';
  private char recordRevision = 'a';
  private char recordType = 'a';
  private char referenceStatus = 'n';
  private char romanizationScheme = 'n';
  private char seriesEntryIndicator = 'b';
  private char seriesNumbering = 'n';
  private char seriesType = 'n';
  private char subDivisionType = 'n';
  private char subjectDescriptor = ' ';
  private char subjectEntryIndicator = 'b';
  private char subjectSystem = 'n';
  private String variableHeadingStringText;


  public AUT() {
    //setLanguageOfCataloguing (Defaults.getString ("authority.languageOfCataloguing"));
    //setCataloguingSourceStringText (Defaults.getString ("authority.cataloguingSourceStringText"));
    persistenceState = new PersistenceState();
  }


  public void evict() throws DataAccessException {
    persistenceState.evict(this);
  }


  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict(obj);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.PersistentObject#generateNewKey()
   */
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setAmicusNumber(new Integer(dao.getNextNumber("AA", session)));
  }



  public char getBilingualUsage() {
    return bilingualUsage;
  }


  public void setBilingualUsage(char c) {
    bilingualUsage = c;
  }


  public char getCataloguingRules() {
    return cataloguingRules;
  }


  public void setCataloguingRules(char c) {
    cataloguingRules = c;
  }


  public char getCataloguingSourceCode() {
    return cataloguingSourceCode;
  }


  public void setCataloguingSourceCode(char c) {
    cataloguingSourceCode = c;
  }


  public char getConferenceOrMeeting() {
    return conferenceOrMeeting;
  }


  public void setConferenceOrMeeting(char c) {
    conferenceOrMeeting = c;
  }


  public AbstractDAO getDAO() {
    return persistenceState.getDAO();
  }


  public char getEncodingLevel() {
    return encodingLevel;
  }


  public void setEncodingLevel(char c) {
    encodingLevel = c;
  }


  public char getGovernmentAgency() {
    return governmentAgency;
  }


  public void setGovernmentAgency(char c) {
    governmentAgency = c;
  }


  public String getHeadingLanguage() {
    return headingLanguage;
  }


  public void setHeadingLanguage(String string) {
    headingLanguage = string;
  }


  public int getHeadingNumber() {
    return headingNumber;
  }


  public void setHeadingNumber(int i) {
    headingNumber = i;
  }


  public char getHeadingStatus() {
    return headingStatus;
  }


  public void setHeadingStatus(char c) {
    headingStatus = c;
  }


  public String getHeadingType() {
    return headingType;
  }


  public void setHeadingType(String string) {
    headingType = string;
  }


  public char getMainAddedEntryIndicator() {
    return mainAddedEntryIndicator;
  }


  public void setMainAddedEntryIndicator(char c) {
    mainAddedEntryIndicator = c;
  }


  public char getNonUniqueName() {
    return nonUniqueName;
  }


  public void setNonUniqueName(char c) {
    nonUniqueName = c;
  }


  public char getRecordModification() {
    return recordModification;
  }


  public void setRecordModification(char c) {
    recordModification = c;
  }


  public char getRecordRevision() {
    return recordRevision;
  }


  public void setRecordRevision(char c) {
    recordRevision = c;
  }


  public char getRecordType() {
    return recordType;
  }


  public void setRecordType(char c) {
    recordType = c;
  }


  public char getReferenceStatus() {
    return referenceStatus;
  }


  public void setReferenceStatus(char c) {
    referenceStatus = c;
  }


  public char getRomanizationScheme() {
    return romanizationScheme;
  }


  public void setRomanizationScheme(char c) {
    romanizationScheme = c;
  }


  public char getSeriesEntryIndicator() {
    return seriesEntryIndicator;
  }


  public void setSeriesEntryIndicator(char c) {
    seriesEntryIndicator = c;
  }


  public char getSeriesNumbering() {
    return seriesNumbering;
  }


  public void setSeriesNumbering(char c) {
    seriesNumbering = c;
  }


  public char getSeriesType() {
    return seriesType;
  }


  public void setSeriesType(char c) {
    seriesType = c;
  }


  public char getSubDivisionType() {
    return subDivisionType;
  }


  public void setSubDivisionType(char c) {
    subDivisionType = c;
  }


  public char getSubjectDescriptor() {
    return subjectDescriptor;
  }


  public void setSubjectDescriptor(char c) {
    subjectDescriptor = c;
  }


  public char getSubjectEntryIndicator() {
    return subjectEntryIndicator;
  }


  public void setSubjectEntryIndicator(char c) {
    subjectEntryIndicator = c;
  }


  public char getSubjectSystem() {
    return subjectSystem;
  }


  public void setSubjectSystem(char c) {
    subjectSystem = c;
  }


  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }


  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
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


  public boolean onDelete(Session s) throws CallbackException {
    return persistenceState.onDelete(s);
  }


  public void onLoad(Session s, Serializable id) {
    persistenceState.onLoad(s, id);
  }


  public boolean onSave(Session s) throws CallbackException {
    return persistenceState.onSave(s);
  }


  public boolean onUpdate(Session s) throws CallbackException {
    return persistenceState.onUpdate(s);
  }

  public String getVariableHeadingStringText() {
    return variableHeadingStringText;
  }

  public void setVariableHeadingStringText(String variableHeadingStringText) {
    this.variableHeadingStringText = variableHeadingStringText;
  }
}
