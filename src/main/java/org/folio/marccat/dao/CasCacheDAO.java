package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.dao.persistence.CasCache;
import org.folio.marccat.config.GlobalStorage;
import org.folio.marccat.util.F;

import java.sql.Timestamp;
import java.util.List;

@SuppressWarnings("unchecked")
public class CasCacheDAO extends AbstractDAO {

  public CasCacheDAO() {
    super();
  }
  
  /**
   * Saves or updates a CasCache persistent object.
   *
   * @param bibNumber -- the amicus number id.
   * @param cache     -- the casCache persistent object.
   * @param session   -- current hibernate session.
   * @throws HibernateException in case of hibernate exception.
   */
  public void persistCasCache(final int bibNumber, final CasCache cache, final Session session) throws HibernateException {
    setDefaultValues(cache);

    final Timestamp today = new Timestamp(new java.util.Date().getTime());
    if (cache.getLstDtCrtSql() == null) {
      cache.setLstDtCrtSql(today);
    }
    cache.setPriceListDateSql(today);

    if (loadCasCache(bibNumber, session).size() == 0) {
      cache.markNew();
    } else {
      cache.markChanged();
    }
    persistByStatus(cache, session);
  }

  /**
   * Loads {@link#CasCache} by amicus number.
   *
   * @param bibNumber -- the amicus number id.
   * @param session   -- current hibernate session.
   * @return {@link#CasCache}
   * @throws HibernateException in case of hibernate exception.
   */
  public List<CasCache> loadCasCache(final int bibNumber, final Session session) throws HibernateException {
    return session.find("from CasCache as ct where ct.bibItemNumber=?",
      new Object[]{bibNumber}, new Type[]{Hibernate.INTEGER});
  }

  /**
   * Sets default values for administrative data if null or empty.
   *
   * @param casCache -- the casCache to enrich.
   */
  private void setDefaultValues(final CasCache casCache) {
    if (casCache == null)
      return;

    casCache.setFlagIsStock(F.fixEmptyFlag(casCache.getFlagIsStock()));
    casCache.setCheckNCORE(F.fixEmptyFlag(casCache.getCheckNCORE()));
    casCache.setCheckNTOCSB(F.fixEmptyFlag(casCache.getCheckNTOCSB()));
    casCache.setFlagReiteration(F.fixEmptyFlag(casCache.getFlagReiteration()));
    casCache.setDigCheck(F.fixEmptyFlag(casCache.getDigCheck()));
    casCache.setFlagNTI(F.fixEmptyFlag(casCache.getFlagNTI()));
    casCache.setContinuazCheck(F.fixEmptyFlag(casCache.getContinuazCheck()));
    casCache.setOnlineCheck(F.fixEmptyFlag(casCache.getOnlineCheck()));
    casCache.setPrintFourCover(F.fixEmptyFlag(casCache.getPrintFourCover()));
    casCache.setMdrFgl(F.isNotNullOrEmpty(casCache.getMdrFgl()) ? GlobalStorage.DEFAULT_MOTHER_LEVEL : casCache.getMdrFgl());
    casCache.setLevelCard(F.isNotNullOrEmpty(casCache.getLevelCard()) ? GlobalStorage.DEFAULT_LEVEL_CARD : casCache.getLevelCard());
    casCache.setNtrLevel(F.isNotNullOrEmpty(casCache.getNtrLevel()) ? GlobalStorage.DEFAULT_LEVEL_NATURE : casCache.getNtrLevel());
    casCache.setStatusDisponibilit(casCache.getStatusDisponibilit() == null ? GlobalStorage.DEFAULT_AVAILABILITY_STATUS : casCache.getStatusDisponibilit());
    casCache.setExistAdminData(isDataAdmin(casCache));
  }


  /**
   * Checks if casCache is valued with administration data.
   *
   * @param casCache -- the casCache to check
   * @return boolean.
   */
  private boolean isDataAdmin(CasCache casCache) {
    return !(casCache.getLstType() == null &&
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
      (casCache.getHeighBookMillimeters() == null || casCache.getHeighBookMillimeters() == 0));
  }

}
