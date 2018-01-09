package org.folio.cataloging.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.persistence.CasCache;

import java.sql.*;
import java.util.List;

@SuppressWarnings("unchecked")
public class DAOCasCache extends HibernateUtil 
{
	private static Log logger = LogFactory.getLog(DAOCasCache.class);
	
	public DAOCasCache() {
		super();
	}
	
	public void persistCasCache(int bibNumber, CasCache cache) throws DataAccessException 
	{
		CasCache cache2;
		if (loadCasCache(bibNumber).size() == 0) {
			cache.setBibItemNumber(bibNumber);
			cache.setLevelCard(cache.getLevelCard()==null?Global.DEFAULT_LEVEL_CARD:cache.getLevelCard());
			cache.setNtrLevel(cache.getNtrLevel()==null?"001":cache.getNtrLevel());
			cache.setMdrFgl(cache.getMdrFgl()==null?"001":cache.getMdrFgl());
//			cache.setWorkingCode(cache.getWorkingCode()==null?IGlobalConst.DEFAULT_WORKING_CODE:cache.getWorkingCode());
			cache.setFlagIsStock("S".equalsIgnoreCase(cache.getFlagIsStock())?"S":"N");
			cache.setCheckNCORE("S".equalsIgnoreCase(cache.getCheckNCORE())?"S":"N");
			cache.setCheckNTOCSB("S".equalsIgnoreCase(cache.getCheckNTOCSB())?"S":"N");
			cache.setFlagReiteration("S".equalsIgnoreCase(cache.getFlagReiteration())?"S":"N");
			cache.setDigCheck("S".equalsIgnoreCase(cache.getDigCheck())?"S":"N");
			cache.setFlagNTI("S".equalsIgnoreCase(cache.getFlagNTI())?"S":"N");	
			cache.setContinuazCheck("S".equalsIgnoreCase(cache.getContinuazCheck())?"S":"N");
			cache.setOnlineCheck("S".equalsIgnoreCase(cache.getOnlineCheck())?"S":"N");
			cache.setPrintFourCover("S".equalsIgnoreCase(cache.getPrintFourCover())?"S":"N");			
			cache.setStatusDisponibilit(cache.getStatusDisponibilit()==null?new Integer(99):cache.getStatusDisponibilit());
			cache.setPriceList(cache.getPriceList());
			cache.setPriceListDate(cache.getPriceListDate());
			cache.setLstPriceDtIni(cache.getLstPriceDtIni());
			cache.setLstPriceDtFin(cache.getLstPriceDtFin());
			cache.setLstDtCrt(cache.getLstDtCrt());
			cache.setLstUser(cache.getLstUser());
			cache.setLstNote(cache.getLstNote());
			cache.setLstType(cache.getLstType());
			cache.setLstCurcy(cache.getLstCurcy());
			cache.setPageNumber(cache.getPageNumber());
			cache.setWeightGrams(cache.getWeightGrams());
			cache.setCodeVendor(cache.getCodeVendor());
			cache.setDateDisponibilit(cache.getDateDisponibilit());
			cache.setCodeWeeklyConsignmentFirst(cache.getCodeWeeklyConsignmentFirst());
			cache.setCodeMonthlyConsignmentFirst(cache.getCodeMonthlyConsignmentFirst());
			cache.setCodeWeeklyConsignmentSecond(cache.getCodeWeeklyConsignmentSecond());
			cache.setCodeMonthlyConsignmentSecond(cache.getCodeMonthlyConsignmentSecond());
			cache.setHeighBookMillimeters(cache.getHeighBookMillimeters());
			cache.setDataSource(cache.getDataSource());
			cache.setPagMin(cache.getPagMin());
			cache.setPagMax(cache.getPagMax());
			cache.setIdPublisher(cache.getIdPublisher());	
			cache.setProductCategory(cache.getProductCategory());
//			cache.setNote(cache.getNote());
			Timestamp ts = null;
			if (cache.getLstDtCrtSql()!=null){ 
				ts = cache.getLstDtCrtSql();
			}else {
				ts = new Timestamp(new java.util.Date().getTime());
			}
			cache.setLstDtCrtSql(ts);		
			ts = new Timestamp(new java.util.Date().getTime());
			cache.setPriceListDateSql(ts);
			logger.info("Insert CasCache " + cache.toString());
			persistByStatus(cache);
		} else {
			cache2 = (CasCache) loadCasCache(bibNumber).get(0);		
			cache2.setBibItemNumber(cache.getBibItemNumber());
			cache2.setLevelCard(cache.getLevelCard()==null?Global.DEFAULT_LEVEL_CARD:cache.getLevelCard());
			cache2.setNtrLevel(cache.getNtrLevel()==null?"001":cache.getNtrLevel());
			cache2.setMdrFgl(cache.getMdrFgl()==null?"001":cache.getMdrFgl());
//			cache2.setWorkingCode(cache.getWorkingCode()==null?IGlobalConst.DEFAULT_WORKING_CODE:cache.getWorkingCode());
			cache2.setFlagIsStock("S".equalsIgnoreCase(cache.getFlagIsStock())?"S":"N");
			cache2.setCheckNCORE("S".equalsIgnoreCase(cache.getCheckNCORE())?"S":"N");
			cache2.setCheckNTOCSB("S".equalsIgnoreCase(cache.getCheckNTOCSB())?"S":"N");
			cache2.setFlagReiteration("S".equalsIgnoreCase(cache.getFlagReiteration())?"S":"N");
			cache2.setDigCheck("S".equalsIgnoreCase(cache.getDigCheck())?"S":"N");
			cache2.setFlagNTI("S".equalsIgnoreCase(cache.getFlagNTI())?"S":"N");	
			cache2.setContinuazCheck("S".equalsIgnoreCase(cache.getContinuazCheck())?"S":"N");
			cache2.setOnlineCheck("S".equalsIgnoreCase(cache.getOnlineCheck())?"S":"N");
			cache2.setPrintFourCover("S".equalsIgnoreCase(cache.getPrintFourCover())?"S":"N");
			cache2.setStatusDisponibilit(cache.getStatusDisponibilit()==null?new Integer(99):cache.getStatusDisponibilit());
			cache2.setPriceList(cache.getPriceList());
			cache2.setPriceListDate(cache.getPriceListDate());
			cache2.setLstPriceDtIni(cache.getLstPriceDtIni());
			cache2.setLstPriceDtFin(cache.getLstPriceDtFin());
			cache2.setLstDtCrt(cache.getLstDtCrt());
			cache2.setLstUser(cache.getLstUser());
			cache2.setLstNote(cache.getLstNote());
			cache2.setLstType(cache.getLstType());
			cache2.setLstCurcy(cache.getLstCurcy());
			cache2.setPageNumber(cache.getPageNumber());
			cache2.setWeightGrams(cache.getWeightGrams());
			cache2.setCodeVendor(cache.getCodeVendor());			
			cache2.setDateDisponibilit(cache.getDateDisponibilit());
			cache2.setCodeWeeklyConsignmentFirst(cache.getCodeWeeklyConsignmentFirst());
			cache2.setCodeMonthlyConsignmentFirst(cache.getCodeMonthlyConsignmentFirst());
			cache2.setCodeWeeklyConsignmentSecond(cache.getCodeWeeklyConsignmentSecond());
			cache2.setCodeMonthlyConsignmentSecond(cache.getCodeMonthlyConsignmentSecond());
			cache2.setHeighBookMillimeters(cache.getHeighBookMillimeters());
			cache2.setDataSource(cache.getDataSource());
			cache2.setPagMin(cache.getPagMin());
			cache2.setPagMax(cache.getPagMax());
			cache2.setIdPublisher(cache.getIdPublisher());	
			cache2.setProductCategory(cache.getProductCategory());
//			cache2.setNote(cache.getNote());
			
			Timestamp ts = null;
			if (cache.getLstDtCrtSql()!=null){ 
				ts = cache.getLstDtCrtSql();
			}else {
				ts = new Timestamp(new java.util.Date().getTime());
			}
			cache2.setLstDtCrtSql(ts);		
			ts = new Timestamp(new java.util.Date().getTime());
			cache2.setPriceListDateSql(ts);		
			cache2.markChanged();
			logger.info("Update CasCache " + cache.toString());
			persistByStatus(cache2);
		}	
	}

