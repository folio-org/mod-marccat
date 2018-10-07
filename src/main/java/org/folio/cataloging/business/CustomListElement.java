package org.folio.cataloging.business;

import org.folio.cataloging.dao.persistence.CollectionCustomer;

import java.sql.Date;

public class CustomListElement {

  CollectionCustomer custom;
  String nameIta;
  String typologyCode;
  int idCollection;
  String statusCode;
  //Aggiunto 12/10/2009
  int countCst;
  //  20101014 inizio: data di associazione record --> se dal record associo le collezioni
  private Date dateAssociatedRecord;

  public CustomListElement(CollectionCustomer Custom) {
    setCustom (custom);

  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public String getTypologyCode() {
    return typologyCode;
  }

  public void setTypologyCode(String typologyCode) {
    this.typologyCode = typologyCode;
  }

  public String getNameIta() {
    return nameIta;
  }

  public void setNameIta(String nameIta) {
    this.nameIta = nameIta;
  }

  public int getIdCollection() {
    return idCollection;
  }

  public void setIdCollection(int idCollection) {
    this.idCollection = idCollection;
  }

  public CollectionCustomer getCustom() {
    return custom;
  }

  public void setCustom(CollectionCustomer master) {
    this.custom = custom;
  }

  /**
   * @return the countCst
   */
  public int getCountCst() {
    return countCst;
  }

  /**
   * @param countCst the countCst to set
   */
  public void setCountCst(int countCst) {
    this.countCst = countCst;
  }

  public Date getDateAssociatedRecord() {
    return dateAssociatedRecord;
  }

  public void setDateAssociatedRecord(Date dateAssociatedRecord) {
    this.dateAssociatedRecord = dateAssociatedRecord;
  }
}
