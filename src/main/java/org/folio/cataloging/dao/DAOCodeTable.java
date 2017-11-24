package org.folio.cataloging.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.folio.cataloging.business.codetable.ValueLabelElement;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.dao.persistence.CLSTN;
import org.folio.cataloging.dao.persistence.CodeTable;
import org.folio.cataloging.dao.persistence.SBJCT_HDG;
import org.folio.cataloging.dao.persistence.T_LANG;
import org.folio.cataloging.dao.persistence.T_LANG_OF_ACS_PNT;
import org.folio.cataloging.dao.persistence.T_LANG_OF_ACS_PNT_SBJCT;
import org.folio.cataloging.dao.persistence.T_LANG_OF_IDXG;
import org.folio.cataloging.dao.persistence.T_LANG_OF_IDXG_LANG;
import org.folio.cataloging.dao.persistence.T_SINGLE;
import org.folio.cataloging.dao.persistence.T_SINGLE_CHAR;
import org.folio.cataloging.dao.persistence.T_SINGLE_LONGCHAR;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.type.Type;

import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.Global;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;

import static java.util.stream.Collectors.toList;

public class DAOCodeTable extends HibernateUtil {
	private Log logger = new Log(DAOCodeTable.class);

	public static final int STEP = 10; /* Standard Amicus increment for CodeTables sequence column */
	private static final String ALPHABETICAL_ORDER = " order by ct.longText ";
	private static final String SEQUENCE_ORDER = " order by ct.sequence ";
	private String defaultListOrder = Defaults.getBoolean("labels.alphabetical.order", true)?ALPHABETICAL_ORDER:SEQUENCE_ORDER;

	/**
	 * TODO: JAVADOC
	 * @param session
	 * @param c
	 * @param locale
	 * @return
	 */
	public List<ValueLabelElement> getList(final Session session, final Class c, final Locale locale) {
		try {
			// NOTE: two steps are required because Hibernate doesn't use generics and the inference type
			// mechanism doesn't work.
			final List<CodeTable> codeTables = session.find(
					"from "
							+ c.getName()
							+ " as ct "
							+ " where ct.language = ?"
							+" and ct.obsoleteIndicator = '0'"
							+" and ct.system = 0"
							+" and ct.code >= -1"
							+ " order by ct.code ",
					new Object[] { locale.getISO3Language()},
					new Type[] { Hibernate.STRING });

			return codeTables
					.stream()
					.filter(codeTable -> codeTable.getLanguage().equals(locale.getISO3Language()))
					.map(codeTable -> new ValueLabelElement(codeTable.getCodeString().trim(), codeTable.getLongText()))
 					.collect(toList());
		} catch (final HibernateException exception) {
			logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
			return Collections.emptyList();
		}
	}

