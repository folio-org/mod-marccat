/*
 * (c) LibriCore
 * 
 * Created on Nov 4, 2004
 * 
 * HeaderField.java
 */
package org.folio.cataloging.business.cataloguing.common;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.shared.CorrelationValues;

import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public interface HeaderField {
	short getCategory();
	CorrelationValues getCorrelationValues();
	void setCorrelationValues(CorrelationValues v);
	List getFirstCorrelationList() throws DataAccessException;
	List getSecondCorrelationList(short value1)
		throws DataAccessException;
	List getThirdCorrelationList(short value1, short value2)
		throws DataAccessException;
	short getHeaderType();
	void setHeaderType(short s);
	boolean isHeaderField();
}