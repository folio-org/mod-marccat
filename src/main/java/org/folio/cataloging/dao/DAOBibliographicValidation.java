/*
 * (c) LibriCore
 * 
 * Created on 03-ago-2004
 * 
 * DAOBibMarcTagValidation.java
 */
package org.folio.cataloging.dao;

import java.util.List;

import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.common.Validation;
import org.folio.cataloging.dao.persistence.BibliographicValidation;
import org.folio.cataloging.dao.persistence.BibliographicValidationKey;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.common.CorrelationValues;
import org.folio.cataloging.business.common.DataAccessException;

/**
 * @author elena
 * @version $Revision: 1.8 $, $Date: 2007/02/13 09:17:59 $
 * @since 1.0
 */
public class DAOBibliographicValidation extends DAOValidation {
	
	private static final Log logger = LogFactory.getLog(DAOBibliographicValidation.class);

	public BibliographicValidation load(String marcNumber, short marcCategory)
		throws DataAccessException {
		return (BibliographicValidation) load(
			BibliographicValidation.class,
			new BibliographicValidationKey(marcNumber, marcCategory));
	}

	/**
	 * @param s
	 * @param values
	 * @return
	 */
	public Validation load(short s, CorrelationValues values) throws MarcCorrelationException, DataAccessException {
		/*modifica barbara 27/04/2007 prn 127 da verificare*/
		/*RIPRISTINATO -  DA PROBLEMI SUL CARICAMENTO DEI CODICI*/
		List l = find("select distinct v from BibliographicValidation as v, " +
			"BibliographicCorrelation as c" +
			" where c.key.marcTagCategoryCode = ?" +
			" and (c.databaseFirstValue = ? or c.databaseFirstValue = -1 or -1 = ?)" +
			" and (c.databaseSecondValue = ? or c.databaseSecondValue = -1 or -1 = ?)" +
			" and (c.databaseThirdValue = ? or c.databaseThirdValue = -1 or -1 = ?)" +
			" and c.key.marcTag = v.key.marcTag" +
			" and c.key.marcTagCategoryCode =  v.key.marcTagCategoryCode",
			new Object[] { new Short(s), 
				new Short(values.getValue(1)), new Short(values.getValue(1)),
				new Short(values.getValue(2)), new Short(values.getValue(2)),
				new Short(values.getValue(3)), new Short(values.getValue(3))},
			new Type[] { Hibernate.SHORT,
				Hibernate.SHORT, Hibernate.SHORT,
				Hibernate.SHORT, Hibernate.SHORT, 
				Hibernate.SHORT, Hibernate.SHORT}
				);
		if (l.size() > 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("BibliographicValidation(s) found:");
				for (int i = 0; i < l.size(); i++) {
					logger.debug(l.get(i));
				}
			}
			return (BibliographicValidation) l.get(0);
		} else {
			/*modifica barbara 27/04/2007 prn 127 da verificare*/
			l = find("select distinct v from BibliographicValidation as v, " +
					"BibliographicCorrelation as c" +
					" where c.key.marcTagCategoryCode = ?"+
					" and c.key.marcTag = v.key.marcTag" +
					" and c.key.marcTagCategoryCode =  v.key.marcTagCategoryCode",
					new Object[] { new Short(s)},
					new Type[] { Hibernate.SHORT}
						);
				if (l.size() > 0) {
					if (logger.isDebugEnabled()) {
						logger.debug("BibliographicValidation(s) found:");
						for (int i = 0; i < l.size(); i++) {
							logger.debug(l.get(i));
						}
					}
					return (BibliographicValidation) l.get(0);
				} else {
					logger.warn("No validation found for category " + s + " and values " + values.toString());
					throw new MarcCorrelationException("no Validation found");
				}
		}
	}
}