	public List getList(Class c) throws DataAccessException 
	{
		List listCodeTable = null;
		try {
			Session s = currentSession();
			listCodeTable =
				s.find("from "
						+ c.getName()
						+ " as ct "
						+ " order by ct.sequence ");
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		logger.debug("Got codetable for " + c.getName());
		return listCodeTable;
	}
 
	public List getList(Class c, boolean alphabeticOrder) throws DataAccessException 
	{
		List listCodeTable = null;	
		String order = SEQUENCE_ORDER;
		if (alphabeticOrder)
			order = ALPHABETICAL_ORDER;
		try {
			Session s = currentSession();
			listCodeTable =
				s.find(
					"from "
						+ c.getName()
						+ " as ct "
						+ " where ct.obsoleteIndicator = 0"
						+ order);
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		logger.debug("Got codetable for " + c.getName());
		return listCodeTable;
	}
	
	public List getCorrelatedList(Class c, boolean alphabeticOrder, String filtro) throws DataAccessException 
	{
		List listCodeTable = null;	
		String order = SEQUENCE_ORDER;
		if (alphabeticOrder)
			order = ALPHABETICAL_ORDER;
		try {
			Session s = currentSession();
			listCodeTable =
				s.find("select distinct ct from "
						+ c.getName()
						+ " as ct, BibliographicCorrelation as bc "
						+ " where ct.obsoleteIndicator = 0"
						+ filtro
						+ order);
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		logger.debug("Got codetable for " + c.getName());
		return listCodeTable;
	}
	
	public List getListCntrl(Class c, boolean alphabeticOrder) throws DataAccessException 
	{
		List listCodeTable = null;	
		String order = SEQUENCE_ORDER;
		if (alphabeticOrder)
			order = ALPHABETICAL_ORDER;
		
		try {
			Session s = currentSession();
			listCodeTable =
				s.find(
					"from "
						+ c.getName()
						+ " as ct "
						+ " where ct.obsoleteIndicator = 0"
						+" and (ct.code<>94 and ct.code<>102 and ct.code<>103)"
						+ order);
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		logger.debug("Got codetable for " + c.getName());
		return listCodeTable;
	}
	
	public List getListFromTag008(Class c,String tag, boolean alphabeticOrder) throws DataAccessException 
	{
		List listCodeTable = null;	
		String order = SEQUENCE_ORDER;
		if (alphabeticOrder)
			order = ALPHABETICAL_ORDER;
		
		try {
			Session s = currentSession();
			listCodeTable =
				s.find(
					"from "
						+ c.getName()
						+ " as ct "
						+ " where ct.obsoleteIndicator = 0"
//						+ " and ct.shortText like '%008%'"
						+ " and ct.longText like '%008%'"
						+ order);
			
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return listCodeTable;
	}
	
	/*T_BIB_HDR*/
	public List getListFromWithoutTag008(Class c,String tag, boolean alphabeticOrder) throws DataAccessException 
	{
		List listCodeTable = null;	
		String order = SEQUENCE_ORDER;
		if (alphabeticOrder)
			order = ALPHABETICAL_ORDER;
		
		try {
			Session s = currentSession();
			listCodeTable =
				s.find(
					"from "
						+ c.getName()
						+ " as ct "
						+ " where ct.obsoleteIndicator = 0"
						+ " and ct.code <> '31' "
						+ " and ct.code <> '32' "
						+ " and ct.code <> '33'"
						+ " and ct.code <> '34' "
						+ " and ct.code <> '35' "
						+ " and ct.code <> '36' "
						+ " and ct.code <> '37' "
						+ " and ct.code <> '15' "
						+ " and ct.code <> '39' "
						+ " and ct.code <> '41' "
						+ order);

		} catch (HibernateException e) {
			logAndWrap(e);
		}
	
		return listCodeTable;
	}

	public List getList(Class c, Locale locale) throws DataAccessException 
	{
		List listCodeTable = null;
		try {
			Session s = currentSession();
			listCodeTable =
				s.find(
					"from "
						+ c.getName()
						+ " as ct "
						+ " where ct.language = ?"
						+" and ct.obsoleteIndicator = '0'"
						+ defaultListOrder,
					new Object[] { locale.getISO3Language()},
					new Type[] { Hibernate.STRING });
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		logger.debug("Got codetable for " + c.getName());
		return listCodeTable;
	}

	public List getListOrderAlphab(Class c, Locale locale) throws DataAccessException 
	{
		List listCodeTable = null;
		try {
			Session s = currentSession();
			listCodeTable =
				s.find(
					"from "
						+ c.getName()
						+ " as ct "
						+ ALPHABETICAL_ORDER);

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		logger.debug("Got codetable for " + c.getName());
		return listCodeTable;
	}

	public static List asOptionList(List raw, Locale locale) 
	{
		if (raw == null) {
			return null;
		}

		List result = new ArrayList();
		Iterator iterator = raw.iterator();

		while (iterator.hasNext()) {
			CodeTable element = (CodeTable) iterator.next();
			if (element.getLanguage().equals(locale.getISO3Language())) {
				result.add(new ValueLabelElement(element.getCodeString(), element.getLongText()));
				//Natascia: bug 2711 (chiamata errata)
				//result.add(new ValueLabelElement(element.getCodeString().trim(), element.getLongText()));
			}
		}
		return result;
	}
	
	/**
	 * Il metodo ritorna una tendina (T_NME_WRK_RLTR) con Label-Label invece che Code-Label
	 * @param raw
	 * @return
	 */
	public static List getReversedOptionList(List raw) 
	{
		if (raw == null) {
			return null;
		}
		List result = new ArrayList();
		Iterator iterator = raw.iterator();
		ValueLabelElement element = null;
		
		while (iterator.hasNext()) {
			element = (ValueLabelElement) iterator.next();
			result.add(new ValueLabelElement(element.getLabel(),element.getLabel()));
		}
		return result;
	}	

	/**
	** Return a list with the fields I need to fill the newCopy.jsp's checkboxes
	** the parameter class is the name of the code table I need	
	 * @since 1.0
	 */
	public List getOptionList(Class c, Locale locale) throws DataAccessException 
	{
		return asOptionList(getList(c, locale), locale);
	}
	
	public List getOptionListOrderAlphab(Class c, Locale locale) throws DataAccessException 
	{
		return asOptionList(getListOrderAlphab(c, locale), locale);
	}

	public static T_SINGLE getSelectedCodeTable(List raw, Locale locale, short code) 
	{
		if (raw == null) {
			return null;
		}

		Iterator iterator = raw.iterator();

		while (iterator.hasNext()) {
			T_SINGLE element = (T_SINGLE) iterator.next();
			if (element.getLanguage().equals(locale.getISO3Language()) && element.getCode() == code) {
				return element;
			}
		}
		return null;
	}

	public T_SINGLE_CHAR load(Class c, char code, Locale locale) throws DataAccessException 
	{
		T_SINGLE_CHAR key;
		try {
			key = (T_SINGLE_CHAR) c.newInstance();
			key.setCode(code);
			key.setLanguage(locale.getISO3Language());
		} catch (Exception e) {
			throw new RuntimeException("unable to create code table object");
		}
		return (T_SINGLE_CHAR) loadCodeTableEntry(c, key);
	}

	public T_SINGLE load(Class c, short code, Locale locale) throws DataAccessException 
	{
		T_SINGLE key;
		try {
			key = (T_SINGLE) c.newInstance();
			key.setCode(code);
			key.setLanguage(locale.getISO3Language());
		} catch (Exception e) {
			throw new RuntimeException("unable to create code table object");
		}
		return (T_SINGLE) loadCodeTableEntry(c, key);
	}

	private CodeTable loadCodeTableEntry(Class c, Serializable ser) throws DataAccessException 
	{
		CodeTable result = (CodeTable) get(c, ser);
		if(result != null)
			logger.debug(
				"Got codetable entry for "
					+ c.getName() 
					+ " ( value='" + result.getCodeString()
					+ "', text='" + result.getLongText() + "')");
		return result;
	}
	
	public String getLongText(char code, Class c, Locale locale) throws DataAccessException 
	{
		String result = new String("");
		CodeTable ct = (CodeTable) load(c, code, locale);
		
		if(ct != null)	result = ct.getLongText();

		return result;
	}
	
	public String getLongText(short code, Class c, Locale locale) throws DataAccessException 
	{
		String result = new String("");
		CodeTable ct = (CodeTable) load(c, code, locale);
		result = ct.getLongText();
		return result;
    }
	
	public String getLongText(String code, Class c, Locale locale) throws DataAccessException 
	{
		String result = new String("");
		CodeTable ct = (CodeTable) load(c, code, locale);
		result = ct.getLongText();
		return result;
	}
	
	public String getTranslationString(long translationKey, Locale locale) throws DataAccessException 
	{
		List l =
			find(
				"select t.text "
					+ " from T_TRLTN as t, T_LANG as l "
					+ " where t.stringNumber = ? and t.languageNumber = l.sequence / 10 "
					+ " and l.code = ? ",
				new Object[] {
					new Long(translationKey),
					locale.getISO3Language()},
				new Type[] { Hibernate.LONG, Hibernate.STRING });
		if (l.size() > 0) {
			return (String) l.get(0);
		} else {
			return null;
		}
	}

	/*
	 * This doesn't seem to be doing anything very useful
	 * and it is not currently referenced.  At one time
	 * it was so leave it for now
	 */
	public String getLanguageOfIndexing(int code) throws DataAccessException 
	{
		String result = "und";
		List scriptingLanguage = null;
		int valueCode = 0;
		try {
			Session s = currentSession();
			scriptingLanguage = s.find("from T_LANG_OF_IDXG as t where t.code = '" + code + "'");
			Iterator iter = scriptingLanguage.iterator();
			while (iter.hasNext()) 
			{
				T_LANG_OF_IDXG rawElmt = (T_LANG_OF_IDXG) iter.next();
				valueCode = rawElmt.getSequence();
			}
			scriptingLanguage = s.find("from T_LANG_OF_IDXG_LANG as t where t.languageIndexing = " + valueCode);
			iter = scriptingLanguage.iterator();
			while (iter.hasNext()) 
			{
				T_LANG_OF_IDXG_LANG rawElmt = (T_LANG_OF_IDXG_LANG) iter.next();
				valueCode = rawElmt.getLanguage();
			}
			scriptingLanguage = s.find("from T_LANG as t where t.sequence = " + valueCode);
			iter = scriptingLanguage.iterator();
			while (iter.hasNext()) 
			{
				T_LANG rawElmt = (T_LANG) iter.next();
				result = rawElmt.getCode();
			}
			
		} catch (HibernateException e) {
			logAndWrap(e);
		}

		return result;
	}
	
	public String getAccessPointLanguage(int code, Descriptor aDescriptor) throws DataAccessException 
	{
		String result = "und";
		List scriptingLanguage = null;
		int valueCode = 0;
		Iterator iter = null;
		try {
			Session s = currentSession();
			if (aDescriptor instanceof SBJCT_HDG) {
				scriptingLanguage = s.find("from T_LANG_OF_ACS_PNT_SBJCT as t where t.code = '"	+ code + "'");
				iter = scriptingLanguage.iterator();
				while (iter.hasNext()) 
				{
					T_LANG_OF_ACS_PNT_SBJCT rawElmt = (T_LANG_OF_ACS_PNT_SBJCT) iter.next();
					valueCode = rawElmt.getSequence();
				}
			
			} else {
				scriptingLanguage = s.find("from T_LANG_OF_ACS_PNT as t where t.code = '" + code + "'");
				iter = scriptingLanguage.iterator();
				while (iter.hasNext()) 
				{
					T_LANG_OF_ACS_PNT rawElmt = (T_LANG_OF_ACS_PNT) iter.next();
					valueCode = rawElmt.getSequence();
				}
			}
			
			scriptingLanguage = s.find("from T_LANG_OF_IDXG_LANG as t where t.languageIndexing = " + valueCode);
			iter = scriptingLanguage.iterator();
			while (iter.hasNext()) 
			{
				T_LANG_OF_IDXG_LANG rawElmt = (T_LANG_OF_IDXG_LANG) iter.next();
				valueCode = rawElmt.getLanguage();
			}
			scriptingLanguage = s.find("from T_LANG as t where t.sequence = " + valueCode);
			iter = scriptingLanguage.iterator();
			while (iter.hasNext()) 
			{
				T_LANG rawElmt = (T_LANG) iter.next();
				result = rawElmt.getCode();
			}
			
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public List getEntries(Class c, Object code, boolean alphabeticOrder) throws DataAccessException, InstantiationException, IllegalAccessException 
	{
		List listCodeTable = null;	
		String order = SEQUENCE_ORDER;
		if (alphabeticOrder)
			order = ALPHABETICAL_ORDER;
		try {
			Session s = currentSession();
			listCodeTable =
				s.find(
					"from "
						+ c.getName()
						+ " as ct "
						+ " where ct.obsoleteIndicator = 0"
						+ " and ct.code = "+getCorrectValue(c,code) + " "
						+ order);
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		logger.debug("Got codetable for " + c.getName());
		return listCodeTable;
	}

	private String getCorrectValue(Class type, Object code) throws InstantiationException, IllegalAccessException 
	{
		Object obj = type.newInstance();
		if (obj instanceof T_SINGLE_CHAR || obj instanceof T_SINGLE_LONGCHAR) {
			return "'" + code  + "'";
		} else return "" + code;
	}

	/**
	 * Save a group of rows in the same code table. If it is an insertion then the
	 * sequence is calculated the same for all items present in the list
	 * Be carefull to do not mixed CodeTables in items list
	 * @param items
	 * @throws DataAccessException
	 */
	public void save(final List/*<CodeTable>*/ items) throws DataAccessException 
	{
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
				throws HibernateException, DataAccessException {
					if(items==null || items.isEmpty()) {
						return;
					}
					int sequence = suggestNewSequence((CodeTable)items.get(0));
					Iterator it = items.iterator();
					while (it.hasNext()) {
						CodeTable nextCodeTable = (CodeTable)it.next();
						if(nextCodeTable.isNew()){
							nextCodeTable.setSequence(sequence);
						}
						persistByStatus(nextCodeTable);
					}
			}
		}.execute();
	}

	/**
	 * Symply returns the calculated new sequence (MAX sequence + 1)
	 * @param codeTable
	 * @return
	 * @throws DataAccessException
	 */
	public synchronized int suggestSequence(CodeTable codeTable) throws DataAccessException 
	{
		try {
			Session s = currentSession();
			Query q = s.createQuery("select ct.sequence from "
									+ codeTable.getClass().getName()
									+ " as ct order by ct.sequence desc");
			q.setMaxResults(1);
			List results = q.list();
			int newSequence = STEP; // default for empty table
			if(results.size()>0) {
				newSequence = ((Integer)results.get(0)).intValue() + STEP ;
			}
			return newSequence;	
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		/* pratically unreachable because logAndWrap throws an exception everytime */
		return STEP;
	}
	
	public synchronized int suggestNewSequence(CodeTable codeTable) throws DataAccessException 
	{
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct.sequence from "
									+ codeTable.getClass().getName()
									+ " as ct order by ct.sequence desc");
			q.setMaxResults(1);
			List results = q.list();
			int newSequence = STEP; // default for empty table
			if(results.size()>0) {
				newSequence = ((Integer)results.get(0)).intValue() + STEP ;
			}
			return newSequence;	
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		/* pratically unreachable because logAndWrap throws an exception everytime */
		return STEP;
	}
	
	public synchronized int suggestNewCode(CodeTable codeTable) throws DataAccessException 
	{
		int newCode = 0; 
		try {
			Session s = currentSession();
			Query q = s.createQuery("select ct.code from "
									+ codeTable.getClass().getName()
									+ " as ct order by ct.code desc");
			q.setMaxResults(1);
			List results = q.list();
			// default for empty table
			if(results.size()>0) {
				newCode = ((Short)results.get(0)).intValue() + 1 ;
			}
		
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		/* pratically unreachable because logAndWrap throws an exception everytime */
		return newCode;
	}
	
	public List getList(Class c, Locale locale, boolean alphabeticOrder) throws DataAccessException 
	{
		List listCodeTable = null;
		String order = SEQUENCE_ORDER;
		if (alphabeticOrder)
			order = ALPHABETICAL_ORDER;

		try {
			Session s = currentSession();
			listCodeTable =
				s.find(
					"from "
						+ c.getName()
						+ " as ct "
						+ " where ct.language = ?"
						/*Carmen modifica 01/02/2008 non devo visualizzare nella lista gli obsoleti*/
						+" and ct.obsoleteIndicator = 0"
						+ order,
					new Object[] { locale.getISO3Language()},
					new Type[] { Hibernate.STRING });
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		logger.debug("Got codetable for " + c.getName());
		return listCodeTable;
	}
	
	public boolean findHeading(String code) throws DataAccessException 
	{	
		try {
			Session s = currentSession();
			List l = s.createCriteria(CLSTN.class)
			.add( Expression.like("stringText", "%"+code+"%") )
	        .add( Expression.eq("typeCode", new Short((short)29)))
			.list();
	
			if (l.size() >= 1) {
				return true;
			} else {
				return false;
			}
		} catch (HibernateException e) {
			logAndWrap(e);
			return false;
		}
	}

	public void deleteCode(Class c,String code) throws DataAccessException 
	{
		Session s = currentSession();
		Transaction tx = null;
		try {	
			tx = s.beginTransaction();
			s.delete("from " + c.getName() + " as ct " + " where ct.code = ?",
					 new Object[] {code}, new Type[] {Hibernate.STRING });
			tx.commit();
	    	
		} catch (HibernateException e) {
			if (tx != null) {
				try {
	                 tx.rollback();
	             } catch (HibernateException e1) {
	            	 logAndWrap(e);
	             }
	         }
		}
	}
	
	public void deleteNoteSubCode(String code) throws DataAccessException 
	{
		Session s = currentSession();
		Transaction tx = null;
		try{	
			tx = s.beginTransaction();
			s.delete( 
					 "from T_CAS_STND_NTE_SUB_TYP as ct where ct.sequence = ?",
					 new Object[] {code}, new Type[] {Hibernate.STRING });
			tx.commit();
    	
		} catch (HibernateException e) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					logAndWrap(e);
				}
			}		
		}
	}
	
	public  int getCodeNote(int code) throws DataAccessException 
	{
		int newCode = 0;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct.code from T_CAS_STND_NTE_SUB_TYP as ct where ct.sequence =" + code);
			q.setMaxResults(1);
			List results = q.list();
			if(results.size()>0) {
				newCode = ((Integer)results.get(0)).intValue();
		    }
			
		} catch (HibernateException e) {
			logAndWrap(e);
		}	
		return newCode;
	}

	public  String getCodeCache(String code) throws DataAccessException 
	{
		String newCode = "";
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct.levelCard from CasCache as ct where ct.levelCard = :code");
			q.setString("code", code);
			q.setMaxResults(1);
			List results = q.list();
			if(results.size()>0) {
				newCode = (String)results.get(0);
		    }
			
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return newCode;
	}
	
	public List getCodeNoteList(int code) throws DataAccessException 
	{
		List result=null;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from T_CAS_STND_NTE_SUB_TYP as ct where ct.sequence =" + code);
			q.setMaxResults(1);
			result = q.list();
		
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public List getMasterClient(int code) throws DataAccessException 
	{
		List result=null;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from CLCTN_MST_CSTMR as ct where ct.collectionCode =" + code);
			q.setMaxResults(1);
			result = q.list();
		
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}

	public  List getDiacritici() throws DataAccessException 
	{
		List listCodeTable = null;
		
		try {
			Session s = currentSession();
			listCodeTable = s.find("from Diacritici as a order by 1");
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		logger.debug("Got codetable for Diacritici");
		
		return listCodeTable;
	}
	
	
	/**
	 * Il metodo ritorna la lista dei caratteri presenti in tabella senza doppioni
	 * @return
	 * @throws DataAccessException
	 */
	public  List getDiacriticiDistinctCharacter() throws DataAccessException 
	{		
		Session s = currentSession();
		Statement st = null;
		ResultSet rs = null;
		List<String> characterList = new ArrayList<String>();
		try {
			Connection con = s.connection();
			String sql = "Select distinct carattere from Diacritici as a order by carattere";
			st = con.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				characterList.add((rs.getString("CARATTERE")));
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		}
		
		finally{
			try{
				rs.close();
			}catch (Exception e) {}
			try{
				st.close();
			}catch (Exception e) {}
		}
		return characterList;
	}
	
	public List getNoteTranslation(String code2, String language) throws DataAccessException 
	{
		List result=null;
		int code = new Integer(code2).intValue();
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from T_TRSLTN_NTE_TYP as ct where ct.code = " + code + " and ct.language=" + "'" + language + "'");
			q.setMaxResults(1);
			result = q.list();
		
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public List getNoteTranslationLanguage(String language) throws DataAccessException 
	{
		List result=null;
		
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from T_TRSLTN_NTE_TYP as ct where ct.language=" + "'"+language+"'");
			q.setMaxResults(1);
			result = q.list();
		
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public List getDefaultNoteTranslation(String code2, String language) throws DataAccessException 
	{
		List result=null;
		int code = new Integer(code2).intValue();
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from T_DFLT_TRSLTN_NTE as ct where ct.code = " + code + " and ct.language=" + "'"+language+"'");
			q.setMaxResults(1);
			result = q.list();
		
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public List getDefaultNoteTranslationLanguage(String language) throws DataAccessException 
	{
		List result=null;
		
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from T_DFLT_TRSLTN_NTE as ct where  ct.language=" + "'" + language + "'");
			q.setMaxResults(1);
			result = q.list();
		
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public T_SINGLE_LONGCHAR load(Class c, String code, Locale locale) throws DataAccessException 
	{
		T_SINGLE_LONGCHAR key;
		try {
			key = (T_SINGLE_LONGCHAR) c.newInstance();
			key.setCode(code);
			key.setLanguage(locale.getISO3Language());
		} catch (Exception e) {
			throw new RuntimeException("unable to create code table object");
		}
		return (T_SINGLE_LONGCHAR) loadCodeTableEntry(c, key);
	}
	
	public List getCorrelationListFromSuggest(short category,String marcTag,Class codeTable) throws DataAccessException 
	{
		return find(
			" select distinct ct from "
				+ codeTable.getName()
				+ " as ct, BibliographicCorrelation as bc "
				+ " where bc.key.marcTagCategoryCode = ? and "
//					Natascia 13/06/2007: scommentate chiocciole
				+ " bc.key.marcFirstIndicator <> '@' and "
				+ " bc.key.marcSecondIndicator <> '@' and "
				+ " bc.key.marcTag = ? and "
				+ " bc.databaseFirstValue = ct.code and  "
				+ "ct.obsoleteIndicator = 0  order by ct.sequence ",
			new Object[] { new Short(category), new String(marcTag)},
			new Type[] { Hibernate.SHORT, Hibernate.STRING });
	}
	
	/**
	 * Metodo che mi serve per leggere lo short text della tabella T_CAS_DIG_TYP 
	 * in cui trovo la directory di riferimento per il repository digitale 
	 */	
	public String getShortText(String code, Class c, Locale locale) throws DataAccessException 
	{
		String result = new String("");
		CodeTable ct = (CodeTable) load(c, code, locale);
		result = ct.getShortText();
		return result;
	}
	
	public String getShortText(short code, Class c, Locale locale) throws DataAccessException 
	{
		String result = new String("");
		CodeTable ct = (CodeTable) load(c, code, locale);
		result = ct.getShortText();
		return result;
	}

	public String getRecordInformation(Integer headingNumber,Integer amicusNumber,Integer mainLibrary) throws DataAccessException 
	{
		Session s = currentSession();
		String result = "";
		Statement st = null;
		ResultSet rs = null;
		try {
			Connection con = s.connection();
			String sql = "select * from s_cache_bib_itm_dsply where bib_itm_nbr=(select bib_itm_nbr from shlf_list_acs_pnt where bib_itm_nbr=" + amicusNumber + " and shlf_list_key_nbr=" + headingNumber + " and org_nbr="+mainLibrary+")";
			st = con.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next())
			{
				StringBuffer sb = new StringBuffer();
				String serie=rs.getString("TTL_HDG_SRS_STRNG_TXT");
				sb.append(rs.getString("TTL_HDG_MAIN_STRNG_TXT")).append(" ").
				//append(rs.getString("NME_MAIN_ENTRY_STRNG_TXT")).append("\n").
				append(rs.getString("BIB_NTE_EDTN_STRNG_TXT")).append(" ").
				append(rs.getString("BIB_NTE_IPRNT_STRNG_TXT")).append(" ").
				append(rs.getString("BIB_NTE_EXTNT_STRNG_TXT")).append("\n");
				if(serie!=null && !serie.trim().equals(""))
			       sb.append("(").append(serie).append(")").append(" ");
				result = sb.toString();
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		}
		finally{
			try{rs.close();}catch (Exception e) {}
			try{st.close();}catch (Exception e) {}
		}
		return result;
	}
	
	public int countStandardNote(String code) throws DataAccessException 
	{
		Session s = currentSession();
		Statement st = null;
		ResultSet rs = null;
		try {
			Connection con = s.connection();
			String sql = "select count(*) from STD_NTE_ACS_PNT where STD_NTE_TYP_CDE="+code;
			st = con.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next())
				return rs.getInt(1);
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		}
		
		finally{
			try{rs.close();}catch (Exception e) {}
			try{st.close();}catch (Exception e) {}
		}
		return 0;
	}
	
	public List getOptionList(Class c, Locale locale, boolean alphabetic) throws DataAccessException 
	{
		return asOptionList(getList(c, locale, alphabetic), locale);
	}
	
	private final String SELECT_RDA_CARRIER_LIST = /*"SELECT * FROM OLISUITE.T_RDA_CARRIER WHERE LANGID = ? ORDER BY STRING_TEXT";*/
		"SELECT * FROM "+System.getProperty(Global.SCHEMA_SUITE_KEY)+".T_RDA_CARRIER WHERE LANGID = ? ORDER BY STRING_TEXT";
					
	/**
	 * Metodo fatto perche' in questa tabella (CUSTOM.T_RDA_CARRIER) ci sono più righe con lo stesso ValueCode ma con label diverse 
	 * (CodeTable non gestisce questa cosa)
	 * @param locale
	 * @return
	 * @throws DataAccessException
	 */
	public List<ValueLabelElement> getRdaCarrierList(Locale locale) throws DataAccessException
	{
		PreparedStatement statement = null;
		ResultSet rs = null;
		List<ValueLabelElement> list = new ArrayList<ValueLabelElement>();
		ValueLabelElement element = null;		
		
		try {
			statement = currentSession().connection().prepareStatement(SELECT_RDA_CARRIER_LIST);
			statement.setString(1, locale.getISO3Language());
			rs = statement.executeQuery();
			while (rs.next()) 
			{
				element = new ValueLabelElement(rs.getString("TBL_VLU_CDE"), rs.getString("STRING_TEXT"));
				list.add(element);
			}

		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);			
			
		} finally {
			try{ rs.close(); } catch(Exception ex){}
			try{ statement.close(); } catch(Exception ex){}
		}
		return list;
	} 	
}