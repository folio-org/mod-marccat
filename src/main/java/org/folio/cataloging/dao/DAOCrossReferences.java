package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.persistence.Descriptor;
import org.folio.cataloging.dao.persistence.REF;
import org.folio.cataloging.dao.persistence.ReferenceType;

import java.util.List;

/**
 * Abstract class representing the Cross References for a single heading
 *
 * @author paulm
 */
public class DAOCrossReferences extends AbstractDAO {
	protected Log logger = LogFactory.getLog(DAOCrossReferences.class);


	public void save(Persistence p) throws DataAccessException {
		final REF ref = (REF)p;
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
				throws HibernateException {
				// save this row
				s.save(ref);
				logger.debug("saving ref with linkDisplay '" + ref.getLinkDisplay() + "'");
				// calculate the reciprocal
				REF reciprocal = ref.createReciprocal();
				// save the reciprocal
				s.save(reciprocal);
			}
		}
		.execute();
	}

	public void delete(Persistence p) throws DataAccessException {
		final REF ref = (REF)p;
		final REF reciprocal =
			loadReciprocal(ref, View.toIntView(ref.getUserViewString()));
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
				throws HibernateException {
				s.delete(ref);
				s.delete(reciprocal);
			}
		}
		.execute();
	}

	public REF loadReciprocal(REF ref, int cataloguingView)
		throws DataAccessException {

		int reciprocalType =
			ReferenceType.getReciprocal(ref.getType());

		REF result = null;

		List l =
			find(
				"from "
					+ ref.getClass().getName()
					+ " as ref "
					+ " where ref.key.target = ? AND "
					+ " ref.key.source = ? AND "
					+ " substr(ref.key.userViewString, ?, 1) = '1' AND "
					+ " ref.key.type = ?",
				new Object[] {
					ref.getSource(),
					ref.getTarget(),
					cataloguingView,
					reciprocalType},
				new Type[] {
					Hibernate.INTEGER,
					Hibernate.INTEGER,
					Hibernate.INTEGER,
					Hibernate.INTEGER });
		if (l.size() == 1) {
			result = (REF) l.get(0);
		}

		return result;
	}

	public REF load(
		final Descriptor source,
		final Descriptor target,
		final short referenceType,
		final int cataloguingView,
	    final Session session)
			throws DataAccessException, HibernateException {

		return ((DAODescriptor) source.getDAO()).loadReference(
			source,
			target,
			referenceType,
			cataloguingView, session);
	}
}
