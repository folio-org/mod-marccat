package org.folio.cataloging.bean.digital;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.bean.LibrisuiteBean;
import org.folio.cataloging.bean.cataloguing.common.EditBean;
import org.folio.cataloging.business.cataloguing.bibliographic.*;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.common.RecordNotFoundException;
import org.folio.cataloging.business.controller.SessionUtils;
import org.folio.cataloging.business.digital.DigitalDoiException;
import org.folio.cataloging.business.digital.DigitalLevelException;
import org.folio.cataloging.business.digital.RequiredFieldsException;
import org.folio.cataloging.dao.BibliographicCatalogDAO;
import org.folio.cataloging.dao.DAOCache;
import org.folio.cataloging.dao.DAOCasDigAdmin;
import org.folio.cataloging.dao.persistence.CNTL_NBR;
import org.folio.cataloging.dao.persistence.CasDigAdmin;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.util.StringText;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public class DigitalDoiBean extends LibrisuiteBean  
{	
	private String amicusNumber;
	private String title;
	private String titleOpera;
	private String autor;
	private String annoPubl;
	private String linguaOpera;
	private String paesePubl;
	private String isbn;
	private String progr;
	private String editore;
	private String notificationType;
	private String amicusNumberMother773;
	private String amicusNumberMother097;
	private String amicusNumberMother;
	private String DOIEsistente;
	private String CreaDOI; 
	private String AnnullaDoi;
	private String password;
	private BibliographicNoteTag tag856;
	private String pagTot; 
	private String issn;
	private String ntrLevel;
	private String progNum;
	private String descFascicolo;
	private String editoreBreve;
	private String url_1;
	private String url_2;
	private static final String DIGITAL_DOI_FOR_ARTRIV = Defaults.getString("digital.doi.013");
	private static final String DIGITAL_DOI_FOR_PARTMON = Defaults.getString("digital.doi.015");
	private static final Log logger = LogFactory.getLog(DigitalDoiBean.class);
	private List messages = new ArrayList();
	
	public DigitalDoiBean(){
	}
	
	public DigitalDoiBean(EditBean editBean, HttpServletRequest request) throws NumberFormatException, DataAccessException, DigitalDoiException, DigitalLevelException 
	{		
		loadForDoi(editBean, request);
	}
	
	public void loadForDoi(EditBean editBean, HttpServletRequest request) throws NumberFormatException, DataAccessException, DigitalDoiException, DigitalLevelException 
	{
		refresh();
		
		setAmicusNumber(editBean.getCatalogItem().getAmicusNumber().toString());
		
		setNtrLevel(editBean.getOptDigitalLevel());
		if (!"013".equals(ntrLevel) && !"015".equals(ntrLevel))
		{
			//controllo che abilita il doi anche su 011 e 014 (bug 3081)
			if ( !("011".equals(editBean.getOptDigitalLevel()) || "014".equals(editBean.getOptDigitalLevel())) && !editBean.isInternalDoiPermitted())
			{
				throw new DigitalLevelException();
			}
		}
		
		/* Se sto chiamando il servizio dal tag856 aperto */
		if ("856".equals(editBean.getCurrentTag().getMarcEncoding().getMarcTag())){
			setTag856((BibliographicNoteTag) editBean.getCurrentTag());
		/* altrimenti ciclo per prendere l'856 */
		}else {
			BibliographicNoteTag noteTag = editBean.get856WithDoi();
			if (noteTag!=null){
				setTag856(noteTag);
			} else {
				logger.error("A_T_T_E_N_Z_I_O_N_E : previsto tag856 con $W impostato - disallineamento!!!");
				throw new DataAccessException();
			}
		}
		
		if (getTag856()!= null && getTag856().getStringText().getSubfieldsWithCodes("w").getDisplayText().trim().length()>0)
			setNotificationType("07");
		else
			setNotificationType("06");
		
		//Natascia bug 3081
		if ( ("011".equals(editBean.getOptDigitalLevel()) || "014".equals(editBean.getOptDigitalLevel())) && editBean.isInternalDoiPermitted())
		{
			setCreaDOI("YES");
			setAnnullaDoi("NO");
			return;
		}
		
	    TitleAccessPoint t245 = (TitleAccessPoint)editBean.getCatalogItem().findFirstTagByNumber("245");
		if (t245 != null) {
			setTitle(t245.getDescriptor().getDisplayText() + " " + t245.getAccessPointStringText().getDisplayText());
		}
		
		MaterialDescription t008 = (MaterialDescription) editBean.getCatalogItem().findFirstTagByNumber("008");
		if (t008!=null){
			setLinguaOpera(t008.getLanguageCode());	
			setPaesePubl(t008.getMarcCountryCode());
		}
		
		/* 20100212 inizio: modifica per il tag260 */		
		List listTag260 = searchTag260(editBean);
		if (listTag260.size()!=0) {
			PublisherManager manager = chooseTag260(listTag260);
			if (manager!=null){
				if (manager.getStringText().getSubfieldsWithCodes("c").getDisplayText().toString().trim().length()>0)
					setAnnoPubl(manager.getStringText().getSubfieldsWithCodes("c").getDisplayText());
				if (manager.getStringText().getSubfieldsWithCodes("b").getDisplayText().toString().trim().length()>0)
					setEditore(manager.getStringText().getSubfieldsWithCodes("b").getDisplayText());
			}			
		}
		
		/* Solo per "015" (parte opera) */ 
		if (getNtrLevel().equalsIgnoreCase("015")) {
			ControlNumberAccessPoint t020 = (ControlNumberAccessPoint) editBean.getCatalogItem().findFirstTagByNumber("020");
			if (t020 != null) {
				if (t020.getStringText().getSubfieldsWithCodes("a").getDisplayText().toString().trim().length()>0){
					setIsbn(t020.getStringText().getSubfieldsWithCodes("a").getDisplayText());
				}
			}
		}
		/* Solo per "013" (articolo rivista) */ 	
		if (getNtrLevel().equalsIgnoreCase("013")) {
			ControlNumberAccessPoint t022 = (ControlNumberAccessPoint) editBean.getCatalogItem().findFirstTagByNumber("022");
			if (t022 != null) {
				if (t022.getStringText().getSubfieldsWithCodes("a").getDisplayText().toString().trim().length()>0){
					setIssn(t022.getStringText().getSubfieldsWithCodes("a").getDisplayText());
				}
			}
		}
		
		ControlNumberAccessPoint t097 = (ControlNumberAccessPoint) editBean.getCatalogItem().findFirstTagByNumber("097");
		if (t097!=null) { 
			if (t097.getStringText().getSubfieldsWithCodes("d").getDisplayText().toString().trim().length()>0)
				setProgr(t097.getStringText().getSubfieldsWithCodes("d").getDisplayText().toString());
			if (t097.getStringText().getSubfieldsWithCodes("a").getDisplayText().toString().trim().length()>0)
				setAmicusNumberMother097(t097.getStringText().getSubfieldsWithCodes("a").getDisplayText().toString());
		}	
		
		BibliographicRelationshipTag tag773 = (BibliographicRelationshipTag)editBean.getCatalogItem().findFirstTagByNumber("773");
		if (tag773!=null) {
			if (tag773.getStringText().getSubfieldsWithCodes("t").getDisplayText().toString().trim().length()>0)
				setTitleOpera(tag773.getStringText().getSubfieldsWithCodes("t").getDisplayText().toString());
			if (tag773.getStringText().getSubfieldsWithCodes("w").getDisplayText().toString().trim().length()>0)
				setAmicusNumberMother773(tag773.getStringText().getSubfieldsWithCodes("w").getDisplayText().toString());
		}else if (!getAmicusNumberMother097().equalsIgnoreCase(""))
			setTitleOpera(getTitleByAmicusNumber(Integer.parseInt(getAmicusNumberMother097()), SessionUtils.getCataloguingView(request)));
	
		if (getAmicusNumberMother773().equals("")) {
			if (getAmicusNumberMother097().equals(""))
				throw new DigitalDoiException();	
			else
				setAmicusNumberMother(getAmicusNumberMother097());
		}else 
			setAmicusNumberMother(getAmicusNumberMother773());	

		/* Ricerca il codice isbn o issn */ 
		String codice = null;
		int typeCode = 0;
		if (getNtrLevel().equalsIgnoreCase("013")) {
			codice = getIssn();
			typeCode = 10;
		} else if (getNtrLevel().equalsIgnoreCase("015")) {
			codice = getIsbn();
			typeCode = 9;
		}
		
		if ("".equals(codice) && !"".equals(getAmicusNumberMother())){
			BibliographicCatalogDAO catalogDao = new BibliographicCatalogDAO();
			List lista =  catalogDao.loadAccessPointTags(ControlNumberAccessPoint.class, Integer.parseInt(getAmicusNumberMother()), SessionUtils.getCataloguingView(request)); 
			for (int i = 0; i < lista.size(); i++) {
				ControlNumberAccessPoint cntlNumber = (ControlNumberAccessPoint) lista.get(i);					
				if ((((CNTL_NBR)cntlNumber.getDescriptor()).getTypeCode())==typeCode) {
					StringText text = new StringText(cntlNumber.getDescriptor().getStringText());
					if (getNtrLevel().equalsIgnoreCase("015")) 
						setIsbn(text.getSubfieldsWithCodes("a").getDisplayText());
					else if (getNtrLevel().equalsIgnoreCase("013")) 
						setIssn(text.getSubfieldsWithCodes("a").getDisplayText());
				}
			}
		}
		
		DAOCasDigAdmin dao = new DAOCasDigAdmin();
		List admin = (dao.loadCasDigAdmin(editBean.getCatalogItem().getAmicusNumber().intValue()));
		if (admin.size()!=0) {
			CasDigAdmin casDigAdmin = (CasDigAdmin) admin.get(0);
			if (casDigAdmin.getCodEditore()!=null && getEditore().trim().length()>0) 
				setEditore(casDigAdmin.getDenEditore());
			if (casDigAdmin.getPageTot()==null)
				setPagTot("");
			else
				setPagTot(casDigAdmin.getPageTot().toString());
			if (casDigAdmin.getIdFascicolo()==null)
				setProgNum("");
			else
				setProgNum(casDigAdmin.getIdFascicolo());
			if (casDigAdmin.getDescrizione()==null)
				setDescFascicolo("");
			else
				setDescFascicolo(casDigAdmin.getDescrizione());
			
			if (casDigAdmin.getCodEditoreBreve()!=null && casDigAdmin.getCodEditoreBreve().trim().length()>0){
				setEditoreBreve(casDigAdmin.getCodEditoreBreve());
			}
		}
		
		if (getTag856()!=null) {  
			setUrl_1("");
			setUrl_2("");
			Subfield subField = null;
			List urlList = editBean.getAdditionalUrl856(getTag856());
			if (urlList.size()>2){
				logger.debug("SEND DOI --> ATTENZIONE : ci sono + di 2 url aggiuntive");
			}
			if (urlList.size()>0){
				for (int t = 0; t < urlList.size(); t++) {
					subField = (Subfield)urlList.get(t);
					if (url_1.equals("")){
						url_1=subField.getContent();
					}else if (url_2.equals("")){ 
						url_2=subField.getContent();
					}else {
						break;
					}
				}	
			}
		}
		
		List appo = ((BibliographicItem)editBean.getCatalogItem()).getOrderableNames();
		Iterator it = appo.iterator();
		while (it.hasNext()) {
			NameAccessPoint name = (NameAccessPoint) it.next();
			setAutor(getAutor().concat(name.getDescriptor().getDisplayText()));
			if (it.hasNext()) {
				setAutor(getAutor().concat(" ; "));
			}
		}
		
		if (editBean.isModifyDoi())
			setDOIEsistente(getTag856().getStringText().getSubfieldsWithCodes("w").getDisplayText());
		else 
			setDOIEsistente("");
		
		setCreaDOI("YES");
		setPassword("primesource09");
		setAnnullaDoi("NO");
	}
	
	public PublisherManager chooseTag260(List listTag260) 
	{
		PublisherManager publisherManager = null; 
		if (listTag260.size()==1){
		/* Se c'e' un solo tag260 (dovrebbe essere 260 ##) prendo i suoi dati */
			publisherManager = (PublisherManager) listTag260.get(0);
		} else if (listTag260.size()>1){
			/* Se ci sono piu' tag260 prendo i dati del tag260 3# */
			for (int i = 0; i < listTag260.size(); i++) {
				/* Prendo i dati del tag260 3# che e' l'ultimo */
				if (((PublisherManager) listTag260.get(i)).getCorrelation(1)==382 && 
					((PublisherManager) listTag260.get(i)).getCorrelation(2)==-1  &&
					((PublisherManager) listTag260.get(i)).getCorrelation(3)==-1){
					publisherManager = (PublisherManager) listTag260.get(i);
				}
			}
		}
		return publisherManager;
	}

	public List searchTag260(EditBean bean) throws MarcCorrelationException, DataAccessException 
	{
		List tags = bean.getCatalogItem().getTags();
		List tags260 = new ArrayList();
		Iterator it = tags.iterator();
		while (it.hasNext()) {
			Tag tag = (Tag) it.next();
			 if(tag instanceof PublisherManager) {
				if (tag.getMarcEncoding().getMarcTag().equalsIgnoreCase("260"))
					tags260.add(tag);
			 }
		}
		return tags260;
	}
	
	private String getTitleByAmicusNumber(int amicusNumber, int view) throws RecordNotFoundException, DataAccessException
	{
		 String result = new String();
		 DAOCache dcbid = new DAOCache();
		 result = dcbid.load(amicusNumber, view).getTitleHeadingMainStringText();
		 
		 return result;
	}

	private void refresh()
	{
		setTitle("");
		setTitleOpera("");
		setAutor("");
		setAnnoPubl("");
		setLinguaOpera("");
		setPaesePubl("");
		setIsbn("");
		setProgr("");
		setEditore("");
		setNotificationType("");
		setAmicusNumberMother773("");
		setAmicusNumberMother097("");
		setAmicusNumberMother("");
		setDOIEsistente("");
		setCreaDOI(""); 
		setAnnullaDoi("");
		setPassword("");
		setTag856(null);
		setPagTot("");
		setIssn("");
		setProgNum("");
		setDescFascicolo("");
		setEditoreBreve("");
		setUrl_1("");
		setUrl_2("");
	}

	public String httpPost() throws HttpException, IOException, RequiredFieldsException 
	{

		messages = requiredFields();
		if (messages.size()>0){
			throw new RequiredFieldsException(messages);
		}

		HttpClient client = new HttpClient();
		PostMethod httppost = null;
		
		if (getNtrLevel().equalsIgnoreCase("013")){
			httppost = new PostMethod(DIGITAL_DOI_FOR_ARTRIV);
		}else if (getNtrLevel().equalsIgnoreCase("015")){
			httppost = new PostMethod(DIGITAL_DOI_FOR_PARTMON);
		}
		
	    BufferedReader br = null;
	    
	    String codiceDOI = "";
		
		if (getNtrLevel().equalsIgnoreCase("015"))
			httppost.addParameters(loadMonogrParameters());
		else if (getNtrLevel().equalsIgnoreCase("013")) 
			httppost.addParameters(loadArtParameters());           
		
		int returnCode;
		
		returnCode = client.executeMethod(httppost);
		
		if (returnCode == HttpStatus.SC_OK) {
			br = new BufferedReader(new InputStreamReader(httppost.getResponseBodyAsStream()));
		    String readLine;
		    while(((readLine = br.readLine()) != null)) {
		    	codiceDOI = readLine.trim();
		    	logger.debug("Codice DOI : " + codiceDOI);
		    }
		}else {
           logger.debug("action failed, response=" + HttpStatus.getStatusText(returnCode));
           httppost.releaseConnection();
           throw new HttpException();
        }
		
		httppost.releaseConnection();
	    if(br != null)  br.close(); 
	    
	    if ((!"".equals(getDOIEsistente()) || (getDOIEsistente()!=null)) && getCreaDOI().equalsIgnoreCase("NO") ) {
	    	codiceDOI = getDOIEsistente();
	    }
		
		return codiceDOI;
	}

	private NameValuePair[] loadArtParameters()
	{
		NameValuePair [] listaParameters = new NameValuePair[20];
		NameValuePair element = null;
		
		logger.debug("****  Parametri per Articolo di rivista  ****");
		logger.debug("Amicus Number        : " + getAmicusNumber());
		logger.debug("Titolo Rivista       : " + getTitleOpera());
		logger.debug("Descrizione fascicolo : " + getDescFascicolo());
		logger.debug("Issn                 : " + getIssn());
		logger.debug("Editore              : " + getEditore());
		logger.debug("Progressivo numerico : " + getProgNum());
		logger.debug("Progressivo articolo : " + getProgr());
		logger.debug("Titolo Articolo      : " + getTitle());
		logger.debug("Autore Articolo      : " + getAutor());
		logger.debug("Notification Type    : " + getNotificationType());		
		logger.debug("Anno pubbl           : " + getAnnoPubl());
		logger.debug("Paese publ           : " + getPaesePubl().toUpperCase());
		logger.debug("Totale Pagine        : " + getPagTot());
		logger.debug("DoiEsistente         : " + getDOIEsistente());
		logger.debug("CreaDoi              : " + getCreaDOI());
		logger.debug("AnnullaDoi           : " + getAnnullaDoi());
		logger.debug("Password             : " + getPassword());
		logger.debug("Editore breve        : " + getEditoreBreve());
		logger.debug("Url aggiuntiva 1     : " + getUrl_1());
		logger.debug("Url aggiuntiva 2     : " + getUrl_2());

		element = new NameValuePair("AmicusNumber", getAmicusNumber());
		listaParameters[0]=element;
		element = new NameValuePair("TitoloRivista", getTitleOpera());
		listaParameters[1]=element;
		element = new NameValuePair("DescrizioneFascicolo", getDescFascicolo());
		listaParameters[2]=element;
		element = new NameValuePair("ISSN", getIssn());
		listaParameters[3]=element;
		element = new NameValuePair("DenominazioneEditore", getEditore());
		listaParameters[4]=element;
		element = new NameValuePair("ProgressivoNumero", getProgNum());
		listaParameters[5]=element;
		element = new NameValuePair("ProgressivoArticolo", getProgr());
		listaParameters[6]=element;
		element = new NameValuePair("TitoloArticolo", getTitle());
		listaParameters[7]=element;
		element = new NameValuePair("AutoreArticolo", getAutor());
		listaParameters[8]=element;
		element = new NameValuePair("NotificationType", getNotificationType());
		listaParameters[9]=element;
		element = new NameValuePair("AnnoPubblicazione", getAnnoPubl());
		listaParameters[10]=element;
		element = new NameValuePair("PaesePubblicazione", getPaesePubl().toUpperCase());
		listaParameters[11]=element;
		element = new NameValuePair("TotalePagine", getPagTot());
		listaParameters[12]=element;
		element = new NameValuePair("DOIEsistente", getDOIEsistente());
		listaParameters[13]=element;
		element = new NameValuePair("CreaDOI", getCreaDOI());
		listaParameters[14]=element;
		element = new NameValuePair("AnnullaDOI", getAnnullaDoi());
		listaParameters[15]=element;
		element = new NameValuePair("Password", getPassword());
		listaParameters[16]=element;
		element = new NameValuePair("CodiceEditore", getEditoreBreve());
		listaParameters[17]=element;
		element = new NameValuePair("Url1", getUrl_1());
		listaParameters[18]=element;
		element = new NameValuePair("Url2", getUrl_2());
		listaParameters[19]=element;
		
		return listaParameters;
	}

	private NameValuePair [] loadMonogrParameters() 
	{
		NameValuePair [] listaParameters = new NameValuePair[18];
		NameValuePair element = null;
		
		logger.debug("****  Parametri per Parti Monografie  ****");
		logger.debug("Amicus Number        : " + getAmicusNumber());
		logger.debug("Titolo parte         : " + getTitle());
		logger.debug("Titolo opera intera  : " + getTitleOpera());
		logger.debug("Autore               : " + getAutor());
		logger.debug("Anno pubbl           : " + getAnnoPubl());
		logger.debug("Lingua opera         : " + getLinguaOpera().toLowerCase());
		logger.debug("Paese publ           : " + getPaesePubl().toUpperCase());
		logger.debug("Isbn                 : " + getIsbn());
		logger.debug("Progres parte        : " + getProgr());
		logger.debug("Editore              : " + getEditore());
		logger.debug("Notification Type    : " + getNotificationType());
		logger.debug("DoiEsistente         : " + getDOIEsistente());
		logger.debug("CreaDoi              : " + getCreaDOI());
		logger.debug("AnnullaDoi           : " + getAnnullaDoi());
		logger.debug("Password             : " + getPassword());
		logger.debug("Editore breve        : " + getEditoreBreve());
		logger.debug("Url aggiuntiva 1     : " + getUrl_1());
		logger.debug("Url aggiuntiva 2     : " + getUrl_2());
		
		element = new NameValuePair("AmicusNumber", getAmicusNumber());
		listaParameters[0]=element;
		element = new NameValuePair("TitoloOperaIntera", getTitleOpera());
		listaParameters[1]=element;
		element = new NameValuePair("TitoloParte", getTitle());
		listaParameters[2]=element;
		element = new NameValuePair("Autore", getAutor());
		listaParameters[3]=element;
		element = new NameValuePair("ProgressivoParte", getProgr());
		listaParameters[4]=element;
		element = new NameValuePair("DenominazioneEditore", getEditore());
		listaParameters[5]=element;
		element = new NameValuePair("NotificationType", getNotificationType());
		listaParameters[6]=element;
		element = new NameValuePair("LinguaOpera", getLinguaOpera().toLowerCase());
		listaParameters[7]=element;
		element = new NameValuePair("AnnoPubblicazione", getAnnoPubl());
		listaParameters[8]=element;
		element = new NameValuePair("PaesePubblicazione", getPaesePubl().toUpperCase());
		listaParameters[9]=element;
		element = new NameValuePair("ISBN", getIsbn());
		listaParameters[10]=element;
		element = new NameValuePair("DOIEsistente", getDOIEsistente());
		listaParameters[11]=element;
		element = new NameValuePair("CreaDOI", getCreaDOI());
		listaParameters[12]=element;
		element = new NameValuePair("AnnullaDOI", getAnnullaDoi());
		listaParameters[13]=element;
		element = new NameValuePair("Password", getPassword());
		listaParameters[14]=element;
		element = new NameValuePair("CodiceEditore", getEditoreBreve());
		listaParameters[15]=element;
		element = new NameValuePair("Url1", getUrl_1());
		listaParameters[16]=element;
		element = new NameValuePair("Url2", getUrl_2());
		listaParameters[17]=element;
		
		return listaParameters;
	}
	
	public List requiredFields()
	{
		List errorList = new ArrayList();
		
		if (getNtrLevel().equalsIgnoreCase("015")){
			if (!(getTitle().trim().length()>0)){
				errorList.add("doi.title");
			}
			if (!(getTitleOpera().trim().length()>0)){
				errorList.add("doi.title.opera");
			}
			if (!(getAnnoPubl().trim().length()>0)){
				errorList.add("doi.publ.year");
			}
			if (!(getLinguaOpera().trim().length()>0)){
				errorList.add("doi.lang.opera");
			}
			if (!(getPaesePubl().trim().length()>0)){
				errorList.add("doi.publ.country");
			}
			if (!(getIsbn().trim().length()>0)){
				errorList.add("doi.isbn");
			}
			if (!(getProgr().trim().length()>0)){
				errorList.add("doi.progr");
			}
			if (!(getEditore().trim().length()>0)){
				errorList.add("doi.descr.publisher");
			}
			if (!(getEditoreBreve().trim().length()>0)){
				errorList.add("doi.short.publisher");
			}
		}else if (getNtrLevel().equalsIgnoreCase("013")){	
			if (!(getTitleOpera().trim().length()>0)){
				errorList.add("doi.title.opera");
			}
			if (!(getDescFascicolo().trim().length()>0)){
				errorList.add("doi.descr.part");
			}
			if (!(getIssn().trim().length()>0)){
				errorList.add("doi.issn");
			}
			if (!(getEditore().trim().length()>0)){
				errorList.add("doi.descr.publisher");
			}
			if (!(getEditoreBreve().trim().length()>0)){
				errorList.add("doi.short.publisher");
			}
			if (!(getProgNum().trim().length()>0)){
				errorList.add("doi.progr.part");
			}
			if (!(getTitle().trim().length()>0)){
				errorList.add("doi.title");
			}
			if (!(getAnnoPubl().trim().length()>0)){
				errorList.add("doi.publ.year");
			}
			if (!(getPaesePubl().trim().length()>0)){
				errorList.add("doi.publ.country");
			}
			if (!(getPagTot().trim().length()>0)){
				errorList.add("doi.total.pages");
			}		
		}
		return errorList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleOpera() {
		return titleOpera;
	}

	public void setTitleOpera(String titleOpera) {
		this.titleOpera = titleOpera;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getAnnoPubl() {
		return annoPubl;
	}

	public void setAnnoPubl(String annoPubl) {
		this.annoPubl = annoPubl;
	}

	public String getLinguaOpera() {
		return linguaOpera;
	}

	public void setLinguaOpera(String linguaOpera) {
		this.linguaOpera = linguaOpera;
	}

	public String getPaesePubl() {
		return paesePubl;
	}

	public void setPaesePubl(String paesePubl) {
		this.paesePubl = paesePubl;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getProgr() {
		return progr;
	}

	public void setProgr(String progr) {
		this.progr = progr;
	}

	public String getEditore() {
		return editore;
	}

	public void setEditore(String editore) {
		this.editore = editore;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getAmicusNumberMother() {
		return amicusNumberMother;
	}

	public void setAmicusNumberMother(String amicusNumberMother) {
		this.amicusNumberMother = amicusNumberMother;
	}

	public String getDOIEsistente() {
		return DOIEsistente;
	}

	public void setDOIEsistente(String esistente) {
		DOIEsistente = esistente;
	}

	public String getCreaDOI() {
		return CreaDOI;
	}

	public void setCreaDOI(String creaDOI) {
		CreaDOI = creaDOI;
	}

	public String getAmicusNumberMother773() {
		return amicusNumberMother773;
	}

	public void setAmicusNumberMother773(String amicusNumberMother773) {
		this.amicusNumberMother773 = amicusNumberMother773;
	}

	public String getAmicusNumberMother097() {
		return amicusNumberMother097;
	}

	public void setAmicusNumberMother097(String amicusNumberMother097) {
		this.amicusNumberMother097 = amicusNumberMother097;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAnnullaDoi() {
		return AnnullaDoi;
	}

	public void setAnnullaDoi(String annullaDoi) {
		AnnullaDoi = annullaDoi;
	}

	public BibliographicNoteTag getTag856() {
		return tag856;
	}

	public void setTag856(BibliographicNoteTag tag856) {
		this.tag856 = tag856;
	}

	public String getAmicusNumber() {
		return amicusNumber;
	}

	public void setAmicusNumber(String amicusNumber) {
		this.amicusNumber = amicusNumber;
	}

	public String getPagTot() {
		return pagTot;
	}

	public void setPagTot(String pagTot) {
		this.pagTot = pagTot;
	}

	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	public String getNtrLevel() {
		return ntrLevel;
	}

	public void setNtrLevel(String ntrLevel) {
		this.ntrLevel = ntrLevel;
	}

	public String getProgNum() {
		return progNum;
	}

	public void setProgNum(String progNum) {
		this.progNum = progNum;
	}

	public String getDescFascicolo() {
		return descFascicolo;
	}

	public void setDescFascicolo(String descFascicolo) {
		this.descFascicolo = descFascicolo;
	}

	public String getEditoreBreve() {
		return editoreBreve;
	}

	public void setEditoreBreve(String editoreBreve) {
		this.editoreBreve = editoreBreve;
	}

	public String getUrl_1() {
		return url_1;
	}

	public void setUrl_1(String url_1) {
		this.url_1 = url_1;
	}

	public String getUrl_2() {
		return url_2;
	}

	public void setUrl_2(String url_2) {
		this.url_2 = url_2;
	}

	public List getMessages() {
		return messages;
	}

	public void setMessages(List messages) {
		this.messages = messages;
	}
}