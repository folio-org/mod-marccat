package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.business.common.DataAccessException;

import java.util.List;

public interface Equivalent {
  List replaceEquivalentDescriptor(short indexingLanguage, int cataloguingView) throws DataAccessException;
}
