package org.folio.cataloging.form.searching;

import org.folio.cataloging.form.LibrisuiteForm;

public class ResultSummaryForm extends LibrisuiteForm 
{
	private static final long serialVersionUID = -4772480191097671966L;

	private String cclQuery = null;
	private String newSearchButton = null;
	private String newSearchCollButton = null;
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
	private String relationshipSaveTagButton = null;
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
	/* modifica barbara 27/04/2007 prn 137 - */
	private String closeButton = null;
	private String duplicateButton = null;
	private String equivalentButton = null;
	private String language = null;
	private String languageButton = null;
	private String qualityButton = null;
	private String managementButton = null;
	private String motherButton = null;
	private String cancelMotherButton = null;
	private String hierarchuButton = null;
	private String formatRecordType = null;
	private String changeFormatRecordType = null;
	private String otherFormatButton = null;
	private String pdfBookmarksButton = null;
	private String tabType;
	private String showRDAButton;
	private String serialsControlButton;

	// visualizzazione recor mades
	private int pageNumberSimpleButton = 0;
	private String nextPageSimpleButton = null;
	private String previousPageSimpleButton = null;
	private String closeSimpleButton = null;
	private String firstPageSimpleButton = null;
	private String lastPageSimpleButton = null;
	private String selectedRecordforHeldTransfert = null;	
	private String cancelforHeldTransfert = null;
	private String discardedButton = null;
	
	private String[] checkRecordForCart = null;
	private String addToCartButton = null;
	private String addToCartButtonByRecord = null;
	private String checkAll = null;

	public String getCheckAll() {
		return checkAll;
	}

	public void setCheckAll(String checkAll) {
		this.checkAll = checkAll;
	}

	public String getAddToCartButtonByRecord() {
		return addToCartButtonByRecord;
	}

	public void setAddToCartButtonByRecord(String addToCartButtonByRecord) {
		this.addToCartButtonByRecord = addToCartButtonByRecord;
	}

	public String[] getCheckRecordForCart() {
		return checkRecordForCart;
	}

	public void setCheckRecordForCart(String[] checkRecordForCart) {
		this.checkRecordForCart = checkRecordForCart;
	}
	
	public String getCancelforHeldTransfert() {
		return cancelforHeldTransfert;
	}

	public void setCancelforHeldTransfert(String cancelforHeldTransfert) {
		this.cancelforHeldTransfert = cancelforHeldTransfert;
	}

	public void setSelectedRecordforHeldTransfert(String selectedRecordforHeldTransfert) {
		this.selectedRecordforHeldTransfert = selectedRecordforHeldTransfert;
	}
	
	public String getSelectedRecordforHeldTransfert() {
		return selectedRecordforHeldTransfert;
	}

	public String getPdfBookmarksButton() {
		return pdfBookmarksButton;
	}

	public void setPdfBookmarksButton(String pdfBookmarksButton) {
		this.pdfBookmarksButton = pdfBookmarksButton;
	}

	public String getChangeFormatRecordType() {
		return changeFormatRecordType;
	}

	public void setChangeFormatRecordType(String changeFormatRecordType) {
		this.changeFormatRecordType = changeFormatRecordType;
	}

	public String getOtherFormatButton() {
		return otherFormatButton;
	}

	public void setOtherFormatButton(String otherFormatButton) {
		this.otherFormatButton = otherFormatButton;
	}

	public String getFormatRecordType() {
		return formatRecordType;
	}

	public void setFormatRecordType(String formatRecord) {
		this.formatRecordType = formatRecord;
	}

	public String getNewSearchCollButton() {
		return newSearchCollButton;
	}

	public void setNewSearchCollButton(String newSearchCollButton) {
		this.newSearchCollButton = newSearchCollButton;
	}

	public String getCloseSimpleButton() {
		return closeSimpleButton;
	}

