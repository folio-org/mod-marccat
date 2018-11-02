package org.folio.marccat.business.cataloguing.bibliographic;

import org.folio.marccat.business.common.DataAccessException;

import java.util.List;

public interface Equivalent {
  List replaceEquivalentDescriptor(short indexingLanguage, int cataloguingView) throws DataAccessException;
}
