package org.folio.cataloging.bean.cataloguing.bibliographic.codelist;

import java.util.List;
import java.util.Locale;

import org.folio.cataloging.bean.LocalisedBean;
import org.folio.cataloging.dao.DAOCodeTable;
import org.folio.cataloging.business.common.DataAccessException;

@SuppressWarnings("unchecked")
public abstract class CodeListBean extends LocalisedBean 
{
	private static DAOCodeTable daoCodeTable = new DAOCodeTable();

	public CodeListBean(Class clazz) {
		super(clazz);
	}
	
   @Override
	public Object loadObject(Locale locale) 
	{
		List list = null;
		try {
			list = daoCodeTable.getOptionList(getClazz(), locale);
		} catch (DataAccessException dataAccessException) {
			throw new RuntimeException("Error retrieving codetable");
		}
		return list;
	}

	public List getCodeList(Locale locale) 
	{
		return (List) getObject(locale);
	}
	
	public List getCodeList(Locale locale, boolean alphabetic) 
	{
		return (List) getObject(locale, alphabetic);
	}
	
	public List getReversedCodeList(Locale locale) 
	{		
		return DAOCodeTable.getReversedOptionList((List) getObject(locale));
	}
	
	public static List getRdaCarrierList(Locale locale) throws DataAccessException 
	{
		return daoCodeTable.getRdaCarrierList(locale);
	}
}