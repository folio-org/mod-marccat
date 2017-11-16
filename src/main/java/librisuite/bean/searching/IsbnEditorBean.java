package librisuite.bean.searching;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import librisuite.bean.LibrisuiteBean;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.ErrorIsbnProcException;
import librisuite.business.exception.InvalidDescriptorException;
import librisuite.business.searching.AssociatedIsbnException;
import librisuite.business.searching.DAOPublCdeIsbn;
import librisuite.business.searching.DuplicateKeyException;

import com.casalini.hibernate.model.CasPublCdeIsbn;

public class IsbnEditorBean extends LibrisuiteBean
{
	private String codEditore;
	private String denEditore;
	private String operation; 
	private boolean newIsbn = false;
	private boolean newIsbnConfirm = false;
	private DAOPublCdeIsbn dao = new DAOPublCdeIsbn();
	private String newCodice;
	private String workedIsbn;
	private List isbnList = new ArrayList();
	private static final Log logger = LogFactory.getLog(IsbnEditorBean.class);
	
	public static IsbnEditorBean getInstance(HttpServletRequest request) 
	{
		IsbnEditorBean bean = (IsbnEditorBean) IsbnEditorBean.getSessionAttribute(request,IsbnEditorBean.class);
		
		if (bean == null) {
			bean = new IsbnEditorBean();
			bean.setSessionAttribute(request, IsbnEditorBean.class);
		}
		bean.setSessionAttribute(request, IsbnEditorBean.class);
		
		return bean;
	}
	
	public List loadIsbnFromEditor(String codEditore) throws DataAccessException 
	{
		List lista = null;
		lista = getDao().loadIsbnFromEditor(codEditore);

		return lista;
	}
	
	public static String rightAlign(String str, int width, char filler) 
	{
		while (str.length() < width) {
			str = str + filler;
		}
		return str;
	}
	
	public void insertIsbn(String codiceIsbnNew, String button) throws DataAccessException
	{					
		CasPublCdeIsbn item = new CasPublCdeIsbn();
		item.setCodEditore(getCodEditore());

		String radice = prefixIsbn(codiceIsbnNew);
//----> Deve prendere solo la radice dell'isbn 
//		String radice = codiceIsbnNew.substring(0,(codiceIsbnNew.substring(0, codiceIsbnNew.length()-2)).lastIndexOf("-"));
//		System.out.println("Radice da inserire " + radice);	
		
//----> Radice Isbn digitato dall'utente
//		item.setCodIsbn(radice.replaceAll("-", ""));
//		item.setCodIsbn(codiceIsbnNew.replaceAll("-", ""));
//----> Radice Isbn digitato dall'utente senza trattini
		item.setIsbnSortForm(radice.replaceAll("-", ""));
//----> Radice Isbn digitato dall'utente con i trattini
		item.setIsbnStringText(radice);
		item.setUser("LibriCat");
		
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		item.setDate(formatter.parse(formatter.format(new Date()), new ParsePosition(0)));
		
		setNewIsbnConfirm(false);
		if (getDao().loadIsbnFromEditorIsbn(item).size()==0){
			if (getDao().loadAssociatedEditorFromIsbn(item).size()==0){
				item.markNew();
				getDao().persistByStatus(item);
			}else {
				if (button.equalsIgnoreCase("saveButton")){
					setNewIsbnConfirm(true);
					throw new AssociatedIsbnException ();
				} else {
					item.markNew();
					getDao().persistByStatus(item);
				}
					
			}				
		}else {
				throw new DuplicateKeyException ();
		}
	}
	
	public String prefixIsbn(String isbn) 
	{
		String radice = isbn.substring(0,(isbn.substring(0, isbn.length()-2)).lastIndexOf("-"));
		return radice;	
	}

