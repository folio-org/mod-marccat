package org.folio.cataloging.dao;

import net.sf.hibernate.*;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.F;
import org.folio.cataloging.dao.persistence.CasCache;
import org.folio.cataloging.integration.GlobalStorage;
import org.folio.cataloging.integration.log.MessageCatalogStorage;
import org.folio.cataloging.log.Log;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;

@SuppressWarnings("unchecked")
public class CasCacheDAO extends AbstractDAO {
  private Log logger = new Log(CasCacheDAO.class);

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
  public List <CasCache> loadCasCache(final int bibNumber, final Session session) throws HibernateException {
    return session.find("from CasCache as ct where ct.bibItemNumber=?",
      new Object[]{bibNumber}, new Type[]{Hibernate.INTEGER});
  }

  /**
   * Gets {@link#CasCache} by amicus number.
   *
   * @param bibNumber -- the amicus number id.
   * @param session   -- current hibernate session.
   * @return {@link#CasCache}
   * @throws HibernateException in case of hibernate exception.
   */
  public CasCache getCasCache(final int bibNumber, final Session session) throws HibernateException {

    CasCache casCache = loadCasCache(bibNumber, session).stream().filter(Objects::nonNull).findFirst().orElse(null);
    setDefaultValues(casCache);

    return casCache;
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

  /**
   * Deletes casCache that represent record administration data.
   *
   * @param amicusNumber -- the amicus number id.
   * @param session      -- the current hibernate session.
   * @throws HibernateException in case of hibernate exception.
   */
  public void deleteCasCache(final int amicusNumber, final Session session) throws HibernateException {
    final Transaction transaction = getTransaction(session);
    try {
      session.delete("from CasCache as ct where ct.bibItemNumber =" + amicusNumber);
      transaction.commit();
    } catch (Exception e) {
      cleanUp(transaction);
      logger.error(MessageCatalogStorage._00010_DATA_ACCESS_FAILURE, e);
      throw new HibernateException(e);
    }
  }

  /**
   * Checks if item exists and if it's a digital serial resource.
   *
   * @param amicusNumber -- the amicus number corresponding to item.
   * @param view         -- the associated view to item.
   * @param session      -- the current hibernate session.
   * @return a boolean.
   * @throws HibernateException in case of hibernate exception.
   */
  public boolean isDigitalSerial(final int amicusNumber, final int view, final Session session) throws HibernateException {
    Query query = session.createQuery("Select a.bib_itm_nbr from S_Cas_Cache a, BIB_ITM b where a.bib_itm_nbr = b.bib_itm_nbr " +
      " and a.bib_itm_nbr = :bibNumber " +
      " and a.DIG_CHK = 'S' and b.ITM_BIB_LVL_CDE = 's' and substr(b.usr_vw_ind, :view , 1 ) = '1'");
    query.setInteger("bibNumber", amicusNumber);
    query.setInteger("view", view);
    return ofNullable(query.list().stream().filter(Objects::nonNull).findFirst()).map(exist -> true).orElse(false);
  }

  @Deprecated
  public boolean isDigitalSerial(int amicusNumber, int view) throws IllegalArgumentException {
    throw new IllegalArgumentException("don't use!");
  }


  /**
   * Loads level nature for the item.
   *
   * @param amicusNumber -- the amicus number of item.
   * @param session      -- the current hibernate session.
   * @return level nature for digital item.
   * @throws HibernateException in case of hibernate exception.
   */
  public String getNtrLvlForDigital(final int amicusNumber, final Session session) throws HibernateException {
    List <CasCache> list = session.find("select a.ntrLevel from CasCache as a where a.bibItemNumber = " + amicusNumber + " and a.digCheck = 'S'");
    String ntrLevel = ofNullable(list.stream().filter(Objects::nonNull).findFirst()).map(casCache -> {
      return casCache.get().getNtrLevel();
    }).orElse(null);

    return ntrLevel;
  }

}
