package com.casalini.digital.form;

import javax.servlet.http.HttpServletRequest;

import librisuite.business.common.DateInputException;
import librisuite.form.LibrisuiteForm;

import org.apache.commons.validator.GenericValidator;

public class DigitalAmminForm extends LibrisuiteForm
{
	private static final long serialVersionUID = -5833468556798539025L;

	private String operation;
	private String operationCodeTable;
	private int policyIndex;
	private String typeEditor = null;
	
	private String buttonSave = null;
	private String buttonCancel = null;
	private String buttonClose = null;		
	private String buttonRefresh = null;
	
	private String fileName = null;
	private String fileSize = null;
	private String costoPagina = null;
	private String costoPaginaCurcy = null;
	private String costoRivista = null;
	private String costoRivistaCurcy = null;
	private String costoArticolo = null;
	private String costoArticoloCurcy = null;
	private String costoSezione = null;
	private String costoSezioneCurcy = null;
	private String costoCapitolo = null;
	private String costoCapitoloCurcy = null;
	private String pageTot = null;
	private String flagWebStatus = null;
	private String costoItem = null;
	private String costoItemCurcy = null;
	private String saleTypeId = null;
	private String itemTypeSale = null;
	private String codEditore = null;
	private String codEditoreBreve = null;
	private String denEditore = null;
	private String anno = null;
	private String volume = null;
	private String fascicolo = null;
	private String capitolo = null;
	private String PagineDa;
	private String PagineA;
	private String progressivo = null;
	private String flagAllegato = null;
	private String flagSupplemento = null;
	private String flagEsclusione = null;
    private String descrizione = null;
	private String flagStato = null;
	private String flagCopiaIncolla = null;
	private String opeStampa = null;
	private String numStampa = null;
	private String pdf;
	private String flagFullText = null;
	private Float priceList;
	private String priceListDate;
	private String lstPriceDtIni;
	private String lstPriceDtFin;
	private String lstDtCrtString;
	private String lstUser;
	private String lstNote;
	private String lstCurcy;
	private String lstType;	
	private String[] checkPolicies;
	private String[] policyCurcy;
	private String[] policyPrice;
	private String[] policyStamps;
	private String[] policyTotPrice;
	private String policyOnlineType;
	private String productCategory;	
	private String medraModifyCheck = null;

	private String flagReiteration;
	private String flagNTI;
	private String pageMin;
	private String pageMax;
	private String codeWeeklyConsignmentFirst;
	private String codeMonthlyConsignmentFirst;
	private String codeWeeklyConsignmentSecond;
	private String codeMonthlyConsignmentSecond;
//	private String workingCode;
	private String levelCard;
	private String statusDisponibilit;
//	private String note;
	
//	public String getNote() {
//		return note;
//	}
//
//	public void setNote(String note) {
//		this.note = note;
//	}
//
//	public String getWorkingCode() {
//		return workingCode;
//	}
//
//	public void setWorkingCode(String workingCode) {
//		this.workingCode = workingCode;
//	}

	public String getLevelCard() {
		return levelCard;
	}

	public void setLevelCard(String levelCard) {
		this.levelCard = levelCard;
	}

	public String getStatusDisponibilit() {
		return statusDisponibilit;
	}

	public void setStatusDisponibilit(String statusDisponibilit) {
		this.statusDisponibilit = statusDisponibilit;
	}

	public String getFlagReiteration() {
		return flagReiteration;
	}

	public void setFlagReiteration(String flagReiteration) {
		this.flagReiteration = flagReiteration;
	}

	public String getFlagNTI() {
		return flagNTI;
	}

	public void setFlagNTI(String flagNTI) {
		this.flagNTI = flagNTI;
	}

	public String getPageMin() {
		return pageMin;
	}

	public void setPageMin(String pageMin) {
		this.pageMin = pageMin;
	}

	public String getPageMax() {
		return pageMax;
	}

	public void setPageMax(String pageMax) {
		this.pageMax = pageMax;
	}

	public String getCodeWeeklyConsignmentFirst() {
		return codeWeeklyConsignmentFirst;
	}

	public void setCodeWeeklyConsignmentFirst(String codeWeeklyConsignmentFirst) {
		this.codeWeeklyConsignmentFirst = codeWeeklyConsignmentFirst;
	}

