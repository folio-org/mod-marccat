package com.casalini.cataloguing.form;

import javax.servlet.http.HttpServletRequest;

import librisuite.form.LibrisuiteForm;


public class CollectionsMasterForm extends LibrisuiteForm 
{		
	private static final long serialVersionUID = 383375516391523666L;

	private String modifyButton;
	private String operation; 
	private String statoPagina;
	private String[] checkCollection;
	private String searchButton;
	private String closeButton;
	private Integer selectIdCollection;
	private String saveButton;
	private String searchMSTButton;
    private String searchNome;
	private String searchId;
	private String colonna;
	private String status;
    private String copyButton;
	private String openButton;
	private String okButton;
	private String selectMotherButton;
	private String selectChildButton;
	private String deleteButton;
	private String visualButton;
	private String typologyCode;
	
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

	public String getSelectChildButton() {
		return selectChildButton;
	}

	public void setSelectChildButton(String selectChildButton) {
		this.selectChildButton = selectChildButton;
	}

	public String getSelectMotherButton() {
		return selectMotherButton;
	}

	public void setSelectMotherButton(String selectMotherButton) {
		this.selectMotherButton = selectMotherButton;
	}

	public String getCopyButton() {
		return copyButton;
	}

	public void setCopyButton(String copyButton) {
		this.copyButton = copyButton;
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
