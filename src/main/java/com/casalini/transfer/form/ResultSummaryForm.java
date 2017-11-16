package com.casalini.transfer.form;

import javax.servlet.http.HttpServletRequest;

import librisuite.form.LibrisuiteForm;

public class ResultSummaryForm extends LibrisuiteForm 
{
	private static final long serialVersionUID = -5509966931707741146L;

	private String cclQuery = null;

	private String newSearchButton = null;
	
	private String openNewSearchButton = null;
	
	private String executeAllButton = null;

	private String showNumberOfRecordsButton = null;

	private int showNumberOfRecords = 10;

	private String printResultsButton = null;

	private String briefViewButton = null;

	private String fullViewButton = null;

	private String openURLButton = null;
	
	private String backButton = null;

	private String displayFormat = null;
	
	private String displayFormatButton = null;

	private String copiesButton = null;

	private String editButton = null;
	
	private String editCollection = null;
	
	private String relationshipButton = null;

	private String cancelButton = null;

	private String addCopyButton = null;

	private String deleteRecordButton = null;

	private String previousPageButton = null;
	
	private String sortButton = null;
	
	private int sortCriteria = 0;

	private int pageNumberButton = 0;
	
	private int amicusNumber;

	private String nextPageButton = null;
	
	private String selectedRecordNumber;
	/*modifica barbara 27/04/2007 prn 137 -*/
	private String closeButton = null;
	
	private String duplicateButton = null;
	
	
	//NIC
	private String addToTransferListButton = null;
	private String executeTransferButton = null;
	private String executeAllTransferButton = null;
	private String deleteFromListButton = null;
	private String[] checkTransf = null;
	
	private String addToDeleteListButton = null;
	private String executeDeleteButton = null;
	private String executeAllDeleteButton = null;
	private String[] checkDelete = null;
	
	private String addToNTIListButton = null;
	private String executeNTIButton =null;
	private String executeAllNTIButton = null;
	private String[] checkNTI = null;
	
	private String cleanupTransferListButton = null;
	private String cleanupDeleteListButton = null;
	private String cleanupNTIListButton = null;
	
    private String checkUncheckTransf = "0";
	private String checkUncheckDelete = "0";
	private String checkUncheckNti = "0";
	
	private String recordToDeleteFromDeleteList=null;
	private String recordToDeleteFromTransfList=null;
	private String recordToDeleteFromNTIList=null;
	
	private String uncheckedNTIArray = null;
	
	public String[] getCheckTransf() {
		return checkTransf;
	}

	public void setCheckTransf(String[] checkTransf) {
		this.checkTransf = checkTransf;
	}

	public String getAddToDeleteListButton() {
		return addToDeleteListButton;
	}

	public void setAddToDeleteListButton(String addToDeleteListButton) {
		this.addToDeleteListButton = addToDeleteListButton;
	}

	public String getAddToNTIListButton() {
		return addToNTIListButton;
	}

	public void setAddToNTIListButton(String addToNTIListButton) {
		this.addToNTIListButton = addToNTIListButton;
	}

	public String getAddToTransferListButton() {
		return addToTransferListButton;
	}

	public void setAddToTransferListButton(String addToTransferListButton) {
		this.addToTransferListButton = addToTransferListButton;
	}

	public String[] getCheckDelete() {
		return checkDelete;
	}

	public void setCheckDelete(String[] checkDelete) {
		this.checkDelete = checkDelete;
	}

	public String[] getCheckNTI() {
		return checkNTI;
	}

	public void setCheckNTI(String[] checkNTI) {
		this.checkNTI = checkNTI;
//        for(int i=0; i<this.checkNTI.length;i++){
//        	if(this.checkNTI[i].equals("false")){
//        		this.checkNTI
//        	}
//        }
	}

	public String getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(String string) {
		closeButton = string;
	}
	
	/*modifica Natascia 11/06/2007 - prn 198*/
	private String closeButtonCopies = null;
	public String getCloseButtonCopies() {
		return closeButtonCopies;
	}
	public void setCloseButtonCopies(String string) {
		closeButtonCopies = string;
	}
	
	public void removeElementFromCheckNTI(String recNumber){
		
		int appoggio=0;
		if(this.checkNTI!=null && this.checkNTI.length!=0){
		String[] arrayAppoggio=new String[this.checkNTI.length-1];
		for(int i=0;i<this.checkNTI.length;i++){
			if(checkNTI[i].equals(recNumber)){
				appoggio=appoggio+1;
				continue;
			}
			if(i<this.checkNTI.length-1){
			arrayAppoggio[i-appoggio]=checkNTI[i];
			}
		}
		this.setCheckNTI(arrayAppoggio);
		}
	}

	/**
	 * Getter for cclQuery
	 * 
	 * @return cclQuery
	 * @since 1.0
	 */
	public String getCclQuery() {
		return cclQuery;
	}

	/**
	 * Getetr for newSearchButton
	 * 
	 * @return newSearchButton
	 * @since 1.0
	 */
	public String getNewSearchButton() {
		return newSearchButton;
	}

