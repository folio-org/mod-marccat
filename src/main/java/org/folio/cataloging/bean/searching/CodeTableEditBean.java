package org.folio.cataloging.bean.searching;

import org.apache.commons.validator.GenericValidator;
import org.folio.cataloging.business.authorisation.AuthorisationException;
import org.folio.cataloging.business.authorisation.Permission;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.controller.SessionUtils;
import org.folio.cataloging.dao.DAOCodeTable;
import org.folio.cataloging.dao.DAONoteStandardSubdivision;
import org.folio.cataloging.dao.persistence.BibliographicNoteType;
import org.folio.cataloging.dao.persistence.CodeTable;
import org.folio.cataloging.dao.persistence.T_CLCTN_CST_TYP;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class CodeTableEditBean extends CodeTableBean {

	private String title;
	private List/*<Locale>*/ localeList;
	private Avp selected;
	private List/*<CodeTable>*/ items;
	

	public Locale currentLocale; 

	private boolean insert = false;

	private static final String CODE_TABLE_MAP_FILE_NAME = "/mades/tagman/codeTableMap.properties";
	
	private Properties codeTableMap;
	private List codeTableList;
	private List equivalentCodeTableList;
	
	public List getEquivalentCodeTableList() {
		if(equivalentCodeTableList==null){
			equivalentCodeTableList = new ArrayList();
		}
		return equivalentCodeTableList;
	}

	public void setEquivalentCodeTableList(List equivalentCodeTableList) {
		this.equivalentCodeTableList = equivalentCodeTableList;
	}

	private List  tempCodeTableList;
	

	public List getTempCodeTableList() {
		if(tempCodeTableList==null){
			tempCodeTableList = new ArrayList();
		}
		return tempCodeTableList;
	}

	public void setTempCodeTableList(List tempCodeTableList) {
		this.tempCodeTableList = tempCodeTableList;
	}

	public static CodeTableEditBean getInstance(HttpServletRequest request) {
		CodeTableEditBean bean = 
			(CodeTableEditBean) getSessionAttribute(
				request, CodeTableEditBean.class);
		if (bean == null) {
			bean = new CodeTableEditBean();
			bean.setSessionAttribute(request, CodeTableEditBean.class);
			bean.setAuthorisationAgent(SessionUtils
					.getUserProfile(request.getSession(false))
					.getAuthorisationAgent());
		}
		return bean;
	}

	public void init(Class clazz, String tableName, Locale locale) throws DataAccessException, AuthorisationException {
		checkPermission(Permission.TO_EDIT_CODE_TABLE_ITEM);
		removeSelection();
		this.currentLocale = locale;
		super.setClazz(clazz);
		loadCodeTable();
		setTitle(tableName);
	}
    /*E' stato modificato per avere le liste non ordinate alfabeticamente*/
	public void loadCodeTable() throws DataAccessException {
		List list;
			list = DAOCodeTable.asOptionList(daoCodeTable.getList(getClazz(), currentLocale,false), currentLocale);
			setCodeTable(list);
	}
	
	public void loadEquivalentCodeTable() throws DataAccessException {
		List list;
		Locale LOC_T1 = new Locale("it", "IT");
		Locale LOC_T2 = new Locale("en", "EN");
		if (currentLocale.getLanguage().equals("it")) {
			list = DAOCodeTable.asOptionList(daoCodeTable.getList(
						getClazz(), LOC_T2, false), LOC_T2);
			setEquivalentCodeTableList(list);
		} else if (currentLocale.getLanguage().equals("en")) {
			list = DAOCodeTable.asOptionList(daoCodeTable.getList(
						getClazz(), LOC_T1, false), LOC_T1);
			setEquivalentCodeTableList(list);
		}

	}
	public List getLocaleList() {
		if(localeList==null){
			localeList = new ArrayList();
			String langs = Defaults.getString("cataloguing.codeTable.languages");
			List twoCharLanguages = Arrays.asList(langs.split(","));
			Iterator it = twoCharLanguages.iterator();
			while(it.hasNext()){
				localeList.add(new Locale((String)it.next()));
			}
		}
		return localeList;
	}

	public Avp getSelected() {
		return selected;
	}

	public void removeSelection() {
		this.selected = null;
		this.items = new ArrayList();
		this.insert = false;
	}

	public void select(int selectedIndex) throws DataAccessException {
		selected = (Avp) getCodeTable().get(selectedIndex);
		try {
			List retrieved = daoCodeTable.getEntries(getClazz(), selected.getValue(), false);
			Iterator it = retrieved.iterator();
			items = new ArrayList();
			while (it.hasNext()) {
				CodeTable item = (CodeTable) it.next();
				if(iso3LanguagePresents(item.getLanguage())){
					items.add(item);
				}
			}
			
		} catch (Exception e) {
			throw new ClassCastException(e.getMessage()+". CodeTable: "+getClazz());
		} 
	}
	
	public boolean isEditingCodeTable(){
		return selected != null;
	}
	
	public void save(String code, List longLabels, List shortLabels) throws AuthorisationException, DataAccessException, CodeTableException {
		checkPermission(Permission.TO_EDIT_CODE_TABLE_ITEM);
		Iterator it = items.iterator();
		int i=0;
		int j=0;
		boolean wrongCodeType = false;
		while (it.hasNext()) {
			CodeTable item = (CodeTable) it.next();
			/*if(item instanceof T_CLCTN_CST_TYP){
			  if(item.isNew() && ((String)longLabels.get(i)).indexOf("_")!=-1)
				 throw new CodeTableException(CodeTableException.UNDERSCORE_PRESENT, true, code);
			}*/
			item.setLongText((String)longLabels.get(i));
			item.setShortText((String)shortLabels.get(i));
			if(item.isNew()){
				try {
					item.setExternalCode(code);
				} catch (RuntimeException e) {
					wrongCodeType = true;
				}
			} 
			i++;
		}
		if(wrongCodeType) {
			throw new CodeTableException(CodeTableException.WRONG_CODE_TYPE, true, code);
		}
		Iterator ite = items.iterator();
		while (ite.hasNext()) {
			CodeTable item = (CodeTable) ite.next();
			if(item instanceof T_CLCTN_CST_TYP){
			  if(item.isNew() && ((String)longLabels.get(j)).indexOf("_")!=-1)
				 throw new CodeTableException(CodeTableException.UNDERSCORE_PRESENT, true, code);
			}
			j++;
		}
		validate(code, longLabels, shortLabels);
		
		// if all is ok...
		markItemsChanged();

		daoCodeTable.save(items);
		reloadTable();
		removeSelection();
	}


	public void saveCas(String code, List longLabels, List shortLabels, String optCodeNote, boolean isNote, String delimitator) throws AuthorisationException, DataAccessException, CodeTableException {
		checkPermission(Permission.TO_EDIT_CODE_TABLE_ITEM);
		Iterator it = items.iterator();
		int i=0;
		boolean wrongCodeType = false;
		while (it.hasNext()) {
			CodeTable item = (CodeTable) it.next();
			if(!delimitator.equals("")&& ((String)longLabels.get(i)).indexOf(delimitator)==-1){
			   item.setLongText(delimitator+(String)longLabels.get(i));
			   item.setShortText(delimitator+(String)shortLabels.get(i));
			}
			else{
			  item.setLongText((String)longLabels.get(i));
			  item.setShortText((String)shortLabels.get(i));
			}
			if(isNote)
				new DAONoteStandardSubdivision().saveNoteStandardSubdivision(code, optCodeNote, i);
			if(item.isNew()){
				try {
					item.setExternalCode(code);
					
				} catch (RuntimeException e) {
					wrongCodeType = true;
				}
			} 
			i++;
		}
		
		if(wrongCodeType) {
			throw new CodeTableException(CodeTableException.WRONG_CODE_TYPE, true, code);
		}
		validate(code, longLabels, shortLabels);
		
		// if all is ok...
		markItemsChanged();

		daoCodeTable.save(items);
		reloadTable();
		removeSelection();
	}

	private void saveNoteStandardSubdivision(String code, String optCodeNote,
			int i) throws DataAccessException {
		new DAONoteStandardSubdivision()
		.saveNoteStandardSubdivision(code, optCodeNote, i);
	}

	private void markItemsChanged() {
		if(isInsert()) return;
		Iterator it = items.iterator();
		while (it.hasNext()) {
			CodeTable item = (CodeTable) it.next();
			if(!item.isNew()){
				item.markChanged();
			} 
		}
	}

	/**
	 * Validation to prevent DB access
	 * @param code
	 * @param longLabels
	 * @param shortLabels
	 * @throws CodeTableException
	 */
	private void validate(String code, List longLabels, List shortLabels) throws CodeTableException {
		checkString(code, CodeTableException.NO_CODE);
		for(int i=0; i<longLabels.size(); i++){
			checkString((String) longLabels.get(i), CodeTableException.NO_LONG);
			checkString((String) shortLabels.get(i), CodeTableException.NO_SHORT);
		}
		if(isInsert()){
			Iterator it = getCodeTable().iterator();
			while (it.hasNext()) {
				Avp item = (Avp) it.next();
				// MIKE: this control checks only for string format without any trims
				if(item.getValue().equals(code)){
					throw new CodeTableException(CodeTableException.CODE_ALREADY_PRESENT, true, code);
				}
				checkPresentString(longLabels,shortLabels, item );
			}
		}
	}

	private void checkString(String s, String msgKey) throws CodeTableException{
		if(GenericValidator.isBlankOrNull(s)) {
			throw new CodeTableException(msgKey,true,null);
		}
	}
	private void checkPresentString(List longLabels, List shortLabels, Avp item ) throws CodeTableException{
		for(int i=0; i<longLabels.size(); i++){
			String s= (String)longLabels.get(i);
			if(s.equalsIgnoreCase(item.getLabel())) {
				throw new CodeTableException(CodeTableException.STRING_ALREADY_PRESENT,true,null);
			}
		}
		for(int i=0; i<shortLabels.size(); i++){
			String s= (String)shortLabels.get(i);
			if(s.equalsIgnoreCase(item.getLabel())) {
				throw new CodeTableException(CodeTableException.STRING_ALREADY_PRESENT,true,null);
			}
		}
		
	}
	
	void reloadTable() throws DataAccessException {
		onChange(getClazz());
		loadCodeTable();
	}

	public void cancel() {
		removeSelection();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void delete() throws AuthorisationException {
		checkPermission(Permission.TO_EDIT_CODE_TABLE_ITEM);
		removeSelection();
	}

	public List getItems() {
		return items;
	}
	
	public boolean iso3LanguagePresents(String iso3Language){
		Iterator it = localeList.iterator();
		while (it.hasNext()) {
			Locale loc = (Locale) it.next();
			if(loc.getISO3Language().equals(iso3Language)) return true;
		}
		return false;
		// return localeList.contains(new Locale(twoCharLanguage));
	}
	
	public boolean isLanguagePresents(Locale loc){
		return localeList.contains(loc);
	}

	public void create() throws CodeTableException {
		selected = null;
		int nLangs = getLocaleList().size();
		try {
			
			for(int i=0; i<nLangs; i++){
				CodeTable item = (CodeTable) getClazz().newInstance();
				//Carmen aggiunto progressivo per T_SINGLE
				int code = item.getNextNumber();
				if(code==0)
				  item.setExternalCode("");
				else
				   item.setExternalCode(""+code);
				//BY Carmen
				//item.setLanguage((String)localeList.get(i));
				item.setLanguage(((Locale)localeList.get(i)).getISO3Language());
				items.add(item);
			}
			selected = new Avp(); // dummy value
			insert = true;
		} catch (DataAccessException e) {
			throw new CodeTableException(getClazz(), e);
		} catch (InstantiationException e) {
			throw new CodeTableException(getClazz(), e);
		} catch (IllegalAccessException e) {
			throw new CodeTableException(getClazz(), e);
		}
	}

	public boolean isInsert() {
		return insert;
	}
	
	// ------------------------
	// Loading CodeTables props
	// ------------------------
	public Properties getCodeTableMap() throws IOException{
		if(codeTableMap==null){
			codeTableMap = new Properties();
			InputStream stream = getClass().getResourceAsStream(CODE_TABLE_MAP_FILE_NAME);
			if(stream==null) {
				throw new FileNotFoundException(CODE_TABLE_MAP_FILE_NAME);
			}
			codeTableMap.load(stream);
		}
		return codeTableMap;
	}
	
	public String getCodeTableName(String propertyName) throws CodeTableException {
		try {
			String name =  getCodeTableMap().getProperty(propertyName);
			if(name==null){
				throw new CodeTableException(CodeTableException.PROPERTY_NOT_FOUND,true, propertyName);
			}
			return name;
		} catch (IOException e) {
			throw new CodeTableException(CodeTableException.MAP_NOT_FOUND,true, CODE_TABLE_MAP_FILE_NAME);
		}
	}
	
	public List getCodeTableList() throws IOException{
		if(codeTableList==null){
			Properties p = getCodeTableMap();
			codeTableList = Collections.list(p.elements());
		}
		return codeTableList;
	}
	
	public void addTemp(int selectedIndex)throws CodeTableException {
		Avp selectedTemp = (Avp) getCodeTable().get(selectedIndex);
		if(getTempCodeTableList().contains(selectedTemp))
			throw new CodeTableException(CodeTableException.DUPLICATE_CODE,true, selectedTemp.getValue());
		else
	      getTempCodeTableList().add(selectedTemp);
	
	}
		
	public void deleteItem(String code, short category)
			throws CodeTableException, DataAccessException {
		if (category == 6 && !daoCodeTable.findHeading(code)) {
			daoCodeTable.deleteCode(getClazz(), code);
			reloadTable();
			removeSelection();
		} else if (category == 7) {
			daoCodeTable.deleteCode(getClazz(), code);
			daoCodeTable.deleteNoteSubCode(code);
			reloadTable();
			removeSelection();
		} else if (getClazz().getName().equals("T_CAS_MNGRL_LVL_TYP")
				&& daoCodeTable.getCodeCache(code).equals("")) {
			daoCodeTable.deleteCode(getClazz(), code);
			reloadTable();
			removeSelection();
		} else
			throw new CodeTableException(CodeTableException.USE_CODE, true,
					code);
	}
	public List getNoteCodeList()
	throws DataAccessException {
		return DAOCodeTable.asOptionList(daoCodeTable.getCorrelatedList(BibliographicNoteType.class,true," and bc.key.marcSecondIndicator <> '@' and bc.databaseFirstValue = ct.code "), currentLocale);
		//return DAO_CODE_TABLE.getCorrelatedList(BibliographicNoteType.class,true," and bc.key.marcSecondIndicator <> '@' and bc.databaseFirstValue = ct.code ");
   }
   
  public int getNoteCode(int code) throws DataAccessException{
    return daoCodeTable.getCodeNote(code);
  }

	public Locale getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
	}

	boolean isDefaultLanguage(Locale loc){
		return loc.equals(getCurrentLocale());
	}
	public void setItems(List items) {
		this.items = items;
	}

}
