package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.log.MessageCatalog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Manages access to table S_BIB_MARC_IND_DB_CRLTN
 * database encoding and MARC21 encoding
 * @author paulm
 * @author natasciab
 */

public class DAOBibliographicCorrelation extends DAOCorrelation {
	private static final Log logger = LogFactory.getLog(DAOBibliographicCorrelation.class);


	@Deprecated
	public Correlation getBibliographicCorrelation(CorrelationKey bibliographicCorrelationKey) throws DataAccessException {

		return getBibliographicCorrelation(
			bibliographicCorrelationKey.getMarcTag(),
			bibliographicCorrelationKey.getMarcFirstIndicator(),
			bibliographicCorrelationKey.getMarcSecondIndicator(),
			bibliographicCorrelationKey.getMarcTagCategoryCode());
	}

	/**
	 * Returns the BibliographicCorrelation from BibliographicCorrelationKey
	 * @param bibliographicCorrelationKey -- the database bibliographicCorrelationKey
	 * @return a BibliographicCorrelation object containing or null when none found
	 *
	 */
	public Correlation getBibliographicCorrelation(final Session session, final CorrelationKey bibliographicCorrelationKey) throws HibernateException {

		return getBibliographicCorrelation( session,
							bibliographicCorrelationKey.getMarcTag(),
							bibliographicCorrelationKey.getMarcFirstIndicator(),
							bibliographicCorrelationKey.getMarcSecondIndicator(),
							bibliographicCorrelationKey.getMarcTagCategoryCode());
	}

	/**
	 * Returns the BibliographicCorrelation based on MARC encoding and category code
	 * @param tag -- marc tag
	 * @param firstIndicator -- marc first indicator
	 * @param secondIndicator -- marc second indicator
	 * @param categoryCode -- category code
	 * @return a BibliographicCorrelation object or null when none found
	 *
	 */
	public BibliographicCorrelation getBibliographicCorrelation( final Session session,
													final String tag,
													final char firstIndicator,
													final char secondIndicator,
													final short categoryCode) throws HibernateException {

        List<BibliographicCorrelation> bibliographicCorrelations = null;

        if (categoryCode != 0){
		bibliographicCorrelations = session.find(
			"from BibliographicCorrelation as bc "
				+ "where bc.key.marcTag = ? and "
				+ "(bc.key.marcFirstIndicator = ? or bc.key.marcFirstIndicator='S' )and "
				+ "bc.key.marcFirstIndicator <> '@' and "
				+ "(bc.key.marcSecondIndicator = ? or bc.key.marcSecondIndicator='S')and "
				+ "bc.key.marcSecondIndicator <> '@' and "
				+ "bc.key.marcTagCategoryCode = ?",
			new Object[] { new String(tag), new Character(firstIndicator), new Character(secondIndicator), new Short(categoryCode)},
			new Type[] { Hibernate.STRING, Hibernate.CHARACTER, Hibernate.CHARACTER, Hibernate.SHORT });

		}
		else {
			bibliographicCorrelations = session.find(
			"from BibliographicCorrelation as bc "
				+ "where bc.key.marcTag = ? and "
				+ "(bc.key.marcFirstIndicator = ? or bc.key.marcFirstIndicator='S' )and "
				+ "bc.key.marcFirstIndicator <> '@' and "
				+ "(bc.key.marcSecondIndicator = ? or bc.key.marcSecondIndicator='S')and "
				+ "bc.key.marcSecondIndicator <> '@' order by bc.key.marcTagCategoryCode asc",
			new Object[] { new String(tag), new Character(firstIndicator), new Character(secondIndicator)},
			new Type[] { Hibernate.STRING, Hibernate.CHARACTER, Hibernate.CHARACTER});
		}

		Optional<BibliographicCorrelation> firstElement = bibliographicCorrelations.stream().filter(Objects::nonNull).findFirst();
		if (firstElement.isPresent()) {
			return firstElement.get();
		}else
			return null;
	}


