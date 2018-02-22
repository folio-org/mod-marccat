/*
 * (c) LibriCore
 * 
 * $Author: Paulm $
 * $Date: 2005/12/12 12:54:37 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.6 $
 * $Source: /source/LibriSuite/src/librisuite/bean/cataloguing/bibliographic/StringTextEditBean.java,v $
 * $State: Exp $
 */
package org.folio.cataloging.bean.cataloguing.bibliographic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableField;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Validator;
import org.folio.cataloging.dao.DAOBibliographicValidation;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class comment
 * @author janick
 */
public abstract class StringTextEditBean {
	private static Log logger = LogFactory.getLog(StringTextEditBean.class);
	
	private Validator validator =
		getBibliographicValidator();
	private Map codeTableLists = new HashMap();

	private VariableField field = null;

	private static final DAOBibliographicValidation daoValidation =
		new DAOBibliographicValidation();
	private StringText stringText = null;
	private StringText accessPointStringText = null;
	private String accessPointDescriptorStringText = null;
	private short categoryCode = 0;
	private CorrelationValues correlationValues = null;

	private Set remainingValidSubfields;
	private List validSubfieldList;
	private String defaultSubfieldCode = "a";
	private StringText editableSubfields;
	private StringText fixedSubfields;

    public abstract Validator getBibliographicValidator();
    
	public StringTextEditBean(VariableField f)
		throws DataAccessException {
		logger.debug("StringTextEditBean"+f.getStringText());
		setField(f);
	}

	private void populateSubfieldLists()
		throws DataAccessException {
			logger.debug("PopulatingSubfieldLists in StringTextEditBean");
		if (field != null) {
			remainingValidSubfields =
				validator.computeRemainingValidSubfields(field);
			validSubfieldList = validator.computeValidSubfieldList(field);
			editableSubfields = validator.getEditableSubfields(field);
			fixedSubfields = validator.getFixedSubfields(field);
		}
	}

	/**
	 * @return
	 */
	public Map getCodeTableLists() {
		return codeTableLists;
	}

	/**
	 * @return
	 */
	public Set getRemainingValidSubfields() {
		return remainingValidSubfields;
	}

	/**
	 * @return
	 */
	public StringText getEditableSubfields() {
		return editableSubfields;
	}

	/**
	 * @return
	 */
	public StringText getFixedSubfields() {
		return fixedSubfields;
	}

	/**
	 * @return
	 */
	public List getValidSubfieldList() {
		return validSubfieldList;
	}

	/**
	 * @param map
	 */
	public void setCodeTableLists(Map map) {
		codeTableLists = map;
	}

	/**
	 * @param set
	 */
	public void setRemainingValidSubfields(Set set) {
		remainingValidSubfields = set;
	}

	/**
	 * @param text
	 */
	private void setEditableSubfields(StringText text) {
		//nothing
	}

	/**
	 * @param text
	 */
	private void setFixedSubfields(StringText text) {
		//nothing
	}

	/**
	 * @param list
	 */
	public void setValidSubfieldList(List list) {
		validSubfieldList = list;
	}

	/**
	 * @return
	 */
	private VariableField getField() {
		return field;
	}

	/**
	 * @param field
	 */
	private void setField(VariableField field)
		throws DataAccessException {
		this.field = field;
		populateSubfieldLists();
	}

	/**
	 * @return
	 */
	public String getDefaultSubfieldCode() {
		return defaultSubfieldCode;
	}

	/**
	 * @param string
	 */
	public void setDefaultSubfieldCode(String string) {
		defaultSubfieldCode = string;
	}

	/**
	 * Used in model editing since all subfields are "editable" in a model
	 * 
	 * @since 1.0
	 */
	public StringText getAllSubfields() {
		StringText result = new StringText();
		result.add(getFixedSubfields());
		result.add(getEditableSubfields());
		return result;
	}

	/**
	 * @return
	 */
	//	public boolean[] getEditable() {
	//		return editable;
	//	}

	/**
	 * @param bs
	 */
	//	public void setEditable(boolean[] bs) {
	//		editable = bs;
	//	}

	/**
	 * @return
	 */
	//	public boolean getEditable(int i) {
	//		return editable[i];
	//	}

	/**
	 * @param bs
	 */
	//	public void setEditable(int i, boolean b) {
	//		editable[i] = b;
	//	}	
}
