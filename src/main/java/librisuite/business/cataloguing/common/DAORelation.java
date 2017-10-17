package librisuite.business.cataloguing.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Locale;

import librisuite.bean.searching.FrbrBrowseItem;
import librisuite.business.cataloguing.bibliographic.NameAccessPoint;
import librisuite.business.common.DAOBibliographicCorrelation;
import librisuite.business.common.DataAccessException;
import librisuite.business.descriptor.DAONameDescriptor;
import librisuite.business.descriptor.SortFormParameters;
import librisuite.business.descriptor.UnsupportedHeadingException;
import librisuite.hibernate.Correlation;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.StringText;
import com.libricore.librisuite.common.Subfield;

public class DAORelation extends HibernateUtil 
{
	private static final Log logger = LogFactory.getLog(DAORelation.class);
	
	public void createRecordRelation(Integer amicusNumber,Integer amicusNumberRel,String code,boolean isAuthority,boolean isAuthorityRel,Integer designator,Integer  subDesignator,Integer  subSubDesignator,Integer isReciprocal) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
	
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("INSERT INTO " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_RECORD_REL " +
					" (BIB_ITM_NBR,BIB_ITM_NBR_REL,RLT_CODE,TYPE,TYPE_REL,DESIGNATOR,SUB_DESIGNATOR,SUB_SUB_DESIGNATOR,RECIPROCAL) VALUES(?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1, amicusNumber);
			stmt.setInt(2, amicusNumberRel);
			stmt.setString(3, code);
			stmt.setInt(4, isAuthority == true ? 0 : 1);
			stmt.setInt(5, isAuthorityRel == true ? 0 : 1);
			setNullOrInteger(stmt,6, designator);
			setNullOrInteger(stmt,7, subDesignator);
			setNullOrInteger(stmt,8, subSubDesignator);
			stmt.setInt(9, isReciprocal);
			stmt.executeUpdate();
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}
	
	/**
	 * Utility method that checks if the given value is null and eventually
	 * provides the PreparedStatement.setNull shortcut.
	 * 
	 * @param ps the prepared statement.
	 * @param position the index position within the prepared statement of the given parameter.
	 * @param value the parameter value.
	 * @throws SQLException  in case of database failure.
	 */
	private void setNullOrInteger(PreparedStatement ps, int position, Integer value) throws SQLException
	{
		if(value == null)
		{
			ps.setNull(position, Types.INTEGER);
		}else
		{
			ps.setInt(position, value);
		}
	}
	
	public static final Integer NOT_RECIPROCAL = 0;	
	public static final Integer RECIPROCAL = 1;	
	
	public void createRecordRelations(Integer amicusNumber,Integer amicusNumberRel,String code, boolean isAuthority,boolean isAuthorityRel,Integer designator,Integer subDesignator,Integer subSubDesignator,Locale locale) throws DataAccessException
	{
		createRecordRelation(amicusNumber, amicusNumberRel, code, isAuthority, isAuthorityRel,designator,subDesignator,subSubDesignator,NOT_RECIPROCAL);
		String reciprocalCode = getReciprocalCode(code);
		if(reciprocalCode  != null){
			createRecordRelation(amicusNumberRel, amicusNumber, reciprocalCode, isAuthorityRel, isAuthority, designator,subDesignator,subSubDesignator,RECIPROCAL);
		}
	}
	