	public List loadCasCache(int bibNumber) throws DataAccessException 
	{
		List result = null;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from CasCache as ct where ct.bibItemNumber =" + bibNumber);
			q.setMaxResults(1);
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public CasCache getCasCache(int bibNumber) throws DataAccessException 
	{
		List result = null;
		CasCache cas=null;
	
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from CasCache as ct where ct.bibItemNumber =" + bibNumber);
			q.setMaxResults(1);
			result = q.list();
			if (result.size()==1)
				cas =(CasCache)result.get(0);

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		
		if (cas!=null) {
			cas.setFlagIsStock("S".equalsIgnoreCase(cas.getFlagIsStock())?"S":"N");
			cas.setCheckNCORE("S".equalsIgnoreCase(cas.getCheckNCORE())?"S":"N");
			cas.setCheckNTOCSB("S".equalsIgnoreCase(cas.getCheckNTOCSB())?"S":"N");
			cas.setFlagReiteration("S".equalsIgnoreCase(cas.getFlagReiteration())?"S":"N");
			cas.setDigCheck("S".equalsIgnoreCase(cas.getDigCheck())?"S":"N");
			cas.setFlagNTI("S".equalsIgnoreCase(cas.getFlagNTI())?"S":"N");	
			cas.setContinuazCheck("S".equalsIgnoreCase(cas.getContinuazCheck())?"S":"N");
			cas.setOnlineCheck("S".equalsIgnoreCase(cas.getOnlineCheck())?"S":"N");
			cas.setPrintFourCover("S".equalsIgnoreCase(cas.getPrintFourCover())?"S":"N");
			cas.setMdrFgl(cas.getMdrFgl()==null?"001":cas.getMdrFgl());
			cas.setLevelCard(cas.getLevelCard()==null? Global.DEFAULT_LEVEL_CARD:cas.getLevelCard());
			cas.setNtrLevel(cas.getNtrLevel()==null?"001":cas.getNtrLevel());
//			cas.setWorkingCode(cas.getWorkingCode()==null?IGlobalConst.DEFAULT_WORKING_CODE:cas.getWorkingCode());
			cas.setStatusDisponibilit(cas.getStatusDisponibilit()==null?new Integer(99):cas.getStatusDisponibilit());
			cas.setExistAdminData(isDataAdmin(cas));
		}
		
		return cas;
	}
	
	/**
	 * Controllo se nella S_CAS_CACHE i dati amministrativi sono valorizzati oppure no
	 * @param casCache
	 * @return
	 */
	private boolean isDataAdmin(CasCache casCache)
	{
		boolean isPresent = true;
		if (casCache.getLstType() == null &&
			(casCache.getLstCurcy() == null || "EUR".equalsIgnoreCase(casCache.getLstCurcy())) &&
			(casCache.getPriceList() == null || casCache.getPriceList() == 0) &&
			casCache.getCodeVendor() == null &&
			(casCache.getPrintFourCover() == null || "N".equals(casCache.getPrintFourCover())) &&
			(casCache.getFlagIsStock() == null || "N".equals(casCache.getFlagIsStock())) &&
			casCache.getLstPriceDtIni() == null &&
			casCache.getLstPriceDtFin() == null &&
			casCache.getLstNote() == null &&
			casCache.getDateDisponibilit() == null &&
			(casCache.getPagMin() == null || casCache.getPagMin() == 0) &&
			(casCache.getPagMax() == null || casCache.getPagMax() == 0) &&
			(casCache.getPageNumber() == null || casCache.getPageNumber() == 0) &&
			(casCache.getWeightGrams() == null || casCache.getWeightGrams() == 0) && 
			(casCache.getHeighBookMillimeters() == null || casCache.getHeighBookMillimeters() == 0)
		)
			isPresent=false;
	
		return isPresent;
	}
	
	public void deleteCasCache(int bibNumber) throws DataAccessException 
	{
		try {
			Session s = currentSession();
			s.delete("from CasCache as ct where ct.bibItemNumber =" + bibNumber );

		} catch (HibernateException e) {
			logAndWrap(e);
		}
	}	
	
    public boolean isDigitalSerial(int amicusNumber, int view) throws DataAccessException 
    {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Session session = currentSession();
        
        boolean isSerialDigital = false;

        try {
            connection = session.connection();
            stmt = connection.prepareStatement(
                    "Select * from S_Cas_Cache a, BIB_ITM b" 
                    + " where a.bib_itm_nbr = b.bib_itm_nbr"
                    + " and a.bib_itm_nbr = "
                    + amicusNumber
                    + " and a.DIG_CHK = 'S'" 
                    + " and b.ITM_BIB_LVL_CDE = 's'" 
                    + " and substr(b.usr_vw_ind," + view + ", 1 ) = '1'"
                    );
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                isSerialDigital = true;
            }
            
        } catch (HibernateException e) {
            logAndWrap(e);
        } catch (SQLException e) {
            logAndWrap(e);            
            
        } finally {
            try {rs.close(); } catch(Exception ex){}
            try {stmt.close(); } catch(Exception ex){}
        }    
        return isSerialDigital;
    }
    
