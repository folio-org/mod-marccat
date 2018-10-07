package org.folio.cataloging.business;

import org.folio.cataloging.dao.persistence.CollectionPublisher;

import java.sql.Date;

public class PublisherListElement {

  CollectionPublisher publisherCollection;
  String nameIta;
  String levelCode;
  String publCode;
  int idCollection;
  String statusCode;
  boolean isHierarchy;
  int countPub;

  private Date dateAssociatedRecord;

  public PublisherListElement(CollectionPublisher publisherCollection) {
    setPublisherCollection (publisherCollection);
  }

  public String getPublCode() {
    return publCode;
  }

  public void setPublCode(String publCode) {
    this.publCode = publCode;
  }

  public boolean isHierarchy() {
    return isHierarchy;
  }

  public void setHierarchy(boolean isHierarchy) {
    this.isHierarchy = isHierarchy;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public String getLevelCode() {
    return levelCode;
  }

  public void setLevelCode(String levelCode) {
    this.levelCode = levelCode;
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

  public CollectionPublisher getPublisherCollection() {
    return publisherCollection;
  }

  public void setPublisherCollection(CollectionPublisher publisherCollection) {
    this.publisherCollection = publisherCollection;
    this.idCollection = publisherCollection.getIdCollection ( );
    this.publCode = publisherCollection.getPublCode ( );
  }

  public int getCountPub() {
    return countPub;
  }

  public void setCountPub(int countPub) {
    this.countPub = countPub;
  }

  public Date getDateAssociatedRecord() {
    return dateAssociatedRecord;
  }

  public void setDateAssociatedRecord(Date dateAssociatedRecord) {
    this.dateAssociatedRecord = dateAssociatedRecord;
  }

}
