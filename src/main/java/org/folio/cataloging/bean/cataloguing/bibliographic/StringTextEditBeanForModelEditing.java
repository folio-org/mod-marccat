/*
 * (c) LibriCore
 * 
 * Created on Jun 15, 2005
 * 
 * StringTextEditBeanForModelEditing.java
 */
package org.folio.cataloging.bean.cataloguing.bibliographic;

import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableField;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Validator;
import org.folio.cataloging.business.common.ValidatorForModelEditing;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/21 13:33:35 $
 * @since 1.0
 */
public class StringTextEditBeanForModelEditing extends StringTextEditBean {

	/**
	 * Class constructor
	 *
	 * @param f
	 * @throws MarcCorrelationException
	 * @throws DataAccessException
	 * @since 1.0
	 */
	public StringTextEditBeanForModelEditing(VariableField f)
		throws MarcCorrelationException, DataAccessException {
		super(f);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see StringTextEditBean#getBibliographicValidator()
	 */
	public Validator getBibliographicValidator() {
		return new ValidatorForModelEditing();
	}

}
