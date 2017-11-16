/*
 * (c) LibriCore
 * 
 * Created on Nov 4, 2004
 * 
 * HeaderField.java
 */
package librisuite.business.cataloguing.common;

import java.util.List;

import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DataAccessException;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public interface HeaderField {
	public short getCategory();
	public CorrelationValues getCorrelationValues();
	public void setCorrelationValues(CorrelationValues v);
	public List getFirstCorrelationList() throws DataAccessException;
	public List getSecondCorrelationList(short value1)
		throws DataAccessException;
	public abstract List getThirdCorrelationList(short value1, short value2)
		throws DataAccessException;
	public short getHeaderType();
	public void setHeaderType(short s);
	public boolean isHeaderField();
}