	public void setCloseSimpleButton(String closeSimpleButton) {
		this.closeSimpleButton = closeSimpleButton;
	}

	public int getPageNumberSimpleButton() {
		return pageNumberSimpleButton;
	}

	public void setPageNumberSimpleButton(int pageNumberSimpleButton) {
		this.pageNumberSimpleButton = pageNumberSimpleButton;
	}

	public String getPreviousPageSimpleButton() {
		return previousPageSimpleButton;
	}

	public void setPreviousPageSimpleButton(String previousPageSimpleButton) {
		this.previousPageSimpleButton = previousPageSimpleButton;
	}

	public String getNextPageSimpleButton() {
		return nextPageSimpleButton;
	}

	public void setNextPageBiBlioButton(String nextPageSimpleButton) {
		this.nextPageSimpleButton = nextPageSimpleButton;
	}

	public String getHierarchuButton() {
		return hierarchuButton;
	}

	public void setHierarchuButton(String hierarchuButton) {
		this.hierarchuButton = hierarchuButton;
	}

	public String getLanguageButton() {
		return languageButton;
	}

	public void setLanguageButton(String languageButton) {
		this.languageButton = languageButton;
	}

	public String getlanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getEquivalentButton() {
		return equivalentButton;
	}

	public void setEquivalentButton(String equivalentButton) {
		this.equivalentButton = equivalentButton;
	}

	public String getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(String string) {
		closeButton = string;
	}

	/* modifica Natascia 11/06/2007 - prn 198 */
	private String closeButtonCopies = null;

	public String getCloseButtonCopies() {
		return closeButtonCopies;
	}

	public void setCloseButtonCopies(String string) {
		closeButtonCopies = string;
	}

	public String getCclQuery() {
		return cclQuery;
	}

	public String getNewSearchButton() {
		return newSearchButton;
	}

	public String getNextPageButton() {
		return nextPageButton;
	}

	public int getPageNumberButton() {
		return pageNumberButton;
	}

	public String getPreviousPageButton() {
		return previousPageButton;
	}

	public String getPrintResultsButton() {
		return printResultsButton;
	}

	public int getShowNumberOfRecords() {
		return showNumberOfRecords;
	}

	public String getShowNumberOfRecordsButton() {
		return showNumberOfRecordsButton;
	}

	public void setCclQuery(String string) {
		cclQuery = string;
	}

	public void setNewSearchButton(String string) {
		newSearchButton = string;
	}

	public void setNextPageButton(String string) {
		nextPageButton = string;
	}

	public void setPageNumberButton(int i) {
		pageNumberButton = i;
	}

	public void setPreviousPageButton(String string) {
		previousPageButton = string;
	}

	public void setPrintResultsButton(String string) {
		printResultsButton = string;
	}

	public void setShowNumberOfRecords(int i) {
		showNumberOfRecords = i;
	}

	public void setShowNumberOfRecordsButton(String string) {
		showNumberOfRecordsButton = string;
	}

	public String getBriefViewButton() {
		return briefViewButton;
	}

	public void setBriefViewButton(String briefViewButton) {
		this.briefViewButton = briefViewButton;
	}

	public String getFullViewButton() {
		return fullViewButton;
	}

	public void setFullViewButton(String fullViewButton) {
		this.fullViewButton = fullViewButton;
	}

	public int getAmicusNumber() {
		return amicusNumber;
	}

	public void setAmicusNumber(int i) {
		amicusNumber = i;
	}

	public String getAddCopyButton() {
		return addCopyButton;
	}

	public String getCopiesButton() {
		return copiesButton;
	}

	public String getDeleteRecordButton() {
		return deleteRecordButton;
	}

	public String getEditButton() {
		return editButton;
	}

	public void setAddCopyButton(String string) {
		addCopyButton = string;
	}

	public void setCopiesButton(String string) {
		copiesButton = string;
	}

	public void setDeleteRecordButton(String string) {
		deleteRecordButton = string;
	}

