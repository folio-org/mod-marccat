package com.casalini.digital.business; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Defaults;
import librisuite.business.common.RecordNotFoundException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;

import com.casalini.digital.bean.DigitalPoliciesBean;
import com.casalini.hibernate.model.CasDigAdmin;
import com.libricore.librisuite.common.HibernateUtil;

@SuppressWarnings("unchecked")
public class DAOCasDigAdmin extends HibernateUtil
{	
	public DAOCasDigAdmin() {
		super();
	}

	public List loadCasDigAdmin(int bibNumber) throws DataAccessException 
	{
		List result = null;
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from CasDigAdmin as ct " + " where ct.bibItemNumber =" + bibNumber);
			q.setMaxResults(1);
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public List loadCasSapPubl(String editor) throws DataAccessException
	{
		String editore = editor.toLowerCase();
		List result = null;
		try {
			Session s = currentSession();
			result = s.find("from CasSapPubl as c " + " where lower(c.codEditore) = ? ",
					new Object[] {editore},	new Type[] { Hibernate.STRING });
			
			if (result.size() == 0) {
				throw new RecordNotFoundException("CasSapPubl : codice editore " + editor + " not found");
			}

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public List loadCasSapPublBreve(String editorSmall) throws DataAccessException
	{
		String editorBreve = editorSmall.toLowerCase();
		List result = null;
		try {
			Session s = currentSession();
			result = s.find("from CasSapPubl as c " + " where lower(c.codEditoreBreve) = ? ",
					new Object[] {editorBreve},	new Type[] { Hibernate.STRING });
			
			if (result.size() == 0) {
				throw new RecordNotFoundException();
			}
	
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}

	
	public List loadCasSapPublFromDen(String denEditor) throws DataAccessException 
	{
		List result = null;
		String denominaz= "'" + denEditor.toLowerCase() + "%'";
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct from CasSapPubl as ct where lower(ct.denEditore) like "
					+ denominaz	+ " order by ct.codEditore" );
			result = q.list();
			
			if (result.size() == 0) {
				throw new RecordNotFoundException();
			}

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	/**
	 * Metodo che legge le policy scelte e salvate in precedenza nella tabella CAS_DGA_POLICY
	 * @param bibNumber
	 * @return lista delle policy
	 * @throws DataAccessException
	 */
	public List loadCasDgaPolicy(int bibNumber) throws DataAccessException 
	{	
		List result = new ArrayList();
		try {
			Session s = currentSession();
			Query q = s.createQuery("select distinct ct"
					+ " from CasDgaPolicy as ct " + " where ct.bibItemNumber ="
					+ bibNumber 
					+ " order by ct.idPolicy, ct.typePolicy");
			result = q.list();

		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}
	
	public void deleteCasDgaPolicyByBibNumber(int bibNumber) throws DataAccessException 
	{
		Session s = currentSession();
		Transaction tx = null;
		try{	
			 tx = s.beginTransaction();
			 s.delete("from CasDgaPolicy as ct where ct.bibItemNumber =" + bibNumber );
			 tx.commit();
	    	
		} catch (HibernateException e) {
			 if (tx != null) {
	             try {
	                 tx.rollback();
	             } catch (HibernateException e1) {
	            	 logAndWrap(e);
	             }
	         }
		}
	}
	
	public void deleteCasDgaPolicy(int bibNumber) throws DataAccessException 
	{
		try {
			Session s = currentSession();
			s.delete("from CasDgaPolicy as ct where ct.bibItemNumber =" + bibNumber );

		} catch (HibernateException e) {
			logAndWrap(e);
		}
	}	
	
	public void deleteCasDgaPolicyFromKey(String code, String type, int bibNumber) throws DataAccessException 
	{
		try {
			Session s = currentSession();
			s.delete("from CasDgaPolicy as ct where ct.bibItemNumber =" + bibNumber 
					+ " and ct.idPolicy = '" + code + "' and ct.typePolicy = '" + type	+ "'");

		} catch (HibernateException e) {
			logAndWrap(e);
		}
	}	
	
	/**
	 * Medoto che legge tutte le policy disponibili dalla vista Sap
	 * @return lista policy disponibili
	 * @throws DataAccessException
	 */
	public List loadPolicy(String publisherCode) throws DataAccessException
	{
		List listPolicies = new ArrayList();
		DigitalPoliciesBean policiesBean = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		if (publisherCode !=null && publisherCode.trim().length()>0){
			try {					
				Class.forName("net.sourceforge.jtds.jdbc.Driver");
				connection = DriverManager.getConnection(Defaults.getString("sql.server.policy.connection.url"),Defaults.getString("sql.server.policy.connection.username"), Defaults.getString("sql.server.policy.connection.password"));
				statement = connection.prepareStatement("SELECT * FROM POLICY_EDITORI WHERE CARDCODE = ?");
				statement.setString(1, publisherCode);
				rs = statement.executeQuery();
				while (rs.next()){
					policiesBean = new DigitalPoliciesBean();
					policiesBean.setPolicyCode(rs.getString("ITEMCODE"));
					policiesBean.setPolicyName(rs.getString("ITEMNAME"));
					policiesBean.setPolicyPrice(new Float(rs.getFloat("PRICE")));			
					policiesBean.setPolicyCurcy(rs.getString("CURRENCY"));
					policiesBean.setPolicyType(rs.getString("POLICYTYPE"));
					policiesBean.setPolicyStamps(rs.getInt("COPY"));
					policiesBean.setDgaPolicy(false);
					/* 20100521 inizio: impostazione prezzo totale per policy di tipo "I" */				
					if ("I".equalsIgnoreCase(policiesBean.getPolicyType())) {
						policiesBean.setPolicyTotPrice(new Float(rs.getFloat("PRICE")));
					}else {
						policiesBean.setPolicyTotPrice(new Float(0));
					}
					listPolicies.add(policiesBean);
				}
			} catch(Throwable exception)
			{ 
				exception.printStackTrace();
				throw new DataAccessException();
				
			} finally {
				try{ rs.close(); } catch(Exception ex){}
				try{ statement.close(); } catch(Exception ex){}
				try{ connection.close(); } catch(Exception ex){}
			}
		}		
		return listPolicies;
	}
	
	
	/**
	 * the method set the default values from Policy and Fruition type for this publisher code
	 * @throws DataAccessException
	 */
	public void loadPolicyAndFruitionType(String publisherCode,CasDigAdmin casDigA) throws DataAccessException
	{	
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
			    
		if (publisherCode !=null && publisherCode.trim().length()>0){
			try {					
				Class.forName("net.sourceforge.jtds.jdbc.Driver");
				connection = DriverManager.getConnection(Defaults.getString("sql.server.policy.connection.url"),Defaults.getString("sql.server.policy.connection.username"), Defaults.getString("sql.server.policy.connection.password"));
				statement = connection.prepareStatement("SELECT * FROM TIPOFRUIZIONE_EDITORI WHERE CARDCODE = ?");
				statement.setString(1, publisherCode);
				rs = statement.executeQuery();
				while (rs.next()){
					casDigA.setPolicyOnlineType(rs.getString("ITEMCODE"));
				    casDigA.setSaleTypeId(new Integer(rs.getString("TIPOFRUIZIONE")));
				}
				
			} catch(Throwable exception){ 
				exception.printStackTrace();
				throw new DataAccessException();
				
			} finally {
				try{ rs.close(); } catch(Exception ex){}
				try{ statement.close(); } catch(Exception ex){}
				try{ connection.close(); } catch(Exception ex){}
			}
		}			
	}
}