	@Deprecated
	public Correlation getBibliographicCorrelation( String tag,
													char firstIndicator,
													char secondIndicator,
													short categoryCode) throws DataAccessException {
		List l=null;
		if(categoryCode!=0){
			l =
					find(
							"from BibliographicCorrelation as bc "
									+ "where bc.key.marcTag = ? and "
									+ "(bc.key.marcFirstIndicator = ? or bc.key.marcFirstIndicator='S' )and "
									+ "bc.key.marcFirstIndicator <> '@' and "
									+ "(bc.key.marcSecondIndicator = ? or bc.key.marcSecondIndicator='S')and "
									+ "bc.key.marcSecondIndicator <> '@' and "
									+ "bc.key.marcTagCategoryCode = ?",
							new Object[] {
									new String(tag),
									new Character(firstIndicator),
									new Character(secondIndicator),
									new Short(categoryCode)},
							new Type[] {
									Hibernate.STRING,
									Hibernate.CHARACTER,
									Hibernate.CHARACTER,
									Hibernate.SHORT });
		}
		else {
			l =
					find(
							"from BibliographicCorrelation as bc "
									+ "where bc.key.marcTag = ? and "
									+ "(bc.key.marcFirstIndicator = ? or bc.key.marcFirstIndicator='S' )and "
									+ "bc.key.marcFirstIndicator <> '@' and "
									+ "(bc.key.marcSecondIndicator = ? or bc.key.marcSecondIndicator='S')and "
									+ "bc.key.marcSecondIndicator <> '@' order by bc.key.marcTagCategoryCode asc",

							new Object[] {
									new String(tag),
									new Character(firstIndicator),
									new Character(secondIndicator)},
							new Type[] {
									Hibernate.STRING,
									Hibernate.CHARACTER,
									Hibernate.CHARACTER});
		}

		//if (l.size() == 1) {
		if (l.size() >=1) {
			return (Correlation) l.get(0);
		}
		else
			return null;
	}

	/**
	 * Returns the MARC encoding based on the input database encodings
	 * @param category -- the database category (1-name, etc...)
	 * @param value1 -- the first database code
	 * @param value2 -- the second database code
	 * @param value3 -- the third database code
	 * @return a BibliographicCorrelationKey object containing 
	 * the MARC encoding (tag and indicators) or null when none found
	 *
	 */
	public CorrelationKey getMarcEncoding(
		short category,
		short value1,
		short value2,
		short value3)
		throws DataAccessException {

		List l =
			find(
				"from BibliographicCorrelation as bc "
					+ "where bc.key.marcTagCategoryCode = ? and "
					+ "bc.databaseFirstValue = ? and "
					+ "bc.databaseSecondValue = ? and "
					+ "bc.databaseThirdValue = ?",
				new Object[] {
					new Short(category),
					new Short(value1),
					new Short(value2),
					new Short(value3)},
				new Type[] {
					Hibernate.SHORT,
					Hibernate.SHORT,
					Hibernate.SHORT,
					Hibernate.SHORT });

		if (l.size() == 1) {
			return ((Correlation) l.get(0)).getKey();
		} else {
			return null;
		}
	}

	@Deprecated
	public List getSecondCorrelationList(short category,short value1,Class codeTable) throws DataAccessException
	{
		return find("Select distinct ct from "
					+ codeTable.getName()
					+ " as ct, BibliographicCorrelation as bc "
					+ " where bc.key.marcTagCategoryCode = ? and "
					+ " bc.key.marcFirstIndicator <> '@' and "
					+ " bc.key.marcSecondIndicator <> '@' and "
					+ " bc.databaseFirstValue = ? and "
					+ " bc.databaseSecondValue = ct.code and  "
					+ "ct.obsoleteIndicator = '0'  order by ct.sequence ",
				new Object[] { new Short(category), new Short(value1)},
				new Type[] { Hibernate.SHORT, Hibernate.SHORT});
	}