	public void setEditButton(String string) {
		editButton = string;
	}

	public String getSelectedRecordNumber() {
		return selectedRecordNumber;
	}

	public void setSelectedRecordNumber(String string) {
		selectedRecordNumber = string;
	}

	public String getRelationshipButton() {
		return relationshipButton;
	}

	public void setRelationshipButton(String string) {
		relationshipButton = string;
	}

	public String getSortButton() {
		return sortButton;
	}

	public int getSortCriteria() {
		return sortCriteria;
	}

	public void setSortButton(String string) {
		sortButton = string;
	}

	public void setSortCriteria(int i) {
		sortCriteria = i;
	}

	public String getOpenURLButton() {
		return openURLButton;
	}

	public void setOpenURLButton(String string) {
		openURLButton = string;
	}

	public String getDisplayFormat() {
		return displayFormat;
	}

	public void setDisplayFormat(String string) {
		displayFormat = string;
	}

	public String getDisplayFormatButton() {
		return displayFormatButton;
	}

	public void setDisplayFormatButton(String string) {
		displayFormatButton = string;
	}

	public String getCancelButton() {
		return cancelButton;
	}

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

	public String getQualityButton() {
		return qualityButton;
	}

	public void setQualityButton(String qualityButton) {
		this.qualityButton = qualityButton;
	}

	public String getManagementButton() {
		return managementButton;
	}

	public void setManagementButton(String managementButton) {
		this.managementButton = managementButton;
	}

	public String getCancelMotherButton() {
		return cancelMotherButton;
	}

	public void setCancelMotherButton(String cancelMotherButton) {
		this.cancelMotherButton = cancelMotherButton;
	}

	public String getMotherButton() {
		return motherButton;
	}

	public void setMotherButton(String motherButton) {
		this.motherButton = motherButton;
	}

	public String getSelectionTableTypeSlect() {
		return tabType;
	}

	public void setSelectionTableTypeSlect(String selectionTableTypeSlect) {
		this.tabType = selectionTableTypeSlect;
	}

	public void setNextPageSimpleButton(String nextPageSimpleButton) {
		this.nextPageSimpleButton = nextPageSimpleButton;
	}

	public String getFirstPageSimpleButton() {
		return firstPageSimpleButton;
	}

	public void setFirstPageSimpleButton(String firstPageSimpleButton) {
		this.firstPageSimpleButton = firstPageSimpleButton;
	}

	public String getLastPageSimpleButton() {
		return lastPageSimpleButton;
	}

	public void setLastPageSimpleButton(String lastPageSimpleButton) {
		this.lastPageSimpleButton = lastPageSimpleButton;
	}
	
	private String showVariantsButton = null; //pm 2011

	public String getShowVariantsButton() {
		return showVariantsButton;
	}

	public void setShowVariantsButton(String showVariantsButton) {
		this.showVariantsButton = showVariantsButton;
	}
	public String getRelationshipSaveTagButton() {
		return relationshipSaveTagButton;
	}

	public void setRelationshipSaveTagButton(String relationshipSaveTagButton) {
		this.relationshipSaveTagButton = relationshipSaveTagButton;
	}

	public String getShowRDAButton() {
		return showRDAButton;
	}

	public void setShowRDAButton(String showRDAButton) {
		this.showRDAButton = showRDAButton;
	}

	public String getSerialsControlButton() {
		return serialsControlButton;
	}

	public void setSerialsControlButton(String serialsControlButton) {
		this.serialsControlButton = serialsControlButton;
	}

	public String getAddToCartButton() {
		return addToCartButton;
	}

	public void setAddToCartButton(String addToCartButton) {
		this.addToCartButton = addToCartButton;
	}

	public String getDiscardedButton() {
		return discardedButton;
	}

	public void setDiscardedButton(String discardedButton) {
		this.discardedButton = discardedButton;
	}
}