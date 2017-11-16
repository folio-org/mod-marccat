/*
 * (c) LibriCore
 * 
 * Created on Nov 4, 2005
 * 
 * TagImpl.java
 */
package librisuite.business.cataloguing.common;

import java.io.Serializable;
import java.util.Set;

import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.common.DataAccessException;
import librisuite.hibernate.Correlation;
import librisuite.hibernate.CorrelationKey;

/**
 * This class is the Abstract Implementation of the Tag interface
 * Subclasses are intended to be different schemes/formats of tags 
 * (e.g. MARC21 BIB, MARC21 AUT)
 * 
 * An instance of this class is composed within the Tag class so that
 * format specific differences can be bridged through the common Tag interface
 * 
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public abstract class TagImpl implements Serializable {

	abstract public String getHeadingType(Tag t);

	abstract public Catalog getCatalog();
	
	/**
	* @return the MARC tag and indicators for this tag
	*/
	abstract public CorrelationKey getMarcEncoding(Tag t)
		throws DataAccessException, MarcCorrelationException;

	abstract public Validation getValidation(Tag t)
		throws MarcCorrelationException, DataAccessException;

	/**
	 * return the list of subfields that can be edited on the worksheet
	 * 
	 * @since 1.0
	 */
	abstract public Set getValidEditableSubfields(short category);

	abstract public Correlation getCorrelation(String tagNumber, char indicator1,
			char indicator2, short category) throws DataAccessException;
}
