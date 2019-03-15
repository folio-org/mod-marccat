package org.folio.marccat.business.common.group;

import org.folio.marccat.business.cataloguing.common.Tag;

public interface GroupManager {
  TagGroup getGroup(Tag tag);

  boolean isSameGroup(Tag tag1, Tag tag2);

  void add(TagGroup group);
}
