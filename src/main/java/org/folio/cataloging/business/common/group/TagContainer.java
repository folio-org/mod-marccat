package org.folio.cataloging.business.common.group;

import org.folio.cataloging.business.cataloguing.common.Tag;

import java.util.Collection;
import java.util.Iterator;

public interface TagContainer extends Comparable {
  Tag getLeaderTag();

  void add(Tag tag);

  Iterator iterator();

  Collection getList();

  void sort();
}
