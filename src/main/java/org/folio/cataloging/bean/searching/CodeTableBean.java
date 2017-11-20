package org.folio.cataloging.bean.searching;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.folio.cataloging.bean.LocalisedBean;
import org.folio.cataloging.dao.DAOCodeTable;
import org.folio.cataloging.business.common.DataAccessException;

@SuppressWarnings("unchecked")
public class CodeTableBean extends LocalisedBean 
{
	private List codeTable = new ArrayList();
	private String linkURL = new String();
	private String returnString = new String();
	static DAOCodeTable daoCodeTable = new DAOCodeTable();
	
	public CodeTableBean(Class clazz) {
		super(clazz);
	}
	
	public CodeTableBean() {
		super();
	}
	public int countStandardNote(String code) throws DataAccessException {
		return daoCodeTable.countStandardNote(code);
	}
	
	@Override
	public Object loadObject(Locale locale) {
		List list = null;
		try {
			list = daoCodeTable.getOptionList(getClazz(), locale);
		} catch (DataAccessException dataAccessException) {
			/* TODO cath error*/
		}
		setCodeTable(list);
		return list;
	}
	
	public List getCodeList(Locale locale) {
		return (List) getObject(locale);
	}

	public List getCodeTable(){
		return codeTable;
	}

	public void setCodeTable(List list) {
		codeTable = list;
	}

	public String getLinkURL() {
		return linkURL;
	}

	public void setLinkURL(String string) {
		linkURL = string;
	}

	public String getReturnString() {
		return returnString;
	}

	public void setReturnString(String string) {
		returnString = string;
	}
}