	/**
	 * Get second correlation values by marc category and first correlation.
	 *
	 * @param session the hibernate session
	 * @param category the marc category used as filter criterion
	 * @param value1 the first correlation value used as filter criterion
	 * @param classTable the mapped class in the hibernate configuration
	 * @param locale the locale associated to language used as filter criterion
	 * @return
	 * @throws DataAccessException
	 */
	public List<Avp<String>> getSecondCorrelationList(final Session session,
													  final short category,
                                                      final short value1,
                                                      final Class classTable,
                                                      final Locale locale) throws DataAccessException
	{
		try {

            final List<CodeTable> codeTables = session.find("Select distinct ct from  "
							+ classTable.getName()
							+ " as ct, BibliographicCorrelation as bc "
							+ " where bc.key.marcTagCategoryCode = ? and "
							+ " bc.key.marcFirstIndicator <> '@' and "
							+ " bc.key.marcSecondIndicator <> '@' and "
							+ " bc.databaseFirstValue = ? and "
							+ " bc.databaseSecondValue = ct.code and "
							+ " ct.obsoleteIndicator = '0' and "
                            + " ct.language = ? "
                            + " order by ct.sequence ",
                    new Object[]{new Short(category), new Short(value1), locale.getISO3Language()},
                    new Type[]{Hibernate.SHORT, Hibernate.SHORT, Hibernate.STRING});

			return codeTables
					.stream()
					.map(codeTable -> (Avp<String>) new Avp(codeTable.getCodeString().trim(), codeTable.getLongText()))
					.collect(toList());

		} catch (final HibernateException exception) {
			logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
			return Collections.emptyList();
		}
	}

	public static final String SELECT_CLASSIFICATION_TAG_LABELS = 
		"SELECT AA.TBL_SEQ_NBR, AA.TYP_VLU_CDE, aa.FNCTN_VLU_CDE, AA.TBL_VLU_OBSLT_IND, AA.SHORT_STRING_TEXT, AA.STRING_TEXT, AA.LANGID"
		+ " FROM "
		+ System.getProperty(Global.SCHEMA_SUITE_KEY) +".S_BIB_CLSTN_TYP_FNCTN AA,"
		+ System.getProperty(Global.SCHEMA_SUITE_KEY) +".S_BIB_MARC_IND_DB_CRLTN BC"
		+ " WHERE BC.MARC_TAG_CAT_CDE = ?"
		+ " AND BC.MARC_TAG_1ST_IND <> '@'" 
		+ " AND BC.MARC_TAG_2ND_IND <> '@'" 
		+ " AND BC.MARC_TAG_IND_VLU_1_CDE = ?"
		+ " AND BC.MARC_TAG_IND_VLU_2_CDE = AA.FNCTN_VLU_CDE"
		+ " AND BC.MARC_TAG_IND_VLU_1_CDE = AA.TYP_VLU_CDE"
		+ " AND AA.TBL_VLU_OBSLT_IND = 0"  
		+ " ORDER BY AA.TBL_SEQ_NBR";

	public List<ClassificationFunction> getClassificationTagLabels(short category, short value1) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Session session = currentSession();
		List<ClassificationFunction> list = new ArrayList<ClassificationFunction>();
		ClassificationFunction item = null;
		
