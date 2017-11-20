/*
 * (c) LibriCore
 * 
 * Created on Jul 20, 2004
 * 
 * IndexList.java
 */
package org.folio.cataloging.dao.persistence;

/**
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class IndexList {
	private IndexListKey key;
	private short characterUserInterfaceSequenceNumber;
	private short characterUserInterfaceSCNSequenceNumber;
	private int completenessAttribute;
	private int truncationAttribute;
	private int structureAttribute;
	private int positionAttribute;
	private int relationAttribute;
	private int useAttribute;	
	private String languageCode;
	private short sortFormMainTypeCode;
	private short sortFormSubTypeCode;
	private short sortFormTypeCode;
	private short sortFormFunctionCode;
	private short sortFormSkipInFiling;
	private short browseTypeCode;
	private String nameTitleColumnYesNo;
	private String crossReferenceColumnYesNo;
	private String authorityColumnYesNo;
	private String browseCode;
	private String browseProcedureName;	
	private String languageDescription;
	private String codeTableName;
	private String accessPointTableName;
	private String accessPointKeyName;
	/*modifica barbara 26/04/2007 per distinguere indici di mades da quelli di LC*/
	private String codeLibriCatMades;

	public String getCodeLibriCatMades() {
		return codeLibriCatMades;
	}


	public void setCodeLibriCatMades(String codeLibriCatMades) {
		this.codeLibriCatMades = codeLibriCatMades;
	}

	
	
	public String getAccessPointKeyName() {
		return accessPointKeyName;
	}


	public String getAccessPointTableName() {
		return accessPointTableName;
	}


	public String getBrowseCode() {
		return browseCode;
	}


	public String getBrowseProcedureName() {
		return browseProcedureName;
	}


	public short getBrowseTypeCode() {
		return browseTypeCode;
	}


	public short getCharacterUserInterfaceSCNSequenceNumber() {
		return characterUserInterfaceSCNSequenceNumber;
	}


	public short getCharacterUserInterfaceSequenceNumber() {
		return characterUserInterfaceSequenceNumber;
	}


	public String getCodeTableName() {
		return codeTableName;
	}


	public int getCompletenessAttribute() {
		return completenessAttribute;
	}


	public IndexListKey getKey() {
		return key;
	}


	public String getLanguageCode() {
		return languageCode;
	}


	public String getLanguageDescription() {
		return languageDescription;
	}


	public int getPositionAttribute() {
		return positionAttribute;
	}


	public int getRelationAttribute() {
		return relationAttribute;
	}


	public short getSortFormFunctionCode() {
		return sortFormFunctionCode;
	}


	public short getSortFormMainTypeCode() {
		return sortFormMainTypeCode;
	}


	public short getSortFormSkipInFiling() {
		return sortFormSkipInFiling;
	}


	public short getSortFormSubTypeCode() {
		return sortFormSubTypeCode;
	}


	public short getSortFormTypeCode() {
		return sortFormTypeCode;
	}


	public int getStructureAttribute() {
		return structureAttribute;
	}


	public int getTruncationAttribute() {
		return truncationAttribute;
	}


	public int getUseAttribute() {
		return useAttribute;
	}


	public void setAccessPointKeyName(String string) {
		accessPointKeyName = string;
	}


	public void setAccessPointTableName(String string) {
		accessPointTableName = string;
	}


	public void setBrowseCode(String string) {
		browseCode = string;
	}


	public void setBrowseProcedureName(String string) {
		browseProcedureName = string;
	}


	public void setBrowseTypeCode(short s) {
		browseTypeCode = s;
	}


	public void setCharacterUserInterfaceSCNSequenceNumber(short s) {
		characterUserInterfaceSCNSequenceNumber = s;
	}


	public void setCharacterUserInterfaceSequenceNumber(short s) {
		characterUserInterfaceSequenceNumber = s;
	}


	public void setCodeTableName(String string) {
		codeTableName = string;
	}


	public void setCompletenessAttribute(int i) {
		completenessAttribute = i;
	}


	public void setKey(IndexListKey key) {
		this.key = key;
	}


	public void setLanguageCode(String string) {
		languageCode = string;
	}


	public void setLanguageDescription(String string) {
		languageDescription = string;
	}


	public void setPositionAttribute(int i) {
		positionAttribute = i;
	}


	public void setRelationAttribute(int i) {
		relationAttribute = i;
	}


	public void setSortFormFunctionCode(short s) {
		sortFormFunctionCode = s;
	}


	public void setSortFormMainTypeCode(short s) {
		sortFormMainTypeCode = s;
	}


	public void setSortFormSkipInFiling(short s) {
		sortFormSkipInFiling = s;
	}


	public void setSortFormSubTypeCode(short s) {
		sortFormSubTypeCode = s;
	}


	public void setSortFormTypeCode(short s) {
		sortFormTypeCode = s;
	}


	public void setStructureAttribute(int i) {
		structureAttribute = i;
	}


	public void setTruncationAttribute(int i) {
		truncationAttribute = i;
	}


	public void setUseAttribute(int i) {
		useAttribute = i;
	}


	public String getAuthorityColumnYesNo() {
		return authorityColumnYesNo;
	}


	public String getCrossReferenceColumnYesNo() {
		return crossReferenceColumnYesNo;
	}


	public String getNameTitleColumnYesNo() {
		return nameTitleColumnYesNo;
	}


	public void setAuthorityColumnYesNo(String string) {
		authorityColumnYesNo = string;
	}


	public void setCrossReferenceColumnYesNo(String string) {
		crossReferenceColumnYesNo = string;
	}


	public void setNameTitleColumnYesNo(String string) {
		nameTitleColumnYesNo = string;
	}


}
