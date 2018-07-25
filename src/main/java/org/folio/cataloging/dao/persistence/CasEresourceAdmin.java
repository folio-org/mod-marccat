package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOCasEresourceAdmin;

import java.io.Serializable;

public class CasEresourceAdmin implements Persistence 
{
	private static final long serialVersionUID = 2522128570785338271L;

	static DAOCasEresourceAdmin dao = new DAOCasEresourceAdmin(); 
	
	private int     bibItemNumber;
	private String  fileName;
	private String  fileSize;
	private String  flagWebStatus;
	private String  flagAllegato;
	private String  flagSupplemento;
	private String  flagStato;
	private String  flagCopiaIncolla;
	private String  opeStampa;
	private String  numStampa;
	private Float   costoPagina;
	private String  costoPaginaCurcy;
	private Float   costoRivista;
	private String  costoRivistaCurcy;
	private Float   costoArticolo;
	private String  costoArticoloCurcy;
	private Float   costoSezione;
	private String  costoSezioneCurcy;
	private Float   costoCapitolo;
	private String  costoCapitoloCurcy;
	private Float   costoItem;	
	private String  costoItemCurcy;
	private Integer pageTot;
	private Integer saleTypeId;
	private String  itemTypeSale;
	private String  anno;
	private String  volume;
	private String  idFascicolo;
	private String  capitolo;
	private Integer pagineDa;
	private Integer pagineA;
	private String  codEditore;
	private String  codEditoreBreve;
	private String  denEditore;
	private String  progressivo;
	private String  descrizione;
	private String  Pdf;

	public CasEresourceAdmin(int bibItemNumber) {
		this();
		setBibItemNumber(bibItemNumber);
	}
	
	private PersistenceState persistenceState = new PersistenceState();
	
	/**
	 * Class constructor
	 */
	public CasEresourceAdmin() {
		super();
	}

	public int getBibItemNumber() {
		return bibItemNumber;
	}

	public PersistenceState getPersistenceState() {
		return persistenceState;
	}

	public void setBibItemNumber(int i) {
		bibItemNumber = i;
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
	
	public AbstractDAO getDAO() {
		return dao;
	}

	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
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
		final CasEresourceAdmin other = (CasEresourceAdmin) obj;
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

	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		// not applicable for this class
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public Integer getSaleTypeId() {
		return saleTypeId;
	}

	public void setSaleTypeId(Integer saleTypeId) {
		this.saleTypeId = saleTypeId;
	}

	public String getItemTypeSale() {
		return itemTypeSale;
	}

	public void setItemTypeSale(String itemTypeSale) {
		this.itemTypeSale = itemTypeSale;
	}


	public String getCodEditore() {
		return codEditore;
	}

	public void setCodEditore(String codEditore) {
		this.codEditore = codEditore;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	public void setIdFascicolo(String fascicolo) {
		this.idFascicolo = fascicolo;
	}

	public String getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}

	public String getFlagAllegato() {
		return flagAllegato;
	}

	public void setFlagAllegato(String flagAllegato) {
		this.flagAllegato = flagAllegato;
	}

	public String getFlagSupplemento() {
		return flagSupplemento;
	}

	public void setFlagSupplemento(String flagSupplemento) {
		this.flagSupplemento = flagSupplemento;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getFlagStato() {
		return flagStato;
	}

	public void setFlagStato(String flagStato) {
		this.flagStato = flagStato;
	}

	public String getFlagCopiaIncolla() {
		return flagCopiaIncolla;
	}

	public void setFlagCopiaIncolla(String flagCopiaIncolla) {
		this.flagCopiaIncolla = flagCopiaIncolla;
	}

	public String getOpeStampa() {
		return opeStampa;
	}

	public void setOpeStampa(String opeStampa) {
		this.opeStampa = opeStampa;
	}

	public String getFlagWebStatus() {
		return flagWebStatus;
	}

	public void setFlagWebStatus(String flagWebStatus) {
		this.flagWebStatus = flagWebStatus;
	}

	public String getNumStampa() {
		return numStampa;
	}

	public void setNumStampa(String numStampa) {
		this.numStampa = numStampa;
	}

	public Integer getPageTot() {
		return pageTot;
	}

	public void setPageTot(Integer pageTot) {
		this.pageTot = pageTot;
	}
	
	public Float getCostoRivista() {
		return costoRivista;
	}

	public void setCostoRivista(Float costoRivista) {
		this.costoRivista = costoRivista;
	}

	public Float getCostoArticolo() {
		return costoArticolo;
	}

	public void setCostoArticolo(Float costoArticolo) {
		this.costoArticolo = costoArticolo;
	}

	public Float getCostoSezione() {
		return costoSezione;
	}

	public void setCostoSezione(Float costoSezione) {
		this.costoSezione = costoSezione;
	}

	public String getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}

	public String getPdf() {
		return Pdf;
	}

	public void setPdf(String pdf) {
		Pdf = pdf;
	}

	public Float getCostoPagina() {
		return costoPagina;
	}

	public void setCostoPagina(Float costoPagina) {
		this.costoPagina = costoPagina;
	}

	public Float getCostoItem() {
		return costoItem;
	}

	public void setCostoItem(Float costoItem) {
		this.costoItem = costoItem;
	}

	public String getDenEditore() {
		return denEditore;
	}

	public void setDenEditore(String denEditore) {
		this.denEditore = denEditore;
	}

	public Float getCostoCapitolo() {
		return costoCapitolo;
	}

	public void setCostoCapitolo(Float costoCapitolo) {
		this.costoCapitolo = costoCapitolo;
	}

	public String getCostoItemCurcy() {
		return costoItemCurcy;
	}

	public void setCostoItemCurcy(String costoItemCurcy) {
		this.costoItemCurcy = costoItemCurcy;
	}

	public String getCostoPaginaCurcy() {
		return costoPaginaCurcy;
	}

	public void setCostoPaginaCurcy(String costoPaginaCurcy) {
		this.costoPaginaCurcy = costoPaginaCurcy;
	}

	public String getCostoRivistaCurcy() {
		return costoRivistaCurcy;
	}

	public void setCostoRivistaCurcy(String costoRivistaCurcy) {
		this.costoRivistaCurcy = costoRivistaCurcy;
	}

	public String getCostoArticoloCurcy() {
		return costoArticoloCurcy;
	}

	public void setCostoArticoloCurcy(String costoArticoloCurcy) {
		this.costoArticoloCurcy = costoArticoloCurcy;
	}

	public String getCostoSezioneCurcy() {
		return costoSezioneCurcy;
	}

	public void setCostoSezioneCurcy(String costoSezioneCurcy) {
		this.costoSezioneCurcy = costoSezioneCurcy;
	}

	public String getCostoCapitoloCurcy() {
		return costoCapitoloCurcy;
	}

	public void setCostoCapitoloCurcy(String costoCapitoloCurcy) {
		this.costoCapitoloCurcy = costoCapitoloCurcy;
	}

	public Integer getPagineDa() {
		return pagineDa;
	}

	public void setPagineDa(Integer pagineDa) {
		this.pagineDa = pagineDa;
	}

	public Integer getPagineA() {
		return pagineA;
	}

	public void setPagineA(Integer pagineA) {
		this.pagineA = pagineA;
	}

	public String getCodEditoreBreve() {
		return codEditoreBreve;
	}

	public void setCodEditoreBreve(String codEditoreBreve) {
		this.codEditoreBreve = codEditoreBreve;
	}
	
}
