/*
 * (c) LibriCore
 * 
 * Created on 14-jul-2004
 * 
 * LIB.java
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;
import java.util.Date;

/**
 * @author elena
 * @version $Revision: 1.3 $, $Date: 2004/08/02 09:39:52 $
 * @since 1.0
 */
public class LIB implements Serializable{

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

	public String getLibraryBddqSymbolCode() {
		return libraryBddqSymbolCode;
	}

	public String getLibraryCollectionConvertionPctNote() {
		return libraryCollectionConvertionPctNote;
	}

	public String getLibraryExternalUnnCatalogNote() {
		return libraryExternalUnnCatalogNote;
	}

	public String getLibraryIllNote() {
		return libraryIllNote;
	}

	public String getLibraryNote() {
		return libraryNote;
	}

	public String getLibraryOclcSymbolCode() {
		return libraryOclcSymbolCode;
	}

	public String getLibraryOtherSymbolCode() {
		return libraryOtherSymbolCode;
	}

	public String getLibraryRlinSymbolCode() {
		return libraryRlinSymbolCode;
	}

	public Date getLibraryRptgEndDate() {
		return libraryRptgEndDate;
	}

	public Date getLibraryRptgStrtDate() {
		return libraryRptgStrtDate;
	}

	public String getLibrarySanNumber() {
		return librarySanNumber;
	}

	public int getLibraryStatusCode() {
		return libraryStatusCode;
	}

	public int getLibrarySymbolTypeCode() {
		return librarySymbolTypeCode;
	}

	public int getLibraryTypeCode() {
		return libraryTypeCode;
	}

	public String getLibraryUtlasWhoSymbolCode() {
		return libraryUtlasWhoSymbolCode;
	}

	public String getLibrarySymbolCode() {
		return librarySymbolCode;
	}

	public int getOrganisationNumber() {
		return organisationNumber;
	}

	public void setLckgVersionUpdateNumber(int i) {
		lckgVersionUpdateNumber = i;
	}

	public void setLibraryBddqSymbolCode(String string) {
		libraryBddqSymbolCode = string;
	}

	public void setLibraryCollectionConvertionPctNote(String string) {
		libraryCollectionConvertionPctNote = string;
	}

	public void setLibraryExternalUnnCatalogNote(String string) {
		libraryExternalUnnCatalogNote = string;
	}

	public void setLibraryIllNote(String string) {
		libraryIllNote = string;
	}

	public void setLibraryNote(String string) {
		libraryNote = string;
	}

	public void setLibraryOclcSymbolCode(String string) {
		libraryOclcSymbolCode = string;
	}

	public void setLibraryOtherSymbolCode(String string) {
		libraryOtherSymbolCode = string;
	}

	public void setLibraryRlinSymbolCode(String string) {
		libraryRlinSymbolCode = string;
	}

	public void setLibraryRptgEndDate(Date date) {
		libraryRptgEndDate = date;
	}

	public void setLibraryRptgStrtDate(Date date) {
		libraryRptgStrtDate = date;
	}

	public void setLibrarySanNumber(String string) {
		librarySanNumber = string;
	}

	public void setLibraryStatusCode(int i) {
		libraryStatusCode = i;
	}

	public void setLibrarySymbolTypeCode(int i) {
		librarySymbolTypeCode = i;
	}

	public void setLibraryTypeCode(int i) {
		libraryTypeCode = i;
	}

	public void setLibraryUtlasWhoSymbolCode(String string) {
		libraryUtlasWhoSymbolCode = string;
	}

	public void setLibrarySymbolCode(String string) {
		librarySymbolCode = string;
	}

	public void setOrganisationNumber(int i) {
		organisationNumber = i;
	}

}