	public String getCodeMonthlyConsignmentFirst() {
		return codeMonthlyConsignmentFirst;
	}

	public void setCodeMonthlyConsignmentFirst(String codeMonthlyConsignmentFirst) {
		this.codeMonthlyConsignmentFirst = codeMonthlyConsignmentFirst;
	}

	public String getCodeWeeklyConsignmentSecond() {
		return codeWeeklyConsignmentSecond;
	}

	public void setCodeWeeklyConsignmentSecond(String codeWeeklyConsignmentSecond) {
		this.codeWeeklyConsignmentSecond = codeWeeklyConsignmentSecond;
	}

	public String getCodeMonthlyConsignmentSecond() {
		return codeMonthlyConsignmentSecond;
	}

	public void setCodeMonthlyConsignmentSecond(String codeMonthlyConsignmentSecond) {
		this.codeMonthlyConsignmentSecond = codeMonthlyConsignmentSecond;
	}

	public String getMedraModifyCheck() {
		return medraModifyCheck;
	}

	public void setMedraModifyCheck(String medraModifyCheck) {
		this.medraModifyCheck = medraModifyCheck;
	}

	public String getPolicyOnlineType() {
		return policyOnlineType;
	}

	public void setPolicyOnlineType(String policyOnlineType) {
		this.policyOnlineType = policyOnlineType;
	}

	public Float getPriceList() {
		return priceList;
	}

	public void setPriceList(Float priceList) {
		this.priceList = priceList;
	}

	public String getPriceListDate() {
		return priceListDate;
	}

	public void setPriceListDate(String priceListDate) {
		this.priceListDate = priceListDate;
	}

	public String getLstPriceDtIni() {
		return lstPriceDtIni;
	}

	public void setLstPriceDtIni(String lstPriceDtIni) {
		this.lstPriceDtIni = lstPriceDtIni;
	}

	public String getLstPriceDtFin() {
		return lstPriceDtFin;
	}

	public void setLstPriceDtFin(String lstPriceDtFin) {
		this.lstPriceDtFin = lstPriceDtFin;
	}

	public String getLstDtCrtString() {
		return lstDtCrtString;
	}

	public void setLstDtCrtString(String lstDtCrtString) {
		this.lstDtCrtString = lstDtCrtString;
	}
	
	public String getLstUser() {
		return lstUser;
	}

	public void setLstUser(String lstUser) {
		this.lstUser = lstUser;
	}

	public String getLstNote() {
		return lstNote;
	}

	public void setLstNote(String lstNote) {
		this.lstNote = lstNote;
	}

	public String getLstCurcy() {
		return lstCurcy;
	}

	public void setLstCurcy(String lstCurcy) {
		this.lstCurcy = lstCurcy;
	}

	public String getLstType() {
		return lstType;
	}

	public void setLstType(String lstType) {
		this.lstType = lstType;
	}

	public String getButtonRefresh() {
		return buttonRefresh;
	}

	public void setButtonRefresh(String buttonRefresh) {
		this.buttonRefresh = buttonRefresh;
	}

	public String[] getPolicyTotPrice() {
		return policyTotPrice;
	}

	public void setPolicyTotPrice(String[] policyTotPrice) {
		this.policyTotPrice = policyTotPrice;
	}

	public String[] getPolicyCurcy() {
		return policyCurcy;
	}

	public void setPolicyCurcy(String[] policyCurcy) {
		this.policyCurcy = policyCurcy;
	}

	public String[] getPolicyPrice() {
		return policyPrice;
	}

	public void setPolicyPrice(String[] policyPrice) {
		this.policyPrice = policyPrice;
	}

	public String[] getPolicyStamps() {
		return policyStamps;
	}

	public void setPolicyStamps(String[] policyStamps) {
		this.policyStamps = policyStamps;
	}

	public int getPolicyIndex() {
		return policyIndex;
	}

	public void setPolicyIndex(int policyIndex) {
		this.policyIndex = policyIndex;
	}
	
	public String getOperationCodeTable() {
		return operationCodeTable;
	}

	public void setOperationCodeTable(String operationCodeTable) {
		this.operationCodeTable = operationCodeTable;
	}

	public String getButtonSave() {
		return buttonSave;
	}

