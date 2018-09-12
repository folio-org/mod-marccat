package org.folio.cataloging.business.marchelper;

import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableField;
import org.folio.cataloging.business.cataloguing.common.AccessPoint;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.Descriptor;
import org.folio.cataloging.dao.persistence.CorrelationKey;

import org.folio.cataloging.util.StringText;

/**
 * Marker for AccessPoints used by MarcHelper.
 * 
 * @author michelem
 * @since 1.0
 *
 */
public interface MarcHelperTag {
	
	/**
	 * Returns the AccessPoint's variant codes.
	 *
	 * @return the AccessPoint's variant codes.
	 */
	String getVariantCodes();
	
	/**
	 * @see {@link AccessPoint#getDescriptor()}
	 * @return
	 */
	Descriptor getDescriptor();
	
	/**
	 * @see {@link VariableField#getStringText()}
	 * @return
	 */
	StringText getStringText();
	
	/**
	 * @see {@link Tag#getMarcEncoding()}
	 * @return
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 */
	CorrelationKey getMarcEncoding() throws DataAccessException, MarcCorrelationException;
	
	/**
	 * @see {@link Tag#getMarcEncoding()}
	 * @return
	 * @throws DataAccessException
	 * @throws MarcCorrelationException
	 */
	String getKey()throws DataAccessException, MarcCorrelationException;
}
