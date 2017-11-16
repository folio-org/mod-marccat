/*
 * (c) LibriCore
 * 
 * Created on Jun 15, 2005
 * 
 * StringTextEditBeanForItemEditing.java
 */
package librisuite.bean.cataloguing.bibliographic;

import librisuite.bean.cataloguing.bibliographic.StringTextEditBean;
import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.cataloguing.bibliographic.VariableField;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Validator;
import librisuite.business.common.ValidatorForItemEditing;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/21 13:33:35 $
 * @since 1.0
 */
public class StringTextEditBeanForItemEditing extends StringTextEditBean {

	/**
	 * Class constructor
	 *
	 * @param f
	 * @throws MarcCorrelationException
	 * @throws DataAccessException
	 * @since 1.0
	 */
	public StringTextEditBeanForItemEditing(VariableField f)
		throws MarcCorrelationException, DataAccessException {
		super(f);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see librisuite.bean.cataloguing.bibliographic.StringTextEditBean#getBibliographicValidator()
	 */
	public Validator getBibliographicValidator() {
		return new ValidatorForItemEditing();
	}

}