	public void cntrIsbn(String codiceIsbn)throws InvalidDescriptorException, DataAccessException, ErrorIsbnProcException 
	{
		/*
		if (codiceIsbn.trim().length()==0){
			throw new Exception("error.isbn");
		}
 		*/
		
		codiceIsbn=codiceIsbn.replaceAll("-", "");
		
		/* 
		DAOGlobalVariable dgv = new DAOGlobalVariable();
		char value_hyphen = dgv.getValueByName("isbn_hyphen").charAt(0);
		if (value_hyphen == '0'){
			checkISBNWithoutHyphens(codiceIsbn);
		} else if(value_hyphen == '1'){
			checkISBNWithHyphens(codiceIsbn);
		}
		checkISBNWithoutHyphens(codiceIsbn);
		*/
		
		setWorkedIsbn(formatIsbn(codiceIsbn));	
	}
	
	public String formatIsbn(String isbn) throws ErrorIsbnProcException
	{
		String newIsbn = ""; 
    	int lenIsbn = 10;
    	isbn=isbn.trim();
    	
    	if (isbn.length()<10 || isbn.length()<13){
    		if (isbn.length()>2){
    			if (isbn.substring(0, 3).equalsIgnoreCase("978") || isbn.substring(0, 3).equalsIgnoreCase("979")){
    				lenIsbn = 13;   	
    			}
    		}
    		isbn=rightAlign(isbn, lenIsbn, '0');
    	}
    	logger.debug("isbn riempito : " + isbn);
     	
	    ISBNHyphenAppender hyphenAppender = new ISBNHyphenAppender();
	    try {
		    if (isbn.length()==10){
		    	newIsbn = hyphenAppender.appendHyphenToISBN10(isbn);
		    }else if (isbn.length()==13){
		    		newIsbn = hyphenAppender.appendHyphenToISBN13(isbn);
		    }else{
		    	throw new ErrorIsbnProcException("Length ISBN not provided");
		    } 
	    } catch (NullPointerException ex) {
	    	throw new ErrorIsbnProcException(ex.getMessage());
	    } catch (IllegalArgumentException ex) {
	    	throw new ErrorIsbnProcException(ex.getMessage());
	    } catch (UnsupportedOperationException ex) {
	    	throw new ErrorIsbnProcException(ex.getMessage());
	    } catch (ErrorIsbnProcException ex) {
	    	throw new ErrorIsbnProcException(ex.getMessage());
	    } catch (Exception ex) {
	    	throw new ErrorIsbnProcException();
        }
	    
	    logger.debug("Isbn restituito: " + newIsbn);
//	    System.out.println("Isbn restituito: " + newIsbn);
	    return newIsbn;
	}
	
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public List getIsbnList() {
		return isbnList;
	}

	public void setIsbnList(List isbnList) {
		this.isbnList = isbnList;
	}

	public boolean isNewIsbn() {
		return newIsbn;
	}

	public void setNewIsbn(boolean newIsbn) {
		this.newIsbn = newIsbn;
	}

	public String getCodEditore() {
		return codEditore;
	}

	public void setCodEditore(String codEditore) {
		this.codEditore = codEditore;
	}

	public String getDenEditore() {
		return denEditore;
	}

	public void setDenEditore(String denEditore) {
		this.denEditore = denEditore;
	}

	public DAOPublCdeIsbn getDao() {
		return dao;
	}

	public void setDao(DAOPublCdeIsbn dao) {
		this.dao = dao;
	}

	public String getNewCodice() {
		return newCodice;
	}

	public void setNewCodice(String newCodice) {
		this.newCodice = newCodice;
	}

	public String getWorkedIsbn() {
		return workedIsbn;
	}

	public void setWorkedIsbn(String workedIsbn) {
		this.workedIsbn = workedIsbn;
	}

	public boolean isNewIsbnConfirm() {
		return newIsbnConfirm;
	}

	public void setNewIsbnConfirm(boolean newIsbnConfirm) {
		this.newIsbnConfirm = newIsbnConfirm;
	}
}