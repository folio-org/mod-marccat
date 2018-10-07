/*
 * (c) LibriCore
 *
 * Created on 14-jul-2004
 *
 * LIB.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * @author elena
 * @version $Revision: 1.3 $, $Date: 2004/08/02 09:39:52 $
 * @since 1.0
 */
public class LIB implements Serializable {

  private int organisationNumber;
  private int lckgVersionUpdateNumber;
  private int librarySymbolTypeCode;
  private String librarySymbolCode;
  private int libraryTypeCode;
  private int libraryStatusCode;
  private String libraryUtlasWhoSymbolCode;
  private String libraryOclcSymbolCode;
  private String libraryRlinSymbolCode;
  private String libraryBddqSymbolCode;
  private String libraryOtherSymbolCode;
  private Date libraryRptgStrtDate;
  private Date libraryRptgEndDate;
  private String libraryIllNote;
  private String libraryNote;
  private String libraryCollectionConvertionPctNote;
  private String libraryExternalUnnCatalogNote;
  private String librarySanNumber;


  public int getLckgVersionUpdateNumber() {
    return lckgVersionUpdateNumber;
  }

  public void setLckgVersionUpdateNumber(int i) {
    lckgVersionUpdateNumber = i;
  }

  public String getLibraryBddqSymbolCode() {
    return libraryBddqSymbolCode;
  }

  public void setLibraryBddqSymbolCode(String string) {
    libraryBddqSymbolCode = string;
  }

  public String getLibraryCollectionConvertionPctNote() {
    return libraryCollectionConvertionPctNote;
  }

  public void setLibraryCollectionConvertionPctNote(String string) {
    libraryCollectionConvertionPctNote = string;
  }

  public String getLibraryExternalUnnCatalogNote() {
    return libraryExternalUnnCatalogNote;
  }

  public void setLibraryExternalUnnCatalogNote(String string) {
    libraryExternalUnnCatalogNote = string;
  }

  public String getLibraryIllNote() {
    return libraryIllNote;
  }

  public void setLibraryIllNote(String string) {
    libraryIllNote = string;
  }

  public String getLibraryNote() {
    return libraryNote;
  }

  public void setLibraryNote(String string) {
    libraryNote = string;
  }

  public String getLibraryOclcSymbolCode() {
    return libraryOclcSymbolCode;
  }

  public void setLibraryOclcSymbolCode(String string) {
    libraryOclcSymbolCode = string;
  }

  public String getLibraryOtherSymbolCode() {
    return libraryOtherSymbolCode;
  }

  public void setLibraryOtherSymbolCode(String string) {
    libraryOtherSymbolCode = string;
  }

  public String getLibraryRlinSymbolCode() {
    return libraryRlinSymbolCode;
  }

  public void setLibraryRlinSymbolCode(String string) {
    libraryRlinSymbolCode = string;
  }

  public Date getLibraryRptgEndDate() {
    return libraryRptgEndDate;
  }

  public void setLibraryRptgEndDate(Date date) {
    libraryRptgEndDate = date;
  }

  public Date getLibraryRptgStrtDate() {
    return libraryRptgStrtDate;
  }

  public void setLibraryRptgStrtDate(Date date) {
    libraryRptgStrtDate = date;
  }

  public String getLibrarySanNumber() {
    return librarySanNumber;
  }

  public void setLibrarySanNumber(String string) {
    librarySanNumber = string;
  }

  public int getLibraryStatusCode() {
    return libraryStatusCode;
  }

  public void setLibraryStatusCode(int i) {
    libraryStatusCode = i;
  }

  public int getLibrarySymbolTypeCode() {
    return librarySymbolTypeCode;
  }

  public void setLibrarySymbolTypeCode(int i) {
    librarySymbolTypeCode = i;
  }

  public int getLibraryTypeCode() {
    return libraryTypeCode;
  }

  public void setLibraryTypeCode(int i) {
    libraryTypeCode = i;
  }

  public String getLibraryUtlasWhoSymbolCode() {
    return libraryUtlasWhoSymbolCode;
  }

  public void setLibraryUtlasWhoSymbolCode(String string) {
    libraryUtlasWhoSymbolCode = string;
  }

  public String getLibrarySymbolCode() {
    return librarySymbolCode;
  }

  public void setLibrarySymbolCode(String string) {
    librarySymbolCode = string;
  }

  public int getOrganisationNumber() {
    return organisationNumber;
  }

  public void setOrganisationNumber(int i) {
    organisationNumber = i;
  }

}
