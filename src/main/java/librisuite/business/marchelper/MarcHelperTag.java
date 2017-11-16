package librisuite.business.marchelper;

import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.cataloguing.bibliographic.VariableField;
import librisuite.business.cataloguing.common.AccessPoint;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.common.DataAccessException;
import librisuite.business.descriptor.Descriptor;
import librisuite.hibernate.CorrelationKey;

import com.libricore.librisuite.common.StringText;

/**
 * Marker for AccessPoints used by MarcHelper
 * 
 * @author michelem
 *
 */
public interface MarcHelperTag {
	
	/**
	 * @return the AccessPoint's variant codes
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