	/**
	 * Getter for nextPageButton
	 * 
	 * @return nextPageButton
	 * @since 1.0
	 */
	public String getNextPageButton() {
		return nextPageButton;
	}

	/**
	 * Getter for pageNumberButton
	 * 
	 * @return pageNumberButton
	 * @since 1.0
	 */
	public int getPageNumberButton() {
		return pageNumberButton;
	}

	/**
	 * Getter for previousPageButton
	 * 
	 * @return previousPageButton
	 * @since 1.0
	 */
	public String getPreviousPageButton() {
		return previousPageButton;
	}

	/**
	 * Getter for printResultsButton
	 * 
	 * @return printResultsButton
	 * @since 1.0
	 */
	public String getPrintResultsButton() {
		return printResultsButton;
	}

	/**
	 * Getter for showNumberOfRecords
	 * 
	 * @return showNumberOfRecords
	 * @since 1.0
	 */
	public int getShowNumberOfRecords() {
		return showNumberOfRecords;
	}

	/**
	 * Getter for showNumberOfRecordsButton
	 * 
	 * @return showNumberOfRecordsButton
	 * @since 1.0
	 */
	public String getShowNumberOfRecordsButton() {
		return showNumberOfRecordsButton;
	}

	/**
	 * Setter for cclQuery
	 * 
	 * @param string cclQuery
	 * @since 1.0
	 */
	public void setCclQuery(String string) {
		cclQuery = string;
	}

	/**
	 * Setter for newSearchButton
	 * 
	 * @param string newSearchButton
	 * @since 1.0
	 */
	public void setNewSearchButton(String string) {
		newSearchButton = string;
	}

	/**
	 * Setter for nextPageButton
	 * 
	 * @param string nextPageButton
	 * @since 1.0
	 */
	public void setNextPageButton(String string) {
		nextPageButton = string;
	}

	/**
	 * Setter for pageNumberButton
	 * 
	 * @param i pageNumberButton
	 * @since 1.0
	 */
	public void setPageNumberButton(int i) {
		pageNumberButton = i;
	}

	/**
	 * Setter for previousPageButton
	 * 
	 * @param string previousPageButton
	 * @since 1.0
	 */
	public void setPreviousPageButton(String string) {
		previousPageButton = string;
	}

	/**
	 * Setter for printResultsButton
	 * 
	 * @param string printResultsButton
	 * @since 1.0
	 */
	public void setPrintResultsButton(String string) {
		printResultsButton = string;
	}

	/**
	 * Setter for showNumberOfRecords
	 * 
	 * @param i showNumberOfRecords
	 * @since 1.0
	 */
	public void setShowNumberOfRecords(int i) {
		showNumberOfRecords = i;
	}

	/**
	 * Setter for showNumberOfRecordsButton
	 * 
	 * @param string showNumberOfRecordsButton
	 * @since 1.0
	 */
	public void setShowNumberOfRecordsButton(String string) {
		showNumberOfRecordsButton = string;
	}

	/**
	 * @return Returns the briefViewButton.
	 */
	public String getBriefViewButton() {
		return briefViewButton;
	}

	/**
	 * @param briefViewButton The briefViewButton to set.
	 */
	public void setBriefViewButton(String briefViewButton) {
		this.briefViewButton = briefViewButton;
	}

	/**
	 * @return Returns the fullViewButton.
	 */
	public String getFullViewButton() {
		return fullViewButton;
	}

