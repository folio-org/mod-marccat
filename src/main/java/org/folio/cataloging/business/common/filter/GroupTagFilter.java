package org.folio.cataloging.business.common.filter;

import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.group.TagGroup;

public class GroupTagFilter implements TagFilter {
  private TagGroup group;

  public GroupTagFilter(TagGroup group) {
    super ( );
    this.group = group;
  }

  public boolean accept(Tag tag, Object optionalCondition) throws MarcCorrelationException, DataAccessException {
    return group.contains (tag);
  }

}
