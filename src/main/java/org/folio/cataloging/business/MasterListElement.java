package org.folio.cataloging.business;


import org.folio.cataloging.dao.persistence.CollectionMaster;

import java.sql.Date;

public class MasterListElement {
	
	CollectionMaster master;
	String nameIta;
	String typologyCode;
    int idCollection;
    String statusCode;
    boolean isHierarchy;
    //Aggiunto 12/10/2009
    int countMst; 
//  20101014 inizio: data di associazione record --> se dal record associo le collezioni  
	private Date dateAssociatedRecord;
     
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


	public CollectionMaster getMaster() {
		return master;
	}

	public void setMaster(CollectionMaster master) {
		this.master = master;
	}

	public MasterListElement(CollectionMaster master) {
		setMaster(master);
		this.idCollection = master.getIdCollection();
	}

	/**
	 * @return the countMst
	 */
	public int getCountMst() {
		return countMst;
	}

	/**
	 * @param countMst the countMst to set
	 */
	public void setCountMst(int countMst) {
		this.countMst = countMst;
	}

	public Date getDateAssociatedRecord() {
		return dateAssociatedRecord;
	}

	public void setDateAssociatedRecord(Date dateAssociatedRecord) {
		this.dateAssociatedRecord = dateAssociatedRecord;
	}
}