	public void setButtonSave(String buttonSave) {
		this.buttonSave = buttonSave;
	}

	public String getButtonCancel() {
		return buttonCancel;
	}

	public void setButtonCancel(String buttonCancel) {
		this.buttonCancel = buttonCancel;
	}

	public String getButtonClose() {
		return buttonClose;
	}

	public void setButtonClose(String buttonClose) {
		this.buttonClose = buttonClose;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFlagWebStatus() {
		return flagWebStatus;
	}

	public void setFlagWebStatus(String webStatus) {
		this.flagWebStatus = webStatus;
	}

	public String getSaleTypeId() {
		return saleTypeId;
	}

	public void setSaleTypeId(String saleTypeId) {
		this.saleTypeId = saleTypeId;
	}

	public String getItemTypeSale() {
		return itemTypeSale;
	}

	public void setItemTypeSale(String itemTypeSale) {
		this.itemTypeSale = itemTypeSale;
	}
	
//	public void validateForm() throws Exception
	public void validateForm() throws DateInputException
	{			
		if ((GenericValidator.isBlankOrNull(getPagineDa()))&&(!GenericValidator.isBlankOrNull(getPagineA())) || 
			(!GenericValidator.isBlankOrNull(getPagineDa()))&&(GenericValidator.isBlankOrNull(getPagineA())) )	{
//			  throw new Exception("error.pagineDaA");
			throw new DateInputException("error.pagineDaA");
			
		}
		
		if ((GenericValidator.isBlankOrNull(getPagineDa()))&&(GenericValidator.isBlankOrNull(getPagineA()))) {
			setPageTot(null);
		} else {
			if ((Integer.parseInt(getPagineDa())==0)|| (Integer.parseInt(getPagineA())==0)) {
//				throw new Exception("error.pagineDaA");
				throw new DateInputException("error.pagineDaA");
			}
			
			if ((!GenericValidator.isBlankOrNull(getPagineDa())) && (!GenericValidator.isBlankOrNull(getPagineA()))) {		
				int confronto = new Integer(getPagineDa()).compareTo(new Integer(getPagineA()));
				if (confronto > 0) {
//					throw new Exception("error.format.pagineDaA");
					throw new DateInputException("error.pagineDaA");
				}
				int pagDa = Integer.parseInt(getPagineDa());
				int pagA =  Integer.parseInt(getPagineA());
				int pagtot = (pagA - pagDa) + 1 ;
				setPageTot(Integer.toString(pagtot)); 
			}
		}
			
		if(!GenericValidator.isBlankOrNull(getLstPriceDtIni())){
		  if(!GenericValidator.isDate(getLstPriceDtIni(), "dd-MM-yyyy", true)){
			  throw new DateInputException("error.data.ini.lst");
		  }
		}
		
		if(!GenericValidator.isBlankOrNull(getLstPriceDtFin())){
		  if(!GenericValidator.isDate(getLstPriceDtFin(), "dd-MM-yyyy", true)){
			  throw new DateInputException("error.data.fin.lst");
		  }
		}
		
		if ("N".equalsIgnoreCase(getFlagWebStatus()) && "S".equalsIgnoreCase(getMedraModifyCheck())){
//			throw new Exception("error.dga.flags");
			throw new DateInputException("error.dga.flags");
		}
	}

	public String[] getCheckPolicies() {
		return checkPolicies;
	}

	public void setCheckPolicies(String[] checkPolicies) {
		this.checkPolicies = checkPolicies;
	}

	public String getCodEditore() {
		return codEditore;
	}

	public void setCodEditore(String codEditore) {
		this.codEditore = codEditore;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getFlagAllegato() {
		return flagAllegato;
	}

	public void setFlagAllegato(String flagAllegato) {
		this.flagAllegato = flagAllegato;
	}

	public String getFlagSupplemento() {
		return flagSupplemento;
	}

	public void setFlagSupplemento(String flagSupplemento) {
		this.flagSupplemento = flagSupplemento;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getFlagStato() {
		return flagStato;
	}

	public void setFlagStato(String flagStato) {
		this.flagStato = flagStato;
	}

	public String getFlagCopiaIncolla() {
		return flagCopiaIncolla;
	}

	public void setFlagCopiaIncolla(String flagCopiaIncolla) {
		this.flagCopiaIncolla = flagCopiaIncolla;
	}

	public String getOpeStampa() {
		return opeStampa;
	}

	public void setOpeStampa(String opeStampa) {
		this.opeStampa = opeStampa;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(String fascicolo) {
		this.fascicolo = fascicolo;
	}

	public String getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}

	public String getNumStampa() {
		return numStampa;
	}

	public void setNumStampa(String numStampa) {
		this.numStampa = numStampa;
	}

	public String getPageTot() {
		return pageTot;
	}

	public void setPageTot(String pageTot) {
		this.pageTot = pageTot;
	}

	public String getCostoPagina() {
		return costoPagina;
	}

	public void setCostoPagina(String costoPagina) {
		this.costoPagina = costoPagina;
	}

	public String getCostoRivista() {
		return costoRivista;
	}

	public void setCostoRivista(String costoRivista) {
		this.costoRivista = costoRivista;
	}

	public String getCostoArticolo() {
		return costoArticolo;
	}

	public void setCostoArticolo(String costoArticolo) {
		this.costoArticolo = costoArticolo;
	}

	public String getCostoSezione() {
		return costoSezione;
	}

	public void setCostoSezione(String costoSezione) {
		this.costoSezione = costoSezione;
	}

	public String getCostoItem() {
		return costoItem;
	}

	public void setCostoItem(String costoItem) {
		this.costoItem = costoItem;
	}

	public String getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	public String getCostoCapitolo() {
		return costoCapitolo;
	}

	public void setCostoCapitolo(String costoCapitolo) {
		this.costoCapitolo = costoCapitolo;
	}

	public String getCostoItemCurcy() {
		return costoItemCurcy;
	}

	public void setCostoItemCurcy(String costoItemCurcy) {
		this.costoItemCurcy = costoItemCurcy;
	}

	public String getCostoPaginaCurcy() {
		return costoPaginaCurcy;
	}

	public void setCostoPaginaCurcy(String costoPaginaCurcy) {
		this.costoPaginaCurcy = costoPaginaCurcy;
	}

	public String getCostoRivistaCurcy() {
		return costoRivistaCurcy;
	}

	public void setCostoRivistaCurcy(String costoRivistaCurcy) {
		this.costoRivistaCurcy = costoRivistaCurcy;
	}

	public String getCostoArticoloCurcy() {
		return costoArticoloCurcy;
	}

	public void setCostoArticoloCurcy(String costoArticoloCurcy) {
		this.costoArticoloCurcy = costoArticoloCurcy;
	}

	public String getCostoSezioneCurcy() {
		return costoSezioneCurcy;
	}

	public void setCostoSezioneCurcy(String costoSezioneCurcy) {
		this.costoSezioneCurcy = costoSezioneCurcy;
	}

	public String getCostoCapitoloCurcy() {
		return costoCapitoloCurcy;
	}

	public void setCostoCapitoloCurcy(String costoCapitoloCurcy) {
		this.costoCapitoloCurcy = costoCapitoloCurcy;
	}

	public String getDenEditore() {
		return denEditore;
	}

	public void setDenEditore(String denEditore) {
		this.denEditore = denEditore;
	}

	public String getPagineDa() {
		return PagineDa;
	}

	public void setPagineDa(String pagineDa) {
		PagineDa = pagineDa;
	}

	public String getPagineA() {
		return PagineA;
	}

	public void setPagineA(String pagineA) {
		PagineA = pagineA;
	}

	public String getCodEditoreBreve() {
		return codEditoreBreve;
	}

	public void setCodEditoreBreve(String codEditoreBreve) {
		this.codEditoreBreve = codEditoreBreve;
	}

	public String getTypeEditor() {
		return typeEditor;
	}

	public void setTypeEditor(String typeEditor) {
		this.typeEditor = typeEditor;
	}
	
	public String getFlagFullText() {
		return flagFullText;
	}

	public void setFlagFullText(String flagFullText) {
		this.flagFullText = flagFullText;
	}
	
	public String getProductCategory() {
		return productCategory;
	}
	
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	
	public String getFlagEsclusione() {
		return flagEsclusione;
	}

	public void setFlagEsclusione(String flagEsclusione) {
		this.flagEsclusione = flagEsclusione;
	}
}