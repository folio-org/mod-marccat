/*
 * (c) LibriCore
 * 
 * Created on 03-ago-2004
 * 
 * DAOBibMarcTagValidation.java
 */
package librisuite.business.common;

import java.util.List;

import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.cataloguing.common.Validation;
import librisuite.hibernate.BibliographicValidation;
import librisuite.hibernate.BibliographicValidationKey;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author elena
 * @version $Revision: 1.7 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class DAOMadesValidation extends DAOValidation {
	
	private static final Log logger = LogFactory.getLog(DAOMadesValidation.class);

	// TODO _MIKE change in MadesValidation?
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
		List l = find("select distinct v from librisuite.hibernate.BibliographicValidation as v, " +
			"librisuite.hibernate.BibliographicCorrelation as c" +
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
			throw new MarcCorrelationException("no Validation found");
		}
	}
}