	/**
	 * 20110606 carica la natura livello per ogni record digitale
	 */ 
	public String getNtrLvlForDigital(int amicusNumber, int view) throws DataAccessException 
	{
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Session session = currentSession();

        String SELECT_NTR_LVL = "SELECT A.NTR_LVL FROM S_CAS_CACHE A"  + " WHERE A.BIB_ITM_NBR = " + amicusNumber + " AND A.DIG_CHK = 'S'";
        String natureLevel = null;

        try {
            connection = session.connection();
            stmt = connection.prepareStatement(SELECT_NTR_LVL);
            rs = stmt.executeQuery();
            if (rs.next()) {
                natureLevel = rs.getString("NTR_LVL");    
            }
            
        } catch (HibernateException e) {
            logAndWrap(e);
        } catch (SQLException e) {
            logAndWrap(e);            
            
        } finally {
            try{ rs.close(); } catch(Exception ex){}
            try{ stmt.close(); } catch(Exception ex){}
        }    
        return natureLevel;
	}	
	
	public static final String DELETE_S_CAS_CACHE = "DELETE FROM S_CAS_CACHE WHERE BIB_ITM_NBR = ?"; 
	
	public static final String INSERT_S_CAS_CACHE = 
			"INSERT INTO S_CAS_CACHE (BIB_ITM_NBR,REC_LVL,PRICE_LST,PRICE_LST_DATE,PAG_NBR,WEIGHT_GR,ID_VNDR,RECURR,NTI," 
		+ " AVLBLTY_STATUS,AVLBLTY_DATE,SHPNG_WK_CODE_1,SHPNG_MNTH_CODE_1,SHPNG_WK_CODE_2,SHPNG_MNTH_CODE_2,BOOK_HGHT_MM," 
		+ " PRNT_4_CVR,DATA_SOURCE,NTR_LVL,DIG_CHK,PAG_MIN,PAG_MAX,ONLINE_CHK,CONTINUAZ_CHK,IS_STOCK,MDR_FGL,CURCY_CDE," 
		+ " ID_PUBL,LST_DT_CRT,LST_PRICE_DT_INI,LST_PRICE_DT_FIN,LST_USER,LST_CURCY,LST_NOTE,LST_TYPE,PRODUCT_CATEGORY," 
		+ " NTOCSB,NCORE,WORKING_CODE,INTERNAL_NOTE)" 
		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public void saveCasCache(CasCache casCache) throws DataAccessException
	{
		Connection connection = null;
		PreparedStatement stmt1 = null;
		Session session = currentSession();
		Date dataSql = null;
		
		try {
			connection = session.connection();
			connection.setAutoCommit(false);

			stmt1 = connection.prepareStatement(DELETE_S_CAS_CACHE);		
			stmt1.setInt(1, casCache.getBibItemNumber());
			stmt1.execute();
			stmt1.close();
			
			stmt1 = connection.prepareStatement(INSERT_S_CAS_CACHE);
			stmt1.setInt(1, casCache.getBibItemNumber());
			stmt1.setString(2, casCache.getLevelCard());
			
			if (casCache.getPriceList()==null){
				casCache.setPriceList(new Float(0));
			}
			stmt1.setFloat(3, casCache.getPriceList());
			
			/* Data modifica */
			Timestamp ts = new Timestamp(new java.util.Date().getTime());
			stmt1.setTimestamp(4, ts);
			
			if (casCache.getPageNumber()==null){
				casCache.setPageNumber(new Integer(0));
			}
			stmt1.setInt(5, casCache.getPageNumber());
			
			if (casCache.getWeightGrams()==null){
				casCache.setWeightGrams(new Integer(0));
			}
			stmt1.setInt(6, casCache.getWeightGrams());
			
			stmt1.setString(7, casCache.getCodeVendor());
			stmt1.setString(8, casCache.getFlagReiteration());
			stmt1.setString(9, casCache.getFlagNTI());
			if (casCache.getStatusDisponibilit()==null){
				casCache.setStatusDisponibilit(new Integer(99));
			}
			stmt1.setInt(10, casCache.getStatusDisponibilit());
			
			dataSql = null;
			if (casCache.getDateDisponibilit()!=null){
				dataSql = new Date(casCache.getDateDisponibilit().getTime());
			}
			stmt1.setDate(11, dataSql);
			
			stmt1.setString(12, casCache.getCodeWeeklyConsignmentFirst()); 
			stmt1.setString(13, casCache.getCodeMonthlyConsignmentFirst()); 
			stmt1.setString(14, casCache.getCodeWeeklyConsignmentSecond());
			stmt1.setString(15, casCache.getCodeMonthlyConsignmentSecond());
			stmt1.setInt(16, casCache.getHeighBookMillimeters());

			stmt1.setString(17, casCache.getPrintFourCover());
			stmt1.setString(18, casCache.getDataSource());
			stmt1.setString(19, casCache.getNtrLevel());
			stmt1.setString(20, casCache.getDigCheck());			
			
			if (casCache.getPagMin()==null){
				casCache.setPagMin(new Integer(0));
			}
			stmt1.setInt(21, casCache.getPagMin());
			
			if (casCache.getPagMax()==null){
				casCache.setPagMax(new Integer(0));
			}
			stmt1.setInt(22, casCache.getPagMax());
			
			stmt1.setString(23, casCache.getOnlineCheck());
			stmt1.setString(24, casCache.getContinuazCheck());
			stmt1.setString(25, null);  						/* IS_STOCK */
			stmt1.setString(26, casCache.getMdrFgl());  
			stmt1.setString(27, null); 							/* CURCY_CDE */
			stmt1.setString(28, casCache.getIdPublisher());
			
			/* Data inserimento */
			ts = null;
			if (casCache.getLstDtCrtSql()!=null){ 
				ts = casCache.getLstDtCrtSql();
			}else {
				ts = new Timestamp(new java.util.Date().getTime());
			}
			stmt1.setTimestamp(29, ts);

			dataSql = null;			
			if (casCache.getLstPriceDtIni()!=null){
				dataSql = new Date(casCache.getLstPriceDtIni().getTime());
			}
			stmt1.setDate(30, dataSql);
			
			dataSql = null;
			if (casCache.getLstPriceDtFin()!=null){
				dataSql = new Date(casCache.getLstPriceDtFin().getTime());
			}
			stmt1.setDate(31, dataSql);
			
			stmt1.setString(32, casCache.getLstUser());
			stmt1.setString(33, casCache.getLstCurcy());
			stmt1.setString(34, casCache.getLstNote());
			stmt1.setString(35, casCache.getLstType());
			stmt1.setString(36, casCache.getProductCategory());
			stmt1.setString(37, casCache.getCheckNTOCSB());
			stmt1.setString(38, casCache.getCheckNCORE());
//			stmt1.setString(39, casCache.getWorkingCode());
//			stmt1.setString(40, casCache.getNote());
			stmt1.setString(39, null);
			stmt1.setString(40, null);
			
			stmt1.execute();
			stmt1.close();
			
			connection.commit();
			
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			try {if (connection!=null) connection.rollback();} catch (SQLException ex) {logger.error("SQLException", ex);}
			logAndWrap(e);
		} finally {
			try {
				connection.setAutoCommit(true);
				if (stmt1!=null){stmt1.close();}
			} catch (SQLException e) {
				logAndWrap(e);
			}
		}
	}
}