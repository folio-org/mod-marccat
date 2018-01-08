package org.folio.cataloging.bean.digital;

import org.apache.commons.httpclient.HttpException;
import org.folio.cataloging.bean.LibrisuiteBean;
import org.folio.cataloging.bean.cas.CasaliniCodeListsBean;
import org.folio.cataloging.bean.cas.CasaliniContextBean;
import org.folio.cataloging.bean.cas.EditCasCacheBean;
import org.folio.cataloging.bean.cataloguing.bibliographic.BibliographicEditBean;
import org.folio.cataloging.bean.cataloguing.bibliographic.codelist.DigitalSalType;
import org.folio.cataloging.bean.cataloguing.bibliographic.codelist.OnlinePolicyType;
import org.folio.cataloging.bean.cataloguing.common.EditBean;
import org.folio.cataloging.business.authorisation.AuthorisationException;
import org.folio.cataloging.business.cataloguing.bibliographic.*;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.CorrelationValues;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.DateInputException;
import org.folio.cataloging.business.controller.SessionUtils;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.business.digital.DigitalDoiException;
import org.folio.cataloging.business.digital.DigitalLevelException;
import org.folio.cataloging.business.digital.PermalinkException;
import org.folio.cataloging.business.digital.RequiredFieldsException;
import org.folio.cataloging.dao.DAOCasDigAdmin;
import org.folio.cataloging.dao.DAOCodeTable;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.exception.DuplicateTagException;
import org.folio.cataloging.exception.RecordInUseException;
import org.folio.cataloging.exception.ValidationException;
import org.folio.cataloging.form.digital.DigitalAmminForm;
import org.folio.cataloging.util.StringText;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.*;
import java.util.*;

@SuppressWarnings("unchecked")
public class DigitalAmminBean extends LibrisuiteBean  
{	 
	private int bibNumber; 
	private String autore;
	private String titolo;
	private String descrTag008;
	private String typeTag008;
	private CasDigAdmin currentItem;
	private CasSapPubl currentEditor;
	private List items = new ArrayList();
	private List listPolicies = new ArrayList();
	private List listSapPolicies = new ArrayList();
	private static DigitalSalType digitalSalType = new DigitalSalType();
	private static OnlinePolicyType onlinePolicyType = new OnlinePolicyType();
	private List onlinePolicyList = null;
	private List saleTypeList = null;
	private HashMap listTag59;
	private boolean existItem;	
	private boolean tag365Disable = true;
	
	private String[] policyCode;
	private String[] policyName;
	private String[] policyType;
	private String[] policyCurcy;
	private String[] policyPrice;
	private String[] policyStamps;
	private List listPoliciesToInsert = new ArrayList();
	private String[] policyTotPrice;
	/* 20101010: aggiunto flag per proteggere l'editore */
	private boolean publisherDisable = true;
	private boolean saveRecord = false;
	private String publisher998;
	private String medraModifyCheck;
	
	/* Campi della S_CAS_CACHE */
	private String flagReiteration;
	private String flagNTI;
	private Integer pageMin;
	private Integer pageMax;
	private String codeWeeklyConsignmentFirst;
	private String codeMonthlyConsignmentFirst;
	private String codeWeeklyConsignmentSecond;
	private String codeMonthlyConsignmentSecond;
	private String workingCode;
	private String levelCard;
	private Integer statusDisponibilit;
	private String note;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List getStatusAvailableList(){
		return CasaliniCodeListsBean.getStatusAvailable().getCodeList(getLocale());
	}
	
	public List getManagerialLevelList() {
		return CasaliniCodeListsBean.getManagerialLevelType().getCodeList(getLocale());
	}
	
//	public List getWorkingCodeList() {
//		return CasaliniCodeListsBean.getWorkingCodeType().getCodeList(getLocale());
//	}
	
	public String getWorkingCode() {
		return workingCode;
	}

	public void setWorkingCode(String workingCode) {
		this.workingCode = workingCode;
	}

	public String getLevelCard() {
		return levelCard;
	}

	public void setLevelCard(String levelCard) {
		this.levelCard = levelCard;
	}

	public Integer getStatusDisponibilit() {
		return statusDisponibilit;
	}

