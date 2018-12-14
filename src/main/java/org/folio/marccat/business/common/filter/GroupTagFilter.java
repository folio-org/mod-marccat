package org.folio.marccat.business.common.filter;

import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.common.group.TagGroup;
import org.folio.marccat.exception.DataAccessException;

public class GroupTagFilter implements TagFilter {
  private TagGroup group;

  public GroupTagFilter(TagGroup group) {
    super();
    this.group = group;
  }

  public boolean accept(Tag tag, Object optionalCondition) throws DataAccessException {
    return group.contains(tag);
  }

}