	private String getReciprocalCode(String code) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		String result = null;
		
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_RLT_REC WHERE RLT_CODE =? ");
			stmt.setString(1, code);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
			{	
				result = rs.getString("RLT_CODE_REC");
			}
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return result;
	}
	
	public boolean isRelationPresent(Integer amicusNumber,Integer amicusNumberRel,String code,boolean isAuthority,boolean isAuthorityRel) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		boolean result = false;
		
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_RECORD_REL WHERE BIB_ITM_NBR =? AND BIB_ITM_NBR_REL =? AND RLT_CODE = ? AND TYPE=? AND TYPE_REL=?");
			stmt.setInt(1, amicusNumber);
			stmt.setInt(2, amicusNumberRel);
			stmt.setString(3, code);
			stmt.setInt(4, isAuthority == true ? 0 : 1);
			stmt.setInt(5, isAuthorityRel == true ? 0 : 1);
			ResultSet rs = stmt.executeQuery();
			result = rs.next();
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return result;
	}
	
	public boolean isRelationsPresent(Integer amicusNumber,Integer amicusNumberRel,String code,boolean isAuthority,boolean isAuthorityRel) throws DataAccessException
	{
		boolean result = false;
		if(isRelationPresent(amicusNumber, amicusNumberRel, code, isAuthority, isAuthorityRel))
			result = true;
			
		String reciprocalCode = getReciprocalCode(code);
		if(isRelationPresent(amicusNumberRel, amicusNumber, reciprocalCode, isAuthorityRel,isAuthority))
			result = true;
		
		return result;
	}
	
	public void createHeadingRelation(Integer amicusNumber, String code, boolean isAuthority, String hdgType, FrbrBrowseItem item) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("INSERT INTO " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_HDG_REL (BIB_ITM_NBR,HDG_NBR,RLT_CODE,TYPE,NAME,HDG_TYP_CDE) VALUES(?,?,?,?,?,?)");
			stmt.setInt(1, amicusNumber);
			stmt.setInt(2, item.getHeadingNumber());
			stmt.setString(3, code);
			stmt.setInt(4, isAuthority == true ? 0 : 1);
			stmt.setString(5,item.getHeadingName());
			stmt.setString(6,hdgType);
			stmt.executeUpdate();
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	public boolean isHeadingRelationPresent(Integer amicusNumber,Integer headingNumber,String code,boolean isAuthority) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		boolean result = false;
		
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_HDG_REL WHERE BIB_ITM_NBR =? AND HDG_NBR =? AND RLT_CODE = ? AND TYPE=?");
			stmt.setInt(1, amicusNumber);
			stmt.setInt(2, headingNumber);
			stmt.setString(3, code);
			stmt.setInt(4, isAuthority == true ? 0 : 1);
			ResultSet rs = stmt.executeQuery();
			
			result = rs.next();
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return result;
	}
	
	public void insertHeading(FrbrBrowseItem item,Integer amicusNumber) throws DataAccessException, UnsupportedHeadingException
	{
		if ("NH".equalsIgnoreCase(item.getTypeHeading())){
			insertNameHeading(item, amicusNumber);
			insertHeadingNameAcsPntPlusRefs(item, amicusNumber);
			
		}else if ("TH".equalsIgnoreCase(item.getTypeHeading())){
			insertTitleHeading(item, amicusNumber);
			insertHeadingTitleAcsPntPlusRefs(item, amicusNumber);
			
		}else {
			logger.error("Intestazione non valida, scegliere solo nomi o titoli! -- Tipo hdg: " + item.getTypeHeading() + " Numero hdg: " + item.getHeadingNumber());
			throw new UnsupportedHeadingException();
		}
	}
	
	final static String INSERT_NME_ACS_PNT =
		"INSERT INTO NME_ACS_PNT (NME_HDG_NBR,BIB_ITM_NBR,NME_TTL_HDG_NBR,NME_FNCTN_CDE,USR_VW_IND,NME_WRK_RLTR_STRNG_TXT,NME_OTHER_SBFLDS) VALUES  (?,?,?,?,?,?,?)";
	
	public void insertNameHeading(FrbrBrowseItem item,Integer amicusNumber) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		int nameFunctionCode = Integer.parseInt(item.getTagType());
		
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement(INSERT_NME_ACS_PNT);
			stmt.setInt(1, item.getHeadingNumber());
			stmt.setInt(2, amicusNumber);
			stmt.setInt(3, 0);
			stmt.setInt(4, nameFunctionCode);
			stmt.setString(5, "1000000000000000");
