package librisuite.bean.searching;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import librisuite.business.authorisation.AuthorisationException;
import librisuite.business.authorisation.Permission;
import librisuite.business.codetable.DAOCodeTable;
import librisuite.business.codetable.ValueLabelElement;
import librisuite.business.common.DataAccessException;
import librisuite.hibernate.CodeTable;

import com.libricore.librisuite.controller.SessionUtils;

public class CodeTableGridBean extends CodeTableEditBean {
	
	/**
	 * Griglia contenente la codeTable in n colonne, una per ogni lingua
	 */
	private CodeTableGrid grid;
	
	private List/*<Integer>*/ indexSearchList = new ArrayList();
	
	private int sortColumn = -1;
	private boolean sortAscending = true;
    private String subfieldIndex;
    private String closeOperation;
    private String searchString;
	
	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getCloseOperation() {
		return closeOperation;
	}

	public void setCloseOperation(String closeOperation) {
		this.closeOperation = closeOperation;
	}

	public String getSubfieldIndex() {
		return subfieldIndex;
	}

	public void setSubfieldIndex(String subfieldIndex) {
		this.subfieldIndex = subfieldIndex;
	}
	
	public static CodeTableEditBean getInstance(HttpServletRequest request) {
		CodeTableGridBean bean = 
			(CodeTableGridBean) CodeTableGridBean.getSessionAttribute(
				request, CodeTableGridBean.class);
		if (bean == null) {
			bean = new CodeTableGridBean();
			bean.setSessionAttribute(request, CodeTableGridBean.class);
			bean.setAuthorisationAgent(SessionUtils
					.getUserProfile(request.getSession(false))
					.getAuthorisationAgent());
		}
		return bean;
	}

	public void loadCodeTable() throws DataAccessException {
		grid = new CodeTableGrid();
		List list = loadOptionList(getCurrentLocale());
		setCodeTable(list);
		Iterator it = getLocaleList().iterator();
		while (it.hasNext()) {
			Locale lang = (Locale) it.next();
			if(!isDefaultLanguage(lang)){
				grid.add(lang, loadOptionList(lang));
			}
		}
	}
	
	private List loadOptionList(Locale locale) throws DataAccessException{
		return DAOCodeTable.asOptionList(daoCodeTable.getList(getClazz(), locale, false), locale);
	}
	
	/**
	 * Ridefinisce il set della codetable di default
	 */
	public void setCodeTable(List list) {
		grid.add(getCurrentLocale(), list);
	}

	/**
	 * Ridefinisce il get della codetable di default
	 */
	public List getCodeTable(){
		return grid.getCodeTableList(getCurrentLocale());
	}

	public CodeTableGrid getGrid() {
		return grid;
	}
	
	/**
	 * for testing purpose only
	 * @param grid
	 */
	protected void setGrid(CodeTableGrid grid) {
		this.grid = grid;
	}
	
	public void sort(int numColonna){
		if(numColonna==sortColumn) {
			sortAscending = (!sortAscending);
		}
		grid.sort(numColonna, sortAscending);
		sortColumn = numColonna;
		clearIndexList();
	}
	
	/**
	 * Esegue una ricerca e produce la lista delle righe che contengono la stringa cercata
	 * cercando indifferentemente su tutte le lingue
	 * @param searchString
	 */
	public void find(String searchString){
		indexSearchList = new ArrayList();
		List hor = grid.getHorizontalGrid();
		List hor2= new ArrayList();
		int nRows = hor.size();
		for(int r=0; r<nRows; r++){
			List row = (List) hor.get(r);
			if(isPresent(row, searchString.toLowerCase())){
				indexSearchList.add(new Integer(r));
				hor2.add(row);
			}
		}
		
		grid.getHorizontalGrid().removeAll(hor);
		grid.getHorizontalGrid().addAll(hor2);
	}

	

	/**
	 * Restituisce l'indice dell'elemento selezionato
	 * <li>sia con la tabella completa
	 * <li>sia con la tabella filtrata
	 * @param n l'indice relativo dell'elemento selezionato della tabella filtrata o meno
	 * @return l'indice corretto dell'elemento nella tabella originale
	 */
	public int getRealIndex(int n){
		return indexSearchList.isEmpty()?n:((Integer)indexSearchList.get(n)).intValue();
	}
	
	private final boolean isPresent(List row, String searchString) {
		int nCols = row.size();
		for(int c=0; c<nCols; c++){
			ValueLabelElement element = (ValueLabelElement) row.get(c);
			if(element.getLabel().toLowerCase().indexOf(searchString)>=0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Ripulisce la precedente ricerca eseguita. Generalmente è necessario
	 * ripulire quando si esegue una variazione alla tabella o un sort
	 */
	public void clearIndexList(){
		indexSearchList = new ArrayList();
	}
	
	/**
	 * Forza la pulitura della indexSearchList
	 */
	void reloadTable() throws DataAccessException {
		clearIndexList();
		super.reloadTable();
	}
	
 	public List getIndexSearchList() {
		return indexSearchList;
	}

	public int getColumnSorted() {
		return sortColumn;
	}

	public boolean isSortAscending() {
		return sortAscending;
	}
	public void init(Class clazz, String tableName, Locale locale) throws DataAccessException, AuthorisationException {
		checkPermission(Permission.TO_EDIT_CODE_TABLE_ITEM);
		removeSelection();
		setCurrentLocale(locale);
		super.setClazz(clazz);
		loadCodeTable();
		setTitle(tableName);
		setSearchString("");
	}
    
	public String getLabelWithoutSubfield(String label){
		if(label.indexOf("\u001f")!=-1) 
			label= label.substring(label.indexOf("\u001f")+2);
		return label;
		
	}
	

}
