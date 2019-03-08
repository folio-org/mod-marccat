/*
 * (c) LibriCore
 *
 * Created on Nov 2, 2005
 *
 * AUT.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;

import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.SystemNextNumberDAO;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

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
    persistenceState = new PersistenceState();
  }

  /**
   * @since 1.0
   */
  public void evict() {
    persistenceState.evict(this);
  }

  /**
   * @since 1.0
   */
  public void evict(Object obj) {
    persistenceState.evict(obj);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.PersistentObject#generateNewKey()
   */
  public void generateNewKey(final Session session)throws HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setAmicusNumber(dao.getNextNumber("AA", session));
  }


  /**
   * @since 1.0
   */
  public char getBilingualUsage() {
    return bilingualUsage;
  }

  /**
   * @since 1.0
   */
  public void setBilingualUsage(char c) {
    bilingualUsage = c;
  }

  /**
   * @since 1.0
   */
  public char getCataloguingRules() {
    return cataloguingRules;
  }

  /**
   * @since 1.0
   */
  public void setCataloguingRules(char c) {
    cataloguingRules = c;
  }

  /**
   * @since 1.0
   */
  public char getCataloguingSourceCode() {
    return cataloguingSourceCode;
  }

  /**
   * @since 1.0
   */
  public void setCataloguingSourceCode(char c) {
    cataloguingSourceCode = c;
  }

  /**
   * @since 1.0
   */
  public char getConferenceOrMeeting() {
    return conferenceOrMeeting;
  }

  /**
   * @since 1.0
   */
  public void setConferenceOrMeeting(char c) {
    conferenceOrMeeting = c;
  }

  /**
   * @since 1.0
   */
  public AbstractDAO getDAO() {
    return persistenceState.getDAO();
  }

  /**
   * @since 1.0
   */
  @Override
  public char getEncodingLevel() {
    return encodingLevel;
  }

  /**
   * @since 1.0
   */
  @Override
  public void setEncodingLevel(char c) {
    encodingLevel = c;
  }

  /**
   * @since 1.0
   */
  public char getGovernmentAgency() {
    return governmentAgency;
  }

  /**
   * @since 1.0
   */
  public void setGovernmentAgency(char c) {
    governmentAgency = c;
  }

  /**
   * @since 1.0
   */
  public String getHeadingLanguage() {
    return headingLanguage;
  }

  /**
   * @since 1.0
   */
  public void setHeadingLanguage(String string) {
    headingLanguage = string;
  }

  /**
   * @since 1.0
   */
  public int getHeadingNumber() {
    return headingNumber;
  }

  /**
   * @since 1.0
   */
  public void setHeadingNumber(int i) {
    headingNumber = i;
  }

  /**
   * @since 1.0
   */
  public char getHeadingStatus() {
    return headingStatus;
  }

  /**
   * @since 1.0
   */
  public void setHeadingStatus(char c) {
    headingStatus = c;
  }

  /**
   * @since 1.0
   */
  public String getHeadingType() {
    return headingType;
  }

  /**
   * @since 1.0
   */
  public void setHeadingType(String string) {
    headingType = string;
  }

  /**
   * @since 1.0
   */
  public char getMainAddedEntryIndicator() {
    return mainAddedEntryIndicator;
  }

  /**
   * @since 1.0
   */
  public void setMainAddedEntryIndicator(char c) {
    mainAddedEntryIndicator = c;
  }

  /**
   * @since 1.0
   */
  public char getNonUniqueName() {
    return nonUniqueName;
  }

  /**
   * @since 1.0
   */
  public void setNonUniqueName(char c) {
    nonUniqueName = c;
  }

  /**
   * @since 1.0
   */
  public char getRecordModification() {
    return recordModification;
  }

  /**
   * @since 1.0
   */
  public void setRecordModification(char c) {
    recordModification = c;
  }

  /**
   * @since 1.0
   */
  public char getRecordRevision() {
    return recordRevision;
  }

  /**
   * @since 1.0
   */
  public void setRecordRevision(char c) {
    recordRevision = c;
  }

  /**
   * @since 1.0
   */
  public char getRecordType() {
    return recordType;
  }

  /**
   * @since 1.0
   */
  public void setRecordType(char c) {
    recordType = c;
  }

  /**
   * @since 1.0
   */
  public char getReferenceStatus() {
    return referenceStatus;
  }

  /**
   * @since 1.0
   */
  public void setReferenceStatus(char c) {
    referenceStatus = c;
  }

  /**
   * @since 1.0
   */
  public char getRomanizationScheme() {
    return romanizationScheme;
  }

  /**
   * @since 1.0
   */
  public void setRomanizationScheme(char c) {
    romanizationScheme = c;
  }

  /**
   * @since 1.0
   */
  public char getSeriesEntryIndicator() {
    return seriesEntryIndicator;
  }

  /**
   * @since 1.0
   */
  public void setSeriesEntryIndicator(char c) {
    seriesEntryIndicator = c;
  }

  /**
   * @since 1.0
   */
  public char getSeriesNumbering() {
    return seriesNumbering;
  }

  /**
   * @since 1.0
   */
  public void setSeriesNumbering(char c) {
    seriesNumbering = c;
  }

  /**
   * @since 1.0
   */
  public char getSeriesType() {
    return seriesType;
  }

  /**
   * @since 1.0
   */
  public void setSeriesType(char c) {
    seriesType = c;
  }

  /**
   * @since 1.0
   */
  public char getSubDivisionType() {
    return subDivisionType;
  }

  /**
   * @since 1.0
   */
  public void setSubDivisionType(char c) {
    subDivisionType = c;
  }

  /**
   * @since 1.0
   */
  public char getSubjectDescriptor() {
    return subjectDescriptor;
  }

  /**
   * @since 1.0
   */
  public void setSubjectDescriptor(char c) {
    subjectDescriptor = c;
  }

  /**
   * @since 1.0
   */
  public char getSubjectEntryIndicator() {
    return subjectEntryIndicator;
  }

  /**
   * @since 1.0
   */
  public void setSubjectEntryIndicator(char c) {
    subjectEntryIndicator = c;
  }

  /**
   * @since 1.0
   */
  public char getSubjectSystem() {
    return subjectSystem;
  }

  /**
   * @since 1.0
   */
  public void setSubjectSystem(char c) {
    subjectSystem = c;
  }

  /**
   * @since 1.0
   */
  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }

  /**
   * @since 1.0
   */
  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
  }

  /**
   * @since 1.0
   */
  public boolean isChanged() {
    return persistenceState.isChanged();
  }

  /**
   * @since 1.0
   */
  public boolean isDeleted() {
    return persistenceState.isDeleted();
  }

  /**
   * @since 1.0
   */
  public boolean isNew() {
    return persistenceState.isNew();
  }

  /**
   * @since 1.0
   */
  public boolean isRemoved() {
    return persistenceState.isRemoved();
  }

  /**
   * @since 1.0
   */
  public void markChanged() {
    persistenceState.markChanged();
  }

  /**
   * @since 1.0
   */
  public void markDeleted() {
    persistenceState.markDeleted();
  }

  /**
   * @since 1.0
   */
  public void markNew() {
    persistenceState.markNew();
  }

  /**
   * @since 1.0
   */
  public void markUnchanged() {
    persistenceState.markUnchanged();
  }

  /**
   * @since 1.0
   */
  public boolean onDelete(Session s) throws CallbackException {
    return persistenceState.onDelete(s);
  }

  /**
   * @since 1.0
   */
  public void onLoad(Session s, Serializable id) {
    persistenceState.onLoad(s, id);
  }

  /**
   * @since 1.0
   */
  public boolean onSave(Session s) throws CallbackException {
    return persistenceState.onSave(s);
  }

  /**
   * @since 1.0
   */
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
