package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import org.folio.cataloging.dao.DAOCasDigAdmin;
import org.folio.cataloging.dao.common.HibernateUtil;

public class CasSapPubl implements Persistence 
{
	private static final long serialVersionUID = 2522128570785338271L;

//	static DAOCasSapPubl dao = new DAOCasSapPubl(); 
	static DAOCasDigAdmin dao = new DAOCasDigAdmin();
	
	private String codEditore;
	private String codEditoreBreve;
	private String denEditore;
	private Float  costoPagEdi;
	private String costoPagCurcyEdi;
	private Float  costoArticoloEdi;
	private String costoArticoloCurcyEdi;
	private Float  costoSezioneEdi;
	private String costoSezioneCurcyEdi;
	private Float  costoCapitoloEdi;
	private String costoCapitoloCurcyEdi;
	private Float  costoRivistaEdi;
	private String costoRivistaCurcyEdi;
	private String flagCopiaIncollaEdi;
	private String flagOperStampaEdi;
	private Integer numOperStampaEdi;
	private String flagFullText;	

	private PersistenceState persistenceState = new PersistenceState();
	
	public CasSapPubl(String editor) {
		this();
		setCodEditore(editor);
	}
	
	public CasSapPubl() {
		super();
	}

	public void setPersistenceState(PersistenceState state) {
		persistenceState = state;
	}

	public void evict(Object obj) throws DataAccessException {
		persistenceState.evict(obj);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codEditore == null) ? 0 : codEditore.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CasSapPubl other = (CasSapPubl) obj;
		if (codEditore == null) {
			if (other.codEditore != null)
				return false;
		} else if (!codEditore.equals(other.codEditore))
			return false;
		return true;
	}

	public void evict() throws DataAccessException {
		evict((Object)this);
	}
	
	public HibernateUtil getDAO() {
		return dao;
	}

	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
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

	public String getCodEditore() {
		return codEditore;
	}

	public void setCodEditore(String codEditore) {
		this.codEditore = codEditore;
	}

	public String getDenEditore() {
		return denEditore;
	}

	public void setDenEditore(String denEditore) {
		this.denEditore = denEditore;
	}

	public Float getCostoPagEdi() {
		return costoPagEdi;
	}

	public void setCostoPagEdi(Float costoPagEdi) {
		this.costoPagEdi = costoPagEdi;
	}

	public Float getCostoArticoloEdi() {
		return costoArticoloEdi;
	}

	public void setCostoArticoloEdi(Float costoArticoloEdi) {
		this.costoArticoloEdi = costoArticoloEdi;
	}

	public Float getCostoSezioneEdi() {
		return costoSezioneEdi;
	}

	public void setCostoSezioneEdi(Float costoSezioneEdi) {
		this.costoSezioneEdi = costoSezioneEdi;
	}

	public Float getCostoCapitoloEdi() {
		return costoCapitoloEdi;
	}

	public void setCostoCapitoloEdi(Float costoCapitoloEdi) {
		this.costoCapitoloEdi = costoCapitoloEdi;
	}

	public String getFlagCopiaIncollaEdi() {
		return flagCopiaIncollaEdi;
	}

	public void setFlagCopiaIncollaEdi(String flagCopiaIncollaEdi) {
		this.flagCopiaIncollaEdi = flagCopiaIncollaEdi;
	}

	public String getFlagOperStampaEdi() {
		return flagOperStampaEdi;
	}

	public void setFlagOperStampaEdi(String flagOperStampaEdi) {
		this.flagOperStampaEdi = flagOperStampaEdi;
	}

	public Integer getNumOperStampaEdi() {
		return numOperStampaEdi;
	}

	public void setNumOperStampaEdi(Integer numOperStampaEdi) {
		this.numOperStampaEdi = numOperStampaEdi;
	}

	public Float getCostoRivistaEdi() {
		return costoRivistaEdi;
	}

	public void setCostoRivistaEdi(Float costoRivistaEdi) {
		this.costoRivistaEdi = costoRivistaEdi;
	}

	public String getCostoPagCurcyEdi() {
		return costoPagCurcyEdi;
	}

	public void setCostoPagCurcyEdi(String costoPagCurcyEdi) {
		this.costoPagCurcyEdi = costoPagCurcyEdi;
	}

	public String getCostoArticoloCurcyEdi() {
		return costoArticoloCurcyEdi;
	}

	public void setCostoArticoloCurcyEdi(String costoArticoloCurcyEdi) {
		this.costoArticoloCurcyEdi = costoArticoloCurcyEdi;
	}

	public String getCostoSezioneCurcyEdi() {
		return costoSezioneCurcyEdi;
	}

	public void setCostoSezioneCurcyEdi(String costoSezioneCurcyEdi) {
		this.costoSezioneCurcyEdi = costoSezioneCurcyEdi;
	}

	public String getCostoCapitoloCurcyEdi() {
		return costoCapitoloCurcyEdi;
	}

	public void setCostoCapitoloCurcyEdi(String costoCapitoloCurcyEdi) {
		this.costoCapitoloCurcyEdi = costoCapitoloCurcyEdi;
	}

	public String getCostoRivistaCurcyEdi() {
		return costoRivistaCurcyEdi;
	}

	public void setCostoRivistaCurcyEdi(String costoRivistaCurcyEdi) {
		this.costoRivistaCurcyEdi = costoRivistaCurcyEdi;
	}

	public String getCodEditoreBreve() {
		return codEditoreBreve;
	}

	public void setCodEditoreBreve(String codEditoreBreve) {
		this.codEditoreBreve = codEditoreBreve;
	}
	
	public String getFlagFullText() {
		return flagFullText;
	}

	public void setFlagFullText(String flagFullText) {
		this.flagFullText = flagFullText;
	}
}
