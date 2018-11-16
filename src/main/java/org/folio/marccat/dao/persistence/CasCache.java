package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.CasCacheDAO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CasCache implements Persistence {

  private DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

  private boolean existAdminData;
  private int bibItemNumber;
  private String levelCard;
  private String ntrLevel;
  private String digCheck;
  private String onlineCheck;
  private String continuazCheck;
  private String checkNTOCSB;
  private String checkNCORE;

  private Float priceList;
  private Date priceListDate;
  private Date lstPriceDtIni;
  private Date lstPriceDtFin;
  private Date lstDtCrt;
  private String lstUser;
  private String lstNote;
  private String lstType;
  private String lstCurcy;
  private Integer pageNumber;
  private Integer weightGrams;
  private String codeVendor;
  private String flagReiteration;
  private String flagNTI;
  private Integer statusDisponibilit;
  private Date dateDisponibilit;
  private String codeWeeklyConsignmentFirst;
  private String codeMonthlyConsignmentFirst;
  private String codeWeeklyConsignmentSecond;
  private String codeMonthlyConsignmentSecond;
  private Integer heighBookMillimeters;
  private String printFourCover;
  private String dataSource;
  private Integer pagMin;
  private Integer pagMax;
  private String flagIsStock;
  private String mdrFgl;
  private String idPublisher;

  private String productCategory;

  private Timestamp priceListDateSql;
  private Timestamp lstDtCrtSql;
  private PersistenceState persistenceState = new PersistenceState();

  public CasCache(int bibItemNumber) {
    this();
    setBibItemNumber(bibItemNumber);
  }

  public CasCache() {
    super();
  }

  public Timestamp getPriceListDateSql() {
    return priceListDateSql;
  }

  public void setPriceListDateSql(Timestamp priceListDateSql) {
    this.priceListDateSql = priceListDateSql;
  }

  public Timestamp getLstDtCrtSql() {
    return lstDtCrtSql;
  }

  public void setLstDtCrtSql(Timestamp lstDtCrtSql) {
    this.lstDtCrtSql = lstDtCrtSql;
  }

  public String getCheckNTOCSB() {
    return checkNTOCSB;
  }

  public void setCheckNTOCSB(String checkNTOCSB) {
    this.checkNTOCSB = checkNTOCSB;
  }

  public String getCheckNCORE() {
    return checkNCORE;
  }

  public void setCheckNCORE(String checkNCORE) {
    this.checkNCORE = checkNCORE;
  }

  public String getIdPublisher() {
    return idPublisher;
  }

  public void setIdPublisher(String idPublisher) {
    this.idPublisher = idPublisher;
  }

  public Float getPriceList() {
    return priceList;
  }

  public void setPriceList(Float priceList) {
    this.priceList = priceList;
  }

  public Date getPriceListDate() {
    return priceListDate;
  }

  public void setPriceListDate(Date priceListDate) {
    this.priceListDate = priceListDate;
  }

  public String getlstPriceDtIniString() {
    if (getLstPriceDtIni() != null)
      return formatter.format(getLstPriceDtIni());
    else
      return "";
  }

  public String getlstPriceDtFinString() {
    if (getLstPriceDtFin() != null)
      return formatter.format(getLstPriceDtFin());
    else
      return "";
  }

  public String getLstDtCrtString() {
    if (getLstDtCrt() != null)
      return formatter.format(getLstDtCrt());
    else
      return "";
  }

  public String getPriceListDateString() {
    if (getPriceListDate() != null)
      return formatter.format(getPriceListDate());
    else
      return "";
  }

  public Integer getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }

  public Integer getWeightGrams() {
    return weightGrams;
  }

  public void setWeightGrams(Integer weightGrams) {
    this.weightGrams = weightGrams;
  }

  public String getCodeVendor() {
    return codeVendor;
  }

  public void setCodeVendor(String codeVendor) {
    this.codeVendor = codeVendor;
  }

  public String getFlagReiteration() {
    return flagReiteration;
  }

  public void setFlagReiteration(String flagReiteration) {
    this.flagReiteration = flagReiteration;
  }

  public String getFlagNTI() {
    return flagNTI;
  }

  public void setFlagNTI(String flagNTI) {
    this.flagNTI = flagNTI;
  }

  public Integer getStatusDisponibilit() {
    return statusDisponibilit;
  }

  public void setStatusDisponibilit(Integer statusDisponibilit) {
    this.statusDisponibilit = statusDisponibilit;
  }

  public Date getDateDisponibilit() {
    return dateDisponibilit;
  }

  public void setDateDisponibilit(Date dateDisponibilit) {
    this.dateDisponibilit = dateDisponibilit;
  }

  public String getDateDisponibilitString() {
    if (getDateDisponibilit() != null)
      return formatter.format(getDateDisponibilit());
    else
      return "";
  }

  public String getCodeWeeklyConsignmentFirst() {
    return codeWeeklyConsignmentFirst;
  }

  public void setCodeWeeklyConsignmentFirst(String codeWeeklyConsignmentFirst) {
    this.codeWeeklyConsignmentFirst = codeWeeklyConsignmentFirst;
  }

  public String getCodeMonthlyConsignmentFirst() {
    return codeMonthlyConsignmentFirst;
  }

  public void setCodeMonthlyConsignmentFirst(String codeMonthlyConsignmentFirst) {
    this.codeMonthlyConsignmentFirst = codeMonthlyConsignmentFirst;
  }

  public String getCodeWeeklyConsignmentSecond() {
    return codeWeeklyConsignmentSecond;
  }

  public void setCodeWeeklyConsignmentSecond(String codeWeeklyConsignmentSecond) {
    this.codeWeeklyConsignmentSecond = codeWeeklyConsignmentSecond;
  }

  public String getCodeMonthlyConsignmentSecond() {
    return codeMonthlyConsignmentSecond;
  }

  public void setCodeMonthlyConsignmentSecond(String codeMonthlyConsignmentSecond) {
    this.codeMonthlyConsignmentSecond = codeMonthlyConsignmentSecond;
  }

  public Integer getHeighBookMillimeters() {
    return heighBookMillimeters;
  }

  public void setHeighBookMillimeters(Integer heighBookMillimeters) {
    this.heighBookMillimeters = heighBookMillimeters;
  }

  public String getPrintFourCover() {
    return printFourCover;
  }

  public void setPrintFourCover(String printFourCover) {
    this.printFourCover = printFourCover;
  }

  public String getDataSource() {
    return dataSource;
  }

  public void setDataSource(String dataSource) {
    this.dataSource = dataSource;
  }

  public String getLevelCard() {
    return levelCard;
  }

  public void setLevelCard(String levelCard) {
    this.levelCard = levelCard;
  }

  public int getBibItemNumber() {
    return bibItemNumber;
  }

  public void setBibItemNumber(int i) {
    bibItemNumber = i;
  }

  public PersistenceState getPersistenceState() {
    return persistenceState;
  }

  public void setPersistenceState(PersistenceState state) {
    persistenceState = state;
  }

  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict(obj);
  }

  public void evict() throws DataAccessException {
    evict(this);
  }

  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }

  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + bibItemNumber;
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CasCache other = (CasCache) obj;
    return bibItemNumber == other.bibItemNumber;
  }

  public boolean isChanged() {
    return persistenceState.isChanged();
  }

  public boolean isDeleted() {
    return persistenceState.isDeleted();
  }

  public boolean isNew() {
    return persistenceState.isNew();
  }

  public boolean isRemoved() {
    return persistenceState.isRemoved();
  }

  public void markChanged() {
    persistenceState.markChanged();
  }

  public void markDeleted() {
    persistenceState.markDeleted();
  }

  public void markNew() {
    persistenceState.markNew();
  }

  public void markUnchanged() {
    persistenceState.markUnchanged();
  }

  public boolean onDelete(Session arg0) throws CallbackException {
    return persistenceState.onDelete(arg0);
  }

  public void onLoad(Session arg0, Serializable arg1) {
    persistenceState.onLoad(arg0, arg1);
  }

  public boolean onSave(Session arg0) throws CallbackException {
    return persistenceState.onSave(arg0);
  }

  public boolean onUpdate(Session arg0) throws CallbackException {
    return persistenceState.onUpdate(arg0);
  }

  public void generateNewKey() throws DataAccessException {
    // not applicable for this class
  }

  @Override
  public AbstractDAO getDAO() {
    return new CasCacheDAO();
  }

  public String getNtrLevel() {
    return ntrLevel;
  }

  public void setNtrLevel(String ntrLevel) {
    this.ntrLevel = ntrLevel;
  }

  public String getDigCheck() {
    return digCheck;
  }

  public void setDigCheck(String digCheck) {
    this.digCheck = digCheck;
  }

  public Integer getPagMin() {
    return pagMin;
  }

  public void setPagMin(Integer pagMin) {
    this.pagMin = pagMin;
  }

  public Integer getPagMax() {
    return pagMax;
  }

  public void setPagMax(Integer pagMax) {
    this.pagMax = pagMax;
  }

  public String getOnlineCheck() {
    return onlineCheck;
  }

  public void setOnlineCheck(String onlineCheck) {
    this.onlineCheck = onlineCheck;
  }

  public String getContinuazCheck() {
    return continuazCheck;
  }

  public void setContinuazCheck(String continuazCheck) {
    this.continuazCheck = continuazCheck;
  }

  public String getFlagIsStock() {
    return flagIsStock;
  }

  public void setFlagIsStock(String flagIsStock) {
    this.flagIsStock = flagIsStock;
  }

  public String getMdrFgl() {
    return mdrFgl;
  }

  public void setMdrFgl(String mdrFgl) {
    this.mdrFgl = mdrFgl;
  }

  public Date getLstPriceDtIni() {
    return lstPriceDtIni;
  }

  public void setLstPriceDtIni(Date lstPriceDtIni) {
    this.lstPriceDtIni = lstPriceDtIni;
  }

  public Date getLstPriceDtFin() {
    return lstPriceDtFin;
  }

  public void setLstPriceDtFin(Date lstPriceDtFin) {
    this.lstPriceDtFin = lstPriceDtFin;
  }

  public Date getLstDtCrt() {
    return lstDtCrt;
  }

  public void setLstDtCrt(Date lstDtCrt) {
    this.lstDtCrt = lstDtCrt;
  }

  public String getLstUser() {
    return lstUser;
  }

  public void setLstUser(String lstUser) {
    this.lstUser = lstUser;
  }

  public String getLstNote() {
    return lstNote;
  }

  public void setLstNote(String lstNote) {
    this.lstNote = lstNote;
  }

  public String getLstCurcy() {
    return lstCurcy;
  }

  public void setLstCurcy(String lstCurcy) {
    this.lstCurcy = lstCurcy;
  }

  public String getLstType() {
    return lstType;
  }

  public void setLstType(String lstType) {
    this.lstType = lstType;
  }

  public boolean isExistAdminData() {
    return existAdminData;
  }

  public void setExistAdminData(boolean existAdminData) {
    this.existAdminData = existAdminData;
  }

  public String getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(String productCategory) {
    this.productCategory = productCategory;
  }
}
