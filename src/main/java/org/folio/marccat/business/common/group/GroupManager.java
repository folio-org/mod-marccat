package org.folio.marccat.business.common.group;

import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.exception.DataAccessException;

public interface GroupManager {
  TagGroup getGroup(Tag tag) throws DataAccessException;

  boolean isSameGroup(Tag tag1, Tag tag2) throws DataAccessException;

  void add(TagGroup group);
}
