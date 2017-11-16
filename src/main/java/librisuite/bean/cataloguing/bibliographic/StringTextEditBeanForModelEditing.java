/*
 * (c) LibriCore
 * 
 * Created on Jun 15, 2005
 * 
 * StringTextEditBeanForModelEditing.java
 */
package librisuite.bean.cataloguing.bibliographic;

import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.cataloguing.bibliographic.VariableField;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Validator;
import librisuite.business.common.ValidatorForModelEditing;

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
	 * @see librisuite.bean.cataloguing.bibliographic.StringTextEditBean#getBibliographicValidator()
	 */
	public Validator getBibliographicValidator() {
		return new ValidatorForModelEditing();
	}

}
