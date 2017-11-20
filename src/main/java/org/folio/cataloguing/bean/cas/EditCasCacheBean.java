package org.folio.cataloguing.bean.cas;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.folio.cataloging.bean.LibrisuiteBean;
import org.folio.cataloging.bean.cataloguing.bibliographic.BibliographicEditBean;
import org.folio.cataloging.bean.cataloguing.common.EditBean;
import org.folio.cataloging.business.authorisation.AuthorisationException;
import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicNoteTag;
import org.folio.cataloging.business.cataloguing.bibliographic.ClassificationAccessPoint;
import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.bibliographic.NewTagException;
import org.folio.cataloging.business.cataloguing.bibliographic.PublisherManager;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.CorrelationValues;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.DateInputException;
import org.folio.cataloging.business.common.PublisherException;
import org.folio.cataloging.exception.ValidationException;
import org.folio.cataloging.dao.persistence.CasCache;

import org.folio.cataloging.dao.DAOCasCache;
import org.folio.cataloging.util.StringText;
import org.folio.cataloging.business.controller.SessionUtils;
import org.folio.cataloging.business.controller.UserProfile;

@SuppressWarnings("unchecked")
public class EditCasCacheBean extends LibrisuiteBean 
{
	private int bibNumber;
	private CasCache currentItem;
	private List items = new ArrayList();
	private Locale locale; 
	private String denEditore;
	private String codEditoreBreve;
	private boolean tag365Disable = true;
	private boolean publisherDisable = true;
	private boolean saveRecord = false;
	private String publisher998;
	
	
	public static EditCasCacheBean getInstance(HttpServletRequest request)	throws DataAccessException 
	{
		EditCasCacheBean bean =	(EditCasCacheBean) EditCasCacheBean.getSessionAttribute(request,EditCasCacheBean.class);
		if (bean == null) {
			bean = new EditCasCacheBean();
			bean.setSessionAttribute(request, bean.getClass());
			bean.setLocale(SessionUtils.getCurrentLocale(request));
		}
		return bean;
	}
	
	public void loadItems(int bibNumber) throws DataAccessException 
	{
		setBibNumber(bibNumber);
		DAOCasCache dao = new DAOCasCache();
		setItems(dao.loadCasCache(bibNumber));
		if (items.size() == 0) {
			addNew();
		}	
		setCurrentItem((CasCache) items.get(items.size() - 1));
	}
	
//  20091229 : imposto data creazione, data modifica e utente 
	public void cntrDateUser(HttpServletRequest request) throws DateInputException
	{
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		if (getCurrentItem().getlstPriceDtIniString().trim().length()>0 &&
			getCurrentItem().getlstPriceDtFinString().trim().length()>0){
			if (!getCurrentItem().getLstPriceDtFin().after(getCurrentItem().getLstPriceDtIni())){
				throw new DateInputException("error.date.input");
			}
		}
	
		if (getCurrentItem().getLstDtCrtString().trim().length()==0) {
			getCurrentItem().setLstDtCrt(formatter.parse(formatter.format(new Date()), new ParsePosition(0)));
		}
		getCurrentItem().setPriceListDate(formatter.parse(formatter.format(new Date()), new ParsePosition(0)));
		UserProfile user = SessionUtils.getUserProfile(request.getSession(false));
		getCurrentItem().setLstUser(user.getName());
	}

	@SuppressWarnings("unchecked")
	public void addNew() throws DataAccessException 
	{
		CasCache newItem =	new CasCache(getBibNumber());
		getItems().add(newItem);
		setCurrentItem(newItem);
	}

	public void save() throws DataAccessException 
	{
		CasCache item = getCurrentItem();
		item.markChanged();
		item.getDAO().persistByStatus(item);
	}
	
	public void delete() throws DataAccessException 
	{
		CasCache item = getCurrentItem();
		if (!item.isNew()) {
			item.markDeleted();
			item.getDAO().persistByStatus(item);
		}	
	}
	
