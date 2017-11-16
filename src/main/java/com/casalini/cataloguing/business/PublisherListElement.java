package com.casalini.cataloguing.business;

import java.sql.Date;

import librisuite.hibernate.CollectionPublisher;

public class PublisherListElement {
	
	CollectionPublisher publisherCollection;
	String nameIta;
	String levelCode;
	String publCode;
    int idCollection;
    String statusCode;
    boolean isHierarchy;
    int countPub; 
//  20101014 inizio: data di associazione record --> se dal record associo le collezioni  
	private Date dateAssociatedRecord;
     
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
	}

	public PublisherListElement(CollectionPublisher publisherCollection) {
		setPublisherCollection(publisherCollection);	
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