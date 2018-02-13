package org.folio.cataloging.business.cataloguing.bibliographic;

import java.util.List;

import org.folio.cataloging.business.common.DataAccessException;

public interface Equivalent {
	List replaceEquivalentDescriptor(short indexingLanguage, int cataloguingView)throws DataAccessException;
}
