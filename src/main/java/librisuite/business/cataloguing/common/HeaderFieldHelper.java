/*
 * (c) LibriCore
 * 
 * Created on Nov 4, 2004
 * 
 * HeaderFieldHelper.java
 */
package librisuite.business.cataloguing.common;

import java.io.Serializable;
import java.util.List;

import librisuite.business.codetable.DAOCodeTable;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DataAccessException;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public abstract class HeaderFieldHelper implements HeaderField, Serializable {
	private static DAOCodeTable daoCodeTable = new DAOCodeTable();

	protected short headerType = -1;

	abstract public short getCategory();

	abstract public Class getHeaderListClass();
	
	public List getFirstCorrelationList() throws DataAccessException {
		return daoCodeTable.getList(getHeaderListClass(),false);		
	}

	public List getSecondCorrelationList(short value1) throws DataAccessException {
		return null;
	}

	public List getThirdCorrelationList(short value1, short value2) throws DataAccessException {
		return null;
	}

	public short getHeaderType() {
		return headerType;
	}

	public void setHeaderType(short s) {
		headerType = s;
	}
	
	public final boolean isHeaderField() {
		return true;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.HeaderField#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return (new CorrelationValues()).change(1, getHeaderType());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.HeaderField#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		setHeaderType(v.getValue(1));
	}

}