	/**
	 * @param fullViewButton The fullViewButton to set.
	 */
	public void setFullViewButton(String fullViewButton) {
		this.fullViewButton = fullViewButton;
	}


	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public int getAmicusNumber() {
		return amicusNumber;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setAmicusNumber(int i) {
		amicusNumber = i;
	}

	/**
	 */
	public String getAddCopyButton() {
		return addCopyButton;
	}

	/**
	 */
	public String getCopiesButton() {
		return copiesButton;
	}

	/**
	 */
	public String getDeleteRecordButton() {
		return deleteRecordButton;
	}

	/**
	 */
	public String getEditButton() {
		return editButton;
	}

	/**
	 */
	public void setAddCopyButton(String string) {
		addCopyButton = string;
	}

	/**
	 */
	public void setCopiesButton(String string) {
		copiesButton = string;
	}

	/**
	 */
	public void setDeleteRecordButton(String string) {
		deleteRecordButton = string;
	}

	/**
	 */
	public void setEditButton(String string) {
		editButton = string;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public String getSelectedRecordNumber() {
		return selectedRecordNumber;
	}

	/**
	 * 
	 * 
	 * @param string
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setSelectedRecordNumber(String string) {
		selectedRecordNumber = string;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public String getRelationshipButton() {
		return relationshipButton;
	}

	/**
	 * 
	 * 
	 * @param string
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setRelationshipButton(String string) {
		relationshipButton = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getSortButton() {
		return sortButton;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getSortCriteria() {
		return sortCriteria;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSortButton(String string) {
		sortButton = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSortCriteria(int i) {
		sortCriteria = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getOpenURLButton() {
		return openURLButton;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setOpenURLButton(String string) {
		openURLButton = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getDisplayFormat() {
		return displayFormat;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setDisplayFormat(String string) {
		displayFormat = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getDisplayFormatButton() {
		return displayFormatButton;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setDisplayFormatButton(String string) {
		displayFormatButton = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getCancelButton() {
		return cancelButton;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setCancelButton(String string) {
		cancelButton = string;
	}

	public String getEditCollection() {
		return editCollection;
	}

	public void setEditCollection(String editCollection) {
		this.editCollection = editCollection;
	}

	public String getDuplicateButton() {
		return duplicateButton;
	}

	public void setDuplicateButton(String duplicateButton) {
		this.duplicateButton = duplicateButton;
	}

	public String getBackButton() {
		return backButton;
	}

	public void setBackButton(String backButton) {
		this.backButton = backButton;
	}

	public String getCleanupDeleteListButton() {
		return cleanupDeleteListButton;
	}

	public void setCleanupDeleteListButton(String cleanupDeleteListButton) {
		this.cleanupDeleteListButton = cleanupDeleteListButton;
	}

	public String getCleanupNTIListButton() {
		return cleanupNTIListButton;
	}

	public void setCleanupNTIListButton(String cleanupNTIListButton) {
		this.cleanupNTIListButton = cleanupNTIListButton;
	}

	public String getCleanupTransferListButton() {
		return cleanupTransferListButton;
	}

	public void setCleanupTransferListButton(String cleanupTransferListButton) {
		this.cleanupTransferListButton = cleanupTransferListButton;
	}

	public String getExecuteTransferButton() {
		return executeTransferButton;
	}

	public void setExecuteTransferButton(String executeTransferButton) {
		this.executeTransferButton = executeTransferButton;
	}

	public String getExecuteDeleteButton() {
		return executeDeleteButton;
	}

	public void setExecuteDeleteButton(String executeDeleteButton) {
		this.executeDeleteButton = executeDeleteButton;
	}

	public String getDeleteFromListButton() {
		return deleteFromListButton;
	}

	public void setDeleteFromListButton(String deleteFromListButton) {
		this.deleteFromListButton = deleteFromListButton;
	}

	public String getUncheckedNTIArray() {
		return uncheckedNTIArray;
	}

	public void setUncheckedNTIArray(String uncheckedNTIArray) {
		this.uncheckedNTIArray = uncheckedNTIArray;
	}

	public String getExecuteAllButton() {
		return executeAllButton;
	}

	public void setExecuteAllButton(String executeAllButton) {
		this.executeAllButton = executeAllButton;
	}

	public String getRecordToDeleteFromDeleteList() {
		return recordToDeleteFromDeleteList;
	}

	public void setRecordToDeleteFromDeleteList(String recordToDeleteFromDeleteList) {
		this.recordToDeleteFromDeleteList = recordToDeleteFromDeleteList;
	}

	public String getRecordToDeleteFromTransfList() {
		return recordToDeleteFromTransfList;
	}

	public void setRecordToDeleteFromTransfList(String recordToDeleteFromTransfList) {
		this.recordToDeleteFromTransfList = recordToDeleteFromTransfList;
	}

	public String getCheckUncheckDelete() {
		return checkUncheckDelete;
	}

	public void setCheckUncheckDelete(String checkUncheckDelete) {
		this.checkUncheckDelete = checkUncheckDelete;
	}

	public String getCheckUncheckNti() {
		return checkUncheckNti;
	}

	public void setCheckUncheckNti(String checkUncheckNti) {
		this.checkUncheckNti = checkUncheckNti;
	}

	public String getCheckUncheckTransf() {
		return checkUncheckTransf;
	}

	public void setCheckUncheckTransf(String checkUncheckTransf) {
		this.checkUncheckTransf = checkUncheckTransf;
	}

	public String getRecordToDeleteFromNTIList() {
		return recordToDeleteFromNTIList;
	}

	public void setRecordToDeleteFromNTIList(String recordToDeleteFromNTIList) {
		this.recordToDeleteFromNTIList = recordToDeleteFromNTIList;
	}

	public String getExecuteNTIButton() {
		return executeNTIButton;
	}

	public void setExecuteNTIButton(String executeNTIButton) {
		this.executeNTIButton = executeNTIButton;
	}

	public String getExecuteAllTransferButton() {
		return executeAllTransferButton;
	}

	public void setExecuteAllTransferButton(String executeAllTransferButton) {
		this.executeAllTransferButton = executeAllTransferButton;
	}

	public String getExecuteAllDeleteButton() {
		return executeAllDeleteButton;
	}

	public void setExecuteAllDeleteButton(String executeAllDeleteButton) {
		this.executeAllDeleteButton = executeAllDeleteButton;
	}

	public String getExecuteAllNTIButton() {
		return executeAllNTIButton;
	}

	public void setExecuteAllNTIButton(String executeAllNTIButton) {
		this.executeAllNTIButton = executeAllNTIButton;
	}

	public String getOpenNewSearchButton() {
		return openNewSearchButton;
	}

	public void setOpenNewSearchButton(String openNewSearchButton) {
		this.openNewSearchButton = openNewSearchButton;
	}

}
