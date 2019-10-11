package org.folio.marccat.integration;

import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.InvalidBrowseIndexException;
import org.folio.marccat.shared.MapHeading;

import java.util.List;

public interface IStorageService {
  public List<MapHeading> getFirstPage(final String query, final int view, final int mainLibrary, final int pageSize, final String lang) throws DataAccessException, InvalidBrowseIndexException;
}