	public void tag365(EditBean bean) throws DataAccessException, MarcCorrelationException, AuthorisationException, ValidationException, NewTagException
	{
//----> 20100813 inizio: Solo se il codice listino e' diverso da null devo scrivere il tag 365
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
	
	public void removeTag(BibliographicNoteTag tag, EditBean bean) throws MarcCorrelationException, AuthorisationException, DataAccessException, ValidationException 
	{
		int i = bean.getCatalogItem().getTags().indexOf(tag);
		bean.setTagIndex(i);
		bean.deleteTag();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	}
	
	/**
	 * Metodo che imposta i valori di default della S_CAS_CACHE per le combo visualizzate nel form
	 * @param bean
	 * @throws DataAccessException 
	 * @throws MarcCorrelationException 
	 * @throws PublisherException 
	 */
	public void setDefault(EditBean bibBean) throws MarcCorrelationException, DataAccessException 
	{
		if (getCurrentItem().getLstType()==null || getCurrentItem().getLstType().trim().length()==0)
			getCurrentItem().setLstType("");
			
		if (getCurrentItem().getLstCurcy()==null || getCurrentItem().getLstCurcy().trim().length()==0)
			getCurrentItem().setLstCurcy("EUR");
		
		if (getCurrentItem().getStatusDisponibilit()==null)
			getCurrentItem().setStatusDisponibilit(new Integer(99));		
		
//--->	20100910 inizio: Se non e' presente, imposto il codice editore prendendolo dall' eventuale tag 998
		
		setPublisher998("");
		ClassificationAccessPoint t998 = (ClassificationAccessPoint ) bibBean.getCatalogItem().findFirstTagByNumber("998");
		if(t998!=null){
			if (t998.getStringText().getSubfieldsWithCodes("a").getDisplayText().toString().trim().length()>0){
				setPublisher998(t998.getStringText().getSubfieldsWithCodes("a").getDisplayText().trim());
			}
		}	
		
		setPublisherDisable(false);
		if (getCurrentItem().getIdPublisher()==null){
			getCurrentItem().setIdPublisher("");
			if (getPublisher998().trim().length()>0){
				getCurrentItem().setIdPublisher(getPublisher998());
				setPublisherDisable(true);
			}		
		}else {
			if (getCurrentItem().getIdPublisher().equalsIgnoreCase(getPublisher998())){
				setPublisherDisable(true);
			}	
		}
		
//--->	Imposto anche i tre check altrimenti quando salvo i DLA vengono persi se impostati
	    getCurrentItem().setContinuazCheck(bibBean.getCheckContinuaz());
	    getCurrentItem().setOnlineCheck(bibBean.getCheckOnline());
	    getCurrentItem().setDigCheck(bibBean.getCheckDigital());
	    getCurrentItem().setCheckNTOCSB(bibBean.getCheckNTOCSB());
	    getCurrentItem().setCheckNCORE(bibBean.getCheckNCORE());
	    
		if (getCurrentItem().getLevelCard()==null){
			getCurrentItem().setLevelCard("L1");
		}
	}

	public void createTag998(EditBean bibBean) throws DataAccessException, NewTagException, AuthorisationException, ValidationException 
	{
		if (getCurrentItem().getIdPublisher()!=null && getCurrentItem().getIdPublisher().trim().length()>0) {
			ClassificationAccessPoint t998 = (ClassificationAccessPoint ) bibBean.getCatalogItem().findFirstTagByNumber("998");
			if (t998==null){
				BibliographicEditBean biblioBean = (BibliographicEditBean) bibBean;
				biblioBean.createTag998(getCurrentItem().getIdPublisher());
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

	public String getCodEditoreBreve() {
		return codEditoreBreve;
	}

	public void setCodEditoreBreve(String codEditoreBreve) {
		this.codEditoreBreve = codEditoreBreve;
	}

	public String getDenEditore() {
		return denEditore;
	}

	public void setDenEditore(String denEditore) {
		this.denEditore = denEditore;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public List getdigitalCurcyTypeList(){
		return CasaliniCodeListsBean.getDigitalCurcyType().getCodeList(getLocale());
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

	public List getItems() {
		return items;
	}

	public void setItems(List items) {
		this.items = items;
	}

	public CasCache getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(CasCache currentItem) {
		this.currentItem = currentItem;
	}

	public int getBibNumber() {
		return bibNumber;
	}

	public void setBibNumber(int bibNumber) {
		this.bibNumber = bibNumber;
	}
}