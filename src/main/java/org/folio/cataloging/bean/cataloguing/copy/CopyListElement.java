package org.folio.cataloging.bean.cataloguing.copy;

import org.folio.cataloging.dao.persistence.CPY_ID;

import java.util.List;
import java.util.Vector;

/**
 * @author elena
 * @version $Revision: 1.5 $, $Date: 2005/08/03 12:40:10 $
 * @since 1.0
 */
public class CopyListElement {
  CPY_ID copy;
  private String dueDate;
  private String librarySymbol;
  private String branchSymbol;
  private String location;
  private String shelfList;
  private int howManyNotes;
  private int howManyInventory;
  private List notesList = new Vector ( );
  private Integer hldgNbr;
  private String hldgText;
  private int bndCpyIdNbr;
  private String discardDate;
  private int discardCode;

  /**
   * MODIFICA BARBARA - inserire nella lista delle copie il posseduto
   * la tipologia del prestito e il copyremarknote
   *
   * @since 1.0
   */
  private String copyStatementText;
  private String loanType;
  private String copyRemarkNote;
  private String shortCopyStatementText;

  private String hardback;
  private String hardbackElement;


  public CopyListElement(CPY_ID copy) {
    setCopy (copy);

  }

  public String getHardbackElement() {
    return hardbackElement;
  }

  public void setHardbackElement(String hardbackElement) {
    this.hardbackElement = hardbackElement;
  }

  public String getShortCopyStatementText() {
    return copyStatementText != null && copyStatementText.length ( ) > 20 ? copyStatementText.substring (0, 20) + "..." : copyStatementText;
  }

  public void setShortCopyStatementText(String shortCopyStatementText) {
    this.shortCopyStatementText = shortCopyStatementText;
  }

  /**
   * @since 1.0
   */
  public String getBarCodeNumber() {
    return getCopy ( ).getBarCodeNumber ( );
  }

  /**
   * @param string
   * @since 1.0
   */
  public void setBarCodeNumber(String string) {
    getCopy ( ).setBarCodeNumber (string);
  }

  /**
   * @since 1.0
   */
  public int getCopyIdNumber() {
    return getCopy ( ).getCopyIdNumber ( );
  }

  /**
   * @param i
   * @since 1.0
   */
  public void setCopyIdNumber(int i) {
    getCopy ( ).setCopyIdNumber (i);
  }

  /**
   * @since 1.0
   */
  public String getLocation() {
    return location;
  }

  /**
   * @param string
   * @since 1.0
   */
  public void setLocation(String string) {
    location = string;
  }

  /**
   * @since 1.0
   */
  public List getNotesList() {
    return notesList;
  }

  /**
   * @param list
   * @since 1.0
   */
  public void setNotesList(List list) {
    notesList = list;
  }

  /**
   * @since 1.0
   */
  public String getBranchSymbol() {
    return branchSymbol;
  }

  /**
   * @param string
   * @since 1.0
   */
  public void setBranchSymbol(String string) {
    branchSymbol = string;
  }

  /**
   * @since 1.0
   */
  public int getHowManyNotes() {
    return howManyNotes;
  }

  /**
   * @param i
   * @since 1.0
   */
  public void setHowManyNotes(int i) {
    howManyNotes = i;
  }

  /**
   * @since 1.0
   */
  public String getShelfList() {
    return shelfList;
  }

  /**
   * @param string
   * @since 1.0
   */
  public void setShelfList(String string) {
    shelfList = string;
  }

  /**
   * @since 1.0
   */
  public String getDueDate() {
    return dueDate;
  }

  /**
   * @param string
   * @since 1.0
   */
  public void setDueDate(String string) {
    dueDate = string;
  }

  /**
   * @since 1.0
   */
  public int getHowManyInventory() {
    return howManyInventory;
  }

  /**
   * @since 1.0
   */
  public void setHowManyInventory(int i) {
    howManyInventory = i;
  }

  /**
   * MODIFICA BARBARA - inserire nella lista delle copie il posseduto
   *
   * @since 1.0
   */
  public String getCopyStatementText() {
    return copyStatementText;
  }

  /**
   * MODIFICA BARBARA - inserire nella lista delle copie il posseduto
   *
   * @since 1.0
   */
  public void setCopyStatementText(String i) {
    copyStatementText = i;
  }

  /**
   * MODIFICA BARBARA - inserire nella lista delle copie il tipo prestito
   *
   * @since 1.0
   */
  public String getLoanType() {
    return loanType;
  }

  public void setLoanType(String loanType) {
    this.loanType = loanType;
  }

  /**
   * MODIFICA BARBARA - inserire nella lista delle copie il tipo prestito
   *
   * @since 1.0
   */
  public String getCopyRemarkNote() {
    return copyRemarkNote;
  }

  public void setCopyRemarkNote(String copyRemarkNote) {
    this.copyRemarkNote = copyRemarkNote;
  }

  /**
   * @since 1.0
   */
  public CPY_ID getCopy() {
    return copy;
  }

  /**
   * @since 1.0
   */
  public void setCopy(CPY_ID cpy_id) {
    copy = cpy_id;
  }

  public String getLibrarySymbol() {
    return librarySymbol;
  }

  public void setLibrarySymbol(String librarySymbol) {
    this.librarySymbol = librarySymbol;
  }

  public Integer getHldgNbr() {
    return hldgNbr;
  }

  public void setHldgNbr(Integer hldgNbr) {
    this.hldgNbr = hldgNbr;
  }

  public String getHldgText() {
    return hldgText;
  }

  public void setHldgText(String hldgText) {
    this.hldgText = hldgText;
  }

  public int getBndCpyIdNbr() {
    return bndCpyIdNbr;
  }

  public void setBndCpyIdNbr(int bndCpyIdNbr) {
    this.bndCpyIdNbr = bndCpyIdNbr;
  }

  public String getHardback() {
    return hardback;
  }

  public void setHardback(String hardback) {
    this.hardback = hardback;
  }


  public String getDiscardDate() {
    return discardDate;
  }

  public void setDiscardDate(String discardDate) {
    this.discardDate = discardDate;
  }

  public int getDiscardCode() {
    return discardCode;
  }

  public void setDiscardCode(int discardCode) {
    this.discardCode = discardCode;
  }


}
