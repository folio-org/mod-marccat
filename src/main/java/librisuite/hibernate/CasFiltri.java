package librisuite.hibernate;

import java.io.Serializable;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import com.casalini.in.business.importSpot.DAOSearchImportSpot;
import com.libricore.librisuite.common.HibernateUtil;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;

public class CasFiltri implements Persistence{
	
	private static final long serialVersionUID = 2522128570785338271L;
	
	//static DAOCasFiltri dao = new DAOCasFiltri();
	static DAOSearchImportSpot dao = new DAOSearchImportSpot();
	
	private int fltrItemNumber;
	private int idFiltro;
	//private String idFonte;
	private String nomeFiltro;
	private String tipoFiltro;
	
	public int getIdFiltro() {
		return idFiltro;
	}
	public void setIdFiltro(int idFiltro) {
		this.idFiltro = idFiltro;
	}	
	/*public String getIdFonte() {
		return idFonte;
	}
	public void setIdFonte(String idFonte) {
		this.idFonte = idFonte;
	}*/
	public String getNomeFiltro() {
		return nomeFiltro;
	}
	public void setNomeFiltro(String nomeFiltro) {
		this.nomeFiltro = nomeFiltro;
	}
	public String getTipoFiltro() {
		return tipoFiltro;
	}
	public void setTipoFiltro(String tipoFiltro) {
		this.tipoFiltro = tipoFiltro;
	}

	private PersistenceState persistenceState = new PersistenceState();
	
	/**
	 * Class constructor
	 */
	public CasFiltri() {
		super();
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
		evict((Object)this);
	}
	
	public HibernateUtil getDAO() {
		return dao;
	}

	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}

//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + bibItemNumber;
//		return result;
//	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CasFiltri other = (CasFiltri) obj;
		if (fltrItemNumber != other.fltrItemNumber)
			return false;
		return true;
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

	public void generateNewKey() throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

}
