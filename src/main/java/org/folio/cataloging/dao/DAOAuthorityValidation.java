/*
 * (c) LibriCore
 * 
 * Created on Nov 18, 2005
 * 
 * DAOAuthorityValidation.java
 */
package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.shared.Validation;

import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class DAOAuthorityValidation extends DAOValidation {

	private static final Log logger = LogFactory.getLog(DAOAuthorityValidation.class);

	public Validation load(String tag, String headingType, short category)
		throws DataAccessException {
			List l = find("from AuthorityValidation as v, " +
				" where v.key.marcTagCategoryCode = ? " +
				" and v.key.marcTag = ? " +
				" and v.key.headingType = ? ",
				new Object[] { new Short(category), 
					tag,
					headingType},
				new Type[] { Hibernate.SHORT,
					Hibernate.STRING,
					Hibernate.STRING}
					);
			if (l.size() == 1) {
				if (logger.isDebugEnabled()) {
					logger.debug("AuthorityValidation(s) found:");
					for (int i = 0; i < l.size(); i++) {
						logger.debug(l.get(i));
					}
				}
				return (Validation) l.get(0);
			} else {
				throw new MarcCorrelationException("no Validation found");
			}
		}

}