//			stmt.setString(6, normalizeTextSubfield(item.getRelatorTerm()));
			
			String inputRelatorTerm = normalizeTextSubfield(item.getRelatorTerm());
			if (inputRelatorTerm.trim().length()>0){
				stmt.setString(6, normalizeTextSubfield(item.getRelatorTerm()));
			}else if (item.getDesignator()!=null && item.getDesignator().trim().length()>0) {
				stmt.setString(6, normalizeTextSubfield("$e" + item.getDesignator()));
			} else {
				stmt.setNull(6, Types.NULL);
			}
			
			if (nameFunctionCode==2){ 	/* Tag 1XX */
				stmt.setNull(7, Types.NULL);
			}else {						/* Tag 7XX */
				stmt.setString(7, normalizeTextSubfield(item.getIntroduction()));
				
//				Prova per bug 4934 vedere con Annalisa
//				String introduction = normalizeTextSubfield(item.getIntroduction());
//				if (introduction.trim().length()>0){
//					stmt.setString(7, introduction);
//				} else {
//					stmt.setString(7, normalizeTextSubfield("$i" + item.getDesignator()));
//				}
				
			}
			stmt.execute();
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {}
		}
	}
	
	final static String INSERT_NME_ACS_PNT_PLUS_REFS =
		"INSERT INTO NME_ACS_PNT_PLUS_REFS (NME_HDG_NBR,BIB_ITM_NBR,USR_VW_IND,NME_WRK_RLTR_STRNG_TXT) VALUES  (?,?,?,?)";
	
	public void insertHeadingNameAcsPntPlusRefs(FrbrBrowseItem item,Integer amicusNumber) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement(INSERT_NME_ACS_PNT_PLUS_REFS);
			stmt.setInt(1, item.getHeadingNumber());
			stmt.setInt(2, amicusNumber);
			stmt.setString(3, "1000000000000000");
			stmt.setString(4, normalizeTextSubfield(item.getRelatorTerm()));
			stmt.execute();
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}
	
	public void insertTitleHeading(FrbrBrowseItem item,Integer amicusNumber) throws DataAccessException
	{
		/* DA SVILUPPARE MANCANO LE SPECIFICHE! */
	}
	
	public void insertHeadingTitleAcsPntPlusRefs(FrbrBrowseItem item,Integer amicusNumber) throws DataAccessException
	{
		/* DA SVILUPPARE MANCANO LE SPECIFICHE! */
	}
	
	public void updateHeading(FrbrBrowseItem item,Integer amicusNumber) throws DataAccessException, UnsupportedHeadingException
	{
		if ("NH".equalsIgnoreCase(item.getTypeHeading())){
			updateNameHeading(item, amicusNumber);
			updateHeadingNameAcsPntPlusRefs(item, amicusNumber);
			
		}else if ("TH".equalsIgnoreCase(item.getTypeHeading())){
			updateTitleHeading(item, amicusNumber);
			updateHeadingTitleAcsPntPlusRefs(item, amicusNumber);
			
		}else {
			logger.error("Intestazione non valida, scegliere solo nomi o titoli! -- Tipo hdg: " + item.getTypeHeading() + " Numero hdg: " + item.getHeadingNumber());
			throw new UnsupportedHeadingException();
		}
	}
	
	final static String UPDATE_NME_ACS_PNT = 
		"UPDATE NME_ACS_PNT SET NME_WRK_RLTR_STRNG_TXT=?, NME_OTHER_SBFLDS=? WHERE NME_HDG_NBR=? AND BIB_ITM_NBR=? AND NME_FNCTN_CDE=?";
	
	public void updateNameHeading(FrbrBrowseItem item,Integer amicusNumber) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		
		int nameFunctionCode = Integer.parseInt(item.getTagType());		
		String relator = normalizeTextSubfield(item.getRelatorTerm());
		relator = ((relator==null || relator.trim().length()==0)?"":relator);
		String introduction = normalizeTextSubfield(item.getIntroduction());
		introduction = ((introduction==null || introduction.trim().length()==0)?"":introduction);
		
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement(UPDATE_NME_ACS_PNT);
			if ("".equals(relator)){
				stmt.setNull(1, Types.NULL);
			}else {
				stmt.setString(1, relator);	
			}
			if (nameFunctionCode==2){
				stmt.setNull(2, Types.NULL);
			}else {
				if ("".equals(introduction)){
					stmt.setNull(2, Types.NULL);
				}else {
					stmt.setString(2, introduction);	
				}
			}			
			stmt.setInt(3, item.getHeadingNumber());
			stmt.setInt(4, amicusNumber);
			stmt.setInt(5, nameFunctionCode);
			stmt.execute();
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}
	
	final static String UPDATE_NME_ACS_PNT_PLUS_REFS =
		"UPDATE NME_ACS_PNT_PLUS_REFS SET NME_WRK_RLTR_STRNG_TXT=? WHERE NME_HDG_NBR=? AND BIB_ITM_NBR=?";
	
	public void updateHeadingNameAcsPntPlusRefs(FrbrBrowseItem item,Integer amicusNumber) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement(UPDATE_NME_ACS_PNT_PLUS_REFS);
			stmt.setString(1, normalizeTextSubfield(item.getRelatorTerm()));
			stmt.setInt(2, item.getHeadingNumber());
			stmt.setInt(3, amicusNumber);
			stmt.execute();
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}
	
	public void updateTitleHeading(FrbrBrowseItem item,Integer amicusNumber) throws DataAccessException
	{
		/* DA SVILUPPARE MANCANO LE SPECIFICHE! */
	}
	
	public void updateHeadingTitleAcsPntPlusRefs(FrbrBrowseItem item,Integer amicusNumber) throws DataAccessException
	{
		/* DA SVILUPPARE MANCANO LE SPECIFICHE! */
	}
	
	private String normalizeTextSubfield(String text)
	{
		String result = "";
		if(text != null)
		{
			/* Bug 4931 inizio */
//			text.replaceAll("\\$", Subfield.SUBFIELD_DELIMITER);
			result = text.replaceAll("\\$", Subfield.SUBFIELD_DELIMITER);
			/* Bug 4931 fine */
		}
		return result;
	}
	public Integer getHeadingNumber(StringText term,Integer amicusNumber,String tag,char firstIndicator,char secondIndicator) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Integer headingNumber = null;
		
		/* Bug 4932 inizio inizio */
		StringText textForSortForm = term.getSubfieldsWithoutCodes(NameAccessPoint.VARIANT_CODES);
		term = textForSortForm;
		/* Bug 4932 fine fine */
		
		String searchTerm = "";

		try {
		//	term = excludeSeparators(term);
			DAOBibliographicCorrelation daoCorrelation = new DAOBibliographicCorrelation();
			String searchT = getNormalizedSortForm(excludeSpecialCharacters(term.toDisplayString()));
		
			Correlation correlation = daoCorrelation.getBibliographicCorrelation(tag,firstIndicator,secondIndicator,(short)2);
			
			SortFormParameters parms = new SortFormParameters(100,101,correlation.getDatabaseFirstValue(),correlation.getDatabaseSecondValue(),0);
			searchTerm = new DAONameDescriptor().calculateSortForm(searchT, parms);
			
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("SELECT A.NME_HDG_NBR FROM NME_HDG A LEFT OUTER JOIN NME_ACS_PNT B ON A.NME_HDG_NBR=B.NME_HDG_NBR WHERE TRIM(A.NME_HDG_SRT_FORM) = TRIM(?) AND B.BIB_ITM_NBR=?");
			stmt.setString(1, searchTerm);
			stmt.setInt(2, amicusNumber);
			rs = stmt.executeQuery();
			if(rs.next()) {
				headingNumber = rs.getInt("NME_HDG_NBR");
			}  
			
		} catch (DataAccessException e) {
			logAndWrap(e);
		}catch (SQLException e) {
			logAndWrap(e);
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		
		if (headingNumber==null){
			logger.error("ATTENZIONE non trovata la hdgNbr per record: " + amicusNumber 
					+ " tag: " + tag 
					+ " indicatore 1: " + firstIndicator
					+ " indicatore 2: " + secondIndicator
					+ " stringText: " + term.getDisplayText()
					+ " searchTerm: " + searchTerm
					);
		}
		
		return headingNumber;
	}
	
	public static String excludeSpecialCharacters(String text)
	{
		if(text != null){
			text = text.replaceAll("\\'", "").replaceAll("\\(", "").replaceAll("\\)", "");
		}
		
		return text;
	}
	
	public static String excludeSeparators(String text)
	{
		StringBuilder builder = new StringBuilder();
		
		if(text != null)
		{
			String[] sep = text.split("\\$");
			for(int i = 0; i<sep.length; i++)
			{
				if(sep[i] != null && !sep[i].trim().equals(""))
				{
					builder.append(sep[i].substring(1,sep[i].length())).append(" ");
					//	result = result + sep[i].substring(1,sep[i].length()) + " ";
				}
			}
			if (builder.length()>0){
				builder.deleteCharAt(builder.length() - 1);
			}
		}
		return builder.toString();
	}
	
	public static String excludeDelimitatorSeparators(String text)
	{
		if (text==null){
			text = "";
		}
		StringBuilder builder = new StringBuilder();
		String[] sep = text.split(Subfield.SUBFIELD_DELIMITER);
		for(int i = 0; i<sep.length; i++)
		{
			if(sep[i] != null && !sep[i].trim().equals(""))
			{
				builder.append(sep[i].substring(1,sep[i].length())).append(" ");
			//	result = result + sep[i].substring(1,sep[i].length()) + " ";
			}
		}
		if (builder.length()>0){
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}

	protected static final String CS_NORMALIZE_SORT_FORM = "begin ? := pack_sortform.sf_normalize(buffersize => ?, inputstrptr => ?, outputbufptr => ?); end;"; 
	protected static final int BUFFER_SIZE = 256;
	
	public String getNormalizedSortForm(String toNormalize) throws SQLException
	{
		if(toNormalize==null)
			return null;
		if("".equals(toNormalize))
			return "";
		
		CallableStatement cs = null;
		try{
			Session s = currentSession();
			Connection conn = s.connection();
			cs = conn.prepareCall(CS_NORMALIZE_SORT_FORM);
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, BUFFER_SIZE);
			cs.setString(3, toNormalize);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			return cs.getString(4);	
			
		} catch(SQLException e)
		{
			throw e;
		} catch(Exception e)
		{
			throw new SQLException(e);
		} finally
		{
			try { cs.close(); } catch(Exception ignore){}
		}
	}
	
	public void deleteRecordRelation(Integer amicusNumber,boolean type,Integer amicusNumberRel,boolean typeRel,String code) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("DELETE FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_RECORD_REL WHERE RLT_CODE =? " +
											   "AND (((BIB_ITM_NBR = ? AND TYPE=?) " +
											   "AND (BIB_ITM_NBR_REL = ? AND TYPE_REL=?)) OR   " +
											   "((BIB_ITM_NBR = ? AND TYPE=?) " +
											   "AND (BIB_ITM_NBR_REL = ? AND TYPE_REL=?)))");
			stmt.setString(1, code);
			stmt.setInt(2, amicusNumber);
			stmt.setInt(3, type == true ? 0:1);
			stmt.setInt(4, amicusNumberRel);
			stmt.setInt(5, typeRel == true ? 0:1);
			stmt.setInt(6, amicusNumberRel);
			stmt.setInt(7, typeRel == true ? 0:1);
			stmt.setInt(8, amicusNumber);
			stmt.setInt(9, type == true ? 0:1);
			stmt.execute();
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}
	
	public void deleteRecordRelations(Integer amicusNumber,boolean type,Integer amicusNumberRel,boolean typeRel,String code) throws DataAccessException 
	{	
		deleteRecordRelation(amicusNumber, type, amicusNumberRel, typeRel, code);
		String reciprocalCode = getReciprocalCode(code);
		if(reciprocalCode  != null)
		{
			deleteRecordRelation(amicusNumberRel, typeRel,amicusNumber, type,reciprocalCode);
		}
	}
	
	public void deleteHeadingRelation(Integer amicusNumber,Integer hdgNumber,boolean type,String code) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("DELETE FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_HDG_REL WHERE RLT_CODE =? " +
											   "AND BIB_ITM_NBR=? AND HDG_NBR=? AND TYPE=?");
			stmt.setString(1, code);
			stmt.setInt(2, amicusNumber);
			stmt.setInt(3, hdgNumber);
			stmt.setInt(4, type == true ? 0:1);
			stmt.execute();
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * Il metodo cancella tutte le relazioni presenti nella tabella F_HDG_REL per il record passato
	 * @param amicusNumber
	 * @throws DataAccessException
	 */
	public void deleteAllHeadingRelationByRecord(Integer amicusNumber) throws DataAccessException 
	{
		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("DELETE FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_HDG_REL WHERE BIB_ITM_NBR=?");
			stmt.setInt(1, amicusNumber);
			stmt.execute();
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try { stmt.close(); } catch (SQLException e) {}
		}
	}
	
	public void deleteHeadingAcsPntPlusRefs(Integer headingNumber,Integer amicusNumber) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("DELETE NME_ACS_PNT_PLUS_REFS WHERE NME_HDG_NBR=? AND BIB_ITM_NBR=?");
			stmt.setInt(1, headingNumber);
			stmt.setInt(2, amicusNumber);
			stmt.execute();
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	public void deleteNmeAcsPntHeading(Integer headingNumber,Integer amicusNumber) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt = null;
		
		try {
			Session session = currentSession();
			connection = session.connection();
			stmt = connection.prepareStatement("DELETE NME_ACS_PNT WHERE NME_HDG_NBR=? AND BIB_ITM_NBR=?");
			stmt.setInt(1, headingNumber);
			stmt.setInt(2, amicusNumber);
			stmt.execute();
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}	
}