package org.folio.cataloging.business.cataloguing.bibliographic;

import java.util.List;

import org.folio.cataloging.business.common.DataAccessException;

public interface Equivalent {
	
	public List replaceEquivalentDescriptor(short indexingLanguage, int cataloguingView)throws DataAccessException;
}
