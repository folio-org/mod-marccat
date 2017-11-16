/*
 * (c) LibriCore
 * 
 * Created on Dec 20, 2005
 * 
 * DAOModel.java
 */
package librisuite.business.cataloguing.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import librisuite.business.codetable.ValueLabelElement;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.ReferentialIntegrityException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.TransactionalHibernateOperation;

/**
 * abstract class for common implementations of DAOModels (Bib and Auth)
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/01/05 13:25:59 $
 * @since 1.0
 */
public abstract class DAOModel extends HibernateUtil {

	private static final Log logger = LogFactory.getLog(DAOModel.class);
	/**
		 * retrieves a list of booleans representing whether a model in the list of all
		 * models is currently in use by a bib item.  Used in Models page to prompt for
		 * cascading delete, if required.
		 * 
		 * @since 1.0
		 */
	public List getModelUsageList() throws DataAccessException {
		/*
		 * TODO look at making this "set" retrieval rather than looping and/or
		 * modifying the page logic so that the testing for delete is done on the
		 * server rather than at the client
		 */
		List l = getModelList();
		List result = new ArrayList();
		Iterator iter = l.iterator();
		ValueLabelElement aModel;
		while (iter.hasNext()) {
			aModel = (ValueLabelElement) iter.next();
			result.add(
				new Boolean(
					getModelItemDAO().getModelUsage(
						Integer.parseInt(aModel.getValue()))));
		}
		return result;
	}

	public boolean getModelUsage(int modelId) throws DataAccessException {
		return getModelItemDAO().getModelUsage(modelId);
	}

	protected abstract DAOModelItem getModelItemDAO();

	public Model load(int id) throws DataAccessException {

		logger.debug("model number: " + id);
		Model result = (Model) load(getPersistentClass(), new Integer(id));
		return result;
	}

	abstract protected Class getPersistentClass();

	public void delete(final Model model)
		throws ReferentialIntegrityException, DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session session)
				throws HibernateException {
				session.delete(
					"from "
						+ getModelItemDAO().getPersistentClass().getName()
						+ " as item"
						+ " where item.model.id = ? ",
					new Integer(model.getId()),
					Hibernate.INTEGER);
				session.delete(model);
			}
		}
		.execute();
	}

	/**
		 * retrieves the list of models for display in optionsCollections
		 * 
		 * @since 1.0
		 */
	public List getModelList() throws DataAccessException {
		return find(
			" select new librisuite.business.codetable.ValueLabelElement(m.id, m.label) "
				+ " from "
				+ getPersistentClass().getName()
				+ " as m "
				+ " order by m.label");
	}
	
	/**
	 * retrieves the list of models for display in optionsCollections
	 * 
	 * @since 1.0
	 */
	public List getBibliographicModelList() throws DataAccessException {
		return find(
				" select new librisuite.business.codetable.ValueLabelElement(m.id, m.label) "
				+ " from "
				+ " librisuite.business.cataloguing.bibliographic.BibliographicModel "
				+ " as m "
				+ " order by m.label");
	}	
	
	/**
	 * retrieves the list of models for display in optionsCollections
	 * 
	 * @since 1.0
	 */
	public List getAuthorityModelList() throws DataAccessException {
		return find(
				" select new librisuite.business.codetable.ValueLabelElement(m.id, m.label) "
				+ " from "
				+ " librisuite.business.cataloguing.authority.AuthorityModel "
				+ " as m "
				+ " order by m.label");
	}	

}