/*
 * (c) LibriCore
 * 
 * Created on Dec 22, 2004
 * 
 * DAOPublisherTag.java
 */
package librisuite.business.cataloguing.bibliographic;

import java.util.List;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.UpdateStatus;
import librisuite.hibernate.BibliographicRelationReciprocal;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.TransactionalHibernateOperation;
/**
  * @author hansv
 * @version $Revision: 1.6 $ $Date: 2007/04/09 09:58:07 $
 * @since 1.0
 */
public class DAOBibliographicRelationshipTag extends HibernateUtil {
	private static final Log logger =
		LogFactory.getLog(BibliographicRelationshipTag.class);
	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.HibernateUtil#delete(librisuite.business.common.Persistence)
	 */
	public void delete(Persistence po) throws DataAccessException {
		if (!(po instanceof BibliographicRelationshipTag)) {
			throw new IllegalArgumentException("I can only persist BibliographicRelationshipTag objects");
		}
		/*
		 * If we want to delete a relationship tag, it is the last saved values that
		 * need to be deleted from the database (not the current values).  Hence we
		 * get the originalTag values from the tag.
		 */
		BibliographicRelationshipTag aRelation =
			((BibliographicRelationshipTag) po).getOriginalTag();
		evictAny(aRelation.getSourceRelationship());
		aRelation.getSourceRelationship().markDeleted();
		super.delete(aRelation.getSourceRelationship());
		if (aRelation.getTargetRelationship() != null) {
			evictAny(aRelation.getTargetRelationship());
			aRelation.getTargetRelationship().markDeleted();
			super.delete(aRelation.getTargetRelationship());
		}

		aRelation.setUpdateStatus(UpdateStatus.REMOVED);
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.HibernateUtil#save(librisuite.business.common.Persistence)
	 */
	public void save(final Persistence po) throws DataAccessException {
		if (!(po instanceof BibliographicRelationshipTag)) {
			throw new IllegalArgumentException("I can only persist BibliographicRelationshipTag objects");
		}
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
				throws HibernateException, DataAccessException {
				BibliographicRelationshipTag aRelation =
					(BibliographicRelationshipTag) po;
			

				logger.debug("save bibliographicRelationshipTag");
				logger.debug("reciprocalOption is " + aRelation.getReciprocalOption());
				logger.debug("target is " + aRelation.getTargetRelationship() == null ? "" : "not " + "null");
                
				/* first evict all objects from Hibernate cache */
				/*evictAny(aRelation.getSourceRelationship());
				evictAny(aRelation.getOriginalTag().getSourceRelationship());
				if (aRelation.getTargetRelationship() != null) {
					evictAny(aRelation.getTargetRelationship());
				}
				if (aRelation.getOriginalTag().getTargetRelationship()
					!= null) {
					evictAny(aRelation.getOriginalTag().getTargetRelationship());
				}*/

				/*
				 * Then delete any pre-existing RLTSP rows
				 */
				BibliographicRelationship aTarget =
					aRelation.getOriginalTag().getTargetRelationship();
				if (aTarget != null) {
					s.delete(
						"from BibliographicRelationship as rltsp "
							+ " where rltsp.bibItemNumber = ? "
							+ " and rltsp.targetBibItemNumber = ? "
							+ " and rltsp.relationTypeCode = ? "
							+ " and rltsp.userViewString = ? ",
						new Object[] {
							new Integer(aTarget.getBibItemNumber()),
							new Integer(aTarget.getTargetBibItemNumber()),
							new Short(aTarget.getRelationTypeCode()),
							aTarget.getUserViewString()},
						new Type[] {
							Hibernate.INTEGER,
							Hibernate.INTEGER,
							Hibernate.SHORT,
							Hibernate.STRING });
				}
				BibliographicRelationship aSource =
					aRelation.getOriginalTag().getSourceRelationship();
				if (aSource != null && !aSource.isNew()) {
					s.delete(
						"from BibliographicRelationship as rltsp "
							+ " where rltsp.bibItemNumber = ? "
							+ " and rltsp.targetBibItemNumber = ? "
							+ " and rltsp.relationTypeCode = ? "
							+ " and rltsp.userViewString = ? ",
						new Object[] {
							new Integer(aSource.getBibItemNumber()),
							new Integer(aSource.getTargetBibItemNumber()),
							new Short(aSource.getRelationTypeCode()),
							aSource.getUserViewString()},
						new Type[] {
							Hibernate.INTEGER,
							Hibernate.INTEGER,
							Hibernate.SHORT,
							Hibernate.STRING });
				}

				/* now flush the session to ensure that deletes are completed
				 * otherwise, it seems, duplicate key will be raised
				 */

				s.flush();

				/* now add the current values as new rows */

				aRelation.getSourceRelationship().markNew();
				if (BibliographicRelationReciprocal.isBlind(aRelation.getReciprocalOption())) {
					logger.debug("Reciprocal option is blind -- generating new key");
					aRelation.getSourceRelationship().generateNewBlindRelationshipKey();
				}
				else {
					logger.debug("Reciprocal option is not blind -- using existing target bib_itm_nbr");
				}
				persistByStatus(aRelation.getSourceRelationship());

				if ((aRelation.getTargetRelationship() != null)
					&& (BibliographicRelationReciprocal.isReciprocal(aRelation.getReciprocalOption()))) {
					aRelation.getTargetRelationship().markNew();
					persistByStatus(aRelation.getTargetRelationship());
				}
				aRelation.markUnchanged();
				aRelation.setOriginalTag();
			}
		}
		.execute();

	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.HibernateUtil#update(librisuite.business.common.Persistence)
	 */
	public void update(Persistence p) throws DataAccessException {
		/*
		 * Since we are deleting and re-adding, save and update are the same
		 */
		logger.debug("update bibliographicRelationshipTag");

		save(p);
	}

	public short getReciprocalType(short relationTypeCode)
		throws DataAccessException {

		try {
			List result =
				currentSession().find(
					"select rcpcl.relationshipReciprocalTypeCode from librisuite.hibernate.RelationReciprocal rcpcl where rcpcl.relationshipTypeCode = ?",
					new Object[] { new Integer(relationTypeCode)},
					new Type[] { Hibernate.INTEGER });
			return ((Integer) result.get(0)).shortValue();
		} catch (HibernateException he) {
			logAndWrap(he);
		}

		return -1;
	}

	private void evictAny(BibliographicRelationship aRelation) throws DataAccessException {
			BibliographicRelationship inCache = (BibliographicRelationship)
				get(BibliographicRelationship.class, aRelation);
			if (inCache != null) {
				inCache.evict();
			}
	}
}