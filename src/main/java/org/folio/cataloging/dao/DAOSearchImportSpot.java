package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.in.Filtro;
import org.folio.cataloging.business.in.Fonte;
import org.folio.cataloging.dao.persistence.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DAOSearchImportSpot extends AbstractDAO {
	
	private static final Log logger = LogFactory.getLog(DAOSearchImportSpot.class);
	
	public List getFonti() throws DataAccessException {
		List l = null;
		List listaFonti = new ArrayList();
		Session s = currentSession();

		try {
			l =	s.find("from CasFonti as a");
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		
		Iterator iter = l.iterator();
		while (iter.hasNext()) {
			CasFonti aRow = (CasFonti) iter.next();
			listaFonti.add( new Avp(aRow.getIdFonte(), aRow.getNomeFonte()) );
		}
		
		return listaFonti;
	}	
	//Nicola
	/*public Fonte getFiltri(String idFonte, String nomeFonte) throws DataAccessException {
		List l = null;
		Fonte fonte = new Fonte();
		Session s = currentSession();

		try {
			l = s.find("from CasFiltriMigrazione as a "+
						"where a.idFonte = ? ",
					new Object[] {idFonte},
					new Type[] { Hibernate.STRING});
	
		} catch (HibernateException e) {
			logAndWrap(e);
		}
				
		fonte.setLabel(nomeFonte);
		fonte.setValue(new Integer(idFonte).intValue());
		
		Iterator iter = l.iterator();
		while (iter.hasNext()) {
			CasFiltriMigrazione aRow = (CasFiltriMigrazione) iter.next();			
			fonte.addNewFiltro (aRow.getFiltro(),aRow.getRagione());			
		}		
		return fonte;
	}*/

	//INIZIO CAMELIA, CARICAMENTO FILTRI, PRENDO LA LISTA DEGLI FILTRI
	public Fonte getFiltri(String idFonte, String nomeFonte) throws DataAccessException {
		List l = null;
		List f = null;
		Fonte fonte = new Fonte();
		Session s = currentSession();

		try {
			l = s.find("from CasFiltriImport as a "+
						"where a.idFonte = ? ",
					new Object[] {idFonte},
					new Type[] { Hibernate.STRING});
	
		} catch (HibernateException e) {
			logAndWrap(e);
		}
				
		fonte.setLabel(nomeFonte);
		fonte.setValue(idFonte);
		
		Iterator iter = l.iterator();
		while (iter.hasNext()) {
			CasFiltriImport aRow = (CasFiltriImport) iter.next();
			try {
				f = s.find("from CasFiltri as a "+
						   "where a.idFiltro = ? ",
					new Object[] {new Integer(aRow.getIdFiltro())},
					new Type[] { Hibernate.INTEGER});
					Iterator iterF = f.iterator();
					while (iterF.hasNext()) {
						CasFiltri bRow = (CasFiltri) iterF.next();
						Filtro filtro = new Filtro();
						filtro.setIdFiltro(bRow.getIdFiltro());
						filtro.setDescFiltro(bRow.getNomeFiltro());							
						filtro.setTipoFiltro(bRow.getTipoFiltro());
						filtro.setValFiltro(aRow.getValFiltro());						
							
						fonte.addNewFiltro(filtro);
					}
					
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}		
		return fonte;
	}

	//FINE CAMELIA
	public int getMessageID(int transactionId) throws DataAccessException {
		List l = null;
		int messageID = 0;
		Session s = currentSession();

		try {
			l = s.find("select a.idEsito " +
					"from CasEsitoImport a "+
					"where a.transactionId = ?",
					new Object[] {new Integer(transactionId)},
					new Type[] { Hibernate.INTEGER});
	
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		
		if (l.size()>0)
			messageID = new Integer(l.get(0).toString()).intValue();	
				
		return messageID;
	}
	
	public int getTransactionId() throws DataAccessException {
		List l = null;
		int transactionId = 0;
		Session s = currentSession();

		try {
			l = s.find("select max(a.transactionId) " +
					"from CasEsitoImport a");
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		
		if (l.size()>0)
			transactionId = new Integer(l.get(0).toString()).intValue();	
				
		return transactionId;
	}
	
	public String getNomeFonte(String idFonte) throws DataAccessException {
		List l = null;
		String nomeFonte = "";
		Session s = currentSession();

		try {
			l =	s.find("select a.nomeFonte from CasFonti as a where a.idFonte = ? ",
					new Object[] {idFonte},
					new Type[] { Hibernate.STRING});
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		
		if (l.size()>0)
			nomeFonte = l.get(0).toString();		
		return nomeFonte;
	}
	public String updateFiltri(Fonte fonte) throws DataAccessException {
		Session s = currentSession();	

		try {

			Transaction tr = s.beginTransaction();

			CasFiltriImportSpot cfis=new CasFiltriImportSpot();
			cfis.setIdFonte(fonte.getValue());

			for(int i=0;i<fonte.getFiltri().size();i++){
				cfis.setIdFiltro(((Filtro)fonte.getFiltri().get(i)).getIdFiltro());
				if(((Filtro)fonte.getFiltri().get(i)).getValFiltroLista()==null){
					cfis.setValFiltro(((Filtro)fonte.getFiltri().get(i)).getValFiltro());}
				else{
					cfis.setValFiltro(((Filtro)fonte.getFiltri().get(i)).getValFiltro());
				}
				s.update(cfis);

				tr.commit();

//				System.out.println("Update successfully!");
				}
//			s.close();
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return "import.spot.executed";		
	}
	
	public String saveSourceState(Fonte fonte) throws DataAccessException {
		
		return "insert successfully!";		
	}
	
	public String insertActivitySource (String fonteCorrente)throws DataAccessException {
		String ret="";

		Session s = currentSession();	

		try {

			Transaction tr = s.beginTransaction();
			CasActvSpot cas=new CasActvSpot();
			cas.setSourceId(fonteCorrente);
			String active="Y";
			cas.setActive(active.charAt(0));

			s.save(cas);

			tr.commit();

//			System.out.println("Insert successfully!");
			s.close();

		} catch (HibernateException e) {
			logAndWrap(e);
			logger.error(e.getMessage());
		}

		return ret;
	}
	public List isInActivitySourceTable(String fonteCorrente)throws DataAccessException {
		
		Session s = currentSession();	
        List list=null;
		try {
			Transaction tr = s.beginTransaction();
			list=s.find("from CasActvSpot as c where c.sourceId='"+fonteCorrente+"'");
		} catch (HibernateException e) {
			logAndWrap(e);
			logger.error(e.getMessage());
		}
		
		return list;
	}
}
