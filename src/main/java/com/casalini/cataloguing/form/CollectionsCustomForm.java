package com.casalini.cataloguing.form;

import javax.servlet.http.HttpServletRequest;

import librisuite.form.LibrisuiteForm;

public class CollectionsCustomForm extends LibrisuiteForm {
		
	private static final long serialVersionUID = 8492750305332422563L;
	
	private String modifyButton;
	private String operation;
	private String statoPagina;
	private String[] checkCollection;
	private String searchButton;
	private String closeButton;
	private Integer selectIdCollection;
	private String saveButton;
	private String searchMSTButton;
    String searchNome;
	String searchId;
	String colonna;
	String status;
	private String openButton;
	private String okButton;
	private String deleteButton;
	private String visualButton;
	private String typologyCode;
//	20110221 inizio: bottone per gestione records per regole
	private String ruleButton;
	private String allRecordRuleButton;
//	20110221 fine
	
	public String getAllRecordRuleButton() {
		return allRecordRuleButton;
	}
	
	public void setAllRecordRuleButton(String allRecordRuleButton) {
		this.allRecordRuleButton = allRecordRuleButton;
	}
	
	public String getRuleButton() {
		return ruleButton;
	}

	public void setRuleButton(String ruleButton) {
		this.ruleButton = ruleButton;
	}
	
	public String getTypologyCode() {
		return typologyCode;
	}

	public void setTypologyCode(String typologyCode) {
		this.typologyCode = typologyCode;
	}

	public String getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(String deleteButton) {
		this.deleteButton = deleteButton;
	}

	public String getStatoPagina() {
		return statoPagina;
	}

	public void setStatoPagina(String statoPagina) {
		this.statoPagina = statoPagina;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getColonna() {
		return colonna;
	}

	public void setColonna(String colonna) {
		this.colonna = colonna;
	}


	public String getSearchMSTButton() {
		return searchMSTButton;
	}

	public void setSearchMSTButton(String searchMSTButton) {
		this.searchMSTButton = searchMSTButton;
	}

	public String getSearchNome() {
		return searchNome;
	}

	public void setSearchNome(String searchNome) {
		this.searchNome = searchNome;
	}


	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}

	public String getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(String saveButton) {
		this.saveButton = saveButton;
	}

	public String getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(String closeButton) {
		this.closeButton = closeButton;
	}

	public String getModifyButton() {
		return modifyButton;
	}

	public void setModifyButton(String modifyButton) {
		this.modifyButton = modifyButton;
	}
	public String getSearchButton() {
		return searchButton;
	}

	public void setSearchButton(String searchButton) {
		this.searchButton = searchButton;
	}

	public String[] getCheckCollection() {
		return checkCollection;
	}

	public void setCheckCollection(String[] checkCollection) {
		this.checkCollection = checkCollection;
	}
	
  
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}


	public Integer getSelectIdCollection() {
		return selectIdCollection;
	}

	public void setSelectIdCollection(Integer selectIdCollection) {
		this.selectIdCollection = selectIdCollection;
	}

	public String getOpenButton() {
		return openButton;
	}

	public void setOpenButton(String openButton) {
		this.openButton = openButton;
	}

	public String getOkButton() {
		return okButton;
	}

	public void setOkButton(String okButton) {
		this.okButton = okButton;
	}

	public String getVisualButton() {
		return visualButton;
	}

	public void setVisualButton(String visualButton) {
		this.visualButton = visualButton;
	}
}