		try {
			connection = session.connection();
		    stmt = connection.prepareStatement(SELECT_CLASSIFICATION_TAG_LABELS);
		    stmt.setInt(1, category);
		    stmt.setInt(2, value1);
			rs = stmt.executeQuery();
			while (rs.next()) {
				item = new ClassificationFunction();
				item.setSequence(rs.getInt("TBL_SEQ_NBR"));
//				item.setCode(rs.getShort("TYP_VLU_CDE"));
				item.setCode(rs.getShort("FNCTN_VLU_CDE"));
				item.setObsoleteIndicator((rs.getString("TBL_VLU_OBSLT_IND")).charAt(0));
				item.setLanguage(rs.getString("LANGID"));
				item.setShortText(rs.getString("SHORT_STRING_TEXT"));
				item.setLongText(rs.getString("STRING_TEXT"));
				list.add(item);
			}
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);			
			
		} finally {
			try{ rs.close(); } catch(Exception ex){}
			try{ stmt.close(); } catch(Exception ex){}
		}
		return list;
	}

	@Deprecated
	public List getThirdCorrelationList(
			short category,
			short value1,
			short value2,
			Class codeTable)
			throws DataAccessException {
			
			return find(
				" select distinct ct from "
					+ codeTable.getName()
					+ " as ct, BibliographicCorrelation as bc "
					+ " where bc.key.marcTagCategoryCode = ? and "
					+ " bc.key.marcFirstIndicator <> '@' and "
					+ " bc.key.marcSecondIndicator <> '@' and "
					+ " bc.databaseFirstValue = ? and "
					+ " bc.databaseSecondValue = ? and "					
					+ " bc.databaseThirdValue = ct.code and "
					+ " ct.obsoleteIndicator = 0  order by ct.sequence ",
				new Object[] {
					new Short(category),
					new Short(value1),
					new Short(value2)},
				new Type[] { Hibernate.SHORT, Hibernate.SHORT, Hibernate.SHORT });
	}

	/**
	 * Gets third correlation values by marc category, first and second correlations.
	 *
	 * @param session the hibernate session
	 * @param category the marc category used as filter criterion
	 * @param value1 the first correlation value used as filter criterion
	 * @param value2 the second correlation value used as filter criterion
	 * @param classTable the mapped class in the hibernate configuration
	 * @param locale the locale associated to language used as filter criterion
	 * @return
	 * @throws DataAccessException
	 */
	public List<Avp<String>> getThirdCorrelationList(final Session session,
													 final short category,
													 final short value1,
													 final short value2,
													 final Class classTable,
													 final Locale locale) throws DataAccessException {
		try{
				final List<CodeTable> codeTables = session.find(" select distinct ct from "
						+ classTable.getName()
						+ " as ct, BibliographicCorrelation as bc "
						+ " where bc.key.marcTagCategoryCode = ? and "
						+ " bc.key.marcFirstIndicator <> '@' and "
						+ " bc.key.marcSecondIndicator <> '@' and "
						+ " bc.databaseFirstValue = ? and "
						+ " bc.databaseSecondValue = ? and "
						+ " bc.databaseThirdValue = ct.code and "
						+ " ct.obsoleteIndicator = '0' and "
                        + " ct.language = ? "
						+ " order by ct.sequence ",
				new Object[] {new Short(category), new Short(value1), new Short(value2), locale.getISO3Language()},
				new Type[] { Hibernate.SHORT, Hibernate.SHORT, Hibernate.SHORT, Hibernate.STRING });

				return codeTables
						.stream()
						.map(codeTable -> (Avp<String>) new Avp(codeTable.getCodeString().trim(), codeTable.getLongText()))
						.collect(toList());

		} catch (final HibernateException exception) {
			logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
			return Collections.emptyList();
		}
	}

	public short getFirstAllowedValue2(
			short category,
			short value1,
			short value3)
			throws DataAccessException {

			List l = find(
				" from BibliographicCorrelation as bc "
					+ " where bc.key.marcTagCategoryCode = ? and "
					+ " bc.key.marcFirstIndicator <> '@' and "
					+ " bc.key.marcSecondIndicator <> '@' and "
					+ " bc.databaseFirstValue = ? and "
					+ " bc.databaseThirdValue = ? ",
				new Object[] {
					new Short(category),
					new Short(value1),
					new Short(value3)},
				new Type[] { Hibernate.SHORT, Hibernate.SHORT, Hibernate.SHORT });
			
			if (l.size() > 0) {
				return ((BibliographicCorrelation)l.get(0)).getDatabaseSecondValue();
			}
			else {
				return -1;
			}
		}

	public String getClassificationIndexByShelfType(short shelfType) throws DataAccessException 
	{
		List l = find("from BibliographicCorrelation as bc "
					+ " where bc.key.marcTagCategoryCode = 13 and "
					+ " bc.databaseFirstValue = ? ",
				new Object[] { new Short(shelfType)},
				new Type[] { Hibernate.SHORT });
		if (l.size() == 1) {
			String s = ((Correlation) l.get(0)).getSearchIndexTypeCode();
			return new DAOIndexList().getIndexByEnglishAbreviation(s);
		} else {
			return null;
		}
	}

	/*modifica barbara 13/04/2007 PRN 127 - nuova intestazione su lista vuota default maschera inserimento intestazione nome*/
	public CorrelationKey getMarcTagCodeBySelectedIndex(String selectedIndex)
	throws DataAccessException {
	
		if(selectedIndex == null)
		{
			return null;
		}
		
	List l =
		find(
			"from BibliographicCorrelation as bc "
				+ " where bc.searchIndexTypeCode = ?" 
				+" or bc.searchIndexTypeCode = ?" ,
	new Object[] { new String(selectedIndex.substring(0, 2)),
				new String(selectedIndex.substring(0, 2).toLowerCase())},
	new Type[] { Hibernate.STRING,  Hibernate.STRING});
	
	if(l.size()>0)
		return ((Correlation) l.get(0)).getKey();
	else	
		return null;
	}

	public CorrelationKey getMarcTagCodeBySelectedIndex(String selectedIndex, String tagNumber) throws DataAccessException 
	{
		List l =
			find(
				"from BibliographicCorrelation as bc "
					+ " where (bc.searchIndexTypeCode = ?" 
					+" or bc.searchIndexTypeCode = ?)" 
					+" and bc.key.marcTag = ?",
					new Object[] { new String(selectedIndex.substring(0, 2)),
					new String(selectedIndex.substring(0, 2).toLowerCase()),
					new String(tagNumber)},
		new Type[] { Hibernate.STRING,  Hibernate.STRING,  Hibernate.STRING});
	
	if(l.size()>0)
		return ((Correlation) l.get(0)).getKey();
	else	
		return null;
	}
	
	/**
	 * Return the label for the tag to display
	 * @return MarcTagDisplay
	 * @throws DataAccessException
	 */
	public List<LabelTagDisplay> getMarcTagDisplay(String language){
		List l = new ArrayList<MarcTagDisplay>();
		try {
			l = find(
				"from MarcTagDisplay as bc "
					+ "where bc.language = ? ",
				new Object[] {
					new String(language)
					},
				new Type[] {
					Hibernate.STRING});
		} catch (DataAccessException e) {
			logger.debug("DataAccessException for list of MarcTagDisplay");
		}

		if (l.size()> 0) {
			
			return l;
		}
		return l;
	}

	/*  Bug 4775 inizio */
	public List<RdaMarcTagDisplay> getRdaMarcTagDisplay(String language)
	{
		List l = new ArrayList<MarcTagDisplay>();
		try {
			l = find(
				"from RdaMarcTagDisplay as bc where bc.language = ? ",
				new Object[] {new String(language)}, new Type[] {Hibernate.STRING});
		} catch (DataAccessException e) {
			logger.debug("DataAccessException for list of RdaMarcTagDisplay");
		}

		if (l.size()> 0) {
			return l;
		}
		return l;
	}
	/*  Bug 4775 fine */
	
	/**
	 * Label per authority
	 * @param language
	 * @return
	 */
	public List<LabelTagDisplay> getAutorityMarcTagDisplay(String language)
	{
		List l = new ArrayList<AutMarcTagDisplay>();
		try {
			
			l = find("from AutMarcTagDisplay as bc where bc.language = ? ",
				new Object[] {new String(language)},new Type[] {Hibernate.STRING});
			
		} catch (DataAccessException e) {
			logger.debug("DataAccessException for list of MarcTagDisplay");
		}

		if (l.size()> 0) {
			return l;
		}
		return l;
	}

	
	public List<BibliographicCorrelation> getFirstAllowedValue2List(short category, short value1, short value3) throws DataAccessException 
	{
		List l = find(" from BibliographicCorrelation as bc "
					+ " where bc.key.marcTagCategoryCode = ? and "
					+ " bc.key.marcFirstIndicator <> '@' and "
					+ " bc.key.marcSecondIndicator <> '@' and "
					+ " bc.databaseFirstValue = ? and "
					+ " bc.databaseThirdValue = ? ",
		new Object[] {
			new Short(category),
			new Short(value1),
			new Short(value3)},
			new Type[] { Hibernate.SHORT, Hibernate.SHORT, Hibernate.SHORT });
		
		return l;
	}
	
	/**
	 * Checking validity of the second correlation
	 * @param l
	 * @param value2
	 * @return
	 * @throws DataAccessException
	 */
	//TODO: move this in storageService!
	public boolean isPresentSecondCorrelation(List l, short value2) throws DataAccessException 
	{
		boolean isPresent = false;
		if (l.size() > 0) {
			Iterator<BibliographicCorrelation> ite = l.iterator();
			while (ite.hasNext()) {
				short secondCorr = ite.next().getDatabaseSecondValue();
				if (secondCorr == value2) {
					isPresent = true;
				}
			}
		}
		return isPresent;
	}

	/**
	 * Loads first correlation list using note group code for tag range values.
	 *
	 * @param session the session of hibernate
	 * @param noteGroupTypeCode the note group code used as filter criterion.
	 * @param locale the Locale, used here as a filter criterion.
	 * @return
	 * @throws DataAccessException
	 */
	public List<Avp<String>> getFirstCorrelationByNoteGroupCode(final Session session, final String noteGroupTypeCode, final Locale locale) throws DataAccessException {

		final String fromTag = (noteGroupTypeCode.length() == 2 ? "0"+noteGroupTypeCode : noteGroupTypeCode);
		final String toTag = String.valueOf(Short.parseShort(noteGroupTypeCode) + 99);

		final StringBuilder sqlFilter = new StringBuilder(" and bc.key.marcSecondIndicator <> '@' ")
				                                  .append(" and bc.databaseFirstValue = ct.code ")
				                                  .append(" and bc.key.marcTagCategoryCode = 7 ")
												  .append(" and bc.key.marcTag between '").append(fromTag)
											      .append("' and '").append(toTag).append("' ");

		return getCorrelatedList(session, BibliographicNoteType.class, true, sqlFilter.toString(), locale);
	}


	/**
	 * Generic method that gets first correlation using filter sql as criterion.
	 *
	 * @param session the session of hibernate
	 * @param clazz the mapped class in the hibernate configuration.
	 * @param alphabeticOrder true if alphabetical order
	 * @param sqlFilter the sql filter added to query
	 * @param locale the Locale, used here as a filter criterion.
	 * @return
	 * @throws DataAccessException in case of SQL exception.
	 */
	public List<Avp<String>> getCorrelatedList(final Session session,
											   final Class clazz,
											   final boolean alphabeticOrder,
											   final String sqlFilter,
											   final Locale locale) throws DataAccessException
	{
		final String sqlOrder = ( alphabeticOrder ? " order by ct.longText " : " order by ct.sequence " );
		try {
			final List<CodeTable> codeTables = session.find("select distinct ct from "
					+ clazz.getName()
					+ " as ct, BibliographicCorrelation as bc "
					+ " where ct.obsoleteIndicator = '0' "
					+ " and ct.language = ? "
					+ sqlFilter
					+ sqlOrder,
					new Object[] { locale.getISO3Language()},
					new Type[] { Hibernate.STRING });

			return codeTables
					.stream()
					.map(codeTable -> (Avp<String>) new Avp(codeTable.getCodeString().trim(), codeTable.getLongText()))
					.collect(toList());

		} catch (final HibernateException exception) {
			logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
			return Collections.emptyList();
		}
	}
}