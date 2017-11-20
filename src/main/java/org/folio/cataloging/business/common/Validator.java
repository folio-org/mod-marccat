/*
 * (c) LibriCore
 * 
 * $Author: Paulm $
 * $Date: 2005/12/01 13:50:05 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.2 $
 * $Source: /source/LibriSuite/src/librisuite/business/common/Validator.java,v $
 * $State: Exp $
 */
package org.folio.cataloging.business.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.folio.cataloging.bean.cataloguing.copy.CopyBean;
import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.bibliographic.VariableField;
import org.folio.cataloging.business.cataloguing.common.Validation;

import org.folio.cataloging.util.StringText;


/**
 * Class comment
 * @author janick
 */
public abstract class Validator {

	public abstract StringText getEditableSubfields(VariableField field);
	public abstract StringText getFixedSubfields(VariableField f);

	private static final Log logger = LogFactory.getLog(CopyBean.class);
	
	public List computeValidSubfieldList(VariableField f) throws MarcCorrelationException, DataAccessException {
		return computeValidSubfieldList(
			getEditableSubfields(f),
			computeRemainingValidSubfields(f));
	}

	public Set computeRemainingValidSubfields(VariableField f) throws MarcCorrelationException, DataAccessException {
		Set remaining =
			computeRemainingValidSubfields(
				f.getStringText(),
				f.getValidation());
		return remaining;
	}

	public Set computeRemainingValidSubfields(StringText stringText, Validation v) {
		logger.debug("computeRemainingValidSubfields   stringText== "+stringText );
	
		Set remainingCodes = new TreeSet(new SubfieldCodeComparator());
		remainingCodes.addAll(v.getValidSubfieldCodes());
		remainingCodes.removeAll(stringText.getUsedSubfieldCodes());
		remainingCodes.addAll(v.getRepeatableSubfieldCodes());
		return remainingCodes;
	}

	public List computeValidSubfieldList(StringText stringText, Set remainingCodes) {
		logger.debug("computeValidSubfieldList   stringText== "+stringText +"   remainingCodes == "+remainingCodes);
		List validSubfieldList = new ArrayList();
		for (int i = 0; i < stringText.getNumberOfSubfields(); i++) {
			Set validCodes = new TreeSet(new SubfieldCodeComparator());
			validCodes.addAll(remainingCodes);
			validCodes.add(stringText.getSubfield(i).getCode());
			validSubfieldList.add(validCodes);
		}
		return validSubfieldList;
	}

}
