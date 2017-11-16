package librisuite.business.cataloguing.bibliographic;

import java.util.List;

import librisuite.business.common.DataAccessException;

public interface Equivalent {
	
	public List replaceEquivalentDescriptor(short indexingLanguage, int cataloguingView)throws DataAccessException;
}