	public void setStatusDisponibilit(Integer statusDisponibilit) {
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

	public Integer getPageMin() {
		return pageMin;
	}

	public void setPageMin(Integer pageMin) {
		this.pageMin = pageMin;
	}

	public Integer getPageMax() {
		return pageMax;
	}

	public void setPageMax(Integer pageMax) {
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

	public String getPublisher998() {
		return publisher998;
	}

	public void setPublisher998(String publisher998) {
		this.publisher998 = publisher998;
	}

	public boolean isSaveRecord() {
		return saveRecord;
	}

	public void setSaveRecord(boolean saveRecord) {
		this.saveRecord = saveRecord;
	}

	public boolean isPublisherDisable() {
		return publisherDisable;
	}

	public void setPublisherDisable(boolean publisherDisable) {
		this.publisherDisable = publisherDisable;
	}

	public boolean isTag365Disable() {
		return tag365Disable;
	}

	public void setTag365Disable(boolean tag365Disable) {
		this.tag365Disable = tag365Disable;
	}

	public List getListPoliciesToInsert() {
		return listPoliciesToInsert;
	}

	public void setListPoliciesToInsert(List listPoliciesToInsert) {
		this.listPoliciesToInsert = listPoliciesToInsert;
	}

	public String[] getPolicyTotPrice() {
		return policyTotPrice;
	}

	public void setPolicyTotPrice(String[] policyTotPrice) {
		this.policyTotPrice = policyTotPrice;
	}

	public String[] getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String[] policyName) {
		this.policyName = policyName;
	}

	public String[] getPolicyCode() {
		return policyCode;
	}

	public void setPolicyCode(String[] policyCode) {
		this.policyCode = policyCode;
	}

	public String[] getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String[] policyType) {
		this.policyType = policyType;
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
	private Locale locale;

	public List getListSapPolicies() {
		return listSapPolicies;
	}

	public void setListSapPolicies(List list) {
		this.listSapPolicies = list;
	}
	
	public List getListPolicies() {
		return listPolicies;
	}

	public void setListPolicies(List list) {
		this.listPolicies = list;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getTypeTag008() {
		return typeTag008;
	}

	public void setTypeTag008(String typeTag008) {
		this.typeTag008 = typeTag008;
	}

	public String getDescrTag008() {
		return descrTag008;
	}

	public void setDescrTag008(String descrTag008) {
		this.descrTag008 = descrTag008;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public List getdigitalCurcyTypeList() throws DataAccessException{
		return CasaliniCodeListsBean.getDigitalCurcyType().getCodeList(getLocale());
	}
	
	public List getdigitalSalTypeList() throws DataAccessException 
	{
		/* Per questa tendina bisogna concatenare alla descrizione anche il codice (tra parentesi) */
		saleTypeList = new ArrayList();
		List listSaleTypeDb = DAOCodeTable.asOptionList(new DAOCodeTable().getList(T_CAS_SAL_TYP.class,false), getLocale());
		Avp elemDb = null;
		Avp elemNew = null;
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < listSaleTypeDb.size(); i++) {
			elemDb = (Avp)listSaleTypeDb.get(i);
			elemNew = new Avp(elemDb.getValue(), buffer.append(elemDb.getLabel() + " (" + elemDb.getValue()+ ")").toString());
			saleTypeList.add(elemNew);
			buffer.delete(0, buffer.length());
		}
		return saleTypeList;
	}
	
	public List getdigitalItemSalTypeList() {
		return CasaliniCodeListsBean.getDigitalItemSalType().getCodeList(getLocale());
	}
	
	public List getOnlinePolicyTypeList() throws DataAccessException
	{
		/* Per questa tendina bisogna concatenare alla descrizione anche il codice (tra parentesi) */
		onlinePolicyList = new ArrayList();
		List listPolicyDb = onlinePolicyType.getCodeList(getLocale());
		Avp elemDb = null;
		Avp elemNew = null;
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < listPolicyDb.size(); i++) {
			elemDb = (Avp)listPolicyDb.get(i);
			elemNew = new Avp(elemDb.getValue(), buffer.append(elemDb.getLabel() + " (" + elemDb.getValue()+ ")").toString());
			onlinePolicyList.add(elemNew);
			buffer.delete(0, buffer.length());
		}
		return onlinePolicyList;
	}
	
	public List getItems() {
		return items;
	}

	public void setItems(List items) {
		this.items = items;
	}

	public CasDigAdmin getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(CasDigAdmin currentItem) {
		this.currentItem = currentItem;
	}

	public int getBibNumber() {
		return bibNumber;
	}

	public void setBibNumber(int bibNumber) {
		this.bibNumber = bibNumber;
	}

	public static DigitalAmminBean getInstance(HttpServletRequest request)throws DataAccessException 
	{
		DigitalAmminBean bean = (DigitalAmminBean) DigitalAmminBean.getSessionAttribute(request, DigitalAmminBean.class);
		if (bean == null) {
			bean = new DigitalAmminBean();
			bean.setSessionAttribute(request, bean.getClass());	
			bean.setLocale(SessionUtils.getCurrentLocale(request));
		}
		return bean;
	}
	
	public void findEditor(String editor, boolean isPresentFruition) throws DataAccessException 
	{
		DAOCasDigAdmin dao = new DAOCasDigAdmin();
		setItems(dao.loadCasSapPubl(editor));
		if (items.size() == 0) {
			CasSapPubl newItem = new CasSapPubl(editor);
			getItems().add(newItem);
			setCurrentEditor(newItem);
		}
		setCurrentEditor((CasSapPubl) items.get(items.size() - 1));
		if(isPresentFruition)
		   dao.loadPolicyAndFruitionType(getCurrentEditor().getCodEditore(),this.getCurrentItem());
	}
	
	public void findEditorBreve(String editorBreve, boolean isPresentFruition) throws DataAccessException 
	{
		DAOCasDigAdmin dao = new DAOCasDigAdmin();
		setItems(dao.loadCasSapPublBreve(editorBreve));
		if (items.size() == 0) {
			CasSapPubl newItem = new CasSapPubl();
			getItems().add(newItem);
			setCurrentEditor(newItem);
		}
		setCurrentEditor((CasSapPubl) items.get(items.size() - 1));
		if(isPresentFruition)
		  dao.loadPolicyAndFruitionType(getCurrentEditor().getCodEditore(),this.getCurrentItem());
	}
	
	public void loadItems(int bibNumber)throws DataAccessException 
	{
		setBibNumber(bibNumber);
		DAOCasDigAdmin dao = new DAOCasDigAdmin();
		setItems(dao.loadCasDigAdmin(bibNumber));
		setExistItem(true);
		if (items.size() == 0) {
			setExistItem(false);
			addNew();
		}
		setCurrentItem((CasDigAdmin) items.get(items.size() - 1));
	}

	public void addNew() throws DataAccessException 
	{
		CasDigAdmin newItem = new CasDigAdmin(getBibNumber());
		getItems().add(newItem);
		setCurrentItem(newItem);
	}
	
	public void save() throws DataAccessException 
	{
		CasDigAdmin item = getCurrentItem();
		item.markChanged();
		item.getDAO().persistByStatus(item);
	}
	
	public void deleteAllPolicy(int amicusNumber) throws DataAccessException
	{ 
		DAOCasDigAdmin dao = new DAOCasDigAdmin();
		dao.deleteCasDgaPolicyByBibNumber(amicusNumber);
	}
	
	public void saveAllPolicy(List listPolicies, int amicusNumber) throws DataAccessException 
	{
		deleteAllPolicy(amicusNumber);
		
		CasDgaPolicy casDgaPolicies = null;
		for (int i = 0; i < getListPoliciesToInsert().size(); i++) {
			casDgaPolicies  = (CasDgaPolicy)getListPoliciesToInsert().get(i);
			casDgaPolicies.getDAO().persistByStatus(casDgaPolicies);
		}
		
//---->	Refresh lista
		setListPoliciesToInsert(new ArrayList());
	}
	
	public void saveOrUpdate() throws DataAccessException 
	{
		CasDigAdmin item = getCurrentItem();
		item.getDAO().persistByStatus(item);
	}
	
	public void delete() throws DataAccessException 
	{
		CasDigAdmin item = getCurrentItem();
		if (!item.isNew()) {
			item.markDeleted();
			item.getDAO().persistByStatus(item);
		}
	}

	public String getAutore() {
		return autore;
	}

	public void setAutore(String autore) {
		this.autore = autore;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	
	public void setDataOfRecord(EditBean editBean) throws MarcCorrelationException, DataAccessException 
	{
		setTitolo(getTitleFromtag245(editBean));
		
		setAutore(getAuthorFromCatalogItem(editBean));	
		
		setDescrTag008(getDescrTag008(editBean));
	}
	/**
	 * Metodo che prende l'autore del tag 245
	 * @param editBean
	 * @return titolo
	 * @throws DataAccessException
	 */
	
	public String getTitleFromtag245(EditBean editBean) throws DataAccessException
	{
		String title = new String("");
		TitleAccessPoint t245 = (TitleAccessPoint)editBean.getCatalogItem().findFirstTagByNumber("245");
		if (t245 != null) {
			title = t245.getDescriptor().getDisplayText() + " " + t245.getAccessPointStringText().getDisplayText();
		}
		return title;
	}
	
	/**
	 * Metodo che prende il titolo scorrendo i tag di tipo NameAccessPoint
	 * @param editBean
	 * @return autore 
	 * @throws DataAccessException
	 */
	public String getAuthorFromCatalogItem(EditBean editBean) throws DataAccessException
	{
		String author = new String("");
		List appo = ((BibliographicItem)editBean.getCatalogItem()).getOrderableNames();
		Iterator it = appo.iterator();
		while (it.hasNext()) {
			NameAccessPoint name = (NameAccessPoint) it.next();
			author = author.concat(name.getDescriptor().getDisplayText());
			if (it.hasNext()) {
				author = author.concat(" ; ");
			}
		}
		return author;	
	}
	
	/**
	 * Metodo che dal tag008 imposta il tipo e ritorna la descrizione del formato specifico scelto
	 * 
	 * @param editBean
	 * @return descrizione del tag008
	 * @throws DataAccessException
	 */
	public String getDescrTag008(EditBean editBean) throws DataAccessException
	{
		DAOCodeTable daoCodeTable = new DAOCodeTable();
		String text = new String();
		String type = new String();
		short category;
		MaterialDescription t008 = (MaterialDescription) editBean.getCatalogItem().findFirstTagByNumber("008");
		if (t008==null){
			return "";
		}
		
		if (t008.isBook()) {
			category = 31;
			type=daoCodeTable.getLongText(category, T_BIB_HDR.class, locale);
			for (int i = 0; i < t008.getNatureOfContentsCode().length(); i++) {
				if (!(String.valueOf(t008.getNatureOfContentsChar()[i]).equalsIgnoreCase(" "))) {
					text = text + " " + daoCodeTable.getLongText(t008.getNatureOfContentsChar()[i], T_NTR_OF_CNTNT.class, locale);
				}
			}
			
		} else if (t008.isComputerFile()) {
			category = 32;
			type=daoCodeTable.getLongText(category, T_BIB_HDR.class, locale);
			text = daoCodeTable.getLongText(t008.getComputerFileTypeCode().charValue(), T_CMPTR_FIL_TYP.class, locale);
			
		} else if (t008.isMap()) {
			category = 33;
			type=daoCodeTable.getLongText(category, T_BIB_HDR.class, locale);
			for (int i = 0; i < t008.getCartographicFormatCode().length(); i++) {
				text = text + " " + daoCodeTable.getLongText(t008.getCartographicFormatChar()[i], T_CRTGC_FRMT.class, locale);
			}
			
		} else if (t008.isMixedMaterial()) {
			category = 34;
			type=daoCodeTable.getLongText(category, T_BIB_HDR.class, locale);
			text = "";
			
		} else if (t008.isMusic()) {
			category = 35;
			type=daoCodeTable.getLongText(category, T_BIB_HDR.class, locale);
			text = daoCodeTable.getLongText(t008.getMusicFormOfCompositionCode(), T_MSC_FORM_OR_TYP.class, locale);
			
		} else if (t008.isSerial()) {
			category = 36;
			type=daoCodeTable.getLongText(category, T_BIB_HDR.class, locale);
			text = daoCodeTable.getLongText(t008.getSerialTypeCode().charValue(), T_SRL_TYP.class, locale);
			for (int i = 0; i < t008.getNatureOfContentsCode().length(); i++) {
				if (!(String.valueOf(t008.getNatureOfContentsChar()[i]).equalsIgnoreCase(" "))) {
					text = text + " " + daoCodeTable.getLongText(t008.getNatureOfContentsChar()[i], T_NTR_OF_CNTNT.class, locale);
				}
			}
			
		} else if (t008.isVisualMaterial()) {
			category = 37;
			type = daoCodeTable.getLongText(category, T_BIB_HDR.class, locale);
			text = daoCodeTable.getLongText(t008.getVisualMaterialTypeCode().charValue(), T_VSL_MTRL_TYP.class, locale);
		}
		
		if (type.trim().length()>4){
			type=type.substring(4);
		}
		setTypeTag008(type);
		
		return text;
	}
	
	public void impostaEditoreSap() 
	{
		getCurrentItem().setCodEditore(getCurrentEditor().getCodEditore());
		getCurrentItem().setCodEditoreBreve(getCurrentEditor().getCodEditoreBreve());
		
		getCurrentItem().setDenEditore(getCurrentEditor().getDenEditore());
		getCurrentItem().setFlagCopiaIncolla(getCurrentEditor().getFlagCopiaIncollaEdi());
		getCurrentItem().setOpeStampa(getCurrentEditor().getFlagOperStampaEdi());
		
		if (getCurrentEditor().getNumOperStampaEdi()!=null)
			getCurrentItem().setNumStampa(getCurrentEditor().getNumOperStampaEdi().toString());	
			
		if (getCurrentEditor().getFlagFullText()!=null)
			getCurrentItem().setFlagFullText(getCurrentEditor().getFlagFullText());	
		
		getCurrentItem().setCostoArticolo(getCurrentEditor().getCostoArticoloEdi());
		getCurrentItem().setCostoPagina(getCurrentEditor().getCostoPagEdi());
		getCurrentItem().setCostoSezione(getCurrentEditor().getCostoSezioneEdi());
		getCurrentItem().setCostoRivista(getCurrentEditor().getCostoRivistaEdi());
		getCurrentItem().setCostoCapitolo(getCurrentEditor().getCostoCapitoloEdi());

		if ((getCurrentEditor().getCostoArticoloCurcyEdi()!=null)&& (getCurrentEditor().getCostoArticoloCurcyEdi().trim().length()>0) )
			getCurrentItem().setCostoArticoloCurcy(getCurrentEditor().getCostoArticoloCurcyEdi());
			
		if ((getCurrentEditor().getCostoPagCurcyEdi()!=null)&& (getCurrentEditor().getCostoPagCurcyEdi().trim().length()>0) )
			getCurrentItem().setCostoPaginaCurcy(getCurrentEditor().getCostoPagCurcyEdi());
		
		if ((getCurrentEditor().getCostoSezioneCurcyEdi()!=null)&& (getCurrentEditor().getCostoSezioneCurcyEdi().trim().length()>0) )
			getCurrentItem().setCostoSezioneCurcy(getCurrentEditor().getCostoSezioneCurcyEdi());
		
		if ((getCurrentEditor().getCostoRivistaCurcyEdi()!=null)&& (getCurrentEditor().getCostoRivistaCurcyEdi().trim().length()>0) )
			getCurrentItem().setCostoRivistaCurcy(getCurrentEditor().getCostoRivistaCurcyEdi());
		
		if ((getCurrentEditor().getCostoCapitoloCurcyEdi()!=null)&& (getCurrentEditor().getCostoCapitoloCurcyEdi().trim().length()>0) )
			getCurrentItem().setCostoCapitoloCurcy(getCurrentEditor().getCostoCapitoloCurcyEdi());
	}
	
	
	public void impostadefault(EditBean editBean) 
	{
		if (getCurrentItem().getSaleTypeId()==null)
			getCurrentItem().setSaleTypeId(new Integer(-1));
		if (getCurrentItem().getFlagCopiaIncolla()==null)
			getCurrentItem().setFlagCopiaIncolla("N");
		if (getCurrentItem().getFlagFullText()==null)
			getCurrentItem().setFlagFullText("N");
		if (getCurrentItem().getOpeStampa()==null)
			getCurrentItem().setOpeStampa("0");
		if (getCurrentItem().getItemTypeSale()==null)
			getCurrentItem().setItemTypeSale("Z");
		
//----> 20100416 inizio: default dati listino 
		if (getCurrentItem().getLstType()==null || getCurrentItem().getLstType().trim().length()==0)
			getCurrentItem().setLstType("");
			
		if (getCurrentItem().getLstCurcy()==null || getCurrentItem().getLstCurcy().trim().length()==0)
			getCurrentItem().setLstCurcy("EUR");
		
//----> 20100614 inizio: imposto default per policy online type 
		if (getCurrentItem().getPolicyOnlineType()==null || getCurrentItem().getPolicyOnlineType().trim().length()==0)
			getCurrentItem().setPolicyOnlineType("00");
		
//----> 20100416 fine		
		
		setListTag59(search59x(editBean));
		
		if (!getListTag59().isEmpty()){			
			if (listTag59.containsKey("T591")){
				BibliographicNoteTag tag = (BibliographicNoteTag)listTag59.get("T591");
				if ((getCurrentItem().getAnno()==null)&& ((tag.getStringText().getSubfieldsWithCodes("a").getDisplayText().trim().length()>0))) {
					getCurrentItem().setAnno(tag.getStringText().getSubfieldsWithCodes("a").getDisplayText().toString().trim());
				}
				if ((getCurrentItem().getVolume()==null)&& ((tag.getStringText().getSubfieldsWithCodes("b").getDisplayText().trim().length()>0))) {
					getCurrentItem().setVolume(tag.getStringText().getSubfieldsWithCodes("b").getDisplayText().toString());
				}
				if ((getCurrentItem().getCapitolo()==null)&& ((tag.getStringText().getSubfieldsWithCodes("c").getDisplayText().trim().length()>0))) {
					getCurrentItem().setCapitolo(tag.getStringText().getSubfieldsWithCodes("c").getDisplayText().toString());
				}
				if ((getCurrentItem().getIdFascicolo()==null)&& ((tag.getStringText().getSubfieldsWithCodes("d").getDisplayText().trim().length()>0))) {
					getCurrentItem().setIdFascicolo(tag.getStringText().getSubfieldsWithCodes("d").getDisplayText().toString().trim());
				}
//				if ((getCurrentItem().getProgressivo()==null)&& ((tag.getStringText().getSubfieldsWithCodes("e").getDisplayText().trim().length()>0))) {
//					getCurrentItem().setProgressivo(tag.getStringText().getSubfieldsWithCodes("e").getDisplayText().toString().trim());
//				}
				
//				Devo controllare che siano numerici!!!
				try {
					if ((getCurrentItem().getPagineDa()==null)&& ((tag.getStringText().getSubfieldsWithCodes("f").getDisplayText().trim().length()>0))) {
						getCurrentItem().setPagineDa(new Integer(tag.getStringText().getSubfieldsWithCodes("f").getDisplayText().toString()));
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				try {
					if ((getCurrentItem().getPagineA()==null)&& ((tag.getStringText().getSubfieldsWithCodes("g").getDisplayText().trim().length()>0))) {
						getCurrentItem().setPagineA(new Integer(tag.getStringText().getSubfieldsWithCodes("g").getDisplayText().toString()));
					}
				} catch (Exception e) {
				// TODO: handle exception
				}
				
			}			
			
			if (listTag59.containsKey("T592")){
				BibliographicNoteTag tag = (BibliographicNoteTag)listTag59.get("T592");
				if ((getCurrentItem().getDescrizione()==null)&& ((tag.getStringText().getSubfieldsWithCodes("a").getDisplayText().trim().length()>0))) {
					getCurrentItem().setDescrizione(tag.getStringText().getSubfieldsWithCodes("a").getDisplayText().toString());
				}
			}
		}
//--->	20100910 inizio: Se non e' presente, imposto il codice editore prendendolo dall' eventuale tag 998
		setPublisher998("");
		ClassificationAccessPoint t998 = (ClassificationAccessPoint ) editBean.getCatalogItem().findFirstTagByNumber("998");
		if(t998!=null){
			if (t998.getStringText().getSubfieldsWithCodes("a").getDisplayText().toString().trim().length()>0){
				setPublisher998(t998.getStringText().getSubfieldsWithCodes("a").getDisplayText().trim());
			}
		}		
		setPublisherDisable(false);
		if (getCurrentItem().getCodEditore()==null){
			getCurrentItem().setCodEditore("");
			if (getPublisher998().trim().length()>0){
				getCurrentItem().setCodEditore(getPublisher998());
				setPublisherDisable(true);
			}		
		}else {
			if (getCurrentItem().getCodEditore().equalsIgnoreCase(getPublisher998())){
				setPublisherDisable(true);
			}
		}
//---->	20110609: imposto ad "S" il flag presenza su web
		if (getCurrentItem().getFlagWebStatus()==null)
			getCurrentItem().setFlagWebStatus("S");
	}
	
	public String defaultTotPages()
	{
		String totPages = new String("");
		if (getCurrentItem().getPagineDa()!=null && getCurrentItem().getPagineDa().intValue()>=0 ) {
			if (getCurrentItem().getPagineA()!=null && getCurrentItem().getPagineA().intValue()>=0 ) {
				int pagtot = (getCurrentItem().getPagineA().intValue() - getCurrentItem().getPagineDa().intValue()) + 1 ;
				totPages=(Integer.toString(pagtot));
			}
		}	
		return totPages;
	}
	
	public HashMap search59x(EditBean editBean)
	{
		HashMap mappa = new HashMap();
		String tag591 = new String("T591");
		String tag592 = new String("T592");
		BibliographicNoteTag t591 = (BibliographicNoteTag) editBean.getCatalogItem().findFirstTagByNumber("591");
		if (t591 != null)
			mappa.put(tag591, t591);
		
		BibliographicNoteTag t592 = (BibliographicNoteTag) editBean.getCatalogItem().findFirstTagByNumber("592");
		if (t592 != null)
			mappa.put(tag592, t592);
			
		return mappa;
	}
	
	public void workTag59x(EditBean bean, HttpServletRequest request) throws MarcCorrelationException, AuthorisationException, DataAccessException, NewTagException, ValidationException 
	{
		setSaveRecord(true);
		if (!getListTag59().isEmpty()){
			if (listTag59.containsKey("T591")){
				BibliographicNoteTag tag = (BibliographicNoteTag)listTag59.get("T591");
				removeTag(tag, bean);
			}
			
			if (listTag59.containsKey("T592")){
				BibliographicNoteTag tag = (BibliographicNoteTag)listTag59.get("T592");
				removeTag(tag, bean);
			}
			insertTag591(bean, request);
			insertTag592(bean, request);
		}else {
			insertTag591(bean, request);
			insertTag592(bean, request);
		}
	}
	
	/**
	 *   Metodo che aggiorna la S_CAS_CACHE : cancellando i dga, se non ci sono tag097, 
	 *   si deve modificare la S_CAS_CACHE mettendo i valori di default del record non digitale 
	 * @param bean
	 * @throws DataAccessException
	 */
	public void updateCasCache(EditBean bean) throws DataAccessException 
	{
		ControlNumberAccessPoint tag097 = (ControlNumberAccessPoint) bean.getCatalogItem().findFirstTagByNumber("097");
//----->Se non ci sono altri tag 097 nel record l'utente puo' modificare i check digital e natura livello
		if (tag097 == null) {			
			EditCasCacheBean cacheBean = new EditCasCacheBean();
			cacheBean.loadItems(bean.getCatalogItem().getAmicusNumber().intValue());
			
			bean.setPresentTag097(false);
			bean.setOptDigitalLevel("001");
			bean.setMdrFgl("001");
			bean.setLevelDisable(true);
			bean.setCheckContinuaz("N");
			bean.setCheckOnline("N");
			bean.setCheckDigital("N");
			bean.setFormatRecordType("F1");
			
			cacheBean.getCurrentItem().setDigCheck(bean.getCheckDigital());
			cacheBean.getCurrentItem().setContinuazCheck(bean.getCheckContinuaz());
			cacheBean.getCurrentItem().setOnlineCheck(bean.getCheckOnline());
			cacheBean.getCurrentItem().setMdrFgl(bean.getMdrFgl());
			cacheBean.getCurrentItem().setNtrLevel(bean.getOptDigitalLevel());
			cacheBean.getCurrentItem().setCheckNCORE(bean.getCheckNCORE());
			cacheBean.getCurrentItem().setCheckNTOCSB(bean.getCheckNTOCSB());
			
			cacheBean.getCurrentItem().setPagMin(this.getPageMin());
			cacheBean.getCurrentItem().setPagMax(Integer.valueOf(this.getPageMax()));
			cacheBean.getCurrentItem().setFlagNTI(this.getFlagNTI());
			cacheBean.getCurrentItem().setFlagReiteration(this.getFlagReiteration());
			cacheBean.getCurrentItem().setCodeWeeklyConsignmentFirst(this.getCodeWeeklyConsignmentFirst());
			cacheBean.getCurrentItem().setCodeWeeklyConsignmentSecond(this.getCodeWeeklyConsignmentSecond());
			cacheBean.getCurrentItem().setCodeMonthlyConsignmentFirst(this.getCodeMonthlyConsignmentFirst());
			cacheBean.getCurrentItem().setCodeMonthlyConsignmentSecond(this.getCodeMonthlyConsignmentSecond());
			cacheBean.save();
		}
	}
	
	public void removeTag(BibliographicNoteTag tag, EditBean bean) throws MarcCorrelationException, AuthorisationException, DataAccessException, ValidationException 
	{
		int i = bean.getCatalogItem().getTags().indexOf(tag);
		bean.setTagIndex(i);
		bean.deleteTag();
	}
	
	@SuppressWarnings("unchecked")
	public void insertTag591(EditBean bean, HttpServletRequest request) throws MarcCorrelationException, NewTagException, AuthorisationException, DataAccessException, ValidationException 
	{	
		if (verifyInsertT591()){
			
			BibliographicNoteTag tag591 = (BibliographicNoteTag) bean.newTag(1, (short) 7);
			CorrelationValues v = new CorrelationValues((short)200,(short) -1, (short) -1);
			tag591.setCorrelationValues(v);
			bean.refreshCorrelation(tag591.getCorrelation(1), tag591.getCorrelation(2), SessionUtils.getCurrentLocale(request));
			
			List subfieldList = new ArrayList();
			List subfields = new ArrayList();
			
			if ((getCurrentItem().getAnno()!=null) && (getCurrentItem().getAnno().trim().length()>0)) {
				subfieldList.add("a");
				subfields.add(getCurrentItem().getAnno());
			}
			if ((getCurrentItem().getVolume()!=null)&& (getCurrentItem().getVolume().trim().length()>0) ) {
				subfieldList.add("b");
				subfields.add(getCurrentItem().getVolume());
			}
			if ((getCurrentItem().getCapitolo()!=null)&& (getCurrentItem().getCapitolo().trim().length()>0)) {
				subfieldList.add("c");
				subfields.add(getCurrentItem().getCapitolo());
			}
			if ((getCurrentItem().getIdFascicolo()!=null)&& (getCurrentItem().getIdFascicolo().trim().length()>0)) {
				subfieldList.add("d");
				subfields.add(getCurrentItem().getIdFascicolo());
			}
			if ((getCurrentItem().getPagineDa()!=null)) {
				subfieldList.add("f");
				subfields.add(getCurrentItem().getPagineDa().toString());
			}
			if ((getCurrentItem().getPagineA()!=null)) {
				subfieldList.add("g");
				subfields.add(getCurrentItem().getPagineA().toString());
			}
			bean.changeText(new StringText(subfieldList, subfields));	
		}
	}
	
	private boolean verifyInsertT591()
	{
		boolean isOk = false;
		if (((getCurrentItem().getAnno()       !=null) && (getCurrentItem().getAnno().trim().length()>0)) ||
			((getCurrentItem().getVolume()     !=null) && (getCurrentItem().getVolume().trim().length()>0)) || 
			((getCurrentItem().getCapitolo()   !=null) && (getCurrentItem().getCapitolo().trim().length()>0)) ||
			((getCurrentItem().getIdFascicolo()!=null) && (getCurrentItem().getIdFascicolo().trim().length()>0)) || 
//			((getCurrentItem().getProgressivo()!=null) && (getCurrentItem().getProgressivo().trim().length()>0)) || 
			 (getCurrentItem().getPagineDa()!=null) || 
			 (getCurrentItem().getPagineA()!=null) )
			 isOk = true;
		
		return isOk;
	}
	
	public void insertTag592(EditBean bean, HttpServletRequest request) throws MarcCorrelationException, NewTagException, AuthorisationException, DataAccessException, ValidationException 
	{
		if ((getCurrentItem().getDescrizione()!=null) && (getCurrentItem().getDescrizione().trim().length()>0)) {
			
			BibliographicNoteTag tag592 = (BibliographicNoteTag) bean.newTag(1, (short) 7);
			CorrelationValues v1 = new CorrelationValues((short)201,(short) -1, (short) -1);
			tag592.setCorrelationValues(v1);
			bean.refreshCorrelation(tag592.getCorrelation(1), tag592.getCorrelation(2), SessionUtils.getCurrentLocale(request));
			
			List subfieldList1 = new ArrayList();
			List subfields1 = new ArrayList();
			subfieldList1.add("a");
			subfields1.add(getCurrentItem().getDescrizione());
			bean.changeText(new StringText(subfieldList1, subfields1));
		}
	}		
	
	public void loadPolicyForPublisherChange() throws DataAccessException
	{
		DAOCasDigAdmin dao = new DAOCasDigAdmin();
		List listPoliciesSap = dao.loadPolicy(currentItem.getCodEditore());
		
		String pagTot = defaultTotPages();
		if (pagTot.trim().length()>0){
			DigitalPoliciesBean bean = null;
			for (int i = 0; i < listPoliciesSap.size(); i++) {
				bean = (DigitalPoliciesBean) listPoliciesSap.get(i);
				if ("P".equalsIgnoreCase(bean.getPolicyType())) {
					float result = Float.parseFloat(pagTot) * bean.getPolicyPrice().floatValue();
					DecimalFormatSymbols symbols = new DecimalFormatSymbols();
					symbols.setDecimalSeparator('.');
					DecimalFormat formatter = new DecimalFormat("#########.##", symbols);
					bean.setPolicyTotPrice(Float.valueOf(formatter.format(result)));
				}
			}
		}
		List sortPolicyList = new ArrayList(listPoliciesSap);
		Collections.sort(sortPolicyList);
		setListPolicies(sortPolicyList);
		
		//---->	Imposto i campi del bean per metterli nel form
		DigitalPoliciesBean bean = null;
		List priceList = new ArrayList();
		List curcyList = new ArrayList();
		List stampsList = new ArrayList();
		List priceTotList = new ArrayList();
		for (int i = 0; i < getListPolicies().size(); i++) {
			bean = (DigitalPoliciesBean) getListPolicies().get(i);
			priceList.add(String.valueOf(bean.getPolicyPrice().floatValue()));
			curcyList.add(bean.getPolicyCurcy());
			stampsList.add(String.valueOf((bean.getPolicyStamps())));
			priceTotList.add(String.valueOf(bean.getPolicyTotPrice().floatValue()));
		}
		setPolicyPrice((String[]) priceList.toArray((new String[0])));
		setPolicyCurcy((String[]) curcyList.toArray((new String[0])));
		setPolicyStamps((String[]) stampsList.toArray((new String[0])));
		setPolicyTotPrice((String[]) priceTotList.toArray((new String[0])));
	}	
	
	public void loadPolicy() throws DataAccessException 
	{		
		DAOCasDigAdmin dao = new DAOCasDigAdmin();
		List listPolicyDga = dao.loadCasDgaPolicy(bibNumber);
		List listDgaPoliciesBean = new ArrayList();
//----> Trasformo la lista di CasDgaPolicy in lista di DigitalPoliciesBean
		CasDgaPolicy casDgaPolicy = null;
		DigitalPoliciesBean digitalPoliciesBean = null;
		for (int i = 0; i < listPolicyDga.size(); i++) {
			casDgaPolicy = (CasDgaPolicy)listPolicyDga.get(i);
			digitalPoliciesBean = new DigitalPoliciesBean(casDgaPolicy);
			listDgaPoliciesBean.add(digitalPoliciesBean);
		}	
		
		List listPoliciesSap = dao.loadPolicy(currentItem.getCodEditore());
		
//		20100521 inizio: controllo per far impostare la prima volta 
//			il prezzo totale delle policy "P" non presenti in Cas_Sap_Policy 
		String pagTot = defaultTotPages();
		if (pagTot.trim().length()>0){
			DigitalPoliciesBean bean = null;
			for (int i = 0; i < listPoliciesSap.size(); i++) {
				bean = (DigitalPoliciesBean) listPoliciesSap.get(i);
				if ("P".equalsIgnoreCase(bean.getPolicyType())) {
					float result = Float.parseFloat(pagTot) * bean.getPolicyPrice().floatValue();
					DecimalFormatSymbols symbols = new DecimalFormatSymbols();
					symbols.setDecimalSeparator('.');
					DecimalFormat formatter = new DecimalFormat("#########.##", symbols);
					bean.setPolicyTotPrice(Float.valueOf(formatter.format(result)));
				}
			}
		}
//		20100521 fine
		
		for (int i = 0; i < listDgaPoliciesBean.size(); i++) {
			digitalPoliciesBean  = (DigitalPoliciesBean)listDgaPoliciesBean.get(i);
			if (listPoliciesSap.contains(digitalPoliciesBean)){
				listPoliciesSap.remove(digitalPoliciesBean);				
			}
		}
		
		Set result = new HashSet();
		result.addAll(listDgaPoliciesBean);
		result.addAll(listPoliciesSap);
		
//----> Ordino la lista per codice e tipo policy
		List sortPolicyList = new ArrayList(result);
		Collections.sort(sortPolicyList);
		setListPolicies(sortPolicyList);	
		
//---->	Imposto i campi del bean per metterli nel form
		DigitalPoliciesBean bean = null;
		List priceList = new ArrayList();
		List curcyList = new ArrayList();
		List stampsList = new ArrayList();
		List priceTotList = new ArrayList();
		for (int i = 0; i < getListPolicies().size(); i++) {
			bean = (DigitalPoliciesBean) getListPolicies().get(i);
			priceList.add(String.valueOf(bean.getPolicyPrice().floatValue()));
			curcyList.add(bean.getPolicyCurcy());
			stampsList.add(String.valueOf((bean.getPolicyStamps())));
			priceTotList.add(String.valueOf(bean.getPolicyTotPrice().floatValue()));
		}
		setPolicyPrice((String[]) priceList.toArray((new String[0])));
		setPolicyCurcy((String[]) curcyList.toArray((new String[0])));
		setPolicyStamps((String[]) stampsList.toArray((new String[0])));
		setPolicyTotPrice((String[]) priceTotList.toArray((new String[0])));
	}	
	
//  20100416 : imposto data creazione, data modifica e utente 
	public void cntrDateUser(HttpServletRequest request) throws DateInputException
	{
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		
//		Se le date di validita' sono valorizzate entrambe
		if (getCurrentItem().getlstPriceDtIniString().trim().length()>0 &&
			getCurrentItem().getlstPriceDtFinString().trim().length()>0){
			if (!getCurrentItem().getLstPriceDtFin().after(getCurrentItem().getLstPriceDtIni())){
				throw new DateInputException("error.date.input");
			}
		}

//		Data creazione
		if (getCurrentItem().getLstDtCrtString().trim().length()==0) {
			getCurrentItem().setLstDtCrt(formatter.parse(formatter.format(new Date()), new ParsePosition(0)));
		}
//		Data aggiornamento
		getCurrentItem().setPriceListDate(formatter.parse(formatter.format(new Date()), new ParsePosition(0)));
//		Utente
		UserProfile user = SessionUtils.getUserProfile(request.getSession(false));
		getCurrentItem().setLstUser(user.getName());
	}
	
	public void tag365(EditBean bean) throws DataAccessException, MarcCorrelationException, AuthorisationException, ValidationException, NewTagException
	{
//----> 20100813 inizio: Solo se il codice listino e' diverso da null devo scrivere il tag 365
		
//--->  Se almeno uno dei campi del listino base storico (esclusa la valuta) e' impostato devo scrivere il tag365
//		if ((getCurrentItem().getLstType()!=null && getCurrentItem().getLstType().trim().length()>0) ||
//		    (getCurrentItem().getLstNote()!=null && getCurrentItem().getLstNote().trim().length()>0) ||
//		    (getCurrentItem().getlstPriceDtIniString()!=null) && (getCurrentItem().getlstPriceDtIniString().trim().length()>0) ||
//		    (getCurrentItem().getlstPriceDtFinString()!=null) && (getCurrentItem().getlstPriceDtFinString().trim().length()>0) ||
//		    (getCurrentItem().getPriceList()!=null && (Float.floatToIntBits(getCurrentItem().getPriceList().floatValue())!=0)) ){
//			BibliographicNoteTag tag = searchTag365(bean);
//			if (tag==null){
//				insertTag365(bean);
//			}else {
//				removeTag(tag, bean);
//				insertTag365(bean);
//			}
//		}
		
		if (getCurrentItem().getLstType()!=null && getCurrentItem().getLstType().trim().length()>0) {
			BibliographicNoteTag tag = searchTag365(bean);
			if (tag==null){
				insertTag365(bean);
			}else {
				removeTag(tag, bean);
				insertTag365(bean);
			}
			setSaveRecord(true);
		}
//----> 20100813 fine
	}

	/**
	 * 20101014: creazione del tag 300 se non esiste ed il range delle pagine da-a e' stato impostato
	 */
	public void tag300(EditBean bean) throws DataAccessException, MarcCorrelationException, AuthorisationException, ValidationException, NewTagException
	{
		BibliographicNoteTag t300 = (BibliographicNoteTag) bean.getCatalogItem().findFirstTagByNumber("300");
		if (t300==null){
			BibliographicNoteTag tag300 = (BibliographicNoteTag) bean.newTag(1, (short) 7);
			CorrelationValues v = new CorrelationValues((short)35,(short) -1, (short) -1);
			tag300.setCorrelationValues(v);
			bean.refreshCorrelation(tag300.getCorrelation(1), tag300.getCorrelation(2), getLocale());
			
			List subfieldList = new ArrayList();
			List subfields = new ArrayList();
			
			StringBuffer buffer = new StringBuffer();
						
			if ((getCurrentItem().getPagineDa()!=null) && (getCurrentItem().getPagineDa().intValue()>0) && 
				(getCurrentItem().getPagineA()!=null) && (getCurrentItem().getPagineA().intValue()>0)) {
				subfieldList.add("a");
				buffer.append("P. ").append(getCurrentItem().getPagineDa()).append("-").append(getCurrentItem().getPagineA());
				subfields.add(buffer.toString());
			}	
			bean.changeText(new StringText(subfieldList, subfields));
			setSaveRecord(true);
		}
	}
	
	public BibliographicNoteTag searchTag365(EditBean bean) throws DataAccessException
	{
		List tags = bean.getCatalogItem().getTags();
		Iterator it = tags.iterator();
		while (it.hasNext()) {
			Tag tag = (Tag) it.next();
			 if(tag instanceof BibliographicNoteTag) {
				BibliographicNoteTag tagNote = (BibliographicNoteTag) tag;
				if (tagNote.getMarcEncoding().getMarcTag().equalsIgnoreCase("365")) {
				    if (tagNote.getStringText().getSubfieldsWithCodes("a").getDisplayText().trim().length()>0 &&	
					    tagNote.getStringText().getSubfieldsWithCodes("a").getDisplayText().equalsIgnoreCase("01")){
					    return tagNote;
				    }
				}
			 }
		}
		return null;
	}
	
//	public void removeTag(BibliographicNoteTag tag, EditBean bean) throws MarcCorrelationException, AuthorisationException, DataAccessException, ValidationException 
//	{
//		int i = bean.getCatalogItem().getTags().indexOf(tag);
//		bean.setTagIndex(i);
//		bean.deleteTag();
//	}
	
	public void insertTag365(EditBean bean) throws MarcCorrelationException, NewTagException, AuthorisationException, DataAccessException, ValidationException 
	{				
			BibliographicNoteTag tag365 = (BibliographicNoteTag) bean.newTag(1, (short) 7);
			CorrelationValues v = new CorrelationValues((short)378,(short) -1, (short) -1);
			tag365.setCorrelationValues(v);
			bean.refreshCorrelation(tag365.getCorrelation(1), tag365.getCorrelation(2), getLocale());
			
			List subfieldList = new ArrayList();
			List subfields = new ArrayList();
			
			if ((getCurrentItem().getLstType()!=null) && (getCurrentItem().getLstType().trim().length()>0)) {
				subfieldList.add("a");
				subfields.add(getCurrentItem().getLstType());
			}
//--------> Deve scrivere il $b solo se impostato e diverso da zero
			if (getCurrentItem().getPriceList()!=null) {
				int appoPrice = Float.floatToIntBits(getCurrentItem().getPriceList().floatValue());
				if (appoPrice!=0) {
					subfieldList.add("b");
					subfields.add(getCurrentItem().getPriceList().toString());
				}
			}			
			if (getCurrentItem().getLstCurcy()!=null){
				subfieldList.add("c");
				subfields.add(getCurrentItem().getLstCurcy().toString());
			}			
			if ((getCurrentItem().getLstNote()!=null) && (getCurrentItem().getLstNote().trim().length()>0) ) {
				subfieldList.add("e");
				subfields.add(getCurrentItem().getLstNote());
			}			
			if ((getCurrentItem().getlstPriceDtIniString()!=null) && (getCurrentItem().getlstPriceDtIniString().trim().length()>0) ) {
				subfieldList.add("f");
				subfields.add(getCurrentItem().getlstPriceDtIniString());
			}
			if ((getCurrentItem().getlstPriceDtFinString()!=null) && (getCurrentItem().getlstPriceDtFinString().trim().length()>0) ) {
				subfieldList.add("g");
				subfields.add(getCurrentItem().getlstPriceDtFinString());
			}			
			
			bean.changeText(new StringText(subfieldList, subfields));
			
//			bean.setNavigation(true);
//			bean.sortTags(locale);
//			bean.resetCommands();
//			bean.saveRecord();
	}
	
	public CasSapPubl getCurrentEditor() {
		return currentEditor;
	}

	public void setCurrentEditor(CasSapPubl currentEditor) {
		this.currentEditor = currentEditor;
	}

	public HashMap getListTag59() {
		return listTag59;
	}

	public void setListTag59(HashMap listTag59) {
		this.listTag59 = listTag59;
	}
	public boolean isExistItem() {
		return existItem;
	}
	public void setExistItem(boolean existItem) {
		this.existItem = existItem;
	}
	
	public void createTag998(EditBean bibBean) throws DataAccessException, NewTagException, AuthorisationException, ValidationException 
	{
		if (getCurrentItem().getCodEditore()!=null && getCurrentItem().getCodEditore().trim().length()>0) {
			ClassificationAccessPoint t998 = (ClassificationAccessPoint ) bibBean.getCatalogItem().findFirstTagByNumber("998");
			if (t998==null){
				BibliographicEditBean biblioBean = (BibliographicEditBean) bibBean;
				biblioBean.createTag998(getCurrentItem().getCodEditore());
				setSaveRecord(true);
			}
		}
	}

	public void removeTag998(EditBean bean) throws MarcCorrelationException, AuthorisationException, DataAccessException, ValidationException 
	{
		PublisherManager publisherManager = (PublisherManager) bean.getCatalogItem().findFirstTagByNumber("260");
		ClassificationAccessPoint t998 = (ClassificationAccessPoint ) bean.getCatalogItem().findFirstTagByNumber("998");
//----> Se non c'e' nussun tag260 e c'e' il tag998 lo cancello
		if (publisherManager==null && t998!=null){
			int i = bean.getCatalogItem().getTags().indexOf(t998);
			bean.setTagIndex(i);
			bean.deleteTag();
			bean.setNavigation(true);
			bean.sortTags(getLocale());
			bean.resetCommands();
			bean.saveRecord();
		}
	}
	
	public void doiModify(EditBean editBean, HttpServletRequest request) throws NumberFormatException, DataAccessException, DigitalDoiException, DigitalLevelException, HttpException, RequiredFieldsException, IOException
	{
		DigitalDoiBean digitalDoiBean = new DigitalDoiBean(editBean, request);
		String codiceDoi = editBean.getDoiCode();
		if (codiceDoi.trim().length()>0 && !editBean.isInternalDoiPermitted())
		{
			digitalDoiBean.setDOIEsistente(codiceDoi);
			digitalDoiBean.setCreaDOI("NO");
			digitalDoiBean.httpPost();	
		}
	}
	
	/**
	 * Metodo che salva il record e la S_CAS_CACHE 
	 * @param editBean
	 * @param currentLocale
	 * @param cataloguingView
	 * @throws DataAccessException
	 * @throws AuthorisationException
	 * @throws ValidationException
	 * @throws RecordInUseException
	 */
	public void saveRecordCasCache(EditBean editBean, Locale currentLocale, int cataloguingView) throws DataAccessException, AuthorisationException, ValidationException, RecordInUseException 
	{
	    editBean.setNavigation(true);
		editBean.sortTags(currentLocale);
		editBean.resetCommands();
		//
		
		editBean.saveRecord();
//----> 20100816 inizio: dopo il salvataggio intermedio deve ricaricare il record per ripulire il catalogItem (altrimenti duplicava il tag 260)		
		BibliographicEditBean biblio = (BibliographicEditBean) editBean;
		biblio.loadItem(editBean.getCatalogItem().getAmicusNumber().intValue(),cataloguingView);
		biblio.sortTags(currentLocale);
	}	
	
	public void saveCasCache(EditBean editBean) throws DataAccessException
	{
		CasaliniContextBean casalini = editBean.getCasaliniBean();
		if (editBean.getCasaliniBean().getCasCache()==null) {
			casalini.createCasCache();
		}
		casalini.getCasCache().setBibItemNumber(editBean.getCatalogItem().getAmicusNumber().intValue());
		casalini.getCasCache().setDigCheck(editBean.getCheckDigital());	
		casalini.getCasCache().setOnlineCheck(editBean.getCheckOnline());	
		casalini.getCasCache().setContinuazCheck(editBean.getCheckContinuaz());
		casalini.getCasCache().setMdrFgl(editBean.getMdrFgl());
		casalini.getCasCache().setNtrLevel(editBean.getOptDigitalLevel());
		casalini.getCasCache().setCheckNCORE(editBean.getCheckNCORE());
		casalini.getCasCache().setCheckNTOCSB(editBean.getCheckNTOCSB());
		casalini.getCasCache().setPagMin(this.getPageMin());
		casalini.getCasCache().setPagMax(this.getPageMax());
		casalini.getCasCache().setFlagNTI(this.getFlagNTI());
		casalini.getCasCache().setFlagReiteration(this.getFlagReiteration());
		casalini.getCasCache().setCodeWeeklyConsignmentFirst(this.getCodeWeeklyConsignmentFirst());
		casalini.getCasCache().setCodeWeeklyConsignmentSecond(this.getCodeWeeklyConsignmentSecond());
		casalini.getCasCache().setCodeMonthlyConsignmentFirst(this.getCodeMonthlyConsignmentFirst());
		casalini.getCasCache().setCodeMonthlyConsignmentSecond(this.getCodeMonthlyConsignmentSecond());
		casalini.getCasCache().setLevelCard(this.getLevelCard());
		casalini.getCasCache().setStatusDisponibilit(this.getStatusDisponibilit());
//		casalini.getCasCache().setWorkingCode(this.getWorkingCode());
//		casalini.getCasCache().setNote(this.getNote());
		casalini.getCasCache().markChanged();
		casalini.getCasCache().getDAO().persistByStatus(casalini.getCasCache());
	}
	
	public String clearNullValueForInteger(String param)
	{
		String result = "";
		if ( (param == null) || (param.trim().equals("")) )
			result = "0";
		else
			result = param.trim();
		
		return result;
	}
	
	public boolean existTag856(EditBean editBean) throws PermalinkException
	{
		//DEVE contenere tag 856 42 $u
		BibliographicNoteTag tag856;
		
		try
		{
			tag856 = editBean.get856_4_2_Tag();
			if (tag856 == null)
			{
				throw new PermalinkException();
			}
			
			if (tag856.getStringText().getSubfieldsWithCodes("u").getDisplayText().equals(""))
			{
				throw new PermalinkException();
			}

		} catch (DuplicateTagException e)			
			//ignore exception
		{
		} catch (DataAccessException e)
		{
			//ignore exception
		}
			
		return true;
	}
	
//	public boolean checkPreFilterApproval() throws DataAccessException
//	{
//		CasDigAdmin currentItem = this.getCurrentItem();
//		DAOApproval dao = new DAOApproval();
//		
//		try
//		{
//			if (dao.getFilterPublisher(currentItem.getCodEditore())!=null)
//			{
//				return true;
//			}
//		} catch (RecordNotFoundException e)
//		{
//			//ignore exception
//		}
//		
//		for (int i = 0; i < listPoliciesToInsert.size(); i++)
//		{
//			CasDgaPolicy casDgaPolicy  = (CasDgaPolicy)getListPoliciesToInsert().get(i);
//			if (dao.existInPolicyFilter(casDgaPolicy.getIdPolicy()))
//			{
//				return true;
//			}				
//		}			
//		
//		return false;
//		
//	}

	/**
	 * This method associate the amicusNumber to collection digital
	 * for FTP platform.
	 * 
	 * @throws DataAccessException in case of SQLException
	 */
//	public void associateRecordToCollectionDigital() throws DataAccessException	
//	{		
//		DAOCollectionCSTAccessPoint daoCollectionCustom = new DAOCollectionCSTAccessPoint();
//		daoCollection " + System.getProperty(org.folio.cataloging.Global.SCHEMA_CUSTOMER_KEY) + ".save(bibNumber, new String[]{IGlobalConst.COLLECTION_DIGITAL_STANDARD}, new Date(), new Date());
//	}
	
	@SuppressWarnings("unchecked")
	public void copyFormToBean(DigitalAmminForm form) throws NumberFormatException
	{
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		CasDigAdmin casDig = getCurrentItem();
	    
	    casDig.setFileName(form.getFileName());
	    casDig.setFileSize(form.getFileSize());
	    
	    if (form.getPolicyOnlineType()!=null)
	    	casDig.setPolicyOnlineType(form.getPolicyOnlineType());
	       
	    if ((form.getAnno()!= null) && (form.getAnno().trim().length()>0)) 
	    	casDig.setAnno(form.getAnno());
	    else
	    	casDig.setAnno(null);
	    
	    if ((form.getCodEditore()!=null) && (form.getCodEditore().trim().length()>0)) 
	    	casDig.setCodEditore(form.getCodEditore());
	    else 
	    	casDig.setCodEditore(null);
	    
	    if ((form.getCodEditoreBreve()!=null) && (form.getCodEditoreBreve().trim().length()>0)) 
	    	casDig.setCodEditoreBreve(form.getCodEditoreBreve().trim());
	    else 
	    	casDig.setCodEditoreBreve(null);
	    
	    if ((form.getDescrizione()!=null) && (form.getDescrizione().trim().length()>0)) 
	    	casDig.setDescrizione(form.getDescrizione());
	    else
	    	casDig.setDescrizione(null);
	    
	    if ((form.getCapitolo()!=null)&& (form.getCapitolo().trim().length()>0))
	    	casDig.setCapitolo(form.getCapitolo());
	    else
	    	casDig.setCapitolo(null);
	    
	    if ((form.getVolume()!=null)&& (form.getVolume() .trim().length()>0))
	    	casDig.setVolume(form.getVolume());
	    else
	    	casDig.setVolume(null);
	    
	    if ((form.getFascicolo()!= null) && (form.getFascicolo().trim().length()>0)) 
	    	casDig.setIdFascicolo(form.getFascicolo());
	    else
	    	casDig.setIdFascicolo(null);
	    
	    if (form.getSaleTypeId()!=null) {
	    	casDig.setSaleTypeId(new Integer(form.getSaleTypeId()));
	    }
	    
	    if ((form.getProductCategory()!= null) && (form.getProductCategory().trim().length()>0)) {
	    	casDig.setProductCategory(form.getProductCategory());
	    }else{
	    	casDig.setProductCategory(null);
	    }
	    
//Pagine
	    if ((form.getPageTot()!= null) && (form.getPageTot().trim().length()>0)) 
    		casDig.setPageTot(new Integer(form.getPageTot()));
	    else
	    	casDig.setPageTot(null);
	    
    	if ((form.getPagineDa()!= null) && (form.getPagineDa().trim().length()>0)) 
    		casDig.setPagineDa(new Integer(form.getPagineDa()));
    	else
    		casDig.setPagineDa(null);
    	
    	if ((form.getPagineA()!= null) && (form.getPagineA().trim().length()>0)) 
    		casDig.setPagineA(new Integer(form.getPagineA()));
    	else
    		casDig.setPagineA(null);
    	
//----> Valuta
		casDig.setLstCurcy(form.getLstCurcy());
			
//-----> Date
		if(form.getLstPriceDtIni()!=null && !form.getLstPriceDtIni().equals(""))
			casDig.setLstPriceDtIni(formatter.parse(form.getLstPriceDtIni(), new ParsePosition(0)));
		else
			casDig.setLstPriceDtIni(null);
		
		if(form.getLstPriceDtFin()!=null && !form.getLstPriceDtFin().equals(""))
			casDig.setLstPriceDtFin(formatter.parse(form.getLstPriceDtFin(), new ParsePosition(0)));
		else
			casDig.setLstPriceDtFin(null);
		
		if(form.getLstDtCrtString()!=null && !form.getLstDtCrtString().equals(""))
			casDig.setLstDtCrt(formatter.parse(form.getLstDtCrtString(), new ParsePosition(0)));
		else
			casDig.setLstDtCrt(null);
		
		casDig.setLstUser(form.getLstUser());
		casDig.setLstType(form.getLstType());
		casDig.setLstNote(form.getLstNote());
		
//--->	20100813 inizio: se sceglie tipo listino "--" devo disabilitare i campi del listino storico
		this.setTag365Disable(true);
		if (form.getLstType()!=null && form.getLstType().trim().length()>0) {
			this.setTag365Disable(false);
		}
//--->	20100813 fine
		
		if (form.getPriceList()==null)
			casDig.setPriceList(new Float(0));
		else
			casDig.setPriceList(form.getPriceList());
		
	    casDig.setFlagWebStatus(form.getFlagWebStatus());
	    casDig.setFlagAllegato(form.getFlagAllegato());
	    casDig.setFlagEsclusione(form.getFlagEsclusione());
	    casDig.setFlagStato(form.getFlagStato());
	    casDig.setFlagSupplemento(form.getFlagSupplemento());
	    casDig.setNumStampa(form.getNumStampa());
	    casDig.setOpeStampa(form.getOpeStampa());
	    casDig.setFlagFullText(form.getFlagFullText());
	    
//----> inizio: check per chiamare il servizio di modifica metadati (doi)
	    this.setMedraModifyCheck(form.getMedraModifyCheck());
//----> fine

//-->	Refresh liste per policy
		List codeList = new ArrayList();
		List typeList = new ArrayList();
		List priceList = new ArrayList();
		List priceTotList = new ArrayList();
		List curcyList = new ArrayList();
		List stampsList = new ArrayList();
		List nameList = new ArrayList();
		this.setPolicyCode(new String[0]);
		this.setPolicyCurcy(new String[0]);
		this.setPolicyPrice(new String[0]);
		this.setPolicyStamps(new String[0]);
		this.setPolicyType(new String[0]);
		this.setPolicyName(new String[0]);
		this.setPolicyTotPrice(new String[0]);
		
//----> Salvo tutti i campi delle policy del form per ritrovarli quando ricarico la pagina
		for (int i = 0; i < form.getPolicyCurcy().length; i++) {					
			
			if (form.getPolicyPrice()[i]==null || form.getPolicyPrice()[i].trim().length()==0)
				priceList.add("");
			else
				priceList.add(form.getPolicyPrice()[i]);
			
			if (form.getPolicyStamps()[i]==null || form.getPolicyStamps()[i].trim().length()==0)
				stampsList.add("");
			else 
				stampsList.add(form.getPolicyStamps()[i]);
			
			if (form.getPageTot()!= null && form.getPageTot().trim().length()>0) {
//				if (form.getPolicyPrice()[i]!=null && form.getPolicyPrice()[i].trim().length()>0 && "P".equalsIgnoreCase(((DigitalPoliciesBean)bean.getListPolicies().get(i)).getPolicyType())) {
				if (form.getPolicyPrice()[i]!=null && form.getPolicyPrice()[i].trim().length()>0) {
					if ("P".equalsIgnoreCase(((DigitalPoliciesBean)this.getListPolicies().get(i)).getPolicyType())) {
						float result = Float.parseFloat(form.getPageTot()) * Float.parseFloat(form.getPolicyPrice()[i]);
						DecimalFormatSymbols symbols = new DecimalFormatSymbols();
						symbols.setDecimalSeparator('.');
						DecimalFormat formatter1 = new DecimalFormat("#########.##", symbols);
						priceTotList.add(formatter1.format(result));
					} else
						priceTotList.add(form.getPolicyPrice()[i]);
				} else
					priceTotList.add("");
			}else {
				//Carmen bug 1592: Per la politica I se non ci sono le pagine e c'e' un prezzo unitario, bisogna passargli quello
				if ("I".equalsIgnoreCase(((DigitalPoliciesBean)this.getListPolicies().get(i)).getPolicyType())&& form.getPolicyPrice()[i]!=null )
					priceTotList.add(form.getPolicyPrice()[i]);
				//Se non ci sono pagine sia per la politica I oppure P (chiedere se servono le pagine per P)
				else
				   priceTotList.add(form.getPolicyTotPrice()[i]);
			}
			
			curcyList.add(form.getPolicyCurcy()[i]);
			codeList.add(((DigitalPoliciesBean)this.getListPolicies().get(i)).getPolicyCode());
			nameList.add(((DigitalPoliciesBean)this.getListPolicies().get(i)).getPolicyName());
			typeList.add(((DigitalPoliciesBean)this.getListPolicies().get(i)).getPolicyType());
		}
		if (form.getPolicyCurcy().length>0){
			this.setPolicyCode((String[])codeList.toArray(new String[0]));
			this.setPolicyCurcy((String[]) curcyList.toArray((new String[0])));
			this.setPolicyPrice((String[]) priceList.toArray((new String[0])));
			this.setPolicyStamps((String[]) stampsList.toArray((new String[0])));
			this.setPolicyType((String[]) typeList.toArray((new String[0])));
			this.setPolicyName((String[]) nameList.toArray((new String[0])));
			this.setPolicyTotPrice((String[]) priceTotList.toArray((new String[0])));
		}
	    if (!form.getPageMin().isEmpty()){
	    	this.setPageMin(new Integer(form.getPageMin()));
	    }else{
	    	this.setPageMin(null);
	    }
	    if (!form.getPageMin().isEmpty()){
	    	this.setPageMax(new Integer(form.getPageMax()));
	    }else{
	    	this.setPageMax(null);
	    }
	    this.setFlagNTI(form.getFlagNTI());
	    this.setFlagReiteration(form.getFlagReiteration());
	    this.setCodeWeeklyConsignmentFirst(form.getCodeWeeklyConsignmentFirst());
	    this.setCodeWeeklyConsignmentSecond(form.getCodeWeeklyConsignmentSecond());
	    this.setCodeMonthlyConsignmentFirst(form.getCodeMonthlyConsignmentFirst());
	    this.setCodeMonthlyConsignmentSecond(form.getCodeMonthlyConsignmentSecond());
	    this.setLevelCard(form.getLevelCard());
//	    this.setWorkingCode(form.getWorkingCode());
//	    this.setNote(form.getNote());
	    this.setStatusDisponibilit(Integer.valueOf(this.clearNullValueForInteger(form.getStatusDisponibilit())));
	}
}