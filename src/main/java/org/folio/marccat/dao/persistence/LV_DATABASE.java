/*
 * (c) LibriCore
 *
 * Created on 19-ene-2005
 *
 * LV_DATABASE.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class LV_DATABASE implements Serializable {

  private int databaseNumber;
  private String databaseName;
  private String databaseDescription;
  private String databaseAuthentication;
  private String databaseAddress;
  private int databasePort;
  private char databaseProtocol;
  private short serverUnicode;
  private short serverLogin;
  private short serverTruncation;
  private short serverSearch;
  private short serverPresent;
  private short serverDeleteResultSet;
  private short serverResourceReport;
  private short serverTriggerResourceControl;
  private short serverResourceControl;
  private short serverAccessControl;
  private short serverScan;
  private short serverSort;
  private short serverExtebdedServices;
  private short serverLevel1Segmentation;
  private short serverLevel2Segmentation;
  private short serverConcurrentResultSets;
  private short serverNamedResultSets;
  private short serverEncapsulation;
  private short serverResultSetCountParameteInSortReponse;
  private short serverNegotiationModel;
  private short serverDuplicateDetection;
  private String attibute;
  private int recordSyntax;
  private Character specification1;
  private Character specification2;
  private Character specification3;
  private Character specification4;
  private Character specification5;
  private String urlDescription;
  private String longDescription;
  private short deletionIndicator;
  private Short showActiveDatabase;
  private String allowedIpAddresses;
  private Integer alternativeAccess;
  private String characterSetScanRequest;
  private String characterSetScanResponseTerm;
  private String characterSetScanResponseDisplayTerm;
  private String characterSetSearchRequest;
  private String chararacterSetPresentResponse;
  private String limitSearchByPqf;


  /**
   * @return Returns the allowedIpAddresses.
   * @throws
   * @since 1.0
   */
  public String getAllowedIpAddresses() {
    return allowedIpAddresses;
  }

  /**
   * @param allowedIpAddresses The allowedIpAddresses to set.
   * @throws
   * @since 1.0
   */
  public void setAllowedIpAddresses(String allowedIpAddresses) {
    this.allowedIpAddresses = allowedIpAddresses;
  }

  /**
   * @return Returns the alternativeAccess.
   * @throws
   * @since 1.0
   */
  public Integer getAlternativeAccess() {
    return alternativeAccess;
  }

  /**
   * @param alternativeAccess The alternativeAccess to set.
   * @throws
   * @since 1.0
   */
  public void setAlternativeAccess(Integer alternativeAccess) {
    this.alternativeAccess = alternativeAccess;
  }

  /**
   * @return Returns the attibute.
   * @throws
   * @since 1.0
   */
  public String getAttibute() {
    return attibute;
  }

  /**
   * @param attibute The attibute to set.
   * @throws
   * @since 1.0
   */
  public void setAttibute(String attibute) {
    this.attibute = attibute;
  }

  /**
   * @return Returns the characterSetScanRequest.
   * @throws
   * @since 1.0
   */
  public String getCharacterSetScanRequest() {
    return characterSetScanRequest;
  }

  /**
   * @param characterSetScanRequest The characterSetScanRequest to set.
   * @throws
   * @since 1.0
   */
  public void setCharacterSetScanRequest(String characterSetScanRequest) {
    this.characterSetScanRequest = characterSetScanRequest;
  }

  /**
   * @return Returns the characterSetScanResponseDisplayTerm.
   * @throws
   * @since 1.0
   */
  public String getCharacterSetScanResponseDisplayTerm() {
    return characterSetScanResponseDisplayTerm;
  }

  /**
   * @param characterSetScanResponseDisplayTerm The characterSetScanResponseDisplayTerm to set.
   * @throws
   * @since 1.0
   */
  public void setCharacterSetScanResponseDisplayTerm(
    String characterSetScanResponseDisplayTerm) {
    this.characterSetScanResponseDisplayTerm = characterSetScanResponseDisplayTerm;
  }

  /**
   * @return Returns the characterSetScanResponseTerm.
   * @throws
   * @since 1.0
   */
  public String getCharacterSetScanResponseTerm() {
    return characterSetScanResponseTerm;
  }

  /**
   * @param characterSetScanResponseTerm The characterSetScanResponseTerm to set.
   * @throws
   * @since 1.0
   */
  public void setCharacterSetScanResponseTerm(
    String characterSetScanResponseTerm) {
    this.characterSetScanResponseTerm = characterSetScanResponseTerm;
  }

  /**
   * @return Returns the characterSetSearchRequest.
   * @throws
   * @since 1.0
   */
  public String getCharacterSetSearchRequest() {
    return characterSetSearchRequest;
  }

  /**
   * @param characterSetSearchRequest The characterSetSearchRequest to set.
   * @throws
   * @since 1.0
   */
  public void setCharacterSetSearchRequest(String characterSetSearchRequest) {
    this.characterSetSearchRequest = characterSetSearchRequest;
  }

  /**
   * @return Returns the chararacterSetPresentResponse.
   * @throws
   * @since 1.0
   */
  public String getChararacterSetPresentResponse() {
    return chararacterSetPresentResponse;
  }

  /**
   * @param chararacterSetPresentResponse The chararacterSetPresentResponse to set.
   * @throws
   * @since 1.0
   */
  public void setChararacterSetPresentResponse(
    String chararacterSetPresentResponse) {
    this.chararacterSetPresentResponse = chararacterSetPresentResponse;
  }

  /**
   * @return Returns the databaseAddress.
   * @throws
   * @since 1.0
   */
  public String getDatabaseAddress() {
    return databaseAddress;
  }

  /**
   * @param databaseAddress The databaseAddress to set.
   * @throws
   * @since 1.0
   */
  public void setDatabaseAddress(String databaseAddress) {
    this.databaseAddress = databaseAddress;
  }

  /**
   * @return Returns the databaseAuthentication.
   * @throws
   * @since 1.0
   */
  public String getDatabaseAuthentication() {
    return databaseAuthentication;
  }

  /**
   * @param databaseAuthentication The databaseAuthentication to set.
   * @throws
   * @since 1.0
   */
  public void setDatabaseAuthentication(String databaseAuthentication) {
    this.databaseAuthentication = databaseAuthentication;
  }

  /**
   * @return Returns the databaseDescription.
   * @throws
   * @since 1.0
   */
  public String getDatabaseDescription() {
    return databaseDescription;
  }

  /**
   * @param databaseDescription The databaseDescription to set.
   * @throws
   * @since 1.0
   */
  public void setDatabaseDescription(String databaseDescription) {
    this.databaseDescription = databaseDescription;
  }

  /**
   * @return Returns the databaseName.
   * @throws
   * @since 1.0
   */
  public String getDatabaseName() {
    return databaseName;
  }

  /**
   * @param databaseName The databaseName to set.
   * @throws
   * @since 1.0
   */
  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }

  /**
   * @return Returns the databaseNumber.
   * @throws
   * @since 1.0
   */
  public int getDatabaseNumber() {
    return databaseNumber;
  }

  /**
   * @param databaseNumber The databaseNumber to set.
   * @throws
   * @since 1.0
   */
  public void setDatabaseNumber(int databaseNumber) {
    this.databaseNumber = databaseNumber;
  }

  /**
   * @return Returns the databasePort.
   * @throws
   * @since 1.0
   */
  public int getDatabasePort() {
    return databasePort;
  }

  /**
   * @param databasePort The databasePort to set.
   * @throws
   * @since 1.0
   */
  public void setDatabasePort(int databasePort) {
    this.databasePort = databasePort;
  }

  /**
   * @return Returns the databaseProtocol.
   * @throws
   * @since 1.0
   */
  public char getDatabaseProtocol() {
    return databaseProtocol;
  }

  /**
   * @param databaseProtocol The databaseProtocol to set.
   * @throws
   * @since 1.0
   */
  public void setDatabaseProtocol(char databaseProtocol) {
    this.databaseProtocol = databaseProtocol;
  }

  /**
   * @return Returns the deletionIndicator.
   * @throws
   * @since 1.0
   */
  public short getDeletionIndicator() {
    return deletionIndicator;
  }

  /**
   * @param deletionIndicator The deletionIndicator to set.
   * @throws
   * @since 1.0
   */
  public void setDeletionIndicator(short deletionIndicator) {
    this.deletionIndicator = deletionIndicator;
  }

  /**
   * @return Returns the limitSearchByPqf.
   * @throws
   * @since 1.0
   */
  public String getLimitSearchByPqf() {
    return limitSearchByPqf;
  }

  /**
   * @param limitSearchByPqf The limitSearchByPqf to set.
   * @throws
   * @since 1.0
   */
  public void setLimitSearchByPqf(String limitSearchByPqf) {
    this.limitSearchByPqf = limitSearchByPqf;
  }

  /**
   * @return Returns the longDescription.
   * @throws
   * @since 1.0
   */
  public String getLongDescription() {
    return longDescription;
  }

  /**
   * @param longDescription The longDescription to set.
   * @throws
   * @since 1.0
   */
  public void setLongDescription(String longDescription) {
    this.longDescription = longDescription;
  }

  /**
   * @return Returns the recordSyntax.
   * @throws
   * @since 1.0
   */
  public int getRecordSyntax() {
    return recordSyntax;
  }

  /**
   * @param recordSyntax The recordSyntax to set.
   * @throws
   * @since 1.0
   */
  public void setRecordSyntax(int recordSyntax) {
    this.recordSyntax = recordSyntax;
  }

  /**
   * @return Returns the serverAccessControl.
   * @throws
   * @since 1.0
   */
  public short getServerAccessControl() {
    return serverAccessControl;
  }

  /**
   * @param serverAccessControl The serverAccessControl to set.
   * @throws
   * @since 1.0
   */
  public void setServerAccessControl(short serverAccessControl) {
    this.serverAccessControl = serverAccessControl;
  }

  /**
   * @return Returns the serverConcurrentResultSets.
   * @throws
   * @since 1.0
   */
  public short getServerConcurrentResultSets() {
    return serverConcurrentResultSets;
  }

  /**
   * @param serverConcurrentResultSets The serverConcurrentResultSets to set.
   * @throws
   * @since 1.0
   */
  public void setServerConcurrentResultSets(short serverConcurrentResultSets) {
    this.serverConcurrentResultSets = serverConcurrentResultSets;
  }

  /**
   * @return Returns the serverDeleteResultSet.
   * @throws
   * @since 1.0
   */
  public short getServerDeleteResultSet() {
    return serverDeleteResultSet;
  }

  /**
   * @param serverDeleteResultSet The serverDeleteResultSet to set.
   * @throws
   * @since 1.0
   */
  public void setServerDeleteResultSet(short serverDeleteResultSet) {
    this.serverDeleteResultSet = serverDeleteResultSet;
  }

  /**
   * @return Returns the serverDuplicateDetection.
   * @throws
   * @since 1.0
   */
  public short getServerDuplicateDetection() {
    return serverDuplicateDetection;
  }

  /**
   * @param serverDuplicateDetection The serverDuplicateDetection to set.
   * @throws
   * @since 1.0
   */
  public void setServerDuplicateDetection(short serverDuplicateDetection) {
    this.serverDuplicateDetection = serverDuplicateDetection;
  }

  /**
   * @return Returns the serverEncapsulation.
   * @throws
   * @since 1.0
   */
  public short getServerEncapsulation() {
    return serverEncapsulation;
  }

  /**
   * @param serverEncapsulation The serverEncapsulation to set.
   * @throws
   * @since 1.0
   */
  public void setServerEncapsulation(short serverEncapsulation) {
    this.serverEncapsulation = serverEncapsulation;
  }

  /**
   * @return Returns the serverExtebdedServices.
   * @throws
   * @since 1.0
   */
  public short getServerExtebdedServices() {
    return serverExtebdedServices;
  }

  /**
   * @param serverExtebdedServices The serverExtebdedServices to set.
   * @throws
   * @since 1.0
   */
  public void setServerExtebdedServices(short serverExtebdedServices) {
    this.serverExtebdedServices = serverExtebdedServices;
  }

  /**
   * @return Returns the serverLevel1Segmentation.
   * @throws
   * @since 1.0
   */
  public short getServerLevel1Segmentation() {
    return serverLevel1Segmentation;
  }

  /**
   * @param serverLevel1Segmentation The serverLevel1Segmentation to set.
   * @throws
   * @since 1.0
   */
  public void setServerLevel1Segmentation(short serverLevel1Segmentation) {
    this.serverLevel1Segmentation = serverLevel1Segmentation;
  }

  /**
   * @return Returns the serverLevel2Segmentation.
   * @throws
   * @since 1.0
   */
  public short getServerLevel2Segmentation() {
    return serverLevel2Segmentation;
  }

  /**
   * @param serverLevel2Segmentation The serverLevel2Segmentation to set.
   * @throws
   * @since 1.0
   */
  public void setServerLevel2Segmentation(short serverLevel2Segmentation) {
    this.serverLevel2Segmentation = serverLevel2Segmentation;
  }

  /**
   * @return Returns the serverLogin.
   * @throws
   * @since 1.0
   */
  public short getServerLogin() {
    return serverLogin;
  }

  /**
   * @param serverLogin The serverLogin to set.
   * @throws
   * @since 1.0
   */
  public void setServerLogin(short serverLogin) {
    this.serverLogin = serverLogin;
  }

  /**
   * @return Returns the serverNamedResultSets.
   * @throws
   * @since 1.0
   */
  public short getServerNamedResultSets() {
    return serverNamedResultSets;
  }

  /**
   * @param serverNamedResultSets The serverNamedResultSets to set.
   * @throws
   * @since 1.0
   */
  public void setServerNamedResultSets(short serverNamedResultSets) {
    this.serverNamedResultSets = serverNamedResultSets;
  }

  /**
   * @return Returns the serverNegotiationModel.
   * @throws
   * @since 1.0
   */
  public short getServerNegotiationModel() {
    return serverNegotiationModel;
  }

  /**
   * @param serverNegotiationModel The serverNegotiationModel to set.
   * @throws
   * @since 1.0
   */
  public void setServerNegotiationModel(short serverNegotiationModel) {
    this.serverNegotiationModel = serverNegotiationModel;
  }

  /**
   * @return Returns the serverPresent.
   * @throws
   * @since 1.0
   */
  public short getServerPresent() {
    return serverPresent;
  }

  /**
   * @param serverPresent The serverPresent to set.
   * @throws
   * @since 1.0
   */
  public void setServerPresent(short serverPresent) {
    this.serverPresent = serverPresent;
  }

  /**
   * @return Returns the serverResourceControl.
   * @throws
   * @since 1.0
   */
  public short getServerResourceControl() {
    return serverResourceControl;
  }

  /**
   * @param serverResourceControl The serverResourceControl to set.
   * @throws
   * @since 1.0
   */
  public void setServerResourceControl(short serverResourceControl) {
    this.serverResourceControl = serverResourceControl;
  }

  /**
   * @return Returns the serverResourceReport.
   * @throws
   * @since 1.0
   */
  public short getServerResourceReport() {
    return serverResourceReport;
  }

  /**
   * @param serverResourceReport The serverResourceReport to set.
   * @throws
   * @since 1.0
   */
  public void setServerResourceReport(short serverResourceReport) {
    this.serverResourceReport = serverResourceReport;
  }

  /**
   * @return Returns the serverResultSetCountParameteInSortReponse.
   * @throws
   * @since 1.0
   */
  public short getServerResultSetCountParameteInSortReponse() {
    return serverResultSetCountParameteInSortReponse;
  }

  /**
   * @param serverResultSetCountParameteInSortReponse The serverResultSetCountParameteInSortReponse to set.
   * @throws
   * @since 1.0
   */
  public void setServerResultSetCountParameteInSortReponse(
    short serverResultSetCountParameteInSortReponse) {
    this.serverResultSetCountParameteInSortReponse = serverResultSetCountParameteInSortReponse;
  }

  /**
   * @return Returns the serverScan.
   * @throws
   * @since 1.0
   */
  public short getServerScan() {
    return serverScan;
  }

  /**
   * @param serverScan The serverScan to set.
   * @throws
   * @since 1.0
   */
  public void setServerScan(short serverScan) {
    this.serverScan = serverScan;
  }

  /**
   * @return Returns the serverSearch.
   * @throws
   * @since 1.0
   */
  public short getServerSearch() {
    return serverSearch;
  }

  /**
   * @param serverSearch The serverSearch to set.
   * @throws
   * @since 1.0
   */
  public void setServerSearch(short serverSearch) {
    this.serverSearch = serverSearch;
  }

  /**
   * @return Returns the serverSort.
   * @throws
   * @since 1.0
   */
  public short getServerSort() {
    return serverSort;
  }

  /**
   * @param serverSort The serverSort to set.
   * @throws
   * @since 1.0
   */
  public void setServerSort(short serverSort) {
    this.serverSort = serverSort;
  }

  /**
   * @return Returns the serverTriggerResourceControl.
   * @throws
   * @since 1.0
   */
  public short getServerTriggerResourceControl() {
    return serverTriggerResourceControl;
  }

  /**
   * @param serverTriggerResourceControl The serverTriggerResourceControl to set.
   * @throws
   * @since 1.0
   */
  public void setServerTriggerResourceControl(
    short serverTriggerResourceControl) {
    this.serverTriggerResourceControl = serverTriggerResourceControl;
  }

  /**
   * @return Returns the serverTruncation.
   * @throws
   * @since 1.0
   */
  public short getServerTruncation() {
    return serverTruncation;
  }

  /**
   * @param serverTruncation The serverTruncation to set.
   * @throws
   * @since 1.0
   */
  public void setServerTruncation(short serverTruncation) {
    this.serverTruncation = serverTruncation;
  }

  /**
   * @return Returns the serverUnicode.
   * @throws
   * @since 1.0
   */
  public short getServerUnicode() {
    return serverUnicode;
  }

  /**
   * @param serverUnicode The serverUnicode to set.
   * @throws
   * @since 1.0
   */
  public void setServerUnicode(short serverUnicode) {
    this.serverUnicode = serverUnicode;
  }

  /**
   * @return Returns the showActiveDatabase.
   * @throws
   * @since 1.0
   */
  public Short getShowActiveDatabase() {
    return showActiveDatabase;
  }

  /**
   * @param showActiveDatabase The showActiveDatabase to set.
   * @throws
   * @since 1.0
   */
  public void setShowActiveDatabase(Short showActiveDatabase) {
    this.showActiveDatabase = showActiveDatabase;
  }

  /**
   * @return Returns the specification1.
   * @throws
   * @since 1.0
   */
  public Character getSpecification1() {
    return specification1;
  }

  /**
   * @param specification1 The specification1 to set.
   * @throws
   * @since 1.0
   */
  public void setSpecification1(Character specification1) {
    this.specification1 = specification1;
  }

  /**
   * @return Returns the specification2.
   * @throws
   * @since 1.0
   */
  public Character getSpecification2() {
    return specification2;
  }

  /**
   * @param specification2 The specification2 to set.
   * @throws
   * @since 1.0
   */
  public void setSpecification2(Character specification2) {
    this.specification2 = specification2;
  }

  /**
   * @return Returns the specification3.
   * @throws
   * @since 1.0
   */
  public Character getSpecification3() {
    return specification3;
  }

  /**
   * @param specification3 The specification3 to set.
   * @throws
   * @since 1.0
   */
  public void setSpecification3(Character specification3) {
    this.specification3 = specification3;
  }

  /**
   * @return Returns the specification4.
   * @throws
   * @since 1.0
   */
  public Character getSpecification4() {
    return specification4;
  }

  /**
   * @param specification4 The specification4 to set.
   * @throws
   * @since 1.0
   */
  public void setSpecification4(Character specification4) {
    this.specification4 = specification4;
  }

  /**
   * @return Returns the specification5.
   * @throws
   * @since 1.0
   */
  public Character getSpecification5() {
    return specification5;
  }

  /**
   * @param specification5 The specification5 to set.
   * @throws
   * @since 1.0
   */
  public void setSpecification5(Character specification5) {
    this.specification5 = specification5;
  }

  /**
   * @return Returns the urlDescription.
   * @throws
   * @since 1.0
   */
  public String getUrlDescription() {
    return urlDescription;
  }

  /**
   * @param urlDescription The urlDescription to set.
   * @throws
   * @since 1.0
   */
  public void setUrlDescription(String urlDescription) {
    this.urlDescription = urlDescription;
  }
}
